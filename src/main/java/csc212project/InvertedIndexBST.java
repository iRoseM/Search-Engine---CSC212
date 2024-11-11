
package csc212project;


public class InvertedIndexBST {
   
    BST<Word> inverted_index; 
    
        public InvertedIndexBST(){
        inverted_index=new BST<Word>();
    }
       public void add(String text, int id) {
        //If the word isn't found 
        if (!search_word_in_inverted(text) ){ 
            Word word = new Word(text);
            word.doc_IDS.insert(id);
            inverted_index.insert(text,word);
        }
        else {
            Word existingWord = inverted_index.retrieve();
            existingWord.add_Id(id);
        }
    }

    public boolean search_word_in_inverted(String word) {
        return inverted_index.findKey(word);
    }
    
    public void display_inverted_index_BST(){
        if (inverted_index==null) {
            System.out.println("Null inverted index");
            return;
        }
        else if (inverted_index.empty()){
            System.out.println("Empty inverted index");
            return;
        }
       inverted_index.inOrder();
    }

}
