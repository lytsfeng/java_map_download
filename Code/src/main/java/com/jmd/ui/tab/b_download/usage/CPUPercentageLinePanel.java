package com.jmd.ui.tab.b_download.usage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serial;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CPUPercentageLinePanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -910619749930001376L;

    @Autowired
    private SharedService sharedService;

    private final double[] systemCpuUsage = new double[60];
    private final double[] processCpuUsage = new double[60];

    @PostConstruct
    private void init() {
        this.setForeground(Color.CYAN);
        this.setBorder(new LineBorder(new Color(128, 128, 128)));
        this.setBounds(260, 17, 120, 100);
        this.setLayout(null);
        this.subShared();
    }

    private void subShared() {
        sharedService.sub(SharedType.CPU_PERCENTAGE_DRAW_SYSTEM).subscribe((res) -> {
            this.drawCpuSystemUsage((double) res);
        });
        sharedService.sub(SharedType.CPU_PERCENTAGE_DRAW_PROCESS).subscribe((res) -> {
            this.drawCpuProcessUsage((double) res);
        });
        sharedService.sub(SharedType.CPU_PERCENTAGE_CLEAR).subscribe((res) -> {
            clear();
        });
    }

    private void drawCpuSystemUsage(double currentSystemCpuUsage) {
        for (int i = 0; i < 59; i++) {
            systemCpuUsage[i] = systemCpuUsage[i + 1];
        }
        systemCpuUsage[59] = currentSystemCpuUsage;
        this.repaint();
    }

    private void drawCpuProcessUsage(double currentProcessCpuUsage) {
        for (int i = 0; i < 59; i++) {
            processCpuUsage[i] = processCpuUsage[i + 1];
        }
        processCpuUsage[59] = currentProcessCpuUsage;
        this.repaint();
    }

    private void clear() {
        for (int i = 0; i < 59; i++) {
            systemCpuUsage[i] = 0.0;
            processCpuUsage[i] = 0.0;
        }
        this.repaint();
    }

    @Override
    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
        int x = 0;
        for (int i = 0; i < 59; i++) {
            int x1 = x;
            int x2 = x + 2;
            int height = 120;
            int ys1 = (int) Math.round(height * (1 - systemCpuUsage[i]));
            int yp1 = (int) Math.round(height * (1 - processCpuUsage[i]));
            int ys2 = (int) Math.round(height * (1 - systemCpuUsage[i + 1]));
            int yp2 = (int) Math.round(height * (1 - processCpuUsage[i + 1]));
            g.setColor(new Color(51, 102, 204));
            g.drawLine(x1, ys1, x2, ys2);
            g.setColor(new Color(255, 102, 0));
            g.drawLine(x1, yp1, x2, yp2);
            x = x + 2;
        }
    }

}
