def main():
    plate = input("Plate: ")
    if is_valid(plate):
        print("Valid")
    else:
        print("Invalid")


def is_valid(s):
    numbers=("0","1","2","3","4","5","6","7","8","9")
    digits = []
    if s[0:2].isalpha() == True:
        if 1<len(s)<7:
            if s.isalpha() == True:
                return True
            else:
                if s.isalnum()==True:
                    if s.endswith(numbers) == True:
                        for c in s:
                            if c.isdigit()==True:
                                digits += c
                        if digits[0] != "0":
                            return True
                        else:
                           return False
                    else:
                        return False
                else:
                    return False
        else:
            return False
    else:
        return False

if __name__ ==  "__main__" :
    main()