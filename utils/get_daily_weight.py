import requests
import json
import time

def toLocalDate(epoch_str):
    epoch = int(epoch_str)
    seconds = epoch/1000
    return time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(seconds))


def get_data(index, endpt):
    # handle mapping between api and db
    if index == "ETHER":
        index = "ETHEREUM"
    elif index == "ETHER_EVEN":
        index = "ETHEREUM_EVEN"
    elif index == "APP":
        index = "APPLICATION"

    data = {'index':index}
    resp = requests.post(endpt, json=data)
    if resp.status_code != 200:
        raise Exception("error getting " + index + str(resp.text))
    return resp.json()


def convert(data):
    weight = {}
    data_str = ""
    for key in data:
        data_str = data_str + str(key) + "=" + str(data[key]) + ","
    weight['weight'] = data_str[0:-1] # the data string minus the last ,
    return json.dumps(weight)


def get_and_upload_data_all(get_endpt):
    upload_endpt = "https://bc-index-api.herokuapp.com/blet/weight/daily"
    # upload_endpt = "http://localhost:8085/blet/weight/daily"

    indexes = ["TEN","TEN_EVEN","TWENTY","TWENTY_EVEN","FORTY","FORTY_EVEN","TOTAL","TOTAL_EVEN","ETHER","ETHER_EVEN","APP","CURRENCY","PLATFORM"]
    # TODO fix but with running all indexes at once, some even indexes are left out, and you get a http 500 error
    indexes = ["ETHER","ETHER_EVEN"]
    all_data = {}
    post_list = {}

    time = 0
    size = 0
    # get all data

    for index in indexes:
        resp = get_data(index, get_endpt)
        size = len(resp['elementList'])
        vars_init = False

        if not vars_init:
            print("idx is ten")
            # its the first time do initializations
            for i in range(0,size):
                post_data = {}
                weights = {}
                time = resp['elementList'][i]['time']
                rIdx = resp['index']

                post_data['time'] = time
                weights[rIdx] = resp['elementList'][i]['dataMap']
                post_data['indexes'] = weights
                post_data['key'] = 'fkdjfkdjfdjfkjfdk'
                post_list[time] = post_data
            vars_init = True
        # things are initialize, grab them from post_list
        else:            
            for i in range(0,size):
                time = resp['elementList'][i]['time']
                rIdx = resp['index']

                post_data = post_list[time]
                post_data['key'] = 'fkdjfkdjfdjfkjfdk'
                weights = post_data['indexes']
                weights[rIdx] = resp['elementList'][i]['dataMap']
                send_data(post_data,upload_endpt)                
    # print(json.dumps(post_list))
    pass


def send_data(json_data, api_endpt):
    resp = requests.post(api_endpt, json=json_data)
    # if resp.status_code != 200:
    #     print(json_data)
    print(resp) # alert the console to the status of the upload
    pass


def get_and_write_sql_for_idx(index, endpt):
    """
        example usage: get_and_write_data_for_idx("TEN", endpt)
    """
    # insert SQL command:  heroku pg:psql --app bc-index-api < output.sql
    text_file = open("output.sql", "w")

    resp = get_data(index, endpt)
    print(resp)

    sql_script = []
    index_name = index
    bid = 1

    for element in resp['elementList']:
        stmt = "INSERT INTO WEIGHTS_" + index + "(ID, BLETCH_ID, INDEX_NAME, time_stamp, weights) VALUES ("
        stmt = stmt + str(bid) + ", " + str(bid) + ", '" + index_name + "', " + str(element['time']) + ", '" + str(convert(element['dataMap'])).replace("'", "\"") + "'" 
        stmt = stmt + ");\n"
        text_file.write(stmt)
        bid = bid + 1

    text_file.close()
    pass


def main():
    endpt = "http://localhost:8085/daily/weight"
    endpt = "http://stage-index-api.herokuapp.com/daily/weight"
    # endpt = "http://bc-index-api.herokuapp.com/daily/weight"

    get_data(endpt)
    pass


if  __name__ =='__main__':
    main()

