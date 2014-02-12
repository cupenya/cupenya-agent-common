package com.cupenya.agent.common.sender;

import com.cupenya.agent.common.CommonConfig;
import com.cupenya.agent.common.queue.MemoryFileBackedQueue;
import com.cupenya.agent.common.model.v1.DefaultEvent;
import com.cupenya.agent.common.model.v1.EventApiKeyWrapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class EventSender {
    private static final Long CHECK_INTERVAL = 500L;

    private final Logger log = LoggerFactory.getLogger(EventSender.class);

    private final Queue<DefaultEvent> queue;

    private final Thread sender;

    public EventSender() throws IOException {
        queue = createQueue();
        // TODO: make non blocking, use threadpool
        sender = new Thread(new Runnable() {
            private int errors = 0;

            @Override
            public void run() {
                // TODO: add listener to shutdown thread
                // TODO: add retry handling on errors that increases time between errors
                try {
                    while (true) {
                        DefaultEvent event = queue.poll();
                        if (event != null) {
                            try {
                                sendEvent(event);
                            } catch (IOException ex) {
                                log.error("Re-queuing event '" + event.getId() + "', because send failed with: " + ex.getMessage());
                                queueEvent(event);
                                Thread.sleep(5 * 1000);
                                errors++;
                                if (errors > 10) {
                                    return;
                                }
                            }
                        } else {
                            Thread.sleep(CHECK_INTERVAL);
                        }
                    }
                } catch (Exception ex) {
                    log.error("Unhandled exception without recovery occurred: " + ex.getMessage(), ex);
                }
            }
        });
        sender.start();
    }

    private Queue<DefaultEvent> createQueue() throws IOException {
        if (CommonConfig.Queue.type == CommonConfig.Queue.QueueType.file) {
            log.info("Init with file backed queue");
            return new MemoryFileBackedQueue<DefaultEvent>(new File(CommonConfig.Queue.file),
                    CommonConfig.Queue.memorySize);
        } else {
            log.info("Init with memory backed queue");
            return new LinkedList<DefaultEvent>();
        }
    }

    public void queueEvent(DefaultEvent event) throws IOException {
        if (queue.size() < CommonConfig.Queue.maxSize) {
            queue.offer(event);
        } else {
            log.warn(
                    "Dropping event " + event.getId() + " because queue is at max size of " + CommonConfig.Queue
                            .maxSize);
            log.debug("Dropped event:\n" + getEventJson(event));
        }
    }

    private void sendEvent(DefaultEvent event) throws IOException {
        EventApiKeyWrapper wrapper = new EventApiKeyWrapper();
        wrapper.setApiKey(CommonConfig.Engine.apiKey);
        wrapper.setEvent(event);
        String json = getEventJson(wrapper);
        sendEvent0(json, "/events");
    }

    private void sendEvent0(String eventContentJson, String apiPath)
            throws IOException {
        // TODO: re-use HTTP client and connection, use pooling
        if (log.isDebugEnabled()) {
            log.debug("Preparing to send event " + eventContentJson);
        }

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(CommonConfig.Engine.endpoint + apiPath);

        ByteArrayEntity jsonContent = new ByteArrayEntity(eventContentJson.getBytes());
        jsonContent.setContentType("application/json");
        post.setEntity(jsonContent);
        HttpResponse response = httpclient.execute(post);

        if (log.isDebugEnabled()) {
            log.debug("Event:\n" + eventContentJson);
            log.debug("Send event to " + post.getURI() + " with result: " + response.getStatusLine());
        }

        if (response.getStatusLine().getStatusCode() != 200) {
            String error = "Could not send event, received unknown status code: " + response.getStatusLine().getStatusCode();
            log.warn(error);
            log.warn("Event:\n" + eventContentJson);
            log.warn("Send event to " + post.getURI() + " with result: " + response.getStatusLine());
            throw new IOException(error);
        }
    }

    private String getEventJson(Object event) throws IOException {
        return new ObjectMapper().writeValueAsString(event);
    }
}
