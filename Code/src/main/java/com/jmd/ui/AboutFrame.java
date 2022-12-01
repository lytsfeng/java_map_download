package com.jmd.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Desktop;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.util.CommonUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serial;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import javax.swing.border.EtchedBorder;

@Component
public class AboutFrame extends JFrame {

	@Serial
	private static final long serialVersionUID = -5612514860347124476L;

	private final String git = "https://gitee.com/CrimsonHu/java_map_download";

	@Autowired
	private SharedService sharedService;

//	public AboutFrame() {
//		init();
//	}

	@PostConstruct
	private void init() {

		this.setIconImage(
				Toolkit.getDefaultToolkit().getImage(AboutFrame.class.getResource("/com/jmd/assets/icon/java.png")));

		JTextArea springbootTextArea = new JTextArea();
		getContentPane().add(springbootTextArea, BorderLayout.NORTH);
		springbootTextArea.setFont(StaticVar.FONT_YaHeiConsolas_13);
		springbootTextArea.setText("" + "  .   ____          _            __ _ _\n"
				+ " /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\\n"
				+ "( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\\n" + " \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )\n"
				+ "  '  |____| .__|_| |_|_| |_\\__, | / / / /\n" + " =========|_|==============|___/=/_/_/_/\n"
				+ " :: Spring Boot ::        (" + SpringBootVersion.getVersion() + ")" + "");
		springbootTextArea.setEditable(false);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		getContentPane().add(panel, BorderLayout.CENTER);

		JLabel jdkIconLabel = new JLabel("");
		jdkIconLabel.setBounds(15, 10, 30, 30);
		ImageIcon jdkIconImage = new ImageIcon(
				Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/jetbrains.png")));
		jdkIconImage.setImage(jdkIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		jdkIconLabel.setIcon(jdkIconImage);
		panel.add(jdkIconLabel);

		JLabel jdkTextLabel = new JLabel("JetBrains Runtime 17");
		jdkTextLabel.setBounds(55, 10, 166, 30);
		jdkTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		panel.add(jdkTextLabel);

		JLabel jcefIconLabel = new JLabel("");
		jcefIconLabel.setBounds(238, 10, 40, 30);
		ImageIcon jcefIconImage = new ImageIcon(
				Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/cef.png")));
		jcefIconImage.setImage(jcefIconImage.getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH));
		jcefIconLabel.setIcon(jcefIconImage);
		panel.add(jcefIconLabel);

		JLabel jcefTextLabel = new JLabel("JCEF 104");
		jcefTextLabel.setBounds(288, 10, 156, 30);
		jcefTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		panel.add(jcefTextLabel);

		JLabel openlayersIconLabel = new JLabel("");
		openlayersIconLabel.setBounds(15, 46, 30, 30);
		ImageIcon openlayersIconImage = new ImageIcon(
				Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/openlayers.png")));
		openlayersIconImage.setImage(openlayersIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		openlayersIconLabel.setIcon(openlayersIconImage);
		panel.add(openlayersIconLabel);

		JLabel openlayersTextLabel = new JLabel("OpenLayers 7.1.0");
		openlayersTextLabel.setBounds(55, 46, 166, 30);
		openlayersTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		ImageIcon eclipseIconImage = new ImageIcon(
				Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/eclipse.png")));
		eclipseIconImage.setImage(eclipseIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		panel.add(openlayersTextLabel);

		JLabel gitIconLabel = new JLabel("");
		gitIconLabel.setBounds(15, 118, 30, 30);
		ImageIcon gitIconImage = new ImageIcon(
				Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/git.png")));
		gitIconImage.setImage(gitIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		gitIconLabel.setIcon(gitIconImage);
		panel.add(gitIconLabel);

		JLabel angularIconLabel = new JLabel("");
		angularIconLabel.setBounds(238, 46, 30, 30);
		ImageIcon angularIconImage = new ImageIcon(
				Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/angular.png")));
		angularIconImage.setImage(angularIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		angularIconLabel.setIcon(angularIconImage);
		panel.add(angularIconLabel);

		JLabel angularTextLabel = new JLabel("Angular 14.2.7");
		angularTextLabel.setBounds(278, 46, 166, 30);
		angularTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		panel.add(angularTextLabel);

		JLabel opencvIconLabel = new JLabel("");
		opencvIconLabel.setBounds(15, 82, 30, 30);
		ImageIcon opencvIconImage = new ImageIcon(
				Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/opencv.png")));
		opencvIconImage.setImage(opencvIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		opencvIconLabel.setIcon(opencvIconImage);
		panel.add(opencvIconLabel);

		JLabel opencvTextLabel = new JLabel("OpenCV 4.5.5");
		opencvTextLabel.setBounds(55, 82, 166, 30);
		opencvTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		panel.add(opencvTextLabel);

		JLabel gitTextlabel = new JLabel(git);
		gitTextlabel.setBounds(55, 118, 389, 30);
		gitTextlabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		panel.add(gitTextlabel);

		JButton gitCopyButton = new JButton("复制git地址");
		gitCopyButton.setBounds(10, 158, 217, 29);
		gitCopyButton.setFocusable(false);
		gitCopyButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		gitCopyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1) {
					CommonUtils.setClipboardText(git);
					JOptionPane.showMessageDialog(null, "已复制到剪贴板");
				}
			}
		});
		panel.add(gitCopyButton);

		JButton gitOpenButton = new JButton("打开git");
		gitOpenButton.setBounds(233, 158, 211, 29);
		gitOpenButton.setFocusable(false);
		gitOpenButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		gitOpenButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1) {
					try {
						Desktop desktop = Desktop.getDesktop();
						desktop.browse(new URI(git));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panel.add(gitOpenButton);

		JTextArea tipTextArea = new JTextArea();
		tipTextArea.setBounds(10, 193, 434, 78);
		tipTextArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tipTextArea.setLineWrap(true);
		tipTextArea.setText("Build日期：2022-11-26");
		tipTextArea.setEditable(false);
		panel.add(tipTextArea);

		this.setTitle("关于地图下载器");
		this.setSize(new Dimension(470, 450));
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
