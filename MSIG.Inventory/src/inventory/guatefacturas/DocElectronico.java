/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.guatefacturas;

/**
 *
 * @author dsantacruz
 */
public class DocElectronico {
    private Encabezado Encabezado = new Encabezado();
    private Detalles Detalles = new Detalles();

    public Encabezado getEncabezado() {
        return Encabezado;
    }

    public void setEncabezado(Encabezado Encabezado) {
        this.Encabezado = Encabezado;
    }

    public Detalles getDetalles() {
        return Detalles;
    }

    public void setDetalles(Detalles Detalles) {
        this.Detalles = Detalles;
    }
    
}
