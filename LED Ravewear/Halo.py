#region import statements and setup
import board
import neopixel
import time
import pwmio
import random
import adafruit_itertools as itertools
from analogio import AnalogIn
battery_pin = AnalogIn(board.VOLTAGE_MONITOR)
#BLE Services
from adafruit_ble import BLERadio
from adafruit_ble.advertising.standard import ProvideServicesAdvertisement
from adafruit_ble.services.nordic import UARTService
ble = BLERadio()
uart_service = UARTService()
advertisement = ProvideServicesAdvertisement(uart_service)
advertisement.complete_name = ble.name[-6:]
print(advertisement.complete_name)



#BLUEFRUIT APP
from adafruit_bluefruit_connect import (magnetometer_packet, accelerometer_packet, gyro_packet,
location_packet, quaternion_packet, raw_text_packet, button_packet, color_packet, packet)
ButtonPacket = button_packet.ButtonPacket
ColorPacket = color_packet.ColorPacket
Packet = packet.Packet

#ANIMATIONS AND LEDS
from adafruit_led_animation.color import (RAINBOW, AMBER, BLUE, BLACK, WHITE, RED, GREEN, YELLOW)
from adafruit_led_animation.animation import (blink, solid, colorcycle, chase, comet, pulse, rainbow,
sparkle, rainbowchase, rainbowcomet, rainbowsparkle, sparklepulse, Animation)
from adafruit_led_animation.sequence import AnimationSequence
from adafruit_led_animation.group import AnimationGroup
Comet = comet.Comet
SparklePulse = sparklepulse.SparklePulse
Pulse = pulse.Pulse
Blink = blink.Blink
Solid = solid.Solid
#endregion

#region global variables
NANOS_PER_MS = 1000000
MS_PER_SECOND = 1000
SECONDS_PER_MINUTE = 60
TEMPO = 0.5
BPM = 120
tempoTimeout = time.monotonic()
press_threshold = .5

UV = (123,0,141)
c = BLUE
color_mode = "static"


music_time = 1
light_time = 1
light_timer = 500
colors = [BLUE, WHITE, RED, GREEN, AMBER, UV]
colors_index = 0
speed_levels = [.100, .050, .010, .007]
speed_levels_index = 2
subdivisions = [0.25, 0.5, 1, 2]
subdivisions_index = 2
brightness_levels = [0, .15, .5, 1]
brightness_levels_index = 2
animation_names = []
animation_modes_index = 1
period_levels = [.5, 1.5, 2, 2.5, 3]
period_levels_index = 2

palette = colors

palette_iterator = itertools.cycle(iter(palette))




brightness = "brightness_levels[brightness_levels_index]"
speed = "speed_levels[speed_levels_index]"
color = "colors[colors_index]"
period = "period_levels[period_levels_index]"
duty_cycle = "int(eval(brightness) * 65534)"
status = ("f'Mode: {animations.current_animation.name} Index:{animation_modes_index} Speed Level:\
{speed_levels[speed_levels_index]} Brightness Level:{brightness_levels[brightness_levels_index]}'")

toggle = "RGB"
toggle_speed =  .001
pixels = neopixel.NeoPixel(board.D13, 48, brightness = 0)
dummypixels = neopixel.NeoPixel(board.D12, 48, brightness = 0)
pwm_rgb = pwmio.PWMOut(board.D5, duty_cycle = 65535, frequency = 500)
pwm_white = pwmio.PWMOut(board.D10, duty_cycle = 0, frequency = 500)
active_pwm = None
pixels.fill(BLACK)


#endregion

