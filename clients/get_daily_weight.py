import requests
import json
import pandas as pd
import sys

def get_daily():
    endpt = "http://stage-index-api.herokuapp.com/daily/weight"
    data = {'index':'TEN'}

    # print(requests.post(endpt, json=data).text)

    j = requests.post(endpt, json=data).text
    print(j)

    data = pd.read_json(j, encoding='utf-8')

    with pd.option_context('display.max_rows', None, 'display.max_columns', 3):
        print(data)


    for val in data['elementList']:
        print (val)

def get_last_weight():
    # endpt = "https://stage-index-view.herokuapp.com/api/weight/list"
    # data = {'indexList':['TEN','TWENTY']}

    endpt = "http://localhost:8090/eight/weight/"
    data = {'index':'TWENTY'}

    # j = requests.post(endpt, json=data).text
    j = requests.get(endpt).text
    print(j)

    apiData = pd.read_json(j, typ='series')
    with pd.option_context('display.max_rows', None, 'display.max_columns', 3):
        print(apiData)

# get_daily()
get_last_weight()