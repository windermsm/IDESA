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
public class ObjetosDetalleSerie {
    private int id_d_serie;
    private int id_serie;
    private String id_producto;
    private String numero_serie;
    private String descripcion;
    private String usuarioregistra;
    private String usuariomodifica;
    private Date fechacreacion;
    private String estado;

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

    public Date getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public int getId_d_serie() {
        return id_d_serie;
    }

    public void setId_d_serie(int id_d_serie) {
        this.id_d_serie = id_d_serie;
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
    
}
