import validators

def main():
    tests = ["malan@harvard.edu", "phenomenon1995@gmail.com",
             "malan@@@harvard.edu", "phenomenon1995@gmail..com"]

    email = validators.email(input("What's your email address? "))
    if email == True:
        print("Valid")
    else:
        print("Invalid")



if __name__ == "__main__":
    main()
