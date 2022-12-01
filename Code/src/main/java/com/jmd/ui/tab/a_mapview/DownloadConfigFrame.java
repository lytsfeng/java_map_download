package com.jmd.ui.tab.a_mapview;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import com.jmd.taskfunc.TaskState;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.entity.geo.Polygon;
import com.jmd.entity.task.TaskAllInfoEntity;
import com.jmd.entity.task.TaskCreateEntity;
import com.jmd.taskfunc.TaskExecFunc;
import com.jmd.ui.MainFrame;
import com.jmd.util.TaskUtils;

import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import java.awt.GridBagConstraints;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@Component
public class DownloadConfigFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = 394597878094632313L;

    @Autowired
    private SharedService sharedService;
    @Autowired
    private TaskExecFunc taskExec;
    @Autowired
    private MainFrame mainFrame;
    @Autowired
    private DownloadPreviewFrame downloadPreviewFrame;

    private final int minZoom = 0;
    private final int maxZoom = 21;
    private String url;
    private List<Polygon> polygons;
    private String tileName;
    private String mapType;
    private final ArrayList<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
    private JComboBox<String> imgTypeComboBox;
    private JComboBox<String> pathStyleDefaultComboBox;
    private JCheckBox isCoverCheckBox;
    private JCheckBox mergeTileCheckBox;
    //	private JCheckBox isSqliteCheckBox;
    private String lastDirPath;
    private File selectedPath;
    private JCheckBox selectAllCheckBox;
    private JTextArea pathTextArea;
    private JButton previewButton;
    private JButton downloadButton;

