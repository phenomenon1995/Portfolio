def main():
    time=convert(input("What Time Is It?:  " ))
    if 7 <= time <= 8:
        print("breakfast time")
    elif 12 <= time <= 13:
        print("lunch time")
    elif 18 <= time <= 19:
        print("dinner time")


def convert(time):
    hours, minutes = time.split(":")
    newTime=int(hours) + (int(minutes)/60)
    return newTime

if __name__ == "__main__":
    main()