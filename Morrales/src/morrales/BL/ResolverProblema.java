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
    ArrayList distribucionCantMorrales, distribucionOptima;
    int cantidadVariables, cantidadCajas, cantVariablesCantMorrales, 
            cantVariablesDistribucion;
    long cantIteracionesCantMorrales, cantNodosCantMorrales,
            cantIteracionesDistribucion, cantNodosDistribucion;
    double MGrande = 1000000;
    String mensajeResultado = "Por definir, pero ya todo funciona";
    double cantOptimaMorrales, valorFunObjetivoDistribucion;
    long tiempoEjecucionCantMorrales, tiempoEjecucionDistribucion;

    public LpSolve getSolver() {
        return solver;
    }

    public ArrayList<Double> getPropiedades() {
        return propiedades;
    }

    public ArrayList getDistribucionCantMorrales() {
        return distribucionCantMorrales;
    }

    public ArrayList getDistribucionOptima() {
        return distribucionOptima;
    }

    public int getCantidadVariables() {
        return cantidadVariables;
    }

    public int getCantidadCajas() {
        return cantidadCajas;
    }

    public long getCantIteracionesCantMorrales() {
        return cantIteracionesCantMorrales;
    }

    public long getCantNodosCantMorrales() {
        return cantNodosCantMorrales;
    }

    public long getCantIteracionesDistribucion() {
        return cantIteracionesDistribucion;
    }

    public long getCantNodosDistribucion() {
        return cantNodosDistribucion;
    }

    public double getCantOptimaMorrales() {
        return cantOptimaMorrales;
    }

    public double getValorFunObjetivoDistribucion() {
        return valorFunObjetivoDistribucion;
    }

    public long getTiempoEjecucionCantMorrales() {
        return tiempoEjecucionCantMorrales;
    }

    public long getTiempoEjecucionDistribucion() {
        return tiempoEjecucionDistribucion;
    }

    public int getCantVariablesCantMorrales() {
        return cantVariablesCantMorrales;
    }

    public int getCantVariablesDistribucion() {
        return cantVariablesDistribucion;
    }
 
    public ResolverProblema(String rutaProblema) {
        DAL dal = new DAL();
        this.propiedades = dal.leerTextoArchivo(rutaProblema);
        this.cantidadCajas = propiedades.get(0).intValue();
        distribucionCantMorrales = new ArrayList();
        distribucionOptima = new ArrayList();
    }


    public void agregarFuncionObjetivoCantMorrales() {
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
    
    public void agregarFuncionObjetivoDistribucionEq(int cantMorrales) {
        try {
            //Necesito cantidadMorrales-1 variables para la f.o, más las de cada caja            
            double[] fila = new double[cantidadVariables + 1];
            for (int i = 0; i < cantMorrales; i++) {
                fila[i] = 1;
            }

            double[] restriccion = new double[cantidadVariables + 1];
            int contador;
                
            for (int i = 1; i < cantMorrales; i++) {
                contador = 4;
                int cotaSuperior = cantidadVariables-cantMorrales+1;
                for (int j = 4; j <= cotaSuperior; j += cantMorrales) {
                    restriccion[i] = -1;
                    restriccion[j] = propiedades.get(contador);
                    restriccion[j+i] = -propiedades.get(contador);
                    contador += 2;
                }  
                solver.addConstraint(restriccion, LpSolve.LE, 0);
                restriccion = new double[cantidadVariables + 1];
            }
            solver.setObjFn(fila);
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
    }
    
    public void armarRestirccionCajaUnMorral(int cantMorrales, int cantVariablesFO){
        try{
            //Las cajas solo va en un morral
            double[] restriccion = new double[cantidadVariables + 1];
            int salto, cotaSuperior;
            for (int i = 1; i <= cantidadCajas; i++) {
                salto = cantVariablesFO + (i-1) * cantMorrales + 1;
                cotaSuperior = cantMorrales + salto - 1;
                for (int j = salto; j <= cotaSuperior; j++) {
                    restriccion[j] = 1;
                }
                solver.addConstraint(restriccion, LpSolve.EQ, 1);
                restriccion = new double[cantidadVariables + 1];
            }
        }catch (LpSolveException e) {
            e.printStackTrace();
        }
    }
    
    public void armarRestriccionNoExcederPesoVolumen(int cantMorrales, int cantVariablesFO){
        try{
            //Las cajas que se llevan en cada morral no puede exceder el volumen 
            //y el peso que soportan los morrales
            double[] restriccion_volumen = new double[cantidadVariables + 1];
            double[] restriccion_peso = new double[cantidadVariables + 1];
            int contador = 3;
            int salto;
            for (int i = 1; i <= cantMorrales; i++) {
                salto = cantVariablesFO + i;
                for (int j = salto; j <= cantidadVariables; j += cantMorrales) {
                    restriccion_volumen[j] = propiedades.get(contador);
                    restriccion_peso[j] = propiedades.get(contador + 1);
                    contador += 2;
                }
                contador = 3;
                solver.addConstraint(restriccion_volumen, LpSolve.LE, propiedades.get(1));
                solver.addConstraint(restriccion_peso, LpSolve.LE, propiedades.get(2));
                restriccion_volumen = new double[cantidadVariables + 1];
                restriccion_peso = new double[cantidadVariables + 1];
            }
        }catch (LpSolveException e) {
            e.printStackTrace();
        }
    }
    
    public void armarRestriccionTecnica(int cantMorrales){
        try{
               //Control de morrales vacios
            double[]restriccion = new double[cantidadVariables + 1];
            int salto;

            for (int i = 1; i <= cantMorrales; i++) {
                salto = i + cantMorrales;
                restriccion[i] = -MGrande;
                for (int j = salto; j <= cantidadVariables; j += cantMorrales) {
                    restriccion[j] = 1;
                }
                solver.addConstraint(restriccion, LpSolve.LE, 0);
                restriccion = new double[cantidadVariables + 1];
            }
        }catch (LpSolveException e) {
            e.printStackTrace();
        }
    }

    public void agregarRestriccionesCantMorrales() {     
        armarRestirccionCajaUnMorral(cantidadCajas, cantidadCajas);
        armarRestriccionNoExcederPesoVolumen(cantidadCajas, cantidadCajas);
        armarRestriccionTecnica(cantidadCajas);
    }
    
    public void agregarRestriccionesDistribucion() {     
        armarRestirccionCajaUnMorral((int) cantOptimaMorrales, (int) cantOptimaMorrales-1);
        armarRestriccionNoExcederPesoVolumen((int) cantOptimaMorrales, (int) cantOptimaMorrales-1);
    }

    public String resolverCantMorrales(int codigoReglaBB) {
        try { 
            this.cantidadVariables = cantidadCajas + cantidadCajas * cantidadCajas;
            cantVariablesCantMorrales = cantidadVariables;
            solver = LpSolve.makeLp(0, cantidadVariables);
            setReglaBB(codigoReglaBB);
            agregarRestriccionesCantMorrales();            
            agregarFuncionObjetivoCantMorrales();
            setVariablesBinarias(1);
            //solver.writeLp("src/lpCantMorrales.lp");
            //solver.setBbRule(LpSolve.NODE_FIRSTSELECT);
            long time_start;
            time_start = System.currentTimeMillis();
            solver.solve();
            tiempoEjecucionCantMorrales = System.currentTimeMillis() - time_start;
            System.out.println("Tiempo de ejecución: " + tiempoEjecucionCantMorrales);
            //solver.printLp();
            //solver.printSolution(1);
            //solver.printObjective();
            //solver.printConstraints(1);
            cantIteracionesCantMorrales = solver.getTotalIter();
            cantNodosCantMorrales = solver.getTotalNodes();
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
                    distribucionCantMorrales.add(cajasLlevadasTemp + MGrande);
                    distribucionCantMorrales.add(row[indiceVol]);
                    distribucionCantMorrales.add(row[indiceVol + 1]); 
                    for (int j = indicePrimerCoeficientes; j < columns.length; j+= cantidadCajas) {
                        indiceCaja++;
                        if(columns[j] > 0){
                            cajas+= indiceCaja +"  ";                            
                        }
                    }
                    distribucionCantMorrales.add(cajas);
                    indiceCaja=0;
                    cajas="";                    
                }                
                indicePrimerCoeficientes++;
                cont++;
            }
            solver.deleteLp();
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
        return mensajeResultado;
    }
    
    public String resolverDistribucionEq(int codigoReglaBB) {
        try {
            cantidadVariables = (int) cantOptimaMorrales*(1 + cantidadCajas) -1;
            cantVariablesDistribucion = cantidadVariables;
            solver = LpSolve.makeLp(0, cantidadVariables);
            setReglaBB(codigoReglaBB);
            agregarRestriccionesDistribucion();            
            agregarFuncionObjetivoDistribucionEq((int) cantOptimaMorrales);
            setVariablesEnteras(1);
            setVariablesBinarias((int) cantOptimaMorrales);
            solver.writeLp("src/lpDistribucion.lp");
            //solver.setBbRule(LpSolve.NODE_FIRSTSELECT);
            long time_start;
            time_start = System.currentTimeMillis();
            solver.solve();
            tiempoEjecucionDistribucion = System.currentTimeMillis() - time_start;
            System.out.println("Tiempo de ejecución: " + tiempoEjecucionDistribucion);
            //solver.printLp();
            //solver.printSolution(1);
            //solver.printObjective();
            //solver.printConstraints(1);
            cantIteracionesDistribucion = solver.getTotalIter();
            cantNodosDistribucion = solver.getTotalNodes();
            valorFunObjetivoDistribucion = solver.getObjective();

            int cont=3;
            double[] row = new double[3*((int) cantOptimaMorrales) + cantidadCajas];
            double[] variables = solver.getPtrVariables();
            int indiceMorral, indiceCaja=0;
            solver.getConstraints(row);
            
            for(int i=0; i < (int) cantOptimaMorrales; i++){
                //Cantidad de cajas  inicial
                distribucionOptima.add(0);
                //Volumen inicial
                distribucionOptima.add(0.0);
                //Peso inicial
                distribucionOptima.add(0.0);
                //String cajas llevadas iniciales
                distribucionOptima.add("");
            }
            
            indiceCaja = 1;
            for (int i = (int) cantOptimaMorrales-1; i <= cantidadVariables - (int) cantOptimaMorrales;
                    i+= (int) cantOptimaMorrales) {   
                indiceMorral = 0;    
                
                for(int j = i; j < i+ (int) cantOptimaMorrales; j++){                     
                    if(variables[j] > 0){
                        distribucionOptima.set(indiceMorral, 
                                (int) distribucionOptima.get(indiceMorral) + 1);
                        distribucionOptima.set(indiceMorral + 1, 
                               (double) distribucionOptima.get(indiceMorral + 1) + propiedades.get(cont));
                        distribucionOptima.set(indiceMorral + 2, 
                               (double) distribucionOptima.get(indiceMorral + 2) + propiedades.get(cont+1));
                        distribucionOptima.set(indiceMorral + 3, 
                               (String) distribucionOptima.get(indiceMorral + 3) + indiceCaja + "  ");
                    }
                    indiceMorral+=4;                    
                }                
                cont+=2;
                indiceCaja ++;
            }  
                
            solver.deleteLp();
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
        return mensajeResultado;
    }

    public void setVariablesBinarias(int primeraVariableBinaria) {
        //Variables binarias
        int numColumns = solver.getNcolumns();
        for (int i = primeraVariableBinaria; i <= numColumns; i++) {
            try {
                solver.setBinary(i, true);
            } catch (LpSolveException e) {
                e.printStackTrace();
            }

        }
    }
    
    public void setVariablesEnteras(int primeraVariableEntera) {
        //Variables binarias
        int numColumns = (int) cantOptimaMorrales;
        for (int i = primeraVariableEntera; i <= numColumns; i++) {
            try {
                solver.setInt(i, true);
            } catch (LpSolveException e) {
                e.printStackTrace();
            }

        }
    }
    
    public void setReglaBB(int codigoReglaBB){
        switch (codigoReglaBB) {            
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
