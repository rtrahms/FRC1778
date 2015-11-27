#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif

#include <Wire.h>

// Note: assumes NeoPixels are connected to PIN 6 on the Arduino!
#define PIN 6

// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
Adafruit_NeoPixel strip = Adafruit_NeoPixel(60, PIN, NEO_GRB + NEO_KHZ800);

// states defined for the arduino.  These equate to strings received by the Roborio.
enum ColorState { inactive, autonomous, teleop, test };

#define RED 0x00ff0000
#define GREEN 0x0000ff00
#define BLUE 0x000000ff

// globals
ColorState cs = inactive;
uint32_t stripColor;
float fadeValue;
uint8_t brightness;
bool increasing;

// IMPORTANT: To reduce NeoPixel burnout risk, add 1000 uF capacitor across
// pixel power leads, add 300 - 500 Ohm resistor on first pixel's data input
// and minimize distance between Arduino and first pixel.  Avoid connecting
// on a live circuit...if you must, connect GND first.

void setup() {
  // This is for Trinket 5V 16MHz, you can remove these three lines if you are not using a Trinket
  #if defined (__AVR_ATtiny85__)
    if (F_CPU == 16000000) clock_prescale_set(clock_div_1);
  #endif
  // End of trinket special code

  Serial.begin(57600);
  Serial.println("Starting up RioDuino!");

  Wire.begin(4);                // join i2c bus with address #4
  Wire.onReceive(receiveEvent); // register event

  strip.begin();
  strip.show(); // Initialize all pixels to 'off'

  fadeValue = 0;
  brightness = 0;
  increasing = false;
}

/************ loop method *************/
// running as fast as processor will allow
void loop() {
 
  switch (cs)
  {
    case autonomous:
        colorPulse(stripColor, 50); // loop on the current strip color
        //colorWipe(stripColor, 50);
        //rainbow(20);
        //rainbowCycle(20);
        //theaterChaseRainbow(50);
        break;
    case teleop:
        theaterChase(stripColor, 100);
        //colorWipe(stripColor, 1);
        break;
    case test:
        colorWipe(stripColor, 1);
        break;
    case inactive:
        colorPulse(stripColor, 25); // loop on the current strip color
        //colorWipe(stripColor, 1);
        break;
  }
}

// function that executes whenever data is received from master
// this function is registered as an event, see setup()
void receiveEvent(int howMany)
{
  String receiveStr = "";
 
  while ( Wire.available() > 0 )
  {
    char n=(char)Wire.read();
    if(((int)n)>((int)(' ')))
   receiveStr += n; 
  }

  Serial.println(receiveStr);
  
  if (receiveStr == "autoInitBlue")
  {
    cs = autonomous;
    stripColor = strip.Color(0, 0, 127); // blue
  }
  else if (receiveStr == "autoInitRed")
  {
    cs = autonomous;
    stripColor = strip.Color(127, 0, 0); // red
  }
  else if (receiveStr == "teleopInitBlue")
  {
    cs = teleop;
    stripColor = strip.Color(0, 0, 127); // blue
  }
  else if (receiveStr == "teleopInitRed")
  {
    cs = teleop;
    stripColor = strip.Color(127, 0, 0); // red
  }
  else if (receiveStr == "testInit")
  {
    cs = test; 
    stripColor = strip.Color(127, 0, 127); // Purple
  }
  else if (receiveStr == "disabledInit")
  {
    cs = inactive;
    stripColor = strip.Color(31, 31, 31); // grey
  }
}

