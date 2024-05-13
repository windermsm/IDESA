/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Derwin Santa Cruz
 */
public class ObjetosRetencion {

    private int id_retencion;
    private String fecha_emision_retencion;
    private String numero_retencion;
    private double monto_retencion;
    private String nit_retencion;
    private int id_compra;
    private String fecha_pago_retencion;
    private String datos_pago_retencion;
    private String guia_retencion;
    private String fecha_envio_retencion;

    public int getId_retencion() {
        return id_retencion;
    }

    public void setId_retencion(int id_retencion) {
        this.id_retencion = id_retencion;
    }

    public String getFecha_emision_retencion() {
        return fecha_emision_retencion;
    }

    public void setFecha_emision_retencion(String fecha_emision_retencion) {
        this.fecha_emision_retencion = fecha_emision_retencion;
    }

    public String getNumero_retencion() {
        return numero_retencion;
    }

    public void setNumero_retencion(String numero_retencion) {
        this.numero_retencion = numero_retencion;
    }

    public double getMonto_retencion() {
        return monto_retencion;
    }

    public void setMonto_retencion(double monto_retencion) {
        this.monto_retencion = monto_retencion;
    }

    public String getNit_retencion() {
        return nit_retencion;
    }

    public void setNit_retencion(String nit_retencion) {
        this.nit_retencion = nit_retencion;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public String getFecha_pago_retencion() {
        return fecha_pago_retencion;
    }

    public void setFecha_pago_retencion(String fecha_pago_retencion) {
        this.fecha_pago_retencion = fecha_pago_retencion;
    }

    public String getDatos_pago_retencion() {
        return datos_pago_retencion;
    }

    public void setDatos_pago_retencion(String datos_pago_retencion) {
        this.datos_pago_retencion = datos_pago_retencion;
    }

    public String getGuia_retencion() {
        return guia_retencion;
    }

    public void setGuia_retencion(String guia_retencion) {
        this.guia_retencion = guia_retencion;
    }

    public String getFecha_envio_retencion() {
        return fecha_envio_retencion;
    }

    public void setFecha_envio_retencion(String fecha_envio_retencion) {
        this.fecha_envio_retencion = fecha_envio_retencion;
    }
    
}
