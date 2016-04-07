import re

fin = open('test.txt')
line = fin.readline()
fwrite = open('test.clean','w')
while(line):
    line = re.sub(r'[\x01-\x1f]','', line)
    fwrite.write(line+'\n')
    line = fin.readline()

