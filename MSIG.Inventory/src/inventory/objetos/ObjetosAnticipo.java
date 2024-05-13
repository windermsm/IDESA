package inventory.objetos;

public class ObjetosAnticipo {
    
    private int id_anticipo;
    private String nombre_persona_anticipo;
    private String telefono_anticipo;
    private String fecha_entrega_persona_aticipo;
    private String fecha_entrega_producto_anticipo;
    private String fecha_uso_anticipo;
    private String detalle_anticipo;
    private double total_anticipo;
    private String comentarios_anticipo;
    private String estado_anticipo;
    private String usuario_registro_anticipo;
    private String usuario_modifico_anticipo;
    private int id_factura;
    private double saldo_anticipo;
    private String nit_anticipo;

    public String getComentarios_anticipo() {
        return comentarios_anticipo;
    }

    public void setComentarios_anticipo(String comentarios_anticipo) {
        this.comentarios_anticipo = comentarios_anticipo;
    }

    public String getDetalle_anticipo() {
        return detalle_anticipo;
    }

    public void setDetalle_anticipo(String detalle_anticipo) {
        this.detalle_anticipo = detalle_anticipo;
    }

    public String getEstado_anticipo() {
        return estado_anticipo;
    }

    public void setEstado_anticipo(String estado_anticipo) {
        this.estado_anticipo = estado_anticipo;
    }

    public String getFecha_entrega_persona_aticipo() {
        return fecha_entrega_persona_aticipo;
    }

    public void setFecha_entrega_persona_aticipo(String fecha_entrega_persona_aticipo) {
        this.fecha_entrega_persona_aticipo = fecha_entrega_persona_aticipo;
    }

    public String getFecha_entrega_producto_anticipo() {
        return fecha_entrega_producto_anticipo;
    }

    public void setFecha_entrega_producto_anticipo(String fecha_entrega_producto_anticipo) {
        this.fecha_entrega_producto_anticipo = fecha_entrega_producto_anticipo;
    }

    public String getFecha_uso_anticipo() {
        return fecha_uso_anticipo;
    }

    public void setFecha_uso_anticipo(String fecha_uso_anticipo) {
        this.fecha_uso_anticipo = fecha_uso_anticipo;
    }

    public int getId_anticipo() {
        return id_anticipo;
    }

    public void setId_anticipo(int id_anticipo) {
        this.id_anticipo = id_anticipo;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public String getNombre_persona_anticipo() {
        return nombre_persona_anticipo;
    }

    public void setNombre_persona_anticipo(String nombre_persona_anticipo) {
        this.nombre_persona_anticipo = nombre_persona_anticipo;
    }

    public String getTelefono_anticipo() {
        return telefono_anticipo;
    }

    public void setTelefono_anticipo(String telefono_anticipo) {
        this.telefono_anticipo = telefono_anticipo;
    }

    public double getTotal_anticipo() {
        return total_anticipo;
    }

    public void setTotal_anticipo(double total_anticipo) {
        this.total_anticipo = total_anticipo;
    }

    public String getUsuario_modifico_anticipo() {
        return usuario_modifico_anticipo;
    }

    public void setUsuario_modifico_anticipo(String usuario_modifico_anticipo) {
        this.usuario_modifico_anticipo = usuario_modifico_anticipo;
    }

    public String getUsuario_registro_anticipo() {
        return usuario_registro_anticipo;
    }

    public void setUsuario_registro_anticipo(String usuario_registro_anticipo) {
        this.usuario_registro_anticipo = usuario_registro_anticipo;
    }

    public double getSaldo_anticipo() {
        return saldo_anticipo;
    }

    public void setSaldo_anticipo(double saldo_anticipo) {
        this.saldo_anticipo = saldo_anticipo;
    }

    public String getNit_anticipo() {
        return nit_anticipo;
    }

    public void setNit_anticipo(String nit_anticipo) {
        this.nit_anticipo = nit_anticipo;
    }
    
}
