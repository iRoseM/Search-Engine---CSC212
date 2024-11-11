
package csc212project;
public class BST_Main {
    public static void main (String[]args) {
        BST<Double>bt=new BST<Double> () ;
        System.out.println("is empty ? :"+bt.empty());
        System.out.println("----------------------------------");
        System.out.println("inserting 11 nodes");
        bt.insert ("A", 35.0) ;
        bt.insert ("B", 14.0) ;
        bt.insert ("B", 5.0) ;
        bt.insert ("D", 33.0) ;
        bt.insert ("E", 53.0) ;
        bt.insert ("C", 50.0) ;
        bt.insert ("F", 44.0) ;
        bt.insert ("G", 40.0) ;
        bt.insert ("H", 58.0) ;
        bt.insert ("I", 55.0) ;
        bt.insert ("I", 56.0) ;

        System.out.println ("All nodes "); 
        bt.inOrder();
        
        
        
    }
}
