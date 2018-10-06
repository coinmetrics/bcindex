import requests
import json

local = "http://localhost:8085/daily/weight"
endpt_day_weight = local
endpt_day_weight_nocache = endpt_day_weight + "/nocache";

def checkWeights(index):
    local = "http://localhost:8090/api/weight"
    data = {'index':index}
    return requests.post(local, json=data).json()

def call_new_data():
    endpt_new_data = "http://localhost:8080/newdata"
    requests.get(endpt_new_data)

def forward_time(num_hours):
    endpt_move_time = "http://localhost:8080/forward_time"
    json_data = str(num_hours)
    requests.post(endpt_move_time, json=json_data)

def assert_weight_add_to_one(weights):
    sum = 0.0
    for ticker,weight in weights.iteritems():
        sum = sum + weight
    if (sum != 1):
        print("error sum is not 1. It is: " + str(sum))
    else:
        print("SUCCESS")
        
    assert 1.0 == sum

print("running Real Time Weight test")
mark = "====>"
print(mark + "call new data")
call_new_data()

print(mark + "get weight data")
weights = checkWeights('TEN')

print(mark + "assert there are 10 tickers")
assert 10 == len(weights)

print(mark + "check the weights add up to 100")
assert_weight_add_to_one(weights)
