import sys
import csv
from tabulate import tabulate

def main():
    command_args_check(".csv")
    with open(sys.argv[1]) as file:
        menu = []
        reader = csv.reader(file)
        for line in reader:
            menu.append(line)

    print(tabulate(menu, headers= "firstrow", tablefmt = "grid" ))

def command_args_check(type = ".py"):
    file_types = {".py": "python", ".csv": "CSV"}
    length = len(type)
    if len(sys.argv) < 2:
        sys.exit("Too few arguments")
    if len(sys.argv) > 2:
        sys.exit("Too many arguments")
    if sys.argv[1][-int(length):] != type:
        sys.exit(f"Not a {file_types[type]} file")

if __name__ == "__main__":
    main()
