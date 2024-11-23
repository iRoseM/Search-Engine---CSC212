package csc212project;
//
class Doc_Rank {
    int id;
    int rank;
    public Doc_Rank (int id , int rank) {
        this.id = id;
        this.rank=rank;
    }
    public void display () {
        System.out.printf("%-8d%-8d\n",id,rank);        
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
        System.out.println("Displaying documents with scores...");

        if (all_doc_ranked.empty()) {
            System.out.println("Empty list, no ranked documents found.");
            return;
        }
        
        System.out.printf("%-8s%-8s\n","DocID","Score");
        all_doc_ranked.findFirst();
        while(!all_doc_ranked.last()){
            all_doc_ranked.retrieve().display();
            all_doc_ranked.findNext();
        }
        all_doc_ranked.retrieve().display();
        System.out.println("Displaying last document with scores...");
    }
    
    public static Document get_doc_given_id(int id) {
        return index1.get_document_given_id(id);
    }
    
    //عدد مرات تكرار الكلمة بالملف
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
    
    public static int get_doc_rank_score(Document document, String Query) {
        if (Query.isEmpty())
            return 0;
        
        String terms[]=Query.split("\\s+");
        int totalFreq=0;
        for(int i =0; i<terms.length; i++) //loops to all words and count word frequency
            totalFreq+= term_frequency_in_doc(document,terms[i].trim().toLowerCase());
        return totalFreq;
    }
    
