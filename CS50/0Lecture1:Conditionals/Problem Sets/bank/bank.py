def main():
    greeting= input("Greeting: ").lower().strip()
    print(isHello(greeting))
def isHello(phrase):
    if phrase.find("hello",0,6)==-1:
        if phrase.find("h", 0, 1)!=-1:
            return "$20"
        else:
            return "$100"
    else:
        return '$0'
main()