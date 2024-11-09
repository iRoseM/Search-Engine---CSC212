
package csc212project;


public class Word {
    String text;
    LinkedList<Integer>doc_IDS;
    
    public Word(String W)
    {
        text=W;
        doc_IDS=new LinkedList<Integer>();
        
    }
    
    public void add_Id(int id)
    { 
        if (!existsIn_doc_IDS(id))
            doc_IDS.insert(id);
        
}
    public boolean existsIn_doc_IDS(Integer id){
        if (doc_IDS.empty()) return false;
        doc_IDS.findFirst();
        
        while (!doc_IDS.last()){
            if (doc_IDS.retrieve().equals(id)){
                return true;
            }
            doc_IDS.findNext();
            
        }
         if (doc_IDS.retrieve().equals(id)){
                return true;
            }
      return false;
    }


}