package compgc01;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A class that encrypts selected resources of our cinema booking management application, such as usernames and passwords.
 * Adapted from Johaness Brodwall's example in https://stackoverflow.com/questions/1132567/encrypt-password-in-configuration-files.
 *
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 12.12.2017
 */
public class Encryption {

    static byte[] salt = new String("12345678").getBytes();
    static int iterationCount = 40000;
    static int keyLength = 128;
    static SecretKeySpec key;
    
    static void setKey() throws NoSuchAlgorithmException, InvalidKeySpecException {

        key = createSecretKey(("<3UCLCS").toCharArray());
    }

    static SecretKeySpec createSecretKey(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    static String decrypt(String string) throws GeneralSecurityException, IOException {

        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private static String base64Encode(byte[] bytes) {
        
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    private static byte[] base64Decode(String property) throws IOException {
        
        return Base64.getDecoder().decode(property);
    }
}