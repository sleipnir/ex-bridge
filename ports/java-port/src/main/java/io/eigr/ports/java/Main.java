package io.eigr.ports.java;

import io.eigr.ports.java.port.Driver;
import io.eigr.ports.java.port.Output;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import io.cloudevents.v1.proto.CloudEvent;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> Driver.run(args));
        executor.submit(() -> {
            // Simulate Elixir sending messages asynchronously

            IntStream.range(0, 20000)
                    // .parallel()
                    .forEach(index -> {
                        Driver.sendAsyncCommand(generateAsyncMessage(index));
                    });
            // for (int i = 0; i < 5; i++) {
            // Driver.sendAsyncCommand(generateAsyncMessage(i));
            // try {
            // Thread.sleep(1000);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
            // }
        });

        executor.shutdown();

        Thread.sleep(Long.MAX_VALUE);
    }

    private static CloudEvent generateAsyncMessage(int index) {
        CloudEvent event = CloudEvent.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSpecVersion("1.0")
                .setType(Integer.toString(index))
                .build();
        return event;
    }
}