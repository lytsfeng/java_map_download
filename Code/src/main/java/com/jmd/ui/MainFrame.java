package com.jmd.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;

import javax.swing.*;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import com.jmd.taskfunc.TaskState;
import com.jmd.util.CommonUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.browser.BrowserEngine;
import com.jmd.common.StaticVar;
import com.jmd.taskfunc.TaskExecFunc;
import com.jmd.ui.tab.a_mapview.MapPanel;
import com.jmd.ui.tab.b_download.DownloadTaskPanel;
import com.jmd.ui.tab.c_syslog.ConsolePanel;

@Component
public class MainFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = -628803972270259148L;

    @Autowired
    private SharedService sharedService;

    @Autowired
    private TaskExecFunc taskExec;
    @Autowired
    private BrowserEngine browserEngine;

    @Autowired
    private MapPanel mapPanel;
    @Autowired
    private DownloadTaskPanel taskPanel;
    @Autowired
    private ConsolePanel consolePanel;

    @Autowired
    private MainMenuBar mainMenuBar;

    private JTabbedPane tabbedPane;

//	public MainFrame() {
//		init();
//	}

    @PostConstruct
    private void init() {

        /* 布局 */
        this.getContentPane().setLayout(new BorderLayout(0, 0));

        /* 任务栏图标 */
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Image image = defaultToolkit.getImage(MainFrame.class.getResource("/com/jmd/assets/icon/map.png"));
        if (CommonUtils.isMac()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            try {
                taskbar.setIconImage(image);
            } catch (UnsupportedOperationException e) {
                System.out.println("The os does not support: 'taskbar.setIconImage'");
            } catch (SecurityException e) {
                System.out.println("There was a security exception for: 'taskbar.setIconImage'");
            }
        } else {
            this.setIconImage(image);
        }

        /* Menu菜单 */
        this.setJMenuBar(mainMenuBar);

        /* Tabbed主界面 */
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setFocusable(false);
        tabbedPane.addTab("地图预览", null, mapPanel, null);
        tabbedPane.addTab("下载任务", null, taskPanel, null);
        tabbedPane.addTab("系统日志", null, consolePanel, null);
        tabbedPane.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        /* 任务栏图标菜单 */
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            java.awt.PopupMenu popupMenu = new java.awt.PopupMenu();
            java.awt.MenuItem openItem = new java.awt.MenuItem("show");
            openItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
            openItem.addActionListener((e) -> {
                this.setVisible(true);
            });
            java.awt.MenuItem exitItem = new java.awt.MenuItem("exit");
            exitItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_12);
            exitItem.addActionListener((e) -> {
                processExit();
            });
            popupMenu.add(openItem);
            popupMenu.add(exitItem);
            TrayIcon trayIcon = new TrayIcon(image, "地图下载器", popupMenu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        setVisible(true);
                    }
                }
            });
            try {
                tray.add(trayIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.setTitle("地图下载器");
        this.setSize(new Dimension(1280, 720));
        this.setMinimumSize(new Dimension(1150, 650));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
        this.setVisible(false);
        this.setResizable(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!SystemTray.isSupported()) {
                    processExit();
                }
            }
        });
        this.subShared();

    }

    private void subShared() {
        sharedService.sub(SharedType.UPDATE_UI).subscribe((res) -> {
            SwingUtilities.invokeLater(() -> {
                SwingUtilities.updateComponentTreeUI(this);
            });
        });
        sharedService.sub(SharedType.MAIN_FRAME_SELECTED_INDEX).subscribe((res) -> {
            tabbedPane.setSelectedIndex((int) res);
        });
    }

    private void processExit() {
        this.setVisible(false);
        if (TaskState.IS_TASKING) {
            taskExec.cancelTaks();
        }
        browserEngine.dispose();
        System.exit(0);
    }

}
