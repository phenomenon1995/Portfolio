def main():
    answer=input("Gimme the Answer my guy: ").strip().lower()
    match answer:
        case "42"|"forty two"|"forty-two":
            print("Yes")
        case _:
            print("No")
main()