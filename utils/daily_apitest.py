import requests
import json
import datetime
import time

def checkDaily(endPt):
    print("hitting end point: " + endPt)
    print("")

    return requests.get(endPt)

def toLocalDate(epoch):
    seconds = epoch/1000
    return time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(seconds))


########## start script ##########
prod = "http://bletchleyindexes.com/daily/ten/even"
stage = "http://stage-index-view.herokuapp.com/daily/ten"
local = "http://localhost:8090/daily/ten"

raw_p = "http://bc-index-view.herokuapp.com/daily/ten"

body = checkDaily(raw_p).text
bodyjson = json.loads(body)

for ele in bodyjson:
    print(ele['pxBtc'])
    print(ele['pxUsd'])
    print(toLocalDate(int(ele['time'])))
    print("")
