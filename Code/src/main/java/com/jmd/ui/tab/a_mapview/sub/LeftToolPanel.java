package com.jmd.ui.tab.a_mapview.sub;

import javax.swing.JPanel;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.browser.BrowserEngine;
import com.jmd.common.StaticVar;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

@Component
public class LeftToolPanel extends JPanel {

	private static final String OSM_NAME = "OSM(wgs84)(可能需要代理)";
	private static final String TIAN_NAME = "天地图(wgs84)";
	private static final String GOOGLE_NAME = "谷歌地图(gcj02)(可能需要代理)";
	private static final String AMAP_NAME = "高德地图(gcj02)";
	private static final String TENCENT_NAME = "腾讯地图(gcj02)";
	private static final String BING_NAME = "必应地图(gcj02)";
	private static final String BING_WGS84_NAME = "必应地图(wgs84)";
	private static final String BAIDU_NAME = "百度地图(仅预览)";

	@Autowired
	private BrowserEngine browserEngine;

//	public LeftToolPanel() {
//		init();
//	}

	@PostConstruct
	private void init() {

		this.setBorder(new TitledBorder(null, "选项", TitledBorder.LEADING, TitledBorder.TOP,
				StaticVar.FONT_SourceHanSansCNNormal_12, null));

		JLabel drawTypeLabel = new JLabel("绘制类型");
		drawTypeLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

		JRadioButton drawTypePolygonRadioButton = new JRadioButton("多边形");
		drawTypePolygonRadioButton.setSelected(true);
		drawTypePolygonRadioButton.setFocusable(false);
		drawTypePolygonRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
		drawTypePolygonRadioButton.addItemListener((e) -> {
			if (drawTypePolygonRadioButton == e.getSource() && drawTypePolygonRadioButton.isSelected()) {
				browserEngine.sendMessageByWebsocket("SwitchDrawType", "Polygon");
			}
		});

		JRadioButton drawTypeCircleRadioButton = new JRadioButton("圆形");
		drawTypeCircleRadioButton.setSelected(false);
		drawTypeCircleRadioButton.setFocusable(false);
		drawTypeCircleRadioButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
		drawTypeCircleRadioButton.addItemListener((e) -> {
			if (drawTypeCircleRadioButton == e.getSource() && drawTypeCircleRadioButton.isSelected()) {
				browserEngine.sendMessageByWebsocket("SwitchDrawType", "Circle");
			}
		});

		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(drawTypePolygonRadioButton);
		btnGroup.add(drawTypeCircleRadioButton);

		JLabel layerTypeLabel = new JLabel("图层类型");
		layerTypeLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

		JScrollPane layerTypeScrollPane = new JScrollPane();

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(drawTypeLabel)
								.addComponent(drawTypePolygonRadioButton).addComponent(drawTypeCircleRadioButton)
								.addComponent(layerTypeLabel)
								.addComponent(layerTypeScrollPane, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(drawTypeLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(drawTypePolygonRadioButton)
						.addComponent(drawTypeCircleRadioButton).addGap(18).addComponent(layerTypeLabel)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(layerTypeScrollPane, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
						.addContainerGap()));

		JTree layerTypeTree = new JTree();
		layerTypeTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("全部图层") {
			private static final long serialVersionUID = -3061452225608628201L;
			{
				DefaultMutableTreeNode node_1 = new DefaultMutableTreeNode(OSM_NAME);
				node_1.add(new DefaultMutableTreeNode("OpenStreetMap"));
				add(node_1);
				DefaultMutableTreeNode node_2 = new DefaultMutableTreeNode(TIAN_NAME);
				node_2.add(new DefaultMutableTreeNode("普通图-无标注"));
				node_2.add(new DefaultMutableTreeNode("地形图-无标注"));
				node_2.add(new DefaultMutableTreeNode("边界线-无标注"));
				node_2.add(new DefaultMutableTreeNode("标注层"));
				add(node_2);
				DefaultMutableTreeNode node_3 = new DefaultMutableTreeNode(GOOGLE_NAME);
				node_3.add(new DefaultMutableTreeNode("普通图-带标注"));
				node_3.add(new DefaultMutableTreeNode("地形图-带标注"));
				node_3.add(new DefaultMutableTreeNode("影像图-带标注"));
				node_3.add(new DefaultMutableTreeNode("影像图-无标注"));
				node_3.add(new DefaultMutableTreeNode("路网图-带标注"));
				add(node_3);
				DefaultMutableTreeNode node_4 = new DefaultMutableTreeNode(AMAP_NAME);
				node_4.add(new DefaultMutableTreeNode("普通图-带标注"));
				node_4.add(new DefaultMutableTreeNode("普通图-无标注"));
				node_4.add(new DefaultMutableTreeNode("影像图-无标注"));
				node_4.add(new DefaultMutableTreeNode("路网图-带标注"));
				node_4.add(new DefaultMutableTreeNode("路网图-无标注"));
				add(node_4);
				DefaultMutableTreeNode node_5 = new DefaultMutableTreeNode(TENCENT_NAME);
				node_5.add(new DefaultMutableTreeNode("普通图-带标注"));
				add(node_5);
				DefaultMutableTreeNode node_6 = new DefaultMutableTreeNode(BING_NAME);
				node_6.add(new DefaultMutableTreeNode("普通图1-带标注-全球"));
				node_6.add(new DefaultMutableTreeNode("普通图1-带标注-国内"));
				node_6.add(new DefaultMutableTreeNode("普通图1-无标注"));
				node_6.add(new DefaultMutableTreeNode("普通图2-带标注-全球"));
				node_6.add(new DefaultMutableTreeNode("普通图2-带标注-国内"));
				node_6.add(new DefaultMutableTreeNode("普通图2-无标注"));
				add(node_6);
				DefaultMutableTreeNode node_7 = new DefaultMutableTreeNode(BING_WGS84_NAME);
				node_7.add(new DefaultMutableTreeNode("影像图-无标注"));
				add(node_7);
				DefaultMutableTreeNode node_8 = new DefaultMutableTreeNode(BAIDU_NAME);
				node_8.add(new DefaultMutableTreeNode("百度瓦片图旧版"));
				add(node_8);
			}
		}));
		layerTypeTree.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
		layerTypeTree.addTreeSelectionListener((e) -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) layerTypeTree.getLastSelectedPathComponent();
			if (node != null && node.isLeaf()) {
				switchLayer(node.getParent().toString(), node.toString());
			}
		});
		layerTypeScrollPane.setViewportView(layerTypeTree);
		setLayout(groupLayout);

	}

	private void switchLayer(String parent, String self) {
		switch (parent) {
		case OSM_NAME:
			switch (self) {
			case "OpenStreetMap":
				browserEngine.sendMessageByWebsocket("SwitchResource", "OpenStreet");
				break;
			}
			break;
		case TIAN_NAME:
			switch (self) {
			case "普通图-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Tianditu-Normal-None");
				break;
			case "地形图-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Tianditu-Terrain-None");
				break;
			case "边界线-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Tianditu-Line");
				break;
			case "标注层":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Tianditu-Tip");
				break;
			}
			break;
		case GOOGLE_NAME:
			switch (self) {
			case "普通图-带标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Google-Normal");
				break;
			case "地形图-带标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Google-Terrain");
				break;
			case "影像图-带标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Google-Satellite");
				break;
			case "影像图-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Google-Satellite-None");
				break;
			case "路网图-带标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Google-Street");
				break;
			}
			break;
		case AMAP_NAME:
			switch (self) {
			case "普通图-带标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "AMap-Normal");
				break;
			case "普通图-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "AMap-Normal-None");
				break;
			case "影像图-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "AMap-Satellite-None");
				break;
			case "路网图-带标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "AMap-Street");
				break;
			case "路网图-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "AMap-Street-None");
				break;
			}
			break;
		case TENCENT_NAME:
			switch (self) {
			case "普通图-带标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Tencent-Normal");
				break;
			}
			break;
		case BING_NAME:
			switch (self) {
			case "普通图1-带标注-全球":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Bing-Normal-1");
				break;
			case "普通图1-带标注-国内":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Bing-Normal-1-CN");
				break;
			case "普通图1-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Bing-Normal-1-None");
				break;
			case "普通图2-带标注-全球":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Bing-Normal-2");
				break;
			case "普通图2-带标注-国内":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Bing-Normal-2-CN");
				break;
			case "普通图2-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Bing-Normal-2-None");
				break;
			}
			break;
		case BING_WGS84_NAME:
			switch (self) {
			case "影像图-无标注":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Bing-Satellite-None");
				break;
			}
			break;
		case BAIDU_NAME:
			switch (self) {
			case "百度瓦片图旧版":
				browserEngine.sendMessageByWebsocket("SwitchResource", "Baidu-Normal");
				break;
			}
			break;
		}
	}

}
