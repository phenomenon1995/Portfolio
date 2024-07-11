from datetime import date
import inflect
import sys
import re

def main():
    birthdate = to_birthdate(input("Date of Birth: "))
    print(to_minutes(birthdate))

def to_minutes(dob):
    today = date.today()
    timedelta = today - dob
    minutes = timedelta.days * 1440
    p = inflect.engine()
    return (f"{p.number_to_words(minutes, andword = "").capitalize()} minutes")

def to_birthdate(dob):
    matches = re.match(r"(\d{4})-(\d{2})-(\d{2})", dob)
    if matches:
        try:
            year, month, day = matches.groups()
            birthdate = date(int(year), int(month), int(day))
            return birthdate
        except ValueError:
            sys.exit("Invalid date")
    else:
        sys.exit("Invalid date")



...


if __name__ == "__main__":
    main()
