package com.jmd.ui.tab.b_download;

import javax.swing.JPanel;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import com.jmd.taskfunc.TaskState;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.taskfunc.TaskExecFunc;
import com.jmd.ui.tab.b_download.merge.TileMergeProgressPanel;
import com.jmd.ui.tab.b_download.task.TaskStatusPanel;
import com.jmd.ui.tab.b_download.usage.ResourceUsagePanel;

import javax.swing.JProgressBar;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import java.io.Serial;
import java.text.SimpleDateFormat;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class DownloadTaskPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 7654815968814511149L;

    @Autowired
    private SharedService sharedService;

    @Autowired
    private TaskExecFunc taskExec;
    @Autowired
    private TaskStatusPanel taskStatusPanel;
    @Autowired
    private ResourceUsagePanel resourceUsagePanel;
    @Autowired
    private TileMergeProgressPanel tileMergeProgressPanel;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JButton pauseButton;
    private JButton cancelButton;
    private JTextArea logTextArea;
    private JProgressBar progressBar;

//	public DownloadTaskPanel() {
//		init();
//	}

    @PostConstruct
    private void init() {

        /* 任务状态 */
        JPanel taskPanel = new JPanel();
        taskPanel.setBorder(new TitledBorder(null, "\u4EFB\u52A1\u72B6\u6001", TitledBorder.LEADING, TitledBorder.TOP,
                StaticVar.FONT_SourceHanSansCNNormal_12, null));
        taskPanel.setLayout(new BorderLayout());
        taskPanel.add(taskStatusPanel, BorderLayout.CENTER);
        /* 任务状态 */

        /* 资源使用量 */
        JPanel usagePanel = new JPanel();
        usagePanel.setBorder(new TitledBorder(null, "\u8D44\u6E90\u4F7F\u7528\u91CF", TitledBorder.LEADING,
                TitledBorder.TOP, StaticVar.FONT_SourceHanSansCNNormal_12, null));
        usagePanel.setLayout(new BorderLayout());
        usagePanel.add(resourceUsagePanel, BorderLayout.CENTER);
        /* 资源使用量 */

        /* 瓦片图合并进度 */
        JPanel mergePanel = new JPanel();
        mergePanel.setBorder(new TitledBorder(null, "\u74E6\u7247\u56FE\u5408\u5E76\u8FDB\u5EA6", TitledBorder.LEADING,
                TitledBorder.TOP, StaticVar.FONT_SourceHanSansCNNormal_12, null));
        mergePanel.setLayout(new BorderLayout());
        mergePanel.add(tileMergeProgressPanel, BorderLayout.CENTER);
        /* 瓦片图合并进度 */

        /* 任务日志 */
        JPanel logPanel = new JPanel();
        logPanel.setBorder(new TitledBorder(null, "\u4EFB\u52A1\u65E5\u5FD7", TitledBorder.LEADING, TitledBorder.TOP,
                StaticVar.FONT_SourceHanSansCNNormal_12, null));
        logPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane logScrollPane = new JScrollPane();
        logPanel.add(logScrollPane, BorderLayout.CENTER);
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        logScrollPane.setViewportView(logTextArea);
        /* 任务日志 */

        /* 下载进度 */
        JPanel progressPanel = new JPanel();
        progressPanel.setBorder(new TitledBorder(null, "\u4E0B\u8F7D\u8FDB\u5EA6", TitledBorder.LEADING,
                TitledBorder.TOP, StaticVar.FONT_SourceHanSansCNNormal_12, null));
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        GroupLayout gl_progressPanel = new GroupLayout(progressPanel);
        gl_progressPanel.setHorizontalGroup(gl_progressPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_progressPanel.createSequentialGroup().addContainerGap()
                        .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE).addContainerGap()));
        gl_progressPanel.setVerticalGroup(gl_progressPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_progressPanel.createSequentialGroup().addContainerGap()
                        .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(163, Short.MAX_VALUE)));
        progressPanel.setLayout(gl_progressPanel);
        /* 下载进度 */

        pauseButton = new JButton("暂停任务");
        pauseButton.setEnabled(false);
        pauseButton.setFocusable(false);
        pauseButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        pauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && pauseButton.isEnabled()) {
                    if (!TaskState.IS_TASKING) {
                        return;
                    }
                    taskExec.pauseTask();
                }
            }
        });

        cancelButton = new JButton("取消任务");
        cancelButton.setEnabled(false);
        cancelButton.setFocusable(false);
        cancelButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && cancelButton.isEnabled()) {
                    if (!TaskState.IS_TASKING) {
                        return;
                    }
                    int n = JOptionPane.showConfirmDialog(null, "是否取消当前任务");
                    if (n == JOptionPane.YES_OPTION) {
                        taskExec.cancelTaks();
                    }
                }
            }
        });

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(logPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
                                        .addComponent(progressPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(taskPanel, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(usagePanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(mergePanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(pauseButton)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(cancelButton)))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(usagePanel, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(mergePanel, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(taskPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(logPanel, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(progressPanel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(cancelButton)
                                        .addComponent(pauseButton))
                                .addContainerGap())
        );
        this.setLayout(groupLayout);
        this.subShared();

    }

    private void subShared() {
        sharedService.sub(SharedType.DOWNLOAD_CONSOLE_CLEAR).subscribe((res) -> {
            clearConsole();
        });
        sharedService.sub(SharedType.DOWNLOAD_CONSOLE_PROGRESS).subscribe((res) -> {
            progressBar.setValue((int) res);
        });
        sharedService.sub(SharedType.DOWNLOAD_CONSOLE_LOG).subscribe((res) -> {
            consoleLog((String) res);
        });
        sharedService.sub(SharedType.DOWNLOAD_CONSOLE_CANCEL_BUTTON_TEXT).subscribe((res) -> {
            cancelButton.setText((String) res);
        });
        sharedService.sub(SharedType.DOWNLOAD_CONSOLE_CANCEL_BUTTON_STATE).subscribe((res) -> {
            cancelButton.setEnabled((boolean) res);
        });
        sharedService.sub(SharedType.DOWNLOAD_CONSOLE_PAUSE_BUTTON_TEXT).subscribe((res) -> {
            pauseButton.setText((String) res);
        });
        sharedService.sub(SharedType.DOWNLOAD_CONSOLE_PAUSE_BUTTON_STATE).subscribe((res) -> {
            pauseButton.setEnabled((boolean) res);
        });
    }

    /**
     * 控制台打印
     */
    public void consoleLog(String log) {
        String time = "[" + sdf.format(System.currentTimeMillis()) + "]";
        String content = time + " " + log;
        logTextArea.append(content + "\n");
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }

    /**
     * 控制台清空
     */
    public void clearConsole() {
        logTextArea.setText("");
    }
}
