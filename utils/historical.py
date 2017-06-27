import requests
import json
import datetime

def create_file(coin):
	# get data
	url = "http://www.coincap.io/history/90day/" + coin
	
	response = requests.get(url)

	data = response.json()
	price_key = 'price'

	if data is not None:	
		print "got data from: " + url
		prices = data['price']

		# populate file
		filename = "historical_prices/" + coin + ".csv"
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
coinfile = open("coins.txt", 'r')
for coin in coinfile:
	create_file(coin.rstrip())
