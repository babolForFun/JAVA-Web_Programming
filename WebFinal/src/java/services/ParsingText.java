package services;

import java.util.StringTokenizer;

public class ParsingText {

    public ParsingText(){}
    
    public static String parsing(String text,int gruppo){
        String[] result = text.split(" ");
        String res = "";
        for (int j=0; j<result.length; j++){
            String singleWord = result[j];
            int length = singleWord.length();
            if('$' == singleWord.charAt(0) && '$' == singleWord.charAt(1) && 
                    '$' == singleWord.charAt(length-1) && '$' == singleWord.charAt(length-2)){
                singleWord = singleWord.replace("$$","");
                //se e' un sito allora aggiungi l'http oppure vai direttamente al sito
                if(singleWord.startsWith("www.")){
                    singleWord = "http://" +singleWord;
                    singleWord = "<a href=\""+singleWord+"\" >"+singleWord+"</a>";                
                }else if(singleWord.startsWith("http://")){
                    singleWord = "<a href=\""+singleWord+"\" >"+singleWord+"</a>";                
                }else{
                    singleWord = "<a href=\"UploadedFile/"+gruppo+"/"+singleWord+"\" >"+singleWord+"</a>";
                }
            }
            res = res + " " +singleWord;
        }
        return res;
    }
}
