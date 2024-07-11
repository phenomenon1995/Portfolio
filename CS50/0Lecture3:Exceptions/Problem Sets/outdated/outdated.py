def main():
    name_months= ["January","February","March","April","May","June","July","August",
                  "September", "October","November","December"]
    while True:
        try:
            usr= input("Date: ").strip()
            if usr[0:1].isalpha() == True:
                if usr.find(",") != -1:
                    month, day, year = usr.title().split()
                    day = day.strip(",")
                    month = str(int(name_months.index(month)) + 1)
            else:
                month, day, year = usr.title().split("/")
                                #checks for format and splits properly
            if int(day)<32:
                if 0 < int(month) < 13:
                    break
        except (ValueError, UnboundLocalError):
            pass

    print(year, month.zfill(2), day.zfill(2), sep="-")


main()