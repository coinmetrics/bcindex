import csv
import datetime

# id, bletch_id, btc, usd, time_stamp

def to_timestamp(time):
    return datetime.datetime.fromtimestamp(time/1000).strftime('%Y-%m-%d %H:%M:%S')  

print(to_timestamp(1526182050222))

bletch_id = 598412

with open('stage_ten_4_day.csv','rb') as csvfile:
    creader = csv.reader(csvfile, delimiter=',')
    for row in creader:
        if row[4] != 'time_stamp':
            t = long(row[4])
            if t > 1526182050222:
                # print(str(bletch_id) + ", " + row[2] + ", " + row[3] + ", " + to_timestamp(t))
                print(str(bletch_id) + ", " + row[2] + ", " + row[3] + ", " + str(t))
                bletch_id = bletch_id + 1
