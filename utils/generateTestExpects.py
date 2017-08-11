import json

def parseBackfillFile(filename, mult, multEven):
	csvfile = open(file, 'r')
	for line in csvfile:
		delim = ","
		line = line.rstrip()
		arr = line.split(delim)
		mult[arr[0]] = arr[1]
		multEven[arr[0]] = arr[2]

def getPricesFromApiJson(filename):
	with open(filename, 'r') as json_file:
		dataStr = json_file.read()
		data = json.loads(dataStr.replace("\'", '"'))
		print data

# file = raw_input("enter path to csv:")
file = "../index-data/src/main/resources/business_rules/jun_rebal.csv"

def createTen(last, btc_usd):
	# do 10 index
	const = 16399912
	divisor = 222310310.380042

	mult = {}
	multEven = {}
	parseBackfillFile(file, mult, multEven)

	sum = 0.0
	for coin in mult:
		sum = sum + float(mult[coin]) * last[coin]

	idxBtc = (sum + const) / divisor
	idxUsd = idxBtc * btc_usd

	print "index btc: " + str(idxBtc)
	print "index usd: " + str(idxUsd)

	print "" # line break

	# do 10 even index
	constEven = 3575708.58522349
	divEven = 99382603.3786684

	sum = 0.0
	for coin in multEven:
		sum = sum + float(multEven[coin]) * last[coin]

	idxBtc = (sum + constEven) / divEven
	idxUsd = idxBtc * btc_usd

	print "index even btc: " + str(idxBtc)
	print "index even usd: " + str(idxUsd)

#### Brgin script ####
last = {}
last['BTC_XMR'] = 0.0159406 
last['BTC_LTC'] = 0.01929704 
last['BTC_ETH'] = 0.0875672 
last['BTC_XRP'] = 0.0000852 
last['BTC_DASH'] = 0.0715 
last['BTC_ETC'] = 0.00753001 
last['BTC_STRAT'] = 0.00149738 
last['BTC_BTS'] = 0.00005 
last['BTC_XEM'] = 0.00005477 
btc_usd = 2015.57

getPricesFromApiJson("/home/rise/Linux_files/repos/bcindex/index-data/src/test/resources/test_data/ten_api.json")

#createTen(last, btc_usd)