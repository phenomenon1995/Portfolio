import re
import sys


def main():
    test_strings = [
        '<iframe src="http://www.youtube.com/embed/xvFZjo5PgG0"></iframe>',
        '<iframe width="560" height="315" src="https://www.youtube.com/embed/xvFZjo5PgG0" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>',
        '<iframe width="560" height="315" src="https://cs50.harvard.edu/python"></iframe>',
    ]
    #print(parse(input("HTML: ")))
    print(parse(test_strings[int(sys.argv[1])]))

def parse(s):
    url=re.search(r'(http(?:s?)://(?:www\.)?youtube\.com/embed/(.*?))"',s)
    if url:
        video = url.group(2)
        return f"https://youtu.be/{video}"


...


if __name__ == "__main__":
    main()
