#with open("names.txt", "r") as file:
    #file.write(F"{name} \n") #you could use var=file.open, then file.close instead, this way auto closes
    # lines = file.readlines() returns all lines as a list, then you can use loop to print

names = []
with open("names.txt") as file:
    for line in file:
        names.append(line.rstrip())
for name in sorted(names):
    print(f"Hello, {name}")