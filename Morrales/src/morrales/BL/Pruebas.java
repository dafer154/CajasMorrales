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
        ResolverProblema solver = new ResolverProblema("src/Morrales/Pruebas/20 cajas 4 morrales.txt");
        //solver.resolverCantMorrales(8);
        solver.cantOptimaMorrales = 4;
        solver.resolverDistribucionEq(7);
        
    }
    
}
