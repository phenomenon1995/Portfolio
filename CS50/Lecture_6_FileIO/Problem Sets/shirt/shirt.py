import sys
from PIL import Image, ImageOps
import os

def main():
    arg_check()

    shirt = Image.open("shirt.png")
    size = shirt.size
    input_image = Image.open(f"{sys.argv[1]}")

    cropped_image = ImageOps.fit(input_image, size , centering = (.5, .5))
    cropped_image.paste(shirt, shirt)
    cropped_image.save(f"{sys.argv[2]}")



def arg_check():
    if len(sys.argv) < 3:
        sys.exit("Too few command-line arguments")
    if len(sys.argv) > 3:
        sys.exit("Too many command-line arguments")

    accepted = [".jpg", ".jpeg", ".png"]
    file_name1 = os.path.splitext(sys.argv[1])
    file_name2 = os.path.splitext(sys.argv[2])

    if file_name1[1] in  accepted:
        if file_name2[1] in accepted:
            if file_name1[1] == file_name2[1]:
                pass
            else:
                sys.exit("Invalid input")
        else:
            sys.exit("Invalid input")
    else:
        sys.exit("Invalid input")

    try:
        image = Image.open(f"{sys.argv[1]}")

    except FileNotFoundError:
        sys.exit(f"Could not read {sys.argv[1]}")


if __name__ == "__main__":
    main()
