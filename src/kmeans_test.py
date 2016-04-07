# -*- coding: utf-8 -*-
from sklearn.cluster import KMeans
from sklearn.externals import joblib
import pca 



if __name__ == '__main__':
    feature,word = pca.loadDataSet('fund.txt')
    lowDMat,reconMat = pca.pca(feature,1)
    print lowDMat

    fout = open('result.txt','w')
    #调用kmeans类
    clf = KMeans(n_clusters=2)
    s = clf.fit(lowDMat)
    #print s
    #中心
    #print clf.cluster_centers_
    #每个样本所属的簇
    for i in range(len(clf.labels_)):
        fout.write(word[i][0]+'\t'+str(clf.labels_[i])+'\n')