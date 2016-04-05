#!/usr/bin/env bash

echo ExMCL first time build script

echo setting up
mkdir runmehere
mkdir runmehere/libs
mkdir providedlibs
mkdir tmp

echo downloading Minecraft.jar
curl https://s3.amazonaws.com/Minecraft.Download/launcher/Minecraft.jar --output tmp/Minecraft.jar

echo placing Minecraft.jar
cp tmp/Minecraft.jar providedlibs/
cp tmp/Minecraft.jar runmehere/

echo started building libs
cd tmp

echo Building LogWindow
git clone https://github.com/n9Mtq4/LogWindow.git
cd LogWindow
chmod +x ./gradlew # just to make sure
bash ./gradlew build
cp -R build/libs/. ../../runmehere/libs/
echo LogWindow done!
cd ..

echo Building KotlinExtLib
git clone https://github.com/n9Mtq4/KotlinExtLib.git
cd KotlinExtLib
chmod +x ./gradlew # just to make sure
bash ./gradlew build
cp -R build/libs/. ../../runmehere/libs/
echo KotlinExtLib done!
cd ..

echo Building ReflectionHelper
git clone https://github.com/n9Mtq4/ReflectionHelper.git
cd ReflectionHelper
chmod +x ./gradlew # just to make sure
bash ./gradlew build
cp -R build/libs/. ../../runmehere/libs/
echo ReflectionHelper done!
cd ..

echo Building FileDrop
git clone https://github.com/n9Mtq4/FileDrop.git
cd FileDrop
chmod +x ./gradlew # just to make sure
bash ./gradlew build
cp -R build/libs/. ../../runmehere/libs/
echo FileDrop done!
cd ..

echo Building Minecraft Launcher Extractor
git clone https://github.com/ExMCL/MinecraftLauncherExtractor.git
cd MinecraftLauncherExtractor
cp ../../providedlibs/Minecraft.jar Minecraft.jar
chmod +x ./gradlew # just to make sure
bash ./gradlew build
echo Minecraft Launcher Extractor done!
cd ..

echo finished all dependancy builds

echo Extracting Minecraft Launcher
cd MinecraftLauncherExtractor
curl https://s3.amazonaws.com/Minecraft.Download/launcher/launcher.pack.lzma --output launcher.pack.lzma
java -jar build/libs/MinecraftLauncherExtractor.jar Minecraft.jar launcher.pack.lzma ../../providedlibs/laucher.jar
echo Done extracting minecraft launcher
cd ..

cd ..
echo cleaning up
rm -rf tmp

echo
echo
echo Building ExMCL
chmod +x ./gradlew
bash ./gradlew build

echo
echo
echo
echo All done!
echo You should be able to use \"./gradlew build\" from now on.
echo You can still use this script to update libraries.
echo The \"runmehere\" directory can be rename to ExMCL and distributed without the rest of the files.
echo Open \"ExMCL Launcher.jar\" in the \"runmehere\" directory
