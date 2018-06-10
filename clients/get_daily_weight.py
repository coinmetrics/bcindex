import requests
import json
import pandas as pd
import sys

endpt = "http://stage-index-api.herokuapp.com/daily/weight"
data = {'index':'TEN'}

# print(requests.post(endpt, json=data).text)

j = requests.post(endpt, json=data).text
data = pd.read_json(j, encoding='utf-8')

with pd.option_context('display.max_rows', None, 'display.max_columns', 3):
    print(data)


for val in data['elementList']:
    print (val)