def main():
    global c, animation, last, palette, palette_iterator, color_mode, colors, time_pressed, time_released
    
    while True:
        # Advertise when not connected.
        ble.start_advertising(advertisement)
        pixels.brightness = .1
        while not ble.connected:
            now = time.monotonic()
            if now - last > 120:
                pixels.brightness = 0
                dAnimation.freeze()
            else:
                dAnimation.animate()
            pass
        ble.stop_advertising()
        #BLUETOOTH CONNECTED ANIMATION
        
        Flash(color = BLUE, times = 2)
        pixels.brightness = eval(brightness)
        animations.activate(animation_modes_index)
        animation2 = new_Animation(c, "SPARKLEPULSE")

        colorTimer = time.monotonic_ns()
        
        while ble.connected:
            animations.animate()
            #if animations.current_animation.name == "SPARKLEPULSE": animation2.animate() 
            if color_mode == "auto":
                if (time.monotonic_ns() - colorTimer) / NANOS_PER_MS > light_timer:
                    print(f"Color Mode {color_mode} | Time is {time.monotonic_ns()} | SubDiv: {music_time} | Time Since Last Beat: {time.monotonic_ns() - colorTimer}")
                    print(f"Palette: \n {palette}")
 
                    animation2.color = animations.color
                    animations.color = next(palette_iterator)
                    colorTimer = time.monotonic_ns()
                    print()
            
            time_since_last = int(time.monotonic() - last) #Time Check
            if time_since_last >= 300: #Battery Check
                battery_check(show = False)
                last = time.monotonic()
            if uart_service.in_waiting: #Listener for app events
                packet = Packet.from_stream(uart_service)

                if isinstance(packet, ColorPacket):
                    if toggle == "W": toggle_LEDs()
                    c = packet.color
                    print(c)
                    if c == animations.color:
                        color_mode = "static"
                        palette = list(colors)
                        print("already here")
                        print()
                    else: palette.append(c)

                    print(palette)
                    palette_iterator = itertools.cycle(iter(palette)) 
                    
                    animations.color = c

                elif isinstance(packet, ButtonPacket):
                    if packet.pressed: time_pressed = time.monotonic()
                    else: 
                        time_released = time.monotonic()
                        if time_pressed != 0:
                            if time_released - time_pressed > press_threshold:
                                which_button(packet, "long")
                            else:
                                print("short press")
                                which_button(packet, "short")
        

#region FUNCTIONS FOR HANDING INPUT FROM APP 
def which_button(packet, press = "short"):
    """
        Button 1: Short Detect Tempo | Long Reset colors to Default
        Button 2: Short Brightness Up | Long Toggle LEDs/White
        Button 3: Short Cycle through colors | Long Color Mode
        Button 4: Short Brightness Down | Long Brightness 0 (emulates off)

        UP: Short Speed Up Animation | Long Speed Up Color
        DOWN: Short Slow Down Animation | Long Slow Down Color
        LEFT: Short Previous Animation
        RIGHT: Short Next Animation
    """

    global tempoTimeout, palette, color_mode, music_time, time_pressed, time_released
    if packet.button == ButtonPacket.BUTTON_1:  #TEMPO|RESET
        if press == "short":
            if time.monotonic() - tempoTimeout > 2:
                detect_tempo()
                tempoTimeout = time.monotonic()
                time_pressed = 0
                select_animation("PULSE")
                
        if press == "long":
            if color_mode == "static":
                animations.color = AMBER
                print("color reset to AMBER") 
            else: 
                palette = colors
                print("reset color palette to default values")        
    if packet.button == ButtonPacket.BUTTON_2:  #BRIGHTNESS UP|  TOGGLE
        if press == "short": adjust_brightness()
        if press == "long":
            toggle_LEDs()
            print(f"{toggle} active, {pwm_white.duty_cycle}, {pixels.brightness}")
    if packet.button == ButtonPacket.BUTTON_3:  #CYCLE COLOR | COLOR MODE
        if press == "short": 
            change_color("up")
        elif press == "long":
            if color_mode == "auto": color_mode = "static"
            else: 
                color_mode = "auto"
                select_animation("PULSE")
    if packet.button == ButtonPacket.BUTTON_4:  #BRIGHTNESS DOWN | OFF
        if press == "short": adjust_brightness("down")
        elif press == "long": 
            if toggle == "W": pwm_white.duty_cycle = 0
            else: pixels.brightness = 0

    if packet.button == ButtonPacket.DOWN:      #SLOW DOWN ANIMATION | COLOR
        if press == "short": adjust_speed("down", "animation")
        if press == "long": adjust_speed("down", "color")
    if packet.button == ButtonPacket.UP:        #SPEED UP ANIMATION | COLOR
        if press == "short": adjust_speed("up", "animation")
        if press == "long": adjust_speed("up", "color")
    if packet.button == ButtonPacket.LEFT:      #NEXT ANIMATION
        select_animation("previous")
    if packet.button == ButtonPacket.RIGHT:     #PREVIOUS ANIMATION
        select_animation("next")
