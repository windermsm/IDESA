/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.servicios;

/**
 *
 * @author Rolando
 */
public class Matematicas {
    
    public double redondear(double numero) {
        return Math.round(numero * 100) / 100.0d;
    }
    
    public double aproxima(double numero, int decimales){
        return Math.round(numero*Math.pow(10, decimales))/Math.pow(10, decimales);
    }
}