/*************** color pulse method ******************/
// pulses a color between on and off
// speed of the pulse is based on the wait parameter
void colorPulse(uint32_t inputColor, uint8_t wait)
{
    const float upper_brightness = 0.5;
    const float lower_brightness = 0.0;

    uint16_t i;
    uint8_t red = (uint8_t)(((float)((inputColor & RED) >> 16)) * fadeValue);
    uint8_t green = (uint8_t)(((float)((inputColor & GREEN)>> 8)) * fadeValue);
    uint8_t blue = (uint8_t)((float)(inputColor & BLUE) * fadeValue);
    uint32_t c = strip.Color(red,green,blue);

    // update fadeValue
    if (increasing)
    {
      fadeValue += 0.03;
    }
    else
    {
      fadeValue -= 0.03;
    }

    // update direction based on limits
    if (fadeValue > upper_brightness)
    {
      fadeValue = upper_brightness;
      increasing = false;
    }
    if (fadeValue < lower_brightness)
    {
        fadeValue = lower_brightness;
        increasing = true;
    }
    
    for(i=0; i< strip.numPixels(); i++) {
      strip.setPixelColor(i, c);
    }
    strip.show();
    delay(wait);
  
}

/*************** colorWipe method *********************/
// Fill the dots one after the other with a color
// speed of the fill is based on the wait parameter
void colorWipe(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<strip.numPixels(); i++) {
    strip.setPixelColor(i, c);
    strip.show();
    delay(wait);
  }
}

/*************** rainbow method ********************/
// cycles through ALL colors for all neopixels 
// speed of cycle based on wait parameter
void rainbow(uint8_t wait) {
  uint16_t i, j;

  for(j=0; j<256; j++) {
    for(i=0; i<strip.numPixels(); i++) {
      strip.setPixelColor(i, Wheel((i+j) & 255));
    }
    strip.show();
    delay(wait);
  }
}

/*************** rainbowCycle method *******************/
// Slightly different, this makes the rainbow equally distributed throughout
void rainbowCycle(uint8_t wait) {
  uint16_t i, j;

  for(j=0; j<256*5; j++) { // 5 cycles of all colors on wheel
    for(i=0; i< strip.numPixels(); i++) {
      strip.setPixelColor(i, Wheel(((i * 256 / strip.numPixels()) + j) & 255));
    }
    strip.show();
    delay(wait);
  }
}

/******************** theaterChase method ****************/
//Theatre-style crawling lights.
void theaterChase(uint32_t c, uint8_t wait) {
  for (int j=0; j<10; j++) {  //do 10 cycles of chasing
    for (int q=0; q < 3; q++) {
      for (int i=0; i < strip.numPixels(); i=i+3) {
        strip.setPixelColor(i+q, c);    //turn every third pixel on
      }
      strip.show();

      delay(wait);

      for (int i=0; i < strip.numPixels(); i=i+3) {
        strip.setPixelColor(i+q, 0);        //turn every third pixel off
      }
    }
  }
}

/****************** theaterChaseRainbow ********************/
//Theatre-style crawling lights with rainbow effect
void theaterChaseRainbow(uint8_t wait) {
  for (int j=0; j < 256; j++) {     // cycle all 256 colors in the wheel
    for (int q=0; q < 3; q++) {
      for (int i=0; i < strip.numPixels(); i=i+3) {
        strip.setPixelColor(i+q, Wheel( (i+j) % 255));    //turn every third pixel on
      }
      strip.show();

      delay(wait);

      for (int i=0; i < strip.numPixels(); i=i+3) {
        strip.setPixelColor(i+q, 0);        //turn every third pixel off
      }
    }
  }
}

/***************** Wheel utility method *******************/
// Input a value 0 to 255 to get a color value.
// The colours are a transition r - g - b - back to r.
uint32_t Wheel(byte WheelPos) {
  WheelPos = 255 - WheelPos;
  if(WheelPos < 85) {
    return strip.Color(255 - WheelPos * 3, 0, WheelPos * 3);
  }
  if(WheelPos < 170) {
    WheelPos -= 85;
    return strip.Color(0, WheelPos * 3, 255 - WheelPos * 3);
  }
  WheelPos -= 170;
  return strip.Color(WheelPos * 3, 255 - WheelPos * 3, 0);
}
