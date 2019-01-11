# Le2LeJosEv3-Elephant
This is the LEGO® Mindstorms EV3 **Elephant** example program in the Java programming language that uses a Java Implementation of _LEGO® Mindstorms EV3 Programming Blocks (icons)_ on LeJOS EV3. 

You can find the building instructions and the LEGO® icon-based program of the _Elephant_ in the LEGO® icon based (LabView-based) Programming Environment (Education Edition).
The Elephant is one of the Expansion Set Models.
You can download the LEGO® Programming Environment at https://education.lego.com/en-us/downloads/mindstorms-ev3/software (or one of the other language pages).

## Dependencies
This project depends on the **Le2LeJosEv3** Library (see https://github.com/robl0377/Le2LeJosEv3) that sits on top of the current version of the LeJOS EV3 framework. 
Please add the JAR file of the _Le2LeJosEv3_ Library _(le2lejosev3.jar)_ into this project's classpath before running it. The LeJOS Eclipse plugin will take care of the transfer of the library and the program JAR files to the EV3 brick.

In this project I am using the **LeJOS EV3 v0.9.1beta** framework (see https://sourceforge.net/projects/ev3.lejos.p/) and a standard LEGO® Mindstorms EV3 Brick.

## Resources
The program uses one sound file that is in the project's _resources_ directory. 
Please do one of the following:
1. Run the ANT script _build_res.xml_ to pack the file in the _resources_ directory into the archive _elephantres.jar_. Then add this archive to your Eclipse project's classpath. The LeJOS Eclipse plugin will take care of the transfer of the archive to the EV3 brick before running the program.
2. Upload (via SCP) the file in the _resources_ directory to your EV3 Brick to the directory _/home/lejos/lib_.

The **sound file** is converted from the MP3 sound file found at http://www.thumb.com.tw/images/blog/lego_ev3_sound_files/ev3_sounds.zip.
There are several ways to convert the MP3 sound file to WAV. 
I was using the online converter at https://audio.online-convert.com/convert-to-wav. 
Be sure to set _Change audio channels:_ to _mono_.

---
LEGO® is a trademark of the LEGO Group of companies which does not sponsor, authorize or endorse this site.
