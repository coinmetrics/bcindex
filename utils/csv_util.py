

# file = raw_input("enter path to csv:")
file = "../info/historical_data/index-daily-hist/10_index.csv"

timepos = 0
btcpos = 1
usdpos = 2
evbtcpos = 3
evusdpos = 4

delim = ","
id = 0

format = "%m/%d/%y %H:%M"

ten = open('index_hist/ten.csv', 'w')
ten_even = open ('index_hist/even_ten.csv', 'w')

csvfile = open(file, 'r')
for line in csvfile:

	if id != 0:

		line = line.rstrip()
		arr = line.split(delim)

		newline = str(id) + delim + arr[btcpos] + delim + arr[usdpos] + delim + arr[timepos] + "\n"
		ten.write(newline)

		neweven = str(id) + delim + arr[evbtcpos] + delim + arr[evusdpos] + delim + arr[timepos] + "\n"
		ten_even.write(neweven)

	id = id + 120