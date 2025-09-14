package org.example;

public class Equations {
Variables body [] = new Variables[3];
final double G = 6.67430*Math.pow(10, -11);

public double distancesqr (Variables a, Variables b){
    for (int i = 0; i< 3; i++){
    double total =  Math.pow(a.getR()[i]-b.getR()[i],2);
    total = total + total;
    }
   return total;

}
public double gravity(Variables a, Variables b){
        double size = (G*a.getM()*b.getM())/distancesqr(a,b);


    }

}
