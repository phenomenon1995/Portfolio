def main():
    text=input("Input: ")
    strip(text)
def strip(whole):
    vowels=["a", "e","i","o","u"]
    for c in whole:
        if c.lower() not in vowels:
            print(c, sep="",end="")


main()