#endregion

#region FUNCTIONS RELATING TO GENERATING NEW ANIMATIONS/PIXEL INFORMATION
def refresh_pixels():
    global pixels
    pixels.deinit()
    pixels = neopixel.NeoPixel(board.D13, 48, brightness = .5)
    ''' ADD LOGIC TO REFRESH RGB OR WHITE DEPENDING ON TOGGLE HERE '''

def power_on_animation():
    pixels.brightness = 1
    for p in range(pixels.n):
        pixels[p] = AMBER
        time.sleep(.01)
    for p in range(pixels.n):
        pixels[p] = (0,0,0)
        time.sleep(.01)

def new_Animation(color = c, mode = "COMET", speed = eval(speed), period = eval(period)):
    global pixels, c
    if mode.upper() == "COMET":
        animation= Comet(pixels,speed = speed ,color = c,ring=True,background_color=(0, 0, 0, 0),tail_length=16, name = "COMET")
        animation.reset()
    elif mode.upper() == "PULSE":
        animation = Pulse(pixels, speed = speed , color = c, period = period, name = "PULSE")
        animation.reset()
    elif mode.upper() == "SPARKLEPULSE":
        animation = SparklePulse(pixels, speed = speed , color = c, period =  period, min_intensity = .6, name = "SPARKLEPULSE")
        animation.reset()
    elif mode.upper() == "WHITEFADE":
        animation = WFade(pixels, pwm_white, max_brightness = eval(brightness), period =  period, name = "WHITEFADE")
        animation.reset()
    elif mode.upper() == "BLINK":
        animation = Blink(pixels, speed = speed , color = c, name = "BLINK")
        animation.reset()
    elif mode.upper() == "SOLID":
        animation = Solid(pixels, color = c, name = "SOLID")
    else:
        refresh_pixels()
        pixels.fill(c)
    return animation

def Flash(color = WHITE, times = 1, speed = .1):
    flash = new_Animation(color = color , mode = "BLINK", speed = speed)
    while flash.draw_count < (2 * times) + 1:
        flash.animate()
    time.sleep(speed)    
  
