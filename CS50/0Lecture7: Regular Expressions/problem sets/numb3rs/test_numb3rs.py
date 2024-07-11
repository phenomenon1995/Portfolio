from numb3rs import validate


def main():
    test_word()
    test_less_groups()
    test_more_groups()
    test_range()
    test_first()

def test_word():
    assert validate("cat") == False

def test_less_groups():
    assert validate("1.1.1") == False
    assert validate("1.      1. 1") == False
    assert validate("1.    1. 1") == False
    assert validate("127.0.0.1") == True

def test_more_groups():
    assert validate("1.1.1.1.1") == False
    assert validate("255.255.255.255") == True


def test_range():
    assert validate("512.512.512.512") == False
    assert validate("1.2.3.1000") == False


def test_first():
    assert validate("5") == False
    assert validate("5.981.5.5") == False

if __name__ == "__main__":
    main()
