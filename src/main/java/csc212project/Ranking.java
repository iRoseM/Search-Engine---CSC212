package csc212project;

class Doc_Rank {
    int id;
    int rank;
    public Doc_Rank (int id , int rank) {
        this.id = id;
        this.rank=rank;
    }

    public void display () {
        System.out.println(id+"   "+rank);        
    }
}
//Using sorted list with ordering ids
public class Ranking {
    static String Query; 
    static InvertedIndexBST inverted;
    static index index1;
    static LinkedList<Integer> all_doc_in_query;
    static LinkedList<Doc_Rank> all_doc_ranked;
    
    public Ranking (InvertedIndexBST inverted, index index1, String Query ) {
        this.inverted = inverted; 
        this.index1=index1;
        this.Query= Query;
        all_doc_in_query=new LinkedList<Integer>();
        all_doc_ranked=new LinkedList<Doc_Rank>();
    }
    
    public void display_all_doc_with_score_usingList() {
        if (all_doc_ranked.empty()) {
            System.out.println("Empty list, no ranked documents found.");
            return;
        }
        System.out.printf("%-8s&-s\n","DocID","Score");
        all_doc_ranked.findFirst();
        while(!all_doc_ranked.last()){
            all_doc_ranked.retrieve().display();
            all_doc_ranked.findNext();
        }
        all_doc_ranked.retrieve().display();
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
    
    public static void insert_sorted_in_list(){
        RankQuery(Query);//Finding all_doc_in_query
        if (all_doc_in_query.empty()){
            System.out.println("Empty query");
            return;
        }
        all_doc_in_query.findFirst();
        while(!all_doc_in_query.last()){
            Document document = get_doc_given_id(all_doc_in_query.retrieve());
            int Rank = get_doc_rank_score(document, Query);
            insert_sorted_list(new Doc_Rank (all_doc_in_query.retrieve(),Rank)); //Adding the document in order
            all_doc_in_query.findNext();
        }
            Document document = get_doc_given_id(all_doc_in_query.retrieve());
            int Rank = get_doc_rank_score(document, Query);
            insert_sorted_list(new Doc_Rank (all_doc_in_query.retrieve(),Rank));
            all_doc_in_query.findNext();
    } 
    
        public static void insert_sorted_list (Doc_Rank dr) {
            if (all_doc_ranked.empty()){
                all_doc_ranked.insert(dr);
                return;
            }
            all_doc_ranked.findFirst();
            while(!all_doc_ranked.last()){
                if (dr.rank>all_doc_ranked.retrieve().rank) {
                    Doc_Rank dr1 = all_doc_ranked.retrieve();
                    all_doc_ranked.update(dr);
                    all_doc_ranked.insert(dr1);
                    return;
                }
                else
                    all_doc_ranked.findNext();
            }
            if (dr.rank>all_doc_ranked.retrieve().rank) {
                Doc_Rank dr1 = all_doc_ranked.retrieve();
                all_doc_ranked.update(dr);
                all_doc_ranked.insert(dr1);
                return;
            }
            else
                all_doc_ranked.findNext();
        }




    
    
    
}
