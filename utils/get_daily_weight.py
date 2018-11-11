import requests
import json

endpt = "http://stage-index-api.herokuapp.com/daily/weight"
data = {'index':'TEN'}

print(requests.post(endpt, json=data).text)