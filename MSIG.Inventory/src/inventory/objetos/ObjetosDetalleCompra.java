/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */

public class ObjetosDetalleCompra {
    
    private int id_d_compra;
    private int id_compra;
    private String id_producto;
    private float cant_d_compra;
    private float precio_d_compra;
    private float precio_estandar;
    private float precio_minimo;
    private float sub_total_d_compra;
    private String comentario_compra;
    private float arancel_compra;
    private String descipcion;

    public float getArancel_compra() {
        return arancel_compra;
    }

    public void setArancel_compra(float arancel_compra) {
        this.arancel_compra = arancel_compra;
    }

    public float getCant_d_compra() {
        return cant_d_compra;
    }

    public void setCant_d_compra(float cant_d_compra) {
        this.cant_d_compra = cant_d_compra;
    }

    public String getComentario_compra() {
        return comentario_compra;
    }

    public void setComentario_compra(String comentario_compra) {
        this.comentario_compra = comentario_compra;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public int getId_d_compra() {
        return id_d_compra;
    }

    public void setId_d_compra(int id_d_compra) {
        this.id_d_compra = id_d_compra;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public float getPrecio_d_compra() {
        return precio_d_compra;
    }

    public void setPrecio_d_compra(float precio_d_compra) {
        this.precio_d_compra = precio_d_compra;
    }

    public float getPrecio_estandar() {
        return precio_estandar;
    }

    public void setPrecio_estandar(float precio_estandar) {
        this.precio_estandar = precio_estandar;
    }

    public float getPrecio_minimo() {
        return precio_minimo;
    }

    public void setPrecio_minimo(float precio_minimo) {
        this.precio_minimo = precio_minimo;
    }

    public float getSub_total_d_compra() {
        return sub_total_d_compra;
    }

    public void setSub_total_d_compra(float sub_total_d_compra) {
        this.sub_total_d_compra = sub_total_d_compra;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }
    
}
