package com.jrakus.playground.remote;

import com.jrakus.players.game_elements.GameInfo;
import com.jrakus.players.game_elements.Move;
import com.jrakus.players.game_elements.PublicState;
import com.jrakus.players.users.LocalUser;
import com.jrakus.players.users.RemoteUser;
import com.jrakus.playground.displays.TerminalPrinter;

import static com.jrakus.players.users.RemoteUser.Message.MessageKind;

import java.io.*;
import java.net.*;

public class RemoteClient {

    private final Socket socket;

    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final LocalUser localUser;

    public RemoteClient(Socket socket) throws Exception {
        this.socket = socket;

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();

        in = new ObjectInputStream(socket.getInputStream());

        localUser = new LocalUser(new TerminalPrinter());
    }

    static void main(String[] args) throws Exception {

        System.out.println("Connecting to the server...");
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connected successfully");

        RemoteClient remoteClient = new RemoteClient(socket);
        remoteClient.startListening();
    }

    private void startListening() throws Exception {
        RemoteUser.Message serverMessage = (RemoteUser.Message) in.readObject();
        PublicState publicState = serverMessage.publicState();
        MessageKind messageKind = serverMessage.messageKind();

        while (publicState.getGameInfo() == GameInfo.ACTIVE_GAME) {

            switch (messageKind) {
                case ATTACK -> this.handleAttackMove(publicState);
                case DEFEND -> this.handleDefendMove(publicState);
                case DISPLAY -> this.handleDisplay(publicState);
            }

            serverMessage = (RemoteUser.Message) in.readObject();
            publicState = serverMessage.publicState();
            messageKind = serverMessage.messageKind();
        }

        // Last message is type of "DISPLAY" and contains the end state of the game
        this.handleDisplay(publicState);

        in.close();
        out.close();
        socket.close();
    }

    private void handleAttackMove(PublicState publicState) throws IOException {
        Move move = localUser.attack(publicState);
        out.writeObject(move);
        out.flush();
    }

    private void handleDefendMove(PublicState publicState) throws IOException {
        Move move = localUser.defend(publicState);
        out.writeObject(move);
        out.flush();
    }

    private void handleDisplay(PublicState publicState) {
        localUser.displayCurrentState(publicState);
    }
}