class Jar:
    def __init__(self, capacity=12):
        self.capacity = capacity
        self.size = 0

    def __str__(self):
        cookies = self.size * "🍪"
        return f"{cookies}"

    def deposit(self, n):
        self.size = self.size + int(n)

    def withdraw(self, n):
        self.size = self.size - int(n)

    @property
    def capacity(self):
        return self._capacity
    @capacity.setter
    def capacity(self, capacity):
        try:
            capacity = int(capacity)
        except TypeError:
            raise ValueError
        if capacity < 0:
            raise ValueError
        else:
            self._capacity = capacity

    @property
    def size(self):
        return self._size
    @size.setter
    def size(self, size):
        if size > self.capacity or size < 0:
            raise ValueError
        else:
            self._size = size


def main():
    jar = Jar(input("Jar Capacity: "))
    while True:
        jar.deposit(input("Deposit: "))
        print(jar)
        jar.withdraw(input("Withdraw: "))
        print(jar)


if __name__ == "__main__":
    main()
