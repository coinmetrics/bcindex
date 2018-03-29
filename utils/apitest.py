import requests
import json
import datetime

def checkIndex():
    local = "http://localhost:8090/api/index"
    stage = "http://stage-index-view.herokuapp.com/api/index"
    prod = "http://www.bletchleyindexes.com/api/index";

    data = {'index':'FORTY_INDEX', 'currency':'USD','timeFrame':'DAILY'}

    return requests.post(local, json=data)
    #resp = requests.post(stage, json=data)
    #resp = requests.post(prod, json=data)

def checkWeights():
    local = "http://localhost:8090/api/weight"
    stage = "http://stage-index-view.herokuapp.com/api/weight"
    prod = "http://www.bletchleyindexes.com/api/weight";

    data = {'index':'TEN'}

    return requests.post(prod, json=data)

def checkWeightList():
    local = "http://localhost:8090/api/weight/list"
    stage = "http://stage-index-view.herokuapp.com/api/weight/list"
    prod = "http://www.bletchleyindexes.com/api/weight/list";

    data = {'indexList':['TEN','TWENTY','FORTY']}

    return requests.post(prod, json=data)

def checkPrice():
    prod = "http://www.bletchleyindexes.com/blet/index";
    stage = "http://stage-index-view.herokuapp.com/blet/index"
    local = "http://localhost:8090/blet/index"

    data = {'index':'FORTY_INDEX','currency':'USD','timeFrame':'MAX'}

    return requests.post(prod, json=data)


print checkPrice().text
#print checkWeightList().text
# print checkWeights().text

