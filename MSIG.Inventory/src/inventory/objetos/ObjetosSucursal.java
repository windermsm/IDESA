/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inventory.objetos;

/**
 *
 * @author Gigi
 */
public class ObjetosSucursal {
    private int     id_sucursal;
    private String  nombre_sucursal;
    private String  descripcion_sucursal;
    private String  direccion_sucursal;
    private String  nit_sucursal;
    private String  telefonos_sucursal;

    public String getDescripcion_sucursal() {
        return descripcion_sucursal;
    }

    public void setDescripcion_sucursal(String descripcion_sucursal) {
        this.descripcion_sucursal = descripcion_sucursal;
    }

    public String getDireccion_sucursal() {
        return direccion_sucursal;
    }

    public void setDireccion_sucursal(String direccion_sucursal) {
        this.direccion_sucursal = direccion_sucursal;
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getNit_sucursal() {
        return nit_sucursal;
    }

    public void setNit_sucursal(String nit_sucursal) {
        this.nit_sucursal = nit_sucursal;
    }

    public String getNombre_sucursal() {
        return nombre_sucursal;
    }

    public void setNombre_sucursal(String nombre_sucursal) {
        this.nombre_sucursal = nombre_sucursal;
    }

    public String getTelefonos_sucursal() {
        return telefonos_sucursal;
    }

    public void setTelefonos_sucursal(String telefonos_sucursal) {
        this.telefonos_sucursal = telefonos_sucursal;
    }
     
}
