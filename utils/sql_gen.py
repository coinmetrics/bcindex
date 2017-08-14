INDEX = "even_twenty"

def create_line(cur, next):
	bidpos = 1
	btcpos = 2
	usdpos = 3
	s = "update "+ INDEX + " set index_value_btc = " + str(cur[btcpos]) + ", index_value_usd = " + str(cur[usdpos]) + " where bletch_id > " + str(cur[bidpos]) + " and bletch_id < " + str(next[bidpos]) + ";\n" 
	return s

##### BEGIN MAIN SCRIPT #####
data = open('even_px_and_bid.txt', 'r')
outfile = open('update_even_usd_px.sql','w')

lines = data.readlines()

# remove \n (strip), split by tab \t into array
for i in range(0,len(lines)):
	lines[i] = lines[i].strip().split('\t')

for i in range(0,len(lines)-1):
	outfile.write(create_line(lines[i],lines[i+1]))

