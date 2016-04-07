#coding:utf-8
import os
import gensim
from numpy import *

#import jieba
#jieba.load_userdict("userdict.txt")

def word2vec_train(strfilein,strfileout):
    #准备应用阶段
    #step 1:繁体转简体
    #cmd1 = 'opencc -i '+strfilein+' -o '+strfileout+'.jian -c zht2zhs.ini'
    #os.system(cmd1)
    #step 2:中文分词
    #cmd2 =  'python -m jieba -d" " '+ strfilein+' > '+strfileout+'.jian.seg'
    #os.system(cmd2)
    #step 3:将不是utf8编码的部分转化为utf8编码
    #cmd3 = 'iconv -c -t UTF-8 < '+strfileout+'.jian.seg'+' > '+strfileout+'.jian.seg.utf-8'
    #os.system(cmd3)  
    #step 4:训练word2vec空间向量
    cmd4 =  'python train_word2vec_model.py '+strfileout+'.seg '+strfileout+'.model '+strfileout+'.vector'
    os.system(cmd4)


def cosVector(x,y):
    if(len(x)!=len(y)):
        print('error input,x and y is not in the same space')
        return;
    result1=0.0;
    result2=0.0;
    result3=0.0;
    for i in range(len(x)):
        result1+=x[i]*y[i]   #sum(X*Y)
        result2+=x[i]**2     #sum(X*X)
        result3+=y[i]**2     #sum(Y*Y)
    return result1/((result2*result3)**0.5)


def word2vec_test(word2vec_model):
    #应用阶段
    model = gensim.models.Word2Vec.load(word2vec_model)
    res= model.most_similar(u"马金融")
    for e in res:
        print e[0],e[1]
    #print model.similarity(u"额度",u"信用卡")#计算两个词的相似度
    
    #x1 = model[u'助学']
    #print '....\n'
    #print x1
    #x2 = model[u'贷款']#该词对应的向量
    #print x2


    #print '...\n'
    #aa = array(x1-x2)
    #sum = 0
    #for x,value in ndenumerate(aa):
    #    sum = sum + value*value
    #print sqrt(sum) 
    #cosVector(x1,x2)

    ##print model.doesnt_match(u"北京 天津 上海 香蕉".split())
    #r = model.most_similar(positive=[u'雷军', u'董明珠'], negative=[u'格力'], topn=15)
    #for aa in r:
    #    print aa[0],aa[1]


def bigram_dect(word2vec_model):
    model = gensim.models.Word2Vec.load(word2vec_model)
    fin = open("new_word.csv")
    fwrite = open("newword_word2vec.csv",'w')
    for line in fin.readlines():
        words = []
        words = line.split('\t')
        h = model[words[2].decode('utf-8')]
        t = model[words[4].decode('utf-8')]
        x1 = model[u'助学']
        x2 = model[u'贷款']#该词对应的向量
        if cosVector(h+t,x1+x2)>0.95:
            print (words[2]+words[4]).decode('utf-8')
        #print model.similarity(u"额度",u"信用卡")#计算两个词的相似度
        #r = array(t-h)
        #sum = 0
        #for L,value in ndenumerate(r):
        #    sum = sum + value*value
        #w2vec_cos = str(sqrt(sum))
        #fwrite.write(words[2]+words[4]+"\t"+w2vec_cos+'\t'+cosVector(h,t)+"\n")
        #print sqrt(sum)



            

if __name__ == '__main__':
    #cmd4 =  'python train_word2vec_model.py totalsentence_seg.txt totalsentence_seg.txt.model totalsentence_seg.txt.vector'
    #os.system(cmd4)
    word2vec_train('finance.txt.clean','finance.txt.clean')
    #rootdir = "../data"    
    #for parent,dirnames,filenames in os.walk(rootdir): 
    #    for filename in filenames:    
    #        sourceDir = os.path.join(parent,filename)  
    #        word2vec_train(sourceDir,"../model/"+filename)
    #word2vec_test("finance.txt.clean.model")
    #bigram_dect("../model/test.clean.model")

