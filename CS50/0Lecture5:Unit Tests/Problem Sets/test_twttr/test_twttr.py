from twttr import shorten

def main():
    test_ALLCAPS()
    test_all_lower()
    test_numbers()
    test_mixed_case()

def test_ALLCAPS():
    assert shorten("TWITTER IS LAME.") == "twttr s lm."

def test_all_lower():
    assert shorten("twitter is lame.") == "twttr s lm."

def test_numbers():
    assert shorten("1234 5678") == "1234 5678"

def test_mixed_case():
    assert shorten("tWiTtEr Is LaMe.") == "twttr s lm."



if __name__ == "__main__":
    main()