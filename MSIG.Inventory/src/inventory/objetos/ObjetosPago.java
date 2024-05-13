/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Megabytes Soluciones Integrales Guatemala
 */
public class ObjetosPago {
    
    private int id_pago;
    private String numero_factura_pago;
    private String serie_factura_pago;
    private String numero_doc_pago;
    private String tipo_doc_pago;
    private String fecha_doc_pago;
    private float monto_doc_pago;
    private String cuenta_pago;
    private String motivo_pago;
    private String fecha_pago;
    private String estado_pago;
    private String tipo_pago;

    public String getCuenta_pago() {
        return cuenta_pago;
    }

    public void setCuenta_pago(String cuenta_pago) {
        this.cuenta_pago = cuenta_pago;
    }

    public String getEstado_pago() {
        return estado_pago;
    }

    public void setEstado_pago(String estado_pago) {
        this.estado_pago = estado_pago;
    }

    public String getFecha_doc_pago() {
        return fecha_doc_pago;
    }

    public void setFecha_doc_pago(String fecha_doc_pago) {
        this.fecha_doc_pago = fecha_doc_pago;
    }

    public String getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(String fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public float getMonto_doc_pago() {
        return monto_doc_pago;
    }

    public void setMonto_doc_pago(float monto_doc_pago) {
        this.monto_doc_pago = monto_doc_pago;
    }

    public String getMotivo_pago() {
        return motivo_pago;
    }

    public void setMotivo_pago(String motivo_pago) {
        this.motivo_pago = motivo_pago;
    }

    public String getNumero_doc_pago() {
        return numero_doc_pago;
    }

    public void setNumero_doc_pago(String numero_doc_pago) {
        this.numero_doc_pago = numero_doc_pago;
    }

    public String getNumero_factura_pago() {
        return numero_factura_pago;
    }

    public void setNumero_factura_pago(String numero_factura_pago) {
        this.numero_factura_pago = numero_factura_pago;
    }

    public String getSerie_factura_pago() {
        return serie_factura_pago;
    }

    public void setSerie_factura_pago(String serie_factura_pago) {
        this.serie_factura_pago = serie_factura_pago;
    }

    public String getTipo_doc_pago() {
        return tipo_doc_pago;
    }

    public void setTipo_doc_pago(String tipo_doc_pago) {
        this.tipo_doc_pago = tipo_doc_pago;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

}
