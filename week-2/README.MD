# Instructions:
In this assignment, your group will modify the Java Echo Server program in the previous assignment (Java Echo ServerLinks to an external site.) to explore and emulate the workings of the modern Internet. You will work collaboratively to achieve the objectives outlined below and document your progress through code, and video demonstrations.

Download the provided Echo Client and Server Java code (Java Echo ServerLinks to an external site.). Modify the server to run on a specified port and convert input messages to uppercase. For example, if the client sends "hello," the server should respond with "HELLO." Use any suitable Java methods to achieve this functionality.

# Modifications:

Compared to Week 1, this assignment is similar, with one key difference: the server always responds in upper case.

Additionally, the server now support Universal Plug and Play, to allow the code to be run on ordinary consumer end-devices following the UPnP protocol. This is done so that the Java application can communicate with the router to automatically open ports.

UPnP is implemented through [weupn](https://github.com/bitletorg/weupnp). The source code is provided for convenience.

# Commands:

Compiling:
```bash
javac -d ./out $(find . -name "*.java")
```

Creating a .jar file:
```bash
cd out # navigate to build directory
jar cvf week-2.jar *
mv week-2.jar ../week-2.jar
cd ../
```

Running the server:

```bash
java -cp "week-2.jar" VerboseEchoServer <port>
```

Running the client:
```bash
java -cp "week-2.jar" VerboseEchoClient <address> <port>
```