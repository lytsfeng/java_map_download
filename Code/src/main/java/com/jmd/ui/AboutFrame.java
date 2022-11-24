package com.jmd.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;
import javax.annotation.PostConstruct;
import javax.swing.GroupLayout.Alignment;
import java.awt.BorderLayout;
import java.awt.Desktop;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.util.CommonUtils;

import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import javax.swing.border.EtchedBorder;

@Component
public class AboutFrame extends JFrame {

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
		getContentPane().add(panel, BorderLayout.CENTER);

		JLabel openjdkIconLabel = new JLabel("");
		ImageIcon openjdkIconImage = new ImageIcon(Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/java.png")));
		openjdkIconImage.setImage(openjdkIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		openjdkIconLabel.setIcon(openjdkIconImage);

		JLabel openjdkTextLabel = new JLabel("Liberica JDK 17");
		openjdkTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

		JLabel openlayersIconLabel = new JLabel("");
		ImageIcon openlayersIconImage = new ImageIcon(
				Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/openlayers.png")));
		openlayersIconImage.setImage(openlayersIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		openlayersIconLabel.setIcon(openlayersIconImage);

		JLabel openlayersTextLabel = new JLabel("OpenLayers 6.12.0");
		openlayersTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

		JLabel eclipseIconLabel = new JLabel("");
		ImageIcon eclipseIconImage = new ImageIcon(Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/eclipse.png")));
		eclipseIconImage.setImage(eclipseIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		eclipseIconLabel.setIcon(eclipseIconImage);

		JLabel eclipseTextLabel = new JLabel("Window Builder 1.9.6");
		eclipseTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

		JLabel gitIconlabel = new JLabel("");
		ImageIcon gitIconImage = new ImageIcon(Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/git.png")));
		gitIconImage.setImage(gitIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		gitIconlabel.setIcon(gitIconImage);
		
		JLabel opencvIconLabel = new JLabel("");
		ImageIcon opencvIconImage = new ImageIcon(Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/opencv.png")));
		opencvIconImage.setImage(opencvIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		opencvIconLabel.setIcon(opencvIconImage);
		
		JLabel opencvTextLabel = new JLabel("OpenCV 4.5.1");
		opencvTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		
		JLabel angularIconLabel = new JLabel("");
		ImageIcon angularIconImage = new ImageIcon(Objects.requireNonNull(AboutFrame.class.getResource("/com/jmd/assets/icon/angular.png")));
		angularIconImage.setImage(angularIconImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		angularIconLabel.setIcon(angularIconImage);
		
		JLabel angularTextLabel = new JLabel("Angular 13");
		angularTextLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

		JLabel gitTextlabel = new JLabel(git);
		gitTextlabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

		JButton gitCopyButton = new JButton("复制git地址");
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

		JButton gitOpenButton = new JButton("打开git");
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

		JTextArea tipTextArea = new JTextArea();
		tipTextArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tipTextArea.setLineWrap(true);
		tipTextArea.setText("Build日期：2022-03-25");
		tipTextArea.setEditable(false);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(tipTextArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(openjdkIconLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(openjdkTextLabel, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(opencvIconLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(opencvTextLabel))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(openlayersIconLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(openlayersTextLabel, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(angularIconLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(angularTextLabel))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(eclipseIconLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(eclipseTextLabel, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(gitIconlabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(gitTextlabel, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(gitCopyButton, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(gitOpenButton, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
							.addComponent(openjdkIconLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addComponent(openjdkTextLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_panel.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(opencvTextLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addComponent(opencvIconLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(openlayersIconLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(openlayersTextLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(eclipseIconLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(eclipseTextLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(angularTextLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(angularIconLabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(gitIconlabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(gitTextlabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(gitOpenButton)
						.addComponent(gitCopyButton))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tipTextArea, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);

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
		sharedService.sub(SharedType.CHANGE_THEME).subscribe((res) -> {
			SwingUtilities.invokeLater(() -> {
				SwingUtilities.updateComponentTreeUI(this);
			});
		});
	}

}