//	public DownloadConfigFrame() {
//		init();
//	}

    @PostConstruct
    private void init() {

        this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(DownloadConfigFrame.class.getResource("/com/jmd/assets/icon/java.png")));
        getContentPane().setLayout(null);

        JPanel layerPanel = new JPanel();
        layerPanel.setBorder(new TitledBorder(null, "等级选择", TitledBorder.LEADING, TitledBorder.TOP,
                StaticVar.FONT_SourceHanSansCNNormal_13, null));
        layerPanel.setBounds(10, 10, 150, 306);
        getContentPane().add(layerPanel);
        layerPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane layerScrollPane = new JScrollPane();
        layerPanel.add(layerScrollPane, BorderLayout.CENTER);

        JPanel layerPanelContent = new JPanel();
        layerScrollPane.setViewportView(layerPanelContent);

        GridBagLayout gbl_layerPanelContent = new GridBagLayout();
        gbl_layerPanelContent.columnWidths = new int[]{0, 0, 0, 0};
        gbl_layerPanelContent.rowHeights = new int[]{0, 0, 0, 0};
        gbl_layerPanelContent.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_layerPanelContent.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        layerPanelContent.setLayout(gbl_layerPanelContent);

        selectAllCheckBox = new JCheckBox();
        selectAllCheckBox.setFocusable(false);
        GridBagConstraints gbc_selectAllCheckBox = new GridBagConstraints();
        gbc_selectAllCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_selectAllCheckBox.gridx = 0;
        gbc_selectAllCheckBox.gridy = 0;
        layerPanelContent.add(selectAllCheckBox, gbc_selectAllCheckBox);
        selectAllCheckBox.addActionListener((e) -> {
            if (selectAllCheckBox.isSelected()) {
                for (int i = 0; i < checkBoxList.size(); i++) {
                    checkBoxList.get(i).setSelected(true);
                }
                previewButton.setEnabled(true);
                if (selectedPath != null) {
                    downloadButton.setEnabled(true);
                }
            } else {
                for (int i = 0; i < checkBoxList.size(); i++) {
                    checkBoxList.get(i).setSelected(false);
                }
                previewButton.setEnabled(false);
                downloadButton.setEnabled(false);
            }
        });

        JLabel layerTitleLabel = new JLabel("等级");
        layerTitleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        layerTitleLabel.setFocusable(false);
        GridBagConstraints gbc_layerTitleLabel = new GridBagConstraints();
        gbc_layerTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_layerTitleLabel.gridx = 1;
        gbc_layerTitleLabel.gridy = 0;
        layerPanelContent.add(layerTitleLabel, gbc_layerTitleLabel);

        for (int i = minZoom; i <= maxZoom; i++) {
            JCheckBox layerEachCheckBox = new JCheckBox("");
            layerEachCheckBox.setFocusable(false);
            GridBagConstraints gbc_layerEachCheckBox = new GridBagConstraints();
            gbc_layerEachCheckBox.insets = new Insets(0, 0, 5, 5);
            gbc_layerEachCheckBox.gridx = 0;
            gbc_layerEachCheckBox.gridy = i - minZoom + 1;
            layerPanelContent.add(layerEachCheckBox, gbc_layerEachCheckBox);
            checkBoxList.add(layerEachCheckBox);
            layerEachCheckBox.addActionListener((e) -> {
                if (layerEachCheckBox.isSelected()) {
                    previewButton.setEnabled(true);
                    if (selectedPath != null) {
                        downloadButton.setEnabled(true);
                    }
                    if (isCheckBoxAllSelected()) {
                        selectAllCheckBox.setSelected(true);
                    }
                } else {
                    selectAllCheckBox.setSelected(false);
                }
                if (isCheckBoxNoneSelected()) {
                    previewButton.setEnabled(false);
                    downloadButton.setEnabled(false);
                }
            });

            JLabel layerEachLabel = new JLabel(String.valueOf(i));
            layerEachLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
            GridBagConstraints gbc_layerEachLabel = new GridBagConstraints();
            gbc_layerEachLabel.insets = new Insets(0, 0, 5, 5);
            gbc_layerEachLabel.gridx = 1;
            gbc_layerEachLabel.gridy = i - minZoom + 1;
            layerPanelContent.add(layerEachLabel, gbc_layerEachLabel);
        }

        /* 其他设置 */
        JPanel configPanel = new JPanel();
        configPanel.setBorder(new TitledBorder(null, "其他设置", TitledBorder.LEADING, TitledBorder.TOP,
                StaticVar.FONT_SourceHanSansCNNormal_13, null));
        configPanel.setBounds(170, 10, 271, 306);
        getContentPane().add(configPanel);

        JLabel imgTypeLabel = new JLabel("图片格式：");
        imgTypeLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        imgTypeComboBox = new JComboBox<String>();
        imgTypeComboBox.setFocusable(false);
        imgTypeComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        imgTypeComboBox.setModel(new DefaultComboBoxModel<String>(new String[]{"PNG", "JPG-低", "JPG-中"}));
        imgTypeComboBox.setSelectedIndex(0);

        JLabel pathStyleLabel = new JLabel("命名风格：");
        pathStyleLabel.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        JRadioButton pathStyleRadioButton1 = new JRadioButton("预设");
        pathStyleRadioButton1.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        pathStyleRadioButton1.setFocusable(false);
        pathStyleRadioButton1.setSelected(true);

        JRadioButton pathStyleRadioButton2 = new JRadioButton("自定义");
        pathStyleRadioButton2.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        pathStyleRadioButton2.setFocusable(false);

        ButtonGroup pathStyleBtnGroup = new ButtonGroup();
        pathStyleBtnGroup.add(pathStyleRadioButton1);
        pathStyleBtnGroup.add(pathStyleRadioButton2);

        pathStyleDefaultComboBox = new JComboBox<String>();
        pathStyleDefaultComboBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        pathStyleDefaultComboBox.setFocusable(false);
        pathStyleDefaultComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{
                        "/{z}/{x}/{y}/x={x}&y={y}&z={z}.[png or jpg]",
                        "/{z}/{x}/{y}/tile.[png or jpg]",
                        "/{z}/{y}/{x}.[png or jpg]",
                        "/{z}/{x}/{y}.[png or jpg]"
                })
        );
        pathStyleDefaultComboBox.setSelectedIndex(1);

        isCoverCheckBox = new JCheckBox("覆盖已下载的瓦片");
        isCoverCheckBox.setSelected(true);
        isCoverCheckBox.setFocusable(false);
        isCoverCheckBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

        mergeTileCheckBox = new JCheckBox("合并下载的瓦片");
        mergeTileCheckBox.setFocusable(false);
        mergeTileCheckBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);

