package com.work.evindj.travler.ui;

/**
 * Created by evindj on 12/26/15.
 */
public class StringUtilities {
    public static String formatTitle(String input){
        if(input.length()<20) {

            return input.concat("\n");
        }
        if(input.length()<40) return  input;
        String[] words = input.split(" ");
        int charCount =0;
        String res ="";
        for(String w:words){
            for(char c:w.toCharArray()){
                charCount++;
            }
            if(charCount <40) {
                res = res.concat(w).concat(" ");
                charCount++;
            }
            else{
                res = res.concat("...");
                return res;
            }

        }
        return res;
    }
}
