package csc212project;

public class QueryProcessing_from_index {
    static index index1;
    
    public QueryProcessing_from_index (index index1) {
        this.index1=index1;
    }


    public static LinkedList<Integer>AndQuery (String Query) {
        LinkedList<Integer> A=new LinkedList<Integer>() ;
        LinkedList<Integer> B=new LinkedList<Integer>() ;
        String terms []=Query.split ("AND"); 
        if (terms.length==0) 
            return A;
        A=index1.get_all_documents_given_term(terms[0].trim().toLowerCase());
        for(int i = 0; i<terms.length ;i++){
            B=index1.get_all_documents_given_term(terms[i].trim().toLowerCase());
            A=AndQuery(A,B);
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
        A=index1.get_all_documents_given_term(terms[0].trim().toLowerCase());
        for(int i = 0; i<terms.length ;i++){
            B=index1.get_all_documents_given_term(terms[i].trim().toLowerCase());
            A=OrQuery(A,B);
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
