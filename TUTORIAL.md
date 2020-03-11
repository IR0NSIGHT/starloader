# StarLoader Tutorial
## What is StarLoader?
StarLoader is a mod API, as well as a program to replace starmades .java files with your own.

## I want to make a mod
Good, however, the mod API is not done, and you will currently have to interface directly with StarMade.
The event system works, but is lacking in events.

## Installation (IntelliJ)
 - Clone into a new project
 - Add StarMade.jar & everything in the StarMade/libs/ folder into your dependencies
 - Mark insert/ and src/ as source directories
 - Set java compiler to target java 7, as that is what is bundled with the starmade starter
 - Usual procedure to build a jar file, set InstallerFrame as the main class.
 - Build artifacts and run the jar file, do not try to run it from intellij.
 - Done!

## ModPlayground.java
This file allows you to play around with the event system and modding in general. Take a look around

It is located in insert/api

## Replacing classes
 - Everything in insert/api and insert/org replace the existing starmade classes.
 - If you want to make your own, decompile the file you want to add stuff to, then copy its contents to the correct folder.
 - You can tell if you got the right folder because the 'package org.schema.whatever' will NOT have an error
 - Once enough functionality is in place, mod makers shouldn't have to use this.

## I have a bug / cant get it to install!
Ask in the starmade discords modding channel, chances are its not your fault