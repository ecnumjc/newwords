#新词发现

马敬超<br/>
华东师范大学<br/>


##数据
###**DATA**<br/>
raw_data：新词发现实验最初的各个领域的锚文本<br/>
clean_data: 去除控制字符等无用信息后的领域文本数据<br/>
ngram_data：进过n-gram切分字符串，根据设定的阈值得到的第一轮结果<br/>
seeds_data: 各个领域的种子词，人工选定，一行一个<br/>
userdict：分词用户字典<br/>
nws_data: 领域新词<br/>



##源代码
###**src**<br/>
pre_data.py：数据预处理，包括去除控制字符、分词、训练word2vec向量空间<br/>
ngram_nw.py:ngram切分字符串<br/>
fw_w2vb_model.py：基于word2vec的新词发现过滤模型<br/>
pca.py:降维算法<br/>
train_word2vec_model.py:word2vec训练脚本<br/>


##实验报告
###**doc**<br/>
包含详细方法介绍以及实验结果的统计<br/>





