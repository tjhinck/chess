# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

Client -> Server [diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAEYAdAAZM9qBACu2AMQALADMABwATACcIDD+yPYAFmA6CD6GAEoo9kiqFnJIEGiYiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAEQDlGjAALYo43XjMOMANCu46gDu0ByLy2srKLPASAj7KwC+mMK1MJWs7FyUDRNTUDPzF4fjm6o7UD2SxW63Gx1O52B42ubE43FgD1uogaUCyOTAlAAFJlsrlKJkAI5pXIAShuNVE9yqsnkShU6ga9hQYAAqoNMe9PigyTTFMo1KoqUYdHUAGJITgwNmUXkwHSWGCcuZiHSo4AAaylgxgWyQYASisGXJgwAQao4CpQAA90RpeXSBfdERSVA1pVBeeSRConVVbi8YAozShgBaOhr0ABRK0qbAEQpeu5lB4lcwNQJuYIjCbzdTAJmLFaRqDeeqG6bKk3B0MK+Tq9DQsycTD2-nqX3Vb0oBpoHwIBCJykPVv01R1EBqjHujmDXk87QO9sPYx1BQcDhamXaQc+4cLttjichjEKHz6zHAM8JOct-ejoUrtcb0-6z1I3cPWHPMs49H4tR9lgX7wh2-plm8RrKssDQHKCl76h0ED1mg0ErFciaUB2qYYA04RuG42aTJBXwwDBIIrPBCSIchqEHNc6AcKYXi+AE0DsEysSinAkbSHACgwAAMhA2RFNhzDOtQAYtO03R9AY6gFGg2YrEqXxLD8fwAkC4zLNcYFCsBAajCpxELOpoKabs+y6ZghkIn6LrdjACDCRKmJCSJhLEmAZLvoYe60gejLMtOqncregX3suIowOKkrurK8rlh8yoRXyUUSVQyKbh625+UKI4Cg0R5yCgL4JBeV43oVS5VCugbriaVV5Y5HZ2Q0LmeQBCBAU8IHJg5kngURFYkWRPyUdRDakWhjb6QN8DIGmMB4QRYwjSlY2zXBV5TShM10U2jGeN4fj+F4KDoLE8RJBdV0eb4WBiUKYENI00iRgJkYdJGPS9PJqiKSMk1IegGH2Y8cIBiDyG2X1mGZdlnWPe5wmPV5ag+Tu-nUneRUwFQppIFoJ5XpVCGg2g86RY60UNHFG4JdocoKjDYM1YKC1+W6s4tV2BV4wyRgoNwpPnpR1WC5zdUxdIIvMoYEt81lH5VO1zlo2emSqIBcNQwimVGXpNQIymS04St+HZvRzamCdLH+KiG7+NgEqagJ6IwAA4sqGjPYbZaNF731-fYyrA7tlPgwZ8NlmzRR2S9jkdeiPu5u5qe+xjpLYwLNNC4TyAk2VZPx9T6W0zL9MSozvPyCzTUU7DHOgcnOVvq1AUV0LyC5GnaiYuXi7S8K1eSv3dpS63XaMsqHf8wt6u92AE-a7ridcyb4ErGHuaFo0Ey7ygACS0iFqCWwJHqKDuly1mrCMKwpKA6q31B5k78qAByyrqZcMBdGNkNLC5swC4StmMT+e83qH2VKfc+KxL7XzfmpHSD80BPwQC-FBZk0FQJQD-eYf8AE22OsxM6HAADskQ3AoDcLESMwQ4DcQAGzwEnIYfuMBiigKTkNN6bROih3DjMSOyFsxH0ISgIBSY1axwaPHCR39lQwljnwlWTkSoYn7piOAHD+7ZyxvlLuw8GiF2JqVcq5MqKUyHgeB8MUGbt2ZkleOaVh7Tw0TzLc8hTDGNxvnMcJp1w6MkcqdYR9T52KilXBqG4j650XvImAWiUAGO6r1fWnijL4PgQ0GRpsqhiXAWtYy4xIln3yaYBiTFToBEsCLFyWxrpIESGABp-YIDNIAFIQAlN7Ss-hn4gHVDw0o4lBq1AESyWSvQj4RybugbM2AsENKgHACALkoARLgdIApEN1aKLGCs4AayNlbJ2fMU+qismI1dDAAAVn0tAOjekSnSUSTGvlO4BO7kE8xxcrFl3cfYumsUa7OPrq4sR7Mp6bxnpC4AiTfmmIJkTYuoTdnRMrqPcF4854uIVBUkFGVJnZX7vPDRec-kND8Bi5UmIj6S0CQ4t02Bi4DPmDIZWQ5PzJKee85Ua8ep62-Nk8C+yQHjJKY-UYpDakOy8KclpbSlUKkQCGWAwBsArMIPkQo3D-aTKkh9L6P0-rGGjnczR3A8DSAAEK6NtVAQx3yF4oqCik51vJB4kpxfVDVeAqwICMDy1WkNvzFWdcKzJYr4VTJgHKq1ZtpWW1KfKoAA)

[Editable](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwdFNp43h+P4XgoOgMRxIk+mGYJ9i+FgomCqB9QNNIEb8RG7QRt0PRyaoCnDBRVHoI2DHacx-gouu-jYOKGr8WiMAAOJKho1niTUdkxc5bn2Eq3mXr5hT0YxOkBBwADsbhOCgTgxBGwRwFxABs8AToYcVzDARTIOYNnVJJrQdOlmXTNliHoFmGVzAAckqmkBUxumWCgfYQJsRlIAkYBzQtS0AFIQOKsUVv4ySgGqbUlGJvpdaWzTMjJPSjSgWXwUNimjNgCDAHNUBwBACDQGsd0AJLSFN+VBV473LatYPyogwawMA2CvYQeQFK1iXnRJl0OU5LlucY-mYEAA)

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
