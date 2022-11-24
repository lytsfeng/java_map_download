package com.jmd.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import com.jmd.common.StaticVar;

import lombok.Getter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Objects;
import javax.swing.border.LineBorder;

public class StartupWindow extends JWindow {

	private static volatile StartupWindow instance;
	@Getter
	private final JProgressBar progressBar;
	@Getter
	private final JLabel progressLabel;
	@Getter
	private final JLabel beanNameLabel;

	public StartupWindow() {

		this.getContentPane().setLayout(null);
		ImageIcon backgroundIconImage = new ImageIcon(
				Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/img/load-background-hi.png")));
		backgroundIconImage.setImage(backgroundIconImage.getImage().getScaledInstance(398, 298, Image.SCALE_SMOOTH));

		JPanel backgroundPanel = new JPanel();
		Color borderColor = new Color(112, 112, 112);
		backgroundPanel.setBorder(new LineBorder(borderColor));
		backgroundPanel.setBounds(0, 0, 400, 301);
		backgroundPanel.setLayout(null);
		getContentPane().add(backgroundPanel);

		JLabel titleLabel = new JLabel("Java地图下载器");
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		titleLabel.setBounds(251, 10, 139, 15);
		titleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		backgroundPanel.add(titleLabel);

		progressLabel = new JLabel("正在加载");
		progressLabel.setForeground(Color.BLACK);
		progressLabel.setBounds(10, 275, 380, 15);
		progressLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		backgroundPanel.add(progressLabel);

		JLabel backgroundLabel = new JLabel("");
		backgroundLabel.setIcon(backgroundIconImage);
		backgroundLabel.setBounds(1, 1, 398, 298);
		backgroundPanel.add(backgroundLabel);

		JPanel beanNamePanel = new JPanel();
		beanNamePanel.setBorder(new LineBorder(borderColor));
		beanNamePanel.setBounds(0, 300, 400, 26);
		beanNamePanel.setLayout(null);
		getContentPane().add(beanNamePanel);

		beanNameLabel = new JLabel("org.springframework.boot");
		beanNameLabel.setBounds(10, 0, 380, 25);
		beanNameLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		beanNamePanel.add(beanNameLabel);

		JPanel progressBarPanel = new JPanel();
		progressBarPanel.setBorder(new LineBorder(borderColor));
		progressBarPanel.setBounds(0, 325, 400, 15);
		getContentPane().add(progressBarPanel);
		progressBarPanel.setLayout(new BorderLayout());

		progressBar = new JProgressBar();
		progressBarPanel.add(progressBar, BorderLayout.CENTER);

		this.setSize(400, 340);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				instance = null;
			}
		});

	}

	public void close() {
		this.dispose();
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public static StartupWindow getIstance() {
		if (instance == null) {
			synchronized (StartupWindow.class) {
				if (instance == null) {
					instance = new StartupWindow();
				}
			}
		}
		return instance;
	}
}
