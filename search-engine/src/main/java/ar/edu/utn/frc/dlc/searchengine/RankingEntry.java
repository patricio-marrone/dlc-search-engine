/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frc.dlc.searchengine;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author RF
 */
public class RankingEntry implements Comparable {
    
    private String title;
    private double acumulator;
    private String linkFile;
    private String author;
    private LinkedList<String> termsIncluded;  
    private String[] arrayTitle, arrayAuthor;
    private boolean flagAcuTitle, flagAcuAuthor;
    
    public RankingEntry(String title, long acumulator, String linkFile, String author) {
        this.title = title;
        this.acumulator = acumulator;
        this.linkFile = linkFile;
        this.author = author;
        this.termsIncluded = new LinkedList<String>();
        this.arrayTitle = this.cleanAndSplit(title);
        this.arrayAuthor = this.cleanAndSplit(author);     
    }

    public String[] cleanAndSplit(String token) {
        String[] cleanWords = token.toLowerCase().split("(?:[^\\w]|_)+");
        return cleanWords;
    }
    
    RankingEntry() {
        termsIncluded = new LinkedList<String>();
        
    }

    public void addTerm(String term){
        if(!termsIncluded.contains(term)) termsIncluded.add(term);
    }
    
    public LinkedList<String> getTermsIncluded() {
        return termsIncluded;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setTermsIncluded(LinkedList<String> termsIncluded) {
        this.termsIncluded = termsIncluded;
    }

    public String getTitle() {
        return title;
    }

    public double getAcumulator() {
        return acumulator;
    }

    public String getLinkFile() {
        return linkFile;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLinkFile(String linkFile) {
        this.linkFile = linkFile;
    }
    
    public void accumulate(double accumulate){
        this.acumulator = this.acumulator + accumulate * termsIncluded.size();
    }
    
    public String getTermsToString(){
        Iterator ite = termsIncluded.iterator();
        StringBuilder sb = new StringBuilder();
        while(ite.hasNext()){
            String term = (String) ite.next();
            sb.append(term);          
            if(ite.hasNext()) sb.append(", ");
        }
        return sb.toString();
    }

    public int compareTo(Object o) {       
        RankingEntry entry = (RankingEntry) o;
        if(entry.getAcumulator() - this.acumulator < 0)
            return -1;
        if(entry.getAcumulator() - this.acumulator > 0)
            return 1;
        return 0;      
    }  

    void accumulateByTitle(String term) {
        if(!flagAcuTitle){
            for(int i = 0; i < arrayTitle.length ; i++){
                if(arrayTitle[i].equals(term)){
                    accumulate(100);
                    flagAcuTitle = true;
                    return;
                }  
            }
        }          
    }

    void accumulateByAuthor(String term) {
        if(!flagAcuAuthor){    
            for(int i = 0; i < arrayAuthor.length ; i++){
                if(arrayAuthor[i].equals(term)){
                    accumulate(200);
                    flagAcuAuthor = true;
                    return;
                }              
            } 
        }
    }

    void generateArrayTitle() {
        this.arrayTitle = this.cleanAndSplit(title);       
    }

    void generateArrayAuthor() {
        this.arrayAuthor = this.cleanAndSplit(author);    
    }

    void accumulateByTerms() {
        this.acumulator = this.acumulator + 10 * termsIncluded.size();
    }
}
