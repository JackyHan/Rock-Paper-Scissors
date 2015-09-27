# Rock-Paper-Scissors
Java network Rock Paper Scissors game using the protocol described in the UDP_protocol.txt file for communication.

Client GUI consists of options (Rock Paper Scissors buttons), a new round button, and score information for the players in the game.

The server uses a session manager to connect clients waiting for games together. If a client exits it quits the session for the other client as well.

Usage:

  Server: java RockPaperScissorsServer 'host address' 'port number'
  
  Client: java RockPaperScissors 'server host' 'server port' 'client host' 'client port' 'name of player'
