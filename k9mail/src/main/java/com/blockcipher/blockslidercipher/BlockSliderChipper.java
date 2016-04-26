/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockcipher.blockslidercipher;

import com.blockcipher.Bits.Bit;
import com.blockcipher.Bits.Block;
import com.blockcipher.Bits.Key;
import com.blockcipher.Bits.Text;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Sentosa
 * @author Randi Chilyon Alfianto
 */
public class BlockSliderChipper {
    private Key key;
    private Text text;
    private String result;
    private Block[] blocks;


    /**
     * BSC Constructor
     */
    public BlockSliderChipper() {
        key = new Key();
        text = new Text();
    }
    
    public void setPlainText(String source) {
        text.setPlainText(source);
    }
    
    public void setEncryptedText(String source) {
        text.setEncryptedText(source);
    }
    
    public void setKey(String key) {
        try {
            this.key.setKey(key, 128);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BlockSliderChipper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Membagi menjadi masing-masing 16-bit
     * @param bitKey Bit[]
     * @return ArrayList of bit
     */
    private ArrayList<Bit[]> divideBits(Bit[] bitKey) {
        ArrayList<Bit[]> result = new ArrayList<Bit[]>();
        for(int i=0; i<8; i++) {
            int idx = 0;
            Bit[] element = new Bit[16];
            for(int j = 16 * i; j < 16 * (i+1); j++) {
                element[idx] = bitKey[j];
                idx++;
            }
            result.add(element);
        }
        return result;
    }

    /**
     * Melakukan encrypt terhadap Plain Text
     * @return String cipherText
     */
    public String encrypt() {
        ArrayList<Block> blocks = text.getBlocks();
        if(blocks.size() % 2 != 0) {
            Block temp = new Block();
            blocks.add(temp);
        }
        System.out.println(key);
        Bit[] bitKey = key.getBits();
        ArrayList<Bit[]> subKey = divideBits(bitKey);
        int idx = 0;
        System.out.println("Text awal");
        System.out.println(blocks);
        for(int i=0; i<blocks.size(); i+=2) {
            // Bekerja dengan model jaringan feistel
            for(int j=0; j<8; j++) {
                if(j % 2 == 0) {
                    blocks.get(i+1).fFunction(subKey.get(idx));
                    blocks.get(i).xor(blocks.get(i+1));
                    blocks.get(i+1).reverseFFunction(subKey.get(idx));
                } else {
                    blocks.get(i).fFunction(subKey.get(idx));
                    blocks.get(i+1).xor(blocks.get(i));
                    blocks.get(i).reverseFFunction(subKey.get(idx));
                }
            }
            idx++;
            if(idx >= subKey.size()) {
                idx = 0;
            }
        }
        Text temp = new Text();
        System.out.println("Text akhir");
        System.out.println(blocks);
        temp.setBlocks(blocks);
        try {
            return temp.getEncryptedText();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BlockSliderChipper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Melakukan decrypt terhadap Cipher Text
     * @return String Plain Text
     */
    public String decrypt() {
        ArrayList<Block> blocks = text.getBlocks();
        Bit[] bitKey = key.getBits();
        ArrayList<Bit[]> subKey = divideBits(bitKey);
        int idx = 0;
        System.out.println(key);
        for(int i=0; i<blocks.size(); i+=2) {
            // Bekerja dengan model jaringan feistel
            for(int j=0; j<8; j++) {
                if(j % 2 == 0) {
                    blocks.get(i).fFunction(subKey.get(idx));
                    blocks.get(i+1).xor(blocks.get(i));
                    blocks.get(i).reverseFFunction(subKey.get(idx));
                } else {
                    blocks.get(i+1).fFunction(subKey.get(idx));
                    blocks.get(i).xor(blocks.get(i+1));
                    blocks.get(i+1).reverseFFunction(subKey.get(idx));
                }
            }
            idx++;
            if(idx >= subKey.size()) {
                idx = 0;
            }
        }
        Text temp = new Text();
        temp.setBlocks(blocks);
        try {
            return temp.getPlainText();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BlockSliderChipper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
