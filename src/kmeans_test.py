# -*- coding: utf-8 -*-
from sklearn.cluster import KMeans
from sklearn.externals import joblib
import pca 



if __name__ == '__main__':
    feature,word = pca.loadDataSet('fund.txt')
    lowDMat,reconMat = pca.pca(feature,1)
    print lowDMat

    fout = open('result.txt','w')
    #����kmeans��
    clf = KMeans(n_clusters=2)
    s = clf.fit(lowDMat)
    #print s
    #����
    #print clf.cluster_centers_
    #ÿ�����������Ĵ�
    for i in range(len(clf.labels_)):
        fout.write(word[i][0]+'\t'+str(clf.labels_[i])+'\n')