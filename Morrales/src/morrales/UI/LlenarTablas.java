/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morrales.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
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
    

    public void llenarTabla1(JTable tabla, String ruta)
    {
        
        
        ResolverProblema resolver= new ResolverProblema(ruta);
        
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
                    ""+resolver.getPropiedades().get(contador),
                    ""+resolver.getPropiedades().get(contador+1)            
                };
                totalVolumen+=resolver.getPropiedades().get(contador);
                totalPeso+=resolver.getPropiedades().get(contador+1);
                modeloTabla.addRow(filas);
                contador+=2;
            }
            String [] filas = {"Total",""+totalVolumen,""+totalPeso};
            modeloTabla.addRow(filas);
            tabla.setModel(modeloTabla);
            tabla.getColumnModel().getColumn(0).setCellRenderer(render);
            tabla.getColumnModel().getColumn(1).setCellRenderer(render);
            tabla.getColumnModel().getColumn(2).setCellRenderer(render);
    }
    
    
    
    public void llenarTabla2(JTable tabla, String ruta)
    {
        
        ResolverProblema resolver= new ResolverProblema(ruta);
        resolver.resolver();
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
        
        String[] columnas={"Morral","Cantidad de cajas","Volumen ocupado","Peso ocupado"};
        int contador=0;
            DefaultTableModel modeloTabla =new DefaultTableModel(null,columnas);
            for (int i = 0; i < cantidadMorrales; i++) {
                String [] filas = {
                    ""+(i+1),
                    ""+Math.round(resolver.getDistribucion().get(contador)),
                    ""+resolver.getDistribucion().get(contador+1),
                    ""+resolver.getDistribucion().get(contador+2)
                };
                totalCantidadCajas+=resolver.getDistribucion().get(contador);
                totalVolumenOc+=resolver.getDistribucion().get(contador+1);
                totalPesoOc+=resolver.getDistribucion().get(contador+2);
                modeloTabla.addRow(filas);
                contador+=3;
            }
            String [] filas = {"Total",""+Math.round(totalCantidadCajas),""+totalVolumenOc,""+totalPesoOc};
            modeloTabla.addRow(filas);
            tabla.setModel(modeloTabla);
            tabla.getColumnModel().getColumn(0).setCellRenderer(render);
            tabla.getColumnModel().getColumn(1).setCellRenderer(render);
            tabla.getColumnModel().getColumn(2).setCellRenderer(render);
            tabla.getColumnModel().getColumn(3).setCellRenderer(render);
           
    }
    
    
    
    
}