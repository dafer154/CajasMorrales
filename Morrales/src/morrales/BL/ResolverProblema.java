/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morrales.BL;

import morrales.DAL.DAL;
import java.util.ArrayList;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

/**
 *
 * @author Edwin Gamboa
 */
public class ResolverProblema {

    LpSolve solver;
    ArrayList<Double> propiedades;
    int cantidadVariables, cantidadCajas;

    public ResolverProblema(String rutaProblema) {         
        DAL dal = new DAL();
        this.propiedades = dal.leerTextoArchivo(rutaProblema);
        this.cantidadCajas = propiedades.get(0).intValue();
        this.cantidadVariables = cantidadCajas + cantidadCajas * cantidadCajas;
    }

    public void crearSolver() {
        try {
            solver = LpSolve.makeLp(0, cantidadVariables);
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
    }

    public void agregarFuncionObjetivo() {
        double[] fila = new double[cantidadVariables + 1];

        for (int i = 1; i <= cantidadCajas; i++) {
            fila[i] = 1;
        }
        try {
            solver.setObjFn(fila);
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
    }

    public void agregarRestricciones() {
        try {
            //Las cajas solo va en un morral
            double[] restriccion = new double[cantidadVariables + 1];

            for (int i = 1; i <= cantidadCajas; i++) {
                int salto = i * cantidadCajas + 1;
                for (int j = salto; j <= salto + 1; j++) {
                    restriccion[j] = 1;
                }
                solver.addConstraint(restriccion, LpSolve.EQ, 1);
                restriccion = new double[cantidadVariables + 1];
            }

            //Control de morrales vacios
            restriccion = new double[cantidadVariables + 1];

            for (int i = 1; i <= cantidadCajas; i++) {
                int salto = i + cantidadCajas;
                restriccion[i] = -10000000;
                for (int j = salto; j <= cantidadVariables; j += cantidadCajas) {
                    restriccion[j] = 1;
                }
                solver.addConstraint(restriccion, LpSolve.LE, 0);
                restriccion = new double[cantidadVariables + 1];
            }
            
            //Las cajas que se llevan en cada morral no puede exceder el volumen 
            //y el peso que soportan los morrales
            restriccion = new double[cantidadVariables+1];
            double[] restriccion_peso = new double[cantidadVariables+1];
            int contador=3;
            for (int i = 1; i <= cantidadCajas; i++) {
                int salto = i+cantidadCajas;
                 for (int j = salto; j <= cantidadVariables; j+=cantidadCajas) {
                    restriccion[j] = propiedades.get(contador);
                    restriccion_peso[j] = propiedades.get(contador+1);
                    contador+=2;
                } 
                 contador=3;
                solver.addConstraint(restriccion, LpSolve.LE, propiedades.get(1));
                solver.addConstraint(restriccion_peso, LpSolve.LE, propiedades.get(2));
                restriccion = new double[cantidadVariables+1];
                restriccion_peso = new double[cantidadVariables+1];
            }

        } catch (LpSolveException e) {
            e.printStackTrace();
        }

        //addConstraint(double[] row, int constrType, double rh) 
    }

    public void resolver() {
        try {
            solver.solve();
            solver.writeLp("src/lp.lp");
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
    }

    public void setVariablesBinarias() {
        //Variables binarias
        int numColumns = solver.getNcolumns();
        for (int i = 1; i <= numColumns; i++) {
            try {
                solver.setBinary(i, true);
            } catch (LpSolveException e) {
                e.printStackTrace();
            }

        }
    }
}
