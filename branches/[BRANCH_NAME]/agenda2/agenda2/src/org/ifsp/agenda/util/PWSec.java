package org.ifsp.agenda.util;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

/**
 *
 * Esta classe é usuada para encriptografa e desencriptografar qualquer
 * dado desejado no sistema ela monta um algoritmo e o utiliza com chave
 * para monta uma nova string.
 * 
 * 
 * @author Ton
 */
public class PWSec {

    private static final String ALGO = "AES";
    private static final byte[] keyValue =
            new byte[]{'A', 'g', 'e', 'n', 'd', 'a', '-',
        'W', 'e', 'b', '-', 'y', '2', '0', '1', '2'};

    /**
     *
     * @param Data
     * @return
     * @throws Exception
     */
    public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    /**
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    /**
     *
     * @param args
     * @throws Exception
     * @throws InvalidKeySpecException
     */
    public static void main(String[] args) throws Exception, InvalidKeySpecException {

        String password = "desacocheio";
        String passwordEnc = PWSec.encrypt(password);
        String passwordDec = PWSec.decrypt(passwordEnc);

        System.out.println("Plain Text : " + password);
        System.out.println("Encrypted Text : " + passwordEnc);
        System.out.println("Decrypted Text : " + passwordDec);
    }
}