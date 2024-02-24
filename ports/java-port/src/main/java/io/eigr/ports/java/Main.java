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
    public int run(Worker worker, Output output, Object[] args) throws Exception {
        this.output = output;

        return 0;
    }
}
