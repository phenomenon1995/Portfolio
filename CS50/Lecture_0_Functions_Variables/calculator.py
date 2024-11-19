def main():
    x = float(input("Input X "))
    #y = float(input("Input Y "))
    #z = round(x / y, 2)
    print("x squared is", square(x))
def square(n):
    return pow(n,2)
#f strings have heckin parameters, that ish down there adds comma separators
#print(f"{z:,}")
#this one rounds to two decimal places
#print(f"{z: .2f}")

main()