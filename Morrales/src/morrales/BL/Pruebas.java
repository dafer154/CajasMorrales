/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morrales.BL;

import java.util.ArrayList;

/**
 *
 * @author Edwin Gamboa
 */
public class Pruebas {
    
    public static void main(String[] args) {
        ArrayList propiedades = new ArrayList<Double>();
        propiedades.add(2.0);
        propiedades.add(8.0);
        propiedades.add(15.0);
        propiedades.add(6.0);
        propiedades.add(10.0);
        propiedades.add(2.0);
        propiedades.add(5.0);
        
        System.out.print("hola");
        
        ResolverProblema solver = new ResolverProblema("src/2_8_15.txt");
        solver.resolver();
        
    }
    
}
