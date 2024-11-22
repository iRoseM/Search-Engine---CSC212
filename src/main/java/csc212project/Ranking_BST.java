
package csc212project;

public class Ranking_BST {
    
    static String Query; 
    static InvertedIndexBST inverted;
    static index index1;
    static LinkedList<Integer> all_doc_in_query;
    static BST_int<Integer>DocIDs_withRank;
    
    public Ranking_BST (InvertedIndexBST inverted, index index1, String Query ) {
        this.inverted = inverted; 
        this.index1=index1;
        this.Query= Query;
        all_doc_in_query=new LinkedList<Integer>();
        DocIDs_withRank=new BST_int<Integer>();
    }
    //undone
    public void display_all_doc_with_score() {
        DocIDs_withRank.display_decreasing();
    }
    
    public static Document get_doc_given_id(int id) {
        return index1.get_document_given_id(id);
    }
    
    public static int term_frequency_in_doc(Document d, String term) {
        int frequency = 0;
        LinkedList<String> words= d.words;
        if (words.empty())
            return 0;
        words.findFirst();
        while (!words.last()){
            if (words.retrieve().equalsIgnoreCase(term))
                frequency++;
            words.findNext();
        }
        if (words.retrieve().equalsIgnoreCase(term)) //last wors
                frequency++;
        return frequency;      
    }
    
    public static int get_doc_rank_score(Document document, String query) {
        if (query.length()==0)
            return 0;
        String terms[]=query.split(" ");
        int sum_freq=0;
        for(int i =0; i<terms.length;i++)
            sum_freq+=term_frequency_in_doc(document,terms[i].trim().toLowerCase());
        return sum_freq;
    }
    
    public static void RankQuery (String query) {
        LinkedList<Integer> A = new LinkedList<Integer> ();
        if (Query.length()==0) 
            return;
        String terms[]=query.split(" ");
        boolean found = false;
        for (int i =0 ;i<terms.length;i++){
            found = inverted.search_word_in_inverted(terms[i].trim().toLowerCase());
            if(found)
                A=inverted.inverted_index.retrieve().doc_IDS;
            Adding_in_1_List_Sorted(A);
        }
    }
    
    public static void Adding_in_1_List_Sorted (LinkedList<Integer> A) {
        if (A.empty())
            return;
        A.findFirst();
        while (!A.empty()){
            boolean found = existsIn_result(all_doc_in_query,A.retrieve());
            if (!found)
                insert_sorted_Id_list(A.retrieve());
            
            if (!A.last())
                A.findFirst();
            else
                break;
        }
    }
    
    public static boolean existsIn_result(LinkedList<Integer> result,Integer id){
        if (result.empty())
            return false;
        result.findFirst();
        while (!result.last()) {
            if (result.retrieve().equals(id))
                return true;
            result.findNext();
        }
        if(result.retrieve().equals(id))
            return true;
        return false;
    }
    
    public static void  insert_sorted_Id_list(Integer id) {
        if (all_doc_in_query.empty()){
            all_doc_in_query.insert(id);
            return;
        }
        all_doc_in_query.findFirst();
        while(!all_doc_in_query.last()){
            if (id<all_doc_in_query.retrieve()){
                Integer id1 = all_doc_in_query.retrieve();
                all_doc_in_query.update(id);
                all_doc_in_query.insert(id1);
                return;
            }
            else
                all_doc_in_query.findNext();
        }
        if (id<all_doc_in_query.retrieve()){
            Integer id1 = all_doc_in_query.retrieve();
            all_doc_in_query.update(id);
            all_doc_in_query.insert(id1);
            return;
        }
        else
            all_doc_in_query.findNext();
            
    }
    
    public static void insert_sorted_inBST(){
        RankQuery(Query);//Finding all_doc_in_query
        if (all_doc_in_query.empty()){
            System.out.println("Empty query");
            return;
        }
        all_doc_in_query.findFirst();
        while(!all_doc_in_query.last()){
            Document document = get_doc_given_id(all_doc_in_query.retrieve());
            int Rank = get_doc_rank_score(document, Query);
            DocIDs_withRank.insert(Rank,all_doc_in_query.retrieve());
            all_doc_in_query.findNext();
        }
            Document document = get_doc_given_id(all_doc_in_query.retrieve());
            int Rank = get_doc_rank_score(document, Query);
            DocIDs_withRank.insert(Rank,all_doc_in_query.retrieve());
            all_doc_in_query.findNext();
    } 
    
}
