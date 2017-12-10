"""
    script to create db insert statements for
    manual testing using the db to add data
"""

def create_line(id, bid, usd, btc, time):
	s = "insert into ODD_INDEX VALUES (" + str(id) + ", " + str(bid) + ", " + str(btc) + ", " + str(usd) + ", " + str(time) + "); " 
	return s

##### BEGIN MAIN SCRIPT #####
size = 60
db_id = 1756
b_id = 1756
usd = 4057.58712731161
btc = 1.44023452417789
minute = 60000 * 24
time = 1512890382560 + minute 


for i in range(0,size):
    print create_line(db_id, b_id, usd, btc, time)
    db_id = db_id + 1
    b_id = b_id + 1
    time = time + minute
    if i % 2 == 0:
        usd = usd + 5
        btc = btc + 5
    else:
        usd = usd - 5
        btc = btc - 5
