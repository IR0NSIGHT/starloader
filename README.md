# StarLoader
## What is StarLoader?
StarLoader is a mod API, as well as a program to replace starmades .java files with your own.


## I want to make a mod
See [Getting Started](https://gitlab.com/generic-username/starloader/-/wikis/home)\
Also see: [Api Documentation](https://starloader-api.readthedocs.io/en/latest/?)

## Installation (IntelliJ)
 - Clone into a new project
 - Add StarMade.jar & everything in the StarMade/libs/ folder into your dependencies
 - Mark insert/ and src/ as source directories
 - Set java compiler to target java 6, as that is what is bundled with the starmade starter
 - Usual procedure to build a jar file, set InstallerFrame as the main class.
 - Build artifacts and run the jar file, do not try to run it from intellij.
 - Done!
 
## Installation (Eclipse)
WIP

## ModPlayground.java
This file allows you to play around with the event system and modding in general. Take a look around

It is located in insert/api

## Replacing classes
 - Everything in insert/api and insert/org will replace the existing starmade classes.
 - If you want to make your own, decompile the file you want to add stuff to, then copy its contents to the correct folder.
 - You can tell if you got the right folder because the 'package org.schema.whatever' will NOT have an error
 - Once enough functionality is in place, mod makers shouldn't have to use this.

## I have a bug / cant get it to install!
Ask in the starmade discords modding channel, or me directly: JakeV#5670.

## StarLoader vs Star-API
Wrapper classes where moved to the Star-API project.
ModLoader classes, and some other helpers go here
Helper Classes/methods are allowed here if they are not a re-define or refactor of an existing method

## Thanks
Mega's SMModLoader for some code for the installer,