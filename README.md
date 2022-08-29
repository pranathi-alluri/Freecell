# Freecell
Implementation of the single-player card game ["Freecell"](https://en.wikipedia.org/wiki/FreeCell). Built in Java using an MVC design pattern.  

## Gameplay
The controller is publically used to run the game, taking in the intial input and printing the current game state to the provided output. 

### Start Game
This implementation has two game types. Hence, to start the game one need to declare the corresponding model: 
<ul>
<li> SINGLEMOVE - new FreecellModelCreator().create(FreecellModelCreator.GameType.SINGLEMOVE) 
<ul>
<li> Only move one card at a time 
</ul>
<li> MULTIMOVE - new FreecellModelCreator().create(FreecellModelCreator.GameType.MULTIMOVE)
<ul>
<li> Move several cards that are consecutively descending value and alternating colors from a one cascade pile to another casacde pile 
</ul>
</ul>

Then, start the game by creating a new insatnce of the public class SimpleFreecellController by passing in the created model, along with a readbale and appendable, and calling 



