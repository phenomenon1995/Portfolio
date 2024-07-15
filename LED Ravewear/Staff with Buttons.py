from adafruit_circuitplayground import cp
from adafruit_led_animation.animation.comet import Comet
from adafruit_led_animation.animation.pulse import Pulse
from adafruit_led_animation.animation.SparklePulse import SparklePulse
from adafruit_led_animation.animation.rainbowcomet import RainbowComet
import adafruit_lis3dh
import supervisor

from math import sqrt
from builtins import round
import keypad  # each event has key_number, pressed, released, timestamp

import time
import board
import neopixel
import colorsys
import digitalio

# define colors
GOLD = (255, 127, 0, 15)
WARM_WHITE = (255, 75, 10)
BLACK = (0, 0, 0, 0)
RED = (255, 0, 0, 0)
GREEN = (0, 255, 0, 0)
BLUE = (0, 0, 255, 0)
pixel_color = [ GOLD, WARM_WHITE, GREEN, BLUE, RED]

# define IO
pixel_num = 24
pixel_brightness = 1 #Gamma says that .5 looks pretty bright still. Try?
i = 0
pixels = neopixel.NeoPixel(
    board.D6,
    pixel_num,
    brightness=pixel_brightness,
    auto_write=False,
    pixel_order=neopixel.GRBW,
)
keys = keypad.Keys((board.A2, board.A3), value_when_pressed=False, pull=True)
long_press_timeout = 500
cp.configure_tap(1, accel_range=adafruit_lis3dh.RANGE_2_G, threshold=50)
Gs= 9.8 * 16

# TEMPO variables
first_tap = 0
last_tap = 0
beat = 0
BPM = 120
TEMPO = 60000 / BPM  # in milliseconds

max_acc = [9.8]  # intialize list that holds max acceleration
count = 0  # initialize counter that defines length of max_acc





mode = 1



def main():
    global pixel_color, pixels, keys, mode,  GOLD, i, first_press, second_press



    comet = new_comet(TEMPO, GOLD)
    pulse = new_pulse(TEMPO, GOLD)
    rainbowcomet = new_rainbowcomet(TEMPO)
    sparkle = SparklePulse(pixels, speed = .000005, color = RED, period = TEMPO/100)

    while True:


        '''#acc_color = acceleration_to_color(cp.acceleration, comet.cycle_count)
        x,y,z = cp.acceleration
        absolute_acceleration = sqrt(x*x + y*y + z*z)
        start_t = supervisor.ticks_ms()
        start_a = absolute_acceleration
        time.sleep(.05)
        x,y,z = cp.acceleration
        absolute_acceleration = sqrt(x*x + y*y + z*z)
        #absolute_acceleration = min(absolute_acceleration, Gs)
        #normalized = absolute_acceleration / Gs
        force = (absolute_acceleration - start_a)


        HSV_color = colorsys.hsv_to_rgb(1, 1, 1)

        if comet.cycle_count == 10:
            comet.cycle_count = 1'''

        event = keys.events.get()


        if event:

            if event.pressed and event.key_number == 1: #button 1 pushed
                button_press = event.timestamp
            if event.released and event.key_number == 1:
                button_release = event.timestamp

                if button_release - button_press  > long_press_timeout:
                    print("long press", button_release, button_press)
                    detect_TEMPO()
                    comet = new_comet(TEMPO, pixel_color[i])
                    pulse = new_pulse(TEMPO, pixel_color[i])
                    rainbowcomet = new_rainbowcomet(TEMPO)




                if button_release - button_press  < long_press_timeout:
                    if mode ==  4:
                        mode = 1
                    else:
                        mode += 1

            if event.pressed and event.key_number == 0:
                button_press = event.timestamp
            if event.released and event.key_number == 0:
                button_release = event.timestamp
                if button_release - button_press  > long_press_timeout: #LONG PRESSED
                    ...



                else:                                                     #SHORT PRESSED
                    if i == len(pixel_color) -1 :
                        i = 0
                    else:
                        i += 1
                        print(i)





        if mode == 1:  # change animation based on mode
            if i == len(pixel_color) :
                        i = 0
            sparkle.color = pixel_color[1-i]
            sparkle.animate()
            sparkle.color = GOLD
            sparkle.animate()
        if mode == 2:
            comet.color = pixel_color[i]
            comet.animate()
        if mode == 3:
            rainbowcomet.animate()
        if mode == 4:
            pulse.color = pixel_color[i]
            pulse.animate()






def new_comet(TEMPO, color):
    global pixels, mode
    speed = TEMPO / 24000

    comet = Comet(
        pixels,
        speed=speed,
        color=color,
        ring=True,
        background_color=(0, 0, 0, 0),
        tail_length=6,
    )
    comet.reset()
    return comet

