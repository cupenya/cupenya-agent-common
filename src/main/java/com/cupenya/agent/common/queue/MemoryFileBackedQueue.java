//http://www.michaelgoldschmidt.eu/memoryfilebackedqueue/index.html
package com.cupenya.agent.common.queue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class MemoryFileBackedQueue<E extends Serializable> extends AbstractQueue<E> {

  private static final int DEFAULT_THRESHOLD = 10;
  private Queue<ByteBuffer> m_memoryQueue;
  private FileBackedQueue m_fileQueue;
  private int m_threshold;

  public MemoryFileBackedQueue(File queueFile, int threshold) throws IOException {
    m_threshold = threshold;
    m_memoryQueue = new LinkedList<ByteBuffer>();
    m_fileQueue = new FileBackedQueue(queueFile);
  }

  public boolean offer(E e) {
    try {
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bout);
      out.writeObject(e);
      out.close();
      ByteBuffer buffer = ByteBuffer.wrap(bout.toByteArray());

      boolean retValue;

      if (m_threshold > m_memoryQueue.size()) {
        retValue = m_memoryQueue.offer(buffer);
      } else {
        retValue = m_fileQueue.offer(buffer);
      }

      return retValue;
    } catch (IOException ex) {
      throw new IllegalArgumentException("Could not serialize: " + ex.getMessage(), ex);
    }
  }

  public E poll() {
    try {
      ByteBuffer buffer;

      if (m_fileQueue.size() > 0) {
        buffer = m_fileQueue.poll();
      } else {
        buffer = m_memoryQueue.poll();
      }

      if (buffer != null) {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
        return (E) in.readObject();
      } else {
        return null;
      }
    } catch (Exception ex) {
      throw new IllegalArgumentException("Could not deserialize: " + ex.getMessage(), ex);
    }
  }

  public E peek() {
    return null;
  }

  public int size() {
    return m_fileQueue.size() + m_memoryQueue.size();
  }

  public Iterator<E> iterator() {
    return null;
  }
}
