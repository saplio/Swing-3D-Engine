# 3D Engine

## Overview

This project is a simple 3D graphics engine created using Java's Swing graphics library.

## Features and Controls

This project contains classes that make it very straightforward to create a 3D environment with a single camera view, display that view in different Swing containers, navigate the environment with keyboard controls, and add custom objects to the environment during runtime.

The main java file is hardcoded to automatically create a field of octagons in front of the camera when first starting the program.

The project does not currently have the ability to control the overlapping of surfaces, which simply overlap according to the earlier placed surface being on top.

### Object Adder

The object adding system reads from .txt files in the src/resources/objects folder. To make a new object in this folder, you must define the color and vertices of each surface of the object like so, leaving a blank line between surfaces:
```
[red] [green] [blue] # specify rgb value of surface color using integers from 0 - 255
[x] [y] [z] # specify relative coordinates of first vertex using floats
[x] [y] [z] # specify relative coordinates of second vertex using floats
... # repeat for all vertices of surface
```
Leave a blank line between each block of surface values to separate surfaces Some example files are already provided in the objects file.

### Controls

W: move forward 0.35 units
S: move backward 0.35 units
A: move left 0.35 units
D: move right 0.35 units
Space: move up 0.35 units
Z: move down 0.35 units
L: move left 5 units
R: move right 5 units

Q: turn left 1/64th of a full rotation
E: turn right 1 64th of a full rotation
T: tilt up 1/64th of a full rotation
G: tilt down 1/64th of a full rotation

N: begin the prompt to add an object from the objects folder to the environment

## Plans for this project

I originally made this project at the beginning of college in the spring of 2025 when first learning the Java language. There are many misuses of the Swing library and design choices that make the program much more disorganized and harder to develop than it needs to be. 

I want to clean up this project to make it easier to understand and develop, and to rewrite it to use Swing properly and more efficiently.

I also want to add features such as the ability for multiple cameras to view a single space from different positions, the ability to manipulate objects in an environment during runtime, add additional UI to the camera such as coordinate information, and create smoother camera movement.