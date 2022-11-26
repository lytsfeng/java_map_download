package com.jmd;

import javax.swing.*;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.Setting;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApplicationTheme {

    private Integer currentThemeType = ApplicationSetting.getSetting().getThemeType();
    @Autowired
    private SharedService sharedService;

    @PostConstruct
    private void postInit() {
        this.subShared();
    }

    private void subShared() {
        sharedService.sub(SharedType.CHANGE_THEME).subscribe((res) -> {
            Map<String, Object> map = (HashMap) res;
            change((String) map.get("name"), (Integer) map.get("type"), (String) map.get("clazz"));
        });
    }

    public void change(String name, Integer type, String clazz) {
        // 保存配置
        Setting setting = ApplicationSetting.getSetting();
        setting.setThemeName(name);
        setting.setThemeType(type);
        setting.setThemeClazz(clazz);
        ApplicationSetting.save(setting);
        // 更新窗口
        if (currentThemeType.equals(type)) {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(clazz);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                sharedService.pub(SharedType.UPDATE_THEME_TEXT, name);
                sharedService.pub(SharedType.UPDATE_UI, true);
            });
        } else {
            JOptionPane.showMessageDialog(null, "切换不同组的主题，程序重启后生效");
        }
    }

}
