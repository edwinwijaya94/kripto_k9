package com.blockcipher.Bits;

/**
 *
 * @author William Sentosa - 13513026
 */
public class Bit {
    private boolean value;
    
    public Bit() {
        value = false;
    }
    
    public Bit(boolean value) {
        this.value = value;
    }
    
    public boolean getValue() {
        return value;
    }
    
    public void setValue(boolean value) {
        this.value = value;
    }
    
    public void setValue(int value) {
        if(value == 0) {
            this.value = false;
        } else if(value == 1) {
            this.value = true;
        }
    }
    
    public int convertToInt() {
        if(value) 
            return 1;
        else 
            return 0;
    }
    
    public boolean isSameAs(Bit b) {
        return value == b.getValue();
    }
    
    @Override
    public String toString() {
        String result = "" + this.convertToInt();
        return result;
    }
    
    public static void main(String[] args) {
        Bit b = new Bit();
        b.setValue(true);
        System.out.println(b.convertToInt());
        b.setValue(true);
        System.out.println(b.convertToInt());
        Bit b2 = new Bit(true);
        System.out.println(b2.convertToInt());
        System.out.println(b.isSameAs(b2));
    }
}
