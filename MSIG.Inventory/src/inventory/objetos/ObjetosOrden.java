package inventory.objetos;

public class ObjetosOrden {

    private int id_orden;
    private int id_cliente;
    private String nombre_cliente_orden;
    private int telefono_cliente_orden;
    private String fecha_creacion_orden;
    private String usuario_creacion_orden;
    private int id_serie;
    private String serie_producto_orden;
    private String marca_producto_orden;
    private String modelo_producto_orden;
    private String caracteristicas_producto_orden;
    private String defecto_reportado_orden;
    private String observaciones_cliente_orden;
    private String diagnostico_orden;
    private String aplica_garantia_orden;
    private String serie_entregada_garantia_orden;
    private String cliente_acepta_presupuesto_orden;
    private String fecha_acepta_presupuesto_orden;
    private double valor_presupuesto_orden;
    private double abono_presupuesto_orden;
    private String tecnico_encargado_reparacion_orden;
    private String fecha_programada_entrega_orden;
    private String fecha_notifica_cliente_orden;
    private String fecha_entrega_orden;
    private int id_factura_emitida;
    private String estado_orden;
    
    private String case_orden;
    private String cargador_orden;
    private String bateria_orden;
    private String ram1_orden;
    private String ram2_orden;
    private String ram3_orden;
    private String ram4_orden;
    private String fuente_orden;
    private String otros_orden;
    
    private double total_orden;
    
    private String tarjeta_orden;
    private String disco_orden;
    
    private String tipo_producto_orden;
    private String tipo_trabajo_orden;

    public double getAbono_presupuesto_orden() {
        return abono_presupuesto_orden;
    }

    public void setAbono_presupuesto_orden(double abono_presupuesto_orden) {
        this.abono_presupuesto_orden = abono_presupuesto_orden;
    }

    public String getAplica_garantia_orden() {
        return aplica_garantia_orden;
    }

    public void setAplica_garantia_orden(String aplica_garantia_orden) {
        this.aplica_garantia_orden = aplica_garantia_orden;
    }

    public String getCaracteristicas_producto_orden() {
        return caracteristicas_producto_orden;
    }

    public void setCaracteristicas_producto_orden(String caracteristicas_producto_orden) {
        this.caracteristicas_producto_orden = caracteristicas_producto_orden;
    }

    public String getCliente_acepta_presupuesto_orden() {
        return cliente_acepta_presupuesto_orden;
    }

    public void setCliente_acepta_presupuesto_orden(String cliente_acepta_presupuesto_orden) {
        this.cliente_acepta_presupuesto_orden = cliente_acepta_presupuesto_orden;
    }

    public String getDefecto_reportado_orden() {
        return defecto_reportado_orden;
    }

    public void setDefecto_reportado_orden(String defecto_reportado_orden) {
        this.defecto_reportado_orden = defecto_reportado_orden;
    }

    public String getDiagnostico_orden() {
        return diagnostico_orden;
    }

    public void setDiagnostico_orden(String diagnostico_orden) {
        this.diagnostico_orden = diagnostico_orden;
    }

    public String getEstado_orden() {
        return estado_orden;
    }

    public void setEstado_orden(String estado_orden) {
        this.estado_orden = estado_orden;
    }

    public String getFecha_acepta_presupuesto_orden() {
        return fecha_acepta_presupuesto_orden;
    }

    public void setFecha_acepta_presupuesto_orden(String fecha_acepta_presupuesto_orden) {
        this.fecha_acepta_presupuesto_orden = fecha_acepta_presupuesto_orden;
    }

    public String getFecha_creacion_orden() {
        return fecha_creacion_orden;
    }

    public void setFecha_creacion_orden(String fecha_creacion_orden) {
        this.fecha_creacion_orden = fecha_creacion_orden;
    }

    public String getFecha_entrega_orden() {
        return fecha_entrega_orden;
    }

    public void setFecha_entrega_orden(String fecha_entrega_orden) {
        this.fecha_entrega_orden = fecha_entrega_orden;
    }

    public String getFecha_notifica_cliente_orden() {
        return fecha_notifica_cliente_orden;
    }

    public void setFecha_notifica_cliente_orden(String fecha_notifica_cliente_orden) {
        this.fecha_notifica_cliente_orden = fecha_notifica_cliente_orden;
    }

    public String getFecha_programada_entrega_orden() {
        return fecha_programada_entrega_orden;
    }

