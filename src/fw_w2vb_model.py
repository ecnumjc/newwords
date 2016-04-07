#coding:utf-8
import os
import gensim
from numpy import *

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


def Bootstrapping_nw(word2vec_model,seedwords_file,field_words_file,newwords_file,gate):
    dict_nw = {}
    f_newwords = open(newwords_file,'w')
    model = gensim.models.Word2Vec.load(word2vec_model)
    init_dict_size = 0.5

    while len(dict_nw) != init_dict_size:
        init_dict_size = len(dict_nw)
        print init_dict_size
        with open(field_words_file) as fin:
            for line in fin:
                list_words = line.replace('\n','').split('\t')
                sumcos = 0
                try:
                    w_vec = model[list_words[0].decode('gb2312')]
                    with open(seedwords_file) as finseed:
                        for lineseed in finseed:
                            try:
                                seed_vec = model[lineseed.replace('\n','').decode('gb2312')]
                                sumcos = sumcos + cosVector(w_vec,seed_vec)
                            except:
                                pass
                    finseed.close()
                except:
                    pass
                if sumcos >= gate:
                    dict_nw[list_words[0]]=sumcos
        fin.close()
        sort_seed = sorted(dict_nw.iteritems(),key = lambda asd:asd[1],reverse = True)      
        with open(seedwords_file,'w') as fwriteseed:
            for i in range(0,10):
                fwriteseed.write(sort_seed[i][0]+'\n')
        fwriteseed.close()         
    for key in dict_nw:
        f_newwords.write(key+'\n')
    f_newwords.close()


if __name__ == '__main__':
    Bootstrapping_nw(".\\model\\securities.model",".\\seed\\seed_securities.txt",".\\field_newwords_first\\securities.txt",".\\field_newwords_final\\securities.txt",0.01)

