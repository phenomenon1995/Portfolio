import inflect
p=inflect.engine()
#join words into a list
def main():
    names=[]
    while True:
        try:
            names.append(input("Name: "))
        except(EOFError):
            break
    print("Adieu, adieu, to", p.join(names))






if __name__ == "__main__":
    main()