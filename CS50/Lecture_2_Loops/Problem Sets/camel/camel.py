def main():
    text=input("camelCase: ")
    camel_to_snake(text)

def camel_to_snake(phrase):
    for c in phrase:
        if c.islower()==True:
            print(c,end="")
        else:
            print("_",c.lower(),sep="", end="")
    print()
main()