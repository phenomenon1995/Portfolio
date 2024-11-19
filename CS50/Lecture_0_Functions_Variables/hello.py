#defining a main function where the bulk of code runs, and then any other functions that main might use, allows
#for better organization of defined functions as code becomes longer and such
def main():

#Ask user for name, then strip whitespace and convert to title case, but you can also separate the functions
    name = input("What's your name? ").strip().title()

#split user name into first and last names
#if function returns multiple values, you can bulk assign to variables left to right, first ot last, with commmas
    first, last = name.split(" ")

# Say hello to user, use f string to use variable in a single argument with var in {}
#print (f"Hello, {first}.")

#function call, one with first var, other with no argument to test default
    hello(first)
    hello()
#create a function using def, colon at the end means all indented below means it's a part of function
def hello(to="World"):
    print("Hello, ", to, ".", sep='', end='\n')

main()