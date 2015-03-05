/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frc.dlc.searchengine;

import ar.edu.utn.frc.dlc.searchengine.indexer.Dictionary;
import ar.edu.utn.frc.dlc.searchengine.sqlite.PostingEntry;
import ar.edu.utn.frc.dlc.searchengine.sqlite.Word;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author RF
 */
public class RankingBuilder {
    
    private DictionaryReader dictionary;
//    private String[] terms;
    private HashMap<Document, RankingEntry> rankingDocuments;
    private TreeSet<TermEntry> termEntries;
    private long totalDocsCount;
    private Map<Integer, Document> documentMap;
    
    public RankingBuilder(DictionaryReader dictionary, String[] terms){
        this.dictionary = dictionary;
        this.documentMap = dictionary.getDocumentMap();
        this.totalDocsCount = documentMap.size();
//        this.terms = terms;
        rankingDocuments = new HashMap<Document, RankingEntry>();
        termEntries = new TreeSet<TermEntry>();
        this.loadTermsAsEntries(terms); // incluye calculos de IDFs para c/term
        
//        Collections.sort(termEntries); // ordenamiento decreciente segun IDFs ***** TREE SET
        
        Iterator ite = termEntries.iterator();
        System.out.print("termEntries ordenadas decrecientes por IDFs: ");
        while(ite.hasNext()){
            TermEntry te = (TermEntry) ite.next();
            System.out.print("Term: ");
            System.out.print(te.getTerm());
            System.out.print(" - IDF: ");
            System.out.print(te.getIDF());
            if(ite.hasNext()) System.out.print(",");
            else System.out.println("");
        }      
    }
    
    private void loadTermsAsEntries(String[] terms){
        for(int i = 0 ; i < terms.length ; i++){       
//            if(termEntries.containsKey(terms[i])){
//                continue; // termino repetido  TREE SET
//            }            
            TermEntry entry = new TermEntry(terms[i]);         
            // no encuentra ninguna entrada en el HashMap con ese termino
//            Map<String,Word> map = this.dictionary.getWordCounts();
//            Word termino = map.get(terms[i]);
//            if(termino == null) continue; // agregado por falta de filtro en la query de entrada
//            Integer cantDocToTerm = termino.getCount();
            
            Integer cantDocToTerm = this.dictionary.getWordPostingCount(terms[i]);            
            entry.calculateIDF(totalDocsCount, cantDocToTerm);
            termEntries.add(entry);
        }
    }
    
    public LinkedList<RankingEntry> getRankingEntries(){
//        double IDF = 0;
        LinkedList<RankingEntry> rankingEntries = new LinkedList<RankingEntry>();           
//        Collection<TermEntry> termsCollection = termEntries.values();        
        Iterator termsIterator = termEntries.iterator();
        while(termsIterator.hasNext()){
            TermEntry termEntry = (TermEntry) termsIterator.next();
            Iterator<PostingEntry> entries = dictionary.getPostingIterator(termEntry.getTerm());
            while(entries.hasNext()) {
                PostingEntry entry = entries.next();
                if(rankingDocuments.containsKey(entry.getDocument())){
                    // weight = TF * IDF
                    rankingDocuments.get(entry.getDocument()).accumulate((entry.getFrequency()*termEntry.getIDF()));                                        
                    rankingDocuments.get(entry.getDocument()).accumulateByTitle(termEntry.getTerm());
                    rankingDocuments.get(entry.getDocument()).accumulateByAuthor(termEntry.getTerm());                   
                    rankingDocuments.get(entry.getDocument()).addTerm(termEntry.getTerm());
                }else{
                    RankingEntry rankingEntry = new RankingEntry();
                    rankingEntry.addTerm(termEntry.getTerm());
                    // weight = TF * IDF
                    rankingEntry.accumulate((long) (entry.getFrequency()*termEntry.getIDF()));
                    rankingEntry.setLinkFile(entry.getDocument().getPath());
                    rankingEntry.setTitle(documentMap.get(entry.getDocumentCode()).getTitle());
                    rankingEntry.setAuthor(documentMap.get(entry.getDocumentCode()).getAuthor());
                    rankingEntry.generateArrayTitle();
                    rankingEntry.generateArrayAuthor();
                    rankingEntry.accumulateByTitle(termEntry.getTerm());
                    rankingEntry.accumulateByAuthor(termEntry.getTerm());
                    rankingDocuments.put(entry.getDocument(), rankingEntry);
                }
            } 
        }
        
        Iterator rankingIterator = rankingDocuments.entrySet().iterator();
        while(rankingIterator.hasNext()){
            Map.Entry e = (Map.Entry) rankingIterator.next();
            RankingEntry entry = (RankingEntry) e.getValue();
            if(entry.getAcumulator()!=0){
                // Plus acumulator for query terms included
                entry.accumulateByTerms(); // according to termsIncluded.size()
                rankingEntries.add(entry);
            }         
        }       
        
        Collections.sort(rankingEntries);
        return rankingEntries;
    }
    
