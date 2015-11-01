/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author gaurav
 */
public class WordBuilder {
    
    //word and line number
    private LinkedList<WordRecord> wordList;
    final int SYNONYM = 1;
    final int ANTONYM = 2;
    
    public WordBuilder() {
        wordList = new LinkedList<WordRecord>();
        wordList.clear();
    }
    
    public void readAndBuild() throws FileNotFoundException {
        File file = new File("src/Resources/dict");
        //List<String> tempStringList = new LinkedList<String>();
        Scanner sc = new Scanner(file);
        
        while(sc.hasNextLine()){
            ConcurrentLinkedQueue< String > synList = new ConcurrentLinkedQueue<String>();
            ConcurrentLinkedQueue< String > antList = new ConcurrentLinkedQueue<String>();
            String line = sc.nextLine();
            String words[] = line.split(";");
            WordRecord wordRecord = new WordRecord();
            wordRecord.setWord(words[0]);
            wordRecord.setMeaning(words[1]);
            
            //building synonyms

            String syns[] = words[2].split(",");
            synList.addAll(Arrays.asList(syns));
            wordRecord.setSyn(synList);
            //end building synonyms
            
            //building antonyms
            String ants[] = words[3].split(",");
            antList.addAll(Arrays.asList(ants));
            wordRecord.setAnt(antList);
            //end building antonyms
            
            wordRecord.setUsage(words[4]);
            
            wordList.add(wordRecord);
        }
  
        for(WordRecord w : wordList) {
            w.setSyn(scanForSynonyms(w, SYNONYM));
        }
        
    }
    
    public int getListSize() {
        return wordList.size();
    }
    public void printWordList() {
        for (WordRecord w : wordList) {
            System.out.println(" Word : " + w.getWord() + " ...Mean " + w.getMeaning() + " ...Syn: " + w.getSyn().toString() + " ....Ant  " + w.getAnt().toString() + " ....Usage " + w.getUsage() );
            for(String s : w.getSyn())
                System.err.println(" Synon  value"  + s);
        }
    }
    
//    @Input to the function is original word and list of comma separated 
//    synonyms already present in word
//    @Output The expanded synonyms list in queue format, not in string
//    form
//    This function updates the antonyms and synonyms list of words
    public ConcurrentLinkedQueue<String> scanForSynonyms(WordRecord originalWord, int p ) {
        boolean elementFound = false;
        ConcurrentLinkedQueue<String> idleSynList = originalWord.getSyn();
        ConcurrentLinkedQueue<String> activeSynList = originalWord.getSyn();
        //List<WordRecord> dummySynList = originalWord.getSyn();
        //add original word too in idle list..so as to prevent circular references
        //It has to be DELETED at the end
        idleSynList.add(originalWord.getWord());
   
       
        
        for (Iterator<String> iter = activeSynList.iterator(); iter.hasNext() ;  ){
            //Extract Synonyms of this word
            String word = iter.next();
            if(find(word) == null){
               
                iter.remove();
                continue;
            }
           
            else {
                //Word is there in the list so take its synonym in another list and match with
                //the active list if its already there do nothing else add it to both 
                //idle and active
                ConcurrentLinkedQueue<String> subSynonymList = find(word).getSyn();
                //compare each string of his sublist with the idleList if not found
                //add it to both active and idle list
                //Using a flag to check if found or not
                for(String s : subSynonymList) {
                    elementFound = false;
                    for(String idleElement : idleSynList) {
                        if(s.equals(idleElement)) {
                            elementFound = true;
                            break;
                        }
                    }
                    if(!elementFound) {
                        idleSynList.add(s);
                        activeSynList.add(s);
                    }
                }
                
                
            }
            //get original synonyms of a given word...
 
//            for(String tempWordToMatch : activeSynList) {
//                elementFound = false;
//                for(WordRecord origWordRecord : wordList) {
//                    if(origWordRecord.getWord().equals(tempWordToMatch)) {
//                        elementFound = true;
//                        break;
//                    }
//                }
//                if(elementFound == true) {
//                    idleSynList.add(tempWordToMatch);
//                    activeSynList.add(tempWordToMatch);
//                }
//
//            }
            iter.remove();
           
        }
            
        activeSynListSynList.remove(originalWord.getWord());

        
        return idleSynList;
        
    }

    //This functions find the string and if present then it returns
    //its corresponding WordRecord, if not found, null is returned.
    private WordRecord find(String next) {
        for(WordRecord w : wordList) {
            if(w.getWord().equals(next))
                return w;
        }
        return null;
    }
    
}
