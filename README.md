# Freecell
Implementation of the single-player card game ["Freecell"](https://en.wikipedia.org/wiki/FreeCell). Built in Java using an MVC design pattern.  

## Gameplay
The controller is publically used to run the game, taking in the initial input and printing the current game state to the provided output. 

#### Starting Game
This implementation has two game types. Hence, to start the game one needs to declare the corresponding model: 
<ul>
<li> SINGLEMOVE - new FreecellModelCreator().create(FreecellModelCreator.GameType.SINGLEMOVE) 
<ul>
<li> Only move one card at a time 
</ul>
<li> MULTIMOVE - new FreecellModelCreator().create(FreecellModelCreator.GameType.MULTIMOVE)
<ul>
<li> Move several cards that are consecutively descending value and alternating colors from one cascade pile to another cascade pile 
</ul>
</ul>

Then, start the game by creating a new instance of the public class SimpleFreecellController by passing in the created model, along with a readable and appendable, and then calling playGame: 

<ul>
<li> new SimpleFreecellController(model, readable, appendable).playGame(deck, numOfCascPiles, numOfOpenPiles, shuffle);
<ul>
<li> model = created model
<li> readable = input
  <ul>
  <li> ex: new StringReader("C1 1 F1")
  </ul> 
<li> appendable = output
  <ul>
  <li> ex: new StringBuffer()
  </ul> 
 <li> deck = model.getDeck()
 <li> numOfCascPiles = Number of Cascade Piles
 <li> numOfOpenPiles = Number of Open Piles
 <li> shuffle = shuffles the deck if true and doesn't if false 
</ul>
</ul> 

#### Playing Game
To move a card a valid input includes the source pile of the card being moved, the card index, and the destination pile. For example, a valid input would be "C3 5 F2".

<ul>
<li> C = Cacscade Pile
<li> F = Foundation Pile
<li> O = Open Pile 
</ul>

#### Ending Game
When the game is won, the final game state is returned along with a "Game Over" Message. If at any time a player would like to quit the game they can input "Q" or "Quit". 