//		isSqliteCheckBox = new JCheckBox("使用sqlite存储瓦片");
//		isSqliteCheckBox.setFocusable(false);
//		isSqliteCheckBox.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
//		isSqliteCheckBox.addActionListener((e) -> {
//			removeSelectedFile();
//		});

        GroupLayout gl_configPanel = new GroupLayout(configPanel);
        gl_configPanel.setHorizontalGroup(gl_configPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_configPanel.createSequentialGroup().addContainerGap()
                        .addGroup(gl_configPanel.createParallelGroup(Alignment.LEADING)
                                        .addGroup(gl_configPanel.createSequentialGroup().addComponent(imgTypeLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED).addComponent(imgTypeComboBox,
                                                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .addGroup(gl_configPanel.createSequentialGroup().addComponent(pathStyleLabel)
                                                .addPreferredGap(ComponentPlacement.RELATED).addComponent(pathStyleRadioButton1)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(pathStyleRadioButton2))
                                        .addComponent(pathStyleDefaultComboBox, 0, 239, Short.MAX_VALUE)
                                        .addComponent(isCoverCheckBox).addComponent(mergeTileCheckBox)
                                //.addComponent(isSqliteCheckBox)
                        )
                        .addContainerGap()));
        gl_configPanel.setVerticalGroup(gl_configPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_configPanel.createSequentialGroup().addContainerGap()
                        .addGroup(gl_configPanel.createParallelGroup(Alignment.BASELINE).addComponent(imgTypeLabel)
                                .addComponent(imgTypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addGroup(gl_configPanel.createParallelGroup(Alignment.BASELINE).addComponent(pathStyleLabel)
                                .addComponent(pathStyleRadioButton1).addComponent(pathStyleRadioButton2))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(pathStyleDefaultComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGap(18).addComponent(isCoverCheckBox).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(mergeTileCheckBox).addPreferredGap(ComponentPlacement.RELATED)
                        //.addComponent(isSqliteCheckBox)
                        .addContainerGap(31, Short.MAX_VALUE)));
        configPanel.setLayout(gl_configPanel);

        /* 保存路径 */
        JPanel pathPanel = new JPanel();
        pathPanel.setBorder(new TitledBorder(null, "保存路径", TitledBorder.LEADING, TitledBorder.TOP,
                StaticVar.FONT_SourceHanSansCNNormal_13, null));
        pathPanel.setBounds(10, 326, 426, 157);
        getContentPane().add(pathPanel);
        pathPanel.setLayout(null);

        JButton pathSelectorButton = new JButton("选择路径");
        pathSelectorButton.setFocusable(false);
        pathSelectorButton.setBounds(323, 124, 93, 23);
        pathSelectorButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        pathPanel.add(pathSelectorButton);
        pathSelectorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    JFileChooser chooser = new JFileChooser();
//					if (getIsSaveInSqlite()) {
//						chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//					} else {
//						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//					}
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (lastDirPath != null && !"".equals(lastDirPath)) {
                        chooser.setCurrentDirectory(new File(lastDirPath));
                    }
                    chooser.setDialogTitle("选择保存路径");
                    chooser.setApproveButtonText("确定");
                    chooser.showOpenDialog(null);
                    File file = chooser.getSelectedFile();
                    if (file != null) {
                        lastDirPath = file.getAbsolutePath();
                        selectedPath = file;
                        pathTextArea.setText(file.getAbsolutePath());
                        if (isCheckBoxHasSelected()) {
                            downloadButton.setEnabled(true);
                        }
                    }
                }
            }
        });

        pathTextArea = new JTextArea();
        pathTextArea.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        pathTextArea.setEditable(false);
        pathTextArea.setBounds(10, 20, 406, 94);
        pathPanel.add(pathTextArea);

        previewButton = new JButton("预估下载量");
        previewButton.setFocusable(false);
        previewButton.setEnabled(false);
        previewButton.setBounds(123, 493, 110, 23);
        previewButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        getContentPane().add(previewButton);
        previewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1 && previewButton.isEnabled()) {
                    downloadPreviewFrame.showPreview(getSelectedLayers(), polygons);
                }
            }
        });

        downloadButton = new JButton("下载");
        downloadButton.setFocusable(false);
        downloadButton.setEnabled(false);
        downloadButton.setBounds(243, 493, 93, 23);
        downloadButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        getContentPane().add(downloadButton);

        /* 开始下载按钮点击事件 */
        downloadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1 && downloadButton.isEnabled()) {
                    if (TaskState.IS_TASKING) {
                        JOptionPane.showMessageDialog(null, "当前正在进行下载任务");
                        return;
                    }
                    boolean isCreate = true;
                    if (isTaskExist(selectedPath.getAbsolutePath())) {
                        Object[] options = {"导入任务", "创建新任务"};
                        int n = JOptionPane.showOptionDialog(null, "该目录下已存在下载任务，请选择导入任务或新建任务", "选择",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        switch (n) {
                            case JOptionPane.YES_OPTION:
                                isCreate = false;
                                break;
                            case JOptionPane.NO_OPTION:
                                isCreate = true;
                                break;
                            case JOptionPane.CANCEL_OPTION:
                                return;
                            case JOptionPane.CLOSED_OPTION:
                                return;
                            default:
                                return;
                        }
                    }
                    if (!isCreate) {
                        TaskAllInfoEntity taskAllInfo = TaskUtils.getExistTaskByPath(selectedPath.getAbsolutePath());
                        if (null != taskAllInfo) {
                            sharedService.pub(SharedType.MAIN_FRAME_SELECTED_INDEX, 1);
                            taskExec.loadTask(taskAllInfo);
                        } else {
                            int m = JOptionPane.showConfirmDialog(null, "读取现有任务失败，这将创建新的下载任务", "选择",
                                    JOptionPane.YES_NO_OPTION);
                            if (m == JOptionPane.YES_OPTION) {
                                sharedService.pub(SharedType.MAIN_FRAME_SELECTED_INDEX, 1);
                                taskExec.createTask(getTaskCreate());
                            } else {
                                JOptionPane.showMessageDialog(null, "用户取消创建下载任务");
                                return;
                            }
                        }
                    } else {
                        sharedService.pub(SharedType.MAIN_FRAME_SELECTED_INDEX, 1);
                        taskExec.createTask(getTaskCreate());
                    }
                    setVisible(false);
                }
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.setFocusable(false);
        cancelButton.setBounds(346, 493, 93, 23);
        cancelButton.setFont(StaticVar.FONT_SourceHanSansCNNormal_13);
        getContentPane().add(cancelButton);
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setVisible(false);
            }
        });

        this.setTitle("下载设置");
        this.setSize(new Dimension(455, 555));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2);
        this.setVisible(false);
        this.setResizable(false);
        this.subShared();

    }

    private void subShared() {
        sharedService.sub(SharedType.UPDATE_UI).subscribe((res) -> {
            SwingUtilities.invokeLater(() -> {
                SwingUtilities.updateComponentTreeUI(this);
            });
        });
    }

    // 创建任务，打开面板
    public void createNewTask(String url, List<Polygon> polygons, String tileName, String mapType) {
        this.removeAllSelectedLayers();
        this.removeSelectedFile();
        this.setVisible(true);
        this.url = url;
        this.polygons = polygons;
        this.tileName = tileName;
        this.mapType = mapType;
    }

    // 创建任务实例类
    private TaskCreateEntity getTaskCreate() {
        TaskCreateEntity taskCreate = new TaskCreateEntity();
        taskCreate.setTileUrl(url);
        taskCreate.setTileName(tileName);
        taskCreate.setMapType(mapType);
        taskCreate.setSavePath(selectedPath.getAbsolutePath());
        taskCreate.setZoomList(getSelectedLayers());
        taskCreate.setPolygons(polygons);
        taskCreate.setPathStyle((String) pathStyleDefaultComboBox.getSelectedItem());
        taskCreate.setImgType(imgTypeComboBox.getSelectedIndex());
        taskCreate.setIsCoverExists(getIsCoverExist());
        taskCreate.setIsMergeTile(getIsMergeTile());
        return taskCreate;
    }

    private boolean getIsCoverExist() {
        return isCoverCheckBox.isSelected();
    }

    private boolean getIsMergeTile() {
        return mergeTileCheckBox.isSelected();
    }

