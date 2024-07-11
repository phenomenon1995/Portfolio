import plates
from plates import is_valid

def test_beginning():
    assert is_valid("23ASDF") == False
    assert is_valid("AA1234") == True
def test_length():
    assert is_valid("asdfghhjk") == False
def test_number_placement():
    assert is_valid("ASD23S") == False
def test_alnum():
    assert is_valid("sad.13") == False
def test_zeroes():
    assert is_valid("asd012") == False