class WFade(Animation):
    '''
        Animates white LEDs smoothly between min and max brightness levels.
    '''
    def __init__(self, pixel_object, pwm_pin, speed = eval(speed), period = 2, step = 256, max_brightness = eval(brightness), name = "WFADE"):
        '''
            Parameters
            ----------
            pixel_object : neopixel object
                the neopixel object to be toggled
            pwm_pin: pin object
                the pin that controls PWM duty_cycle for white LEDs
            speed: float
                duration of time between each draw() call
            period: float or int
                duration between full animation cycle
            step: int
                resolution of duty_cycle incrementation (256 for 16 bit resolution)
            max_brightness: float
                determines maximum duty cycle for LED brightness
        '''
        super().__init__(pixel_object, speed, (0,0,0), name = name)
        self._step = step
        self._pin = pwm_pin
        self._current = -1
        self.max_brightness = max_brightness
        self.period = period
        self._max = self.max_brightness * 65535
        self.speed = (self._period/2) / self._step
        self._direction = "Forward"
    on_cycle_complete_supported = True
 
    def draw(self):
        
        if self._direction == "Forward":
            if self._current > self._max - self._step:
                self._direction = "Backward"
                self._current = self._max - self._step
                pixels.brightness = 0 
            self._current += self._step 
            
        if self._direction == "Backward":
            if self._current <= 255:
                self._direction = "Forward"
                self._current = 256
            self._current -= self._step
        #print(self._current)
        self._pin.duty_cycle = self._current
    
    def reset(self):
        toggle_White()
        '''
        self._pin.duty_cycle = 0
        pixels.brightness = eval(brightness)
        '''   
    
    #region Properties
    @property
    def max_brightness(self):
        return self._max_brightness
    @max_brightness.setter
    def max_brightness(self, max_brightness):
        self._max_brightness = max_brightness
        self._max = int(max_brightness * 65535)
        if self._max > 65279:
            self._max -= self._max % 256
        else:
            self._max += 256 - (self._max % 256)
    @property
    def period(self):
        return self._period
    @period.setter
    def period(self, period):
        self._period = period
    
    @property
    def speed(self):
        return self._speed
    @speed.setter
    def speed(self, speed):
        self._speed = speed
    #endregion

def create_Animation_Sequence(speed = TEMPO / 48, period = TEMPO):
    global wfade, comet, pulse, sparklepulse, animation_names, c
    comet = new_Animation(c, "COMET", speed, period)
    pulse = new_Animation(c, "PULSE", speed, period)
    sparklepulse = new_Animation(c,"SPARKLEPULSE", speed, period)
    wfade = new_Animation(c, "WHITEFADE", speed, period)
    solid = new_Animation(c, "SOLID")
    tempAnimations = AnimationSequence(comet, sparklepulse, pulse, solid, auto_reset= True)
    #wAnimations= AnimationSequence(wfade, auto_reset = False)
    animation_names = []
    for member in tempAnimations._members:
        animation_names.append(member.name)
    return tempAnimations

#endregion

#region THIS SECTION CONTAINS FUNCTIONS FOR ADJUSTING BRIGHTNESS, SPEED, AND ANIMATION
def adjust_brightness(direction = "UP"):
    global pixels, brightness_levels_index
    if direction.upper() == "UP":
        if brightness_levels_index == len(brightness_levels) - 1: brightness_levels_index = 0
        else: brightness_levels_index += 1

    if direction.upper() == "DOWN":
        if brightness_levels_index == 0: brightness_levels_index = len(brightness_levels) - 1
        else: brightness_levels_index -= 1

    if toggle == "W":
        pwm_white.duty_cycle = eval(duty_cycle)
    else:
        pixels.brightness = eval(brightness)
        #wfade.max_brightness = eval(brightness)
    print(eval(status))

def adjust_speed(direction = "UP", mode = "color"):
    global speed_levels_index, animation, period_levels_index, animations, subdivisions_index,  \
    music_time, light_time, light_timer

    current = animations.current_animation.name

    """if direction.upper() == "UP":
            if speed_levels_index == len(speed_levels)-1:
                speed_levels_index = 0
            else:
                speed_levels_index += 1

            if period_levels_index == len(period_levels) - 1:
                period_levels_index = 0
            else:
                period_levels_index += 1

        if direction.upper() == "DOWN":
            if speed_levels_index == 0:
                speed_levels_index = len(speed_levels)-1
            else:
                speed_levels_index -= 1

            if period_levels_index == 0:
                period_levels_index = len(period_levels) - 1   
            else:
                period_levels_index -= 1 
        animations = create_Animation_Sequence(eval(speed), eval(period))
        animations.activate(animation_names.index(current))"""

    if direction.upper() == "UP":
        if subdivisions_index != len(subdivisions) - 1:
            subdivisions_index += 1
    if direction.upper() == "DOWN":
        if subdivisions_index > 0:
            subdivisions_index -= 1

    if mode.upper() == "ANIMATION":
        music_time = subdivisions[subdivisions_index]
        newTempo = TEMPO / music_time
        animations = create_Animation_Sequence(newTempo / 48 , newTempo)
        animations.activate(animation_names.index(current))
    elif mode.upper() == "COLOR":
        light_time = subdivisions[subdivisions_index]
        light_timer = (TEMPO * MS_PER_SECOND) / light_time


        

    print(eval(status), eval(period))

