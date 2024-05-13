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
public class Receptor {
    private String Codigo;
    private String NITReceptor;     /* @param NITRecepto Nit del cliente comprador */
    private String Nombre;          /* @param Nombre Nombre del cliente comprador (debe mandarse si es comprador final) */
    private String Direccion;       /* @param Direccion Direccion del cliente comprador (debe mandarse si es comprador final) */
    private String bodega;          /* @param Codigo de la bodega del cliente */
    
    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }
    
    public String getNITReceptor() {
        return NITReceptor;
    }

    public void setNITReceptor(String NITReceptor) {
        this.NITReceptor = NITReceptor;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }
    
}
