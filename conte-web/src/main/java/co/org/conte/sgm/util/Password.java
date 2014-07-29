package co.org.conte.sgm.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 *
 * @author jam
 */
public class Password {
    public static String NUMEROS = "0123456789";    
    public static String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";    
    public static String ESPECIALES = "#*$%/_-";
    
    public static String getPinNumber() {
        return getPassword(NUMEROS, 4);
    }
    
    public static String getPassword() {
        return getPassword(8);
    }
    
    public static String getPassword(int length) {
        return getPassword(NUMEROS + MAYUSCULAS + MINUSCULAS, length);
    }

    private static String getPassword(String key, int length) {
        String pswd = "";
        
        for (int i = 0; i < length; i++) {
            pswd+=(key.charAt((int)(Math.random() * key.length())));
        }        
        return pswd;	
    }
    
    public static String generatePassword(){
        return getPassword(Password.MINUSCULAS+
                Password.MAYUSCULAS+
                Password.ESPECIALES,8);
    }
    
    public static String encryptPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] codigo = md.digest();
            StringBuilder retValue = new StringBuilder(codigo.length * 2);
            String strItem;
            for (byte item : codigo) {
                strItem = Integer.toHexString(item < 0 ? (256 + item) : item);
                retValue.append(strItem.length() < 2 ? "0" + strItem : strItem);
            }
            return retValue.toString();                
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("No se encontro el algoritmo MD5", ex);
        }
    }
    
    public static MyPasswordValidator.ValidationResult validate(String password, List<String> history){
        return MyPasswordValidator.getInstance().validate(password, history);
    }
}