
def main():
    while True:
            p = convert(input("Fraction: "))
            if p < 100:
                break
    print(gauge(p))



def convert(fraction):
    while True:
        try:
            x,y = fraction.split("/")
            if x > y:
                raise ValueError
            else:
                return int((int(x)/int(y))*100)
        except (ZeroDivisionError, ValueError):
            fraction = input("Fraction: ")




def gauge(percentage):
    if percentage <= 1:
        return("E")
    elif percentage >= 99:
        return ("F")
    else:
        return(f"{percentage}%")


if __name__ == "__main__":
    main()