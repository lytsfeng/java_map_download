import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InnerMqService } from 'src/app/rx/inner-mq.service';
import { MapBase } from 'src/app/map/map-base';
import { SubmitService } from 'src/app/service/submit.service';
import { CommonUtil } from 'src/app/util/common-util';
import { MapMessageProcessor } from '../../../connection/onmassage/map-message.processor';

@Component({
	selector: 'app-map',
	templateUrl: './map.page.html',
	styleUrls: ['./map.page.less'],
	providers: [SubmitService],
})
export class MapPage implements OnInit, OnDestroy {

	@ViewChild('map', { static: true }) mapEleRef: ElementRef<HTMLDivElement> | undefined;
	@ViewChild('mapTypeDiv', { static: true }) mapTypeDiv: ElementRef<HTMLElement> | undefined;
	@ViewChild('coordinateTypeDiv', { static: true }) coordinateTypeDiv: ElementRef<HTMLElement> | undefined;
	@ViewChild('changeKeyDiv', { static: true }) changeKeyDiv: ElementRef<HTMLDivElement> | undefined;

	validateForm!: FormGroup;

	mapBase!: MapBase;
	zoom: number = 0;
	keyType: string = '';
	isChangeKeyModalVisible: boolean = false;

	constructor(
		private fb: FormBuilder,
		private innerMqService: InnerMqService,
		private submitService: SubmitService,
	) {
	}

	ngOnInit(): void {
		let mqClient = this.innerMqService.createConnect('MapPage');
		this.validateForm = this.fb.group({
			key: [null, [Validators.required]],
		});
		if (this.mapEleRef != null) {
			let config = CommonUtil.getConfigCache();
			this.mapBase = new MapBase({
				dom: this.mapEleRef.nativeElement,
				option: config
			}, {
				onFinish: () => {
					new MapMessageProcessor(mqClient, this.submitService, this.mapBase, this);
					this.mapInitFinish();
				},
				onResize: (e: HTMLDivElement) => {
					this.mapAutoSize(e);
				}
			});
		}
	}

	ngOnDestroy(): void {
		this.innerMqService.destroyClient('MapPage');
	}

	mapInitFinish(): void {
		this.mapBase.getOlMap().on('moveend', (e: any) => {
			let z = e.map.getView().getZoom();
			if (z != null) {
				this.zoom = Math.round(z);
			}
		})
		// this.javaSharedListener.addSubMapSharedNoMap('VConsoleOpenOrClose');
		// this.javaSharedListener.addSubMapShared('ZoomIn', this.mapBase);
		// this.javaSharedListener.addSubMapShared('ZoomOut', this.mapBase);
		// this.javaSharedListener.addSubMapShared('Pan', this.mapBase);
		// this.javaSharedListener.addSubMapShared('GridSwitch', this.mapBase);
		// this.javaSharedListener.addSubMapShared('switchResource', this.mapBase, () => {
		// 	this.updateShowInfo()
		// });
		// this.javaSharedListener.addSubMapShared('switchDrawType', this.mapBase);
		// this.javaSharedListener.addSubMapShared('openDraw', this.mapBase);
		// this.javaSharedListener.addSubMapShared('drawPolygonAndPositioning', this.mapBase);
		// this.javaSharedListener.addSubMapShared('fitview', this.mapBase);
		// this.javaSharedListener.addSubMapShared('removeDrawedShape', this.mapBase);
		// this.sharedService.sub('SubmitBlockDownload').subscribe((res: string) => {
		// 	let data = {
		// 		tileName: this.mapBase?.getCurrentXyzName(),
		// 		mapType: CommonUtil.getMapType(this.mapBase?.getCurrentXyzName()),
		// 		tileUrl: this.mapBase?.getCurrentXyzUrlResources(),
		// 		points: this.mapBase?.getDrawedPoints(),
		// 	};
		// 	this.submitService.blockDownload(data).subscribe((r) => {
		// 	})
		// })
		// this.sharedService.sub('SubmitWorldDownload').subscribe((res: string) => {
		// 	let data = {
		// 		tileName: this.mapBase?.getCurrentXyzName(),
		// 		mapType: CommonUtil.getMapType(this.mapBase?.getCurrentXyzName()),
		// 		tileUrl: this.mapBase?.getCurrentXyzUrlResources()
		// 	};
		// 	this.submitService.worldDownload(data).subscribe((r) => {
		// 	})
		// });
		this.updateShowInfo();
	}

	updateShowInfo(): void {
		if (this.mapBase == null ||
			this.changeKeyDiv == null ||
			this.mapTypeDiv == null ||
			this.coordinateTypeDiv == null) {
			return;
		}
		this.keyType = CommonUtil.needKey(this.mapBase.getCurrentXyzName()).type;
		let canChangeKey = CommonUtil.needKey(this.mapBase.getCurrentXyzName()).has;
		let currentXyzName = CommonUtil.getMapType(this.mapBase.getCurrentXyzName());
		let coordinateType = this.mapBase.getCurrentCoordinateType();
		if (canChangeKey) {
			this.changeKeyDiv.nativeElement.style.display = 'block';
		} else {
			this.changeKeyDiv.nativeElement.style.display = 'none';
		}
		this.mapTypeDiv.nativeElement.innerText = currentXyzName;
		this.coordinateTypeDiv.nativeElement.innerText = '坐标类型：' + coordinateType;
	}

	openChangeKeyModal(): void {
		if (this.keyType == 'tian') {
			this.validateForm.controls['key'].setValue(CommonUtil.getConfigCache().key[this.keyType])
			this.isChangeKeyModalVisible = true;
		}
	}

	closeChangeKeyModal(): void {
		this.isChangeKeyModalVisible = false;
	}

	keyFormValid(): boolean {
		for (const i in this.validateForm.controls) {
			this.validateForm.controls[i].markAsDirty();
			this.validateForm.controls[i].updateValueAndValidity();
		}
		return this.validateForm.valid;
	}

	changeKey(): void {
		if (this.keyType == 'tian') {
			if (this.keyFormValid()) {
				let key = this.validateForm.controls['key'].value;
				let config = CommonUtil.getConfigCache();
				config.key[this.keyType] = key;
				CommonUtil.setConfigCache(config);
				this.reload();
			}
		}
	}

	reload(): void {
		window.location.reload();
	}

	private mapAutoSize(e: HTMLDivElement): void {
		let width = e.offsetWidth;
		let height = e.offsetHeight;
		this.mapBase?.getOlMap()?.setSize([width, height]);
	}

}
