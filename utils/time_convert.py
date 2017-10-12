from datetime import datetime

def to_readable(raw):
  raw = raw 
  exp = raw	
  dt = datetime.fromtimestamp(exp/1000)

  s = dt.strftime('%Y-%m-%d %H:%M:%S')
  s += '.' + str(int(raw % 100000000000)).zfill(9)
  return s

print "min: " + to_readable(1496905847973)
print "t1:: " + to_readable(1496992247973)
print "t2:: " + to_readable(1497078647973)
print "max: " + to_readable(1497092859065)
