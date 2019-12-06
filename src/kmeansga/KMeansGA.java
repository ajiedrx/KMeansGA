/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeansga;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ajied
 */
public class KMeansGA extends JPanel implements Runnable {
    static Data data = new Data();
    ArrayList<Integer> fitness = new ArrayList<>();
    ArrayList<Integer> bestFitness = new ArrayList<>();
    static int GENERASI = 0;
    
    public void runGA() throws IOException {
        data.countFitness();
        data.roulette();
        data.crossoverCheck();
        data.mutation();
        data.elitism();
        for(int i = 0; i < data.individu.size(); i++){
            fitness.add(data.individu.get(i).getFitness());
            data.individu2n.clear();
        }
        bestFitness.add(Collections.max(fitness));
        fitness.clear();
    }
    
    public static void main(String[] args) throws IOException {
        data = new Data();
        Scanner input = new Scanner(System.in);
        KMeansGA go = new KMeansGA();
        data.readFile("C:/ruspini.csv");
        data.checkMinMax();
        System.out.print("Masukkan jumlah individu : " );
        int x = input.nextInt();
        System.out.print("Masukkan jumlah kromosom : " );
        int a = input.nextInt();
        System.out.print("Masukkan jumlah generasi : " );
        GENERASI = input.nextInt();
        data.generatePopulation(x, a);
        JFrame frame = new JFrame("Points");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(go);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Thread t = new Thread(go);
        t.start();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        
        int sumbux = 20;
        int sumbuy = 440;
        g2d.setColor(Color.black);
        g2d.drawLine(sumbux, sumbuy, sumbux, 10);
        g2d.drawLine(sumbux, sumbuy, 450, sumbuy);
        
        for(int i = 0; i < data.data.size(); i++){
            int x = (int)data.data.get(i).getX()* 2;
            int y = (int)data.data.get(i).getY() * 2;
            g2d.setColor(Color.blue);
            g2d.fillRect(sumbux+x, sumbuy-y, 3,3);
        }
        
        for(int i = 0; i < data.k; i++){
            int x = data.individu.get(i).getKromosom().get(i*2) * 2;
            int y = data.individu.get(i).getKromosom().get(i*2+1) * 2;
            g2d.setColor(Color.red);
            g2d.fillRect(sumbux+x, sumbuy-y, 3,3);
        }
    }

    @Override
    public void run() {
        int i = 0;
        while(i < GENERASI){
            try {
                runGA();
                repaint();    
                i++;
                System.out.println(i);
                Thread.sleep(80);
            } catch (IOException ex) {
                Logger.getLogger(KMeansGA.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(KMeansGA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FitnessGraph fg = new FitnessGraph("Fitness Chart", "Fitness Chart", bestFitness);
        fg.pack();
        fg.setVisible(true);
    }
}
