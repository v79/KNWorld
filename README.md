# KNWorld - Experiments in Cross-Compilation

This is a test project for Kotlin Native. I've written a simple Kotlin program (inspired by the code at https://kotlinlang.org/docs/tutorials/native/using-intellij-idea.html) and modified it to work. My goal is to cross-compile from Windows to Raspberry Pi.

### Sources

I've duplicated the source code in `src\main\nativeMain` and `src\mainPiMain` - the only difference is the output, which states whether the compilation was from `native` or `pi`.

### Gradle

I've modified `build.gradle.kts` a little, as the examples on Kotlin's website did not work for me. I have added a new target called *Pi*, like this:

```kotlin
 linuxArm32Hfp("Pi") {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
```

### Compilation

To build the 'native' binary (i.e. an executable for the machine you are currently running), just run `gradle nativeBinaries`.

To build for the Raspberry Pi, execute `gradle PiBinaries`.

The resulting executable file called 'KNWorld.kexe' can be found in `build\bin\native\releaseExecutable` and `build\bin\Pi\releaseExecutable`.

Copy the Pi version of KNWorld.kexe to your Raspberry Pi. I've even added extra steps to verify this is a Linux binary build from my host - Windows - machine.

```shell script
pi@raspberrypi:~/PiShare $ uname -a
Linux raspberrypi 5.4.72-v7l+ #1356 SMP Thu Oct 22 13:57:51 BST 2020 armv7l GNU/Linux
pi@raspberrypi:~/PiShare $
pi@raspberrypi:~/PiShare $ file KNWorld.kexe
KNWorld.kexe: ELF 32-bit LSB executable, ARM, EABI5 version 1 (SYSV), dynamically linked, interpreter /lib/ld-linux-armhf.so.3, for GNU/Linux 2.6.32, BuildID[xxHash]=a487af0917a9a720, not stripped
pi@raspberrypi:~/PiShare $ ./KNWorld.kexe
PiMain: Hello, enter your name:
Liam Davison
Your name contains 11 letters
Your name contains 9 unique letters
pi@raspberrypi:~/PiShare $
``` 

Voila, cross-compilation from MS Windows X64 to Raspbian ARM 32Hfp Pi code. No Windows Subsystem for Linux was used in this code.

The next step is to start integrating C libraries _from the Pi_ into the build process, but that's tearing my hair out and is, apparently, not possible from Windows.
