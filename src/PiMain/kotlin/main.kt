import kotlinx.cinterop.pin
import libbcm.*
import platform.posix.*


@OptIn(ExperimentalUnsignedTypes::class)
class LED(val pin: u_int8_t) {
    fun blink(delayMs: UInt = 500u) {
        bcm2835_gpio_write(pin, HIGH)
        bcm2835_delay(delayMs)
        bcm2835_gpio_write(pin, LOW)
        bcm2835_delay(delayMs)
    }

    fun on() {
        bcm2835_gpio_write(pin, HIGH)
    }
    fun off() {
        bcm2835_gpio_write(pin, LOW)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
class Button(val pin: u_int8_t) {
    val readMode :  UByte = 0x00u
    val pud_up: UByte = 0x02u
    init {
        println("init button: pin $pin function select BCM2835_GPIO_FSEL_INPT")
        bcm2835_gpio_fsel(pin, readMode);
        //  with a pullup
        println("With a pullup BCM2835_GPIO_PUD_UP")
        bcm2835_gpio_set_pud(pin, pud_up);
        // And a low detect enable
        println("And low detect enable")
        bcm2835_gpio_len(pin);
    }

}

@OptIn(ExperimentalUnsignedTypes::class)
fun main(args: Array<String>) {
    if(bcm2835_init() == 1 ) {
        println("BCM2835 initialised")

//        println("Set PIN 18 to output")
        val led = LED(18u)
        val button = Button(25u)
        val writeMode : UByte = 0x01u
        val readMode :  UByte = 0x00u
        val pud_up: UByte = 0x02u

        bcm2835_gpio_fsel(led.pin, writeMode) // not sure why this is 18 and not 24 (it's PIN 18, which should have value 24?)
        println("Pull LED down")
        bcm2835_gpio_write(led.pin, LOW)

        println("Blinking")
        for(i in 0..5) {
            println("\t\t$i")
            led.blink(1000u)
        }

        println("Now, button presses... Click to quit")
        var clicked = false
        val uintOne: u_int8_t = 1U
        println("Click button to exit")
        while (!clicked)
        {
            if (bcm2835_gpio_eds(button.pin) == uintOne)
            {
                println("bcm2835_gpio_eds(button) == uintOne registering a click?")
                // Now clear the eds flag by setting it to 1
                bcm2835_gpio_set_eds(button.pin);
                println("low event detect for pin ${button.pin}");
                clicked = true
            }

            // wait a bit
            bcm2835_delay(500);
        }

        println("Closing...")
        bcm2835_close()
    } else {
        println("BCM2835 failed to initialise")
    }
    exit(1)
}
