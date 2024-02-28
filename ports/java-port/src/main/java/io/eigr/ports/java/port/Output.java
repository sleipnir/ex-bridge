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
    static Output start() {
        var output = new Output();

        // Using a daemon thread to ensure program termination if the main thread stops.
        var consumerThread = new Thread(output);
        consumerThread.setDaemon(true);
        consumerThread.start();

        return output;
    }

    private final BlockingQueue<CloudEvent> messages;

    private Output() {
        this.messages = new LinkedBlockingQueue<>();
    }

    public void emitCallResponse(Port.Command command, CloudEvent response) throws InterruptedException {
        this.emit(response);
    }

    public void emit(CloudEvent message) throws InterruptedException {
        this.emit(message, false);
    }

    public void emit(CloudEvent message, boolean emitMetrics) throws InterruptedException {
        Long now = null;
        if (emitMetrics) {
            now = System.nanoTime();
        }

        this.messages.put(message);
    }

    @Override
    public void run() {
        // Writing to the file descriptor 4, which is allocated by Elixir for output
        try (var output = new DataOutputStream(new FileOutputStream("/dev/fd/4"))) {
            while (true) {
                var message = this.messages.take();

                // writing to the port is to some extent a blocking operation, so we measure it
                var sendingAt = System.nanoTime();
                this.notify(output, message);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void notify(DataOutputStream output, CloudEvent event) throws IOException {
        output.write(event.toByteArray());
    }
}