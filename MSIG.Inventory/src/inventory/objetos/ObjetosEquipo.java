/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author DSANTACRUZ
 */
public class ObjetosEquipo {

    private int id_equipo;
    private String descripcion_equipo;
    private int depreciacion_equipo;
    private String numero_serie;
    private float costo_equipo;
    private String estado_equipo;

    public float getCosto_equipo() {
        return costo_equipo;
    }

    public void setCosto_equipo(float costo_equipo) {
        this.costo_equipo = costo_equipo;
    }

    public int getDepreciacion_equipo() {
        return depreciacion_equipo;
    }

    public void setDepreciacion_equipo(int depreciacion_equipo) {
        this.depreciacion_equipo = depreciacion_equipo;
    }

    public String getDescripcion_equipo() {
        return descripcion_equipo;
    }

    public void setDescripcion_equipo(String descripcion_equipo) {
        this.descripcion_equipo = descripcion_equipo;
    }

    public String getEstado_equipo() {
        return estado_equipo;
    }

    public void setEstado_equipo(String estado_equipo) {
        this.estado_equipo = estado_equipo;
    }

    public int getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }

    public String getNumero_serie() {
        return numero_serie;
    }

    public void setNumero_serie(String numero_serie) {
        this.numero_serie = numero_serie;
    }
    
}
