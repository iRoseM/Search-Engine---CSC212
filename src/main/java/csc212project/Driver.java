
package csc212project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class Driver{
LinkedList<String> stopWords;
index indexl;
InvertedIndex inverted;
String line ;

public Driver (){
    stopWords=new LinkedList<>();
    indexl=new index();
    inverted=new InvertedIndex();
}

public void LoadStopWords(String fileName){
    try{
        File file=new File (fileName) ;
        Scanner Read=new Scanner (file) ;
        while (Read.hasNextLine() )
        line=Read.nextLine(); 
        stopWords.insert(line) ;
    }catch(IOException e){
        e.printStackTrace();
    }
}

public void LoadAllDoc(String fileName){
    String line=null;
    try{
        File file=new File (fileName) ;
        Scanner Read=new Scanner (file) ;
        Read.nextLine() ;

        while (Read.hasNextLine()){
        line=Read.nextLine() ;

        if (line.trim().length()<3)
        break;

        String F =line.substring(0,line.indexOf(',')).trim();
        int id = Integer.parseInt(F) ;
        String WORDS = line.substring (line.indexOf(',')+1).trim() ;
        LinkedList<String>WordsINDoc=InvertedIndexDoc(WORDS, id) ;
        indexl.add_document(new Document (id,WordsINDoc)) ;
    }
    }catch(Exception e) {
        System.out.println("end of file");
    }
}

public LinkedList<String > InvertedIndexDoc (String WORDS, int id){
    LinkedList<String>WordsINDoc=new LinkedList<String>();
    InvertedIndex(WORDS, WordsINDoc, id);
    return WordsINDoc;
}

public void InvertedIndex(String WORDS, LinkedList<String>WordsINDoc, int id){
    WORDS = WORDS.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "");
    String[] tokens = WORDS.split("\\s+");
    for (String w: tokens) {
        if (!IsStopWord(w)){
            WordsINDoc.insert(w);
            inverted.add(w , id);
        }
    }
}

public boolean IsStopWord (String word) {
    if (stopWords==null||stopWords.empty())
        return false;
    stopWords.findFirst();
    
    while(!stopWords.last()){
        if(stopWords.retrieve().equals(word))
            return true;
        stopWords.findNext();
    }
    if(stopWords.retrieve().equals(word))
        return true;

    return false;
}

public void LoadFiles(String stop , String doucment){
    LoadAllDoc(doucment);
    LoadStopWords(stop);
}


public void displayWords(){
    stopWords.display ();
}

public static void main(String args[]){
    Driver driver=new Driver();
    driver.LoadFiles( "stop.txt", "dataset.csv");
    driver.indexl.displayDocuments();
    System.out.println("\n");
    driver.inverted.display_inverted_index();
}

}
