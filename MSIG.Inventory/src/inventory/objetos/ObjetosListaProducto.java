/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Will Rod
 */
public class ObjetosListaProducto {
    private String idproducto;
    private String descripcionproducto;
    private String serie;// SI O NO
    private String componentes;// SI O NO
    private String garantia;// SI O NO
    private int cantidad;

    public ObjetosListaProducto() {
    }

    public ObjetosListaProducto(String idproducto, String descripcionproducto, String serie, String componentes, String garantia, int cantidad) {
        this.idproducto = idproducto;
        this.descripcionproducto = descripcionproducto;
        this.serie = serie;
        this.componentes = componentes;
        this.garantia = garantia;
        this.cantidad = cantidad;
    }

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getDescripcionproducto() {
        return descripcionproducto;
    }

    public void setDescripcionproducto(String descripciónproducto) {
        this.descripcionproducto = descripciónproducto;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getComponentes() {
        return componentes;
    }

    public void setComponentes(String componentes) {
        this.componentes = componentes;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }    
    
}
