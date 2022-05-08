# 0  파라미터는 bd_idx를 받는다.
# 1. 테이블데이터를 셀렉트한다.
# 2. 작업헤더 테이블을 업데이트 한다. 검사중
# 3. 맞춤법 체크를 한다.
# 4. 맞춤법 내용, 결과를 업데이트 한다
# 5. 작업헤더 테이블을 업데이트 한다. 검사완료
# 6. 엑셀을 생성한다
from hanspell import spell_checker
import sys
import base64
import pymysql
import openpyxl
import time
start = time.time()  # 시작 시간 저장
#print("....start")
wb = openpyxl.Workbook()
sheet = wb.active
sheet.append(['고유번호', '검사대상','변경위치','변경문장', '검사결과'])

bd_idx = sys.argv[1]
path = sys.argv[2]
db_name = sys.argv[3]
conn = pymysql.connect(host='192.168.180.236', user='playcom', password='tmvlfldzm!23',
                       db=db_name, port=39306, charset='utf8')
#print("connect"+bd_idx)
mc = conn.cursor();
table_name = 'tb_spell_'+bd_idx
#검사중 업데이트
sql_update = "update tb_work_bd set bd_state='0002' where bd_idx=%s"
mc.execute(sql_update, (bd_idx))
#print("update"+bd_idx)
#select_sql = "insert into {0}(uni_num,bd_idx,quest,answer) values (%s, %s, %s, %s)".format(table_name)
select_sql = "SELECT wk_idx, quest,uni_num FROM {0}".format(table_name)
chk_upd_sql = "update {0} set sp_quest=%s,mod_contents=%s, mod_yn=%s where wk_idx=%s".format(table_name)
#print("select....start")
mc.execute(select_sql)
conn.commit()
#print("select....end")
mr = mc.fetchall()
for x in mr:
    quest = x[1]
    quest = quest.splitlines()
    #quest.replace('\n', '$$') # 줄바꿈은 $$f로 바꾸어서 맞춤법 검사를 진행한다.
    #if(str(type(quest))=="list"):
    #    print("list->"+quest.splitlines())
    #else:
    #    print(quest+str(type(quest)))
    quest = '$$'.join(quest)
    #print("quest:"+quest)
    wk_idx=x[0]
    uni_num=x[2]
    mod_yn='N'
    mod_str=""
    try:
        spelled_sent = spell_checker.check(quest)
        hanspell_sent = spelled_sent.checked
        result_tot =str(hanspell_sent)
        words = spelled_sent.words #dictionary를 뽑는다
        #print(words)
        for k in words.keys(): #변경지점 확인
            if(words[k]!=0):
                #mod_str=mod_str+"<B>"+k+"</B> "
                mod_str=mod_str+k+", "
            #else:    
            #    mod_str=mod_str+k+" "
    except:
        result_tot = quest
    #print("spell chk"+result_tot)
    if result_tot != quest:
        mod_yn='Y'
    result2 = result_tot.replace("$$","\n")
    quest = quest.replace("$$","\n")
    mod_db =  mod_str.replace("$$",", ")
    mod_db = mod_db[:-2]
    mod_str = mod_str.replace("$$",", ")
    mod_str = mod_str[:-2]
    #print(result2)
    #result.replace('$$', '\n') # 맞춤법검사한 $$를 줄바꿈으로 다시 바꾼다
    mc.execute(chk_upd_sql, (result2,mod_db,mod_yn,wk_idx))
    #print(quest,result,mod_yn)
    sheet.append([uni_num, quest, mod_str, result2, mod_yn])
now_time = time.time() - start
sql_update = "update tb_work_bd set bd_state='0003',bd_time=%s where bd_idx=%s"
mc.execute(sql_update, (now_time,bd_idx))
wb.save(path+'/tb_spell_'+bd_idx+'.xlsx')
conn.commit()
conn.close()
print('update_ok')