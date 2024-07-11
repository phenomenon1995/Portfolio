def main():
    text = input ()
    print(convert(text))
def convert(text):
    smiled = text.replace(":)", "🙂")
    frowned = smiled.replace(":(", "🙁")
    return frowned
main()