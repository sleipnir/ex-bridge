package io.eigr.ports.java.port;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * The worker thread of a port program. See {@link Driver} for details.
 */
public final class Worker implements Runnable {
    static Worker start(Port port, Output output) {
        var worker = new Worker(port, output);

        // Using a daemon thread to ensure program termination if the main thread stops.
        var consumerThread = new Thread(worker);
        consumerThread.setDaemon(true);
        consumerThread.start();

        return worker;
    }

    private final BlockingQueue<Port.Command> commands = new LinkedBlockingQueue<>();
    private final Port port;
    private final Output output;

    private Worker(Port port, Output output) {
        this.port = port;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            var exitStatus = this.port.run(this, this.output);
            System.exit(exitStatus);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    void sendCommand(Port.Command command) {
        this.commands.add(command);
    }

    public Collection<Port.Command> drainCommands() {
        var result = new ArrayList<Port.Command>();
        this.commands.drainTo(result);
        return result;
    }

    public Port.Command take() throws InterruptedException {
        return this.commands.take();
    }
}
