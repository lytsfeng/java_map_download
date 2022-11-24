package com.jmd;

import javax.swing.SwingUtilities;

import com.jmd.common.StaticVar;
import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jmd.common.Setting;
import com.jmd.ui.MainFrame;
import com.jmd.ui.StartupWindow;
import com.jmd.z0test.TestFunc;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(1)
public class MainInit implements ApplicationRunner {

    @Autowired
    private SharedService sharedService;
    @Autowired
    private MainFrame mainFrame;
    @Autowired
    private TestFunc test;

    @Override
    public void run(ApplicationArguments args) {
        SwingUtilities.invokeLater(() -> {
            if (StaticVar.IS_Windows_10 || StaticVar.IS_Windows_11) {
                // Win10系统直接显示界面
                showMainFrame();
            } else {
                // 其他系统先通过jtattoo触发窗口装饰后，再回归所选主题，然后显示界面
                Setting setting = ApplicationSetting.getSetting();
                Map<String, Object> map = new HashMap<>();
                map.put("name", setting.getThemeName());
                map.put("clazz", setting.getThemeClazz());
                sharedService.pub(SharedType.CHANGE_THEME, map);
                this.showMainFrame();
            }
        });
        new Thread(() -> test.run()).start();
    }

    private void showMainFrame() {
        mainFrame.setVisible(true);
        StartupWindow.getIstance().close();
    }

}
