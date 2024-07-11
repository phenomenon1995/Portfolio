from working import convert
import pytest

def main():
    test_valid()
    test_range()
    test_format()
    test_dash()

def test_valid():
    assert convert("9 AM to 5 PM") == "09:00 to 17:00"
    assert convert("9:00 AM to 5:00 PM") == "09:00 to 17:00"
    assert convert("10 PM to 8 AM") == "22:00 to 08:00"
    assert convert("10:30 PM to 8:50 AM") == "22:30 to 08:50"

def test_range():
    with pytest.raises(ValueError):
        convert("9:60 AM to 5:60 PM")

def test_format():
    with pytest.raises(ValueError):
        convert("9 CAT to 3 CAT")
    with pytest.raises(ValueError):
        convert("12 AM 12 PM")


if __name__ == "__main__":
    main()

