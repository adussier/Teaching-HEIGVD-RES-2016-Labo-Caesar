package ch.heigvd.res.caesar.server;

import ch.heigvd.res.caesar.protocol.ICipher;
import ch.heigvd.res.caesar.protocol.Protocol;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client handler thread who responds to client messages
 * @author Amel Dussier & Anastasia Zharkova
 */
public class ClientHandler implements Runnable {

    private static final Logger LOG = Logger.getLogger(CaesarServer.class.getName());
    private final Socket client;
    private final int clientId;
    private final ICipher cipher;
    private final int KEY;

    // constructor
    public ClientHandler(int clientId, Socket client, int key, ICipher cipher) {
        this.client = client;
        this.clientId = clientId;
        this.KEY = key;
        this.cipher = cipher;
    }

    @Override
    public void run() {
        try {
            // get input & output buffers
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            
            // send shared key to client (in plain text)
            LOG.info(String.format("Client #%1s - Sending shared key to client...", clientId));
            out.write(KEY);
            out.flush();
            
            // message loop
            while(true) {
                // wait on client message
                String cipherText = in.readLine();
                LOG.info(String.format("Client #%1s - Crypted message recieved : %2s", clientId, cipherText));
                
                // decrypt message
                String plainText = cipher.decrypt(cipherText);
                LOG.info(String.format("Client #%1s - Decrypted message : %2s", clientId, plainText));
                
                // detect termination message
                if (plainText.equals(Protocol.TERMINATION_MESSAGE))
                    break;
                
                // transform message to response
                plainText = String.format("You said \"%1s\"... But why?", plainText);
                
                // re-encrypt response
                cipherText = cipher.encrypt(plainText);
                
                // send response to client
                LOG.info(String.format("Client #%1s - Sending response to client...", clientId));
                out.write(cipherText);
                out.newLine();
                out.flush();
            }
            
            // close socket and exit thread
            LOG.info(String.format("Client #%1s - Closing socket...", clientId));
            in.close();
            out.close();
            client.close();
            LOG.info(String.format("Client #%1s - Handler terminating...", clientId));
            
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, String.format("Client #%1s - Failed to connect to client", clientId), ex);
        }
    }
}
