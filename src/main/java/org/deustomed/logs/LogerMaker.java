package org.deustomed.logs;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogerMaker {

    private static final Logger LOGGER = Logger.getLogger(LogerMaker.class.getName());
    private static String LOG_FILE_PATH = "";
    private static boolean isConfigured = false;

    public static void setLOG_FILE_PATH(String logFilePath) {
        if (logFilePath == null || logFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo de log no puede ser null o vacía.");
        }
        LOG_FILE_PATH = logFilePath;
        configureLogger();
    }


    private static void configureLogger() {
        if (!isConfigured) {
            try {
                FileHandler fileHandler = new FileHandler(LOG_FILE_PATH, true);
                fileHandler.setFormatter(new SimpleFormatter());
                LOGGER.addHandler(fileHandler);
                LOGGER.setUseParentHandlers(false);
                isConfigured = true;
            } catch (SecurityException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Logger getLogger() {
        if (!isConfigured) {
            throw new IllegalStateException("Logger no configurado. Por favor, establezca la ruta del archivo de log antes de obtener el Logger.");
        }
        return LOGGER;
    }
}
