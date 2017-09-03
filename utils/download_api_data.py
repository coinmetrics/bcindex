import requests
import json
import datetime

def create_line(date, price):
	return str(date) + "," + str(price) + str('\n')

local = "http://localhost:8090/api/index"
stage = "http://stage-index-view.herokuapp.com/api/index"
prod = "http://www.bletchleyindexes.com/api/index";

index = 'EVEN_ETH'
currency = 'USD'
timeframe = 'MAX'
data = {'index':index, 'currency':currency,'timeFrame':timeframe}

#resp = requests.post(stage, json=data)
#resp = requests.post(local, json=data)
resp = requests.post(prod, json=data)

prices = resp.json()['data']
times = resp.json()['times']


filename = "csv_data/" + index + "_" + currency + ".csv"
outfile = open(filename, 'w')

for i in range(0,len(prices)):
	outfile.write(create_line(times[i],prices[i]))	

outfile.close()
