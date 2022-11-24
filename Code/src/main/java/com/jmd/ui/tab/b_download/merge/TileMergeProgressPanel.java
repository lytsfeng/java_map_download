package com.jmd.ui.tab.b_download.merge;

import java.awt.BorderLayout;
import javax.annotation.PostConstruct;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;

@Component
public class TileMergeProgressPanel extends JPanel {

    @Autowired
    private SharedService sharedService;

    private JLabel pixelCountValueLabel;
    private JLabel threadCountValueLabel;
    private JLabel mergeProgressValueLabel;

//	public TileMergeProgressPanel() {
//		init();
//	}

    @PostConstruct
    private void init() {

        /* label */
        JPanel tablePanel = new JPanel();

        JLabel pixelCountTitleLabel = new JLabel("像素数量：");
        pixelCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        pixelCountValueLabel = new JLabel("0/0");
        pixelCountValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        JLabel threadCountTitleLabel = new JLabel("任务线程数：");
        threadCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        threadCountValueLabel = new JLabel("0");
        threadCountValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        JLabel mergeProgressTitleLabel = new JLabel("合并进度：");
        mergeProgressTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        mergeProgressValueLabel = new JLabel("0.00%");
        mergeProgressValueLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        GroupLayout gl_tablePanel = new GroupLayout(tablePanel);
        gl_tablePanel.setHorizontalGroup(
                gl_tablePanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_tablePanel.createSequentialGroup()
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_tablePanel.createSequentialGroup()
                                                .addComponent(pixelCountTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(pixelCountValueLabel))
                                        .addGroup(gl_tablePanel.createSequentialGroup()
                                                .addComponent(threadCountTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(threadCountValueLabel))
                                        .addGroup(gl_tablePanel.createSequentialGroup()
                                                .addComponent(mergeProgressTitleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(mergeProgressValueLabel)))
                                .addContainerGap(40, Short.MAX_VALUE))
        );
        gl_tablePanel.setVerticalGroup(
                gl_tablePanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_tablePanel.createSequentialGroup()
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(pixelCountTitleLabel)
                                        .addComponent(pixelCountValueLabel))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(threadCountTitleLabel)
                                        .addComponent(threadCountValueLabel))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(mergeProgressTitleLabel)
                                        .addComponent(mergeProgressValueLabel))
                                .addContainerGap(18, Short.MAX_VALUE))
        );
        tablePanel.setLayout(gl_tablePanel);
        /* label */

        /* 折线图 */
        JPanel mergePercPanel = new JPanel();
        mergePercPanel.setLayout(new BorderLayout(0, 0));
        /* 折线图 */

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(mergePercPanel, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addContainerGap(0, Short.MAX_VALUE)
                                                .addComponent(tablePanel, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(mergePercPanel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(180, Short.MAX_VALUE))
        );
        this.setLayout(groupLayout);
        this.subShared();

    }

    private void subShared() {
        sharedService.sub(SharedType.TILE_MERGE_PROCESS_PIXEL_COUNT).subscribe((res) -> {
            pixelCountValueLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TILE_MERGE_PROCESS_THREAD).subscribe((res) -> {
            threadCountValueLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TILE_MERGE_PROCESS_PROGRESS).subscribe((res) -> {
            mergeProgressValueLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TILE_MERGE_PROCESS_CLEAR).subscribe((res) -> {
            pixelCountValueLabel.setText("0/0");
            threadCountValueLabel.setText("0");
            mergeProgressValueLabel.setText("0.00%");
        });
    }

}
