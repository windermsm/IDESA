/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Derwin Santa Cruz
 */
public class ObjetosDetalleTraslado {
    private int id_d_traslado;
    private int id_traslado;
    private String id_producto;
    private float cantidad_d_traslado;
    private float total_d_traslado;

    public float getCantidad_d_traslado() {
        return cantidad_d_traslado;
    }

    public void setCantidad_d_traslado(float cantidad_d_traslado) {
        this.cantidad_d_traslado = cantidad_d_traslado;
    }

    public int getId_d_traslado() {
        return id_d_traslado;
    }

    public void setId_d_traslado(int id_d_traslado) {
        this.id_d_traslado = id_d_traslado;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_traslado() {
        return id_traslado;
    }

    public void setId_traslado(int id_traslado) {
        this.id_traslado = id_traslado;
    }

    public float getTotal_d_traslado() {
        return total_d_traslado;
    }

    public void setTotal_d_traslado(float total_d_traslado) {
        this.total_d_traslado = total_d_traslado;
    }
    
}
