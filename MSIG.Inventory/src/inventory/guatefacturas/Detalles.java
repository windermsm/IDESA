/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.guatefacturas;

import java.util.ArrayList;

/**
 *
 * @author dsantacruz
 * @version 1.0
 */
public class Detalles {
    
    private ArrayList<Productos> Productos = new ArrayList(); /* @param detalle Lista de productos */
    private ArrayList<DocAsociados> DocAsociados = new ArrayList(); /*@param DocAsociados Opcional solo para notas de creto o debito */

    public ArrayList<Productos> getProductos() {
        return Productos;
    }

    public void setProductos(ArrayList<Productos> Productos) {
        this.Productos = Productos;
    }

    public ArrayList<DocAsociados> getDocAsociados() {
        return DocAsociados;
    }

    public void setDocAsociados(ArrayList<DocAsociados> DocAsociados) {
        this.DocAsociados = DocAsociados;
    }
    
}
