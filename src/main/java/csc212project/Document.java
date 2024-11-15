
package csc212project;


public class Document {
    LinkedList<String>words= new LinkedList<>();
    int id;
    
    public Document (int id, LinkedList<String>words){
        this.id=id;
        this.words=words;
    }

    public int getId() {
        return id;
    }
    
    
    }
