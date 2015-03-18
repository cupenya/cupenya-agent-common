package com.cupenya.agent.common.model.v1;

import java.io.Serializable;

public class EventApiKeyWrapper implements Serializable {

  private String apiKey;
  private DefaultEvent event;

  @Override
  public String toString() {
    return "EventApiKeyWrapper{" +
      "apiKey='" + apiKey + '\'' +
      ", event=" + event +
      '}';
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public DefaultEvent getEvent() {
    return event;
  }

  public void setEvent(DefaultEvent event) {
    this.event = event;
  }
}
