import requests
import json
import datetime
import csv
import time as clock

class Mult:
	val = 0.0
	even = 0.0

class Bindex:
	btc = 0.0
	usd = 0.0
	time = 0
	bid = 0
	rid = 0

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


def create_line(index):
#	ftime = datetime.datetime.fromtimestamp(index.time/1000).strftime('%Y-%m-%d %H:%M:%S')	
	return str(index.rid) + "," + str(index.bid) + "," + str(index.btc) + "," + str(index.usd) + "," + str(index.time) + str('\n')

######################
#### begin script ####
######################

# populate data structures
api = {}
multData = {}
numPrices = 9999999
cnt = 1
tenIndex = []
tenEven = []
tweIndex = []
tweEvemIndex = []

# populate data 10 index
with open('May_rebal.csv','rb') as csvfile:
	creader = csv.reader(csvfile, delimiter=',')
	for row in creader:
		coin = row[0]

		## px is ordered from oldest[0] -> newest[len] 
		px = get_historical(coin)

		api[coin] = px

		# if numPrices == 0:
		# 	numPrices = len(api[coin])
		# 	print "NUM OF PRICES IS: " + str(numPrices)

		# if cnt > 3:
		# 	break

		tenMult = Mult()
		tenMult.val = float(row[1])
		tenMult.even = float(row[2])
		multData[coin] = tenMult

		pxLen = len(px)
		if pxLen < numPrices:
			numPrices = pxLen

#print "num px: " + str(numPrices)
#numPrices = 4

# init more data
constTen = 16333862.0
divTen   = 21901918.35

consEven = 3022990.404
divEven  = 10964215.29
PRICE_POS = 1
TIME_POS = 0
OFF_SET = 0 # if there is already data in the db
RID_OFF_SET = 64
MIN_PER_HOUR = 60

# init output files
header = "id,bletch_id,index_value_btc,index_value_usd,time_stamp\n"
tenFile = open("target/tenIndex.csv", 'w')
tenEvenFile = open("target/tenEven.csv", 'w')

twentyFile = open("target/twentyIndex.csv", 'w')
tweEvenFile = open("target/tweEvenIndex.csv", 'w')

#for each price entry
for i in range(0,numPrices,12):
# calculate the index
	sumTen = 0
	sumEven = 0;
	btcPrice = -1.0
	# build sums
	for coin in multData:
		px = api[coin][i][PRICE_POS]
		tenMult = multData[coin].val
		evenMult = multData[coin].even

		time = api[coin][i][TIME_POS]

		# time = time / 1000
		# print "coin=" + coin + ", time=" + str(datetime.datetime.fromtimestamp(time).strftime('%Y-%m-%d %H:%M:%S'))
	#	print coin + " px=" + str(px) + " mult=" + str(tenMult) + " eve=" + str(evenMult) + ", time=" + str(time)

		sumTen = sumTen + (px * tenMult)
		sumEven = sumEven + (px * evenMult)

		if (coin == 'BTC'):
			btcPrice = px

	currbId = OFF_SET + (i * MIN_PER_HOUR)
	currRid = RID_OFF_SET + i + 1

	currTen = Bindex()
	currTen.usd = (sumTen + constTen) / divTen
	currTen.btc = currTen.usd / btcPrice
	currTen.time = time
	currTen.bid = currbId
	currTen.rid = currRid
	tenFile.write(create_line(currTen))


	currTenEven = Bindex()
	currTenEven.usd = (sumEven + consEven) / divEven
	currTenEven.btc = currTenEven.usd / btcPrice
	currTenEven.time = time
	currTenEven.bid = currbId
	currTenEven.rid = currRid
	tenEvenFile.write(create_line(currTenEven))


#	print "--------------------------------------------"
# for coin in multData:
# 	px = api[coin][i][PRICE_POS]
# 	tenMult = multData[coin].val
# 	evenMult = multData[coin].even

# #	time =  datetime.datetime.fromtimestamp(api[coin][i][TIME_POS]).strftime('%Y-%m-%d %H:%M:%S')
# 	time = api[coin][i][TIME_POS]
# 	time = time / 1000
# 	print "time=" + str(datetime.datetime.fromtimestamp(time).strftime('%Y-%m-%d %H:%M:%S'))
# #	print coin + " px=" + str(px) + " mult=" + str(tenMult) + " eve=" + str(evenMult) + ", time=" + str(time)

# 	sumTen = sumTen + (px * tenMult)
# 	sumEven = sumEven + (px * evenMult)

# 	if (coin == 'BTC'):
# 		btcPrice = px

# idxTenBtc = (sumTen + constTen) / divTen
# idxTenUsd = idxTenBtc * btcPrice

# idxEvenBtc = (sumEven + consEven) / divEven
# idxEvenUsd = idxEvenBtc * btcPrice
