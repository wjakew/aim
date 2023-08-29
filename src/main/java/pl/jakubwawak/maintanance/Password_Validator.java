/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package pl.jakubwawak.maintanance;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

/**
 *Object for creating password
 * @author x
 */
public class Password_Validator {

    private String given_string;
    private MessageDigest hash;

    ArrayList<String> pv_log;
    /**
     * Constructor
     * @param password
     */
    public Password_Validator(String password) throws NoSuchAlgorithmException{
        given_string = password;
        hash = MessageDigest.getInstance("MD5");
        pv_log = new ArrayList<>();
        pv_log.add("Created object with given ("+password+")");
    }

    /**
     * Constructor with random string generator
     */
    public Password_Validator() throws NoSuchAlgorithmException {
        given_string = random_string_generator(10);
        hash = MessageDigest.getInstance("MD5");
        pv_log = new ArrayList<>();
    }

    /**
     * Function for generating random string
     */
    String random_string_generator(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public String getGiven_string(){
        return given_string;
    }
    /**
     * Function for hashing password
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public String hash() throws NoSuchAlgorithmException{
        pv_log.add("Trying to hash data");
        try{
            byte[] messageD = hash.digest(given_string.getBytes());

            BigInteger number = new BigInteger(1,messageD);

            String hashtext = number.toString(16);

            while ( hashtext.length() < 32 ){
                hashtext = "0" + hashtext;
            }
            pv_log.add("Hash method exited successfull");
            return hashtext;

        }catch(Exception e){
            pv_log.add("Hash method failed ("+e.toString()+")");
            System.out.println(hash);
            return null;
        }
    }

    /**
     * Function for comparing data
     * @param data_to_compare
     * @return Integer
     * @throws NoSuchAlgorithmException
     */
    public int compare(String data_to_compare) throws NoSuchAlgorithmException{
        String hash_data = hash();
        if ( hash_data != null){

            if ( hash_data.equals(data_to_compare)){
                return 1;
            }
            else{
                return 0;
            }
        }
        else{
            return -1;
        }
    }

}