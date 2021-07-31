package ua.com.alevel.util;

import ua.com.alevel.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class PropertiesLoaderUtil {

    public static Properties loadProperties(String password) {

        Properties props = new Properties();

        try (InputStream input = Main.class.getResourceAsStream("/jdbc.properties")) {
            props.load(input);
            props.setProperty("password", password);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }
}
