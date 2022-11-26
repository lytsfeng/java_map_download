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
            mainFrame.setVisible(true);
            StartupWindow.getIstance().close();
        });
        // new Thread(() -> test.run()).start();
    }

}
