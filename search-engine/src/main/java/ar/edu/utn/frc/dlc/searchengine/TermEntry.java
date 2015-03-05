/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frc.dlc.searchengine;

/**
 *
 * @author RF
 */
public class TermEntry implements Comparable {
    
    private double IDF;
    private String term;
    
    public TermEntry(){}

    public TermEntry(String term) {
        this.term = term;
    }

    public TermEntry(double IDF, String term) {
        this.IDF = IDF;
        this.term = term;
    }

    public void calculateIDF(long totalDocsCount, long termDocsCount){
        if(termDocsCount != 0){
            this.IDF = (double)Math.log10(totalDocsCount/termDocsCount);
            return;
        }
        this.IDF = 0;
    }
    
    public double getIDF() {
        return IDF;
    }

    public String getTerm() {
        return term;
    }

    public void setIDF(double IDF) {
        this.IDF = IDF;
    }

    public void setTerm(String term) {
        this.term = term;
    }   

    public int compareTo(Object o) {
        TermEntry entry = (TermEntry) o;
        if(entry.getTerm().compareTo(this.term)!=0){
            if((entry.getIDF() - this.getIDF()) < 0){
                return -1;
            }
            else {
                return 1;
            }
        }
       return 0;
    }

}
