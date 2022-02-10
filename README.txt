=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: jofan
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1.2D Array - Board pieces, which are of a class called "UnitBox", are stored in a 4x4 2D array. They are updated upon each move.

  2.Collections - The game history of the board is stored in a collection (Linked List) where each element is of type
  "UnitBox[][]", essentially representing the entire board. When you press the "Z" key on the keyboard, it sets the board to the last element
  in the Linked List and removes it from the Linked List so that the game history essentially "undoes" itself. Upon each directional keyboard press,
  the current board is added to the Linked List. This is an appropriate implementation of a collection because we need all previous
  boards in order to allow for undo's and they need to be a sequential order, so a Linked List is the most appropriate representation of this property.

  3.File I/O - We write to file the highest score and we retrieve it from the file to compare it with the score of the current game and update it accordingly.
  This use of File I/O is appropriate because we need a method of storing the number in a way such that it is kept even after the project stops running.

  4.JUnit Testing - Tested the state of the game (win/lose? and score), movement of the boxes, correct combinations, file input/output, reset game.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
Game2048
- Implements the methods needed to call the other classes and renders essentially the entire window.
It has a run method which keeps track of any changes made in the game in order to update it.

GameBoard
- Implements the majority of the moving, adding, and game logic of 2048. It also creates the file to store the highest score.
After being called in 'Game2048', it initializes a board, resets it, loads the high score, and renders the page.
Then, it will check for updates from the keyboard to move the board, and subsequently adjust the placement of tiles accordingly,
checking for "movability", winning board, and losing board at each move.

InputKey
- Implements possible key presses and updates a boolean array every time a direction or the 'Z' key is pressed and updates another
boolean (previous key) once it is released. This is called in the GameBoard class in the KeyStatus() method, where it checks what
key was pressed and proceeds with the game logic of moving tiles according to that key.

Instructions
- Just a class for instructions.

UnitBox
- Each individual tile on the board is of class UnitBox placed into a 4x4 array that represents the entire board. It stores
instances of a value, x/y coordinates of current tile position, and x/y coordinates of next point it will move to.

Point
- Implements a coordinate pair to help update the point when moved in the method 'movable()' in GameBoard.
The variables nextPoint and currPoint are of class Point in the class Unit Box that stores the
point the current tile will be sliding to next and the current position, respectively.



- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
Initially, it was hard to implement the checks for movement as well as actually updating the tile position. The logic for it
was confusing and I tried to avoid double for-loops (worrying about time complexity). Once I implemented the for-loops, there was a
bug that was caused by the order of the indices in which I was moving the tiles. After much debugging, I realized that I needed
to switch the order in which I traversed the indices depending on which direction I was moving in. For example, if I was moving
down, then I would have to update the tiles on the bottom row first.
It was also difficult to check if a tile can be added with its neighbor (i.e. if there are 3 tiles with 4 on them in a row
and the direction was to the left, how do I make sure only the leftmost two are added together?).
So for this, I created a 'canAdd' variable in UnitBox that is set to true initially and becomes false once it is already added.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
The body of my code is in the class GameBoard, but overall there is still good separation of functionality because each class
serves a distinct purpose that either behaves differently or defines a different aspect of the game. Most of my private states, if needed,
are encapsulated. If the variable is static and final, I just made it public (like BOARD_WIDTH and BOARD_HEIGHT). For other states
that were needed in other classes, I made sure to create get and set methods for each of them. If given the chance, I
would attempt to split up GameBoard a little more.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
I just googled a lot (helpful for debugging) and looked at a lot of documentation. I didn't use any external images.