/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.objetos;

import java.util.Date;

/**
 *
 * @author Will Rod
 */
public class ObjetosSerie {
    private int id_serie;
    private int id_compra;
    private String id_producto;
    private int id_factura;
    private int id_orden_servicio;
    private String descripcion;
    private String numero_serie;
    private String estado; 
    private String fechacreacion;
    private String usuarioregistra;
    private String fechamodificacion;
    private String usuariomodifica;
    private String fecha_limite_garantia_proveedor;
    private String fecha_limite_garantia_cliente;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(String fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public String getFechamodificacion() {
        return fechamodificacion;
    }

    public void setFechamodificacion(String fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public int getId_orden_servicio() {
        return id_orden_servicio;
    }

    public void setId_orden_servicio(int id_orden_servicio) {
        this.id_orden_servicio = id_orden_servicio;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_serie() {
        return id_serie;
    }

    public void setId_serie(int id_serie) {
        this.id_serie = id_serie;
    }

    public String getNumero_serie() {
        return numero_serie;
    }

    public void setNumero_serie(String numero_serie) {
        this.numero_serie = numero_serie;
    }

    public String getUsuariomodifica() {
        return usuariomodifica;
    }

    public void setUsuariomodifica(String usuariomodifica) {
        this.usuariomodifica = usuariomodifica;
    }

    public String getUsuarioregistra() {
        return usuarioregistra;
    }

    public void setUsuarioregistra(String usuarioregistra) {
        this.usuarioregistra = usuarioregistra;
    }

    public String getFecha_limite_garantia_cliente() {
        return fecha_limite_garantia_cliente;
    }

    public void setFecha_limite_garantia_cliente(String fecha_limite_garantia_cliente) {
        this.fecha_limite_garantia_cliente = fecha_limite_garantia_cliente;
    }

    public String getFecha_limite_garantia_proveedor() {
        return fecha_limite_garantia_proveedor;
    }

    public void setFecha_limite_garantia_proveedor(String fecha_limite_garantia_proveedor) {
        this.fecha_limite_garantia_proveedor = fecha_limite_garantia_proveedor;
    }
    
}
