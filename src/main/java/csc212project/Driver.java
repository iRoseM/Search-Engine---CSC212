
package csc212project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class Driver {
    LinkedList<String> stopWord;
    index index1;
    InvertedIndex inverted;
    InvertedIndexBST invertedBST;
    public Driver(){
    stopWord=new LinkedList<>();
    index1=new index();
    inverted=new InvertedIndex();
    invertedBST=new InvertedIndexBST();
    }
    
    public void Load_stopWord(String fileName){
    try{
        File file = new File(fileName);
        Scanner scanner = new Scanner (file);
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            stopWord.insert(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    }
    
    public void Load_all_doc(String fileName){
        String line = null;
        try{
        File file = new File(fileName);
        Scanner scanner = new Scanner (file);
        //Skiping the first row 
        scanner.nextLine();
        while (scanner.hasNext()){
            line = scanner.nextLine();
            if (line.trim().length() < 3 ){ //To trim the white space, or the 
                System.out.println("Empty line found, skipping to the next one");
                break;
            }
        }
        String index = line.substring(0,line.indexOf(','));
        int id = Integer.parseInt(index.trim());//Remove any additional white spsace
        String content = line.substring(line.indexOf(',')+1);
        LinkedList<String>Words_in_doc=make_Linked_List_Of_Words_in_doc_index_inverted_index(content,id);
        index1.add_document(new Document(id,Words_in_doc));
        }catch (Exception e){
            System.out.println("End of file");
        }
    
    }
    
    
    
    public LinkedList <String> make_Linked_List_Of_Words_in_doc_index_inverted_index(String content , int id ){
        LinkedList<String>Words_in_doc= new LinkedList<String> ();
        make_index_and_inverted_index(content, Words_in_doc,id);
        return Words_in_doc;
    }
    public void make_index_and_inverted_index(String content , LinkedList<String>words_in_doc,int id) {
        content = content.toLowerCase().replaceAll("[^a-zA-Z0-9]","");
        String[] tokens = content.split("\\s+");
        for(String words : tokens){
            if (!existsIn_stop_words(words)) {
                words_in_doc.insert(words);
                inverted.add(words,id);
                invertedBST.add(words,id);

            }
        }
    }
    
    public boolean existsIn_stop_words (String word){
        if (stopWord==null || stopWord.empty())
            return false;
        stopWord.findFirst();
        while (!stopWord.last()) {
            if (stopWord.retrieve().equals(word)) 
                return true;
            stopWord.findNext();
        }
        if(stopWord.retrieve().equals(word))//Last word
            return true;
        return false;
    }
    
    public void Load_all_files (String stop_file,String document_file){
        Load_stopWord(stop_file);
        Load_all_doc(document_file);
    }
    public static void main(String []args ){
        Driver driver = new Driver();
        driver.Load_all_files("stop.txt", "dataset.csv");
        driver.index1.displayDocuments();
        System.out.println("\n--------------------------");
        driver.inverted.display_inverted_index();
        driver.invertedBST.display_inverted_index_BST();
//        QueryProcessing q-new QueryProcessing (driver.inverted);
//        LinkedList res= QueryProcessing.AndQuery ("colorANDgreen") ;
//        driver.display_doc_with_given_IDS (res) ;
//        System.out.println("------union------");
//        LinkedList res1= QueryProcessing.ORQuery(" Arabia OR pole ORcoloroo") ;
//        driver.display_doc_with_given_IDS(res1);
    
    }
    
}
