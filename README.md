# 3D Engine

This project is a simple 3D graphics engine created using Java's Swing graphics library.

## Features and Controls

This project contains classes that make it very straightforward to create a 3D environment and create camera views of the environment that can be displayed in Swing containers. Cameras can navigate the environment with keyboard controls, and the user can add custom models to the environment during runtime.

The project uses a 3-dimensional coordinate system. With the default camera orientation, positive X is to the right, positive Y is forward, and positive Z is up.

The main java file automatically loads two camera perspectives in different windows and a field of octagons in front of the cameras when first starting the program.

The project does not currently have the ability to control the overlapping of surfaces, which simply overlap according to the later placed surface being painted on top.

### Model Adder

The model adding system reads from directories in the src/resources/models folder. To make a new model in this folder, you must create a new directory with the desired name for the model, and within it create a text file with the .main file extension. In this file you must define the color and vertices of each surface of the model like so, leaving a blank line between each block of surface values:
```
[red] [green] [blue] [alpha]* // specify rgba value of surface color using integers from 0 - 255 *alpha optional
[x] [y] [z] // specify relative coordinates of first vertex using floats
[x] [y] [z] // specify relative coordinates of second vertex using floats
... // repeat for all vertices of surface
```
You can also include other model files in a model file using semicolons. These files must be in the same folder as the .main file and have the .txt file extension. Include other files like so:
```
;[model file name]; [scale factor]* // specify model file to include (without ".txt") *scale factor float optional
[x] [y] [z] // specify relative location of included model using floats
```
Secondary .txt model files can include other model files as well! However, a model file including a file that is already part of the nest will make an error model instead.

The model adder will treat any non numeric characters the same as spaces (unless they are used to include a file or make a number negative or a decimal). Some example files are already provided in the models folder.

### Controls

Movement:
- W: move forward 0.35 units
- S: move backward 0.35 units
- A: move left 0.35 units
- D: move right 0.35 units
- Space: move up 0.35 units
- Z: move down 0.35 units
- L: move left 5 units
- R: move right 5 units

Rotation:
- Q: turn left 1/64th of a full rotation
- E: turn right 1/64th of a full rotation
- T: tilt up 1/64th of a full rotation
- G: tilt down 1/64th of a full rotation
- X: roll left 1/64th of a full rotation
- C: roll right 1/64th of a full rotation

Misc:
- N: begin the prompt to add a model from the models folder to the environment
- I: print camera info to the console
- M: scale the most recently placed model by 1.1x its size (temporary testing feature)

## Plans for this project

I originally started this project at the beginning of college in the spring of 2025 when first learning the Java language. There are many misuses of Swing and design choices that make the program much more disorganized and harder to develop than it needs to be. 

I want to clean up this project to make it easier to understand and develop, and rewrite it to use Swing properly and more efficiently. (complete!)

I also want to attempt to add the following features:
- The ability for multiple cameras to view a single space from different positions (added!)
- The ability to manipulate surfaces/models in an environment during runtime
- Provide more customization for model files and update model reader (done!)
- Add additional UI to the camera such as coordinate information
- Create smoother camera movement
- Some form of overlap control, even if not fully accurate
- Ability to save 3D environments and reload them when running a separate instance of the program