    public void setFecha_programada_entrega_orden(String fecha_programada_entrega_orden) {
        this.fecha_programada_entrega_orden = fecha_programada_entrega_orden;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_factura_emitida() {
        return id_factura_emitida;
    }

    public void setId_factura_emitida(int id_factura_emitida) {
        this.id_factura_emitida = id_factura_emitida;
    }

    public int getId_orden() {
        return id_orden;
    }

    public void setId_orden(int id_orden) {
        this.id_orden = id_orden;
    }

    public int getId_serie() {
        return id_serie;
    }

    public void setId_serie(int id_serie) {
        this.id_serie = id_serie;
    }

    public String getMarca_producto_orden() {
        return marca_producto_orden;
    }

    public void setMarca_producto_orden(String marca_producto_orden) {
        this.marca_producto_orden = marca_producto_orden;
    }

    public String getModelo_producto_orden() {
        return modelo_producto_orden;
    }

    public void setModelo_producto_orden(String modelo_producto_orden) {
        this.modelo_producto_orden = modelo_producto_orden;
    }

    public String getNombre_cliente_orden() {
        return nombre_cliente_orden;
    }

    public void setNombre_cliente_orden(String nombre_cliente_orden) {
        this.nombre_cliente_orden = nombre_cliente_orden;
    }

    public String getObservaciones_cliente_orden() {
        return observaciones_cliente_orden;
    }

    public void setObservaciones_cliente_orden(String observaciones_cliente_orden) {
        this.observaciones_cliente_orden = observaciones_cliente_orden;
    }

    public String getSerie_entregada_garantia_orden() {
        return serie_entregada_garantia_orden;
    }

    public void setSerie_entregada_garantia_orden(String serie_entregada_garantia_orden) {
        this.serie_entregada_garantia_orden = serie_entregada_garantia_orden;
    }

    public String getSerie_producto_orden() {
        return serie_producto_orden;
    }

    public void setSerie_producto_orden(String serie_producto_orden) {
        this.serie_producto_orden = serie_producto_orden;
    }

    public String getTecnico_encargado_reparacion_orden() {
        return tecnico_encargado_reparacion_orden;
    }

    public void setTecnico_encargado_reparacion_orden(String tecnico_encargado_reparacion_orden) {
        this.tecnico_encargado_reparacion_orden = tecnico_encargado_reparacion_orden;
    }

    public int getTelefono_cliente_orden() {
        return telefono_cliente_orden;
    }

    public void setTelefono_cliente_orden(int telefono_cliente_orden) {
        this.telefono_cliente_orden = telefono_cliente_orden;
    }

    public String getUsuario_creacion_orden() {
        return usuario_creacion_orden;
    }

    public void setUsuario_creacion_orden(String usuario_creacion_orden) {
        this.usuario_creacion_orden = usuario_creacion_orden;
    }

    public double getValor_presupuesto_orden() {
        return valor_presupuesto_orden;
    }

    public void setValor_presupuesto_orden(double valor_presupuesto_orden) {
        this.valor_presupuesto_orden = valor_presupuesto_orden;
    }

    public String getBateria_orden() {
        return bateria_orden;
    }

    public void setBateria_orden(String bateria_orden) {
        this.bateria_orden = bateria_orden;
    }

    public String getCargador_orden() {
        return cargador_orden;
    }

    public void setCargador_orden(String cargador_orden) {
        this.cargador_orden = cargador_orden;
    }

    public String getCase_orden() {
        return case_orden;
    }

    public void setCase_orden(String case_orden) {
        this.case_orden = case_orden;
    }

    public String getFuente_orden() {
        return fuente_orden;
    }

    public void setFuente_orden(String fuente_orden) {
        this.fuente_orden = fuente_orden;
    }

    public String getOtros_orden() {
        return otros_orden;
    }

    public void setOtros_orden(String otros_orden) {
        this.otros_orden = otros_orden;
    }

    public String getRam1_orden() {
        return ram1_orden;
    }

    public void setRam1_orden(String ram1_orden) {
        this.ram1_orden = ram1_orden;
    }

    public String getRam2_orden() {
        return ram2_orden;
    }

    public void setRam2_orden(String ram2_orden) {
        this.ram2_orden = ram2_orden;
    }

    public String getRam3_orden() {
        return ram3_orden;
    }

    public void setRam3_orden(String ram3_orden) {
        this.ram3_orden = ram3_orden;
    }

    public String getRam4_orden() {
        return ram4_orden;
    }

    public void setRam4_orden(String ram4_orden) {
        this.ram4_orden = ram4_orden;
    }

    public double getTotal_orden() {
        return total_orden;
    }

    public void setTotal_orden(double total_orden) {
        this.total_orden = total_orden;
    }

    public String getDisco_orden() {
        return disco_orden;
    }

    public void setDisco_orden(String disco_orden) {
        this.disco_orden = disco_orden;
    }

    public String getTarjeta_orden() {
        return tarjeta_orden;
    }

    public void setTarjeta_orden(String tarjeta_orden) {
        this.tarjeta_orden = tarjeta_orden;
    }

    public String getTipo_producto_orden() {
        return tipo_producto_orden;
    }

    public void setTipo_producto_orden(String tipo_producto_orden) {
        this.tipo_producto_orden = tipo_producto_orden;
    }

    public String getTipo_trabajo_orden() {
        return tipo_trabajo_orden;
    }

    public void setTipo_trabajo_orden(String tipo_trabajo_orden) {
        this.tipo_trabajo_orden = tipo_trabajo_orden;
    }
    
}
