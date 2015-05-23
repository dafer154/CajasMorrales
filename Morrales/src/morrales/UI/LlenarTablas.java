/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morrales.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import morrales.BL.ResolverProblema;

/**
 *
 * @author roand_000
 */
public class LlenarTablas {
    
    public LlenarTablas(String ruta){
    resolver= new ResolverProblema(ruta);
    }
    
ResolverProblema resolver;
    public void llenarTablarRequerimiento(JTable tabla)
    {
         String pattern = "###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        double cantidadCajas=resolver.getCantidadCajas();
        
        TableCellRenderer render = new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                //Si values es nulo dara problemas de renderizado, por lo tanto se pone como vacio
                JLabel lbl = new JLabel(value == null? "": value.toString());
      
               Font negrita= new Font("",Font.BOLD,12);
                if(row == cantidadCajas) //color de fondo
                    lbl.setFont(negrita);
                
                return lbl;
            }
        };
        double totalVolumen=0;
        double totalPeso=0;
        
        String[] columnas={"Caja","Volumen","Peso"};
        int contador=3;
            DefaultTableModel modeloTabla =new DefaultTableModel(null,columnas);
            
            for (int i = 0; i < cantidadCajas; i++) {
                String [] filas = {
                    ""+(i+1),
                    decimalFormat.format(resolver.getPropiedades().get(contador)),
                    decimalFormat.format(resolver.getPropiedades().get(contador+1))          
                };
                totalVolumen+=resolver.getPropiedades().get(contador);
                totalPeso+=resolver.getPropiedades().get(contador+1);
                modeloTabla.addRow(filas);
                contador+=2;
            }
            String [] filas = {"Total",decimalFormat.format(totalVolumen),decimalFormat.format(totalPeso)};
            modeloTabla.addRow(filas);
            tabla.setModel(modeloTabla);
            tabla.getColumnModel().getColumn(0).setCellRenderer(render);
            tabla.getColumnModel().getColumn(1).setCellRenderer(render);
            tabla.getColumnModel().getColumn(2).setCellRenderer(render);
    }
    
    
    
    public boolean llenarTablaCantidadMorrales(JTable tabla,int opcion)
    {
        try {
            
        
        String pattern = "###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        
        
        resolver.resolverCantMorrales(opcion);
        double cantidadMorrales=Math.round(resolver.getCantOptimaMorrales());
        
         TableCellRenderer render = new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                //Si values es nulo dara problemas de renderizado, por lo tanto se pone como vacio
                JLabel lbl = new JLabel(value == null? "": value.toString());
      
               Font negrita= new Font("",Font.BOLD,12);
                if(row == cantidadMorrales) //color de fondo
                    lbl.setFont(negrita);
                
                return lbl;
            }
        };
        
        double totalCantidadCajas=0;
        double totalVolumenOc=0;
        double totalPesoOc=0;
        
        String[] columnas={"Morral","Cantidad de cajas","Volumen ocupado","Peso ocupado","Cajas llevadas"};
        int contador=0;
            DefaultTableModel modeloTabla =new DefaultTableModel(null,columnas);
            for (int i = 0; i < cantidadMorrales; i++) {
                String [] filas = {
                    ""+(i+1),
                    ""+decimalFormat.format((double)resolver.getDistribucionCantMorrales().get(contador)),
                    ""+decimalFormat.format((double)resolver.getDistribucionCantMorrales().get(contador+1)),
                    ""+decimalFormat.format((double)resolver.getDistribucionCantMorrales().get(contador+2)),
                    ""+resolver.getDistribucionCantMorrales().get(contador+3).toString()
                };
                totalCantidadCajas+=(double)resolver.getDistribucionCantMorrales().get(contador);
                totalVolumenOc+=(double)resolver.getDistribucionCantMorrales().get(contador+1);
                totalPesoOc+=(double)resolver.getDistribucionCantMorrales().get(contador+2);
                modeloTabla.addRow(filas);
                contador+=4;
            }
           String [] filas = {"Total",""+Math.round(totalCantidadCajas),decimalFormat.format(totalVolumenOc),decimalFormat.format(totalPesoOc)};
            modeloTabla.addRow(filas);
            tabla.setModel(modeloTabla);
            tabla.getColumnModel().getColumn(0).setCellRenderer(render);
            tabla.getColumnModel().getColumn(1).setCellRenderer(render);
            tabla.getColumnModel().getColumn(2).setCellRenderer(render);
            tabla.getColumnModel().getColumn(3).setCellRenderer(render);
        } 
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
            
           
    }
    
    public boolean llenarTablaDistEquilibradaMorrales(JTable tabla,int opcion)
    {
        try {
            
        
        String pattern = "###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
    
        resolver.resolverDistribucionEq(opcion);
        double cantidadMorrales=Math.round(resolver.getCantOptimaMorrales());
        
         TableCellRenderer render = new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                //Si values es nulo dara problemas de renderizado, por lo tanto se pone como vacio
                JLabel lbl = new JLabel(value == null? "": value.toString());
      
               Font negrita= new Font("",Font.BOLD,12);
                if(row == cantidadMorrales) //color de fondo
                    lbl.setFont(negrita);
                
                return lbl;
            }
        };
        
        int totalCantidadCajas=0;
        double totalVolumenOc=0;
        double totalPesoOc=0;
        
        String[] columnas={"Morral","Cantidad de cajas","Volumen ocupado","Peso ocupado","Cajas llevadas"};
        int contador=0;
            DefaultTableModel modeloTabla =new DefaultTableModel(null,columnas);
            for (int i = 0; i < cantidadMorrales; i++) {
                String [] filas = {
                    ""+(i+1),
                    ""+decimalFormat.format(resolver.getDistribucionOptima().get(contador)),
                    ""+decimalFormat.format((double)resolver.getDistribucionOptima().get(contador+1)),
                    ""+decimalFormat.format((double)resolver.getDistribucionOptima().get(contador+2)),
                    ""+resolver.getDistribucionOptima().get(contador+3).toString()
                };
                totalCantidadCajas+=(int)resolver.getDistribucionOptima().get(contador);
                totalVolumenOc+=(double)resolver.getDistribucionOptima().get(contador+1);
                totalPesoOc+=(double)resolver.getDistribucionOptima().get(contador+2);
                modeloTabla.addRow(filas);
                contador+=4;
            }
           String [] filas = {"Total",""+Math.round(totalCantidadCajas),decimalFormat.format(totalVolumenOc),decimalFormat.format(totalPesoOc)};
            modeloTabla.addRow(filas);
            tabla.setModel(modeloTabla);
            tabla.getColumnModel().getColumn(0).setCellRenderer(render);
            tabla.getColumnModel().getColumn(1).setCellRenderer(render);
            tabla.getColumnModel().getColumn(2).setCellRenderer(render);
            tabla.getColumnModel().getColumn(3).setCellRenderer(render);
        } 
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
            
           
    }
    
    public double getNumeroOptimoMorrales(){ 
       return resolver.getCantOptimaMorrales();
    }
    
    //Primera parte
    public long getCantidadNodosCantMorrales(){ 
       return resolver.getCantNodosCantMorrales();
    }
    public long getCantidadIteracionesCantMorrales(){ 
       return resolver.getCantIteracionesCantMorrales();
    }
    public double getCantidadVariablesCantMorrales(){ 
       return resolver.getCantVariablesCantMorrales();
    }
    public long getTiempoDeEjecucionCantMorrales(){ 
       return resolver.getTiempoEjecucionCantMorrales();
    }
    
    //Segunda parte
    
    public long getCantidadNodosDistMorrales(){ 
       return resolver.getCantNodosDistribucion();
    }
    public long getCantidadIteracionesDistMorrales(){ 
       return resolver.getCantIteracionesDistribucion();
    }
    public double getCantidadVariablesDistMorrales(){ 
       return resolver.getCantVariablesDistribucion();
    }
    public long getTiempoDeEjecucionDistMorrales(){ 
       return resolver.getTiempoEjecucionDistribucion();
    }
    
}
