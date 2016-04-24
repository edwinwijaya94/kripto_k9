/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockcipher.Bits;

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
    
    public Text() {
        sizeOfBlocks = defaultSizeOfBlocks;
        arrBlocks = new ArrayList<Block>();
        BC = new ByteConverter();
        textInput = new String();
    }
    
    /**
     * Ubah dari text ke blocks
     * @param text string yang mau diubah
     * @param sizeOfBits jumlah bits untuk setiap block message = 128
     */
    public Text(String text, int sizeOfBits) {
        sizeOfBlocks = sizeOfBits / Block.getDefaultSize();
        setText(text);
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
    
    public void setText(String text) {
        try {
            // Ubah dari string ke blocks
            byte[] b = text.getBytes("UTF-8");
            
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
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Text.class.getName()).log(Level.SEVERE, null, ex);
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
    public String getText() throws UnsupportedEncodingException {
        // Ubah dari blocks ke text
        ArrayList<Bit[]> tempArrBit = new ArrayList<>();
        for (int i = 0;i<arrBlocks.size();i++){
            tempArrBit.add(Arrays.copyOfRange(arrBlocks.get(i).getBits(),0,8));
            tempArrBit.add(Arrays.copyOfRange(arrBlocks.get(i).getBits(),8,arrBlocks.get(i).getBits().length));
        }
        
        byte[] arrByte = new byte[tempArrBit.size()];
        
        String result = new String();
        for (int i = 0;i<tempArrBit.size();i++){
            arrByte[i] = (BC.convertBitsToByte(tempArrBit.get(i)));
        }
        for(int i=0; i<arrByte.length; i++) {
            result += (char) arrByte[i];
//            System.out.print((char) arrByte[i] + " ");
//            System.out.println((int) arrByte[i]);
        }
        return result;
    }
    
    public static void main (String[] args) throws UnsupportedEncodingException {
        Text textt = new Text();
        textt.setText("william sentosa");
        System.out.println(textt.arrBlocks);
        Text temp = new Text();
        temp.setBlocks(textt.arrBlocks);
        System.out.println(temp.getText());
    }
    
}
