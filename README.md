## Chinese Checkers
Chinese Checkers, so called Trylma, is a classic strategy board game which can be played by two, three, four or six people at once. The game takes place on the hexagram board. Objective is to get all your marbles into oposite corner. More info [here](https://en.wikipedia.org/wiki/Chinese_checkers).

<hr>

### Implementation and usage details

This game is written in a client-server model. There are a few design patterns used in this project: Singleton, Mediator, Observer and Abstract Factory. All of them are described below in corresponding sections where they were used.



  * ### Board represantion
    One of the primary goals was to make simple and easily usable representation of board and fields on it. Fields are expressed by two co-ordinates and current color. First coordinate is number of row and the second is order in given diagonal. All the fields are added to Array List, our board representation. **All of the classes representing rules and board are in** `model` **package**.
    
    The biggest issue here is generating different board for different number of players. **This is where Abstract Factory design pattern do the job**. There is abstract product - class `AbstractBoard`, which contains methods to generate center and all corner areas. This class is extended by concrete products - boards with given number of players, which contains concrete cases generating some corners in given colors by using inheritated methods. For all board types there is concrete Factory class, for example `FourPlayersBoardGenerator` which is responsible for initiating whole board, this classes are implementing `BoardGenerator` interface, so reffering by interface makes possible deciding which board to initiate in the run time.
    
    
   * ### Server
     Server is written using sockets. There is main server socket which is waiting for clients to connect. For each connected client there is separate thread(`Player` class), responsible for getting input and sending output to a given client socket. **Here I used Mediator and Observer design patterns** allow different clients to comunicate with each other and to flow messages both ways beetween server and clients. `DefaultGameRoom` is a mediator class which have an array of all clients threads and method which is parsing input from given thread and executing sepcified behavior and then sending proper messeges to some threads. `Player` is an observer class which contains client socket and waits for some IO on it, and if given socket is sending message it is transffered to a mediator parser-class. Observer has also method to asynchronously send message to a client from a mediator.
    
   * ### Client
     Client is written in JavaFX. There is simple graphical interface, very goodlooking interface was not the main purpose of the project. Client imports files from model package and whole board is generated from the same board representation as in the first paragraph. 
     
     
     ![](https://preview.ibb.co/dtwBAo/cc.png)
