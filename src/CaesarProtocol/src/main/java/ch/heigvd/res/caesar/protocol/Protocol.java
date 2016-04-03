package ch.heigvd.res.caesar.protocol;

/**
 * Defines the shared protocol parameters between the server and the clients
 * @author Amel Dussier & Anastasia Zharkova
 */
public class Protocol {
    
    // server name or IP address
    public static final String SERVER_ADDRESS = "localhost";
    
    // server port
    public static final int SERVER_PORT = 12345;
    
    // message used by clients to end a communication session
    public static final String TERMINATION_MESSAGE = "/quit";
}