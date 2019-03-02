import requests
import json
import time

def toLocalDate(epoch_str):
    epoch = int(epoch_str)
    seconds = epoch/1000
    return time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(seconds))


def get_data(index):
    endpt = "http://stage-index-api.herokuapp.com/daily/weight"
    data = {'index':index}
    return requests.post(endpt, json=data).json()


def convert(data):
    # from key=value json --> weight: "btc=0.4343,bsv=3.4241,..."
    pass


def main():
    index = "TEN"

    resp = get_data(index)

    sql_script = []
    index_name = resp['index']

    # print(resp)
    bid = 1

    text_file = open("output.txt", "w")

    for element in resp['elementList']:
        stmt = "INSERT INTO WEIGHTS_" + index + "(ID, BLETCH_ID, INDEX_NAME, time_stamp, weights) VALUES ("
        stmt = stmt + str(bid) + ", " + str(bid) + ", '" + index_name + "', " + str(element['time']) + ", '" + str(convert(element['dataMap'])).replace("'", "\"") + "'" 
        stmt = stmt + ");\n"
        text_file.write(stmt)
        bid = bid + 1

    text_file.close()


"""
    insert  into weights_ten (ID, BLETCH_ID, INDEX_NAME, time_stamp, weights) 
    values (3, 3, 'TEN', 1551422468850, 
    '{"weight":"BTC=0.6571134962447712,BSV=0.011455686209046665,BCH=0.02250653890135153,XRP=0.06759417563903954,ETH=0.14049368729727077,XLM=0.015920422083079235,EOS=0.031357619613411374,LTC=0.027140982373841934,TRX=0.015519621330713305,ADA=0.010897770307474412,","weightEven":"BTC=0.6408165519838728,BSV=0.0111715759591643,BCH=0.021948358599048867,XRP=0.06591778561045843,ETH=0.13700933063137322,XLM=0.015525582785483567,EOS=0.03057992537654873,LTC=0.026467864138620407,TRX=0.01513472221477326,ADA=0.0106274968215638,"}')

"""

if  __name__ =='__main__':
    main()

