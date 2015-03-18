package com.cupenya.agent.common.model.v1;

/**
 * The different event types handled by the engine.
 */
public enum EventType {
  /**
   * Called whenever execution for an element, e.g. task or gateway, is begun.
   */
  ELEMENT_BEGIN,
  /**
   * Called whenever execution for an element, e.g. task or gateway, is finished.
   */
  ELEMENT_END,
  /**
   * Used to identify transition steps, e.g. from one task to the next.
   */
  TRANSITION
}
