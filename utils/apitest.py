import requests
import json
import datetime

local = "http://localhost:8090/api/index"
stage = "http://stage-index-view.herokuapp.com/api/index"

data = {'index':'ODD', 'currency':'USD','timeFrame':'MAX'}

#resp = requests.post(stage, json=data)
resp = requests.post(local, json=data)

print resp.text