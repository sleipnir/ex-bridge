package io.eigr.ports.java.port;

import io.cloudevents.v1.proto.CloudEvent;

/*
 * Specifies an interface which must be implemented by the concrete port
 * implementations.
 *
 */
public interface Port {
    CloudEvent sendSyncMessage(CloudEvent message);

    void sendAsyncMessage(CloudEvent message);
}
