package com.jmd.common;

import java.awt.Font;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jmd.entity.theme.ThemeEntity;
import com.jmd.util.CommonUtils;
import com.jmd.util.FontUtils;

public class StaticVar {

	public static final boolean IS_Mac = CommonUtils.isMac();
	public static final boolean IS_Windows = CommonUtils.isWindows();
	public static final boolean IS_Windows_10 = CommonUtils.isWindows10();
	public static final boolean IS_Windows_11 = CommonUtils.isWindows11();

	public static final Font FONT_SourceHanSansCNNormal_12 = FontUtils.SourceHanSansCNNormal(12);
	public static final Font FONT_SourceHanSansCNNormal_13 = FontUtils.SourceHanSansCNNormal(13);
	public static final Font FONT_SourceHanSansCNNormal_14 = FontUtils.SourceHanSansCNNormal(14);
	public static final Font FONT_YaHeiConsolas_13 = FontUtils.YaHeiConsolas(13);

	public static final Font ICON_FONT_ICOMOON_18 = FontUtils.getIconFont("IcoMoon.ttf", 18);
	public static final Font ICON_FONT_ZONDICONS_18 = FontUtils.getIconFont("Zondicons.ttf", 18);
	public static final Font ICON_FONT_BRANDS_28 = FontUtils.getIconFont("Brands.ttf", 28);

	public static final int TILE_WIDTH = 256;
	public static final int TILE_HEIGHT = 256;

	public static final double DISK_BLOCK = 4096.0;
	public static final HashMap<Integer, Double> PNG_PER_SIZE_MAP = new HashMap<>() {
		{
			put(3, 15956.17);
			put(4, 12584.93);
			put(5, 20054.56);
			put(6, 18043.19);
			put(7, 15228.87);
			put(8, 12510.82);
			put(9, 8557.34);
			put(10, 6822.98);
			put(11, 5928.59);
			put(12, 8816.01);
			put(13, 5991.90);
			put(14, 3824.78);
			put(15, 3429.01);
			put(16, 1863.61);
			put(17, 1835.58);
			put(18, 1986.90);
			put(19, 2425.66);
			put(20, 1401.93);
			put(21, 1037.25);
		}
	};
	public static final HashMap<Integer, Double> JPG_MIDDLE_PER_SIZE_MAP = new HashMap<>() {
		{
			put(3, 15956.17 / 2.20);
			put(4, 12584.93 / 2.20);
			put(5, 20054.56 / 2.20);
			put(6, 18043.19 / 2.17);
			put(7, 15228.87 / 2.04);
			put(8, 12510.82 / 1.97);
			put(9, 8557.34 / 1.50);
			put(10, 6822.98 / 1.35);
			put(11, 5928.59 / 1.20);
			put(12, 8816.01 / 2.00);
			put(13, 5991.90 / 1.30);
			put(14, 3824.78 / 1.20);
			put(15, 3429.01 / 1.20);
			put(16, 1863.61 / 1.10);
			put(17, 1835.58 / 1.10);
			put(18, 1986.90 / 1.10);
			put(19, 2425.66 / 1.15);
			put(20, 1401.93 / 1.10);
			put(21, 1037.25 / 1.10);
		}
	};
	public static final HashMap<Integer, Double> JPG_LOW_PER_SIZE_MAP = new HashMap<>() {
		{
			put(3, 15956.17 / 4.34);
			put(4, 12584.93 / 4.30);
			put(5, 20054.56 / 4.65);
			put(6, 18043.19 / 4.23);
			put(7, 15228.87 / 3.98);
			put(8, 12510.82 / 3.69);
			put(9, 8557.34 / 3.10);
			put(10, 6822.98 / 2.67);
			put(11, 5928.59 / 2.60);
			put(12, 8816.01 / 3.42);
			put(13, 5991.90 / 2.52);
			put(14, 3824.78 / 1.50);
			put(15, 3429.01 / 1.45);
			put(16, 1863.61 / 1.15);
			put(17, 1835.58 / 1.15);
			put(18, 1986.90 / 1.15);
			put(19, 2425.66 / 1.25);
			put(20, 1401.93 / 1.10);
			put(21, 1037.25 / 1.10);
		}
	};

	public static final List<ThemeEntity> THEME_LIST = new ArrayList<>() {
		{
			add(new ThemeEntity("Flatlaf", new ArrayList<>() {
				{
					add(new ThemeEntity("Intellij", "com.formdev.flatlaf.FlatIntelliJLaf"));
					add(new ThemeEntity("Darcula", "com.formdev.flatlaf.FlatDarculaLaf"));
				}
			}));
//			add(new ThemeEntity("Swing", new ArrayList<>() {
//				@Serial
//				private static final long serialVersionUID = 4953002115699181721L;
//				{
//					add(new ThemeEntity("metal", "javax.swing.plaf.metal.MetalLookAndFeel"));
//				}
//			}));
			add(new ThemeEntity("Jtattoo", new ArrayList<>() {
				{
					add(new ThemeEntity("acryl", "com.jtattoo.plaf.acryl.AcrylLookAndFeel"));
					add(new ThemeEntity("aero", "com.jtattoo.plaf.aero.AeroLookAndFeel"));
					// add(new ThemeEntity("aluminium", "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"));
					add(new ThemeEntity("bernstein", "com.jtattoo.plaf.bernstein.BernsteinLookAndFeel"));
					add(new ThemeEntity("fast", "com.jtattoo.plaf.fast.FastLookAndFeel"));
					add(new ThemeEntity("graphite", "com.jtattoo.plaf.graphite.GraphiteLookAndFeel"));
					add(new ThemeEntity("hifi", "com.jtattoo.plaf.hifi.HiFiLookAndFeel"));
					add(new ThemeEntity("luna", "com.jtattoo.plaf.luna.LunaLookAndFeel"));
					// add(new ThemeEntity("mcwin", "com.jtattoo.plaf.mcwin.McWinLookAndFeel"));
					add(new ThemeEntity("mint", "com.jtattoo.plaf.mint.MintLookAndFeel"));
					add(new ThemeEntity("smart", "com.jtattoo.plaf.smart.SmartLookAndFeel"));
					add(new ThemeEntity("noire", "com.jtattoo.plaf.noire.NoireLookAndFeel"));
					add(new ThemeEntity("texture", "com.jtattoo.plaf.texture.TextureLookAndFeel"));
				}
			}));
		}
	};

}
