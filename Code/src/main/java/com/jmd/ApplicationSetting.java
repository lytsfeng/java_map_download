package com.jmd;

import java.io.File;

import com.jmd.common.Setting;
import com.jmd.util.CommonUtils;

import lombok.Getter;

public class ApplicationSetting {

	private static final File path = new File(System.getProperty("user.dir") + "\\setting");
	private static final File file = new File(System.getProperty("user.dir") + "\\setting\\setting");

	@Getter
	private static Setting setting;

	public static void load() {
		if (!path.exists() && !path.isFile()) {
			path.mkdir();
		}
		if (!file.exists() && !file.isFile()) {
			setting = createDefault();
		} else {
			setting = loadSettingFile();
		}
	}

	public static void save(Setting _setting) {
		setting = _setting;
		try {
			CommonUtils.saveObj2File(_setting, file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Setting createDefault() {
		Setting _setting = new Setting();
		_setting.setThemeName("Flatlaf IntelliJ");
		_setting.setThemeClazz("com.formdev.flatlaf.FlatIntelliJLaf");
		try {
			CommonUtils.saveObj2File(_setting, file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _setting;
	}

	private static Setting loadSettingFile() {
		Setting _setting = new Setting();
		try {
			_setting = (Setting) CommonUtils.readFile2Obj(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _setting;
	}

}
