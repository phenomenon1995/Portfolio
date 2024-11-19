def main():
    grocery_list= {}
    while True:
        try:
            item=input().upper()
            if item not in grocery_list:
                grocery_list[item]= 1
            else:
                grocery_list[item]+=1

        except (EOFError):
            organize_list(grocery_list)
            break



def organize_list(groceries):
    print_order = sorted(list(groceries.keys()))

    for position in print_order:
        print(f"{groceries[position]} {position}")

main()