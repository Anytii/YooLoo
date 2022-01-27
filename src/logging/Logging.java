package logging;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging implements Serializable{

    private Logger LOGGER=null;

    public Logging(String className){
        try {
            if (Files.notExists(Path.of("logs/app.log"))) {
                Files.createDirectory(Path.of("logs"));
                Files.createFile(Path.of("logs/app.log"));
            }
            LOGGER = Logger.getLogger(className);
            FileHandler fh = new FileHandler("logs/app.log");
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            LOGGER.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger(){
        return this.LOGGER;
    }

}
