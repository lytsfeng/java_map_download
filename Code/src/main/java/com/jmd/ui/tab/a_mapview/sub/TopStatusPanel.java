package com.jmd.ui.tab.a_mapview.sub;

import java.awt.Color;
import java.io.Serial;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.http.ProxySetting;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class TopStatusPanel extends JPanel {

	@Serial
	private static final long serialVersionUID = 570349647502262520L;

	private JLabel proxyStatusValue;

//	public TopStatusPanel() {
//		init();
//	}

	@PostConstruct
	private void init() {

		this.setBorder(new TitledBorder(null, "状态信息", TitledBorder.LEADING, TitledBorder.TOP,
				StaticVar.FONT_SourceHanSansCNNormal_12, null));

		JLabel proxyStatusLabel = new JLabel("代理状态：");
		proxyStatusLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

		proxyStatusValue = new JLabel("");
		proxyStatusLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(proxyStatusLabel)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(proxyStatusValue)
						.addContainerGap(50, Short.MAX_VALUE)));
		groupLayout
				.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(proxyStatusLabel).addComponent(proxyStatusValue))
								.addContainerGap(0, Short.MAX_VALUE)));
		setLayout(groupLayout);

		updateProxyStatus();

	}

	public void updateProxyStatus() {
		if (ProxySetting.enable) {
			proxyStatusValue.setText("开启");
			proxyStatusValue.setForeground(Color.GREEN);
		} else {
			proxyStatusValue.setText("关闭");
			proxyStatusValue.setForeground(Color.BLUE);
		}
	}

}
