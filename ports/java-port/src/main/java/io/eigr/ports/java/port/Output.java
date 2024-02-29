package io.eigr.ports.java.port;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.cloudevents.v1.proto.CloudEvent;

/*
 * The output thread of a port program. This thread is responsible for sending
 * messages to Elixir. Messages are sent as Erlang/Elixir terms.
 *
 * See {@link Driver} for details on port architecture. See {@link Erlang} for
 * details on data encoding.
 */
public final class Output implements Runnable {
    private final DataOutputStream output;
    private final BlockingQueue<CloudEvent> messages;

    private Output(DataOutputStream output) {
        this.output = output;
        this.messages = new LinkedBlockingQueue<>();
    }

    static Output start() {
        try {
            var outputStream = new DataOutputStream(new FileOutputStream("/dev/fd/4"));
            var output = new Output(outputStream);

            // Using a daemon thread to ensure program termination if the main thread stops.
            var consumerThread = new Thread(output);
            consumerThread.setDaemon(true);
            consumerThread.start();

            return output;
        } catch (IOException e) {
            handleException(e);
            return null;
        }
    }

    public void emit(CloudEvent message) {
        try {
            byte[] messageBytes = message.toByteArray();
            output.write(messageBytes);
        } catch (IOException e) {
            handleException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                var message = messages.take();
                emit(message);
            } catch (InterruptedException e) {
                handleException(e);
            }
        }
    }

    public void enqueueMessage(CloudEvent message) {
        messages.offer(message);
    }

    private static void handleException(Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}