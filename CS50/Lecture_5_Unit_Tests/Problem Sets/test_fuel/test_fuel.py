import pytest
from fuel import convert, gauge

def test_fraction():
    assert convert("4/4") == 100

def test_div0():
    with pytest.raises(ZeroDivisionError):
        convert("4/0")
def test_value_error():
    with pytest.raises(ValueError):
        convert("8/7")

def test_gauge():
    assert gauge(12) == "12%"
    assert gauge(1) == "E"
    assert gauge (99) == "F"
