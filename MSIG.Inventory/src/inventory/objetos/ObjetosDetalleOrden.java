package inventory.objetos;

public class ObjetosDetalleOrden {

    private int id_d_orden_producto;
    private int id_orden;
    private String id_producto;
    private String descripcion_d_orden_producto;
    private Double cantidad_d_orden_produco;
    private Double precio_d_orden_producto;
    private Double sub_total_d_orden_producto;
    private String estado_d_orden_producto;

    public Double getCantidad_d_orden_produco() {
        return cantidad_d_orden_produco;
    }

    public void setCantidad_d_orden_produco(Double cantidad_d_orden_produco) {
        this.cantidad_d_orden_produco = cantidad_d_orden_produco;
    }

    public String getDescripcion_d_orden_producto() {
        return descripcion_d_orden_producto;
    }

    public void setDescripcion_d_orden_producto(String descripcion_d_orden_producto) {
        this.descripcion_d_orden_producto = descripcion_d_orden_producto;
    }

    public String getEstado_d_orden_producto() {
        return estado_d_orden_producto;
    }

    public void setEstado_d_orden_producto(String estado_d_orden_producto) {
        this.estado_d_orden_producto = estado_d_orden_producto;
    }

    public int getId_d_orden_producto() {
        return id_d_orden_producto;
    }

    public void setId_d_orden_producto(int id_d_orden_producto) {
        this.id_d_orden_producto = id_d_orden_producto;
    }

    public int getId_orden() {
        return id_orden;
    }

    public void setId_orden(int id_orden) {
        this.id_orden = id_orden;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public Double getPrecio_d_orden_producto() {
        return precio_d_orden_producto;
    }

    public void setPrecio_d_orden_producto(Double precio_d_orden_producto) {
        this.precio_d_orden_producto = precio_d_orden_producto;
    }

    public Double getSub_total_d_orden_producto() {
        return sub_total_d_orden_producto;
    }

    public void setSub_total_d_orden_producto(Double sub_total_d_orden_producto) {
        this.sub_total_d_orden_producto = sub_total_d_orden_producto;
    }
    
}
