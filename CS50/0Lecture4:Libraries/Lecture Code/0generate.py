#can also import random to keep function choice() scoped to the random library as random.choice to prevent
#conflicts, or use from LIBRARY import FUNCTION syntax to enter function name into your namespace so you can call just FUNCTION()
#instead of LIBRARY.FUNCTION()
import random

cards = ["J", "Q", "K"]
random.shuffle(cards)
for card in cards:
    print(card)
