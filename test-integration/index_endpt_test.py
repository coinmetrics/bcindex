import requests

ENV = "http://localhost:8090"
endpt = ENV + "/api/index/"


def test_index():
  data = {'index':'FORTY_INDEX', 'currency':'USD','timeFrame':'DAILY'}  
    
  response = requests.post(endpt, json=data)

  assert 200 == response.status_code
  assert "FORTY_INDEX" == response.json()['index']


### begin main script
print("\nrunning secure_endpt_test")
mark = "====>"
print(mark + "running test_index")
test_index()
print("SUCCESS")




