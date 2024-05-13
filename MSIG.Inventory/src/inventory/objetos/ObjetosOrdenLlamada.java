package inventory.objetos;

public class ObjetosOrdenLlamada {

    private int id_d_orden_llamada;
    private int id_orden;
    private String motivo_d_orden_llamada;
    private String respuesta_d_orden_llamada;
    private String usuario_d_orden_llamada;
    private String fecha_d_orden_llamada;

    public String getFecha_d_orden_llamada() {
        return fecha_d_orden_llamada;
    }

    public void setFecha_d_orden_llamada(String fecha_d_orden_llamada) {
        this.fecha_d_orden_llamada = fecha_d_orden_llamada;
    }

    public int getId_d_orden_llamada() {
        return id_d_orden_llamada;
    }

    public void setId_d_orden_llamada(int id_d_orden_llamada) {
        this.id_d_orden_llamada = id_d_orden_llamada;
    }

    public int getId_orden() {
        return id_orden;
    }

    public void setId_orden(int id_orden) {
        this.id_orden = id_orden;
    }

    public String getMotivo_d_orden_llamada() {
        return motivo_d_orden_llamada;
    }

    public void setMotivo_d_orden_llamada(String motivo_d_orden_llamada) {
        this.motivo_d_orden_llamada = motivo_d_orden_llamada;
    }

    public String getRespuesta_d_orden_llamada() {
        return respuesta_d_orden_llamada;
    }

    public void setRespuesta_d_orden_llamada(String respuesta_d_orden_llamada) {
        this.respuesta_d_orden_llamada = respuesta_d_orden_llamada;
    }

    public String getUsuario_d_orden_llamada() {
        return usuario_d_orden_llamada;
    }

    public void setUsuario_d_orden_llamada(String usuario_d_orden_llamada) {
        this.usuario_d_orden_llamada = usuario_d_orden_llamada;
    }

}
