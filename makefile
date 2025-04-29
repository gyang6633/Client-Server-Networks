JAVAC=javac
JAVA=java
SRC=.
BIN=bin

# Classes
CLIENT_CLASS=$(BIN)/Client.class
SERVER_CLASS=$(BIN)/Server.class

# Default target
all: build run-server

# Compile both Client and Server
build: $(CLIENT_CLASS) $(SERVER_CLASS)

$(CLIENT_CLASS): $(SRC)/Client.java
	@mkdir -p $(BIN)
	$(JAVAC) -d $(BIN) $(SRC)/Client.java

$(SERVER_CLASS): $(SRC)/Server.java
	@mkdir -p $(BIN)
	$(JAVAC) -d $(BIN) $(SRC)/Server.java

run-client: $(CLIENT_CLASS)
	@echo "Running client..."
	$(JAVA) -cp $(BIN) Client

run-server: $(SERVER_CLASS)
	@echo "Server is being started"
	@echo "Please open a new terminal and run the client using the command: make run-client"
	@echo "----------------------------------------------"
	@$(JAVA) -cp $(BIN) Server

clean:
	rm -rf $(BIN)
