package com.jmd.ui.tab.a_mapview.sub;

import java.awt.*;
import java.io.Serial;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.jmd.browser.BrowserType;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.browser.BrowserEngine;
import com.jmd.common.StaticVar;

@Component
public class BrowserPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 6855207015589162698L;

    @Autowired
    private BottomInfoPanel bottomInfoPanel;
    @Autowired
    private BrowserEngine browserEngine;

    private JPanel browserPanel;
    private JPanel devToolPanel;
    @Getter
    private boolean devToolOpen = false;
    private JSplitPane splitPane;

//    BrowserPanel() {
//        init();
//    }

    @PostConstruct
    private void init() {

        this.setBorder(new TitledBorder(null, "地图", TitledBorder.LEADING, TitledBorder.TOP,
                StaticVar.FONT_SourceHanSansCNNormal_12, null));
        this.setLayout(new BorderLayout());

        splitPane = new JSplitPane();
        splitPane.setOneTouchExpandable(false);
        splitPane.setContinuousLayout(false);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.add(splitPane, BorderLayout.CENTER);

        // 浏览器
        browserPanel = new JPanel();
        browserPanel.setLayout(new BorderLayout());
        splitPane.setLeftComponent(browserPanel);

        // 开发者工具
        devToolPanel = new JPanel();
        devToolPanel.setLayout(new BorderLayout());
        splitPane.setRightComponent(null);

        JLabel label = new JLabel("WebView初始化");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        browserPanel.add(label, BorderLayout.CENTER);

        this.createBrowser();

    }

    private void createBrowser() {
        browserEngine.init((browser) -> {
            browserPanel.removeAll();
            browserPanel.add(browser.getBrowserContainer(), BorderLayout.CENTER);
            bottomInfoPanel.getContentLabel().setText(browserEngine.getVersion());
        });
    }

    public void changeBrowser(BrowserType type) {
        browserPanel.removeAll();
        this.closeDevTools();
        devToolOpen = false;
        browserEngine.changeCore(type, (browser) -> {
            browserPanel.add(browser.getBrowserContainer(), BorderLayout.CENTER);
            bottomInfoPanel.getContentLabel().setText(browserEngine.getVersion());
        });
    }

    public void toggleDevTools() {
        if (browserEngine.getBrowserType() == BrowserType.CHROMIUM_EMBEDDED_CEF_BROWSER) {
            if (devToolOpen) {
                this.closeDevTools();
                devToolOpen = false;
            } else {
                this.openDevTools();
                devToolOpen = true;
            }
        } else {
            JOptionPane.showMessageDialog(null, "当前内核不支持");
        }
    }

    private void openDevTools() {
        devToolPanel.add(browserEngine.getBrowser().getDevToolsContainer(), BorderLayout.CENTER);
        splitPane.setRightComponent(devToolPanel);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(this.getSize().width - 500);
    }

    private void closeDevTools() {
        devToolPanel.removeAll();
        splitPane.remove(devToolPanel);
        splitPane.setRightComponent(null);
        splitPane.setContinuousLayout(false);
        splitPane.setDividerLocation(this.getSize().width);
    }

}
