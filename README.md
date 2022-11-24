# 地图瓦片图下载器

#### 复制自本人的gitee
#### gitee仓库地址：https://gitee.com/CrimsonHu/java_map_download

#### Java17 LTS，springboot 2.7，JCEF使用chromium95内核
#### 目前进度：
- 使用rxjava优化代码，解决依赖循环注入的问题（已经完成，正在自测）
- 等待springboot3正式版发布，第一时间吃上springboot3
- 新版本仅优化代码，升级依赖，重写部分模块，无功能添加，可以放心下载2022-03-25版本使用

#### 介绍
使用Java开发的地图瓦片图下载工具，支持以下XYZ瓦片图下载与合并。多线程瓦片图下载，最大限度地使用本机网络资源。个人业余作品，全网仅有的免费开源开箱即用的地图下载工具，业余时间不多，用爱发电，能跑就行。
- OpenStreetMap
- 谷歌地图（需要代理）
- 天地图（务必更换自己的key，并注意配额）
- 高德地图
- 腾讯地图
- 必应地图

#### Build下载地址（已打包的可执行程序，解压即可运行）
- 由于Gitee Release单个附件不能超过100M，故使用网盘发行Build（网盘中同样可以下载到旧版）
- 更新日期：2022-03-25（下载最新版注意该更新时间）
- 百度网盘：https://pan.baidu.com/s/1lRrZvTWAB7AFyQ8zChl5oQ 提取码：y5wj 
- 天翼云盘：https://cloud.189.cn/t/IBFrIzIFZz6j 访问码:5bgb
- 重要提示：下载谷歌地图需正确使用代理，不能下载就是代理没设置好
- 若无法打开，请将文件夹改为英文，并注意文件夹所在详细路径是否为全英文

#### 更新历史
- 2022-03-25：更新至Java17 LTS，springboot 2.6.4，JCEF更新至chromium95内核；优化代码
- 2021-04-09：优化依赖结构，减少打包体积（注意：不要进行无意义的超巨大尺寸合并，那样OpenCV会内存溢出）
- 2021-03-24：添加腾讯地图地图，添加坐标类型显示，修复部分问题
- 2021-03-22：添加天地图key更换功能、添加必应地图，添加并更换默认webview为Chromium Embedded Framework（JCEF）
- 2021-03-01：优化界面显示，修复部分问题
- 2021-02-18：默认地图设为高德地图；添加http代理支持，用于下载谷歌地图
- 2020-11-28：优化错误瓦片图自动重新下载功能
- 2020-11-27：初步添加错误瓦片图自动重新下载功能，解决无法下载天地图的问题

#### Liberica Jdk下载地址
- 天翼云盘：https://cloud.189.cn/t/UNZv2aBRfiai (访问码:7q6g)
- https://bell-sw.com/pages/downloads/#/java-17-current

#### 代码运行说明
1. 本软件用eclipse开发，基于springboot
2. 开发环境：Liberica Jdk 17，Angular CLI 13
3. IDE需要安装lombok插件
4. Web部分使用Angular13开发，需nodejs与angular-cli环境
5. 解压lib目录下的opencv(原版备份).jar文件，将opencv.dll放入至jdk/bin目录下（其他操作系统选择与之对应的库文件）
6. 添加JCEF（仅支持Windows，eclipse为例）：Java Build Path >> 展开JRE，选中Native library location，点击Edit，选择当前项目目录下的binary_win64；
7. pom.xml中已添加Windows、Linux、macOS(Intel)的jxbrowser离线jar包，根据自身平台选择
7. 下述图片基于win10和macOS(Intel)平台运行
8. 目前谷歌地图不能直接访问，现在提供http代理功能用以支持通过代理下载谷歌地图（不提供fq方法，fq自行解决）

#### 软件说明
1. 使用springboot+swing+angular开发的桌面程序
2. 内置若干swing主题皮肤
3. webview使用JxBrowser Chromium、JavaFX webview、Chromium Embedded Framework三种实现方式可供选择
4. 支持Windows与macOS(Intel)，macOS(ARM)本人没有机器故无法进行测试与适配
5. 支持png与jpg格式存储瓦片图，并支持瓦片图合并
6. 多线程瓦片图下载，最大限度地使用网络资源，拒绝付费限速
7. 瓦片图下载使用okhttp3实现
8. 使用OpenCV进行瓦片图合并，支持大尺寸png合成图

