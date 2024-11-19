def main():
    text=input("Input: ")
    print(shorten(text))


def shorten(word):
    vowels=["a","e","i","o","u"]
    tweet = ""
    for c in word:
        if c.lower() not in vowels:
            tweet += c.lower()
    return tweet

if __name__ == "__main__":
    main()
