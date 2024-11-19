def main():
    while True:
        percent=get_percent("Fraction: ")
        if percent <= 100:
            break
    if percent <= 1:
        print("E")
    elif percent >= 99:
        print("F")
    else:
        print(f"{percent}%")
def get_percent(fraction):
        while True:
            try:
                x,y=input(fraction).split("/")
                return int((int(x)/int(y))*100)
            except (ValueError, ZeroDivisionError):
                pass

main()
