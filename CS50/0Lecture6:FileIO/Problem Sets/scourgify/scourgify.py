import csv
import sys
def main():
    arg_check()
    #open file and read each row add, dict to list
    with open(sys.argv[1]) as existing_file:
        Students = []
        reader=csv.DictReader(existing_file, fieldnames = ["name", "house"])

        for row in reader:
            name = row["name"].split(",")
            last = name[0].strip()
            first = name[-1].strip()
            Students.append({"first" : first , "last" : last, "house" : row["house"]})

        with open(sys.argv[2], "w") as new_file:
            writer = csv.DictWriter(new_file, fieldnames = ["first", "last", "house"])
            writer.writeheader()
            for c in Students[1:]:
                writer.writerow(c)

def arg_check():
    if len(sys.argv) < 3:
        sys.exit("Too few command-line arguments")
    if len(sys.argv) > 3:
        sys.exit("Too many command-line arguments")
    try:
        with open(sys.argv[1]) as existing_file:
            reader = csv.DictReader(existing_file, fieldnames=["name", "house"])
    except FileNotFoundError:
        sys.exit(f"Could not read {sys.argv[1]}")



if __name__ == "__main__":
    main()
