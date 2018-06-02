# OpenGL Examples

This repository contains the examples used in class. The examples try to use a common organization of application functionality. [SDL - Simple DirectMedia Layer](https://www.libsdl.org/download-2.0.php) is used to supply application window and user interface event support. **main.cpp** contains the program main function and all SDL calls related to window creationg, configuration of drawing buffers and polling for events. **appCtl.cpp/h** contains application specific logic, initializes the OpenGL context, initializes application state, updates application state for each frame draw and responds to events information sent from **main.cpp**. **draw.cpp/h** contains the drawing code. 

**Ex-1** - Draws coordinate axes using line sequences. Demonstrates matrix stack manipulation and using glDrawArrays(), with GL_LINES, and an array of initialed vertex coordinates. ESC key stops program. 

**Ex-2** - Draws coordinate axes using tetrahedra consisting of four triangular faces. Vertices and face normals are initialized as arrays of vertex coordinates. glDrawArrays() is invoked with GL_TRIANGLES. Fixed pipeline lighting model illuminates faces of objects based on the face normal vector. 'd' key toggles the display of an additional tetrahedron in the middle of the scene. 'r' key toggles rotation of this tetrahedron.

**Ex-3** - Draws coordinate axes both as tetrahedra and cones. Demonstrates use of ``struct faceModel`` for encapsulating data related to a model and implementing a draw function for a model. 't' toggles a central tetrahedra. 's' toggles a central sphere with normals. 'r' toggles rotation of central object. 

**Ex-4** - Demonstrates the order of transformations. Keys 'x', 'X', 'y', 'Y', 'z' and 'Z' apply translations. Keys 'a', 'A', 'b', 'B', 'c' and 'C' apply rotations around the major axes. The 'l' key performs the transformation left to right while the 'r' key performs transformation right to left. The ' ' key clears transformations. With each change of transformation sequence the product matrix is displayed on the console. 

**Ex-5** - Demonstrates loading and displaying OBJ format files. Key 'r' toggles rotation. Key 'p' toggles pirouette. Key 'c' toggles circling around Z axis.

**Ex-6** - Demonstrates using depth buffer offsets to highlight an object. Keys 'F' and 'f' increment and decrement the offset factor. Keys 'U' and 'u' increment and decrement the offset unit.  

**Ex-7** - Demonstrates texture maps. Key 'r' toggles rotation. 

**Ex-8** - Demonstrates simple use of shader. Draws a tetrahedron with one color per face and again with one color per vertex. Per vertex coloring demonstrates how color values are interpolated across fragments during fill operations. Key 'r' toggles rotation.

**Ex-9** - Demonstrates simple use of shader for lighting model. Draws a tetrahedron with one normal per face and again with one normal per vertex. Simple diffuse lighting is calcuated in vertex shader and color is interpolated across fragments during fill operation. Key 'r' toggles rotation.

**Ex-10** - Demonstrates full ambient/diffuse/specular lighting model implemented using 3 different methods. Each method is implemented in the vertex or fragment shader giving a total of 6 combinations. "reflect" shader calculates reflection vector for implementing specular. "halfangle1" uses the dot product with the half angle vector. "halfangle2" uses half angle but assume local viewer position. Key 'r' toggles rotation. Keys '0' ... '5' choose one of the 6 shader program options. Keys 'a'/'A', 'd'/'D', 'd'/'D', 's'/'S' and 'e'/'E' decrease and increase the ambient, diffuse, specular and specular exponent components of the materials array. 

**Ex-11** - Demonstrates different a few things that can be done when vertices carry an extra float tag which has the values 0, 1 and 2 for each vertex enumerated in a face. This tag can be used by the vertex shader to create a barycentric coordinate for each fragement which can be further used to create shader effects. Keys '0' .. '9', ':' select the different shader options. The same keys as Ex-10 control lighting coefficients. 

**Ex-12** - Demonstrates three shader programs. The first uses the uv coordinates of a fragement to create a pattern of strips on a sphere. The second uses the uv coordinates to map a texture image of the earth onto a sphere optionally uses a second texture image to cause only ocean areas to exhibit specular reflection. The third maps a texture image onto the chicken model. Key 'c' displays the chicken. Key 'g' displays the globe and 'm' determines specular reflection. Key 'u' displays the stripped sphere. 

**Ex-13** - Demonstrates the use of shaders to implement an object selection capability. Clicking on a sphere selects it and changes its color. Selected objects can be moved in world coordinates using the 'X'/'x', 'Y'/'y' and 'Z'/'z' keys. 

**Ex-14** - Demonstrates implementing shadows using shaders. The scene is rendered twice. The first time from the point of view of the light source capturing the depth information in a shadow texture map. The second render uses the shadow map to determine if object geometry (on a per fragment basis) are exposed to the light source of obstructed by other objects. The shader uses a fuzzing technique to try to produce smooth shadow edges. Key 'v' displays the scene from the point of view of the light and 'd' displays only the depth information. Culling is used to improve shadow effects - first pass culls front facing triangles and the second pass culls back facing triangles. Key 'c' toggles the culling display mode. 

**Ex-15** - Demonstrates using a shader to produce a moving local light that acts as a spot light. 

**Ex-16** - Extends Ex-14 by including a moving sphere and a camera (an innverviewer) that tracks the movement of the sphere. In the normal view the scene objects, sphere and camera view frame are seen in their global positions. Key 'i' toggles the display to the point of view of the camera. Key 'p' pauses the animation of the sphere. Other keys from Ex-14 are also implemented. 

**Ex-17** - Demonstrates Bezier curves and patches. Key 'c' shows the Bezier curve and control points. Key 'p' shows the Bezier patch and control points. Keys 'v' and 'd' work as in Ex-14.

**Ex-18** - Demonstrates measuring distance across a manifold using Dijkstra's algorithm. Key ' ' selects a new random pair of source and sink points. Key 'r' reverses the selection. Key 'w' toggles wireframe display mode on the manifold. 

**Ex-19** - Demonstrates straight paths across a manifold. Key's 'v' and 'f' toggle the display of vertex and face normals. Mouse click selects a starting face. Key 's' seeds the path at the selected face. Key 'a' advances the path through to the next face. Key 'l' launches an animated object along the path. Key 'p' toggles pause on the animation. 

**Ex-20** - Demonstrates glViewport() and mutil_Frustum() to provide cross-eyed stereo display. Mouse click selects object and 'X'/'x', 'Y'/'y' and 'Z'/'z' translate the selected objects along the coordinate axes. 

**Ex-21** - Demonstrates glViewport() and mutil_Frustum() to provide four regions within the window. The program takes several command line options. 

* ./ex-21 -x 3 -y 3
	* forms a 3x3 grid of viewports
* ./ex-21 -x 3 -y 3 -b 0.5
	* set border around viewports to 1/2
* ./ex-21 -f -1,1,-1,1
	* start with single frustum and correspoding viewport

For single viewport mode 'L'/'l', 'R'/'r', 'B'/'b' and 'T'/'t' adjust left, right, bottom and top by 0.1. Key 'v' toggles from using entire window as viewport and taking viewport from frustum values. In grid mode, 'm' key toggles whether mouse controls trackball or moves viewport. Object selection and movement are as before.  

## Getting the Repository

* from the directory in which you want to place this repository type ``git clone https://cs.uwlax.edu:17443/CS475-F17/GL-Examples``

## Use of C++

* Allows treating vectors and matrices as "value" like entities which simplifies expressions by allowing them to look more like mathematical expressions. 
* Provides extensible (grows as needed) arrays in ``std::vector<type>`` object.
* Provides a variety of other useful data conainer objects. 

## Included Third Party Code

* Matrices.cpp/h and Vectors.h from Song Ho Ahn (no license specified but web site Q/A indicates no restriction on use/modification/redistribution). 
 * http://www.songho.ca/opengl/gl_matrix.html
* open source Json for Modern C++ library (licensed under MIT 3 clause by Niels Lohmann) provides convenient access to dictionary objects
 * https://github.com/nlohmann/json
 
## Platform Necessities

* OS X
 * Xcode gives LLVM compiler and debugger
 * install HomeBrew Package Manager (https://brew.sh)
 * ``brew install sdl2``
 * build examples with ``make ex-1``
* Linux
 * ``apt-get install dev-essentials build-essentials libgl1-mesa-dev sdl2``
 * need OpenGL capable graphics and driver
 * build with ``make ex-1``
* Windows
 * Visual Studio and command line tools, use Visual Studio Developer Command Prompt
 * use nmake build tool, ``winfile`` is the nmake makefile
 * assumes building 32 bit apps
 * install [Git for Windows](https://git-for-windows.github.io), "Git Bash" provides command line git capabilities
 * SDL2 -- download zip place in same directory as projects
 * glew-2.1.0 -- download zip place in same directory as SDL2 and projects.
 * ``nmake /f winfile dll`` will copy the dll files for both SDL2 and glew into the project directory so that executables will cfind it
 * ``nmake /f winfile clean`` deletes build products including the copy of SDL2.dll
 * ``nmake /f winfile ex-1.exe`` builds the example
