package yong.common;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor; 

public class EncryptTest { 
    
    public static void main(String[] args) { 
    
    StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor(); 
    pbeEnc.setAlgorithm("PBEWithMD5AndDES"); 
    pbeEnc.setPassword("gEk@jq24!");
//    String url = "ㅁ";
//    String username = ""; 
//    String password = ""; 
//    System.out.println("기존 URL :: " + url + " | 변경 URL :: " + pbeEnc.encrypt(url)); 
//    System.out.println("기존 username :: " + username + " | 변경 username :: " + pbeEnc.encrypt(username)); 
//    System.out.println("기존 password :: " + password + " | 변경 password :: " + pbeEnc.encrypt(password));
    System.out.println(pbeEnc.decrypt("oxpR2Iqmi2ec0HmV2KGKea9PFJ+aWKxZ"));
    System.out.println(pbeEnc.decrypt("GQ1uc6p21pF25nluespjmQ=="));
    
    }
}