//	private boolean getIsSaveInSqlite() {
//		return isSqliteCheckBox.isSelected();
//	}

    private boolean isTaskExist(String path) {
        File file = new File(path + "/task_info.jmd");
        if (file.exists() && file.isFile()) {
            return true;
        } else {
            return false;
        }
    }

//	private boolean isSqliteExist(String path) {
//		File file = new File(path);
//		if (file.isDirectory()) {
//			file = new File(path + "/tiles.sqlite3");
//		}
//		if (file.exists() && file.isFile()) {
//			return true;
//		} else {
//			return false;
//		}
//	}

    private void removeAllSelectedLayers() {
        this.selectAllCheckBox.setSelected(false);
        for (JCheckBox checkBox : this.checkBoxList) {
            checkBox.setSelected(false);
        }
        this.previewButton.setEnabled(false);
        this.downloadButton.setEnabled(false);
    }

    private void removeSelectedFile() {
        this.selectedPath = null;
        this.pathTextArea.setText(null);
        this.downloadButton.setEnabled(false);
    }

    private boolean isCheckBoxHasSelected() {
        boolean flag = false;
        for (JCheckBox checkBox : checkBoxList) {
            if (checkBox.isSelected()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private boolean isCheckBoxAllSelected() {
        boolean flag = true;
        for (JCheckBox checkBox : checkBoxList) {
            if (!checkBox.isSelected()) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private boolean isCheckBoxNoneSelected() {
        boolean flag = true;
        for (JCheckBox checkBox : checkBoxList) {
            if (checkBox.isSelected()) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private ArrayList<Integer> getSelectedLayers() {
        ArrayList<Integer> layers = new ArrayList<Integer>();
        for (int i = 0; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isSelected()) {
                layers.add(i + minZoom);
            }
        }
        return layers;
    }
}