def new_pulse(TEMPO, color):
    global pixels, mode
    pulse = Pulse(pixels, speed = 0 , color = color, period = TEMPO/1000 )
    pulse.reset()
    return pulse

def new_rainbowcomet(TEMPO):
    global pixels
    rainbowcomet = RainbowComet(pixels, speed = TEMPO/24000, tail_length = 12, ring = True)
    rainbowcomet.reset()
    return rainbowcomet



def animation(color1=BLACK, color2=RED, speed= .01):
    global pixels

    pixels.fill(BLACK)

    for p in range(24):
        pixels[p] = color1
        time.sleep(speed)
        pixels.show()
    for p in range(24):
        pixels[p] = color2
        time.sleep(speed)
        pixels.show()
    for p in range(24):
        pixels[p] = color1
        time.sleep(speed)
        pixels.show()


def detect_TEMPO():
    global first_tap, last_tap, beat, BPM, TEMPO, keys

    animation()

    while beat != 8:
        event = keys.events.get()

        if event:
            if event.pressed and event.key_number == 0:
                tempo_tap()

            if event.released and event.key_number == 1:
                animation(GOLD, BLACK, 0.01)
                print("ESCAPED")
                last_tap = 1000
                first_tap = 500
                beat = 2

                break

    TEMPO = round((last_tap - first_tap) / (beat - 1))
    BPM = round(60000 / TEMPO)
    beat = 0
    first_tap = 0


    print(f"Tempo in ms: {TEMPO} BPM: {BPM}")


def tempo_tap():
    global pixels, first_tap, last_tap, beat

    if first_tap == 0:
        first_tap = supervisor.ticks_ms()

    else:
        last_tap = supervisor.ticks_ms()

    if pixels[0] == GOLD:
        pixels.fill(WARM_WHITE)
        pixels.show()
    else:
        pixels.fill(GOLD)
        pixels.show()

    beat += 1


def on_button(event):
    global key_number
    if event.pressed:
        key_number = event.key_number
        if key_number == 0:
            print("BUTTON A PRESSED", event.timestamp)
        elif key_number == 1:
            print("BUTTON B PRESSED", event.timestamp)

    else:
        if key_number == 0:
            print("BUTTON A RELEASED", event.timestamp)
        elif key_number == 1:
            print("BUTTON B RELEASED", event.timestamp)


def acceleration_to_color(acceleration, cycle):
    global comet
    max_acc = [9.8]  # intialize list that holds max acceleration
    count = 0  # initialize counter that defines length of max_acc
    mode = 0
    mode_max = 2

    # defines how long max acc list is, and how frequenetly it clears
    if cycle == 1:
        max_acc = [0]
        cycle = 0

    # Get acceleration, assign to xyz, convert to abs int and and largest value to max_acc list.
    x, y, z = acceleration


    int_color = (int(abs(x)), int(abs(y)), int(abs(z)))
    m = max(int_color)
    if m not in max_acc:
        max_acc.append(m)
    M = max(max_acc)

    # convert acceleration to HSV color, normalizing input to 1 using max acceleration
    return m / M

class Smoothing:
    def __init__(self, coeff=0.1):
        """Filter to smooth an input signal using a first-order filter.  One state value
         is required.  The smaller the coefficient, the smoother the output."""
        self.coeff = coeff
        self.value = 0

    def update(self, input):
        # compute the error between the input and the accumulator
        difference = input - self.value

        # apply a constant coefficient to move the smoothed value toward the input
        self.value += self.coeff * difference

        return self.value

class MovingAverage:
    def __init__(self, window_size=5):
        """Filter to smooth a signal by averaging over multiple samples.  The recent
        time history (the 'moving window') is kept in an array along with a
        running total.  The window size determines how many samples are held in
        memory and averaged together.
        """
        self.window_size = window_size
        self.ring = [0] * window_size     # ring buffer for recent time history
        self.oldest = 0                   # index of oldest sample
        self.total  = 0                   # sum of all values in the buffer

    def update(self, input):
        # subtract the oldest sample from the running total before overwriting
        self.total = self.total - self.ring[self.oldest]

        # save the new sample by overwriting the oldest sample
        self.ring[self.oldest] = input

        # advance to the next position, wrapping around as needed
        self.oldest += 1
        if self.oldest >= self.window_size:
            self.oldest = 0

        # add the new input value to the running total
        self.total = self.total + input

        # calculate and return the average
        return self.total / self.window_size

if __name__ == "__main__":
    main()
# Write your code here :-)
