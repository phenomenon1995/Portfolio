import emoji
def main():
    txt=input("Input: ")
    print(emoji.emojize(txt, language="alias"))
main()