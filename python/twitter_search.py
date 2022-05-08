twitter_consumer_key = "Fq4tp9B0xgeW49mXCCDBehDrH"
twitter_consumer_secret = "rSAZNhkSGiieiMF1W5rGq7evbMmN3ddb0PN0IDR9q0rlNtzZQ9"  
twitter_access_token = "1381915756279910402-S6L1RjY0DU5DuK6dXB15rjXYW3ulN9"
twitter_access_secret = "tvzeTDyrDjG81TxAHHQq6ZVTALKOcbi9d9PCDblfNYYmi"

import twitter
import time
import sys
query = sys.argv[1]
start = time.time()
twitter_api = twitter.Api(consumer_key=twitter_consumer_key,
                          consumer_secret=twitter_consumer_secret, 
                          access_token_key=twitter_access_token, 
                          access_token_secret=twitter_access_secret)

statuses = twitter_api.GetSearch(term=query, count=100)
output_file_name = "C:/project/py_src/result/"+str(start)+"_"+str(query)+".txt"
#output_file_name = "/home/tomcat/py_src/result/"+str(start)+"_"+str(query)+".txt"

with open(output_file_name, "w", encoding="utf-8") as output_file:
    for status in statuses:
        print(status.text, file=output_file)
print(time.time()-start)