        public static void RankQuery(String Query) {
            LinkedList<Integer> docIDs= new LinkedList<Integer>();
        if (Query.isEmpty())
            return;

        String[] terms = Query.split("\\s+"); //if there is more than one space
        boolean found=false;
        
        for (int i=0; i< terms.length; i++) {
            found = inverted.search_word_in_inverted(terms[i].trim().toLowerCase());
            if (found)
                docIDs = inverted.inverted_index.retrieve().doc_IDS;
            Adding_in_1_List_Sorted(docIDs);
            
        }
    }

    
    public static void Adding_in_1_List_Sorted (LinkedList<Integer> A) { //Addtpquerydoc
        if (A.empty()) 
            return;

        A.findFirst();
        while (!A.last()) {
            if (!existsIn_result(all_doc_in_query, A.retrieve()))
                insert_sortedList_ID(A.retrieve());
            
            A.findNext();
        }
        if (!existsIn_result(all_doc_in_query, A.retrieve()))
            insert_sortedList_ID(A.retrieve());


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
    
    public static void  insert_sortedList_ID(Integer id) {
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
            all_doc_in_query.insert(id);
            
    }
    
    public static void insert_sorted_list(){
        System.out.println("Debug1: Inserting into sorted list..."); //debug
        RankQuery(Query);//Finding all_doc_in_query
        System.out.println("Adding query"); //debug

        if (all_doc_in_query.empty()){
            System.out.println("No matches for this query.");
            return;
        }
            System.out.println("Debug2: Inserting into sorted list...");

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

            System.out.println("Debug4: Inserting into sorted list...");

    } 
    
        public static void insert_sorted_list (Doc_Rank documentRanked) {
            System.out.println("Debug1: Displaying documents with scores...");

            if (all_doc_ranked.empty()){
                all_doc_ranked.insert(documentRanked);
                return;
            }
            System.out.println("Debug2: Displaying documents with scores...");

            all_doc_ranked.findFirst();
            while(!all_doc_ranked.last()){
                if (documentRanked.rank>all_doc_ranked.retrieve().rank) {
                    Doc_Rank documentRanked1 = all_doc_ranked.retrieve();
                    all_doc_ranked.update(documentRanked);
                    all_doc_ranked.insert(documentRanked1);
                    return;
                }
                else
                    all_doc_ranked.findNext();
            }
            System.out.println("Debug3: Displaying documents with scores...");

            if (documentRanked.rank>all_doc_ranked.retrieve().rank) {
                Doc_Rank documentRanked1 = all_doc_ranked.retrieve();
                all_doc_ranked.update(documentRanked);
                all_doc_ranked.insert(documentRanked1);
                return;
            }
            else
                all_doc_ranked.insert(documentRanked);
            System.out.println("Debug4: Displaying documents with scores...");

        }
        




    
    
    
//}

//class DocRank {
//    int id;
//    int rank;
//
//    public DocRank(int i, int r) {
//        id = i;
//        rank = r;
//    }
//
//    public void display() {
//        System.out.printf("%-8d%-8d\n", id, rank);
//    }
//}
//
//public class Ranking {
//    static String Query;
//    static InvertedIndex inverted;
//    static index index1;
//    static LinkedList<Integer> QueryDocuments; //document ids that match the query terms
//    static LinkedList<DocRank> RankDocuments; //document ids with scores (ranked by score)
//
//    public Ranking(InvertedIndex inverted, index index1, String Query) {
//        this.inverted = inverted;
//        this.index1 = index1;
//        this.Query = Query;
//        QueryDocuments = new LinkedList<>();
//        RankDocuments = new LinkedList<>();
//    }
//
//    public static void display_all_doc_with_score_usingList() {
//        if (RankDocuments.empty()) {
//            System.out.println("No ranked documents found.");
//            return;
//        }
//        System.out.printf("%-8s%-8s\n", "DocID", "Score");
//        RankDocuments.findFirst();
//        while (!RankDocuments.last()) {
//            RankDocuments.retrieve().display();
//            RankDocuments.findNext();
//        }
//        RankDocuments.retrieve().display();
//    }
//
//    public static Document get_doc_given_id(int id) {
//        return index1.get_document_given_id(id);
//    }
//
//    public static int term_frequency_in_doc(Document d, String term) {
//        int freq = 0;
//        LinkedList<String> words = d.words;
//        if (words.empty()) return 0;
//
//        words.findFirst();
//        while (!words.last()) {
//            if (words.retrieve().equalsIgnoreCase(term)) freq++;
//            words.findNext();
//        }
//        if (words.retrieve().equalsIgnoreCase(term)) freq++;
//        return freq;
//    }
//
//    public static int get_doc_rank_score(Document d, String Query) {
//        if (Query.isEmpty()) return 0;
//
//        String[] terms = Query.split("\\s+");
//        int totalScore = 0;
//
//        for (String term : terms) {
//            totalScore += term_frequency_in_doc(d, term.trim().toLowerCase());
//        }
//        return totalScore;
//    }
//
//    public static void RankQuery(String Query) {
//        if (Query.isEmpty()) return;
//
//        String[] terms = Query.split("\\s+");
//        for (String term : terms) {
//            boolean found = inverted.search_word_in_inverted(term.trim().toLowerCase());
//            if (found) {
//                LinkedList<Integer> docIDs = inverted.inverted_index.retrieve().doc_IDS;
//                Add_To_QueryDocuments(docIDs);
//            }
//        }
//    }
//
//    public static void Add_To_QueryDocuments(LinkedList<Integer> docIDs) {
//        if (docIDs.empty()) return;
//
//        docIDs.findFirst();
//        while (!docIDs.last()) {
//            if (!existIn_Result(QueryDocuments, docIDs.retrieve())) {
//                QueryDocuments.insert(docIDs.retrieve());
//            }
//            docIDs.findNext();
//        }
//        if (!existIn_Result(QueryDocuments, docIDs.retrieve())) {
//            QueryDocuments.insert(docIDs.retrieve());
//        }
//    }
//
//    public static boolean existIn_Result(LinkedList<Integer> result, Integer id) {
//        if (result.empty()) return false;
//
//        result.findFirst();
//        while (!result.last()) {
//            if (result.retrieve().equals(id)) return true;
//            result.findNext();
//        }
//        return result.retrieve().equals(id);
//    }
//
//    public static void insert_sorted_in_list() {
//        RankQuery(Query);
//
//        if (QueryDocuments.empty()) {
//            System.out.println("No documents matched the query.");
//            return;
//        }
//
//        QueryDocuments.findFirst();
//        while (!QueryDocuments.last()) {
//            Document d = get_doc_given_id(QueryDocuments.retrieve());
//            int score = get_doc_rank_score(d, Query);
//            insert_sorted_list(new DocRank(QueryDocuments.retrieve(), score));
//            QueryDocuments.findNext();
//        }
//
//        Document d = get_doc_given_id(QueryDocuments.retrieve());
//        int score = get_doc_rank_score(d, Query);
//        insert_sorted_list(new DocRank(QueryDocuments.retrieve(), score));
//    }
//
//    public static void insert_sorted_list(DocRank documentRanked) {
//        if (RankDocuments.empty()) {
//            RankDocuments.insert(documentRanked);
//            return;
//        }
//
//        RankDocuments.findFirst();
//        while (!RankDocuments.last()) {
//            if (documentRanked.rank > RankDocuments.retrieve().rank) {
//                DocRank temp = RankDocuments.retrieve();
//                RankDocuments.update(documentRanked);
//                RankDocuments.insert(temp);
//                return;
//            }
//            RankDocuments.findNext();
//        }
//
//        if (documentRanked.rank > RankDocuments.retrieve().rank) {
//            DocRank temp = RankDocuments.retrieve();
//            RankDocuments.update(documentRanked);
//            RankDocuments.insert(temp);
//        } else {
//            RankDocuments.insert(documentRanked);
//        }
//    }
}
