import requests
import json

local = "http://localhost:8085/daily/weight"
endpt_day_weight = local
endpt_day_weight_nocache = endpt_day_weight + "/nocache";

def assert_daily_weightDb_isempty():
    assert_daily_weightDb_hasEntries(0)

def assert_daily_weightDb_hasEntries(num_entries):
    data = {'index':'TEN'}
    elist = requests.post(endpt_day_weight, json=data).json()['elementList']
    assert num_entries == len(elist)

def call_new_data():
    endpt_new_data = "http://localhost:8080/newdata"
    requests.get(endpt_new_data)

def forward_time(num_hours):
    endpt_move_time = "http://localhost:8080/forward_time"
    json_data = str(num_hours)
    requests.post(endpt_move_time, json=json_data)

print("running daily weight test")
mark = "====>"
print(mark + "assert weight api db is empty")
assert_daily_weightDb_isempty()

print(mark + "call new data")
call_new_data()

print(mark + "assert still empty")
assert_daily_weightDb_isempty()

print(mark + "forward time 25 hours")
forward_time(25)

print(mark + "assert weight api db is empty")
assert_daily_weightDb_isempty()

print(mark + "call new data again")
call_new_data()

print(mark + "assert 1 entry exists")
assert_daily_weightDb_hasEntries(1)

# call new data
# assert 1 entry exists
# forward time 26 hours
# call new data
#assert 2 entries exist