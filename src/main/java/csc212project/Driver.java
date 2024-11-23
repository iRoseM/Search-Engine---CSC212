
package csc212project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Driver{
LinkedList<String> stopWords;
static index index1;
InvertedIndex inverted;
InvertedIndexBST invertedBST;
String line ; 
int num_tokens=0;
LinkedList<String> unique_words=new LinkedList<>();

public Driver (){
    stopWords=new LinkedList<>();
    index1=new index();
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
            System.out.println("Line: "+id);
            String content = line.substring (line.indexOf(',')+1).trim() ;
            System.out.println("Conent: "+content);
            LinkedList<String>WordsINDoc=make_Linked_List_of_words_in_doc_index_inverted_index(content, id) ;
            index1.add_document(new Document (id,WordsINDoc)) ;
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
        index1.findAndDisplayDoc(docID);
        System.out.print(",");
        ids.findNext();
    }
    index1.findAndDisplayDoc(ids.retrieve());
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
    public LinkedList<String> make_Linked_List_of_words_in_doc_index_inverted_index(String content, int id) {
        LinkedList<String>words_in_doc=new LinkedList<String>();
        make_index_and_inverted_index(content, words_in_doc,id);
        return words_in_doc;
    }
    public void make_index_and_inverted_index(String content,LinkedList<String>words_in_doc,int id) {
        
        content =content.replaceAll("\'", " ");
        content =content.replaceAll("-", " ");
        while (content.contains("-")) {
            if (content.charAt(content.indexOf("-")-2)==' ')
                content=content.replaceFirst("-", "");
            else 
                content = content.replaceFirst("-", " ");
        }
        content =content.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
        String[] tokens = content.split("\\s+");
        num_tokens+=tokens.length;
        
        for (String w: tokens) {
            if (!unique_words.exist(w))
                unique_words.insert(w);
            
            if (!existsIn_stop_words(w)){
                words_in_doc.insert(w);
                inverted.add(w, id);
                invertedBST.add(w, id);
            }
        }
    }
    public void count_Tokens_and_unique_words(String content) {
        content=content.replaceAll("\'", " ");
        content =content.replaceAll("-", " ");
        content=content.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
        String[] tokens = content.split("\\s+");
        num_tokens+=tokens.length;
        for(String w: tokens) {
            if (!unique_words.exist(w))
                unique_words.insert(w);
        }
    }
        
        public boolean existsIn_stop_words(String word) {
            if (stopWords==null||stopWords.empty())
                return false;
            stopWords.findFirst();
            while (!stopWords.last()) {
                if (stopWords.retrieve().equals(word))
                    return true;
                stopWords.findNext();
            }
            if (stopWords.retrieve().equals(word))
                return true;
            return false;
    }
    public static void test(){
    Driver driver = new Driver();  
        driver.LoadFiles( "stop.txt","dataset.csv");
        Scanner read =new Scanner(System.in);
        int ch=0;
        do{
            System.out.println("===========================Search Engine===========================");
            System.out.println("Please choose from the following: ");
            System.out.println("1-Retrieve a term, there are choices: (Using index with lists - Inverted index with lists - Inverted index with BST");            System.out.println("2-Boolean Retrieval.");
            System.out.println("3-Ranked Retrieval");
            System.out.println("4-Print all Indexed Documents.");
            System.out.println("5-Print the number of document in the index.");
            System.out.println("6-Print the number of unique words in indexed.");
            System.out.println("7-Show inverted index with list of lists.");
            System.out.println("8-Show inverted index with BST.");
            System.out.println("9-Show the number of vocabulary and tokens in the index (Indexed Tokens).");
            System.out.println("10-Exit."); 
            ch=read.nextInt();
            switch(ch){
                case 1:
                    System.out.println("Enter a term to retrieve");  
                    String term=read.next();
                    term=term.toLowerCase().trim();
                    System.out.println(":using index with lists");
                    LinkedList<Integer>res= Driver.index1.get_all_documents_given_term(term);
                    System.out.print("word:"+term+"[");
                    res.display();
                    System.out.println("]");
                    System.out.println("=================================");
                    System.out.println("-inverted index with lists");
                    boolean found=driver.inverted.search_word_in_inverted(term);
                    if(found)
                        driver.inverted.inverted_index.retrieve().display();
                    else
                        System.out.println("Not found in inverted index with lists.");

                    System.out.println("-inverted index with BST.");  
                    boolean found2=driver.invertedBST.search_word_in_inverted(term);
                    if(found2)
                        driver.inverted.inverted_index.retrieve().display();
                    else
                        System.out.println("Not found in inverted index with lists.");
                    break;
                case 2:
                    read.nextLine();
                    System.out.println("Enter a query to retrieve");  
                    String query=read.nextLine();
                    query=query.toLowerCase();
                    query=query.replaceAll(" and "," AND ");
                    query=query.replaceAll(" or "," OR ");
                    System.out.println("""
                                       Please choose the query method:
                                            1-Using index. 
                                            2-Using inverted index list of lists. 
                                            3-Using BST.
                                            4-exit.
                                       """);
                    int choice=read.nextInt();
                    do{
                    if(choice==1){ // Query from index 
                      QueryProcessing_from_index q=new QueryProcessing_from_index(Driver.index1);  
                       System.out.println("========"+query+"=======");
                    LinkedList res1= QueryProcessing_from_index.MixedQuery(query);
                    driver.displayDocById(res1);
                    }
                    else if(choice==2){ // Normal Query                
                    QueryProcessing q=new QueryProcessing(driver.inverted);
                     System.out.println("========"+query+"=======");
                    LinkedList res1= QueryProcessing.MixedQuery(query);
                    driver.displayDocById(res1);
                    }
                    else if(choice==3){ // QueryProcessingBST
                      QueryProcessingBST q=new QueryProcessingBST(driver.invertedBST);
                      System.out.println("========"+query+"=======");
                      LinkedList res1= QueryProcessingBST.MixedQuery(query);
                      driver.displayDocById(res1);
                    }
                    else if(choice==4)
                        break;
                    else
                        System.out.println("invalid query.");

                    System.out.println("""  
                                       Please choose the query method:
                                            1-Using index. 
                                            2-Using inverted index list of lists. 
                                            3-Using BST.
                                            4-exit.
                                       """);
                    choice=read.nextInt();
                    }while(choice!=4);   

                    break;
                case 3:
                    read.nextLine();
                    System.out.println("Enter a query to Rank");
                    String query2=read.nextLine();
//                    System.out.println("Debug: Query is - " + query2);
                    query2=query2.toLowerCase();            
                    Ranking ranking=new Ranking(driver.inverted, index1,query2);
                    ranking.insert_sorted_in_list();
                    ranking.display_all_doc_with_score_usingList();
                    break;
                case 4:
                    driver.index1.displayDocuments();  
                    System.out.println("=================================");
                    break; 
                case 5:
                    System.out.println("Number of documents="+Driver.index1.all_doc.count); 
                    System.out.println("=================================");
                    break;
                case 6:
                    System.out.println("Number of unique words without stop words="+driver.inverted.inverted_index.count); 
                    System.out.println("=================================");
                    break;
                case 7:
                    driver.inverted.display_inverted_index();
                    break;
                case 8:
                    driver.invertedBST.display_inverted_index_BST();
                    break;
                case 9:
                 System.out.println("Number of tokens="+driver.num_tokens);
                 System.out.println("Number of unique words including stop words="+driver.unique_words.count);  
                    break;
                case 10:
                    System.out.println("==========Thank you for using our search engine!==========");
                    break;
                default:
                    System.out.println("Error input number, please try again.");
                    break;            
                }
            }while(ch!=10);

    }
    public static void main(String args[]){
//        test();
        testDataset();

    //    Driver driver=new Driver();
    //
    //    driver.LoadFiles( "stop.txt", "dataset.csv");
    //    driver.displayStopWords();
    //    driver.index1.displayDocuments();
    //    System.out.println("\n");
    //    
    //    driver.inverted.display_inverted_index();
    //    
    //    driver.invertedBST.display_inverted_index_BST();
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
