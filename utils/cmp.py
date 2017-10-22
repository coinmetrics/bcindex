import requests
import json
import datetime

base_url = "https://min-api.cryptocompare.com/data/pricemulti?fsyms="

def checkTen():
	up = []
	up.append("BTC")
	up.append("ETH")
	up.append("XRP")
	up.append("BCH")
	up.append("LTC")
	up.append("DASH")
	up.append("XLM")
	up.append("ZEC")
	up.append("ETC")
	up.append("XMR")
	checkSymbols(up)
	print "ten check completed"

def checkTwenty():
	up = []
	up.append("NEO")
	up.append("MAID")
	up.append("LSK")
	up.append("QTUM")
	up.append("ADA")
	up.append("GNO")
	up.append("EOS")
	up.append("OMG")
	up.append("STEEM")
	up.append("PIVX")
	up.append("DCR")
	up.append("SALT")
	up.append("WAVES")
	up.append("PAY")
	up.append("SIA")
	up.append("STRAT")
	up.append("ARK")
	up.append("DOGE")
	up.append("CVC")
	up.append("IOT")
	checkSymbols(up)
	print "twenty check completed"

def checkEth():
	up = []
	up.append("OMG")
	up.append("EOS")
	up.append("REP")
	up.append("PAY")
	up.append("SALT")
	up.append("GNT")
	up.append("BAT")
	up.append("WTC")
	up.append("KNC")
	up.append("MTL")
	up.append("BNB")
	up.append("ICN")
	up.append("FUN")
	up.append("GNO")
	up.append("CVC")
	up.append("SNT")
	up.append("ZRX")
	up.append("SNGLS")
	up.append("BNT")
	up.append("MCO")
	up.append("AE")
	up.append("ANT")
	checkSymbols(up)
	print "eth check completed"

def checkForty():
	up = []
	up.append("BAT")
	up.append("BCN")
	up.append("ARDR")
	up.append("REP")
	up.append("MONA")
	up.append("GNT")
	up.append("KMD")
	up.append("WTC")
	up.append("KNC")
	up.append("BTS")
	up.append("MTL")
	up.append("FCT")
	up.append("ICN")
	up.append("BNB")
	up.append("DOGE")
	up.append("SC")
	up.append("GAME")
	up.append("FUN")
	up.append("CVC")
	up.append("ETP")
	up.append("ZRX")
	up.append("SNT")
	up.append("SNGLS")
	up.append("BNT")
	up.append("SYS")
	up.append("MCO")
	up.append("DGB")
	up.append("AE")
	up.append("NXS")
	up.append("XVG")
	up.append("VTC")
	up.append("NXT")
	up.append("ANT")
	up.append("ADX")
	up.append("PART")
	up.append("UBQ")
	up.append("MLN")
	up.append("ENG")
	up.append("EDG")
	up.append("WINGS")
	checkSymbols(up)
	print "forty check complete"

def checkSymbols(up):
	paramStr = ""
	size = len(up)

	for i in range(0,size):
		paramStr = paramStr + up[i]
		if (i < size-1):
			paramStr = paramStr + ","

	api_url = base_url + paramStr + "&tsyms=USD"

	resp = requests.get(api_url)
	jsonResp = resp.json()

	for coin in jsonResp:
		if coin not in up:
			print coin + " not supported"


checkTen()
checkTwenty()
checkEth()
checkForty()