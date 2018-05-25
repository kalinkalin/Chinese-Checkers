Th ## Chinese Checkers

Chinese Checkers, so called Trylma, is a classic strategy board game which can be played by two, three, four or six people at once. The game takes place on the hexagram board. Objective is to get all your marbles into oposite corner. More info [here](https://en.wikipedia.org/wiki/Chinese_checkers).

<hr>

### Implementation and usage details

This game is written in a client-server model. There are a few design patterns used in this project: Singleton, Mediator, Observer and Abstract Factory. All of them are described below in corresponding sections where they were used.



  * ### Board and rules representation
    One of the primary goals was to make simple and easily usable representation of board and fields on it. Fields are expressed by two co-ordinates and current color. First coordinate is number of row and the second is order in given diagonal. All the fields are added to Array List, our board representation. 
    
    The biggest issue here is generating different board for different number of players. **This is where Abstract Factory design pattern do the job**. There is abstract product - class `AbstractBoard`, which contains methods to generate center and all corner areas. This class is extended by concrete products - boards with given number of players, which contains concrete cases generating some corners in given colors by using inheritated methods. For all board types there is concrete Factory class, for example `FourPlayersBoardFactory`
   
