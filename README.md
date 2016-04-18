#新词发现

马敬超<br/>
华东师范大学<br/>
email:[mjcica@outlook.com](email:mjcica@outlook.com)


##数据
###**DATA**<br/>
* raw_data：新词发现实验最初的各个领域的锚文本<br/>
* clean_data: 去除控制字符等无用信息后的领域文本数据<br/>
* ngram_data：进过n-gram切分字符串，根据设定的阈值得到的第一轮结果<br/>
* seeds_data: 各个领域的种子词，人工选定，一行一个。其中seeds备份文件夹下的词为最初种子词，在新一轮运行的时候可以用这些种子词根据需要作初始化。<br/>
* model: 各个领域训练得到的词向量空间文件夹<br/>
* seg_data: 训练word2vec的输入文件<br/>
* nws_data: 领域新词<br/>
* userdict: 各个领域的领域词典，主要是为了训练领域词向量空间时是的一些字符串可以被当作词分在一起<br/>



##源代码
###**src**<br/>
* pre_data.py：数据预处理，包括去除控制字符、分词、训练word2vec向量空间<br/>
* ngram.py:ngram切分字符串,需要在main函数中设置一组参数，以及调用jar包jvm参数的设置。<br/>
* fw\_w2vb\_model.py：基于word2vec的新词发现过滤模型<br/>
* kmeans_nw.py：聚类算法找中心，帮助找阈值<br/>
* train_word2vec_model.py:word2vec训练脚本<br/>
* N\_gram.jar是在ngram_nw.py中调用的jar包<br/>


##实验报告
###**doc**<br/>
* 一份领域新词抽取实验报告
* 一份专利申请初稿

##源代码使用的建议
* 首先应该使用pre_data.py做数据预处理，如去除控制字符等，需要查看其中main函数，根据需要调用相应函数；
* ngram.py第一次过滤，初评价；
* fw\_w2vb\_model.py第二次过滤，再评价；
* 其他像分词、训练词向量空间等操作都在pre_data.py中，需要将需要的函数的注释拿掉，并且注释掉不用的函数；
* 所有的.py程序的运行都要在源代码中修改待处理文本文件的路径，代码中给了例子；





