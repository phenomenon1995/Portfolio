import random
def check(level):
    while True:
        try:
            level=int(input("Level: "))
            if int(level)> 0:
                break
        except ValueError:
            pass

def main():
    while True:
        try:
            level=int(input("Level: "))
            if int(level)> 0:
                break
        except ValueError:
            pass

#generate a random number between 1 and level
    rng = random.randrange(1, level + 1)
    guess= pick(level)


    while rng != guess:
        if rng > guess:
            print("Too small!")
            guess = pick(level)
        if rng < guess:
            print("Too large!")
            guess = pick(level)
    print("Just right!")

def pick(max):
    while True:
        try:
            guess=int(input("Guess: "))
            if max >= int(guess)>=0:
                return guess
        except (ValueError):
            pass

if __name__ == "__main__":
    main()
