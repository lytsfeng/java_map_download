package com.jmd.ui.tab.b_download.task;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.annotation.PostConstruct;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;

@Component
public class TaskStatusPanel extends JPanel {

    @Autowired
    private SharedService sharedService;

    private JLabel currentContentLabel;
    private JLabel mapTypeContentLabel;
    private JLabel layersContentLabel;
    private JLabel imgTypeContentLabel;
    private JLabel savePathContentLabel;
    private JLabel pathStyleContentLabel;
    private JLabel isCoverExistContentLabel;
    private JLabel tileAllCountContentLabel;
    private JLabel tileDownloadedCountContentLabel;
    private JLabel progressContentLabel;

//	public TaskStatusPanel() {
//		init();
//	}

    @PostConstruct
    private void init() {

        GridBagLayout gbl_this = new GridBagLayout();
        gbl_this.columnWidths = new int[]{0, 0, 0, 0};
        gbl_this.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_this.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_this.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        this.setLayout(gbl_this);

        /* 当前任务 */
        JLabel currentTitleLabel = new JLabel("当前任务：");
        currentTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_currentTitleLabel = new GridBagConstraints();
        gbc_currentTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_currentTitleLabel.gridx = 0;
        gbc_currentTitleLabel.gridy = 0;
        this.add(currentTitleLabel, gbc_currentTitleLabel);

        currentContentLabel = new JLabel("");
        currentContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_currentContentLabel = new GridBagConstraints();
        gbc_currentContentLabel.anchor = GridBagConstraints.WEST;
        gbc_currentContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_currentContentLabel.gridx = 1;
        gbc_currentContentLabel.gridy = 0;
        this.add(currentContentLabel, gbc_currentContentLabel);
        /* 当前任务 */

        /* 地图类型 */
        JLabel mapTypeTitleLabel = new JLabel("地图类型：");
        mapTypeTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_mapTypeTitleLabel = new GridBagConstraints();
        gbc_mapTypeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_mapTypeTitleLabel.gridx = 0;
        gbc_mapTypeTitleLabel.gridy = 1;
        this.add(mapTypeTitleLabel, gbc_mapTypeTitleLabel);

        mapTypeContentLabel = new JLabel("");
        mapTypeContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_mapTypeContentLabel = new GridBagConstraints();
        gbc_mapTypeContentLabel.anchor = GridBagConstraints.WEST;
        gbc_mapTypeContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_mapTypeContentLabel.gridx = 1;
        gbc_mapTypeContentLabel.gridy = 1;
        this.add(mapTypeContentLabel, gbc_mapTypeContentLabel);
        /* 地图类型 */

        /* 所选图层 */
        JLabel layersTitleLabel = new JLabel("所选图层：");
        layersTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_layersTitleLabel = new GridBagConstraints();
        gbc_layersTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_layersTitleLabel.gridx = 0;
        gbc_layersTitleLabel.gridy = 2;
        this.add(layersTitleLabel, gbc_layersTitleLabel);

        layersContentLabel = new JLabel("");
        layersContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_layersContentLabel = new GridBagConstraints();
        gbc_layersContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_layersContentLabel.anchor = GridBagConstraints.WEST;
        gbc_layersContentLabel.gridx = 1;
        gbc_layersContentLabel.gridy = 2;
        this.add(layersContentLabel, gbc_layersContentLabel);
        /* 所选图层 */

        /* 瓦片格式 */
        JLabel imgTypeTitleLabel = new JLabel("瓦片格式：");
        imgTypeTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_imgTypeTitleLabel = new GridBagConstraints();
        gbc_imgTypeTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_imgTypeTitleLabel.gridx = 0;
        gbc_imgTypeTitleLabel.gridy = 3;
        this.add(imgTypeTitleLabel, gbc_imgTypeTitleLabel);

        imgTypeContentLabel = new JLabel("");
        imgTypeContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_imgTypeContentLabel = new GridBagConstraints();
        gbc_imgTypeContentLabel.anchor = GridBagConstraints.WEST;
        gbc_imgTypeContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_imgTypeContentLabel.gridx = 1;
        gbc_imgTypeContentLabel.gridy = 3;
        this.add(imgTypeContentLabel, gbc_imgTypeContentLabel);
        /* 瓦片格式 */

        /* 保存路径 */
        JLabel savePathTitleLabel = new JLabel("保存路径：");
        savePathTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_savePathTitleLabel = new GridBagConstraints();
        gbc_savePathTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_savePathTitleLabel.gridx = 0;
        gbc_savePathTitleLabel.gridy = 4;
        this.add(savePathTitleLabel, gbc_savePathTitleLabel);

        savePathContentLabel = new JLabel("");
        savePathContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_savePathContentLabel = new GridBagConstraints();
        gbc_savePathContentLabel.anchor = GridBagConstraints.WEST;
        gbc_savePathContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_savePathContentLabel.gridx = 1;
        gbc_savePathContentLabel.gridy = 4;
        this.add(savePathContentLabel, gbc_savePathContentLabel);
        /* 保存路径 */

        /* 命名风格 */
        JLabel pathStyleTitleLabel = new JLabel("命名风格：");
        pathStyleTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_pathStyleTitleLabel = new GridBagConstraints();
        gbc_pathStyleTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_pathStyleTitleLabel.gridx = 0;
        gbc_pathStyleTitleLabel.gridy = 5;
        this.add(pathStyleTitleLabel, gbc_pathStyleTitleLabel);

        pathStyleContentLabel = new JLabel("");
        pathStyleContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_pathStyleContentLabel = new GridBagConstraints();
        gbc_pathStyleContentLabel.anchor = GridBagConstraints.WEST;
        gbc_pathStyleContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_pathStyleContentLabel.gridx = 1;
        gbc_pathStyleContentLabel.gridy = 5;
        this.add(pathStyleContentLabel, gbc_pathStyleContentLabel);
        /* 命名风格 */

        /* 覆盖下载 */
        JLabel isCoverExistTitleLabel = new JLabel("覆盖下载：");
        isCoverExistTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_isCoverExistTitleLabel = new GridBagConstraints();
        gbc_isCoverExistTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_isCoverExistTitleLabel.gridx = 0;
        gbc_isCoverExistTitleLabel.gridy = 6;
        this.add(isCoverExistTitleLabel, gbc_isCoverExistTitleLabel);

        isCoverExistContentLabel = new JLabel("");
        isCoverExistContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_isCoverExistContentLabel = new GridBagConstraints();
        gbc_isCoverExistContentLabel.anchor = GridBagConstraints.WEST;
        gbc_isCoverExistContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_isCoverExistContentLabel.gridx = 1;
        gbc_isCoverExistContentLabel.gridy = 6;
        this.add(isCoverExistContentLabel, gbc_isCoverExistContentLabel);
        /* 覆盖下载 */

        /* 瓦片总数 */
        JLabel tileAllCountTitleLabel = new JLabel("瓦片总数：");
        tileAllCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_tileAllCountTitleLabel = new GridBagConstraints();
        gbc_tileAllCountTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_tileAllCountTitleLabel.gridx = 0;
        gbc_tileAllCountTitleLabel.gridy = 7;
        this.add(tileAllCountTitleLabel, gbc_tileAllCountTitleLabel);

        tileAllCountContentLabel = new JLabel("");
        tileAllCountContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_tileAllCountContentLabel = new GridBagConstraints();
        gbc_tileAllCountContentLabel.anchor = GridBagConstraints.WEST;
        gbc_tileAllCountContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_tileAllCountContentLabel.gridx = 1;
        gbc_tileAllCountContentLabel.gridy = 7;
        this.add(tileAllCountContentLabel, gbc_tileAllCountContentLabel);
        /* 瓦片总数 */

        /* 已下载数 */
        JLabel tileDownloadedCountTitleLabel = new JLabel("已下载数：");
        tileDownloadedCountTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_tileDownloadedCountTitleLabel = new GridBagConstraints();
        gbc_tileDownloadedCountTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_tileDownloadedCountTitleLabel.gridx = 0;
        gbc_tileDownloadedCountTitleLabel.gridy = 8;
        this.add(tileDownloadedCountTitleLabel, gbc_tileDownloadedCountTitleLabel);

        tileDownloadedCountContentLabel = new JLabel("");
        tileDownloadedCountContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_tileDownloadedCountContentLabel = new GridBagConstraints();
        gbc_tileDownloadedCountContentLabel.anchor = GridBagConstraints.WEST;
        gbc_tileDownloadedCountContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_tileDownloadedCountContentLabel.gridx = 1;
        gbc_tileDownloadedCountContentLabel.gridy = 8;
        this.add(tileDownloadedCountContentLabel, gbc_tileDownloadedCountContentLabel);
        /* 已下载数 */

        /* 下载进度 */
        JLabel progressTitleLabel = new JLabel("下载进度：");
        progressTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_progressTitleLabel = new GridBagConstraints();
        gbc_progressTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_progressTitleLabel.gridx = 0;
        gbc_progressTitleLabel.gridy = 9;
        this.add(progressTitleLabel, gbc_progressTitleLabel);

        progressContentLabel = new JLabel("");
        progressContentLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        GridBagConstraints gbc_progressContentLabel = new GridBagConstraints();
        gbc_progressContentLabel.insets = new Insets(0, 0, 5, 0);
        gbc_progressContentLabel.anchor = GridBagConstraints.WEST;
        gbc_progressContentLabel.gridx = 1;
        gbc_progressContentLabel.gridy = 9;
        this.add(progressContentLabel, gbc_progressContentLabel);
        /* 下载进度 */

        this.subShared();

    }

    private void subShared() {
        sharedService.sub(SharedType.TASK_STATUS_CURRENT).subscribe((res) -> {
            currentContentLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TASK_STATUS_MAP_TYPE).subscribe((res) -> {
            mapTypeContentLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TASK_STATUS_LAYERS).subscribe((res) -> {
            layersContentLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TASK_STATUS_SAVE_PATH).subscribe((res) -> {
            savePathContentLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TASK_STATUS_PATH_STYLE).subscribe((res) -> {
            pathStyleContentLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TASK_STATUS_IS_COVER_EXIST).subscribe((res) -> {
            isCoverExistContentLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TASK_STATUS_IMG_TYPE).subscribe((res) -> {
            imgTypeContentLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TASK_STATUS_TILE_ALL_COUNT).subscribe((res) -> {
            tileAllCountContentLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TASK_STATUS_TILE_DOWNLOADED_COUNT).subscribe((res) -> {
            tileDownloadedCountContentLabel.setText((String) res);
        });
        sharedService.sub(SharedType.TASK_STATUS_PROGRESS).subscribe((res) -> {
            progressContentLabel.setText((String) res);
        });
    }

}
