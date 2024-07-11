from seasons import to_birthdate, to_minutes
import pytest
import datetime

def main():
    test_valid()
    test_range()
    test_words()
    test_format()

def test_valid():
    assert to_minutes(to_birthdate("2022-12-15")) == "Five hundred twenty-five thousand, six hundred minutes"
    assert to_minutes(to_birthdate("2021-12-15")) == "One million, fifty-one thousand, two hundred minutes"

def test_range():
    with pytest.raises(SystemExit):
        to_birthdate("2022-13-01")
        to_birthdate("2022-1-33")
        to_birthdate("20022-12-01")
    assert to_birthdate("1995-07-27") == datetime.date(1995, 7, 27)

def test_words():
    with pytest.raises(SystemExit):
        to_birthdate("cat-07-27")
        to_birthdate("1995-cat-27")
        to_birthdate("1995-07-cat")

def test_format():
    with pytest.raises(SystemExit):
        to_birthdate("07-27-1995")
        to_birthdate("July 27th, 1995")
        to_birthdate("july-27-1995")




if __name__ == "__main__":
    main()
