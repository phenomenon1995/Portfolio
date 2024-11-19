from um import count

def main():
    test_solo()
    test_words()
    test_mix()
    test_case()

def test_solo():
    assert count("um?") == 1
    assert count("um um um") == 3

def test_words():
    assert count("yummy") == 0
    assert count("yummy tummy grummy") == 0
def test_mix():
    assert count("um, thanks for the album") == 1
    assert count("um, thanks, um...") == 2
def test_case():
    assert count("UM") == 1
    assert count("Um um uM") == 3

if __name__ == "__main__":
    main()
