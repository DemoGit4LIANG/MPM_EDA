# MPM_EDA_2013_written

大约2013年用Java实现的一种层次聚类算法，源码包中同时包含经典的K-Means算法作为对照组。
在iris和yeast等公开数据集上，该算法在参数调教合适时性能能够超越K-Means和另一种层次聚类算法——BIRTH。

程序发布版(jar)运行方法如下所示，具体详情请阅读源码~
### Manual of the MPM-EDA.jar
The MPM-EDA.jar provides an implement of the MPM-EDA agorithm, and an implement of standrad K-meams as benchmark. Before using this jar package, please make sure the JDK 7 has been correctly installed in your computer.

A simple demo is shown in the Fig. 1. This demo program will employ MPM-EDA algorithm on the yeast data set, and detailed results of the clustering will be printed.

![Alt text](https://raw.githubusercontent.com/DemoGit4LIANG/MPM_EDA_2013_written/master/Screenshots/1.png)

The format of your input data should be accordance with the data files we provide (iris, seeds
and yeast. You can find the in the .rar file). __Notice__ that if you run the algorithm on seeds data set,
please remove the sentence of “System.setProperty("java.util.Arrays.useLegacyMergeSort",
"true");”

To run the __K-means algorithm__, see the program shown in Fig. 2.
![Alt text](https://raw.githubusercontent.com/DemoGit4LIANG/MPM_EDA_2013_written/master/Screenshots/1.png)
