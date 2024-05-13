/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.acceso;

import inventory.objetos.ObjetosConsultaGarantiaCliente;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author FERNANDO
 */
public class AccesoConsultaGarantiaCliente {

    public ArrayList<ObjetosConsultaGarantiaCliente> buscarReclamoId(String idReg, String pUsuario, String pTerminal) {
        ArrayList list = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();

        String sql = "SELECT * FROM m_reclamo_garantias WHERE id_reclamo = '" + idReg + "'";
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias", "buscarReclamoId", pUsuario, pTerminal);
            ObjetosConsultaGarantiaCliente detalles;
            if (tabla.first()) {
                detalles = new ObjetosConsultaGarantiaCliente();
                detalles.setIdReclamo(tabla.getInt("id_reclamo"));
                detalles.setProducto(tabla.getString("descripcion_producto"));
                detalles.setMarca(tabla.getString("marca_producto"));
                detalles.setIdFactura(tabla.getString("id_factura"));
                detalles.setIdProducto(tabla.getString("id_producto"));
                detalles.setSerie(tabla.getString("serie_producto"));
                detalles.setCliente(tabla.getString("nombre_cliente"));
                detalles.setNit(tabla.getString("nit_cliente"));
                detalles.setFalla(tabla.getString("falla_producto"));
                detalles.setFechaReclamo(tabla.getString("fecha_reclamo"));
                detalles.setObservaciones(tabla.getString("observaciones"));
                detalles.setComentario(tabla.getString("comentario_producto_entregado"));
                detalles.setSerieEntregada(tabla.getString("serie_producto_entregado"));

                list.add(detalles);
                System.out.println("Datos encontrados id");
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return list;
    }

    public ArrayList<ObjetosConsultaGarantiaCliente> buscarReclamoFecha(String desde, String hasta, String pUsuario, String pTerminal) {
        
        
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select *  from m_reclamo_garantias "
                + "where str_to_date(date_format(fecha_reclamo, '%Y-%m-%d'), '%Y-%m-%d') between str_to_date('"
                + desde + "', '%Y-%m-%d') and str_to_date('"
                + hasta + "', '%Y-%m-%d') order by fecha_reclamo desc";
        
        try {
            
            ResultSet tabla = Acceso.listarRegistros(sql, "Garantia Cliente", "Buscar Reclamo Fecha", pUsuario, pTerminal);
            ObjetosConsultaGarantiaCliente Registros;
            
            while (tabla.next()) {
                Registros = new ObjetosConsultaGarantiaCliente();
                Registros.setIdReclamo(tabla.getInt("id_reclamo"));
                Registros.setProducto(tabla.getString("descripcion_producto"));
                Registros.setMarca(tabla.getString("marca_producto"));
                Registros.setIdFactura(tabla.getString("id_factura"));
                Registros.setIdProducto(tabla.getString("id_producto"));
                Registros.setSerie(tabla.getString("serie_producto"));
                Registros.setCliente(tabla.getString("nombre_cliente"));
                Registros.setNit(tabla.getString("nit_cliente"));
                Registros.setFalla(tabla.getString("falla_producto"));
                Registros.setFechaReclamo(tabla.getString("fecha_reclamo"));
                Registros.setObservaciones(tabla.getString("observaciones"));
                Registros.setComentario(tabla.getString("comentario_producto_entregado"));
                Registros.setSerieEntregada(tabla.getString("serie_producto_entregado"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return lista;
        
    }
    
    public ArrayList<ObjetosConsultaGarantiaCliente> buscarReclamoSerie(String pSerie, String pUsuario, String pTerminal) {
        ArrayList list = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();

        String sql = "SELECT *  FROM m_reclamo_garantias "
                + "WHERE upper(serie_producto) = upper('" + pSerie + "') " 
                + "OR upper(serie_producto_entregado) = upper('" + pSerie + "') "
                + "ORDER BY fecha_reclamo desc";
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias", "Buscar Reclamo Serie", pUsuario, pTerminal);
            ObjetosConsultaGarantiaCliente detalles;
            while (tabla.next()) {
                detalles = new ObjetosConsultaGarantiaCliente();
                detalles.setIdReclamo(tabla.getInt("id_reclamo"));
                detalles.setProducto(tabla.getString("descripcion_producto"));
                detalles.setMarca(tabla.getString("marca_producto"));
                detalles.setIdFactura(tabla.getString("id_factura"));
                detalles.setIdProducto(tabla.getString("id_producto"));
                detalles.setSerie(tabla.getString("serie_producto"));
                detalles.setCliente(tabla.getString("nombre_cliente"));
                detalles.setNit(tabla.getString("nit_cliente"));
                detalles.setFalla(tabla.getString("falla_producto"));
                detalles.setFechaReclamo(tabla.getString("fecha_reclamo"));
                detalles.setObservaciones(tabla.getString("observaciones"));
                detalles.setComentario(tabla.getString("comentario_producto_entregado"));
                detalles.setSerieEntregada(tabla.getString("serie_producto_entregado"));

                list.add(detalles);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return list;
    }

}
