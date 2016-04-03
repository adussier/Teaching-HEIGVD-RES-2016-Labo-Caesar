package ch.heigvd.res.caesar.protocol;

/**
 * Cryptographic class for the Caesar cipher
 * @author Amel Dussier & Anastasia Zharkova
 */
public class CaesarCipher implements ICipher {
    
    // the Caesar cipher works only on the alphabet (upper & lower case)
    private static final String LOWER_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    // the key used for the offset
    private final int KEY;
    
    // constructor
    public CaesarCipher(int key) {
        KEY = key;
    }

    // applies an offset to a character (if a letter)
    private char cipherChar(char c, int offset) {
        
        // get the right alphabet and position
        String alphabet = LOWER_ALPHABET;
        int position = alphabet.indexOf(c);
        if (position == -1) {
            alphabet = UPPER_ALPHABET;
            position = alphabet.indexOf(c);
            if (position == -1) {
                // not a letter, don't cipher
                return c;
            }
        }

        // apply offset
        int newPosition = position + offset;
        if (newPosition < 0) newPosition += 26;
        else if (newPosition >= 26) newPosition %= 26;
        
        // return ciphered char
        return alphabet.charAt(newPosition);
    }
    
    @Override
    public String encrypt(String plainText) {
        String cipherText = "";
        for (char c : plainText.toCharArray())
            cipherText += cipherChar(c, KEY); // add offset
        return cipherText;
    }

    @Override
    public String decrypt(String cipherText) {
        String plainText = "";
        for (char c : cipherText.toCharArray())
            plainText += cipherChar(c, -KEY); // substract offset
        return plainText;
    }
}