#coding:utf-8
import os
import gensim
import re
import jieba



def cleandata(finstr,foutstr):
    fin = open(finstr)
    line = fin.readline()
    fwrite = open(foutstr,'w')
    while(line):
        line = re.sub(r'[\x01-\x1f]','', line)
        fwrite.write(line+'\n')
        line = fin.readline()


def create_dict(file_finstr,dict_foutstr):
    f_user = open(dict_foutstr,'w')
    with open(file_finstr) as fin:
        for line in fin:
            li = line.strip().replace('\n','').split('\t')
            f_user.write(li[0]+'\t'+li[1]+'\n')
    fin.close()
    f_user.close()


def wordsseg(filedict_str,filefin_str,filefout_str):
    jieba.load_userdict(filedict_str)
    fout = open(filefout_str,'w')
    with open(filefin_str) as fin:
        for line in fin:
            words = jieba.cut(line)
            for w in words:
                fout.write(w.encode('utf-8')+' ')
        fin.close()
    fout.close()


def word2vec_trainmodel(file_fin,file_fout):
    cmd =  'python train_word2vec_model.py '+file_fin+' '+file_fout+'.model '+file_fout+'.vector'
    os.system(cmd)


if __name__ == '__main__':
    # example:
    cleandata("../DATA/raw_data/business.txt","../DATA/clean_data/business.txt.clean")
    # create_dict("../DATA/ngram_data/business.txt","../DATA/userdict/userdict_business.txt")
    # wordsseg("../DATA/userdict/userdict_business.txt","../DATA/clean_data/business.txt.clean","../DATA/seg_data/business.txt.clean.seg")
    # word2vec_trainmodel("../DATA/seg_data/business.txt.clean.seg","../DATA/model/business")