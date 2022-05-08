import pandas as pd
import json
file_imported = pd.read_excel('C:/project/py_src/result/test_excel.xlsx', sheet_name = 'Sheet1')
hlist =[]
list1 = []
list  = []
hlist.append({
        "생성일":"20210518",
        "생성자":"OYH"
})
for index, row in file_imported.iterrows():
    list.append ({
            "id"       : int(row['id']),
            "준비"    : str(row['준비']),
            "Customer" : list1
            })
    list1.append ({
           "작업"       : str(row['작업'])
           })
hlist.append(list)
print (hlist)
with open ('testing.json', 'w') as f:
    json.dump(hlist, f, ensure_ascii=False, indent= True)