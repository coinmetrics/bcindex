import requests
import json
import pandas as pd

def get_last_weight():
    endpt = "https://stage-index-view.herokuapp.com/api/weight/"
    data = {'index':'TWENTY'}

    j = requests.post(endpt, json=data).text

    # need to tell pandas the data type
    apiData = pd.read_json(j, typ='series')

    with pd.option_context('display.max_rows', None, 'display.max_columns', 3):
        print(apiData)

def get_last_weight_list():
    endpt = "https://stage-index-view.herokuapp.com/api/weight/list"
    data = {'indexList':['TEN','TWENTY']}

    j = requests.post(endpt, json=data).text

    data = pd.read_json(j, encoding='utf-8')

    with pd.option_context('display.max_rows', None, 'display.max_columns', 3):
        print(data)


print("getting last weights ...")
get_last_weight()
print("getting last weights list ...")
get_last_weight_list()