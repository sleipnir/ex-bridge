package io.eigr.ports.java.port;

import io.cloudevents.v1.proto.CloudEvent;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PortImpl implements Port, AutoCloseable {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final FileInputStream fileInputStream;
    private final Output output;

    private PortImpl(FileInputStream fileInputStream, Output output) {
        this.fileInputStream = fileInputStream;
        this.output = output;
    }

    public static PortImpl start() {
        try {
            FileInputStream fileInputStream = new FileInputStream("/dev/fd/3");
            // DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            Output output = Output.start();
            return new PortImpl(fileInputStream, output);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
    }

    @Override
    public CloudEvent sendSyncMessage(CloudEvent message) {
        try {
            output.emit(message);
            return receiveMessage();
        } catch (IOException e) {
            handleException(e);
            return CloudEvent.getDefaultInstance();
        }
    }

    @Override
    public void sendAsyncMessage(CloudEvent message) {
        executor.submit(() -> {
            output.emit(message);
        });
    }

    public CloudEvent receiveMessage() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        int messageLength = dataInputStream.readInt();
        byte[] messageBytes = new byte[messageLength];
        dataInputStream.readFully(messageBytes);

        var event = CloudEvent.parseFrom(messageBytes);
        System.out.println(String.format("Received event from Elixir side %s", event));
        return event;
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            fileInputStream.close();
        } catch (IOException e) {
            handleException(e);
        }
    }

    private static void handleException(Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}
