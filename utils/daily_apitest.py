import requests
import json
import datetime
import time

def checkDaily():
    prod = "http://bletchleyindexes.com/daily/ten/even"
    stage = "http://stage-index-view.herokuapp.com/daily/ten"
    local = "http://localhost:8090/daily/ten"

    endPt = prod
    print("hitting end point: " + endPt)
    print("")

    return requests.get(endPt)

def toLocalDate(epoch):
    seconds = epoch/1000
    return time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(seconds))


########## start script ##########

body = checkDaily().text
bodyjson = json.loads(body)

for ele in bodyjson:
    print ele['pxBtc']
    print ele['pxUsd']
    print toLocalDate(long(ele['time']))
    print ""
