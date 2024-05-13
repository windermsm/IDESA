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
public class Productos {
    private String Producto;                /*  */
    private String Descripcion;             /* @param Descripcion nombre del producto */
    private String Medida;                  /* @param Medida Identificador unico de la unidad (1=UNIDAD) */
    private double Cantidad;                /* @param Cantidad Cantidad del producto que se esta entregando */ 
    private double Precio;                  /* @param Precio Precio unitario del produto IVA incluido */
    private double PorcDesc;                /* @param PorcDesc Porcentaje de descuento aplicado al producto */
    private double ImpBruto;                /* @param ImpBruto Importe Bruto resultado de precio por cantidad */
    private double ImpDescuento;            /* @param Descuento aplicado al producto */
    private double ImpExento;               /* @param Importe Excento de calculo de IVA */
    private double ImpOtros;                /* @param ImpOtros Importe de Otros impuestos */
    private double ImpNeto;                 /* @param ImpNeto Importe Neto luego de restar descuentos y que esta sujeto de IVA */
    private double ImpIsr;                  /* @param ImpIsr Importe ISR aplicado al documento */
    private double ImpIva;                  /* @param ImpIva Importe IVA aplicado sobre importe neto */
    private double ImpTotal;                /* @param ImpTotal Importe total del documento */
    private String TipoVenta;               /* @param TipoVenta tipo de la venta del producto */
    private String DatosAdicionalesProd;    /* @param DatosAdicionales Datos adicionales de productos segun requiera el EFACE */

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String Producto) {
        this.Producto = Producto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getMedida() {
        return Medida;
    }

    public void setMedida(String Medida) {
        this.Medida = Medida;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double Cantidad) {
        this.Cantidad = Cantidad;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double Precio) {
        this.Precio = Precio;
    }

    public double getPorcDesc() {
        return PorcDesc;
    }

    public void setPorcDesc(double PorcDesc) {
        this.PorcDesc = PorcDesc;
    }

    public double getImpBruto() {
        return ImpBruto;
    }

    public void setImpBruto(double ImpBruto) {
        this.ImpBruto = ImpBruto;
    }

    public double getImpDescuento() {
        return ImpDescuento;
    }

    public void setImpDescuento(double ImpDescuento) {
        this.ImpDescuento = ImpDescuento;
    }

    public double getImpExento() {
        return ImpExento;
    }

    public void setImpExento(double ImpExento) {
        this.ImpExento = ImpExento;
    }

    public double getImpOtros() {
        return ImpOtros;
    }

    public void setImpOtros(double ImpOtros) {
        this.ImpOtros = ImpOtros;
    }

    public double getImpNeto() {
        return ImpNeto;
    }

    public void setImpNeto(double ImpNeto) {
        this.ImpNeto = ImpNeto;
    }

    public double getImpIsr() {
        return ImpIsr;
    }

    public void setImpIsr(double ImpIsr) {
        this.ImpIsr = ImpIsr;
    }

    public double getImpIva() {
        return ImpIva;
    }

    public void setImpIva(double ImpIva) {
        this.ImpIva = ImpIva;
    }

    public double getImpTotal() {
        return ImpTotal;
    }

    public void setImpTotal(double ImpTotal) {
        this.ImpTotal = ImpTotal;
    }

    public String getTipoVenta() {
        return TipoVenta;
    }

    public void setTipoVenta(String TipoVenta) {
        this.TipoVenta = TipoVenta;
    }

    public String getDatosAdicionalesProd() {
        return DatosAdicionalesProd;
    }

    public void setDatosAdicionalesProd(String DatosAdicionalesProd) {
        this.DatosAdicionalesProd = DatosAdicionalesProd;
    }
    
}
