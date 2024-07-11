import re
import sys


def main():
    print(validate(input("IPv4 Address: ")))



def validate1(ip):
    matches = re.search(r"^(\d{1,3}).(\d{1,3}).(\d{1,3}).(\d{1,3})$", ip)
    if matches:
        if len(matches.groups()) != 4:
            return False
        else:
           for m in matches.groups():
                if 0 <= int(m) <= 255:
                    pass
                else:
                    return False
        return True

    else:
        return False

def validate(ip):
    matches = re.split(r"\.", ip)
    if matches:
        if len(matches) != 4:
            return False
        else:
            for m in matches:
                try:
                    if 0 <= int(m) <= 255:
                        pass
                    else:
                        return False
                except ValueError:
                    return False
            return True

if __name__ == "__main__":
    main()
