#assign list to a variable by using []
#in python, you do not need to initialize the variable you want to use for loop, happens automatically
#len() returns length of list
#if you need to increment index of a list, nest range(len())
#dict data types is used for associations, assign to variables using {" ":" ",} can be multi-line
#use str in dict instead of int to  choose index within dictionary
students= [{"name":"Hermione", "house":" Gryffindoor", "patronus":"Otter"},
           {"name":"Harry", "house":" Gryffindoor", "patronus":"Stag"},
           {"name":"Ron", "house":" Gryffindoor", "patronus":"Jack Russell Terrier"},
           {"name":"Draco", "house":" Slytherine", "patronus": None}
           ]
for student in students:
    print (student["name"], student["house"], student["patronus"], sep=", ")