package com.blockcipher.Bits;

/**
 *
 * @author William Sentosa
 */

import com.blockcipher.Bits.Bit;

import com.blockcipher.encrypt.VigenereCipher;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;


public class Key {
    private Bit bits[];
    private Block keyBlock[];
    private int size;
    private final int defaultSize = 128;
    private String keyText;
    private String encryptedKey;
    VigenereCipher VC;
    ByteConverter BC;
    String vigkeytemp;
    
    public Key() {
        size = defaultSize;
        bits = new Bit[size];
        keyBlock = new Block[8];
        for(int i=0; i<size; i++) {
            bits[i] = new Bit();
        }
        VC = new VigenereCipher();
        BC = new ByteConverter();
    }
    
    /**
     * Consructor
     * @param key string key yang ingin diubah menjadi array of bit
     * @param size panjang key yang seharusnya, dalam bit = 128 bit
     */
    public Key(String key, int size) throws UnsupportedEncodingException {
        this.size = size; 
        setKey(key, size);
    }
    
    public int getSize() {
        return size;
    }
    
    public Bit[] getBits() {
        return bits;
    }
    
    public Block[] getKeyBlock() {
        return keyBlock;
    }
    
    public String getKeyText() {
        return keyText;
    }
    
    public String getEncryptedText() {
        return encryptedKey;
    }
    
    private void setKeyText(String s){
        String sTemp = s;
        while(true){
            if (s.length()<16){
                s += sTemp;
            }
            else{
                break;
            }
        }
        keyText = s.substring(0, 16);
    }
    
    public void setEncryptedKey() throws UnsupportedEncodingException{
        encryptedKey = VC.Encrypt(keyText, generateKeyForVigenere(), 26);
    }
    
    @Override
    public String toString() {
        String result = "";
        for(int i=0; i<bits.length; i++) {
            result = result + bits[i];
        }
        return result;
    }
    
    /**
     * mengubah string menjadi bits
     * @param key String key yang ingin diubah menjadi array of bit
     * @param s panjang key yang seharusnya, dalam bit = 128 bit
     */
    public void setKey(String key, int s) throws UnsupportedEncodingException {
        size = s;
        this.setKeyText(key);
        this.setEncryptedKey();
        
        byte[] b = encryptedKey.getBytes();
//        System.out.println("keyLength" + keyText.length());
//        System.out.println("encryptedlength "+encryptedKey.length() + " " + encryptedKey);
//        System.out.println("b length" + b.length);
        Bit[] tempBits = concat (BC.convertByteToBits(b[0]),BC.convertByteToBits(b[1]));
        Block tempBlock = new Block();
        tempBlock.setBits(tempBits);
        keyBlock[0] = tempBlock;
        
        int count = 1;
        tempBits = null;
        tempBlock = new Block();
        for (int i = 2;i<b.length;i++){
//            System.out.println("count"+ count);
            if ((i - 1) % 2 != 0){
                tempBits = BC.convertByteToBits(b[i]);
            }
            if ((i - 1) % 2 == 0){
                tempBits = concat(tempBits,BC.convertByteToBits(b[i]));
                tempBlock.setBits(tempBits);
                keyBlock[count] = tempBlock;
                tempBits = null;
                tempBlock = new Block();
                count++;
            } 
        }
        int idx = 0;
        for(int i=0; i<keyBlock.length; i++) {
            for(int j=0; j<keyBlock[i].getBits().length; j++) {
                this.bits[idx] = keyBlock[i].getBits()[j];
                idx++;
            }
        }
//        System.out.println(tempBits.length);
    }
    
    /**
     * Menghasilkan kunci untuk dienkripsi dengan vigenere
     * @return kunci untuk enkripsi
     */
    private String generateKeyForVigenere() throws UnsupportedEncodingException {
        // Buat key untuk chipper dengan menggunakan vigenere chipper
        byte[] keyByte = keyText.getBytes();
        Bit[] tempBit = BC.convertByteToBits(keyByte[0]);
        for (int i = 1;i<keyByte.length;i++){
            tempBit = concat(tempBit,BC.convertByteToBits(keyByte[i]));
        }
        Bit[] keyVigBit = new Bit[32];
        int count = 0;
        for (int i = 0;i<tempBit.length;i++){
            if (i%4 == 0){
                keyVigBit[count] = tempBit[i];
                count++;
            }
        }
        Bit[] tempVigBit = new Bit[8];
        byte[] arrVigKeyByte = new byte[4];
        int i = 0;
        int k = 0;
        for(int j = 0;j<keyVigBit.length;j++){
            tempVigBit[i] = keyVigBit[j];
            i++;
            if((j+1) % 8 == 0){
                arrVigKeyByte[k] = BC.convertBitsToByte(tempVigBit);
                tempVigBit = new Bit[8];
                k++;
                i = 0;
            }
            
        }
        
        String vigKey = new String(arrVigKeyByte,"UTF-8");
        vigkeytemp = vigKey;
        return vigKey;
    }
    
    public Bit[] concat(Bit[] a, Bit[] b) {
        int aLen = a.length;
        int bLen = b.length;
        Bit[] c= new Bit[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
    
    public static void main (String[] args) throws UnsupportedEncodingException {
        Key keyy = new Key();
        keyy.setKey("RANDICHILYONALFIANTO",128);
        Block[] a = keyy.getKeyBlock();
        for(int i = 0;i<a.length;i++){
            System.out.println(a[i]);
        }
        System.out.println(keyy.getKeyBlock());
        System.out.println("encrypted " + keyy.getEncryptedText());
        System.out.println("keyText " + keyy.getKeyText());
        System.out.println("vigkey"+ keyy.vigkeytemp);
        System.out.println(keyy);
    }
}
