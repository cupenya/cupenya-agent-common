package com.cupenya.agent.common.model.v1;

import java.io.Serializable;
import java.util.Map;

public class DefaultEvent implements Serializable {
    /**
     * The ID of the event, should be random and is used to track down problems later that might have occured during
     * sending.
     */
    private String id;
    /**
     * The ID of the event, this is typically the ID of the workflow element or transition.
     */
    private String eventId;
    /**
     * The event type used to classify the event, see the EventType type for more details.
     */
    private EventType eventType;
    /**
     * The instance of the process, typically this is on a request basis.
     */
    private String instanceId;
    /**
     * The ID of the process / workflow.
     */
    private String processId;
    /**
     * In case this is a sub process, the ID of the parent process. <code>null</code> in case the process has no parent.
     */
    private String parentProcessId;
    /**
     * The unix timestamp in milliseconds when the event occurred.
     */
    private Long timestamp;

    public Map<String, String> getProcessContext() {
        return processContext;
    }

    public void setProcessContext(Map<String, String> processContext) {
        this.processContext = processContext;
    }

    private Map<String, String> processContext;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getParentProcessId() {
        return parentProcessId;
    }

    public void setParentProcessId(String parentProcessId) {
        this.parentProcessId = parentProcessId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DefaultEvent{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", eventType=" + eventType +
                ", instanceId='" + instanceId + '\'' +
                ", processId='" + processId + '\'' +
                ", parentProcessId='" + parentProcessId + '\'' +
                ", timestamp=" + timestamp +
                ", processContext=" + processContext +
                '}';
    }
}
