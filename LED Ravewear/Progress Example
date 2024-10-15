import adafruit_led_animation.animation
import adafruit_led_animation.animation.comet
import adafruit_led_animation.animation.solid
import adafruit_led_animation.color as Colors
import board
import time
import neopixel
import asyncio
import adafruit_itertools
#import staff
from devices import Staff,Strip
from LEDHandler import LEDHandler
from InputHandler import InputHandler
import adafruit_led_animation
from BleHandler import BTHandler
import time
import board
import digitalio
import asyncio
import random

numArray: int = adafruit_itertools.cycle(["1","2","3","4"])
letterArray: str = adafruit_itertools.cycle(["A","B","C","D"])

async def number_counter():
        while True:
            delay = random.uniform(2.5, 5.0)
            start_time = time.monotonic()
            await asyncio.sleep(delay)
            elapsed_time = time.monotonic() - start_time
            print(f"\t\tNumber Counter {next(numArray)}: Delay = {delay} Elapsed Time = {elapsed_time} ")

async def letter_counter():
    interval = 1.0
    while True:
        start_time = time.monotonic()
        await asyncio.sleep(interval)
        elapsed_time = time.monotonic() - start_time
        print(f"Letter Counter {next(letterArray)}: Interval = {interval}, Elapsed Time = {elapsed_time}")

async def incrementing_number():
    number = 0
    while True:
        print(f"Incrementing Number: {number}")
        number += 1
        await asyncio.sleep(random.uniform(.1,1))

async def test():
    ...

#TODO modify to take in a tempo and callback function w/ args and kwargs and gather as multiple tasks
# per thing that operates on different tempo
# perhaps use a decorator to pass in the tempo and callback function
#     
async def tempoChanges(tempo: int):
     beat = 0 
     while True:
        beat = 1 if beat == 16 else beat + 1
        await asyncio.sleep_ms(int((tempo*1000)/2))
        print(f"Beat: {beat}")
        if beat == 4 or beat == 12:
            #TODO input.excuteCommand(Command.HALF_TIME)
            print("half time")
        if beat == 8:
            #TODO input.executeCommand(Command.REGULAR_TIME)
            device.animations.color = next(device.paletteCycle)
            print("regular time")
        if beat  == 16:
            #TODO input.executeCommand(Command.DOUBLE_TIME)
            device.animations.next()
            print("double time")

async def animation():
    device.color = Colors.GREEN
    while True:
        await asyncio.sleep(0)
        device.animations.animate()


async def BTConnected():
    while bt.radio.connected:
            await asyncio.sleep(0)   
            if bt.uart.in_waiting:
                s: bytes = bt.uart.readline()
                if s[:4] == b'eval' :
                    expression = s[5:-1]
                    try:
                        eval(expression)
                        bt.uart.write(b"Executed\n")
                    except:
                        bt.uart.write(b"Invalid Expression")
                else:
                    bt.uart.write(s + b"to you too!")
                    
    print("Disconnected")

        
async def BTDisconnected():
    bt.advertise()
    last = time.monotonic()
    while not bt.radio.connected:
        await asyncio.sleep(0)
        device.animations.animate()
        if time.monotonic() - last > 5:
            print("ADVERTISING...\n")
            last = time.monotonic()
    print("Connected")

async def main():
    #haloAdvert = bt.findDevice("Halo", 4)
    #bt.connect(haloAdvert)
    
    
    while True:
        device.selectAnimation("COMET")
        device.animations.color = Colors.RED
        await BTDisconnected()
        device.flash(Colors.BLUE, 2)
        tempoTask = asyncio.create_task(tempoChanges(1))
        animationTask = asyncio.create_task(animation())
        await BTConnected()
        tempoTask.cancel()
        animationTask.cancel()



    '''halo = TestBLE.findHalo()
    staffConnected = None
    haloConnection = Nones

    if halo: haloConnection = TestBLE.ble.connect(halo)
    print("Connected To Halo", haloConnection.connected)
    if haloConnection.connected:
        TestBLE.ble.start_advertising(TestBLE.staffAdvert)
    while not staffConnected:
        continue
    staffConnected = True
    print("Staff Connected")'''
    '''test = asyncio.create_task(containerTestBLE())
    await asyncio.gather(test)'''



if __name__ == "__main__":
    #staff.main()

    device = Staff()
    input = InputHandler(device)
    device.powerOnAnimation()
    bt = BTHandler()
    loop = asyncio.get_event_loop()
    asyncio.run(main())
    
    
    
