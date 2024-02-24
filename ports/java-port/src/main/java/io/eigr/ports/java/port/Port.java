package io.eigr.ports.java.port;

/*
 * Specifies an interface which must be implemented by the concrete port
 * implementations.
 *
 */
public interface Port {
    // Invoked in the worker thread to run main port loop. After the function
    // returns, the program will stop, using the returned value as the exit code.
    int run(Worker worker, Output output, Object[] args) throws Exception;

    record Command(String name, Object[] args, String ref) {
    }
}
