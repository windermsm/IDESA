package inventory.objetos;

public class ObjetosAjuste {
  
  private int id_d_ajuste;
  private String id_producto;
  private String tipo_ajuste;
  private float cantidad_ajuste;
  private String motivo_ajuste;
  private String usuario_ajuste;
  private String fecha_ajuste;

    public float getCantidad_ajuste() {
        return cantidad_ajuste;
    }

    public void setCantidad_ajuste(float cantidad_ajuste) {
        this.cantidad_ajuste = cantidad_ajuste;
    }

    public String getFecha_ajuste() {
        return fecha_ajuste;
    }

    public void setFecha_ajuste(String fecha_ajuste) {
        this.fecha_ajuste = fecha_ajuste;
    }

    public int getId_d_ajuste() {
        return id_d_ajuste;
    }

    public void setId_d_ajuste(int id_d_ajuste) {
        this.id_d_ajuste = id_d_ajuste;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getMotivo_ajuste() {
        return motivo_ajuste;
    }

    public void setMotivo_ajuste(String motivo_ajuste) {
        this.motivo_ajuste = motivo_ajuste;
    }

    public String getTipo_ajuste() {
        return tipo_ajuste;
    }

    public void setTipo_ajuste(String tipo_ajuste) {
        this.tipo_ajuste = tipo_ajuste;
    }

    public String getUsuario_ajuste() {
        return usuario_ajuste;
    }

    public void setUsuario_ajuste(String usuario_ajuste) {
        this.usuario_ajuste = usuario_ajuste;
    }
  
}
