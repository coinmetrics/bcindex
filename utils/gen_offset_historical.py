"""
 create offsets for when calc the index with
 1 or more constitents missing. The formula is
 wrong_idx + mis_px / divisor --> correct index price
"""

import requests
import json
import datetime

DIV = 8843361.673
DIV_EV = 7099807.684

def create_file(coin):
	# get data
	url = "http://www.coincap.io/history/30day/" + coin
	
	response = requests.get(url)

	data = response.json()
	price_key = 'price'

	btc_px = getBtc()
	
	if data is not None:	
		print "got data from: " + url
		prices = data['price']

		# populate file
		filename = "historical_prices/" + "bids.txt" + ".sql"
		outfile = open(filename, 'w')
		cnt = -3
		bid0 = 157184
		for price in prices:
			if (isInTimeFrame(price[0])) and cnt < 717:
				# get the index price offset based on the px * mult / div
				idxusd = getIndexEven(price[1])	
#				idxusd = getIndex(price[1])

				idxbtc = idxusd / btc_px[cnt][1]
				outfile.write(create_line(price[0], idxusd, idxbtc, btc_px[cnt][0], bid0))	

				bid0 = bid0 + 60

			cnt = cnt + 1

		outfile.close()

	else:
		print "could not get data for: " + coin

def getBtc():
	url = "http://www.coincap.io/history/30day/BTC"
	response = requests.get(url)
	data = response.json()
	return data['price']


def isInTimeFrame(date_millis):
	return (date_millis >= 1502219059174) and date_millis < 1502504192672

def getIndex(coinpx):
	return (coinpx * 50000000) / DIV

def getIndexEven(coinpx):
	return (coinpx * 33595953.70) / DIV_EV

def create_line(date_num, price, price_btc, btc_date, bid):
	num = date_num / 1000.0
	formatted = datetime.datetime.fromtimestamp(num).strftime('%Y-%m-%d %H:%M:%S')

	btcfor = datetime.datetime.fromtimestamp(btc_date/1000.0).strftime('%Y-%m-%d %H:%M:%S')
#	return str(formatted) + ",  " + str(date_num) + ",  " + str(price) + ",  " + str(price_btc) + " : " + str(btcfor) + str('\n')
#	return "update even_twenty set index_value_btc = index_value_btc + " + str(price_btc) + ", index_value_usd = index_value_usd + " + str(price) + ", time_stamp = " + str(date_num) + " where bletch_id = " + str(bid) + ";\n"
	return str(bid) + ","

def print_bid(bid):
	for x in range(1,84):
		bid = bid + 60
		print bid

#### begin script ####
# coinfile = open("coins.txt", 'r')
# for coin in coinfile:
# 	create_file(coin.rstrip())

create_file("NEO")
