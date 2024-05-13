/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.servicios;

import java.util.List;

/**
 *
 * @author Derwin Santa Cruz
 */
public class Factura {

    private MaestroFactura maestro;
    private List<Producto> productos;

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public MaestroFactura getMaestro() {
        return maestro;
    }

    public void setMaestro(MaestroFactura maestro) {
        this.maestro = maestro;
    }
}
