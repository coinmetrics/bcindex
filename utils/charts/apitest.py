import requests
import json
import datetime
import pandas as pd

def test_pandas():
    a1 = datetime.datetime.fromtimestamp(1483749570).strftime('%Y-%m-%d %H:%M:%S')
    a2 = datetime.datetime.fromtimestamp(1483835968).strftime('%Y-%m-%d %H:%M:%S')

    b1 = datetime.datetime.fromtimestamp(1483833600).strftime('%Y-%m-%d %H:%M:%S')
    b2 = datetime.datetime.fromtimestamp(1483920000).strftime('%Y-%m-%d %H:%M:%S')

    s1 = pd.Series(data=[100,101], index=[a1,a2])

    s2 = pd.Series(data=[90,105], index=[b1,b2])
    s3 = s1.combine_first(s2)

    print(s1)
    print('---')
    print(s2)
    print('---')
    print (s3)

def to_date(timeEpochSec):
    return datetime.datetime.fromtimestamp(timeEpochSec).strftime('%Y-%m-%d %H:%M:%S')


# resp = requests.get('https://api.blockchain.info/charts/n-unique-addresses?timespan=1year&daysAverageString=7&format=json')
# blockjson = resp.json()['values']

# btcresp = requests.get('http://coincap.io/history/365day/BTC')
# btcjson = btcresp.json()


# # convert raw data to x and y
# # as separate arrays for chart js
# btc = {}
# for data in btcjson['price']:
#     time = to_date(data[0]/1000)
#     btc[time] = data[1]


# addr = {}
# for data in blockjson:
#     time = to_date(data['x'])
#     addr[time] = data['y']

# print (addr)

test_pandas()
