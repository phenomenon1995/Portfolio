def main():
    phrase= input("Greeting: ")
    print(f"${value(phrase)}")

def value(greeting):
    greeting = greeting.lower().strip()
    if greeting.find("hello",0,6)==-1:
        if greeting.find("h", 0, 1)!=-1:
            return 20
        else:
            return 100
    else:
        return 0

if __name__ == "__main__":
    main()