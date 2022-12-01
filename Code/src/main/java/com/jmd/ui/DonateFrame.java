package com.jmd.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.Serial;
import java.util.Objects;

import javax.swing.*;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DonateFrame extends JFrame {

	@Serial
	private static final long serialVersionUID = -7739920320485676764L;

	@Autowired
    private SharedService sharedService;

//	public DonateFrame() {
//		init();
//	}

	@PostConstruct
	private void init() {

		this.setIconImage(
				Toolkit.getDefaultToolkit().getImage(AboutFrame.class.getResource("/com/jmd/assets/icon/java.png")));
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 631, 450);
		panel.setLayout(null);

		JLabel alipayImageLabel = new JLabel("");
		alipayImageLabel.setBounds(0, 0, 300, 450);
		alipayImageLabel.setIcon(new ImageIcon(Objects.requireNonNull(DonateFrame.class.getResource("/com/jmd/assets/donate/alipay.jpg"))));
		panel.add(alipayImageLabel);

		JLabel wechatImageLabel = new JLabel("");
		wechatImageLabel.setIcon(new ImageIcon(Objects.requireNonNull(DonateFrame.class.getResource("/com/jmd/assets/donate/wechat.jpg"))));
		wechatImageLabel.setBounds(300, 0, 331, 450);
		panel.add(wechatImageLabel);
		getContentPane().add(panel);

		this.setTitle("捐赠开发者（非强制，不影响软件使用）");
		this.setSize(new Dimension(635, 477));
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

}
