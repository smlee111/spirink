import pymysql
 
conn = pymysql.connect(host='61.42.251.243', user='playcom', password='tmvlfldzm!23',
                       db='spdb', port=39306, charset='utf8')
 
curs = conn.cursor()
sql = """insert into customer(name,category,region) values (%s, %s, %s)"""
curs.execute(sql, ('홍길동', 1, '서울'))
curs.execute(sql, ('이연수', 2, '서울'))
conn.commit()
 
conn.close()