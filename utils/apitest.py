import requests
import json
import datetime

local = "http://localhost:8090/api/index"
stage = "http://stage-index-view.herokuapp.com/api/index"
prod = "http://www.bletchleyindexes.com/api/index";

data = {'index':'xxxFORTY_INDEX', 'currency':'USD','timeFrame':'DAILY'}

#resp = requests.post(local, json=data)
#resp = requests.post(stage, json=data)
#resp = requests.post(prod, json=data)

resp = requests.get('https://api.blockchain.info/charts/n-unique-addresses?timespan=1year&daysAverageString=7&format=json')
btcresp = requests.get('http://coincap.io/history/365day/BTC')
#print resp.text

blockjson = resp.json()
btcjson = btcresp.text
print btcjson

# convert raw data to x and y
# as separate arrays for chart js
x_axis = []
#for pair in jsondata['values']:
  
