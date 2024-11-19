import sys

if len(sys.argv)<2:
    sys.exit("Too few arguments")

for arg in sys.argv[1:]:
    print("Hello, my Name is", arg)
#use if, elif to prevent index error or use an exception, if you add quotes, python treats it as a single thing, even if spaces