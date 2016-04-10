#coding:utf-8
import os

#first argument is input files splitting with ','
#second argument is output file,output 7 columns split with ',' , like below://word, term frequency, left neighbor number, right neighbor number, left neighbor entropy, right neighbor entropy, mutual information
#third argument is stop words list
#forth argument is the NGram parameter N
#5th argument is threshold of output words, default is "20,3,3,1,1,5"//output TF > 20 && (left | right) neighbor number > 3 && (left|right) entropy >1 &&MI > 5


if __name__ == "__main__":
    p1_filein="../DATA/clean_data/business.txt.clean"
    p2_fileout="../DATA/ngram_data/business.txt"
    p3_file_stopwords="stoplist.txt"
    p4_N="5"
    p5_thres="8,1,1,0.01,0.01,5"
    cmd = 'java -jar n_gram.jar '+p1_filein+' '+p2_fileout+' '+p3_file_stopwords+' '+p4_N+' '+p5_thres
    os.system(cmd)