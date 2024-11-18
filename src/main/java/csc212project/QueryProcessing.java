
package csc212project;

public class QueryProcessing { 
    static InvertedIndex inverted;
    
public QueryProcessing (InvertedIndex inverted) {
    this. inverted=inverted;
}

public static LinkedList<Integer>AndQuery (String Query) {
    LinkedList<Integer> A=new LinkedList<Integer>() ;
    LinkedList<Integer> B=new LinkedList<Integer>() ;
    String terms []=Query.split ("AND"); 
    if (terms.length==0) 
        return A;
    boolean found=inverted.search_word_in_inverted (terms[0].trim ().toLowerCase()) ;
    if (found) 
        A=inverted.inverted_index.retrieve().doc_IDS;
    
    for (int i=1;i<terms.length;i++){
    found= inverted. search_word_in_inverted (terms[i].trim().toLowerCase()) ;
    if (found)
        B=inverted.inverted_index.retrieve().doc_IDS;
    A= AndQuery (A, B) ;
    }
    return A;
}

    public static LinkedList<Integer > AndQuery (LinkedList<Integer>A, LinkedList<Integer>B) {
        LinkedList<Integer> result=new LinkedList<Integer>();
        if (A. empty() || B.empty())
            return result;
        A.findFirst();
        while (true) {
            boolean found=existsIn_result(result, A.retrieve());
            if(!found) { //Not found in result
                B.findFirst ();
                while (true) {
                    if (B.retrieve().equals (A.retrieve())){
                        result.insert (A.retrieve());
                        break;
                    }
                if (!B.last())
                    B.findNext();
                else
                    break;
                }//end inner while for B
            }//end if not found
            if (!A.last())
                A.findNext();
            else 
                break;
            }
        return result;
}
    
    public static LinkedList<Integer>OrQuery (String Query) {
        LinkedList<Integer> A=new LinkedList<Integer>() ;
        LinkedList<Integer> B=new LinkedList<Integer>() ;
        String terms []=Query.split ("OR"); 
        if (terms.length==0) 
            return A;
        boolean found=inverted.search_word_in_inverted (terms[0].trim().toLowerCase()) ;
        if (found) 
            A=inverted.inverted_index.retrieve().doc_IDS;
        for (int i=1;i<terms.length;i++){
            found=inverted. search_word_in_inverted (terms[i].trim().toLowerCase()) ;
            if (found)
                B=inverted. inverted_index.retrieve().doc_IDS;
            A= OrQuery(A, B) ;
        }
        return A;
 }
        public static LinkedList<Integer > OrQuery (LinkedList<Integer>A, LinkedList<Integer>B) {
        LinkedList<Integer> result=new LinkedList<Integer>();
        if (A.empty () && B.empty ())
            return result;
        A.findFirst() ;
        while (!A.empty()) {
            boolean found=existsIn_result(result, A.retrieve());
            if(!found)//Not found in result
                result.insert(A.retrieve());

            if (!A. last())
                A.findNext();
            else 
                break;
        }//end inner while for A
        
        B.findFirst();
        while(!B.empty()){
             boolean found=existsIn_result(result, A.retrieve());
            if(!found)//Not found in result
                result.insert(B.retrieve());
        
            if (!B. last())
                B.findNext();
            else 
                break;
        }//end inner while for B
        return result;
}
        
    public static LinkedList<Integer> BooleanQuery(String Query){
        if(!Query.contains("AND") && !Query.contains("OR"))
            return AndQuery(Query);
        else if(Query.contains("AND") && !Query.contains("OR"))
            return AndQuery(Query);
        else if(!Query.contains("AND") && Query.contains("OR"))
            return OrQuery(Query);
        else
            return MixedQuery(Query);
}
    
    public static LinkedList<Integer>MixedQuery(String Query){
        LinkedList<Integer> A= new LinkedList<Integer>();
        LinkedList<Integer> B= new LinkedList<Integer>();
        if(Query.length() == 0)
            return A;
        String ORs[]= Query.split("OR"); //less priorty than AND
        
        A= AndQuery(ORs[0]);
        for(int i=1; i< ORs.length; i++){
            B= AndQuery(ORs[i]);
            A= OrQuery(A, B);
        }
        return A;
}
    
    public static boolean existsIn_result(LinkedList<Integer>result, int id){
        if (result.empty())
            return false;
        result.findFirst();
        while (!result.last()){
            if (result.retrieve().equals(id))
                return true;
            result.findNext();
        }
        if (result.retrieve().equals(id)) 
            return true;
        return false;
    }
}
