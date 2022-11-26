package com.jmd.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;

import com.jmd.ApplicationSetting;
import com.jmd.browser.BrowserEngine;
import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import com.jmd.taskfunc.TaskState;
import com.jmd.ui.tab.a_mapview.sub.BrowserPanel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.entity.task.TaskAllInfoEntity;
import com.jmd.entity.theme.ThemeEntity;
import com.jmd.taskfunc.TaskExecFunc;
import com.jmd.ui.tab.a_mapview.sub.BottomInfoPanel;
import com.jmd.util.TaskUtils;

import javax.swing.ImageIcon;

@Component
public class MainMenuBar extends JMenuBar {

    @Autowired
    private SharedService sharedService;

    @Autowired
    private AboutFrame aboutFrame;
    @Autowired
    private LicenseFrame licenseFrame;
    @Autowired
    private DonateFrame donateFrame;
    @Autowired
    private ProxySettingFrame proxySettingFrame;
    @Autowired
    private TaskExecFunc taskExec;
    @Autowired
    private BrowserPanel browserPanel;
    @Autowired
    private BottomInfoPanel bottomInfoPanel;
    @Autowired
    private BrowserEngine browserEngine;

    private final JMenuItem themeNameLabel = new JMenuItem();
    private final ImageIcon selectedIcon = new ImageIcon(Objects.requireNonNull(MainMenuBar.class.getResource("/com/jmd/assets/icon/selected.png")));

//	public MainMenuBar() {
//		init();
//	}

    @PostConstruct
    private void init() {

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        JMenu styleMenu = new JMenu("主题");
        styleMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(styleMenu);

        for (ThemeEntity parent : StaticVar.THEME_LIST) {
            JMenu themeMenu = new JMenu(parent.getName());
            themeMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
            styleMenu.add(themeMenu);
            for (ThemeEntity theme : parent.getSub()) {
                JMenuItem themeSubMenuItem = new JMenuItem(theme.getName());
                themeSubMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
                themeMenu.add(themeSubMenuItem);
                themeSubMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.getButton() == 1) {
                            String name = parent.getName() + " " + theme.getName();
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", name);
                            map.put("type", theme.getType());
                            map.put("clazz", theme.getClazz());
                            sharedService.pub(SharedType.CHANGE_THEME, map);
                        }
                    }
                });
            }
        }

        JMenu mapMenu = new JMenu("地图");
        mapMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(mapMenu);

        JMenuItem refreshMenuItem = new JMenuItem("刷新");
        refreshMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        mapMenu.add(refreshMenuItem);
        refreshMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    browserEngine.reload();
                }
            }
        });

        JMenuItem consoleMenuItem = new JMenuItem("开发者工具");
        consoleMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        mapMenu.add(consoleMenuItem);
        consoleMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    browserPanel.toggleDevTools();
                    if (browserPanel.isDevToolOpen()) {
                        consoleMenuItem.setIcon(selectedIcon);
                    } else {
                        consoleMenuItem.setIcon(null);
                    }
                }
            }
        });

        JMenuItem revertMenuItem = new JMenuItem("清除缓存");
        revertMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        mapMenu.add(revertMenuItem);
        revertMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    browserEngine.clearLocalStorage();
                    int n = JOptionPane.showConfirmDialog(null, "已清除缓存，是否刷新页面？");
                    if (n == JOptionPane.YES_OPTION) {
                        browserEngine.reload();
                    }
                }
            }
        });

        JMenu taskMenu = new JMenu("任务");
        taskMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(taskMenu);

        JMenuItem loadTaskMenuItem = new JMenuItem("导入未完成的下载");
        loadTaskMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        taskMenu.add(loadTaskMenuItem);
        loadTaskMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    if (TaskState.IS_TASKING) {
                        JOptionPane.showMessageDialog(null, "当前正在进行下载任务");
                        return;
                    }
                    File file = selectTaskFile();
                    if (file != null) {
                        TaskAllInfoEntity taskAllInfo = TaskUtils.getExistTaskByFile(file);
                        if (taskAllInfo != null) {
                            sharedService.pub(SharedType.MAIN_FRAME_SELECTED_INDEX, 1);
                            taskExec.loadTask(taskAllInfo);
                        } else {
                            JOptionPane.showMessageDialog(null, "导入失败，任务文件已损坏");
                        }
                    }
                }
            }
        });

        JMenuItem downloadAllWorldMenuItem = new JMenuItem("直接下载世界地图");
        downloadAllWorldMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        taskMenu.add(downloadAllWorldMenuItem);
        downloadAllWorldMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    if (TaskState.IS_TASKING) {
                        JOptionPane.showMessageDialog(null, "当前正在进行下载任务");
                        return;
                    }
                    browserEngine.sendMessageByWebsocket("SubmitWorldDownload", null);
                }
            }
        });

        JMenu networkMenu = new JMenu("网络");
        networkMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(networkMenu);

        JMenuItem proxyMenuItem = new JMenuItem("代理设置");
        proxyMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        networkMenu.add(proxyMenuItem);
        proxyMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    proxySettingFrame.setVisible(true);
                }
            }
        });

        JMenu otherMenu = new JMenu("其他");
        otherMenu.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        this.add(otherMenu);

        JMenuItem aboutMenuItem = new JMenuItem("关于");
        aboutMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        otherMenu.add(aboutMenuItem);
        aboutMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    aboutFrame.setVisible(true);
                }
            }
        });

        JMenuItem licenseMenuItem = new JMenuItem("license");
        licenseMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        otherMenu.add(licenseMenuItem);
        licenseMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    licenseFrame.setVisible(true);
                }
            }
        });

        JMenuItem donateMenuItem = new JMenuItem("捐赠开发者");
        donateMenuItem.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        otherMenu.add(donateMenuItem);
        donateMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    donateFrame.setVisible(true);
                }
            }
        });

        themeNameLabel.setText("Theme: " + ApplicationSetting.getSetting().getThemeName());
        themeNameLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        themeNameLabel.setFocusable(false);
        themeNameLabel.setEnabled(false);
        this.add(themeNameLabel);

        this.subShared();

    }

    private void subShared() {
        sharedService.sub(SharedType.UPDATE_THEME_TEXT).subscribe((res) -> {
            themeNameLabel.setText("Theme: " + res);
        });
    }

    private File selectTaskFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "地图下载任务(*.jmd)";
            }

            @Override
            public boolean accept(File f) {
                String end = f.getName().toLowerCase();
                return end.endsWith(".jmd") || f.isDirectory();
            }
        });
        chooser.setDialogTitle("选择未完成的下载任务...");
        chooser.setApproveButtonText("导入");
        chooser.setMultiSelectionEnabled(true);
        chooser.showOpenDialog(null);
        return chooser.getSelectedFile();
    }

}
