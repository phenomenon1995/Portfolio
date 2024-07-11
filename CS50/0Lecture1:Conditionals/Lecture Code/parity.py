# % symbol is modulo/remainder symbo
# Bool operators must be capitilized, the return True/False can be used in the IF evaluation without needing conditional operators
x=int(input("gimme x: "))
def isEven(n):
    if n%2==0:
       return True
    else:
        return False
# instead of if else with four lines, do one like this --> return True if n % 2 == 0 else False
def isOdd(n):
    return n % 2 == 0 
def main():
    if isOdd(x):
        print("even")
    else:
        print("odd")

main()