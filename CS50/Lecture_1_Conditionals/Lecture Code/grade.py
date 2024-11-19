score=int(input("Gimme Score: "))
def option1():
    if score >=90 and score <=100:
        print("A baybeeee")
    elif score >=80 and score <90:
        print("B baybeeee")
    elif score >=70 and score <80:
        print("C baybeeee")
    elif score >=60 and score <70:
        print("D baybeeee")
    else:
        print("you are a failure")
#python lets you nest comparison operators
def option2():
    if 90<=score<=100:
        print("A baybeeee")
    elif 80<=score<90:
        print("B baybeeee")
    elif 70<=score<80:
        print("C baybeeee")
    elif 60<=score<70:
        print("D baybeeee")
    else:
        print("you are a failure")
def option3():
    if score>=90:
        print("A baybeeee")
    elif score>=80:
        print("B baybeeee")
    elif score>=70:
        print("C baybeeee")
    elif score>=60:
        print("D baybeeee")
    else:
        print("you are a failure")
option3()