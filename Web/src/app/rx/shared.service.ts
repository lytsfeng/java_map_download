import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable()
export class SharedService {

	public subjects = new Map<SharedType, Subject<any>>(); // 实例
	private persistentQueue = new Map<SharedType, Array<any>>(); // 持久化队列

	// 示例：
	// 不进行缓存，直接发送，不论有没有sub
	// this.sharedService.pub(SharedType.SHARED_TEST, '123123123');
	// 进行缓存，若没有sub则等待任意一个sub建立后再发送
	// this.sharedService.pub(SharedType.SHARED_TEST, '123123123', { persistent: true });

	/* 订阅 */
	sub(topic: SharedType): Subject<any> {
		let subject = this.subjects.get(topic);
		if (subject == null) {
			subject = new Subject<any>();
			this.subjects.set(topic, subject);
		}
		let queue = this.persistentQueue.get(topic);
		if (queue != null) {
			new Observable<boolean>((observer) => { // 异步发送已持久化的消息
				Promise.resolve().then(() => {
					observer.next(true);
				})
			}).subscribe(() => {
				if (queue != null) {
					for (let i = 0; i < queue.length; i++) {
						this.subjects.get(topic)?.next(queue[i]);
					}
				}
				this.persistentQueue.delete(topic);
			})
		}
		return subject;
	}

	/* 发布 */
	pub(topic: SharedType, msg: any, opt?: { persistent?: boolean }): void {
		if (opt && opt.persistent) { // 消息持久化
			if (this.subjects.get(topic) == null) {
				if (this.persistentQueue.get(topic) == null) {
					this.persistentQueue.set(topic, []);
				}
				this.persistentQueue.get(topic)?.push(msg);
			} else {
				this.subjects.get(topic)?.next(msg);
			}
		} else { // 消息不持久化
			this.subjects.get(topic)?.next(msg);
		}
	}

	/* 销毁 */
	destroy(topic: SharedType): void {
		if (this.subjects.get(topic) != null) {
			this.subjects.delete(topic);
		}
		if (this.persistentQueue.get(topic) != null) {
			this.persistentQueue.delete(topic);
		}
	}

	/* 销毁 */
	destroys(topics: Array<SharedType>): void {
		for (let i = 0; i < topics.length; i++) {
			this.destroy(topics[i]);
		}
	}

	/* 清空 */
	destroyAll(): void {
		this.subjects.clear();
		this.persistentQueue.clear();
	}

}

export enum SharedType {

}
