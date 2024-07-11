# == for equality, != for not equal,
x = int(input("What's X? "))
y = int(input("What's Y? "))
# if statements don't need parentheses, but you do need to colon and indent like with functions
#else if is called elif in python, start with an if, then add elif for the additional diddies, else for the last one if applicable
def option1():
    if x < y:
        print("x is less than y")
    elif x>y:
        print("x is greater than y")
    else:
        print("x is equal to y")
# if...or: then you can do else
def option2():
    if x<y or x>y:
        print("not equal")
    else:
        print("equal")
def option3():
    if x!=y:
        print("not equal")
    else:
        print("equal")
option3()