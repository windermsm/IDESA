/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.acceso;

import inventory.objetos.ObjetoGarantiaProveedor;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * @since @author FERNANDO
 */
public class AccesoGarantiaProveedor {
    private String estado;

    public int insertarReclamoProveedor(ObjetoGarantiaProveedor pGarantia, String pUsuario, String pTerminal) {

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "";
        sql = "insert into m_garantia_proveedor values(NULL," + pGarantia.getIdProveedor() + ",'" + pGarantia.getSerieFactura() + "','"
                + pGarantia.getNumeroFacturaCompra() + "','" + pGarantia.getMarca() + "','" + pGarantia.getDescripcion() + "','" + pGarantia.getSerieproducto()
                + "','" + pGarantia.getNit() + "','" + pGarantia.getProveedor() + "','"
                + pGarantia.getNumGuia() + "','" + pGarantia.getFechaEnvio() + "','" + pGarantia.getFechaNotificacion()
                + "','" + pGarantia.getNumeroOrden() + "','" + pGarantia.getFechaRevision() + "'," + pGarantia.getDiasEspera() +
                ",'" + pGarantia.getSolucion()+ "','"+pGarantia.getComentario()+"',"+pGarantia.getIdcompra()+",'" + pUsuario + "','" + pTerminal + "','"
                +pGarantia.getFechaFin()+"','"+pGarantia.getIdProducto()+"','"+pGarantia.getFechaGuardar()+"','"+pGarantia.getFechaCompra()+"','"+pGarantia.getNuevaSerieCambioProducto() 
                + "', '" + pGarantia.getTipoGarantia() + "', '" + pGarantia.getTelefonoCliente() + "', '" + pGarantia.getNombreCliente() + 
                "', " + pGarantia.getIdOrden() + ", '" + pGarantia.getQueLeHicieron()+ "', " + pGarantia.getIdSerie() + ", " + pGarantia.getIdFactura() + ")";
        try {
            return Acceso.ejecutarConsultaKey(sql, "Garantia Proveedor", "Insertar Reclamo", pUsuario, pTerminal);
        } catch (Error error) {
            return 0;
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetoGarantiaProveedor> buscarSerie(String pSerie, String pEstado, String pUsuario, String pTerminal) {
        ArrayList list = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();

        String sql = "select  compras.id_compra, \n"
                + "        compras.id_proveedor, \n"
                + "        compras.fecha_factura_compra, \n"
                + "        compras.num_factura_compra, \n"
                + "        compras.serie_factura_compra, \n"
                + "        producto.marca_producto, \n"
                + "        producto.desc_producto, \n"
                + "        personas.nit_persona, \n"
                + "        series.id_serie, \n"
                + "        personas.nom_persona, \n"
                + "        series.numero_serie, \n"
                + "        series.fecha_limite_garantia_proveedor, \n"
                + "        series.id_producto, \n"
                + "        series.estado, \n"
                + "        series.id_factura \n"
                + "from    m_compra as compras, \n"
                + "        m_proveedor as proveedor, \n"
                + "        m_persona as personas, \n"
                + "        m_serie as series, \n"
                + "        m_producto as producto  \n"
                + "where   compras.id_compra = series.id_compra \n"
                + "    and personas.id_persona = proveedor.id_persona \n"
                + "    and producto.id_producto = series.id_producto \n"
                + "    and proveedor.id_proveedor = compras.id_proveedor \n"
                + "    and series.estado in (" + pEstado + ") \n"
                + "    and series.numero_serie ='" + pSerie + "'";
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias Proveedor", "Buscar Serie", pUsuario, pTerminal);
            ObjetoGarantiaProveedor detalles;
            
            if (tabla.first()) {
                
                detalles = new ObjetoGarantiaProveedor();
                detalles.setIdcompra(tabla.getInt("id_compra"));
                detalles.setIdProveedor(tabla.getInt("id_proveedor"));
                detalles.setFechaCompra(tabla.getString("fecha_factura_compra"));
                detalles.setNumeroFacturaCompra(tabla.getString("num_factura_compra"));
                detalles.setSerieFactura(tabla.getString("serie_factura_compra"));
                detalles.setMarca(tabla.getString("marca_producto"));
                detalles.setDescripcion(tabla.getString("desc_producto"));
                detalles.setNit(tabla.getString("nit_persona"));
                detalles.setProveedor(tabla.getString("nom_persona"));
                detalles.setSerieproducto(tabla.getString("numero_serie"));
                detalles.setFechaFin(tabla.getString("fecha_limite_garantia_proveedor"));
                detalles.setIdProducto(tabla.getString("id_producto"));
                detalles.setIdSerie(tabla.getInt("id_serie"));
                detalles.setEstado(tabla.getString("estado"));
                detalles.setIdFactura(tabla.getString("id_factura"));
                estado = tabla.getString("estado");
                list.add(detalles);
                
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return list;
    }
    
    public ArrayList<ObjetoGarantiaProveedor> buscarSolicitudId(String pId, String pUsuario, String pTerminal) {
        
        ArrayList list = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();

        String sql = "SELECT * FROM m_garantia_proveedor WHERE id_garantia_proveedor="+pId+"";
        
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias Proveedor", "Buscar Solicitud ID", pUsuario, pTerminal);
            ObjetoGarantiaProveedor objeto;
            
            if (tabla.first()) {
                objeto = new ObjetoGarantiaProveedor();
                objeto.setNit(tabla.getString("nit_proveedor"));
                objeto.setProveedor(tabla.getString("nombre_proveedor"));
                objeto.setIdcompra(tabla.getInt("id_compra"));
                objeto.setIdProveedor(tabla.getInt("id_proveedor"));
                objeto.setDescripcion(tabla.getString("detalle_producto"));
                objeto.setSerieFactura(tabla.getString("serie_factura_compra"));
                objeto.setComentario(tabla.getString("comentario"));
                objeto.setSolucion(tabla.getString("solucion"));
                objeto.setFechaEnvio(tabla.getString("fecha_envio"));
                objeto.setFechaRevision(tabla.getString("fecha_revision"));
                objeto.setFechaNotificacion(tabla.getString("fecha_notificacion_prov"));
                objeto.setMarca(tabla.getString("marca_producto"));
                objeto.setNumGuia(tabla.getString("num_guia"));
                objeto.setSerieproducto(tabla.getString("serie_producto"));
                objeto.setNumeroFacturaCompra(tabla.getString("num_factura_compra"));
                objeto.setIdProducto(tabla.getString("id_producto"));
                objeto.setFechaCompra(tabla.getString("fecha_compra_producto"));
                objeto.setFechaFin(tabla.getString("fecha_fin_garantia"));
                objeto.setNumeroOrden(tabla.getString("num_orden"));
                objeto.setDiasEspera(tabla.getInt("dias_espera"));
                objeto.setFechaGuardar(tabla.getString("fecha_solicitud"));
                objeto.setIdRegistro(tabla.getInt("id_garantia_proveedor"));
                objeto.setNuevaSerieCambioProducto(tabla.getString("nueva_serie_cambio_producto"));
                objeto.setTipoGarantia(tabla.getString("tipo_garantia"));
                objeto.setTelefonoCliente(tabla.getString("telefono_cliente"));
                objeto.setNombreCliente(tabla.getString("nombre_cliente"));
                objeto.setIdOrden(tabla.getInt("id_orden"));
                objeto.setQueLeHicieron(tabla.getString("que_le_hicieron"));
                objeto.setIdSerie(tabla.getInt("id_serie"));
                objeto.setIdFactura(tabla.getString("id_factura"));
                list.add(objeto);
            }
            
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return list;
    }
    
    public ArrayList<ObjetoGarantiaProveedor> listarGarantiaProveedor(String pFechaInicial, String pFechaFinal, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  * "
                + "FROM    m_garantia_proveedor "
                + "WHERE   DATE_FORMAT(fecha_solicitud, '%Y-%m-%d') BETWEEN DATE_FORMAT(STR_TO_DATE('" + pFechaInicial + "', '%Y-%m-%d'), '%Y-%m-%d') AND DATE_FORMAT(STR_TO_DATE('" + pFechaFinal + "', '%Y-%m-%d'), '%Y-%m-%d')";
        
        try {
            
            ResultSet tabla = Acceso.listarRegistros(sql, "Garantia Proveedor", "Listar Garantia Proveedor", pUsuario, pTerminal);
            ObjetoGarantiaProveedor Registros;
            
            while (tabla.next()) {
                Registros = new ObjetoGarantiaProveedor();
                Registros.setNit(tabla.getString("nit_proveedor"));
                Registros.setProveedor(tabla.getString("nombre_proveedor"));
                Registros.setIdcompra(tabla.getInt("id_compra"));
                Registros.setIdProveedor(tabla.getInt("id_proveedor"));
                Registros.setDescripcion(tabla.getString("detalle_producto"));
                Registros.setSerieFactura(tabla.getString("serie_factura_compra"));
                Registros.setComentario(tabla.getString("comentario"));
                Registros.setSolucion(tabla.getString("solucion"));
                Registros.setFechaEnvio(tabla.getString("fecha_envio"));
                Registros.setFechaRevision(tabla.getString("fecha_revision"));
                Registros.setFechaNotificacion(tabla.getString("fecha_notificacion_prov"));
                Registros.setMarca(tabla.getString("marca_producto"));
                Registros.setNumGuia(tabla.getString("num_guia"));
                Registros.setSerieproducto(tabla.getString("serie_producto"));
                Registros.setNumeroFacturaCompra(tabla.getString("num_factura_compra"));
                Registros.setIdProducto(tabla.getString("id_producto"));
                Registros.setFechaCompra(tabla.getString("fecha_compra_producto"));
                Registros.setFechaFin(tabla.getString("fecha_fin_garantia"));
                Registros.setNumeroOrden(tabla.getString("num_orden"));
                Registros.setDiasEspera(tabla.getInt("dias_espera"));
                Registros.setFechaGuardar(tabla.getString("fecha_solicitud"));
                Registros.setIdRegistro(tabla.getInt("id_garantia_proveedor"));
                Registros.setNuevaSerieCambioProducto(tabla.getString("nueva_serie_cambio_producto"));
                Registros.setTipoGarantia(tabla.getString("tipo_garantia"));
                Registros.setTelefonoCliente(tabla.getString("telefono_cliente"));
                Registros.setNombreCliente(tabla.getString("nombre_cliente"));
                Registros.setIdOrden(tabla.getInt("id_orden"));
                Registros.setQueLeHicieron(tabla.getString("que_le_hicieron"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return lista;
        
    }
    
    public ArrayList<ObjetoGarantiaProveedor> listarGarantiaProveedorPorNombre(String pNombre, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  * "
                + "FROM    m_garantia_proveedor "
                + "WHERE   UPPER(nombre_proveedor) LIKE  UPPER('%" + pNombre + "%')";
        
        try {
            
            ResultSet tabla = Acceso.listarRegistros(sql, "Garantia Proveedor", "Listar Garantia Proveedor Por Nombre", pUsuario, pTerminal);
            ObjetoGarantiaProveedor Registros;
            
            while (tabla.next()) {
                Registros = new ObjetoGarantiaProveedor();
                Registros.setNit(tabla.getString("nit_proveedor"));
                Registros.setProveedor(tabla.getString("nombre_proveedor"));
                Registros.setIdcompra(tabla.getInt("id_compra"));
                Registros.setIdProveedor(tabla.getInt("id_proveedor"));
                Registros.setDescripcion(tabla.getString("detalle_producto"));
                Registros.setSerieFactura(tabla.getString("serie_factura_compra"));
                Registros.setComentario(tabla.getString("comentario"));
                Registros.setSolucion(tabla.getString("solucion"));
                Registros.setFechaEnvio(tabla.getString("fecha_envio"));
                Registros.setFechaRevision(tabla.getString("fecha_revision"));
                Registros.setFechaNotificacion(tabla.getString("fecha_notificacion_prov"));
                Registros.setMarca(tabla.getString("marca_producto"));
                Registros.setNumGuia(tabla.getString("num_guia"));
                Registros.setSerieproducto(tabla.getString("serie_producto"));
                Registros.setNumeroFacturaCompra(tabla.getString("num_factura_compra"));
                Registros.setIdProducto(tabla.getString("id_producto"));
                Registros.setFechaCompra(tabla.getString("fecha_compra_producto"));
                Registros.setFechaFin(tabla.getString("fecha_fin_garantia"));
                Registros.setNumeroOrden(tabla.getString("num_orden"));
                Registros.setDiasEspera(tabla.getInt("dias_espera"));
                Registros.setFechaGuardar(tabla.getString("fecha_solicitud"));
                Registros.setIdRegistro(tabla.getInt("id_garantia_proveedor"));
                Registros.setNuevaSerieCambioProducto(tabla.getString("nueva_serie_cambio_producto"));
                Registros.setTipoGarantia(tabla.getString("tipo_garantia"));
                Registros.setTelefonoCliente(tabla.getString("telefono_cliente"));
                Registros.setNombreCliente(tabla.getString("nombre_cliente"));
                Registros.setIdOrden(tabla.getInt("id_orden"));
                Registros.setQueLeHicieron(tabla.getString("que_le_hicieron"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return lista;
        
    }
    
    public ArrayList<ObjetoGarantiaProveedor> listarGarantiaProveedorPorNit(String pNit, String pUsuario, String pTerminal) {
        
        ArrayList list = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();

        String sql = "SELECT  * "
                + "FROM    m_garantia_proveedor "
                + "WHERE   UPPER(nit_proveedor) LIKE  UPPER('%" + pNit + "%')";
        
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias Proveedor", "Listar Garantía Proveedor", pUsuario, pTerminal);
            ObjetoGarantiaProveedor objeto;
            
            if (tabla.first()) {
                objeto = new ObjetoGarantiaProveedor();
                objeto.setNit(tabla.getString("nit_proveedor"));
                objeto.setProveedor(tabla.getString("nombre_proveedor"));
                objeto.setIdcompra(tabla.getInt("id_compra"));
                objeto.setIdProveedor(tabla.getInt("id_proveedor"));
                objeto.setDescripcion(tabla.getString("detalle_producto"));
                objeto.setSerieFactura(tabla.getString("serie_factura_compra"));
                objeto.setComentario(tabla.getString("comentario"));
                objeto.setSolucion(tabla.getString("solucion"));
                objeto.setFechaEnvio(tabla.getString("fecha_envio"));
                objeto.setFechaRevision(tabla.getString("fecha_revision"));
                objeto.setFechaNotificacion(tabla.getString("fecha_notificacion_prov"));
                objeto.setMarca(tabla.getString("marca_producto"));
                objeto.setNumGuia(tabla.getString("num_guia"));
                objeto.setSerieproducto(tabla.getString("serie_producto"));
                objeto.setNumeroFacturaCompra(tabla.getString("num_factura_compra"));
                objeto.setIdProducto(tabla.getString("id_producto"));
                objeto.setFechaCompra(tabla.getString("fecha_compra_producto"));
                objeto.setFechaFin(tabla.getString("fecha_fin_garantia"));
                objeto.setNumeroOrden(tabla.getString("num_orden"));
                objeto.setDiasEspera(tabla.getInt("dias_espera"));
                objeto.setFechaGuardar(tabla.getString("fecha_solicitud"));
                objeto.setIdRegistro(tabla.getInt("id_garantia_proveedor"));
                objeto.setNuevaSerieCambioProducto(tabla.getString("nueva_serie_cambio_producto"));
                objeto.setTipoGarantia(tabla.getString("tipo_garantia"));
                objeto.setTelefonoCliente(tabla.getString("telefono_cliente"));
                objeto.setNombreCliente(tabla.getString("nombre_cliente"));
                objeto.setIdOrden(tabla.getInt("id_orden"));
                objeto.setQueLeHicieron(tabla.getString("que_le_hicieron"));
                list.add(objeto);
            }
            
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return list;
    }
    
    public ArrayList<ObjetoGarantiaProveedor> listarGarantiaProveedorPorSerie(String pSerie, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  * "
                + "FROM    m_garantia_proveedor "
                + "WHERE   serie_producto =  '" + pSerie + "' "
                + "OR upper(nueva_serie_cambio_producto) = upper('" + pSerie + "') ";
        
        try {
            
            ResultSet tabla = Acceso.listarRegistros(sql, "Garantia Proveedor", "Listar Garantia Proveedor Por Serie", pUsuario, pTerminal);
            ObjetoGarantiaProveedor Registros;
            
            while (tabla.next()) {
                Registros = new ObjetoGarantiaProveedor();
                Registros.setNit(tabla.getString("nit_proveedor"));
                Registros.setProveedor(tabla.getString("nombre_proveedor"));
                Registros.setIdcompra(tabla.getInt("id_compra"));
                Registros.setIdProveedor(tabla.getInt("id_proveedor"));
                Registros.setDescripcion(tabla.getString("detalle_producto"));
                Registros.setSerieFactura(tabla.getString("serie_factura_compra"));
                Registros.setComentario(tabla.getString("comentario"));
                Registros.setSolucion(tabla.getString("solucion"));
                Registros.setFechaEnvio(tabla.getString("fecha_envio"));
                Registros.setFechaRevision(tabla.getString("fecha_revision"));
                Registros.setFechaNotificacion(tabla.getString("fecha_notificacion_prov"));
                Registros.setMarca(tabla.getString("marca_producto"));
                Registros.setNumGuia(tabla.getString("num_guia"));
                Registros.setSerieproducto(tabla.getString("serie_producto"));
                Registros.setNumeroFacturaCompra(tabla.getString("num_factura_compra"));
                Registros.setIdProducto(tabla.getString("id_producto"));
                Registros.setFechaCompra(tabla.getString("fecha_compra_producto"));
                Registros.setFechaFin(tabla.getString("fecha_fin_garantia"));
                Registros.setNumeroOrden(tabla.getString("num_orden"));
                Registros.setDiasEspera(tabla.getInt("dias_espera"));
                Registros.setFechaGuardar(tabla.getString("fecha_solicitud"));
                Registros.setIdRegistro(tabla.getInt("id_garantia_proveedor"));
                Registros.setNuevaSerieCambioProducto(tabla.getString("nueva_serie_cambio_producto"));
                Registros.setTipoGarantia(tabla.getString("tipo_garantia"));
                Registros.setTelefonoCliente(tabla.getString("telefono_cliente"));
                Registros.setNombreCliente(tabla.getString("nombre_cliente"));
                Registros.setIdOrden(tabla.getInt("id_orden"));
                Registros.setQueLeHicieron(tabla.getString("que_le_hicieron"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return lista;
        
    }

    public String actualizarSerieRecibida(String pEstado, String pIdSerie, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_serie SET estado = '" + pEstado + "' WHERE id_serie=" + pIdSerie + ";";

        try {
            return Acceso.ejecutarConsulta(sql, "Garantia Proveedor", "Actualizar Serie Recibida", pUsuario, pTerminal);
        } catch (Error error) {
            System.out.println(error.getMessage());
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String retornarEstado(){
        return estado;
    }
    
    public String modificarSolicitud(ObjetoGarantiaProveedor objeto,int idSolicitud,String pUsuario,String pTerminal){
        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_garantia_proveedor SET id_proveedor = "+objeto.getIdProveedor()+",serie_factura_compra ='"+
               objeto.getSerieFactura() + "',num_factura_compra='" + objeto.getNumeroFacturaCompra() + "',marca_producto='"
                + objeto.getMarca() + "',detalle_producto='" + objeto.getDescripcion() + "',serie_producto='"+objeto.getSerieproducto() + "',nit_proveedor='"
                + objeto.getNit() + "',nombre_proveedor='" + objeto.getProveedor() + "',num_guia='" + objeto.getNumGuia() + "',fecha_envio='"
                + objeto.getFechaEnvio() + "',fecha_notificacion_prov='" + objeto.getFechaNotificacion() + "',num_orden='" + objeto.getNumeroOrden() + "',fecha_revision='"
                + objeto.getFechaRevision() + "',dias_espera=" + objeto.getDiasEspera() + ",solucion='" + objeto.getSolucion() + "',comentario='" + objeto.getComentario() + "',id_compra="
                + objeto.getIdcompra() + ",usuario='" + pUsuario + "',terminal='" + pTerminal + "',fecha_fin_garantia='" + objeto.getFechaFin() + "',id_producto='"
                + objeto.getIdProducto() + "',fecha_solicitud='" + objeto.getFechaGuardar() + "',fecha_compra_producto='" + objeto.getFechaCompra() + "',nueva_serie_cambio_producto = '" + objeto.getNuevaSerieCambioProducto()
                + "', tipo_garantia = '" + objeto.getTipoGarantia() + "', telefono_cliente = '" + objeto.getTelefonoCliente() + "', nombre_cliente = '" + objeto.getNombreCliente() + "' "
                + ", id_orden = " + objeto.getIdOrden() + ", que_le_hicieron = '" + objeto.getQueLeHicieron() + "', id_serie = " + objeto.getIdSerie() + ", id_factura = " + objeto.getIdFactura() + " WHERE id_garantia_proveedor = " + idSolicitud;
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Garantia Proveedor", "Actualizar Solicitud", pUsuario, pTerminal);
        } catch (Error error) {
            System.out.println(error.getMessage());
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
}
