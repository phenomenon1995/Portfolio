import requests
import sys

if len(sys.argv)<2:
    sys.exit("Missing command-line argument")
if len(sys.argv)>2:
    sys.exit("Too many arguments")
try:
    float(sys.argv[1])
except ValueError:
    sys.exit("Command-Line argument not a number")

try:
    #get price of BTC from Coinbase API, store as a float in var 'r'. For nested keys, just add [key][key][key]
    r = float(requests.get("https://api.coindesk.com/v1/bpi/currentprice.json").json()["bpi"]["USD"]["rate_float"])
    n = float(sys.argv[1])
    print(f"${r * n:,.4f}")
except requests.RequestException:
    ...