import { OsmSource } from './source/osm-source';
import { TianSource } from "./source/tian-source";
import { GoogleSource } from './source/google-source';
import { AmapSource } from './source/amap-source';
import { TencentSource } from './source/tencent-source';
import { BingSource } from './source/bing-source';
import { MapBoxSource } from './source/mapbox-source';
import { BaiduSource } from './source/baidu-source';

export class MapSource {

	public readonly layers = new Map();

	constructor(ol: any) {
		/** -----OpenStreetMap----- */
		let osm = new OsmSource(ol);
		this.layers.set(osm.OpenStreetMap_Name, osm.OpenStreetMap_Source);
		/** -----天地图----- */
		let tian = new TianSource(ol);
		this.layers.set(tian.TiandituNormalNone_Name, tian.TiandituNormalNone_Source);
		this.layers.set(tian.TiandituTerrainNone_Name, tian.TiandituTerrainNone_Source);
		this.layers.set(tian.TiandituLine_Name, tian.TiandituLine_Source);
		this.layers.set(tian.TiandituTip_Name, tian.TiandituTip_Source);
		/** -----谷歌地图----- */
		let google = new GoogleSource(ol);
		this.layers.set(google.GoogleNormal_Name, google.GoogleNormal_Source);
		this.layers.set(google.GoogleTerrain_Name, google.GoogleTerrain_Source);
		this.layers.set(google.GoogleSatellite_Name, google.GoogleSatellite_Source);
		this.layers.set(google.GoogleSatelliteNone_Name, google.GoogleSatelliteNone_Source);
		this.layers.set(google.GoogleStreet_Name, google.GoogleStreet_Source);
		/** -----高德地图----- */
		let amap = new AmapSource(ol);
		this.layers.set(amap.AMapNormal_Name, amap.AMapNormal_Source);
		this.layers.set(amap.AMapNormalNone_Name, amap.AMapNormalNone_Source);
		this.layers.set(amap.AMapSatelliteNone_Name, amap.AMapSatelliteNone_Source);
		this.layers.set(amap.AMapStreet_Name, amap.AMapStreet_Source);
		this.layers.set(amap.AMapStreetNone_Name, amap.AMapStreetNone_Source);
		/** -----腾讯地图----- */
		let tencent = new TencentSource(ol);
		this.layers.set(tencent.TencentNormal_Name, tencent.TencentNormal_Source);
		/** -----必应地图----- */
		let bing = new BingSource(ol);
		this.layers.set(bing.BingMapNormal1_Name, bing.BingMapNormal1_Source);
		this.layers.set(bing.BingMapNormal1CN_Name, bing.BingMapNormal1CN_Source);
		this.layers.set(bing.BingMapNormal1None_Name, bing.BingMapNormal1None_Source);
		this.layers.set(bing.BingMapNormal2_Name, bing.BingMapNormal2_Source);
		this.layers.set(bing.BingMapNormal2CN_Name, bing.BingMapNormal2CN_Source);
		this.layers.set(bing.BingMapNormal2None_Name, bing.BingMapNormal2None_Source)
		this.layers.set(bing.BingMapSatelliteNone_Name, bing.BingMapSatelliteNone_Source);
		/** -----MapBox----- */
		let mapbox = new MapBoxSource(ol);
		this.layers.set(mapbox.MapBoxPDF_Name, mapbox.MapBoxPDF_Source);
		/** -----百度地图----- */
		let baidu = new BaiduSource(ol);
		this.layers.set(baidu.BaiduNormal_Name, baidu.BaiduNormal_Source);

	}


}
