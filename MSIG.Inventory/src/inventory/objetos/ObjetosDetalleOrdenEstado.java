/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author DSANTACRUZ
 */
public class ObjetosDetalleOrdenEstado {

    private int id_d_orden_estado;
    private int id_orden;
    private String descripcion_d_orden_estado;
    private String fecha_d_orden_estado;
    private String usuario_d_orden_estado;

    public String getDescripcion_d_orden_estado() {
        return descripcion_d_orden_estado;
    }

    public void setDescripcion_d_orden_estado(String descripcion_d_orden_estado) {
        this.descripcion_d_orden_estado = descripcion_d_orden_estado;
    }

    public String getFecha_d_orden_estado() {
        return fecha_d_orden_estado;
    }

    public void setFecha_d_orden_estado(String fecha_d_orden_estado) {
        this.fecha_d_orden_estado = fecha_d_orden_estado;
    }

    public int getId_d_orden_estado() {
        return id_d_orden_estado;
    }

    public void setId_d_orden_estado(int id_d_orden_estado) {
        this.id_d_orden_estado = id_d_orden_estado;
    }

    public int getId_orden() {
        return id_orden;
    }

    public void setId_orden(int id_orden) {
        this.id_orden = id_orden;
    }

    public String getUsuario_d_orden_estado() {
        return usuario_d_orden_estado;
    }

    public void setUsuario_d_orden_estado(String usuario_d_orden_estado) {
        this.usuario_d_orden_estado = usuario_d_orden_estado;
    }
    
}
