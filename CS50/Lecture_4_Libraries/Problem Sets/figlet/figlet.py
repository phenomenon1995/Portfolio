import sys
from pyfiglet import Figlet
figlet = Figlet()
Fonts = figlet.getFonts()

def main():
    txt = input("Input: ")
    print(figlet.renderText(txt))



if len(sys.argv)==1:
    main()
elif len(sys.argv)==3:
    if str(sys.argv[1]) == ("-f" or "--font"):
        if sys.argv[2] in Fonts:
            figlet.setFont(font=sys.argv[2])
            main()
        else:
            sys.exit("invalid usage")
    else:
        sys.exit("invalid usage")
else:
    sys.exit("invalid usage")