#### 主要功能
XYZ瓦片图下载与拼接
![下载结果](https://images.gitee.com/uploads/images/2020/1025/194201_51cbcc76_1403243.png "RNOZ4TCN}]TF)I2S1V`P(B1.png")
![瓦片图拼接示例3](https://images.gitee.com/uploads/images/2020/1025/191841_58a9107e_1403243.png "N1EK$(KS$(18YJ1KA2N{@XG.png")
![瓦片图拼接示例1](https://images.gitee.com/uploads/images/2020/1025/184433_266b9408_1403243.png "QTQRF_H$FW`SBJ9R29]KU6W.png")
![瓦片图拼接示例3](https://images.gitee.com/uploads/images/2020/1025/191831_0fe37c36_1403243.png ")W(9J7ZBMNRYQOBNW9Y}TZM.png")
![瓦片图拼接示例2](https://images.gitee.com/uploads/images/2021/0322/192008_a3e72cda_1403243.jpeg "@6{(IVS_OQMOH[~R($KD5(7.jpg")
![瓦片拼接结果集合](https://images.gitee.com/uploads/images/2020/1117/235757_070c3fc7_1403243.png "~7TH`BENU$T66FUDX0{R{0V.png")

#### 最近更新（添加必应地图与腾讯地图）
![主界面必应地图](https://images.gitee.com/uploads/images/2021/0322/190547_ef9e10bd_1403243.png "8CCX18[[YW)DHN@{5VZ(0RW.png")
![主界面腾讯地图](https://images.gitee.com/uploads/images/2021/0324/012326_e90a2ee4_1403243.png "](]I`_27X@FU@5V{WP}TNGJ.png")

#### 主要界面
![主界面谷歌地图](https://images.gitee.com/uploads/images/2021/0322/191011_7b58ab8c_1403243.png "P3[FIH{H4WC9}YW5{OP8F%5.png")
![主界面高德地图](https://images.gitee.com/uploads/images/2021/0322/190953_497f7569_1403243.png "L3AU~Q6%0K0YW~_IIR@]4JA.png")
![主界面天地图](https://images.gitee.com/uploads/images/2021/0322/190713_68a1bd09_1403243.png "N83WEAPJK_2IO{W9L`K)@@7.png")
![浏览器内核切换](https://images.gitee.com/uploads/images/2021/0322/191415_b83b6dfd_1403243.png "~5LGHGB(0A2KK)BYE]%{RMQ.png")

#### 使用代理访问并下载谷歌地图（2021-02-18版本）
![代理1](https://images.gitee.com/uploads/images/2021/0218/152258_3b6f8231_1403243.jpeg "1613632674(1).jpg")
![代理2](https://images.gitee.com/uploads/images/2021/0218/152345_babdc925_1403243.png "$_OQTUXO623_RZ%0G)%[XYM.png")
![代理3](https://images.gitee.com/uploads/images/2021/0218/153929_88e6d78c_1403243.png "ZEAZE$3C7CZS`7@_{X@FI3K.png")

#### 以下为旧版截图
![Windows7](https://images.gitee.com/uploads/images/2020/1123/013255_4aa27099_1403243.png "_JVZV%LF}GJ`(L(B7W(%N}D.png")
![下载设置](https://images.gitee.com/uploads/images/2020/1123/012907_830bb221_1403243.png "I1XDZJNIX3CI)0TH_XO2)LL.png")
![下载界面1](https://images.gitee.com/uploads/images/2020/1031/162720_aecb9b57_1403243.png "~`VE_0FM{HB0_K4S~ERO]DD.png")
![下载界面2](https://images.gitee.com/uploads/images/2020/1031/162734_fc13bdb9_1403243.png "F1ASVTZQH%D}7NM7E4@VC~2.png")
![关于](https://images.gitee.com/uploads/images/2020/1123/013200_324218fd_1403243.png "2_859NL6FOR7(@]_[E3XCFL.png")
![瓦片图拼接示例1](https://images.gitee.com/uploads/images/2020/1025/184409_f512ec03_1403243.png "IHESGJ986LN31[ICDV]5ICQ.png")
![瓦片图拼接示例4](https://images.gitee.com/uploads/images/2020/1029/163712_032f9f19_1403243.png "]~QU7`77({@VL{GLBOKJM{0.png")

##### macOS(Intel)下截图
![主界面1](https://images.gitee.com/uploads/images/2020/1025/200558_73c24f43_1403243.png "C241622F-D0C6-4E07-A20B-6424BD93987D.png")
![主界面2](https://images.gitee.com/uploads/images/2020/1025/200625_fa0bbac7_1403243.png "2FA9C69F-F8AD-4D99-8948-E2412FCC39E2.png")
![下载设置](https://images.gitee.com/uploads/images/2020/1025/200638_ba6a3d43_1403243.png "B6E5D312-3B70-48CA-9268-D8EBF7B0AD2B.png")
![下载界面](https://images.gitee.com/uploads/images/2020/1031/164829_579bde2a_1403243.png "35BDBFD3-699E-48A5-BF22-349E84AC3573.png")
![下载结果](https://images.gitee.com/uploads/images/2020/1025/200828_c79e7461_1403243.png "319FC41E-DDF9-4633-816D-09B813FDE093.png")
![合并结果](https://images.gitee.com/uploads/images/2020/1025/201358_ee4b9a82_1403243.png "D9400C8D-E87D-42A6-BEBF-C5CEA2B9F75C.png")
![合并结果](https://images.gitee.com/uploads/images/2020/1025/201415_178ebde6_1403243.png "61916631-E18B-4A54-BDEA-0BDD2C04A5A0.png")

#### 声明
本项目属于个人研究使用，请勿商用 
