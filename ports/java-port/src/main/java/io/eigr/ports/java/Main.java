package io.eigr.ports.java;

import io.eigr.ports.java.port.Driver;
import io.eigr.ports.java.port.Port;
import io.eigr.ports.java.port.Output;
import io.eigr.ports.java.port.Worker;

public class Main implements Port {

    private Output output;

    public static void main(String[] args) {
        Driver.run(args, new Main());
    }

    @Override
    public int run(Worker worker, Output output) throws Exception {
        this.output = output;

        synchronized (this) {
            while (true) {
                wait(); // Wait for external events or messages
                for (var command : worker.drainCommands()) {
                    command.name();
                    command.args();
                    command.ref();

                    output.emit(null);
                }
            }
        }
    }

    public synchronized void notifyExternalEvent() {
        notify(); // Notify the waiting thread about the external event
    }
}
