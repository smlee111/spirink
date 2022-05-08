import pandas as pd
import json
from collections import OrderedDict
import math
import openpyxl
from openpyxl.styles import PatternFill, Border, Side, Alignment, Protection, Font

wb = openpyxl.Workbook()
sheet = wb.active

sheet.append(['입력문','엑셀문','script','priority','sleep','name','priority','sleep','name','priority','sleep'])

# 인덱스를 지정해 시트 설정
df_sheet_index = pd.read_excel('C:/Temp/json1.xlsx', 
                                sheet_name=0,  #첫번째 시트
                                usecols = "D:L", #필요한칼럼만 읽어온다
                                skiprows = 1) #1줄은 스킵

print(df_sheet_index)
arr1= df_sheet_index.to_numpy()
#print(arr1)
a='null'
for i in arr1:
    filedata = OrderedDict();
    filetot = OrderedDict();
    list=[]
    #filedata['script']=i[0]
    filedata['emotion'] ={'priority':int(i[1]), 'sleep':int(i[2]), 'name':i[3]} #1차데이터 만든다
    list.append(filedata)
    x=(i[4])
    if x == x:    
        filedata['motion'] ={'priority':int(i[4]), 'sleep':int(i[5]), 'name':i[6]}
    filedata['speech'] ={'priority':i[7], 'sleep':i[8]}
    #print(filedata)
    jdata = json.dumps(filedata, ensure_ascii=False)
    print(str(jdata))
    filedata['speech'] ={'priority':int(i[7]), 'sleep':int(i[8]), 'language':a, 'script':i[0], 'sttSet':'off'}
    filetot['resultCode']=0
    filetot['resultMessage']='success'
    filetot['messages'] = list
    #print(filetot)
    jtot = json.dumps(filetot, ensure_ascii=False, indent=4)
    print(jtot)
    sheet.append([jdata,jtot,i[0],i[1],i[2],i[3],i[4],i[5],i[6],i[7],i[8]])
    
#wb.alignment=Alignment(horizontal='general',text_rotation=0,wrap_text=False,shrink_to_fit=False,indent=1);
wb.save('C:/Temp/json_test_out.xlsx')

