package com.blockcipher.Bits;

import com.blockcipher.Bits.Bit;

import static java.lang.Math.random;

/**
 * Kelas ini untuk merepresentasikan block
 * @author William Sentosa
 */
public class Block {
    private Bit[] bits;
    private int size;
    private static final int defaultSize = 16;
    
    public Block() {
        size = defaultSize;
        bits = new Bit[size];
        for(int i=0; i<size; i++) {
            bits[i] = new Bit();
        }
    }
    
    public Block(Bit[] bits) {
        this.bits = bits;
        this.size = bits.length;
    }
    
    public Block(Block b) {
        this.size = b.size;
        bits = new Bit[size];
        for(int i=0; i<size; i++) {
            bits[i] = new Bit();
            bits[i].setValue(b.getBits()[i].getValue());
        }
    }
    
    public int getSize() {
        return size;
    }
    
    public Bit[] getBits() {
        return bits;
    }
    
    public void setBits(Bit[] bits) {
        this.bits = bits;
    }
    
    public static int getDefaultSize() {
        return defaultSize;
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
     * Mengxor dirinya dengan block lain
     * @param anotherBlock block akan dixor dengan block ini
     */
    public void xor(Block anotherBlock) {
        assert(size == anotherBlock.getSize());
        for(int i=0; i<size; i++) {
            bits[i].setValue(bits[i].getValue() ^ anotherBlock.getBits()[i].getValue());
        }
    }
    
    private Bit[][] convertToMatrix(Bit[] bits, int size) {
        Bit[][] result = new Bit[4][4];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                result[i][j] = bits[i*size + j];
            }
        }
        return result;
    }
    
//    private void printMatrix(Bit[][] bits) {
//        for(int i=0; i<bits.length; i++) {
//            for(int j=0; j<bits[i].length; j++) {
//                System.out.print(bits[i][j]);
//            }
//            System.out.println();
//        }
//    }
    
    /**
     * Melakukan fungsi f dengan key tertentu
     * @param key sub key dari kunci internal
     */
    public void fFunction(Bit[] subKey) {
        Bit[][] matrix = convertToMatrix(bits, 4);
        Boolean temp;
        for(int i=0; i<subKey.length; i++) {
            if(i/4 == 0 || i/4 == 2) { // Pergeseran elemen pada baris
                if(subKey[i].convertToInt() == 0) { // Geser Kiri
                    temp = matrix[i%4][0].getValue();
                    for(int j=1; j<4; j++) {
                        matrix[i%4][j-1].setValue(matrix[i%4][j].getValue());
                    }
                    matrix[i%4][3].setValue(temp);
                } else {
                    temp = matrix[i%4][3].getValue();
                    for(int j=2; j>=0; j--) {
                        matrix[i%4][j+1].setValue(matrix[i%4][j].getValue());
                    }
                    matrix[i%4][0].setValue(temp);
                }
                // Xor perbaris
                for(int j=0; j<4; j++) {
                    matrix[i%4][j].setValue(matrix[i%4][j].getValue() ^ subKey[i].getValue());
                }
            } else if(i/4 == 1 || i/4 == 3) { // pergeseran elemen per kolom
                if(subKey[i].convertToInt() == 0) { // Geser Atas
                    temp = matrix[0][i%4].getValue();
                    for(int j=1; j<4; j++) {
                        matrix[j-1][i%4].setValue(matrix[j][i%4].getValue());
                    }
                    matrix[3][i%4].setValue(temp);
                } else { // Geser bawah
                    temp = matrix[3][i%4].getValue();
                    for(int j=2; j>=0; j--) {
                        matrix[j+1][i%4].setValue(matrix[j][i%4].getValue());
                    }
                    matrix[0][i%4].setValue(temp);
                }
                // Xor perbaris
                for(int j=0; j<4; j++) { // Geser Kanan
                    matrix[j][i%4].setValue(matrix[j][i%4].getValue() ^ subKey[i].getValue());
                }
            }
        } 
    }
    
    public void reverseFFunction(Bit[] subKey) {
        Bit[][] matrix = convertToMatrix(bits, 4);
        Boolean temp;
        for(int i=subKey.length; i>=0; i--) {
            if(i/4 == 0 || i/4 == 2) { // Pergeseran elemen pada kolom                
                // Xor perbaris
                for(int j=0; j<4; j++) {
                    matrix[i%4][j].setValue(matrix[i%4][j].getValue() ^ subKey[i].getValue());
                }
                if(subKey[i].convertToInt() == 0) { // Geser Kanan
                    temp = matrix[i%4][3].getValue();
                    for(int j=2; j>=0; j--) {
                        matrix[i%4][j+1].setValue(matrix[i%4][j].getValue());
                    }
                    matrix[i%4][0].setValue(temp);
                } else { // Geser Kiri
                    temp = matrix[i%4][0].getValue();
                    for(int j=1; j<4; j++) {
                        matrix[i%4][j-1].setValue(matrix[i%4][j].getValue());
                    }
                    matrix[i%4][3].setValue(temp);
                }
            } else if(i/4 == 1 || i/4 == 3) { // pergeseran elemen per baris
                // Xor perbaris
                for(int j=0; j<4; j++) { // Geser Kanan
                    matrix[j][i%4].setValue(matrix[j][i%4].getValue() ^ subKey[i].getValue());
                }
                if(subKey[i].convertToInt() == 0) { // Geser Bawah
                    temp = matrix[3][i%4].getValue();
                    for(int j=2; j>=0; j--) {
                        matrix[j+1][i%4].setValue(matrix[j][i%4].getValue());
                    }
                    matrix[0][i%4].setValue(temp);
                } else { // Geser Atas
                    temp = matrix[0][i%4].getValue();
                    for(int j=1; j<4; j++) {
                        matrix[j-1][i%4].setValue(matrix[j][i%4].getValue());
                    }
                    matrix[3][i%4].setValue(temp);
                }
            }
        }
    }
    
    public static void main(String args[]) {
        Block block = new Block();
        Bit[] bits = new Bit[16];
        for(int i=0; i<16; i++) {
            bits[i] = new Bit(((int)(Math.random()*100))%2 == 0 );
        }
        block.setBits(bits);
        System.out.println("*** Block ***");
        System.out.println(block);
        Bit[] key = new Bit[16];
//        System.out.println("*** Key ***");
        for(int i=0; i<16; i++) {
            key[i] = new Bit(((int)(Math.random()*10))%2 == 0 );
//            System.out.print(key[i]);
        }
//        System.out.println();
        block.fFunction(key);
        System.out.println("*** Hasil ***");
        System.out.println(block);
        block.reverseFFunction(key);
        System.out.println("*** Hasil Reverse ***");
        System.out.println(block);
    }

}
