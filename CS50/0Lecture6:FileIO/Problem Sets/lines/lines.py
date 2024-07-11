import sys

def main():
    command_args_check()
    Inputted_Filename = sys.argv[1]
    try:
        with open(Inputted_Filename) as user_file:
            stored_lines= {"line", "count"}
            line_count = 0
            for line in user_file:
                if line.lstrip().startswith("#") == True or line.isspace() == True:
                    pass
                else:
                    line_count += 1
            print(line_count)
    except FileNotFoundError:
        sys.exit("File does not exist")


def command_args_check():
    if len(sys.argv) < 2:
        sys.exit("Too few arguments")
    if len(sys.argv) > 2:
        sys.exit("Too many arguments")
    if sys.argv[1][-3:] != ".py":
        sys.exit("Not a python file")

if __name__ == "__main__":
    main()
