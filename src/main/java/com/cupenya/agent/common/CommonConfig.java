package com.cupenya.agent.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * A Java representation of the configuration file.
 */
public final class CommonConfig {
    private static final Config config = ConfigFactory.load();
    private static final Config agentConfig = config.getConfig("com.cupenya.agent");

    public static final class Engine {
        private static final Config config = agentConfig.getConfig("engine");
        public static final String apiKey = config.getString("apiKey");
        public static final String endpoint = config.getString("endpoint");
    }

    public static final class Queue {
        public static enum QueueType {
            file, memory
        }
        private static final Config config = agentConfig.getConfig("queue");
        public static final QueueType type = QueueType.valueOf(config.getString("type"));
        public static final String file = config.getString("file");
        public static final int memorySize = config.getInt("memorySize");
        public static final int maxSize = config.getInt("maxSize");
    }

}
