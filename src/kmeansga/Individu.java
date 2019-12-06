/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeansga;

import java.util.ArrayList;

/**
 *
 * @author ajied
 */
public class Individu {
    ArrayList<Integer> kromosom = new ArrayList<>();
    int fitness;

    public ArrayList<Integer> getKromosom() {
        return kromosom;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}
