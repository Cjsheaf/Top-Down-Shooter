------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE: Top-Down Shooter (temporary name).
PURPOSE OF PROJECT: Video Game.
VERSION or DATE:
HOW TO START THIS PROJECT: Run the class named TopDownShooter or MapEditor.
AUTHORS: Christopher Sheaf

/*
 ************************************
 *------PROGRAM FLOW (IN BRIEF)-----*
 ************************************
 */

- TopDownShooter(class) creates the main JFrame and contains the constants for screen resolution. Eventually
these will no longer be constants and instead will be able to change at run-time, hopefully handled by a
static method inside TopDownShooter(class).

- TopDownShooter(class) also creates a GamePanel(class) which is an extended JPanel intended to be the
main canvas on which drawing will occur and on which more Java Swing components will be added, such as
for the User Interface. GamePanel(class) also is responsible for periodically calling Update() and Draw()
on all directly subordinate classes and object managers in order to accomplish this. There is a master
game Timer declared inside of GamePanel(class) for this purpose.

- Classes subordinate to GamePanel(class) include, but may not be limited to (in case of out-of-date docs.)
Map(class), and Player(class).

- GamePanel(class) also contains a ViewScreen(class), which defines the position and boundaries of the
currently visible area of the playing field. GamePanel(class) is currently responsible for centering the
ViewScreen(class) on the Player every frame. GamePanel(class) should be responsible for handing over
control of the ViewScreen(class) to other objects under valid conditions, such as to follow an enemy or
to focus on an object other than the Player.



/*
 ************************************
 *------------KNOWN BUGS------------*
 ************************************
 */

- When the player is moving in two directions at once (diagonally) and the direction key that was FIRST
held down is released, but the other key is still held, the player slides significantly in the first
direction.