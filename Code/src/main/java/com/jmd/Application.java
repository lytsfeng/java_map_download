package com.jmd;

import java.io.PrintStream;

import javax.swing.*;

import com.jmd.common.StaticVar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jmd.ui.StartupWindow;

import lombok.Getter;

@SpringBootApplication
public class Application {

    @Getter
    private static JTextArea consoleTextArea;
    @Getter
    private static boolean startFinish = false;

    static {
        if (StaticVar.IS_Windows) {
            System.setProperty("sun.java2d.d3d", "true");
        } else if (StaticVar.IS_Mac) {
            System.setProperty("sun.java2d.metal", "true");
        }
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        // Print
        Application.print();
        // 加载界面主题
        try {
            UIManager.setLookAndFeel(ApplicationSetting.getSetting().getThemeClazz());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 加载启动界面
        SwingUtilities.invokeLater(() -> StartupWindow.getIstance().setVisible(true));
        ProgressBeanPostProcessor.observe().subscribe((result) -> {
            // 监听springboot启动进度
            SwingUtilities.invokeLater(() -> {
                StartupWindow.getIstance().getProgressLabel().setText("正在加载：" + result.getPerc() + "%");
                StartupWindow.getIstance().getBeanNameLabel().setText(result.getBeanName());
                StartupWindow.getIstance().getProgressBar().setValue(result.getPerc());
            });
        }, (e) -> {
        }, () -> {
            // springboot启动完成
            StartupWindow.getIstance().getProgressLabel().setText("加载完成：100%");
            StartupWindow.getIstance().getProgressBar().setValue(100);
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                startFinish = true;
            }).start();
        });
        // 重定向至ConsoleTextArea
        consoleTextArea = new JTextArea();
        JTextAreaOutputStream out = new JTextAreaOutputStream(consoleTextArea);
        System.setOut(new PrintStream(out)); // 设置输出重定向
        System.setErr(new PrintStream(out)); // 将错误输出也重定向,用于e.pritnStackTrace
        // 异步启动springboot核心
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                SpringApplication.run(Application.class, args);
                return null;
            }
        }.execute();

    }

    private static void print() {
        System.out.println("SpringBoot Version: " + SpringBootVersion.getVersion());
        System.out.println("User OS: " + System.getProperty("os.name"));
        System.out.println("Java Name: " + System.getProperty("java.vm.name"));
        System.out.println("Java Version: " + System.getProperty("java.vm.version"));
    }

}
