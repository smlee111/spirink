from hanspell import spell_checker
import sys
import base64
sent = sys.argv[1]
#sent = "1지망학교떨어졌어$$SNS시간낭비인거아는데매일하는중"
spelled_sent = spell_checker.check(sent)
hanspell_sent = spelled_sent.checked
result = str(hanspell_sent)
#print(result)
print(base64.b64encode(result.encode('utf-8')))
