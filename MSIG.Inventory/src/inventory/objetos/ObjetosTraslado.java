/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

/**
 *
 * @author Derwin Santa Cruz
 */
public class ObjetosTraslado {
    private int id_traslado;
    private int id_sucursal;
    private String fecha_trasaldo;
    private float total_traslado;
    private String usuario_traslado;

    public String getFecha_trasaldo() {
        return fecha_trasaldo;
    }

    public void setFecha_trasaldo(String fecha_trasaldo) {
        this.fecha_trasaldo = fecha_trasaldo;
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public int getId_traslado() {
        return id_traslado;
    }

    public void setId_traslado(int id_traslado) {
        this.id_traslado = id_traslado;
    }

    public float getTotal_traslado() {
        return total_traslado;
    }

    public void setTotal_traslado(float total_traslado) {
        this.total_traslado = total_traslado;
    }

    public String getUsuario_traslado() {
        return usuario_traslado;
    }

    public void setUsuario_traslado(String usuario_traslado) {
        this.usuario_traslado = usuario_traslado;
    }
    
}
