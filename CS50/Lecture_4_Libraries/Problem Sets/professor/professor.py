import random
def main():
    q_num = 0
    score = 0
    while True:
        try:
            level = get_level()
            break
        except ValueError:
            pass
    x,y = generate_integer(level)
    strikes = 0
    while q_num < 10:
        try:
            answer = int(input(f"{x} + {y} = "))
            if answer == int(x + y):
                q_num += 1
                x,y=generate_integer(level)
                strikes = 0
                score += 1
            else:
                if strikes < 2:
                    print("EEE")
                    strikes += 1
                else:
                    print(f"{x} + {y} = {int(x+y)}")
                    x,y = generate_integer(level)
                    q_num += 1
                    strikes = 0
        except ValueError:
            if strikes < 2:
                print("EEE")
                strikes += 1
            else:
                print(f"{x} + {y} = {int(x+y)}")
                x,y = generate_integer(level)
                q_num += 1
                strikes = 0

    print("Score: ", score)


def get_level():
    while True:
        try:
            level = int(input("Level: "))
            if 4 > int(level )> 0:
                return level
                break
            if int(level) > 3:
                raise ValueError
        except ValueError:
            pass

def generate_integer(level):
    if level == 1:
        x = random.randrange(0, 10)
        y = random.randrange(0, 10)
    if level == 2:
        x = random.randrange(10, 100)
        y = random.randrange(10, 100)
    if level == 3:
        x = random.randrange(100, 1000)
        y = random.randrange(100, 1000)
    return x,y

if __name__ == "__main__":
    main()