package ch.heigvd.res.caesar.client;

import ch.heigvd.res.caesar.protocol.CaesarCipher;
import ch.heigvd.res.caesar.protocol.ICipher;
import ch.heigvd.res.caesar.protocol.Protocol;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The client
 * @author Amel Dussier & Anastasia Zharkova
 */
public class CaesarClient {

    private static final Logger LOG = Logger.getLogger(CaesarClient.class.getName());

    public static void main(String[] args) {
        
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tH:%1$tM:%1$tS::%1$tL] Client > %5$s%n");
        
        LOG.info("Caesar client starting...");
        
        try {
            // connect to server
            LOG.info(String.format("Connecting server %1s on port %2s...", Protocol.SERVER_ADDRESS, Protocol.SERVER_PORT));
            Socket server = new Socket(Protocol.SERVER_ADDRESS, Protocol.SERVER_PORT);
            LOG.info("Connected to server.");
            
            // get input & output buffers
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
        
            // get shared key from server
            LOG.info("Waiting for shared key...");
            int key = in.read();
            LOG.log(Level.INFO, "Key recieved : {0}", key);
            
            // create new Caesar cipher with key
            ICipher cipher = new CaesarCipher(key);
            
            // message loop
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // get message from user
                System.out.print("Enter message : ");
                String message = scanner.nextLine();
                LOG.log(Level.INFO, "Message entered by user : {0}", message);
                
                // encrypt message
                String cipherText = cipher.encrypt(message);
                
                // sen encrypted message to serevr
                LOG.log(Level.INFO, "Sending encrypted message to server : {0}", cipherText);
                out.write(cipherText);
                out.newLine();
                out.flush();
                
                // detect termination message
                if (message.equals(Protocol.TERMINATION_MESSAGE)) {
                    scanner.close();
                    break;
                }
                
                // decrypt and display server response
                String cipheredResponse = in.readLine();
                String plainResponse = cipher.decrypt(cipheredResponse);
                LOG.log(Level.INFO, "Server response : {0}", plainResponse);
            }
            
            // close socket and exit
            LOG.info("Closing socket...");
            in.close();
            out.close();
            server.close();
            LOG.info("Caesar client terminating...");
            
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Server host not found", ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Failed to connect to server", ex);
        }
    }
}
