/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockcipher.Bits;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Byte;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Sentosa
 */
public class Text {
    private ArrayList<Block> arrBlocks;
    private int sizeOfBlocks;
    private final int defaultSizeOfBlocks = 16; 
    private ByteConverter BC;
    private String textInput;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public Text() {
        sizeOfBlocks = defaultSizeOfBlocks;
        arrBlocks = new ArrayList<Block>();
        BC = new ByteConverter();
        textInput = new String();
    }
    
    public Bit[] concat(Bit[] a, Bit[] b) {
        int aLen = a.length;
        int bLen = b.length;
        Bit[] c= new Bit[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
    
    public ArrayList<Block> getBlocks() {
        return arrBlocks;
    }
    
    public void setBlocks(ArrayList<Block> blocks) {
        this.arrBlocks = blocks;
    }
    
    public void setEncryptedText(String text) {
        byte[] b = hexStringToByteArray(text);
        Block tempblock = new Block();
        Bit[] tempBits = concat (BC.convertByteToBits(b[0]),BC.convertByteToBits(b[1]));
        tempblock.setBits(tempBits);
        arrBlocks.add(tempblock);
        tempBits = null;
        tempblock = new Block();
        Bit[] endBit = new Bit[8];
        for (int i=0;i<endBit.length;i++){
            endBit[i]= new Bit();
        }
        for (int i = 2;i<b.length;i++){
            if ((i - 1) % 2 != 0){
                tempBits = BC.convertByteToBits(b[i]);
            }
            if (b.length%2 !=0 && i == b.length-1){
                tempBits = concat(tempBits,endBit);
                tempblock.setBits(tempBits);
                arrBlocks.add(tempblock);
                tempBits = null;
                tempblock = new Block();
            }
            if ((i - 1) % 2 == 0){
                tempBits = concat(tempBits,BC.convertByteToBits(b[i]));
                tempblock.setBits(tempBits);
                arrBlocks.add(tempblock);
                tempBits = null;
                tempblock = new Block();
            }
        }
    }
    
    public void setPlainText(String text) {
        byte[] b = text.getBytes();
        Block tempblock = new Block();
        Bit[] tempBits = concat (BC.convertByteToBits(b[0]),BC.convertByteToBits(b[1]));
        tempblock.setBits(tempBits);
        arrBlocks.add(tempblock);
        tempBits = null;
        tempblock = new Block();
        Bit[] endBit = new Bit[8];
        for (int i=0;i<endBit.length;i++){
            endBit[i]= new Bit();
        }
        for (int i = 2;i<b.length;i++){
            if ((i - 1) % 2 != 0){
                tempBits = BC.convertByteToBits(b[i]);
            }
            if (b.length%2 !=0 && i == b.length-1){
                tempBits = concat(tempBits,endBit);
                tempblock.setBits(tempBits);
                arrBlocks.add(tempblock);
                tempBits = null;
                tempblock = new Block();
            }
            if ((i - 1) % 2 == 0){
                tempBits = concat(tempBits,BC.convertByteToBits(b[i]));
                tempblock.setBits(tempBits);
                arrBlocks.add(tempblock);
                tempBits = null;
                tempblock = new Block();
            }
        }
    }
    
    public String toString() {
        String result = "";
        for(int i=0; i<arrBlocks.size(); i++) {
            result = result + arrBlocks.get(i);
            result = result + "\n";
        }
        return result;
    }
    
    /**
     * Ubah dari blocks ke string
     * @return string dari sekumpulan blocks
     */
    public String getEncryptedText() throws UnsupportedEncodingException {
        // Ubah dari blocks ke text
        ArrayList<Bit[]> tempArrBit = new ArrayList<>();
        for (int i = 0;i<arrBlocks.size();i++){
            tempArrBit.add(Arrays.copyOfRange(arrBlocks.get(i).getBits(),0,8));
            tempArrBit.add(Arrays.copyOfRange(arrBlocks.get(i).getBits(),8,arrBlocks.get(i).getBits().length));
        }
        byte[] arrByte = new byte[tempArrBit.size()];
        
        for (int i = 0;i<tempArrBit.size();i++){
            arrByte[i] = (BC.convertBitsToByte(tempArrBit.get(i)));
        }
        String result = bytesToHex(arrByte);
        return result;
    }
    
    /**
     * Ubah dari blocks ke string
     * @return string dari sekumpulan blocks
     */
    public String getPlainText() throws UnsupportedEncodingException {
        // Ubah dari blocks ke text
        ArrayList<Bit[]> tempArrBit = new ArrayList<>();
        for (int i = 0;i<arrBlocks.size();i++){
            tempArrBit.add(Arrays.copyOfRange(arrBlocks.get(i).getBits(),0,8));
            tempArrBit.add(Arrays.copyOfRange(arrBlocks.get(i).getBits(),8,arrBlocks.get(i).getBits().length));
        }
        byte[] arrByte = new byte[tempArrBit.size()];
        
        for (int i = 0;i<tempArrBit.size();i++){
            arrByte[i] = (BC.convertBitsToByte(tempArrBit.get(i)));
        }
        String result = new String(arrByte);
        return result;
    }

    public String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static void main (String[] args) throws UnsupportedEncodingException {
        Text textt = new Text();
        textt.setEncryptedText("C4FA965DE9F69496D2FA941CE23480D6F3F6959D");
        System.out.println(textt.arrBlocks);
        Text temp = new Text();
        temp.setBlocks(textt.arrBlocks);
        System.out.println(temp.getEncryptedText());
    }
    
}
