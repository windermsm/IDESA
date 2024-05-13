package inventory.objetos;

public class ObjetosDetalleFactura {
    
    private int id_d_factura;
    private int id_factura;
    private String id_producto;
    private float cantidad_d_factura;
    private float precio_compra_d_factura;
    private float precio_estandar_d_factura;
    private float precio_venta_d_factura;
    private float sub_total_d_factura;
    private String comentario_d_factura;
    private float arancel_d_factura;

    public int getId_d_factura() {
        return id_d_factura;
    }

    public void setId_d_factura(int id_d_factura) {
        this.id_d_factura = id_d_factura;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public float getCantidad_d_factura() {
        return cantidad_d_factura;
    }

    public void setCantidad_d_factura(float cantidad_d_factura) {
        this.cantidad_d_factura = cantidad_d_factura;
    }

    public float getPrecio_compra_d_factura() {
        return precio_compra_d_factura;
    }

    public void setPrecio_compra_d_factura(float precio_compra_d_factura) {
        this.precio_compra_d_factura = precio_compra_d_factura;
    }

    public float getPrecio_estandar_d_factura() {
        return precio_estandar_d_factura;
    }

    public void setPrecio_estandar_d_factura(float precio_estandar_d_factura) {
        this.precio_estandar_d_factura = precio_estandar_d_factura;
    }

    public float getPrecio_venta_d_factura() {
        return precio_venta_d_factura;
    }

    public void setPrecio_venta_d_factura(float precio_venta_d_factura) {
        this.precio_venta_d_factura = precio_venta_d_factura;
    }

    public float getSub_total_d_factura() {
        return sub_total_d_factura;
    }

    public void setSub_total_d_factura(float sub_total_d_factura) {
        this.sub_total_d_factura = sub_total_d_factura;
    }

    public String getComentario_d_factura() {
        return comentario_d_factura;
    }

    public void setComentario_d_factura(String comentario_d_factura) {
        this.comentario_d_factura = comentario_d_factura;
    }

    public float getArancel_d_factura() {
        return arancel_d_factura;
    }

    public void setArancel_d_factura(float arancel_d_factura) {
        this.arancel_d_factura = arancel_d_factura;
    }
    
}
