package com.zy.gcode.cache;

import java.io.*;

/**
 * Created by admin5 on 17/3/27.
 */
public abstract class SerializeUtils {
    public static <T> T de(byte[] bytes, Class<T> tClass) {

        try (ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException t) {
            t.printStackTrace();
            return null;
        }
    }

    public static byte[] en(Object obj) {
        if (!(obj instanceof Serializable)) {
            throw new IllegalArgumentException();
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            outputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
