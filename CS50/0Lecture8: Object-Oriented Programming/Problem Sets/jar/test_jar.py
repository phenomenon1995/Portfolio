from jar import Jar
import pytest


def test_init():
    with pytest.raises(ValueError):
        jar = Jar(-12)
    jar = Jar(12)
    assert str(jar) == ""


def test_str():
    jar = Jar()
    assert str(jar) == ""
    jar.deposit(1)
    assert str(jar) == "🍪"
    jar.deposit(11)
    assert str(jar) == "🍪🍪🍪🍪🍪🍪🍪🍪🍪🍪🍪🍪"


def test_deposit():
    jar = Jar(3)
    with pytest.raises(ValueError):
        jar.deposit("cat")
        jar.deposit(4)
        jar.deposit(-3)

def test_withdraw():
    jar = Jar(3)
    with pytest.raises(ValueError):
        jar.withdraw("cat")
        jar.withdraw(4)
        jar.withdraw(-3)
