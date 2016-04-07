#coding:utf-8
import jieba
jieba.load_userdict(".\\userdict\\userdict_securities.txt")


fout = open(".\\data_seg\\securities.txt.clean.seg",'w')
with open(".\\data\\securities.txt.clean") as fin:
    for line in fin:
        words = jieba.cut(line)
        for w in words:
            fout.write(w.encode('utf-8')+' ')
    fin.close()
fout.close()

#f_user = open(".\\userdict\\userdict_securities.txt",'w')
#with open(".\\field_newwords_first\\securities.txt") as fin:
#    for line in fin:
#        li = line.strip().replace('\n','').split('\t')
#        f_user.write(li[0]+'\t'+li[1]+'\n')
#fin.close()
#f_user.close()



