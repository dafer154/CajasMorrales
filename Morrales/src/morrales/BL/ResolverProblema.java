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
    ArrayList<Object> distribucion;
    int cantidadVariables, cantidadCajas;
    long cantIteraciones, cantNodos;
    double MGrande = 1000000;
    String mensajeResultado = "Por definir, pero ya todo funciona";
    double cantOptimaMorrales;
    long tiempoEjecucion;

    public ArrayList<Double> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(ArrayList<Double> propiedades) {
        this.propiedades = propiedades;
    }

    public ArrayList<Object> getDistribucion() {
        return distribucion;
    }

    public void setDistribucion(ArrayList<Object> distribucion) {
        this.distribucion = distribucion;
    }

    public int getCantidadCajas() {
        return cantidadCajas;
    }

    public void setCantidadCajas(int cantidadCajas) {
        this.cantidadCajas = cantidadCajas;
    }

    public double getCantOptimaMorrales() {
        return cantOptimaMorrales;
    }

    public void setCantOptimaMorrales(double cantOptimaMorrales) {
        this.cantOptimaMorrales = cantOptimaMorrales;
    }

    public long getCantIteraciones() {
        return cantIteraciones;
    }

    public long getCantNodos() {
        return cantNodos;
    }

    public int getCantidadVariables() {
        return cantidadVariables;
    }

    public long getTiempoEjecucion() {
        return tiempoEjecucion;
    }
          
    public ResolverProblema(String rutaProblema) {
        DAL dal = new DAL();
        this.propiedades = dal.leerTextoArchivo(rutaProblema);
        this.cantidadCajas = propiedades.get(0).intValue();
        this.cantidadVariables = cantidadCajas + cantidadCajas * cantidadCajas;
        distribucion = new ArrayList<Object>();
        crearSolver();

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
            int salto, cotaSuperior;
            for (int i = 1; i <= cantidadCajas; i++) {
                salto = i * cantidadCajas + 1;
                cotaSuperior = cantidadCajas + salto - 1;
                for (int j = salto; j <= cotaSuperior; j++) {
                    restriccion[j] = 1;
                }
                solver.addConstraint(restriccion, LpSolve.EQ, 1);
                restriccion = new double[cantidadVariables + 1];
            }

            //Las cajas que se llevan en cada morral no puede exceder el volumen 
            //y el peso que soportan los morrales
            restriccion = new double[cantidadVariables + 1];
            double[] restriccion_peso = new double[cantidadVariables + 1];
            int contador = 3;
            for (int i = 1; i <= cantidadCajas; i++) {
                salto = i + cantidadCajas;
                for (int j = salto; j <= cantidadVariables; j += cantidadCajas) {
                    restriccion[j] = propiedades.get(contador);
                    restriccion_peso[j] = propiedades.get(contador + 1);
                    contador += 2;
                }
                contador = 3;
                solver.addConstraint(restriccion, LpSolve.LE, propiedades.get(1));
                solver.addConstraint(restriccion_peso, LpSolve.LE, propiedades.get(2));
                restriccion = new double[cantidadVariables + 1];
                restriccion_peso = new double[cantidadVariables + 1];
            }

            //Control de morrales vacios
            restriccion = new double[cantidadVariables + 1];

            for (int i = 1; i <= cantidadCajas; i++) {
                salto = i + cantidadCajas;
                restriccion[i] = -MGrande;
                for (int j = salto; j <= cantidadVariables; j += cantidadCajas) {
                    restriccion[j] = 1;
                }
                solver.addConstraint(restriccion, LpSolve.LE, 0);
                restriccion = new double[cantidadVariables + 1];
            }

        } catch (LpSolveException e) {
            e.printStackTrace();
        }

        //addConstraint(double[] row, int constrType, double rh) 
    }

    public String resolver() {
        try {
            
            agregarRestricciones();            
            agregarFuncionObjetivo();
            setVariablesBinarias();
            //solver.writeLp("src/lp.lp");
            //solver.setBbRule(LpSolve.NODE_FIRSTSELECT);
            long time_start;
            time_start = System.currentTimeMillis();
            solver.solve();
            tiempoEjecucion = System.currentTimeMillis() - time_start;
            System.out.println("Tiempo de ejecución: " + tiempoEjecucion);
            //solver.printLp();
            //solver.printSolution(1);
            //solver.printObjective();
            //solver.printConstraints(1);
            cantIteraciones = solver.getTotalIter();
            cantNodos = solver.getTotalNodes();
            cantOptimaMorrales = solver.getObjective();

            int cont=0, indicePrimerMorral = 0, indiceVol;
            double cajasLlevadasTemp;
            double[] row = new double[4 * cantidadCajas + 1];
            double[] columns;
            int indiceCaja=0, indicePrimerCoeficientes=cantidadCajas;
            String cajas="";
            solver.getConstraints(row);
            
            indicePrimerMorral = 3 * cantidadCajas;
            
            for (int i = indicePrimerMorral; i <= 4 * cantidadCajas; i++) {
                cajasLlevadasTemp = row[i];
                if (cajasLlevadasTemp < 0) {
                    indiceVol = indicePrimerMorral - 2*cantidadCajas + 2*cont;
                    columns = solver.getPtrVariables();
                    distribucion.add(cajasLlevadasTemp + MGrande);
                    distribucion.add(row[indiceVol]);
                    distribucion.add(row[indiceVol + 1]); 
                    for (int j = indicePrimerCoeficientes; j < columns.length; j+= cantidadCajas) {
                        indiceCaja++;
                        if(columns[j] > 0){
                            cajas+= indiceCaja +"  ";                            
                        }
                    }
                    distribucion.add(cajas);
                    indiceCaja=0;
                    cajas="";                    
                }                
                indicePrimerCoeficientes++;
                cont++;
            }
            solver.deleteLp();

            /*for (int i = cantidadCajas; i <= 2 * cantidadCajas; i += 2) {
                volTemp = row[i];
                if (volTemp > 0) {
                    pesoTemp = row[i + 1];
                    cajasLlevadasTemp = row[indicePrimerMorral+cont] + MGrande;
                    distribucion.add(cajasLlevadasTemp);
                    distribucion.add(volTemp);
                    distribucion.add(pesoTemp);                    
                }
                cont++;
            }*/
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
        return mensajeResultado;
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
    
    public void setReglaBB(int codigoRegla){
        switch (codigoRegla) {            
            case 0:  solver.setBbRule(solver.NODE_FIRSTSELECT);
                     break;
            case 1:  solver.setBbRule(solver.NODE_GAPSELECT);
                     break;
            case 2:  solver.setBbRule(solver.NODE_RANGESELECT);
                     break;
            case 3:  solver.setBbRule(solver.NODE_FRACTIONSELECT);
                     break;
            case 4:  solver.setBbRule(solver.NODE_PSEUDOCOSTSELECT);
                     break;
            case 5:  solver.setBbRule(solver.NODE_PSEUDONONINTSELECT);
                     break;
            case 6:  solver.setBbRule(solver.NODE_PSEUDORATIOSELECT);
                     break;
            case 7:  solver.setBbRule(solver.NODE_USERSELECT);
                     break;            
            default: solver.setBbRule(solver.NODE_PSEUDONONINTSELECT 
                    + solver.NODE_GREEDYMODE + solver.NODE_DYNAMICMODE 
                    + solver.NODE_RCOSTFIXING);
                     break;
        }
    }
}
