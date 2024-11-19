from bank import value

def test_caps():
    assert value("HELLO") == 0
    assert value("HI") == 20
    assert value ("yo") == 100
def test_space():
    assert value("     hello") == 0