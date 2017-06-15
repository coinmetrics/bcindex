import requests
import json
import datetime

url = "http://localhost:8090/api/index"

data = {'index':'ODD', 'currency':'USD','timeFrame':'HOURLY'}

resp = requests.post(url, json=data)

print resp.text