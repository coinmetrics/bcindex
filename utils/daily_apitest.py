import requests
import json
import datetime
import time

def checkDaily():
    stage = "http://stage-index-view.herokuapp.com/daily/ten"
    return requests.get(stage)

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
