import requests
import json
import datetime

local = "http://localhost:8090/api/index"
stage = "http://stage-index-view.herokuapp.com/api/index"
prod = "http://www.bletchleyindexes.com/api/index";

data = {'index':'xxxFORTY_INDEX', 'currency':'USD','timeFrame':'DAILY'}

resp = requests.post(local, json=data)
#resp = requests.post(stage, json=data)
#resp = requests.post(prod, json=data)

print resp.text

