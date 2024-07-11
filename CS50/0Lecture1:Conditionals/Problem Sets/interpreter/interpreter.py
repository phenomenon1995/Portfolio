def main ():
    x,y,z=input("Expression: ").split(" ")
    print(maff(x,y,z))
def maff(value1, operand, value2):
    if operand=="+":
        answer= float(int(value1))+float(int(value2))
    elif operand=="-":
        answer= float(int(value1))-float(int(value2))
    elif operand=="/":
        answer= float(int(value1))/float(int(value2))
    elif operand=="*":
        answer= float(int(value1))*float(int(value2))
    return answer
main()