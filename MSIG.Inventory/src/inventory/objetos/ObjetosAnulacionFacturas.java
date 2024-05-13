package inventory.objetos;

public class ObjetosAnulacionFacturas {
    
    private int id_factura;
    private int id_d_factura;
    private String id_producto;
    private String descripcion;
    private int num_factura;
    private String serie_factura;
    private String folio_factura;
    private String fecha_emision_factura;
    private float total_factura;
    private String marca_producto;
    private float cant_d_factura;
    private float precio_venta_d_factura;
    private float subtotal;
    private float exi_producto;
    private float subTotal;

    public float getCant_d_factura() {
        return cant_d_factura;
    }

    public void setCant_d_factura(float cant_d_factura) {
        this.cant_d_factura = cant_d_factura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getExi_producto() {
        return exi_producto;
    }

    public void setExi_producto(float exi_producto) {
        this.exi_producto = exi_producto;
    }

    public String getFecha_emision_factura() {
        return fecha_emision_factura;
    }

    public void setFecha_emision_factura(String fecha_emision_factura) {
        this.fecha_emision_factura = fecha_emision_factura;
    }

    public String getFolio_factura() {
        return folio_factura;
    }

    public void setFolio_factura(String folio_factura) {
        this.folio_factura = folio_factura;
    }

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

    public String getMarca_producto() {
        return marca_producto;
    }

    public void setMarca_producto(String marca_producto) {
        this.marca_producto = marca_producto;
    }

    public int getNum_factura() {
        return num_factura;
    }

    public void setNum_factura(int num_factura) {
        this.num_factura = num_factura;
    }

    public float getPrecio_venta_d_factura() {
        return precio_venta_d_factura;
    }

    public void setPrecio_venta_d_factura(float precio_venta_d_factura) {
        this.precio_venta_d_factura = precio_venta_d_factura;
    }

    public String getSerie_factura() {
        return serie_factura;
    }

    public void setSerie_factura(String serie_factura) {
        this.serie_factura = serie_factura;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getTotal_factura() {
        return total_factura;
    }

    public void setTotal_factura(float total_factura) {
        this.total_factura = total_factura;
    }
    
    
}
