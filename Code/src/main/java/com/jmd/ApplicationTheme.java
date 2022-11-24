package com.jmd;

import javax.annotation.PostConstruct;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.callback.CommonAsyncCallback;
import com.jmd.common.Setting;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApplicationTheme {

    @Autowired
    private SharedService sharedService;

    @PostConstruct
    private void postInit() {
        this.subShared();
    }

    private void subShared() {
        sharedService.sub(SharedType.CHANGE_THEME).subscribe((res) -> {
            Map<String, Object> map = (HashMap) res;
            change((String) map.get("name"), (String) map.get("clazz"));
        });
    }

    public void change(String name, String clazz) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(clazz);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            sharedService.pub(SharedType.UPDATE_THEME_TEXT, "Theme: " + name);
            sharedService.pub(SharedType.UPDATE_UI, true);
            Setting setting = ApplicationSetting.getSetting();
            setting.setThemeName(name);
            setting.setThemeClazz(clazz);
            ApplicationSetting.save(setting);
        });
    }

}
