def main():
    plate = input("Plate: ")
    if is_valid(plate):
        print("Valid")
    else:
        print("Invalid")


def is_valid(s):
    if first_two_letters(s)==True:
        if plate_length(s)==True:
            if numbers_at_end(s)==True:
                if s.isalnum()==True:
                    return True
    else:
        return False

def plate_length(plate):
    if 1<len(plate)<7:
        return True
    else:
        return False

def first_two_letters(plate):
    return plate[0:2].isalpha()

def numbers_at_end(plate):
    numbers=[]
    index= None
    for c in plate:
        if c.isdigit()==True:
            numbers+=c
            if index == None:
                index=plate.index(c)
    #print(numbers, index)
    if index==None:
        #print("only letters: ")
        return True
    else:
        if plate[index:].isdigit()==False:
            #contains letters after numbers
            #print("letters after numbers ", plate[index:])
            return False
        else:
            #check for leading zero
            if numbers[0]== "0":
                return False
            else:
                return True




#This bullshit grabs index of where numbers start, creates new sting for the rest of the plate, and checks that the
#string only has digits in it.
    #if plate[plate.index(numbers1.join(numbers)):].isdigit()==True:

main()