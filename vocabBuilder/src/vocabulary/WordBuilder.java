/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary;

import java.io.*;
import java.util.*;

/**
 *
 * @author gaurav
 */
public class WordBuilder {
    
    //word and line number
    private TreeMap<String, Integer> wordList;
    
    public WordBuilder() {
        wordList = new TreeMap<String, Integer>();
    }
    
    public void readAndBuild() throws FileNotFoundException {
        File file = new File("//home//gaurav//manning");
        Scanner sc = new Scanner(file);
        int i = 1;
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String words[] = line.split(";");
            //<word, line number>
            wordList.put(words[0], i);
            ++i;
        }
        
    }
    
    public int getMapSize() {
        return wordList.size();
    }
    public void printmap() {
        for (Map.Entry pairs : wordList.entrySet()) {
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
    }
    
}
