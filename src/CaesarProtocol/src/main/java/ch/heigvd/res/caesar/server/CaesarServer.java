package ch.heigvd.res.caesar.server;

import java.util.logging.Logger;

/**
 * The server
 * @author Amel Dussier & Anastasia Zharkova
 */
public class CaesarServer {

    private static final Logger LOG = Logger.getLogger(CaesarServer.class.getName());

    public static void main(String[] args) {
        
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tH:%1$tM:%1$tS::%1$tL] Server > %5$s%n");
        
        LOG.info("Caesar server starting...");
        
        // create a new listening thread
        LOG.info("Starting listener thread...");
        new Thread(new ClientListener()).start();
    }
}