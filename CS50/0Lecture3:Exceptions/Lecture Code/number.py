def main():
    x = get_int("What's X? ")
    print(f"x is {x}")

def get_int(prompt):
    while True:
        try:
            x = int(input(prompt))
            #anything after this will excute if no error, if error, then it goes to except
            #doesn't try the entire try statement first, goes to except as soon as an arror happens
        except ValueError:
            pass #do don't anything
        else:
            return x #return also breaks

main()