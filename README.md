re ## Chinese Checkers

Chinese Checkers, so called Trylma, is a classic strategy board game which can be played by two, three, four or six people at once. The objective is to be first to race all of one's pieces across the hexagram-shaped board into "home"-the corner of the star opposite one's starting corner-using single-step moves or moves that jump over other pieces. 

<hr>

### Implementation and usage details

This game is written in client-server model. Client is desktop application written in JavaFX. It is multiplayer based, there should be at least two people to start the game. There are a few design patterns used in this project: Singleton, Mediator, Observer and Abstract Factory. All of them are described below in corresponding sections where they were used.



* ### Board and rules representation
