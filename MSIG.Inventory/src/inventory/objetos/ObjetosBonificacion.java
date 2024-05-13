package inventory.objetos;

public class ObjetosBonificacion {
    
    private int id_bonificacion;
    private String descripcion_bonificacion;
    private float cantidad_bonificacion;
    private String estado_bonificacion;
    private float porcentaje_bonificacion;
    private String tipo_empleado;
    private String id_producto;

    public float getCantidad_bonificacion() {
        return cantidad_bonificacion;
    }

    public void setCantidad_bonificacion(float cantidad_bonificacion) {
        this.cantidad_bonificacion = cantidad_bonificacion;
    }

    public String getDescripcion_bonificacion() {
        return descripcion_bonificacion;
    }

    public void setDescripcion_bonificacion(String descripcion_bonificacion) {
        this.descripcion_bonificacion = descripcion_bonificacion;
    }

    public String getEstado_bonificacion() {
        return estado_bonificacion;
    }

    public void setEstado_bonificacion(String estado_bonificacion) {
        this.estado_bonificacion = estado_bonificacion;
    }

    public int getId_bonificacion() {
        return id_bonificacion;
    }

    public void setId_bonificacion(int id_bonificacion) {
        this.id_bonificacion = id_bonificacion;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public float getPorcentaje_bonificacion() {
        return porcentaje_bonificacion;
    }

    public void setPorcentaje_bonificacion(float porcentaje_bonificacion) {
        this.porcentaje_bonificacion = porcentaje_bonificacion;
    }

    public String getTipo_empleado() {
        return tipo_empleado;
    }

    public void setTipo_empleado(String tipo_empleado) {
        this.tipo_empleado = tipo_empleado;
    }
        
}
