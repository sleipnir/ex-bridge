package io.eigr.ports.java.port;

import io.cloudevents.v1.proto.CloudEvent;

import io.eigr.ports.java.port.Port;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public final class Driver {

    private static AtomicReference<Port> portRef = new AtomicReference<>();

    public static void run(String[] args) {
        try (var port = PortImpl.start()) {
            var output = Output.start();
            portRef.set(port);
            while (true) {
                try {
                    // Read bytes from the input stream and parse protobuf message
                    CloudEvent message = port.receiveMessage();

                    // sendCommand(message, port, output);
                } catch (EOFException e) {
                    handleException(e);
                }
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    public static CloudEvent sendCommand(CloudEvent message) {
        return portRef.get().sendSyncMessage(message);
    }

    public static void sendAsyncCommand(CloudEvent message) {
        portRef.get().sendAsyncMessage(message);
    }

    private static void handleException(Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}