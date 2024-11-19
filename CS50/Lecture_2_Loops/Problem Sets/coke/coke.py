def main():
    valid=[5,10,25]
    balance=50
    while balance > 0:
        print ("Amount Due:", balance)
        #coin=int(input("Insert Coin: "))
        while True:
            coin=int(input("Insert Coin: "))
            for c in range(len(valid)):
                if coin == valid[c]:
                    balance = balance - coin
            break


    print("Change Owed:", abs(balance))
main()