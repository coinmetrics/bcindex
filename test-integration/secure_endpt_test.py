import requests

ENV = "http://localhost:8090"
endpt = ENV + "/api/index/protected"

def testInvalidKey():
  data = {'index':'FORTY_INDEX', 'currency':'USD','timeFrame':'DAILY'}

  response = requests.post(endpt, json=data)
  assert 403 == response.status_code
  assert "The key you entered is not valid" == response.text


def testValidKey():
  data = {'index':'FORTY_INDEX', 'currency':'USD','timeFrame':'DAILY', 'apiKey':'abcnomics'}  
    
  response = requests.post(endpt, json=data)
  assert 200 == response.status_code
  assert "FORTY_INDEX" == response.json()['index']


### begin main script
print("\nrunning secure_endpt_test")
mark = "====>"
print(mark + "running test invalid key")
testInvalidKey()
print(mark + "running test a valid key")
testValidKey()
print("SUCCESS")



