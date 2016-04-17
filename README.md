#新词发现

马敬超<br/>
华东师范大学<br/>


##数据
###**DATA**<br/>
* raw_data：新词发现实验最初的各个领域的锚文本<br/>
* clean_data: 去除控制字符等无用信息后的领域文本数据<br/>
* ngram_data：进过n-gram切分字符串，根据设定的阈值得到的第一轮结果<br/>
* seeds_data: 各个领域的种子词，人工选定，一行一个。其中seeds备份文件夹下的词为最初种子词，在新一轮运行的时候可以用这些种子词作初始化。<br/>
* model: 各个领域训练得到的词向量空间文件夹<br/>
* seg_data: 训练word2vec的输入文件<br/>
* nws_data: 领域新词<br/>
* userdict: 各个领域的领域词典，主要是为了训练领域词向量空间时是的一些字符串可以被当作词分在一起<br/>



##源代码
###**src**<br/>
* pre_data.py：数据预处理，包括去除控制字符、分词、训练word2vec向量空间<br/>
* ngram_nw.py:ngram切分字符串<br/>
* fw_w2vb_model.py：基于word2vec的新词发现过滤模型<br/>
* pca.py:降维算法<br/>
* train_word2vec_model.py:word2vec训练脚本<br/>


##实验报告
###**doc**<br/>
* 一份实验报告
* 一份专利申请





