/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */
public class ObjetosProveedor {
    
    private int id_proveedor;
    private int id_persona;
    private String dir_proveedor;
    private int tel_proveedor;
    private String cuenta_proveedor;

    public String getCuenta_proveedor() {
        return cuenta_proveedor;
    }

    public void setCuenta_proveedor(String cuenta_proveedor) {
        this.cuenta_proveedor = cuenta_proveedor;
    }

    public String getDir_proveedor() {
        return dir_proveedor;
    }

    public void setDir_proveedor(String dir_proveedor) {
        this.dir_proveedor = dir_proveedor;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public int getTel_proveedor() {
        return tel_proveedor;
    }

    public void setTel_proveedor(int tel_proveedor) {
        this.tel_proveedor = tel_proveedor;
    }
    
}
