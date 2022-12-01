package com.jmd.ui.tab.a_mapview.sub;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import com.jmd.taskfunc.TaskState;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.browser.BrowserEngine;
import com.jmd.common.StaticVar;
import com.jmd.taskfunc.TaskExecFunc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;

@Component
public class TopToolPanel extends JPanel {

	@Serial
	private static final long serialVersionUID = 3809138500637764353L;

	@Autowired
	private TaskExecFunc taskExec;
	@Autowired
	private BrowserEngine browserEngine;

//    public TopToolPanel() {
//        init();
//    }

	@PostConstruct
	private void init() {

		this.setBorder(new TitledBorder(null, "操作", TitledBorder.LEADING, TitledBorder.TOP,
				StaticVar.FONT_SourceHanSansCNNormal_12, null));

		// zoom +
		JButton zoomInButton = new JButton("放大");
		zoomInButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		zoomInButton.setFocusable(false);
		zoomInButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				browserEngine.sendMessageByWebsocket("ZoomIn", null);
			}
		});

		// zoom -
		JButton zoomOutButton = new JButton("缩小");
		zoomOutButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		zoomOutButton.setFocusable(false);
		zoomOutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				browserEngine.sendMessageByWebsocket("ZoomOut", null);
			}
		});

		// 网格
		JButton gridButton = new JButton("网格");
		gridButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		gridButton.setFocusable(false);
		gridButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				browserEngine.sendMessageByWebsocket("GridSwitch", null);
			}
		});

		// 下载
		JButton downloadButton = new JButton("下载");
		downloadButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		downloadButton.setFocusable(false);
		downloadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (TaskState.IS_TASKING) {
					JOptionPane.showMessageDialog(null, "当前正在进行下载任务");
					return;
				}
				browserEngine.sendMessageByWebsocket("SubmitBlockDownload", null);
			}
		});

		// 拖动
		JButton panButton = new JButton("拖动");
		panButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		panButton.setFocusable(false);
		panButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				browserEngine.sendMessageByWebsocket("Pan", null);
			}
		});

		// 绘制
		JButton drawButton = new JButton("绘制");
		drawButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		drawButton.setFocusable(false);
		drawButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				browserEngine.sendMessageByWebsocket("OpenDraw", null);
			}
		});

		// fitview
		JButton fitviewButton = new JButton("居中");
		fitviewButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		fitviewButton.setFocusable(false);
		fitviewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				browserEngine.sendMessageByWebsocket("Fitview", null);
			}
		});

		// 移除
		JButton removeButton = new JButton("移除");
		removeButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
		removeButton.setFocusable(false);
		removeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				browserEngine.sendMessageByWebsocket("RemoveDrawedShape", null);
			}
		});

		GroupLayout gl_panel = new GroupLayout(this);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(zoomInButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(zoomOutButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(gridButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(downloadButton))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(panButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(drawButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(fitviewButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(removeButton)))
					.addContainerGap(6, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(zoomInButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(zoomOutButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(gridButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(downloadButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(panButton)
						.addComponent(drawButton)
						.addComponent(fitviewButton)
						.addComponent(removeButton))
					.addContainerGap(6, Short.MAX_VALUE))
		);
		this.setLayout(gl_panel);

	}

}
