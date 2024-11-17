
package csc212project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class Driver{
LinkedList<String> stopWords;
index indexl;
InvertedIndex inverted;
InvertedIndexBST invertedBST;
String line ;

public Driver (){
    stopWords=new LinkedList<>();
    indexl=new index();
    inverted=new InvertedIndex();
    invertedBST= new InvertedIndexBST();
}

public void LoadStopWords(String fileName){
    try{
        File file=new File (fileName) ;
        Scanner Read=new Scanner (file) ;
        while (Read.hasNextLine() ){
            line=Read.nextLine(); 
            stopWords.insert(line) ;
        }
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
            

            if (line.trim().length()<3){
                System.out.println("Empty line found, skipping to the next one.");
                break;
        }
            String F =line.substring(0,line.indexOf(',')).trim();
            int id = Integer.parseInt(F) ;
            String WORDS = line.substring (line.indexOf(',')+1).trim() ;
            LinkedList<String>WordsINDoc=InvertedIndexDoc(WORDS, id) ;
            indexl.add_document(new Document (id,WordsINDoc)) ;
        }
    }catch(Exception e) {
        System.out.println("End of file");
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
            invertedBST.add(w, id);
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
    LoadStopWords(stop);
    LoadAllDoc(doucment);
    
}


public void displayStopWords(){
    stopWords.display ();
}

public void displayDocById(LinkedList<Integer> ids) {
    if (ids.empty()) {
        System.err.print("IDs list is empty.");
        return;
    }

    ids.findFirst();
    System.out.print("Result: {");
    while (!ids.last()) {
        int docID = ids.retrieve();
        indexl.findAndDisplayDoc(docID);
        System.out.print(",");
        ids.findNext();
    }
    indexl.findAndDisplayDoc(ids.retrieve());
    System.out.println("}");
}

    public static void testDataset2(){
    Driver driver= new Driver();
    driver.LoadFiles( "stop.txt", "dataset2.csv");
    System.out.println("\n################### Boolean Retrieval ####################");
    QueryProcessing queryProcessing= new QueryProcessing(driver.inverted);
    LinkedList queryResult;
    
    System.out.println("\n# Q: color AND green AND white");
    queryResult= QueryProcessing.MixedQuery("color AND green AND white");
    driver.displayDocById(queryResult);
    
    System.out.println("\n# Q: green OR shahada");
    queryResult= QueryProcessing.MixedQuery("green OR shahada");
    driver.displayDocById(queryResult);
    
}

    public static void testDataset(){
        Driver driver= new Driver();
        driver.LoadFiles( "stop.txt", "dataset.csv");
        System.out.println("\n################### Boolean Retrieval ####################");
        QueryProcessing queryProcessing= new QueryProcessing(driver.inverted);
        LinkedList queryResult;

        System.out.println("\n# Q: market AND sports");
        queryResult= QueryProcessing.MixedQuery("market AND sports");
        driver.displayDocById(queryResult);
        
        System.out.println("\n# Q: weather AND warming");
        queryResult= QueryProcessing.MixedQuery("weather AND warming");
        driver.displayDocById(queryResult);
        
        System.out.println("\n# Q: business AND world");
        queryResult= QueryProcessing.MixedQuery("business AND world");
        driver.displayDocById(queryResult);
        
        System.out.println("\n# Q: weather OR warming");
        queryResult= QueryProcessing.MixedQuery("weather OR warming");
        driver.displayDocById(queryResult);
        
        System.out.println("\n# Q: market OR sports");
        queryResult= QueryProcessing.MixedQuery("market OR sports");
        driver.displayDocById(queryResult);
        
        System.out.println("\n# Q: market OR sports AND warming");
        queryResult= QueryProcessing.MixedQuery("market OR sports AND warming");
        driver.displayDocById(queryResult);
}

public static void main(String args[]){
    testDataset2();
    testDataset();
    
    
    
//    Driver driver=new Driver();
//
//    driver.LoadFiles( "stop.txt", "dataset.csv");
//    driver.displayStopWords();
//    driver.indexl.displayDocuments();
//    System.out.println("\n");
//    driver.inverted.display_inverted_index();
//    
//    
//    System.out.println("\n=================== InvertedBST from the Inverted list ===================");
    //driver.inverted.display_inverted_index();
//    InvertedIndexBST inverted2= new InvertedIndexBST();
    
    
    
    //output is the same but the firstone is by using LinkedList and the other one using BST
    
    
//    QueryProcessing queryProcessing= new QueryProcessing(driver.inverted);
    //testing AndQuery
//    System.out.println("\n=================== Intersection ===================");
//    LinkedList resultIntersection= QueryProcessing.AndQuery("colorANDflag");
//    driver.displayDocById(resultIntersection);
    
    //testing OrQuery
//    System.out.println("\n====================== Union ======================");
//    LinkedList resultUnion= QueryProcessing.OrQuery("Arabia OR pole ORcolor");
//    driver.displayDocById(resultUnion);
    
}

}
