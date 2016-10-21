/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabbuilder;

/**
 *
 * @author gpruthi
 */
public class BaseData {
    String word;
    String meaning;
    String synonyms;
    String antonyms;
    String description;
    int access_count;
    double last_access;
    int level;
    
    boolean flag;
    
    BaseData() {
        word = null;
        meaning = null;
        synonyms = null;
        antonyms = null;
        description = null;
        access_count = 0;
        last_access = 0;
        level = 0;
        flag = false;
    }
    
}
