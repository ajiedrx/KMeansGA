/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeansga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *
 * @author ajied
 */
public class Data {
    double minX,minY,maxX,maxY;
    int n,k;
    Random random = new Random();
    ArrayList<Point> data = new ArrayList<>();
    ArrayList<Double> X = new ArrayList<>();
    ArrayList<Double> Y = new ArrayList<>();
    ArrayList<Individu> individu = new ArrayList<>();
    ArrayList<Double> distances = new ArrayList<>();
    ArrayList<Double> minDistances = new ArrayList<>();
    ArrayList<Individu> individu2n = new ArrayList<>();
    ArrayList<Individu> parent = new ArrayList<>();
    ArrayList<Individu> offspring = new ArrayList<>();
    ArrayList<Double> rouletteProbs = new ArrayList<>();
    double pCROSSOVER = 0.9;
    double pMUTATION = 0.1;
    
    public void readFile(String fileName) throws IOException {
        int[] dataTemp = new int[225];
        int[][] dataSet = new int[75][3];
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        try {
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(",");
                line = br.readLine();
            }
            StringTokenizer st = new StringTokenizer(sb.toString(),",");
            for(int i=0;i<dataTemp.length;i++){
                dataTemp[i] = Integer.parseInt(st.nextToken().replaceAll("\n",""));
            }
            for (int i = 0, k=0; i < 75; i++)
                for (int jl = 0; jl < 3; jl++)
                    dataSet[i][jl] = dataTemp[k++];
         } finally {
            br.close();
        }
        for(int i = 0; i < dataSet.length; i++){
            data.add(new Point(dataSet[i][0],dataSet[i][1]));        
        }
    }
    
    public void checkMinMax(){
        for(int i = 0; i < data.size(); i++){
            X.add(data.get(i).getX());
            Y.add(data.get(i).getY());
        }
        maxX = Collections.max(X);
        maxY = Collections.max(Y);
        minX = Collections.min(X);
        minY = Collections.min(Y);
    }
    
    public void generatePopulation(int n, int k){
        this.n = n;
        this.k = k;
        for(int i = 0; i < n; i++){
            individu.add(new Individu());
            for(int j = 0; j < k; j++){
//                individu.get(i).getKromosom().add(j);
                int a = random.nextInt(((int)maxX - (int)minX) +1 ) + (int)minX;
                individu.get(i).getKromosom().add(a);
                int b = random.nextInt(((int)maxY - (int)minY) +1 ) + (int)minY;
                individu.get(i).getKromosom().add(b);
            }
        }
    }
    
    public void countFitness(){
        for(int i = 0; i < individu.size(); i++){
            double sum = 0;
            for(int j = 0; j < data.size(); j++){
                double a = data.get(j).getX();
                double b = data.get(j).getY();
                for(int k = 0; k < individu.get(i).getKromosom().size(); k++){
                    double x = individu.get(i).getKromosom().get(k);
                    double y = individu.get(i).getKromosom().get(k+1);
                    distances.add(getDistance(x,y,a,b));
                    k++;
                }
                minDistances.add(Collections.min(distances));
                distances.clear();
            }
            for(int l = 0; l < minDistances.size(); l++){
                sum += minDistances.get(l);
            }
            minDistances.clear();
            individu.get(i).setFitness(10000-(int)sum);
        }
    }
    
    public double getDistance(double x, double y, double a, double b){
        return Math.sqrt(((x - a)*(x - a))+((y - b)*(y - b)));
    }
    
    public void roulette(){
        int min = 0;
        double prevProb = 0;
        double sumOfFitness = 0;
        double count = 0;
        for(int i = 0; i < individu.size(); i++){
            sumOfFitness += individu.get(i).getFitness();
        }
        for(int j = 0; j < individu.size(); j++){
            count = prevProb + individu.get(j).getFitness();
            rouletteProbs.add(count);
            prevProb = count;
        }
        int index;
        for(int i = 0; i < individu.size(); i++){
            index = 0;
            int a = random.nextInt((int) ((sumOfFitness - min) +1)) + min;
            parent.add(new Individu());
            for(int j = 0; j<sumOfFitness; j++){
                if(j == rouletteProbs.get(index))
                    index++;
                if(a == j)
                    break;
            }
                parent.get(i).getKromosom().addAll(individu.get(index).getKromosom());
                parent.get(i).setFitness(individu.get(index).getFitness());
        }
        rouletteProbs.clear();
    }
    
    public void crossoverCheck(){
        int temp = 0;
        int temp2 = 0;
        for(int i = 0; i < parent.size()/2; i++){
            double p = random.nextDouble();
            if(p < pCROSSOVER){
                int kiri = 0 + random.nextInt((parent.get(i).getKromosom().size()-1 - 0) + 1);
                int kanan = kiri + random.nextInt((parent.get(i).getKromosom().size() - kiri) + 1);    
//                System.out.println(kanan+" "+kiri);
                for(int j = kiri; j < kanan; j++){
                    temp = parent.get(i).getKromosom().get(j);
                    temp2 = parent.get(i+1).getKromosom().get(j);
                    parent.get(i).getKromosom().set(j, temp2);
                    parent.get(i+1).getKromosom().set(j, temp);
                }
            }
        }
        for(int k = 0; k < parent.size(); k++){
            offspring.add(new Individu());
            offspring.get(k).getKromosom().addAll(parent.get(k).getKromosom());
        }
    }
    
    public void mutation(){
        for(int i = 0; i < offspring.size(); i++){
            for(int j = 0; j < offspring.get(i).getKromosom().size(); j++){
                double p = random.nextDouble();
                double mut = random.nextDouble();
                if(p < pMUTATION) {
                    if(i % 2 == 0){
                        if(offspring.get(i).getKromosom().get(j) == maxX){
                            offspring.get(i).getKromosom().set(j, offspring.get(i).getKromosom().get(j)-5);
                        }
                        if(offspring.get(i).getKromosom().get(j) == minX){
                            offspring.get(i).getKromosom().set(j, offspring.get(i).getKromosom().get(j)+5);
                        }
                        if(mut > 0.5 && offspring.get(i).getKromosom().get(j) < maxX)
                            offspring.get(i).getKromosom().set(j, offspring.get(i).getKromosom().get(j)+5);
                        if(mut < 0.5 && offspring.get(i).getKromosom().get(j) > minX)
                            offspring.get(i).getKromosom().set(j, offspring.get(i).getKromosom().get(j)-5);
                    }
                    else {
                        if(offspring.get(i).getKromosom().get(j) == maxY){
                            offspring.get(i).getKromosom().set(j, offspring.get(i).getKromosom().get(j)-5);
                        }
                        if(offspring.get(i).getKromosom().get(j) == minY){
                            offspring.get(i).getKromosom().set(j, offspring.get(i).getKromosom().get(j)+5);
                        }
                        if(mut > 0.5 && offspring.get(i).getKromosom().get(j) < maxY)
                            offspring.get(i).getKromosom().set(j, offspring.get(i).getKromosom().get(j)+5);
                        else if(mut < 0.5 && offspring.get(i).getKromosom().get(j) > minY)
                            offspring.get(i).getKromosom().set(j, offspring.get(i).getKromosom().get(j)-5);
                    }
                }
            }
        }
    }
    
    public void elitism(){
        fitnessEvaluation();
        individu2n.addAll(parent);
        individu2n.addAll(offspring);
        Collections.sort(individu2n, Collections.reverseOrder(Comparator.comparingInt(h -> h.getFitness())));
        individu.clear();
        for(int i = 0; i < n; i++){
           individu.add(new Individu());
           individu.get(i).getKromosom().addAll(individu2n.get(i).getKromosom());
           individu.get(i).setFitness(individu2n.get(i).getFitness());
        }
        parent.clear();
        offspring.clear();
    }
    
    public void fitnessEvaluation() {
        for(int i = 0; i < parent.size(); i++){
            double sum = 0;
            for(int j = 0; j < data.size(); j++){
                double a = data.get(j).getX();
                double b = data.get(j).getY();
                for(int k = 0; k < parent.get(i).getKromosom().size(); k++){
                    double x = parent.get(i).getKromosom().get(k);
                    double y = parent.get(i).getKromosom().get(k+1);
                    distances.add(getDistance(x,y,a,b));
                    k++;
                }
                minDistances.add(Collections.min(distances));
                distances.clear();
            }
            for(int l = 0; l < minDistances.size(); l++){
                sum += minDistances.get(l);
            }
            minDistances.clear();
            parent.get(i).setFitness(10000-(int)sum);
        }
        
        for(int i = 0; i < offspring.size(); i++){
            double sum = 0;
            for(int j = 0; j < data.size(); j++){
                double a = data.get(j).getX();
                double b = data.get(j).getY();
                for(int k = 0; k < offspring.get(i).getKromosom().size(); k++){
                    double x = offspring.get(i).getKromosom().get(k);
                    double y = offspring.get(i).getKromosom().get(k+1);
                    distances.add(getDistance(x,y,a,b));
                    k++;
                }
                minDistances.add(Collections.min(distances));
                distances.clear();
            }
            for(int l = 0; l < minDistances.size(); l++){
                sum += minDistances.get(l);
            }
            minDistances.clear();
            offspring.get(i).setFitness(10000-(int)sum);
        }
    }
    
    public ArrayList<Individu> getIndividu() {
        return individu;
    }
}
