package com.cupenya.agent.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * A Java representation of the configuration file.
 */
public class CommonAgentConfig {

  public static enum QueueType {
    file, memory
  }

  public final String apiKey;
  public final String endpoint;

  public final QueueType type;
  public final String file;
  public final int memorySize;
  public final int maxSize;

  private CommonAgentConfig(Config config) {
    apiKey = config.getString("engine.apiKey");
    endpoint = config.getString("engine.endpoint");
    type = QueueType.valueOf(config.getString("queue.type"));
    file = config.getString("queue.file");
    memorySize = config.getInt("queue.memorySize");
    maxSize = config.getInt("queue.maxSize");
  }

  public static CommonAgentConfig fromConfig(Config config) {
    return new CommonAgentConfig(config);
  }

}
