//http://www.michaelgoldschmidt.eu/memoryfilebackedqueue/index.html
package com.cupenya.agent.common.queue;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class FileBackedQueue {
    private RandomAccessFile m_file;

    public FileBackedQueue(File f) throws IOException {
        if (!f.exists()) {
            f.createNewFile();
        }

        m_file = new RandomAccessFile(f, "rw");
    }

    public int size() {
        int retValue = 0;

        try {
            if (0 < m_file.length()) {
                retValue = 1;
            }
        } catch (IOException e) {
            // TODO: handle / wrap in better exception
            throw new RuntimeException("Could not write to queue: " + e.getMessage(), e);
        }

        return retValue;
    }

    public boolean offer(ByteBuffer buffer) {
        boolean retValue = false;

        try {
            m_file.seek(m_file.length());
            m_file.writeInt(buffer.capacity());
            m_file.write(buffer.array());

            retValue = true;
        } catch (IOException e) {
            // TODO: handle / wrap in better exception
            throw new RuntimeException("Could not write to queue: " + e.getMessage(), e);
        }

        return retValue;
    }

    public ByteBuffer poll() {
        ByteBuffer retBuffer = null;

        try {
            if (0 < m_file.length()) {
                long positionBeforeRead = m_file.getFilePointer();
                m_file.seek(0L);
                int arraySize = m_file.readInt();
                byte array[] = new byte[arraySize];
                m_file.read(array);
                long positionAfterRead = m_file.getFilePointer();

                assert (positionAfterRead <= positionBeforeRead);

                // Compact file
                long fileLength = m_file.length();
                byte remaining[] = new byte[(int) (fileLength - positionAfterRead)];
                m_file.readFully(remaining);
                m_file.setLength(0L);
                m_file.write(remaining);

                retBuffer = ByteBuffer.allocate(array.length);
                retBuffer.put(array);
                retBuffer.flip();
            }
        } catch (IOException e) {
            // TODO: handle / wrap in better exception
            throw new RuntimeException("Could not write to queue: " + e.getMessage(), e);
        }

        return retBuffer;
    }
}
