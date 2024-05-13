package inventory.objetos;

public class ObjetosAbono {
    
    private int id_abono;
    private int id_cliente;
    private int id_persona;
    private int id_factura;
    private String fecha_abono;
    private float monto_abono;
    private float saldo_disponible;
    private int dias_transcurridos;
    private float comision_sobre_abono;
    private String nit_persona;
    private String nom_persona;
    private String factura;
    private float total;
    private float monto_credito;
    private float monto_contado;
    private int dias_de_credito;
    private float total_abonado;
    private float saldo_pendiente;
    private String fecha_emision_factura;
    private float total_factura;
    private int id_empleado;
    private String estado_factura;
    private String tipo_venta;
    private float iva_factura;
    private float comision_factura;
    private String fecha;
    private String fecha_limite;
    private String tipo_abono;
    private String numero_abono;
    private String banco_abono;
    private String serie_fel;
    private String numero_fel;

    public float getComision_factura() {
        return comision_factura;
    }

    public void setComision_factura(float comision_factura) {
        this.comision_factura = comision_factura;
    }

    public float getComision_sobre_abono() {
        return comision_sobre_abono;
    }

    public void setComision_sobre_abono(float comision_sobre_abono) {
        this.comision_sobre_abono = comision_sobre_abono;
    }

    public int getDias_de_credito() {
        return dias_de_credito;
    }

    public void setDias_de_credito(int dias_de_credito) {
        this.dias_de_credito = dias_de_credito;
    }

    public int getDias_transcurridos() {
        return dias_transcurridos;
    }

    public void setDias_transcurridos(int dias_transcurridos) {
        this.dias_transcurridos = dias_transcurridos;
    }

    public String getEstado_factura() {
        return estado_factura;
    }

    public void setEstado_factura(String estado_factura) {
        this.estado_factura = estado_factura;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha_abono() {
        return fecha_abono;
    }

    public void setFecha_abono(String fecha_abono) {
        this.fecha_abono = fecha_abono;
    }

    public String getFecha_emision_factura() {
        return fecha_emision_factura;
    }

    public void setFecha_emision_factura(String fecha_emision_factura) {
        this.fecha_emision_factura = fecha_emision_factura;
    }

    public int getId_abono() {
        return id_abono;
    }

    public void setId_abono(int id_abono) {
        this.id_abono = id_abono;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public float getIva_factura() {
        return iva_factura;
    }

    public void setIva_factura(float iva_factura) {
        this.iva_factura = iva_factura;
    }

    public float getMonto_abono() {
        return monto_abono;
    }

    public void setMonto_abono(float monto_abono) {
        this.monto_abono = monto_abono;
    }

    public float getMonto_contado() {
        return monto_contado;
    }

    public void setMonto_contado(float monto_contado) {
        this.monto_contado = monto_contado;
    }

    public float getMonto_credito() {
        return monto_credito;
    }

    public void setMonto_credito(float monto_credito) {
        this.monto_credito = monto_credito;
    }

    public String getNit_persona() {
        return nit_persona;
    }

    public void setNit_persona(String nit_persona) {
        this.nit_persona = nit_persona;
    }

    public String getNom_persona() {
        return nom_persona;
    }

    public void setNom_persona(String nom_persona) {
        this.nom_persona = nom_persona;
    }

    public float getSaldo_disponible() {
        return saldo_disponible;
    }

    public void setSaldo_disponible(float saldo_disponible) {
        this.saldo_disponible = saldo_disponible;
    }

    public float getSaldo_pendiente() {
        return saldo_pendiente;
    }

    public void setSaldo_pendiente(float saldo_pendiente) {
        this.saldo_pendiente = saldo_pendiente;
    }

    public String getTipo_venta() {
        return tipo_venta;
    }

    public void setTipo_venta(String tipo_venta) {
        this.tipo_venta = tipo_venta;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTotal_abonado() {
        return total_abonado;
    }

    public void setTotal_abonado(float total_abonado) {
        this.total_abonado = total_abonado;
    }

    public float getTotal_factura() {
        return total_factura;
    }

    public void setTotal_factura(float total_factura) {
        this.total_factura = total_factura;
    }

    public String getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(String fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getBanco_abono() {
        return banco_abono;
    }

    public void setBanco_abono(String banco_abono) {
        this.banco_abono = banco_abono;
    }

    public String getNumero_abono() {
        return numero_abono;
    }

    public void setNumero_abono(String numero_abono) {
        this.numero_abono = numero_abono;
    }

    public String getTipo_abono() {
        return tipo_abono;
    }

    public void setTipo_abono(String tipo_abono) {
        this.tipo_abono = tipo_abono;
    }

    public String getNumero_fel() {
        return numero_fel;
    }

    public void setNumero_fel(String numero_fel) {
        this.numero_fel = numero_fel;
    }

    public String getSerie_fel() {
        return serie_fel;
    }

    public void setSerie_fel(String serie_fel) {
        this.serie_fel = serie_fel;
    }
    
}
