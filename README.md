TicTacToe
=========

Basic TicTacToe game on Android for learning purposes

Features include:

1 player
2 players with an easy and more difficult setting
Ability to switch between 1 player and 2 players while playing

-------------------------------------------------

In terms of implementation

The game components and game state variables are in the main activity class because the application itself was extremely simple. Basically, keep track of who's turn it is and check if the move played is a winning move. Game state variables are changed through tapping different settings which are linked through onClickListeners.

The AI was pretty simple and implemented through an abstract superclass class with different difficulties as subclasses since that setting was the only feature of the game likely to change.

Easy AI simply moves to an avaliable space while "Hard" AI looks for 2 in a row's to either win or block the player.
