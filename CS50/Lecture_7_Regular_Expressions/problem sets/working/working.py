import re
import sys


def main():
    tests = ["9 AM to 5 PM", "9:00 AM to 5:00 PM", "10 PM to 8 AM",
             "9:60 AM to 5:60 PM","10 PM  8 AM", "12 AM to 12 PM"]
    print(convert(input("Hours: ")))
    #print(convert(tests[int(sys.argv[1])]))


def convert(s):
    times = re.search(r"(\d{1,2})(?: |:)(\d{1,2})? ?(AM|PM) to (\d{1,2})(?: |:)(\d{1,2})? ?(AM|PM)", s)
    if times :
        if None in times.groups(): # if hh AM format
            if (int(times.group(1)) > 12 or int(times.group(4)) > 12): #range check
                raise ValueError
            return am_to_pm(times)

        else: #hh:mm format
            if (int(times.group(1)) > 12 or int(times.group(4)) > 12
                or int(times.group(2)) > 59 or int(times.group(5)) > 59
                ): #range check
                raise ValueError
            return am_to_pm(times)

    else:
        raise ValueError


def am_to_pm(times):
    h1, m1, p1, h2, m2, p2 = times.groups()
    if (m1 == None or m2 == None):
        m1, m2 = "00"
    if (p1 == "AM" and h1 == "12"):
        h1 = int(0)
    if (p2 == "AM" and h2 == "12"):
        h2 = int(0)

    if p1 == "PM":
        if h1 == "12":
            pass
        else:
            h1 = int(h1) + 12
    if p2 == "PM":
        if h2 == "12":
            pass
        else:
            h2 = int(h2) + 12


    return f"{int(h1):02}:{int(m1):02} to {int(h2):02}:{int(m2):02}"


if __name__ == "__main__":
    main()
