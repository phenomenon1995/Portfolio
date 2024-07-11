def main():
    m = int(input("m: "))
    print(f"E: {maff(m)}")
def maff(m):
    c = int(300000000)
    return m * pow(c,2)
main()