## How To Build
### With shell script
Linux / Mac / Windows (when bash finally arrives).

Open terminal and type:

```git clone https://github.com/ExMCL/ExMCL.git && cd ExMCL && sh fullbuild.sh```

To start ExMCL, run ```ExMCL/runmehere/ExMCL Launcher.jar```
### Without shell script
(These steps are designed for linux/mac. They following instructions will be
similar on windows)
 1. cd to a workspace
 2. ```git clone https://github.com/ExMCL/ExMCL.git```
 3. ```cd ExMCL```
 4. Make a folder called ```/ExMCL/providedlibs```
  1. Download "Minecraft.jar" from [here https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar](https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar)
  2. Copy ```Minecraft.jar``` to ```ExMCL/providedlibs```
  3. Goto wherever you minecraft is stored
    * Windows: ```%appdata%\.minecraft```
    * Mac: ```~/Library/Application Support/minecraft```
    * Linux: ```~/.minecraft```
  4. Copy ```launcher.jar``` to ```ExMCL/providedlibs```
 5. Make a folder called ```ExMCL/runmehere/```
  * Also add another copy of the "Minecraft.jar" from step 4. To ```ExMCL/runmehere``` [Download here](https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar)
  1. Git clone all these repos
    * https://github.com/n9Mtq4/LogWindow
    * https://github.com/n9Mtq4/KotlinExtLib
    * https://github.com/n9Mtq4/ReflectionHelper
    * https://github.com/n9Mtq4/FileDrop
  2. cd into and ```./gradlew build``` on all the repos
  3. Copy all the builds (located in ```reponame/build/libs/```) and copy them into ```ExMCL/runmehere/libs```
 6. Run ```./gradlew build``` on the ExMCL project
 7. To start ExMCL, run ```ExMCL/runmehere/ExMCL Launcher.jar```
