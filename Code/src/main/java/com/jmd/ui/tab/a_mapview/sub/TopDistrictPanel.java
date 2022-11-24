package com.jmd.ui.tab.a_mapview.sub;

import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jmd.browser.BrowserEngine;
import com.jmd.common.StaticVar;
import com.jmd.db.service.AllDistrictService;
import com.jmd.entity.district.Area;
import com.jmd.entity.district.City;
import com.jmd.entity.district.District;
import com.jmd.entity.district.Province;
import com.jmd.entity.geo.LngLatPoint;
import com.jmd.util.GeoUtils;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

@Component
public class TopDistrictPanel extends JPanel {

	@Autowired
	private BrowserEngine browserEngine;
	@Autowired
	private AllDistrictService allDistrictService;

	private JComboBox<String> provinceComboBox;
	private JComboBox<String> cityComboBox;
	private JComboBox<String> districtComboBox;

	private HashMap<String, String> provinceAdcodeMap = new HashMap<>();
	private HashMap<String, String> cityAdcodeMap = new HashMap<>();
	private HashMap<String, String> districtAdcodeMap = new HashMap<>();

//	public TopDistrictPanel() {
//		init();
//	}

	@PostConstruct
	private void init() {

		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		this.setBorder(new TitledBorder(null, "快速选择", TitledBorder.LEADING, TitledBorder.TOP,
				StaticVar.FONT_SourceHanSansCNNormal_12, null));

		provinceComboBox = new JComboBox<String>();
		provinceComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		provinceComboBox.setFocusable(false);
		provinceComboBox.addItemListener((e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				this.getCitysByProvinceAdcode(this.provinceAdcodeMap.get(e.getItem()));
			}
		});

		cityComboBox = new JComboBox<String>();
		cityComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		cityComboBox.setFocusable(false);
		cityComboBox.addItemListener((e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				this.getDistrictsByCityAdcode(this.cityAdcodeMap.get(e.getItem()));
			}
		});

		districtComboBox = new JComboBox<String>();
		districtComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		districtComboBox.setFocusable(false);

		JButton okButton = new JButton("确定");
		okButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		okButton.setFocusable(false);
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1) {
					String adcode = null;
					if (districtComboBox.getSelectedItem() != null) {
						String name = (String) districtComboBox.getSelectedItem();
						adcode = districtAdcodeMap.get(name);
					}
					if (adcode == null && cityComboBox.getSelectedItem() != null) {
						String name = (String) cityComboBox.getSelectedItem();
						adcode = cityAdcodeMap.get(name);
					}
					if (adcode == null && provinceComboBox.getSelectedItem() != null) {
						String name = (String) provinceComboBox.getSelectedItem();
						adcode = provinceAdcodeMap.get(name);
					}
					getPolygonByAdcode(adcode);
				}
			}
		});

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(okButton)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(provinceComboBox, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cityComboBox, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(districtComboBox, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(6, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(provinceComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cityComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(districtComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(okButton)
					.addContainerGap(6, Short.MAX_VALUE))
		);
		this.setLayout(groupLayout);

		this.getAllProvinces();

	}

	private void getAllProvinces() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				provinceAdcodeMap.clear();
				provinceComboBox.removeAllItems();
				List<Province> provinces = allDistrictService.getAllProvinces();
				String provinceItems[] = new String[provinces.size()];
				for (int i = 0; i < provinces.size(); i++) {
					provinceItems[i] = provinces.get(i).getProvince();
					provinceAdcodeMap.put(provinces.get(i).getProvince(), provinces.get(i).getAdcode());
				}
				provinceComboBox.setModel(new DefaultComboBoxModel<String>(provinceItems));
				return null;
			}
		};
		worker.execute();
	}

	private void getCitysByProvinceAdcode(String adcode) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				cityAdcodeMap.clear();
				cityComboBox.removeAllItems();
				districtAdcodeMap.clear();
				districtComboBox.removeAllItems();
				List<City> cities = allDistrictService.getCitysByProvinceAdcode(adcode);
				if (cities == null || cities.size() == 0) {
					return null;
				}
				String cityItems[] = new String[cities.size() + 1];
				cityItems[0] = "全部";
				for (int i = 0; i < cities.size(); i++) {
					cityItems[i + 1] = cities.get(i).getName();
					cityAdcodeMap.put(cities.get(i).getName(), cities.get(i).getAdcode());
				}
				cityComboBox.setModel(new DefaultComboBoxModel<String>(cityItems));
				getDistrictsByCityAdcode(cities.get(0).getAdcode());
				return null;
			}
		};
		worker.execute();
	}

	private void getDistrictsByCityAdcode(String adcode) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				districtAdcodeMap.clear();
				districtComboBox.removeAllItems();
				List<District> districts = allDistrictService.getDistrictsByCityAdcode(adcode);
				if (districts == null || districts.size() == 0) {
					return null;
				}
				String districtItems[] = new String[districts.size() + 1];
				districtItems[0] = "全部";
				for (int i = 0; i < districts.size(); i++) {
					districtItems[i + 1] = districts.get(i).getName();
					districtAdcodeMap.put(districts.get(i).getName(), districts.get(i).getAdcode());
				}
				districtComboBox.setModel(new DefaultComboBoxModel<String>(districtItems));
				return null;
			}
		};
		worker.execute();
	}

	private void getPolygonByAdcode(String adcode) {
		if (adcode == null) {
			return;
		}
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				Area area = allDistrictService.getAreaByAdcode(adcode);
				List<List<LngLatPoint>> blocks = new ArrayList<>();
				String[] block = area.getPolyline().split("\\|");
				for (int i = 0; i < block.length; i++) {
					String[] polyline = block[i].split(";");
					List<LngLatPoint> points = new ArrayList<>();
					for (int j = 0; j < polyline.length; j++) {
						if (j % 1 == 0) {
							String[] a = polyline[j].split(",");
							LngLatPoint lngLatPoint = new LngLatPoint(Double.valueOf(a[0]), Double.valueOf(a[1]));
							if (adcode.indexOf("710") == 0 || adcode.indexOf("810") == 0
									|| adcode.indexOf("820") == 0) {
								lngLatPoint = GeoUtils.gcj02_To_wgs84(lngLatPoint);
							}
							points.add(lngLatPoint);
						}
					}
					blocks.add(points);
				}
				String json = JSON.toJSONString(blocks);
				browserEngine.sendMessageBySocket("DrawPolygonAndPositioning", json);
				return null;
			}
		};
		worker.execute();
	}

}
