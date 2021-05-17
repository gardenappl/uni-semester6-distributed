package ua.yuriih.lab2task5.common;

import java.io.*;
import java.util.Base64;

public final class Utils {
    public static byte[] toBytes(final Serializable object) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            ObjectOutputStream out = new ObjectOutputStream(byteStream);
            out.writeObject(object);

            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object fromBytes(final byte[] data) throws ClassNotFoundException {
        try (final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return in.readObject();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
