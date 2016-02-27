## How To Build
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
 6. Make a folder called ```ExMCL/runmehere/libs```
  1. Download this file [here](https://drive.google.com/file/d/0B7Dty1HqMYtYcXBRY1FFZWVwMTA/view?usp=sharing)
  2. Extract the libraries and copy them to ```ExMCL/runmehere/libs```
  3. Make sure you have these files in ```ExMCL/runmehere/libs```
    * ```FileDrop.jar```
    * ```LogWindowFramework-5.1.jar```
    * ```ReflectionHelper.jar```
 7. Run ```gradlew build```
 8. To start ExMCL, run ```ExMCL/runmehere/ExMCL Launcher.jar```
