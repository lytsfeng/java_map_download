import { InnerMqClient } from '../../rx/inner-mq.service';
import { SubmitService } from '../../service/submit.service';
import { MapBase } from '../../map/map-base';
import { CommonUtil } from '../../util/common-util';
import { MapPage } from '../../view/page/map/map.page';
import { MapDraw } from '../../map/draw/map-draw';
import { MapWrap } from '../../map/draw/map-wrap';
import { GeoUtil } from "../../map/geo-util";
import { Point } from "../../map/entity/Point";

export class MapMessageProcessor {

	constructor(
		private mqClient: InnerMqClient,
		private submitService: SubmitService,
		private mapBase: MapBase,
		private mapPage: MapPage,
	) {
		/** 放大 */
		mqClient.sub('ZoomIn').subscribe((res) => {
			mapBase.zoomIn();
		});
		/** 缩小 */
		mqClient.sub('ZoomOut').subscribe((res) => {
			mapBase.zoomOut();
		});
		/** 拖动 */
		mqClient.sub('Pan').subscribe((res) => {
			mapBase.pan();
		});
		/** 显示网格 */
		mqClient.sub('GridSwitch').subscribe((res) => {
			let update;
			if (mapBase.getGridVisible()) {
				mapBase.closeGrid();
				update = false;
			} else {
				mapBase.showGrid();
				update = true;
			}
			let config = mapBase.getMapConfig();
			if (config) {
				config.grid = update;
				CommonUtil.setConfigCache(config);
				mapBase.setMapConfig(config);
			}
		});
		/** 切换图层源 */
		mqClient.sub('SwitchResource').subscribe((res) => {
			// 切换图层
			let lastType = mapBase.getCurrentCoordinateType();
			mapBase.switchMapResource(res);
			let currentType = mapBase.getCurrentCoordinateType();
			// 保存设置
			let config = mapBase.getMapConfig();
			if (config) {
				config.layer = res;
				CommonUtil.setConfigCache(config);
				mapBase.setMapConfig(config);
			}
			// 检查坐标类型
			if (lastType != currentType) {
				if (lastType == 'wgs84' && currentType == 'gcj02') {
					mapBase.turnMapFeaturesFromWgs84ToGcj02();
				} else if (lastType == 'gcj02' && currentType == 'wgs84') {
					mapBase.turnMapFeaturesFromGcj02ToWgs84();
				}
			}
			// 回调
			setTimeout(() => {
				mapPage.updateShowInfo();
			});
		});
		/** 绘制类型切换 - */
		mqClient.sub('SwitchDrawType').subscribe((res) => {
			mapBase.setDrawType(res);
		});
		/** 绘制 - */
		mqClient.sub('OpenDraw').subscribe((res) => {
			mapBase.pan();
			mapBase.removeDrawedFeatures();
			mapBase.openDraw({
				drawEnd: () => {
					setTimeout(() => {
						mapBase.removeDrawInteraction();
					})
				},
				modifyEnd: () => {
				}
			});
		});
		/** 绘制指定多边形并定位 - */
		mqClient.sub('DrawPolygonAndPositioning').subscribe((res) => {
			mapBase.pan();
			mapBase.removeDrawedFeatures();
			let blocks = JSON.parse(res);
			for (let i = 0; i < blocks.length; i++) {
				let points: Array<Point> = [];
				for (let j = 0; j < blocks[i].length; j++) {
					let point = new Point(blocks[i][j].lng, blocks[i][j].lat);
					if (mapBase.getCurrentCoordinateType() == 'wgs84') {
						points.push(GeoUtil.gcj02_To_wgs84(point));
					} else {
						points.push(point);
					}
				}
				let feature = MapDraw.createPolygonFeature(points);
				MapWrap.addFeature(mapBase, mapBase.drawLayerName, feature);
			}
			mapBase.setFitviewFromDrawLayer();
		});
		/** fitview - */
		mqClient.sub('Fitview').subscribe((res) => {
			mapBase.setFitviewFromDrawLayer();
		});
		/** 删除绘制 - */
		mqClient.sub('RemoveDrawedShape').subscribe((res) => {
			mapBase.removeDrawedFeatures();
		});
		/** 提交区块下载 - */
		mqClient.sub('SubmitBlockDownload').subscribe((res) => {
			let data = {
				tileName: this.mapBase?.getCurrentXyzName(),
				mapType: CommonUtil.getMapType(this.mapBase?.getCurrentXyzName()),
				tileUrl: this.mapBase?.getCurrentXyzUrlResources(),
				points: this.mapBase?.getDrawedPoints(),
			};
			this.submitService.blockDownload(data).then((r) => {
			});
		});
		/** 提交世界下载 - */
		mqClient.sub('SubmitWorldDownload').subscribe((res) => {
			let data = {
				tileName: this.mapBase?.getCurrentXyzName(),
				mapType: CommonUtil.getMapType(this.mapBase?.getCurrentXyzName()),
				tileUrl: this.mapBase?.getCurrentXyzUrlResources()
			};
			this.submitService.worldDownload(data).then((r) => {
			});
		});
	}

}
