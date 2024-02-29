package io.eigr.ports.java.port;

import io.cloudevents.v1.proto.CloudEvent;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public final class Driver {

    private Driver() {
    }

    public static void run(String[] args, Port port) {

        try (FileInputStream input = new FileInputStream("/dev/fd/3")) {
            var output = Output.start();
            var worker = Worker.start(port, output);

            while (true) {
                try {
                    // Read bytes from the input stream and parse protobuf message
                    CloudEvent message = readNextMessage(input);
                    if (message != null) {
                        var command = takeCommand(message);
                        worker.sendCommand(command);
                    }
                } catch (EOFException e) {
                    // Handle end of input stream (optional, based on your requirements)
                    handleException(e);
                    break;
                }
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    private static CloudEvent readNextMessage(FileInputStream input) throws IOException {
        try {
            // Read the length of the message
            DataInputStream dataInputStream = new DataInputStream(input);
            int messageLength = dataInputStream.readInt();

            // Read the message bytes
            byte[] messageBytes = new byte[messageLength];
            dataInputStream.readFully(messageBytes);

            // Parse the protobuf message
            return CloudEvent.parseFrom(messageBytes);
        } catch (NegativeArraySizeException e) {

        }
        return null;
    }

    private static Port.Command takeCommand(CloudEvent message) {
        return new Port.Command(null, null, null);
    }

    private static void handleException(Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}