def select_animation(mode = ""):
    global animation_modes_index, animation
    if mode.upper() in animation_names:
        animation_modes_index = animation_names.index(mode)
        animations.activate(animation_modes_index) 
        
    if mode.upper() == "NEXT":
        if animations.current_animation.name == "WHITEFADE":
            animations.reset()
        animations.next()
    elif mode.upper() == "PREVIOUS":
        if animations.current_animation.name == "WHITEFADE":
            animations.reset()        
        animations.previous()

    print(eval(status))

def change_color(direction = "UP"):
    global animations, animation2, colors_index, c
    if direction.upper() == "UP":
        if colors_index == len(colors) - 1: colors_index = 0
        else: colors_index += 1

    if direction.upper() == "DOWN":
        if colors_index == 0 : colors_index = len(colors) - 1
        else: colors_index -= 1
        
    print(colors_index, len(colors))
    
    animations.color = colors[colors_index]
    c = colors[colors_index]
    

def toggle_White():
    '''
    Turns WHITE OFF, Turns RGB ON.
    '''
    global toggle
    if pwm_white.duty_cycle > 0: #IF WHITE ON
        pixels.brightness = 0 
        while pixels.brightness < 1 : #TURN RGB ON 
            pixels.brightness += 1/256
            time.sleep(toggle_speed)
        toggle = "RGB"        
        if pwm_white.duty_cycle >= 65279: #ROUNDING DUTY CYCLE
            pwm_white.duty_cycle -= pwm_white.duty_cycle % 256 #ROUND DOWN
        else:
            pwm_white.duty_cycle += 256 - (pwm_white.duty_cycle % 256) #ROUND UP
        while pwm_white.duty_cycle > 0: #TURN WHITE OFF
            pwm_white.duty_cycle -= 256
            time.sleep(toggle_speed)
def toggle_RGB():
    '''
    Turns RGB OFF, Turns WHITE ON.
    '''
    global pixels, toggle
    if pixels.brightness > 0: #IF RGB ON
        pwm_white.duty_cycle = 0
        for _ in range(int((eval(brightness) * 256)- 1)): #TURN ON WHITE
            pwm_white.duty_cycle += 256
            time.sleep(toggle_speed)
        toggle = "W"
        while pixels.brightness > 0: #TURN RGB OFF
            pixels.brightness -= 1/256
            time.sleep(toggle_speed)

def toggle_LEDs():
    global toggle, pwm_white, pixels

    if pwm_white.duty_cycle > 0: #IF WHITE ON
        pixels.brightness = 0 
        while pixels.brightness < 1 : #TURN RGB ON 
                pixels.brightness += 1/256
                print(pixels.brightness)
        pixels.brightness = 1       

        if 65535 > pwm_white.duty_cycle > 65279: #ROUNDING DUTY CYCLE
            #pwm_white.duty_cycle -= pwm_white.duty_cycle % 256 #ROUND DOWN
            pwm_white.duty_cycle = 65535
            print(f"duty cycle rounded to {pwm_white.duty_cycle}")
        elif pwm_white.duty_cycle < 256:
            #pwm_white.duty_cycle += 256 - (pwm_white.duty_cycle % 256) #ROUND UP
            pwm_white.duty_cycle = 256
            print(f"duty cycle rounded up to {pwm_white.duty_cycle}")

        while pwm_white.duty_cycle > 255: #TURN WHITE OFF
                pwm_white.duty_cycle -= 256
                print(pwm_white.duty_cycle)
        pwm_white.duty_cycle = 0
        toggle = "RGB"

    elif pixels.brightness > 0: #IF RGB ON
        pwm_white.duty_cycle = 0 

        while pwm_white.duty_cycle < 65279: #TURN ON WHITE
                pwm_white.duty_cycle += 256
                print(pwm_white.duty_cycle)
        pwm_white.duty_cycle = 65535

        while pixels.brightness > 0: #TURN RGB OFF
                pixels.brightness -= 1/256
                print(pixels.brightness)

        pixels.brightness = 0
        toggle = "W"

