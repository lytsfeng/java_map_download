package com.jmd.ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.http.ProxySetting;
import com.jmd.ui.tab.a_mapview.sub.TopStatusPanel;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;

@Component
public class ProxySettingFrame extends JFrame {

	@Serial
	private static final long serialVersionUID = -3198278534105518944L;

	private JCheckBox proxyEnableCheckBox;
	private JTextField hostnameInputTextField;
	private JTextField portInputTextField;

	@Autowired
	private SharedService sharedService;
	@Autowired
	private TopStatusPanel topStatusPanel;

//	public ProxySettingFrame() {
//		init();
//	}

	@PostConstruct
	private void init() {

		proxyEnableCheckBox = new JCheckBox("使用代理");
		proxyEnableCheckBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		proxyEnableCheckBox.setSelected(ProxySetting.enable);

		JLabel hostnameTitleLabel = new JLabel("域名/IP地址：");
		hostnameTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

		hostnameInputTextField = new JTextField();
		hostnameInputTextField.setColumns(10);
		hostnameInputTextField.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		hostnameInputTextField.setText(ProxySetting.hostname);

		JLabel portTitleLabel = new JLabel("HTTP代理端口：");
		portTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

		portInputTextField = new JTextField();
		portInputTextField.setColumns(10);
		portInputTextField.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		portInputTextField.setText(String.valueOf(ProxySetting.port));

		JButton button = new JButton("确定");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				ProxySetting.enable = proxyEnableCheckBox.isSelected();
				ProxySetting.hostname = hostnameInputTextField.getText();
				ProxySetting.port = Integer.parseInt(portInputTextField.getText());
				topStatusPanel.updateProxyStatus();
				setVisible(false);
			}
		});
		button.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		button.setFocusable(false);

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(proxyEnableCheckBox)
								.addComponent(hostnameTitleLabel)
								.addComponent(hostnameInputTextField, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
								.addComponent(portTitleLabel)
								.addComponent(portInputTextField, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
								.addComponent(button, Alignment.TRAILING))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(proxyEnableCheckBox)
						.addGap(18).addComponent(hostnameTitleLabel).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(hostnameInputTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(portTitleLabel).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(portInputTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 79, Short.MAX_VALUE).addComponent(button)
						.addContainerGap()));
		getContentPane().setLayout(groupLayout);

		this.setTitle("代理设置");
		this.setSize(new Dimension(240, 320));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
		this.setVisible(false);
		this.setResizable(false);
		this.subShared();

	}

	private void subShared() {
		sharedService.sub(SharedType.UPDATE_UI).subscribe((res) -> {
			SwingUtilities.invokeLater(() -> {
				SwingUtilities.updateComponentTreeUI(this);
			});
		});
	}

	@Override
	public void setVisible(boolean b) {
		proxyEnableCheckBox.setSelected(ProxySetting.enable);
		hostnameInputTextField.setText(ProxySetting.hostname);
		portInputTextField.setText(String.valueOf(ProxySetting.port));
		super.setVisible(b);
	}

}
