# White paper for the client-server protocol

The protocol is based on UDP because we need a steady flow of packets (with the out-of-order ones being discarded) to allow real-time interactions. This game will probably not need it but we may want to implement other time-sensitive functionalities in the future. We need some reliability functions to allow packets to be somewhat reliably delivered.

The protocol message is constituted of a header and a payload, and is formated as follows:

```[ [ TYPE ][ SIZE ] ][ PAYLOAD ]```

where:
- `TYPE` is a 4 bytes integer containing the type of the message 
- `SIZE` is a 4 bytes integer containing the size of the payload 
- `PAYLOAD` is a UTF-8 JSON formated string containing additionnal data related to the message type

The default port for the server is UDP port `12345`.

## Connection-less messages
- `OK(200)`: Generic success response. This is always sent by the server and will contain any data appropriate

- `DISCOVER(110)`: Usually sent to the broadcast address (on the default port). Allows discovery of servers on the local network.
    - A client's payload will contain the following mandatory fields:
        - `port`: an integer representing the port the server should use to respond
    - A server's payload will contain the following mandatory fields:
        - `name`: a string representing the server's name
        - `description`: a string representing the server's description
        - `online_players`: an integer representing the current amount of connected players

## Connection-full messages
- `CONNECT(100)`: Request connection to a server.
    - A client's payload will contain the following mandatory fields:
        - `name`: a string representing the client's display name
    - A client's payload may contain the following optional fields:
        - `uuid`: a string representing a unique identifier. This is used to uniquely identify a player and manage the session (reconnect when connection dropped etc.).
    - A server's payload will contain the following mandatory fields:
        - `uuid`: a string representing a unique identifier. This must be present in the payload of every client request once the connection is established. If the client provided their own UUID, the client is trying to reestablished a lost connection. The server must try to reconnect the user, and will respond with the provided UUID on success, another UUID on failure.
        
- `DISCONNECT(101)`: 
