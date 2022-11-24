package com.jmd.ui.tab.c_syslog;

import javax.annotation.PostConstruct;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

import com.jmd.Application;
import com.jmd.common.StaticVar;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@Component
public class ConsolePanel extends JPanel {

//	public ConsolePanel() {
//		init();
//	}

	@PostConstruct
	private void init() {
		
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		JTextArea textArea = Application.getConsoleTextArea();
		textArea.setEditable(false);
		textArea.setFont(StaticVar.FONT_YaHeiConsolas_13);
		scrollPane.setViewportView(textArea);
		
	}

}
