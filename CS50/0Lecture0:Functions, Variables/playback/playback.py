def main ():
    text = input("Tell me something real quick: ")
    print(slowdown(text))
def slowdown(text):
    return text.replace(" ", "...")
def slowdown1(text):
    first, second, third= text.split(" ")
    x = "..."
    return first + x + second + x + third + x
main()