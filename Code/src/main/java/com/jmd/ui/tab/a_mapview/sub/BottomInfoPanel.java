package com.jmd.ui.tab.a_mapview.sub;

import javax.swing.JPanel;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;

import lombok.Getter;

import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;

@Component
public class BottomInfoPanel extends JPanel {

    @Getter
    private JLabel contentLabel;

//	public BottomInfoPanel() {
//		init();
//	}

    @PostConstruct
    private void init() {

        this.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP,
                StaticVar.FONT_SourceHanSansCNNormal_12, null));
        setLayout(new BorderLayout(0, 0));

        JLabel leftLabel = new JLabel(" JetBrains Runtime: " + System.getProperty("java.vm.version") + ", ");
        leftLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        add(leftLabel, BorderLayout.WEST);

        contentLabel = new JLabel("");
        contentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        add(contentLabel, BorderLayout.CENTER);

    }

}
