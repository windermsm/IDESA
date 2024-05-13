/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */
public class ObjetosCompra {
    
    private int id_compra;
    private int id_proveedor;
    private String nit_proveedor;
    private String nombre_proveedor;
    private String saldo_pendiente;
    private String fecha_factura_compra;
    private String numero_factura_compra;
    private String serie_factura_compra;
    private String folio_factura_compra;
    private float total_factura_compra;

    public String getFecha_factura_compra() {
        return fecha_factura_compra;
    }

    public void setFecha_factura_compra(String fecha_factura_compra) {
        this.fecha_factura_compra = fecha_factura_compra;
    }

    public String getFolio_factura_compra() {
        return folio_factura_compra;
    }

    public void setFolio_factura_compra(String folio_factura_compra) {
        this.folio_factura_compra = folio_factura_compra;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getNit_proveedor() {
        return nit_proveedor;
    }

    public void setNit_proveedor(String nit_proveedor) {
        this.nit_proveedor = nit_proveedor;
    }

    public String getNombre_proveedor() {
        return nombre_proveedor;
    }

    public void setNombre_proveedor(String nombre_proveedor) {
        this.nombre_proveedor = nombre_proveedor;
    }

    public String getNumero_factura_compra() {
        return numero_factura_compra;
    }

    public void setNumero_factura_compra(String numero_factura_compra) {
        this.numero_factura_compra = numero_factura_compra;
    }

    public String getSerie_factura_compra() {
        return serie_factura_compra;
    }

    public void setSerie_factura_compra(String serie_factura_compra) {
        this.serie_factura_compra = serie_factura_compra;
    }

    public float getTotal_factura_compra() {
        return total_factura_compra;
    }

    public void setTotal_factura_compra(float total_factura_compra) {
        this.total_factura_compra = total_factura_compra;
    }

    public String getSaldo_pendiente() {
        return saldo_pendiente;
    }

    public void setSaldo_pendiente(String saldo_pendiente) {
        this.saldo_pendiente = saldo_pendiente;
    }
    
}
