import requests
import json
import datetime
import csv

class Mult:
	val = 0.0
	even = 0.0


# tenMult = Mult()
# tenMult.val = 8.0
# print tenMult.val

def get_historical(symbol):
	url = "http://www.coincap.io/history/30day/" + symbol
	response = requests.get(url).json()

	if response is not None:
		return response['price']
	else:
		print "no data for " + symbol
		return response

def create_file(symbol,mult,even):	
	# get data
	url = "http://www.coincap.io/history/30day/" + symbol
	
	response = requests.get(url)

	data = response.json()
	price_key = 'price'

	if data is not None:	
		print "got data from: " + url
		prices = data['price']

		# populate file
		filename = coin + ".csv"
		outfile = open(filename, 'w')
		for price in prices:
			outfile.write(create_line(price[0],price[1]))	

		outfile.close()

	else:
		print "could not get data for: " + coin

def create_line(date_num, price):
	num = date_num / 1000.0
	formatted = datetime.datetime.fromtimestamp(num).strftime('%Y-%m-%d %H:%M:%S')
	return str(formatted) + "," + str(price) + str('\n')

#### begin script ####

# populate data structures
api = {}
multData = {}
numPrices = 0
cnt = 1
with open('May_rebal.csv','rb') as csvfile:
	creader = csv.reader(csvfile, delimiter=',')
	for row in creader:
		coin = row[0]
		# api[coin] = get_historical(coin)

		# if numPrices == 0:
		# 	numPrices = len(api[coin])
		# 	print "NUM OF PRICES IS: " + str(numPrices)

		# if cnt > 3:
		# 	break

		tenMult = Mult()
		tenMult.val = float(row[1])
		tenMult.even = float(row[2])
		multData[coin] = tenMult

		print coin 
		cnt = cnt + 1
#      	create_file(row[0], float(row[1]), float(row[2]))
 
