# 0  파라미터는 bd_idx를 받는다.
# 1. 테이블데이터를 셀렉트한다.
# 2. 작업헤더 테이블을 업데이트 한다. 검사중
# 3. 맞춤법 체크를 한다.
# 4. 맞춤법 내용, 결과를 업데이트 한다
# 5. 작업헤더 테이블을 업데이트 한다. 검사완료
# 6. 엑셀을 생성한다

import sys
import base64
import pymysql
import openpyxl
import time
start = time.time()  # 시작 시간 저장
#print("....start")
wb = openpyxl.Workbook()
sheet = wb.active
sheet.append(['아이디', '작업자','검수상태','원문장','수정위치','수정문장', '수정안채택','오류유형'])

bd_idx = sys.argv[1]
path = sys.argv[2]
db_name = sys.argv[3]
#conn = pymysql.connect(host='61.42.251.243', user='playcom', password='tmvlfldzm!23',
#                       db=db_name, port=39306, charset='utf8')
conn = pymysql.connect(host='192.168.180.236', user='playcom', password='tmvlfldzm!23',
                       db=db_name, port=39306, charset='utf8')
#print("connect"+bd_idx)
mc = conn.cursor();
table_name = 'tb_type_chk_'+bd_idx

select_sql = "SELECT uni_num, quest,mod_contents,sp_quest,mod_yn,sp_answer,noun_list,worker_nm FROM {0}".format(table_name)

mc.execute(select_sql)
conn.commit()
#print("select....end")
mr = mc.fetchall()
for x in mr:
    uni_num=x[0]
    quest = x[1]
    mod_contents = x[2]
    sp_quest = x[3]
    mod_yn = x[4]
    print('mod_yn'+mod_yn)
    if(mod_yn=="Y"):
        sp_quest = x[3]
    else:
        sp_quest = x[1]
    sp_answer = x[5]
    noun_list = x[6]
    worker_nm = x[7]
    sheet.append([uni_num,worker_nm, noun_list, quest, mod_contents, sp_quest, mod_yn,sp_answer])
now_time = time.time() - start

wb.save(path+'/tb_type_chk_'+bd_idx+'.xlsx')
conn.commit()
conn.close()
print('create_ok')