    private LinkedList<RankingEntry> heapSortRankingEntries(LinkedList<RankingEntry> rankingEntries){
        /* Insertion onto heap */
        for (int heapsize=0; heapsize<rankingEntries.size(); heapsize++) {
            /* Step one in adding an element to the heap in the
             * place that element at the end of the heap array-
             * in this case, the element is already there. */
            int n = heapsize; // the index of the inserted int
            while (n > 0) { // until we reach the root of the heap
                int p = (n-1)/2; // the index of the parent of n
                if (rankingEntries.get(n).getAcumulator() > rankingEntries.get(p).getAcumulator()) { // child is larger than parent
                    RankingEntry aux = rankingEntries.get(n);
                    rankingEntries.add(n,rankingEntries.get(p));
                    rankingEntries.add(p,aux); // swap child with parent
                    n = p; // check parent
                }
                else // parent is larger than child
                    break; // all is good in the heap
            }
        }
 
        /* Removal from heap */
        for (int heapsize=rankingEntries.size(); heapsize>0;) {
            RankingEntry aux = rankingEntries.get(0);
            rankingEntries.add(0,rankingEntries.get(--heapsize));
            rankingEntries.add(--heapsize,aux); // swap root with the last heap element

            int n = 0; // index of the element being moved down the tree
            while (true) {
                int left = (n*2)+1;
                if (left >= heapsize) // node has no left child
                    break; // reached the bottom; heap is heapified
                int right = left+1;
                if (right >= heapsize) { // node has a left child, but no right child
                    if (rankingEntries.get(left).getAcumulator() > rankingEntries.get(n).getAcumulator()){ // if left child is greater than node
                        RankingEntry aux2 = rankingEntries.get(left);
                        rankingEntries.add(left,rankingEntries.get(n));
                        rankingEntries.add(n,aux2); // swap left child with node
                    }
                    break; // heap is heapified
                }
                if (rankingEntries.get(left).getAcumulator() > rankingEntries.get(n).getAcumulator()) { // (left > n)
                    if (rankingEntries.get(left).getAcumulator() > rankingEntries.get(right).getAcumulator()) { // (left > right) & (left > n)
                        RankingEntry aux2 = rankingEntries.get(left);
                        rankingEntries.add(left,rankingEntries.get(n));
                        rankingEntries.add(n,aux2);
                        n = left; continue; // continue recursion on left child
                    } else { // (right > left > n)
                        RankingEntry aux2 = rankingEntries.get(right);
                        rankingEntries.add(right,rankingEntries.get(n));
                        rankingEntries.add(n,aux2);
                        n = right; continue; // continue recursion on right child
                    }
                }else { // (n > left)
                    if (rankingEntries.get(right).getAcumulator() > rankingEntries.get(n).getAcumulator()) { // (right > n > left)
                        RankingEntry aux2 = rankingEntries.get(right);
                        rankingEntries.add(right,rankingEntries.get(n));
                        rankingEntries.add(n,aux2);
                        n = right; continue; // continue recursion on right child
                    }else { // (n > left) & (n > right)
                        break; // node is greater than both children, so it's heapified
                    }
                }
            }
        }
        return rankingEntries;
    }  
}
