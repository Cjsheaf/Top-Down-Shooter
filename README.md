Top-Down-Shooter
================

An in-progress game written in Java, and using a top-down perspective and open source art assets. There's no real gameplay (yet) to speak of. However, just the creation of the admittedly simple game engine has been a tremendous learning experience.

Features:
 - Infinite game world, similar in nature to Minecraft's "chunk" system, loaded in a multithreaded way to avoid performance bottlenecks.
 - Simple map editor to create custom maps instead of relying on the (currently primitive) random map generator.
 
Bugs:
 - The multithreaded map-loading code currently fails to load some portions of the map during game start, leaving them black squares. Moving far enough away from these world "chunks" and then returning usually corrects the issue.
 - The collision-detection system frequently makes the player get stuck on walls when moving diagonally and upwards against them.
