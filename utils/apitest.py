import requests
import json
import time

local = "http://localhost:8090"
stage = "http://stage-index-view.herokuapp.com"
prod = "http://www.bletchleyindexes.com"

ENV = prod

def checkIndex():
    endpt = ENV + "/api/index"
    data = {'index':'FORTY_INDEX', 'currency':'USD','timeFrame':'DAILY'}

    return requests.post(endpt, json=data)
    #resp = requests.post(stage, json=data)
    #resp = requests.post(prod, json=data)

def checkWeights():
    endpt = ENV + "/api/weight"
    data = {'index':'TEN'}
    return requests.post(endpt, json=data)

def checkWeightList():
    endpt = ENV + "/api/weight/list"
    # data = {'indexList':['TEN','TWENTY','FORTY']}
    data = {'indexList':['TEN','TEN_EVEN','TWENTY','TWENTY_EVEN','FORTY','FORTY_EVEN','TOTAL','TOTAL_EVEN','ETHEREUM','ETHEREUM_EVEN','CURRENCY','PLATFORM','APPLICATION']}

    return requests.post(endpt, json=data)

def checkPrice():
    endpt = ENV + "/blet/index";
    data = {'index':'FORTY_INDEX','currency':'USD','timeFrame':'MAX'}

    return requests.post(endpt, json=data)

def checkDailyPrice():
    endpt = ENV + "/daily/ten"
    return requests.get(endpt)

### api ###
localapi = "http://localhost:8085"
stageapi = "http://stage-index-api.herokuapp.com"
prodapi = "http://bc-index-api.herokuapp.com"
APIENV = stageapi

def checkDailyWeight():
    endpt = APIENV + "/daily/weight"
    data = {'index':'TEN'}
    return requests.post(endpt, json=data)

def checkDailyWeightContents():
    for entry in checkDailyWeight().json()['elementList']:
        print toLocalDate(entry['time'])

def toLocalDate(epoch):
    seconds = epoch/1000
    return time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(seconds))


print checkPrice().text
# print checkDailyPrice().text
# print checkWeightList().text
# print checkWeights().text
# print checkDailyWeight().text
# print checkDailyWeightContents()



