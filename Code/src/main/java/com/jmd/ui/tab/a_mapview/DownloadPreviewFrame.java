package com.jmd.ui.tab.a_mapview;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.entity.geo.Polygon;
import com.jmd.entity.task.TaskInstEntity;
import com.jmd.taskfunc.TaskStepFunc;

import lombok.extern.slf4j.Slf4j;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Slf4j
@Component
public class DownloadPreviewFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = 5621937977510720723L;

    @Autowired
    private SharedService sharedService;
    @Autowired
    private TaskStepFunc taskStepFunc;

    private boolean taskStop = false;

    private final DecimalFormat df2 = new DecimalFormat("#.##");
    private final String[] dw = {"B", "KB", "MB", "GB", "TB"};
    private SwingWorker<Void, Void> calculateWorker;

    private final HashMap<Integer, Integer> countMap = new HashMap<>();
    private List<Integer> zoomList = null;
    private List<Polygon> polygons = null;

    private JTextArea textArea;
    private JComboBox<String> imgTypeComboBox;
    private JLabel tileCountContentLabel;
    private JLabel downloadAmountContentLabel;
    private JLabel diskUsageContentLabel;
    private JLabel loadingTitleLabel;
    private JLabel loadingGifIconLabel;

//	public DownloadPreviewFrame() {
//		init();
//	}

    @PostConstruct
    private void init() {
        this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(DownloadConfigFrame.class.getResource("/com/jmd/assets/icon/java.png")));

        JScrollPane scrollPane = new JScrollPane();

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        scrollPane.setViewportView(textArea);

        JPanel panel = new JPanel();

        JLabel imgTypeTitleLabel = new JLabel("图片格式：");
        imgTypeTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        imgTypeComboBox = new JComboBox<>();
        imgTypeComboBox.setFocusable(false);
        imgTypeComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        imgTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"PNG", "JPG-低", "JPG-中"}));
        imgTypeComboBox.setSelectedIndex(0);
        imgTypeComboBox.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                changeImageType((String) e.getItem());
            }
        });

        JLabel diskBlockSizeLabel = new JLabel("硬盘簇大小（以NTFS为例）：" + StaticVar.DISK_BLOCK + "字节");
        diskBlockSizeLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        JLabel tileCountTitleLabel = new JLabel("瓦片图下载总数：");
        tileCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        tileCountContentLabel = new JLabel("正在计算...");
        tileCountContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        JLabel downloadAmountTitleLabel = new JLabel("预计下载总量：");
        downloadAmountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        downloadAmountContentLabel = new JLabel("正在计算...");
        downloadAmountContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        JLabel diskUsageTitleLabel = new JLabel("预计硬盘占用：");
        diskUsageTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        diskUsageContentLabel = new JLabel("正在计算...");
        diskUsageContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);

        loadingTitleLabel = new JLabel("正在计算...");
        loadingTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_14);
        loadingTitleLabel.setVisible(false);

        loadingGifIconLabel = new JLabel("");
        loadingGifIconLabel
                .setIcon(new ImageIcon(Objects.requireNonNull(DownloadPreviewFrame.class.getResource("/com/jmd/assets/icon/loading.gif"))));
        loadingGifIconLabel.setVisible(false);

        // Right panel layout
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
                .createSequentialGroup().addContainerGap()
                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_panel.createSequentialGroup().addComponent(imgTypeTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED).addComponent(
                                                        imgTypeComboBox, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addComponent(loadingGifIconLabel, GroupLayout.PREFERRED_SIZE, 32,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(loadingTitleLabel, GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(6))
                                        .addComponent(diskBlockSizeLabel))
                                .addContainerGap(28, Short.MAX_VALUE))
                        .addGroup(
                                gl_panel.createSequentialGroup().addComponent(tileCountTitleLabel)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(tileCountContentLabel, GroupLayout.DEFAULT_SIZE, 160,
                                                Short.MAX_VALUE)
                                        .addContainerGap())
                        .addGroup(gl_panel.createSequentialGroup().addComponent(downloadAmountTitleLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(downloadAmountContentLabel, GroupLayout.DEFAULT_SIZE, 174,
                                        Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(gl_panel.createSequentialGroup().addComponent(diskUsageTitleLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(diskUsageContentLabel, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                .addContainerGap()))));
        gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
                .createSequentialGroup().addContainerGap()
                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(imgTypeTitleLabel).addComponent(
                        imgTypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(diskBlockSizeLabel)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(tileCountTitleLabel)
                        .addComponent(tileCountContentLabel))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(downloadAmountTitleLabel)
                        .addComponent(downloadAmountContentLabel))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(diskUsageTitleLabel)
                        .addComponent(diskUsageContentLabel))
                .addPreferredGap(ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(loadingTitleLabel, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                        .addComponent(loadingGifIconLabel, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                .addContainerGap()));
        panel.setLayout(gl_panel);

        // This frame layout
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE).addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE));

        getContentPane().setLayout(groupLayout);

        this.setTitle("预估下载量");
        this.setSize(new Dimension(650, 383));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
        this.setVisible(false);
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (calculateWorker != null) {
                    taskStop = true;
                    calculateWorker.cancel(true);
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
    }

    public void showPreview(List<Integer> zoomList, List<Polygon> polygon) {
        this.setVisible(true);
        this.clear();
        this.calculate(zoomList, polygon);
        this.taskStop = false;
    }

    private void clear() {
        this.textArea.setText("");
        this.tileCountContentLabel.setText("");
        this.downloadAmountContentLabel.setText("");
        this.diskUsageContentLabel.setText("");
        this.loadingTitleLabel.setVisible(false);
        this.loadingGifIconLabel.setVisible(false);
        this.countMap.clear();
        this.zoomList = null;
        this.polygons = null;
    }

    private void calculate(List<Integer> _zoomList, List<Polygon> _polygons) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                loadingTitleLabel.setVisible(true);
                loadingGifIconLabel.setVisible(true);
                zoomList = _zoomList;
                polygons = _polygons;
                countMap.clear();
                consoleLog("开始计算...");
                int count = 0;
                long start = System.currentTimeMillis();
                for (int z : zoomList) {
                    if (taskStop) {
                        break;
                    } else {
                        try {
                            TaskInstEntity inst = taskStepFunc.tileTaskInstCalculation(z, polygons, (e) -> consoleLog(e));
                            countMap.put(z, inst.getRealCount());
                            count = count + inst.getRealCount();
                        } catch (InterruptedException | ExecutionException e) {
                            log.error("Tile Calculation Error", e);
                        }
                    }
                }
                long end = System.currentTimeMillis();
                double time = (double) (end - start) / 1000.0;
                consoleLog("计算完成");
                consoleLog("需要下载的总数：" + count);
                consoleLog("瓦片图计算所用时间：" + df2.format(time) + "秒");
                consoleLog("结果仅供参考");
                tileCountContentLabel.setText(String.valueOf(count));
                loadingTitleLabel.setVisible(false);
                loadingGifIconLabel.setVisible(false);
                changeImageType((String) imgTypeComboBox.getSelectedItem());
                calculateWorker = null;
                return null;
            }
        };
        worker.execute();
        calculateWorker = worker;
    }

    private void changeImageType(String type) {
        if (zoomList == null || countMap.size() == 0) {
            return;
        }
        switch (type) {
            case "PNG" -> {
                downloadAmountContentLabel.setText(getTileSizeAmount(zoomList, StaticVar.PNG_PER_SIZE_MAP, countMap));
                diskUsageContentLabel.setText(
                        getTileDiskUsageAmount(zoomList, StaticVar.PNG_PER_SIZE_MAP, countMap, StaticVar.DISK_BLOCK));
            }
            case "JPG-低" -> {
                downloadAmountContentLabel.setText(getTileSizeAmount(zoomList, StaticVar.JPG_LOW_PER_SIZE_MAP, countMap));
                diskUsageContentLabel.setText(
                        getTileDiskUsageAmount(zoomList, StaticVar.JPG_LOW_PER_SIZE_MAP, countMap, StaticVar.DISK_BLOCK));
            }
            case "JPG-中" -> {
                downloadAmountContentLabel
                        .setText(getTileSizeAmount(zoomList, StaticVar.JPG_MIDDLE_PER_SIZE_MAP, countMap));
                diskUsageContentLabel.setText(getTileDiskUsageAmount(zoomList, StaticVar.JPG_MIDDLE_PER_SIZE_MAP, countMap,
                        StaticVar.DISK_BLOCK));
            }
            default -> {
            }
        }
    }

    private String getTileSizeAmount(List<Integer> zoomList, Map<Integer, Double> perSizeMap,
                                     Map<Integer, Integer> countMap) {
        String dx = null;
        double allSize = 0.0;
        for (int z : zoomList) {
            allSize = allSize + perSizeMap.get(z) * countMap.get(z);
        }
        for (int i = 0; i <= 4; i++) {
            dx = dw[i];
            if (allSize < 1024) {
                break;
            } else {
                allSize = allSize / 1024;
            }
        }
        return df2.format(allSize) + dx;
    }

    private String getTileDiskUsageAmount(List<Integer> zoomList, Map<Integer, Double> perSizeMap,
                                          Map<Integer, Integer> countMap, double diskBlock) {
        diskBlock = diskBlock * 0.8; // 更接近真实数据
        String dx = null;
        double allSize = 0.0;
        for (int z : zoomList) {
            double perAvg = perSizeMap.get(z);
            double eachUsage = 0.0;
            while (true) {
                eachUsage = eachUsage + diskBlock;
                if (perAvg % diskBlock < perAvg) {
                    perAvg = perAvg - diskBlock;
                } else {
                    break;
                }
            }
            allSize = allSize + eachUsage * countMap.get(z);
        }
        for (int i = 0; i <= 4; i++) {
            dx = dw[i];
            if (allSize < 1024) {
                break;
            } else {
                allSize = allSize / 1024;
            }
        }
        return df2.format(allSize) + dx;
    }

    /**
     * 控制台打印
     */
    private void consoleLog(String log) {
        textArea.append(log + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

}
