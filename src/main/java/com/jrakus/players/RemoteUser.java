package com.jrakus.players;

import com.jrakus.game_elements.Move;
import com.jrakus.game_elements.Player;
import com.jrakus.game_elements.PublicState;

import java.io.*;
import java.net.*;
import java.util.List;

public class RemoteUser implements Player {

    static ServerSocket serverSocket;

    ObjectInputStream in;
    ObjectOutputStream out;

    private RemoteUser(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public static List<RemoteUser> createTwoRemoteUsers(int port) {
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Server waiting for clients...");

            Socket client1 = serverSocket.accept();
            System.out.println("Client 1 connected");

            Socket client2 = serverSocket.accept();
            System.out.println("Client 2 connected");

            ObjectInputStream in1 = new ObjectInputStream(client1.getInputStream());
            ObjectOutputStream out1 = new ObjectOutputStream(client1.getOutputStream());

            ObjectInputStream in2 = new ObjectInputStream(client2.getInputStream());
            ObjectOutputStream out2 = new ObjectOutputStream(client2.getOutputStream());

            RemoteUser remoteUser1 = new RemoteUser(in1, out1);
            RemoteUser remoteUser2 = new RemoteUser(in2, out2);

            return List.of(remoteUser1, remoteUser2);

        } catch (IOException e) {
            throw new RuntimeException("Error occurred while connecting to server", e);
        }
    }

    @Override
    public Move defend(PublicState publicState) {

        try {
            Message message = new Message(publicState, Message.MessageKind.DEFEND);
            out.writeObject(message);
            out.flush();

            return (Move) in.readObject();

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while communicating with client", e);
        }
    }

    @Override
    public Move attack(PublicState publicState) {
        try {
            Message message = new Message(publicState, Message.MessageKind.ATTACK);
            out.writeObject(message);
            out.flush();

            return (Move) in.readObject();

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while communicating with client", e);
        }
    }

    @Override
    public void displayCurrentState(PublicState publicState) {
        try {
            Message message = new Message(publicState, Message.MessageKind.DISPLAY);
            out.writeObject(message);
            out.flush();

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while communicating with client", e);
        }
    }

    public static void closeSocketConnection() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while closing serverSocket", e);
        }
    }

    public record Message(PublicState publicState, MessageKind messageKind) implements Serializable {

        public enum MessageKind {
                ATTACK, DEFEND, DISPLAY
            }
        }
}