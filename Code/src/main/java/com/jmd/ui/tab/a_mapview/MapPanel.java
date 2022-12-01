package com.jmd.ui.tab.a_mapview;

import javax.swing.JPanel;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.ui.tab.a_mapview.sub.BottomInfoPanel;
import com.jmd.ui.tab.a_mapview.sub.BrowserPanel;
import com.jmd.ui.tab.a_mapview.sub.LeftToolPanel;
import com.jmd.ui.tab.a_mapview.sub.TopDistrictPanel;
import com.jmd.ui.tab.a_mapview.sub.TopStatusPanel;
import com.jmd.ui.tab.a_mapview.sub.TopToolPanel;

import java.awt.BorderLayout;
import java.io.Serial;

@Component
public class MapPanel extends JPanel {

	@Serial
	private static final long serialVersionUID = 7958127591579659670L;

	@Autowired
	private TopToolPanel topToolPanel;
	@Autowired
	private TopDistrictPanel topDistrictPanel;
	@Autowired
	private TopStatusPanel topStatusPanel;
	@Autowired
	private LeftToolPanel leftToolPanel;
	@Autowired
	private BrowserPanel browserPanel;
	@Autowired
	private BottomInfoPanel bottomInfoPanel;

//	public MapPanel() {
//		init();
//	}

	@PostConstruct
	private void init() {

		this.setLayout(new BorderLayout(0, 0));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		JPanel topSubPanel = new JPanel();
		topSubPanel.setLayout(new BorderLayout());
		topSubPanel.add(topToolPanel, BorderLayout.WEST);
		topSubPanel.add(topDistrictPanel, BorderLayout.EAST);
		
		topPanel.add(topSubPanel, BorderLayout.WEST);
		topPanel.add(topStatusPanel, BorderLayout.CENTER);

		this.add(topPanel, BorderLayout.NORTH);
		this.add(leftToolPanel, BorderLayout.WEST);
		this.add(browserPanel, BorderLayout.CENTER);
		this.add(bottomInfoPanel, BorderLayout.SOUTH);

	}

}
