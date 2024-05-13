/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.acceso;

import inventory.objetos.ObjetosSerie;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Will Rod
 */
public class AccesoSerie {
    
    AccesoInventario Acceso = new AccesoInventario();

    /**
     * Registra el maestro de series
     *
     * @param obj
     * @return el numero de id ingresado o el id actualizado en caso de ser una
     * moficiacion
     */
    public int guardarSerie(ObjetosSerie obj, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        
        int llave = 0;

        String sql = "insert into m_serie "
                + "(id_serie,"
                + "id_compra,"
                + "id_producto,"
                + "id_factura,"
                + "id_orden_servicio,"
                + "descripcion,"
                + "numero_serie,"
                + "estado,"
                + "fechacreacion,"
                + "usuarioregistra, fecha_limite_garantia_proveedor, fecha_limite_garantia_cliente)"
                + "values (null,"
                + obj.getId_compra() + ",'"
                + obj.getId_producto() + "',"
                + obj.getId_factura() + ","
                + obj.getId_orden_servicio() + ",'"
                + obj.getDescripcion() + "','"
                + obj.getNumero_serie().replaceAll("'", "´") + "','"
                + obj.getEstado() + "',now(),'"
                + obj.getUsuarioregistra() + "', '" + obj.getFecha_limite_garantia_proveedor() + "', '" + obj.getFecha_limite_garantia_cliente() + "'"
                + ")";
        try {
            llave = Acceso.ejecutarConsultaKey(sql, pPantalla, pOpcion, pUsuario, pTerminal);
        } catch (Error Error) {
            llave = 0;
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return llave;
        
    }
    
    public String actualizarSerie(ObjetosSerie obj, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {


        String sql = "UPDATE m_serie SET\n"
                + "  id_compra = " + obj.getId_compra() + " \n"
                + "  ,id_producto = '" + obj.getId_producto() + "' \n"
                + "  ,id_factura = " + obj.getId_factura() + "\n"
                + "  ,id_orden_servicio =" + obj.getId_orden_servicio() + " \n"
                + "  ,descripcion = '" + obj.getDescripcion() + "'\n"
                + "  ,numero_serie = '" + obj.getNumero_serie().replaceAll("'", "´") + "'\n"
                + "  ,estado = '" + obj.getEstado() +"'\n"
                + "  ,fechamodificacion = NOW()\n"
                + "  ,usuariomodifica = '" + pUsuario + "'\n"
                + "  ,fecha_limite_garantia_proveedor = '" + obj.getFecha_limite_garantia_proveedor() + "'\n"
                + "  ,fecha_limite_garantia_cliente = '" + obj.getFecha_limite_garantia_cliente() + "'\n"
                + "WHERE id_serie = " + obj.getId_serie() + " AND  estado NOT IN ('Vendido')";
        
        try {
            return Acceso.ejecutarConsulta(sql, "Catalogo Series", "Actualizar Serie", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
    }
    
    
    public String modificarNumeroSerie(String pIdSerie, String pNumeroSerieNueva, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {


        String sql = "UPDATE d_serie SET\n"
                + "  numero_serie = '" + pNumeroSerieNueva.replaceAll("'", "´").trim() + "'\n"
                + "WHERE id_d_serie = " + pIdSerie;
        
        try {
            return Acceso.ejecutarConsulta(sql, "Catalogo Series", "Actualizar Serie", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.auditoria(sql, pPantalla, pOpcion, pUsuario, pTerminal, "Modificó serie manualmente.");
            Acceso.desconectar();
        }
        
    }
    
    public String modificarGarantiaClienteSerie(String pIdSerie, String pFechaGarantiaCliente, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {


        String sql = "UPDATE m_serie SET\n"
                + "  fecha_limite_garantia_cliente = '" + pFechaGarantiaCliente + "'\n"
                + "WHERE id_serie = " + pIdSerie;
        
        try {
            return Acceso.ejecutarConsulta(sql, "Catalogo Series", "Actualizar Fecha Garantia Cliente", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.auditoria(sql, pPantalla, pOpcion, pUsuario, pTerminal, "Modificó fecha manualmente.");
            Acceso.desconectar();
        }
        
    }
    
    public String actualizarEstadoProducto(String pId_Serie, String pId_Venta, String pEstado, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        
        String resultado = "";
        String sql = "UPDATE m_serie SET estado = '" + pEstado + "', id_factura = " + pId_Venta + " WHERE id_serie = " + pId_Serie;
        
        try {
            resultado = Acceso.ejecutarConsulta(sql, "Catalogo Series", "Actualizar Estado Serie", pUsuario, pTerminal);
        } catch (Error error) {
            resultado = "";
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
        return resultado;
    
    }
    
    public String actualizarOrdenServicio(String pSerie, String pId_Orden, String pEstado, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        
        String resultado = "";
        String sql = "UPDATE m_serie SET estado = '" + pEstado + "', id_orden_servicio = " + pId_Orden + " WHERE numero_serie = '" + pSerie + "'";
        
        try {
            resultado = Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Guardar", pUsuario, pTerminal);
        } catch (Error error) {
            resultado = "";
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
        return resultado;
    
    }
    
    public String asignarVentaSerie(String pId_Serie, String pId_factura, String pEstado, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        
        String resultado = "";
        String sql = "UPDATE m_serie SET estado = '" + pEstado + "', id_factura = " + pId_factura + " WHERE id_serie = " + pId_Serie;
        
        try {
            resultado = Acceso.ejecutarConsulta(sql, "Intercambio de Series", "Guardar", pUsuario, pTerminal);
        } catch (Error error) {
            resultado = "";
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
        return resultado;
    
    }
    
    public String asignarVentaSerieDistintoProducto(String pId_Serie, String pId_factura, String pEstado, String pIdProducto, 
            String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        
        String resultado = "";
        String sql = "UPDATE m_serie "
                + "SET id_producto = '" + pIdProducto + "', estado = '" + pEstado + "', id_factura = " + pId_factura + " "
                + "WHERE id_serie = " + pId_Serie;
        
        try {
            resultado = Acceso.ejecutarConsulta(sql, "Intercambio de Series", "Guardar", pUsuario, pTerminal);
        } catch (Error error) {
            resultado = "";
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
        return resultado;
    
    }
    
    public String eliminarSerie(String pId_Serie, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {

        String sql = "DELETE FROM m_serie WHERE id_serie = " + pId_Serie;
        
        try {
            return Acceso.ejecutarConsulta(sql, "Catalogo Series", "Eliminar Serie", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
    }

    public ObjetosSerie buscarSerie(String serie, String producto, String pUsuario, String pTerminal) {
        
        String sql = "select * from m_serie where numero_serie = '"+serie+"' and id_producto = '"+producto+"'";
        ObjetosSerie obj = new ObjetosSerie();
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalogo Series", "Buscar Serie", pUsuario, pTerminal);
            while (tabla.next()) { 
                obj.setId_serie(tabla.getInt("id_serie"));
                obj.setId_compra(tabla.getInt("id_compra"));
                obj.setId_producto(tabla.getString("id_producto"));
                obj.setId_factura(tabla.getInt("id_factura"));
                obj.setId_orden_servicio(tabla.getInt("id_orden_servicio"));
                obj.setDescripcion(tabla.getString("descripcion"));
                obj.setNumero_serie(tabla.getString("numero_serie")); 
                obj.setEstado(tabla.getString("estado"));
                obj.setFechacreacion(tabla.getString("fechacreacion"));
                obj.setFecha_limite_garantia_proveedor(tabla.getString("fecha_limite_garantia_proveedor"));
                obj.setFecha_limite_garantia_cliente(tabla.getString("fecha_limite_garantia_cliente"));
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return obj;
        
    }
    
    public ObjetosSerie buscarSeriePorIdProducto(String producto, String pUsuario, String pTerminal) {
        
        String sql = "select * from m_serie where id_producto = '"+producto+"' order by id_serie";
        ObjetosSerie obj = new ObjetosSerie();
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalogo Series", "Buscar Serie", pUsuario, pTerminal);
            while (tabla.next()) { 
                obj.setId_serie(tabla.getInt("id_serie"));
                obj.setId_compra(tabla.getInt("id_compra"));
                obj.setId_producto(tabla.getString("id_producto"));
                obj.setId_factura(tabla.getInt("id_factura"));
                obj.setId_orden_servicio(tabla.getInt("id_orden_servicio"));
                obj.setDescripcion(tabla.getString("descripcion"));
                obj.setNumero_serie(tabla.getString("numero_serie")); 
                obj.setEstado(tabla.getString("estado"));
                obj.setFechacreacion(tabla.getString("fechacreacion"));
                obj.setFecha_limite_garantia_proveedor(tabla.getString("fecha_limite_garantia_proveedor"));
                obj.setFecha_limite_garantia_cliente(tabla.getString("fecha_limite_garantia_cliente"));
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return obj;
        
    }
    
    
    public ObjetosSerie buscarNumeroSerie(String serie, String pUsuario, String pTerminal) {
        
        String sql = "select * from m_serie where numero_serie = '" + serie + "'";
        ObjetosSerie obj = new ObjetosSerie();
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalogo Series", "Buscar Serie", pUsuario, pTerminal);
            while (tabla.next()) { 
                obj.setId_serie(tabla.getInt("id_serie"));
                obj.setId_compra(tabla.getInt("id_compra"));
                obj.setId_producto(tabla.getString("id_producto"));
                obj.setId_factura(tabla.getInt("id_factura"));
                obj.setId_orden_servicio(tabla.getInt("id_orden_servicio"));
                obj.setDescripcion(tabla.getString("descripcion"));
                obj.setNumero_serie(tabla.getString("numero_serie")); 
                obj.setEstado(tabla.getString("estado"));
                obj.setFechacreacion(tabla.getString("fechacreacion"));
                obj.setFecha_limite_garantia_proveedor(tabla.getString("fecha_limite_garantia_proveedor"));
                obj.setFecha_limite_garantia_cliente(tabla.getString("fecha_limite_garantia_cliente"));
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return obj;
        
    }
    
    public ObjetosSerie buscarSerieEnComentario(String pComentario, String pUsuario, String pTerminal) {
        
        String sql = "SELECT  0 AS id_serie, "
                + "        0 AS id_compra, "
                + "        'OTRO SISTEMA' AS id_producto, "
                + "        id_factura AS id_factura, "
                + "        0 AS id_orden_servicio, "
                + "        'EQUIPO DE OTRO SISTEMA' AS descripcion, "
                + "        comentario_d_factura AS numero_serie, "
                + "        'Vendido' AS estado, "
                + "        '' AS fecha_creacion, "
                + "        'S/G' AS fecha_limite_garantia_proveedor, "
                + "        'S/G' AS fecha_limite_garantia_cliente "
                + "FROM    d_factura "
                + "WHERE   comentario_d_factura = '" + pComentario + "'";
        ObjetosSerie obj = new ObjetosSerie();
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalogo Series", "Buscar Serie", pUsuario, pTerminal);
            while (tabla.next()) { 
                obj.setId_serie(tabla.getInt("id_serie"));
                obj.setId_compra(tabla.getInt("id_compra"));
                obj.setId_producto(tabla.getString("id_producto"));
                obj.setId_factura(tabla.getInt("id_factura"));
                obj.setId_orden_servicio(tabla.getInt("id_orden_servicio"));
                obj.setDescripcion(tabla.getString("descripcion"));
                obj.setNumero_serie(tabla.getString("numero_serie")); 
                obj.setEstado(tabla.getString("estado"));
                obj.setFechacreacion(tabla.getString("fechacreacion"));
                obj.setFecha_limite_garantia_proveedor(tabla.getString("fecha_limite_garantia_proveedor"));
                obj.setFecha_limite_garantia_cliente(tabla.getString("fecha_limite_garantia_cliente"));
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return obj;
        
    }
    
    public ArrayList<ObjetosSerie> listarSeriePorIdCompra(String pIdCompra, String pIdProducto, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosSerie> listado = new ArrayList<>();

        //String sql = "select * from m_serie where id_compra = " + pIdCompra + " AND id_producto = '" + pIdProducto + "' order by numero_serie";
        String sql = "SELECT  * \n"
                + "FROM    vw_series_compradas \n"
                + "WHERE   (id_compra = " + pIdCompra + "  OR id_compra_posterior = " + pIdCompra + ")\n"
                + "    AND ( id_producto = '" + pIdProducto + "' OR id_producto_posterior = '" + pIdProducto + "' )\n"
                + "ORDER BY numero_serie";
        //System.out.println(sql);
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalogo Series", "Buscar Serie", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosSerie obj = new ObjetosSerie();
                obj.setId_serie(tabla.getInt("id_serie"));
                obj.setId_compra(tabla.getInt("id_compra"));
                obj.setId_producto(tabla.getString("id_producto"));
                obj.setId_factura(tabla.getInt("id_factura"));
                obj.setId_orden_servicio(tabla.getInt("id_orden_servicio"));
                obj.setDescripcion(tabla.getString("descripcion"));
                obj.setNumero_serie(tabla.getString("numero_serie"));
                obj.setEstado(tabla.getString("estado"));
                obj.setFechacreacion(tabla.getString("fechacreacion")); 
                obj.setFecha_limite_garantia_proveedor(tabla.getString("fecha_limite_garantia_proveedor"));
                obj.setFecha_limite_garantia_cliente(tabla.getString("fecha_limite_garantia_cliente"));
                listado.add(obj);
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        return listado;
        
    }
      
    public ArrayList<ObjetosSerie> listarSeriePorIdFactura(String pIdFactura, String pIdProducto, String pUsuario, String pTerminal) {

        ArrayList<ObjetosSerie> listado = new ArrayList<>();

        //String sql = "select * from m_serie where id_producto = '" + pIdProducto + "' AND id_factura = " + pIdFactura + " order by numero_serie";
        String sql = "SELECT  * \n"
                + "FROM    vw_series_compradas \n"
                + "WHERE   (id_factura = " + pIdFactura + "  OR id_compra_posterior = " + pIdFactura + ")\n"
                + "    AND ( id_producto = '" + pIdProducto + "' OR id_producto_posterior = '" + pIdProducto + "' )\n"
                + "ORDER BY numero_serie";
        //System.out.println(sql);
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalogo Series", "Buscar Serie", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosSerie obj = new ObjetosSerie();
                obj.setId_serie(tabla.getInt("id_serie"));
                obj.setId_compra(tabla.getInt("id_compra"));
                obj.setId_producto(tabla.getString("id_producto"));
                obj.setId_factura(tabla.getInt("id_factura"));
                obj.setId_orden_servicio(tabla.getInt("id_orden_servicio"));
                obj.setDescripcion(tabla.getString("descripcion"));
                obj.setNumero_serie(tabla.getString("numero_serie"));
                obj.setEstado(tabla.getString("estado"));
                obj.setFechacreacion(tabla.getString("fechacreacion"));
                obj.setFecha_limite_garantia_proveedor(tabla.getString("fecha_limite_garantia_proveedor"));
                obj.setFecha_limite_garantia_cliente(tabla.getString("fecha_limite_garantia_cliente"));
                listado.add(obj);
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        return listado;
        
    }
    
    public String actualizarEstadoSerie(String pId_serie, String pEstado, String pId_Factura, String fecha_garantia, String pPantalla, String pUsuario, String pTerminal) {

        String respuesta = "";
        String sql = "UPDATE m_serie SET estado = '" + pEstado + "', id_factura = " + pId_Factura + ", usuariomodifica = '" + pUsuario 
                + "', fechamodificacion = NOW(), fecha_limite_garantia_cliente = '" + fecha_garantia + "' "
                + "WHERE id_serie = " + pId_serie;
        
        try {
            respuesta =  Acceso.ejecutarConsulta(sql, "Catalogo Series", "Entregar Producto", pUsuario, pTerminal);
        } catch(Exception Error) {
            respuesta = Error.getMessage();
        }
        
        return respuesta;
    }
    
    public String ajusteInventarioQuitarSerie(String pSerie, String pIdProducto, String pPantalla, String pUsuario, String pTerminal) {
        
        String respuesta = "";
        String sql = "UPDATE m_serie SET estado = 'Eliminada', usuariomodifica = '" + pUsuario + "', fechamodificacion = NOW() WHERE id_producto = '" + pIdProducto + "' AND numero_serie = '" + pSerie + "'";
        
        try {
            respuesta =  Acceso.ejecutarConsulta(sql, "Ajuste Inventario", "Guardar", pUsuario, pTerminal);
        } catch(Exception Error) {
            respuesta = Error.getMessage();
        }
        
        return respuesta;
    }
    
    public String sumarTiempoAFechaActual(String Cantidad, String tipo, String pUsuario, String pTerminal) {
        
        String fecha = "";
        String unidad = "";
        
        //Validar el tipo, dias, meses o años
        if(tipo.equals("Dia")) {
            unidad = "DAY";
        }
        
        if(tipo.equals("Mes")) {
            unidad = "MONTH";
        }
        
        if(tipo.equals("Año")) {
            unidad = "YEAR";
        }
        
        String sql = "SELECT DATE_FORMAT(DATE_ADD(NOW(), INTERVAL " + Cantidad + " " + unidad + "), '%Y-%m-%d') FECHA";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalogo Series", "Buscar Serie", pUsuario, pTerminal);
            while (tabla.next()) {
                fecha = tabla.getString("FECHA");
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return fecha;
    }
    
    public int contarSeries(String pIdCompra, String pIdProducto, String pUsuario, String pTerminal) {
        
        int resultado = 0;
        
        String sql = "select count(id_serie) total from m_serie where id_compra = " + pIdCompra + " AND id_producto = '" + pIdProducto + "' order by numero_serie";

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalogo Series", "Buscar Serie", pUsuario, pTerminal);
            while (tabla.next()) {
                resultado = tabla.getInt("total");
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return resultado;
        
    }
    
    public int insertarIntercambio(ObjetosSerie pErroneo, ObjetosSerie pCorrecto, String pMotivo, 
            String pIdProductoAnterior, String pIdProductoPosterior, String pIdCompraAnterior, String pIdCompraPosterior,
            String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        
        int llave = 0;

        String sql = "INSERT INTO m_intercambio VALUE "
                + "(NULL,"
                + "'" + pErroneo.getNumero_serie() + "',"
                + "'" + pCorrecto.getNumero_serie() + "',"
                + "'" + pUsuario + "',"
                + "NOW(),"
                + "'" + pMotivo + "',"
                + "" + pErroneo.getId_factura() + ","
                + "" + pCorrecto.getId_factura() + ", "
                + "'" + pIdProductoAnterior + "', "
                + "'" + pIdProductoPosterior + "', " + pIdCompraAnterior + ", "
                + "" + pIdCompraPosterior + ")";
        try {
            llave = Acceso.ejecutarConsultaKey(sql, pPantalla, pOpcion, pUsuario, pTerminal);
        } catch (Error Error) {
            llave = 0;
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return llave;
        
    }
    
}