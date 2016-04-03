package ch.heigvd.res.caesar.server;

import ch.heigvd.res.caesar.protocol.CaesarCipher;
import ch.heigvd.res.caesar.protocol.Protocol;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listener thread who waits for clients, and creates new threads for each one
 * @author Amel Dussier & Anastasia Zharkova
 */
public class ClientListener implements Runnable {

    private static final Logger LOG = Logger.getLogger(CaesarServer.class.getName());
    private static int clientId = 1; // used to identify clients in log output
    
    @Override
    public void run() {
        
        // open server socket
        ServerSocket socket;
        try {
            LOG.log(Level.INFO, "Opening server socket (port {0})...", Protocol.SERVER_PORT);
            socket = new ServerSocket(Protocol.SERVER_PORT);
            LOG.info("Socket sucessfully opened.");
            
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Failed to open server socket", ex);
            return;
        }

        // wait on clients
        LOG.info("Waiting for clients...");
        while (true) {
            try {
                // accept new client
                Socket client = socket.accept();
                LOG.info("New client has connected!");
                
                // create a new random shared key
                Random r = new Random();
                int key = r.nextInt(26);
                
                // create client handler
                LOG.info(String.format("Starting a new dedicated client handler for client #%1s with key %2d...", clientId, key));
                new Thread(new ClientHandler(clientId++, client, key, new CaesarCipher(key))).start();
                
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Failed to accept client", ex);
            }
        }
    }
}