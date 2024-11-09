
package csc212project;


public class index {
    
    LinkedList<Document>all_doc;
    public index(){
        all_doc=new LinkedList<Document>();
        
    }
    
    ///helping methods//
    public void add_document(Document d)
    {
        all_doc.insert(d);
    }
    
    public void displayDocuments(){
        
      if (all_doc==null){
          System.out.println("null docs");
          return;
          
      }  
      else if (all_doc.empty()){
          System.out.println("empty docs");
                  return;
      }
      
      all_doc.findFirst();
      while (!all_doc.last()){
          Document doc= all_doc.retrieve();
          System.out.println("\n ------------------------");
            System.out.println("ID:"+ doc.id);
            doc.words.display();
            all_doc.findNext();
      }
      Document doc=all_doc.retrieve();
       System.out.println("\n ------------------------");
            System.out.println("ID:"+ doc.id);
            doc.words.display();
            
    }
    public static void main(String[]args){
        index ind1= new  index();
        LinkedList<String> words = new LinkedList<>();
        words.insert("national");
         words.insert("flag");
         Document d1= new Document(1,words);
         ind1.add_document(d1);
         
         
        LinkedList<String> words2 = new LinkedList<>();
        words2.insert("green");
         words2.insert("flag");
          Document d2= new Document(2,words2);
         
           ind1.add_document(d2);
           
           ind1.displayDocuments();
    }
  
    }
