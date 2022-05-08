from konlpy.tag import Okt
from collections import Counter
import csv
filename ="result\\1618539157.3023088_언어.txt"
f = open(filename,'r',encoding='utf-8')
news = f.read()

okt =Okt()
noun =okt.nouns(news)
for i,v in enumerate(noun):
    if len(v)<2:
        noun.pop(i)

count = Counter(noun)
f.close()

noun_list = count.most_common(100)
for v in noun_list:
    print(v)

with open("result\\noun_list.txt",'w',encoding='utf-8') as f:
    for v in noun_list:
        f.write(" ".join(map(str,v)))
        f.write("\n")
