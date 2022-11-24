import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable()
export class InnerMqService {

	private clients = new Map<string, InnerMqClient>(); // 客户端
	private persistentQueue = new Map<any, Array<{ type: PersistentType, data: any }>>(); // 持久化队列

	constructor() {
	}

	/* 建立连接 */
	public createConnect(clientId: string): InnerMqClient {
		let client = this.clients.get(clientId);
		if (client == null) {
			client = new InnerMqClientImpl({
				subscribe: (t, s) => {
					this.clientSubscribeCallback(t, s);
				}
			});
			this.clients.set(clientId, client);
		}
		return client;
	}

	/* 取消连接 */
	public destroyClient(clientId: string): void {
		let client = this.clients.get(clientId);
		if (client != null) {
			client.destroy();
		}
		this.clients.delete(clientId);
	}

	/* 发布 */
	public pub(topic: string, msg: any, opt?: { persistent: boolean, type: PersistentType }): void {
		let published = false;
		for (let client of this.clients.values()) {
			published = client.pub(topic, msg);
		}
		// 消息未发送，进行持久化存储
		if (published == false && (opt && opt.persistent)) {
			if (this.persistentQueue.get(topic) == null) {
				this.persistentQueue.set(topic, []);
			}
			this.persistentQueue.get(topic)?.push({ type: opt.type, data: msg });
		}
	}

	/* 客户端订阅回调 */
	private clientSubscribeCallback(topic: string, subject: Subject<any>): void {
		this.processPersistentQueue(topic, subject);
	}

	/* 处理持久化消息 */
	private processPersistentQueue(topic: string, subject: Subject<any>): void {
		let queue = this.persistentQueue.get(topic);
		if (queue == null) {
			return;
		}
		new Observable<boolean>((observer) => { // 异步发送已持久化的消息
			Promise.resolve().then(() => {
				observer.next(true);
			})
		}).subscribe(() => {
			if (queue == null) {
				return;
			}
			for (let i = 0; i < queue.length; i++) {
				switch (queue[i].type) {
					case PersistentType.ON_ONCE_SUB:
						subject.next(queue[i].data);
						queue.splice(i, 1); // 将使后面的元素依次前移，数组长度减1
						i--; // 如果不减，将漏掉一个元素
						break;
					case PersistentType.ON_EVERY_CLIENT_EVERY_SUB:
						subject.next(queue[i].data);
						break;
					default:
						break;
				}
			}
			if (queue.length == 0) {
				this.persistentQueue.delete(topic);
			}
		})
	}

}

export interface InnerMqClient {

	sub(topic: string): Observable<any>;

	pub(topic: string, msg: any): boolean;

	destroy(): void;

}

class InnerMqClientImpl implements InnerMqClient {

	private subjects = new Map<string, Subject<any>>(); // 实例
	private destroyed: boolean = false;

	constructor(
		private callback: {
			subscribe: (topic: string, subject: Subject<any>) => void
		}
	) {
	}

	/* 订阅 */
	public sub(topic: string): Observable<any> {
		let subject = this.subjects.get(topic);
		if (subject == null) {
			subject = new Subject<any>();
			this.subjects.set(topic, subject);
		}
		this.callback.subscribe(topic, subject);
		return subject.asObservable();
	}

	/* 发布 */
	public pub(topic: string, msg: any): boolean {
		if (this.destroyed) {
			return false;
		}
		let subject = this.subjects.get(topic);
		if (subject != null && !subject.closed) {
			subject.next(msg);
			return true;
		} else {
			return false;
		}
	}

	/* 销毁 */
	public destroy(): void {
		this.destroyed = true;
		for (let subject of this.subjects.values()) {
			subject.unsubscribe();
		}
		this.subjects.clear();
	}

}

export enum PersistentType {
	ON_ONCE_SUB, // 只进行一次缓存，一次sub后即删除
	ON_EVERY_CLIENT_EVERY_SUB, // 持久化，对每个客户端的每一次该TOPIC的sub都发送信息
}