def delay(delay):
    delay = delay * MS_PER_SECOND
    last = time.monotonic_ns()
    while ((time.monotonic_ns() - last) // (NANOS_PER_MS)) < delay :
        print(delay, ",  time since last", (time.monotonic_ns() - last)//NANOS_PER_MS)

#endregion

#region MONITOR/MEASURING FUNCTIONS
def battery_check(show = True):
    global pixels, animation, last
    voltage = (battery_pin.value * 3.6) / 65536*2
    normalized = (voltage - 3.2)
    print(f"Vbat voltage: {voltage:.2f} Battery Percentage: {normalized:.0%}")
    if normalized <= .05: #Battery Low Alert regardless of show state
        animations.freeze()
        Flash(RED, 3)
        #animation = new_Animation(c, eval(animation_mode))
        last = time.monotonic()
        animations.resume()
        return
    if show == True: #Show battery life on halo
        health = None
        if normalized > .66:
            health = GREEN
        elif .32 < normalized < .67:
            health = YELLOW
        elif normalized < .33:
            health = RED
        if normalized > .05:
            Flash(health, 3)
            '''for p in range(int(48 * normalized)):
                pixels[p] = health
                time.sleep(eval(speed))'''
        last= time.monotonic()
        animations.resume()

def detect_tempo():
    global first_tap, last_tap, beat, BPM, TEMPO, speed_levels_index, period_levels_index, c, animations, subdivisions_index
    def tempo_tap():
        
        global pixels, first_tap, last_tap, beat
        if first_tap == 0:
            first_tap = time.monotonic_ns()
            print("First Tap", first_tap)
        last_tap = time.monotonic_ns()

        if pixels[0] == c:
            pixels.fill(BLACK)
            pixels.show()
        else:
            pixels.fill(c)
            pixels.show()
        beat += 1
        print("tap")      
    beat = 0
    first_tap = 0
    
    timeout = time.monotonic()
    Flash(c, 1, TEMPO/2)

    while beat != 8:
        if uart_service.in_waiting: #Listener for app events
                packet = Packet.from_stream(uart_service)
                if isinstance(packet, ButtonPacket) and packet.pressed == True:
                    tempo_tap()
                    timeout = time.monotonic()
        if time.monotonic() - timeout > 5:
            print("TIMEOUT")
            return None


    print(f"Beats: {beat} First Tap: {first_tap} Last Tap {last_tap}")
    TEMPO = ((last_tap - first_tap) / (MS_PER_SECOND * NANOS_PER_MS)) / (beat - 1 )
    BPM = round((SECONDS_PER_MINUTE) / TEMPO)
    subdivisions_index = 2

    """for member in animations._members:
        if member.name == "SPARKLEPULSE" or member.name == "PULSE":
            member.period = TEMPO / 1000
            member.speed = 0
            print(member.period)
            member.reset()
        if member.name == "COMET":
            member.speed = TEMPO / 48
        print(member.speed)"""
    
    print(f"Tempo in seconds: {TEMPO} BPM: {BPM}")
    print()
    animations = create_Animation_Sequence(TEMPO/48, TEMPO)
    
#endregion

if __name__ == "__main__":
    power_on_animation()
    animations = create_Animation_Sequence()
    dAnimation = new_Animation(c, "PULSE", TEMPO/12) #Disconnected Animation
    
    last = time.monotonic()
    battery_check(False)
    main()