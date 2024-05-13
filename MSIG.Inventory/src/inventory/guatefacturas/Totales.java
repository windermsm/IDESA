/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.guatefacturas;

/**
 *
 * @author dsantacruz
 * @version 1.0
 */
public class Totales {
    private String Bruto;       /* @param Bruto Importe bruto luego de calcular importe por cantidad */
    private String Descuento;   /* @param Descuento Importe descuento aplicado al documento */   
    private String Exento;      /* @param Exento Importe exento del calculo de IVA */
    private String Otros;       /* @param Otros Importe otros impuestos no afectos al IVA */ 
    private String Neto;        /* @param Neto Importe neto sujeto al calculo de IVA */
    private String Isr;         /* @param ISR Importe ISR aplicado al documento */
    private String Iva;         /* @param Iva Importe IVA aplicado sobre importe Neto */
    private String Total;       /* @param Total Importe total del documento */

    public String getBruto() {
        return Bruto;
    }

    public void setBruto(String Bruto) {
        this.Bruto = Bruto;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String Descuento) {
        this.Descuento = Descuento;
    }

    public String getExento() {
        return Exento;
    }

    public void setExento(String Exento) {
        this.Exento = Exento;
    }

    public String getOtros() {
        return Otros;
    }

    public void setOtros(String Otros) {
        this.Otros = Otros;
    }

    public String getNeto() {
        return Neto;
    }

    public void setNeto(String Neto) {
        this.Neto = Neto;
    }

    public String getIsr() {
        return Isr;
    }

    public void setIsr(String Isr) {
        this.Isr = Isr;
    }

    public String getIva() {
        return Iva;
    }

    public void setIva(String Iva) {
        this.Iva = Iva;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }
    
}
