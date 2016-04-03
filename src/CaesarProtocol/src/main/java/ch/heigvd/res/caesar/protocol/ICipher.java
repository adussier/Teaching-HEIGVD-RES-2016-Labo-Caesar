package ch.heigvd.res.caesar.protocol;

/**
 * Interface for the cryptographic class used by the server and the clients
 * @author Amel Dussier & Anastasia Zharkova
 */
public interface ICipher {
    
    // encrypts a plain text to a ciphered one
    String encrypt(String plainText);
    
    // decrypts a ciphered text to a plain one
    String decrypt(String cipherText);
}
