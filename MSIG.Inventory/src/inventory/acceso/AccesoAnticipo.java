package inventory.acceso;

import inventory.objetos.ObjetosAnticipo;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoAnticipo {
    
    public int insertarAnticipo(ObjetosAnticipo anticipo, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        int valor = 0;
        String sql = "INSERT INTO m_anticipo VALUES ( "
                + "   NULL "
                + "  ,'" + anticipo.getNombre_persona_anticipo() + "' "
                + "  ,'" + anticipo.getTelefono_anticipo() + "' "
                + "  ,NOW() "
                + "  ,'" + anticipo.getFecha_entrega_producto_anticipo() + "' "
                + "  ,NULL "
                + "  ,'" + anticipo.getDetalle_anticipo() + "' "
                + "  ," + anticipo.getTotal_anticipo() + "  "
                + "  ,'" + anticipo.getComentarios_anticipo() + "' "
                + "  ,'" + anticipo.getEstado_anticipo() + "' "
                + "  ,'" + anticipo.getUsuario_registro_anticipo() + "' "
                + "  ,NULL "
                + "  ," + anticipo.getId_factura() + ", " + anticipo.getSaldo_anticipo() + ", '" + anticipo.getNit_anticipo() + "' "
                + ")";
        System.out.println(sql);
        try {
            valor = Acceso.ejecutarConsultaKey(sql, "Anticipos", "Insertar Anticipo", pUsuario, pTerminal);
        } catch (Error error) {
            error.getMessage();
            valor = 0;
        } finally {
            Acceso.desconectar();
        }
        
        return valor;
        
    }
    
    public ArrayList<ObjetosAnticipo> listarAnticipo(String pFechaInicial, String pFechaFinal, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosAnticipo> listado = new ArrayList<>();
        AccesoInventario acceso = new AccesoInventario();
        
        String sql = "SELECT  * "
                + "FROM    m_anticipo "
                + "WHERE   DATE_FORMAT(fecha_entrega_persona_aticipo, '%Y-%m-%d') BETWEEN DATE_FORMAT(STR_TO_DATE('" + pFechaInicial + "', '%Y-%m-%d'), '%Y-%m-%d') AND DATE_FORMAT(STR_TO_DATE('" + pFechaFinal + "', '%Y-%m-%d'), '%Y-%m-%d')";

        try {
            ResultSet rs = acceso.listarRegistros(sql, "Anticipo", "Listar Anticipo", pUsuario, pTerminal);
            while (rs.next()) {
                ObjetosAnticipo Objeto = new ObjetosAnticipo();
                Objeto.setId_anticipo(rs.getInt("id_anticipo"));
                Objeto.setNombre_persona_anticipo(rs.getString("nombre_persona_anticipo"));
                Objeto.setTelefono_anticipo(rs.getString("telefono_anticipo"));
                Objeto.setFecha_entrega_persona_aticipo(rs.getString("fecha_entrega_persona_aticipo"));
                Objeto.setFecha_entrega_producto_anticipo(rs.getString("fecha_entrega_producto_anticipo"));
                Objeto.setFecha_uso_anticipo(rs.getString("fecha_uso_anticipo"));
                Objeto.setDetalle_anticipo(rs.getString("detalle_anticipo"));
                Objeto.setTotal_anticipo(rs.getDouble("total_anticipo"));
                Objeto.setComentarios_anticipo(rs.getString("comentarios_anticipo"));
                Objeto.setEstado_anticipo(rs.getString("estado_anticipo"));
                Objeto.setUsuario_registro_anticipo(rs.getString("usuario_registro_anticipo"));
                Objeto.setUsuario_modifico_anticipo(rs.getString("usuario_modifico_anticipo"));
                Objeto.setId_factura(rs.getInt("id_factura"));
                Objeto.setSaldo_anticipo(rs.getDouble("saldo_anticipo"));
                Objeto.setNit_anticipo(rs.getString("nit_anticipo"));
                listado.add(Objeto);
            }
        } catch (Exception error) {
            return null;
        }
        return listado;
        
    }
    
    public ArrayList<ObjetosAnticipo> listarAnticipoPorEstado(String pFechaInicial, String pFechaFinal, String pEstado, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosAnticipo> listado = new ArrayList<>();
        AccesoInventario acceso = new AccesoInventario();
        
        String sql = "SELECT  * "
                + "FROM    m_anticipo "
                + "WHERE   estado_anticipo = '" + pEstado + "'  "
                + " AND DATE_FORMAT(fecha_entrega_persona_aticipo, '%Y-%m-%d') BETWEEN DATE_FORMAT(STR_TO_DATE('" + pFechaInicial + "', '%Y-%m-%d'), '%Y-%m-%d') AND DATE_FORMAT(STR_TO_DATE('" + pFechaFinal + "', '%Y-%m-%d'), '%Y-%m-%d')";

        try {
            ResultSet rs = acceso.listarRegistros(sql, "Anticipo", "Listar Anticipo", pUsuario, pTerminal);
            while (rs.next()) {
                ObjetosAnticipo Objeto = new ObjetosAnticipo();
                Objeto.setId_anticipo(rs.getInt("id_anticipo"));
                Objeto.setNombre_persona_anticipo(rs.getString("nombre_persona_anticipo"));
                Objeto.setTelefono_anticipo(rs.getString("telefono_anticipo"));
                Objeto.setFecha_entrega_persona_aticipo(rs.getString("fecha_entrega_persona_aticipo"));
                Objeto.setFecha_entrega_producto_anticipo(rs.getString("fecha_entrega_producto_anticipo"));
                Objeto.setFecha_uso_anticipo(rs.getString("fecha_uso_anticipo"));
                Objeto.setDetalle_anticipo(rs.getString("detalle_anticipo"));
                Objeto.setTotal_anticipo(rs.getDouble("total_anticipo"));
                Objeto.setComentarios_anticipo(rs.getString("comentarios_anticipo"));
                Objeto.setEstado_anticipo(rs.getString("estado_anticipo"));
                Objeto.setUsuario_registro_anticipo(rs.getString("usuario_registro_anticipo"));
                Objeto.setUsuario_modifico_anticipo(rs.getString("usuario_modifico_anticipo"));
                Objeto.setId_factura(rs.getInt("id_factura"));
                Objeto.setSaldo_anticipo(rs.getDouble("saldo_anticipo"));
                Objeto.setNit_anticipo(rs.getString("nit_anticipo"));
                listado.add(Objeto);
            }
        } catch (Exception error) {
            return null;
        }
        return listado;
        
    }
    
    public ArrayList<ObjetosAnticipo> listarAnticipoPorNombre(String pNombre, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosAnticipo> listado = new ArrayList<>();
        AccesoInventario acceso = new AccesoInventario();
        
        String sql = "SELECT  * "
                + "FROM    m_anticipo "
                + "WHERE   UPPER(nombre_persona_anticipo) like UPPER('%" + pNombre + "%')";

        try {
            ResultSet rs = acceso.listarRegistros(sql, "Anticipo", "Listar por Nombre", pUsuario, pTerminal);
            while (rs.next()) {
                ObjetosAnticipo Objeto = new ObjetosAnticipo();
                Objeto.setId_anticipo(rs.getInt("id_anticipo"));
                Objeto.setNombre_persona_anticipo(rs.getString("nombre_persona_anticipo"));
                Objeto.setTelefono_anticipo(rs.getString("telefono_anticipo"));
                Objeto.setFecha_entrega_persona_aticipo(rs.getString("fecha_entrega_persona_aticipo"));
                Objeto.setFecha_entrega_producto_anticipo(rs.getString("fecha_entrega_producto_anticipo"));
                Objeto.setFecha_uso_anticipo(rs.getString("fecha_uso_anticipo"));
                Objeto.setDetalle_anticipo(rs.getString("detalle_anticipo"));
                Objeto.setTotal_anticipo(rs.getDouble("total_anticipo"));
                Objeto.setComentarios_anticipo(rs.getString("comentarios_anticipo"));
                Objeto.setEstado_anticipo(rs.getString("estado_anticipo"));
                Objeto.setUsuario_registro_anticipo(rs.getString("usuario_registro_anticipo"));
                Objeto.setUsuario_modifico_anticipo(rs.getString("usuario_modifico_anticipo"));
                Objeto.setId_factura(rs.getInt("id_factura"));
                Objeto.setSaldo_anticipo(rs.getDouble("saldo_anticipo"));
                Objeto.setNit_anticipo(rs.getString("nit_anticipo"));
                listado.add(Objeto);
            }
        } catch (Exception error) {
            return null;
        }
        return listado;
        
    }
    
    public ArrayList<ObjetosAnticipo> listarAnticipoPorNit(String pNit, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosAnticipo> listado = new ArrayList<>();
        AccesoInventario acceso = new AccesoInventario();
        
        String sql = "SELECT  * "
                + "FROM    m_anticipo "
                + "WHERE   UPPER(nit_anticipo) like UPPER('%" + pNit + "%')";

        try {
            ResultSet rs = acceso.listarRegistros(sql, "Anticipo", "Listar por Nombre", pUsuario, pTerminal);
            while (rs.next()) {
                ObjetosAnticipo Objeto = new ObjetosAnticipo();
                Objeto.setId_anticipo(rs.getInt("id_anticipo"));
                Objeto.setNombre_persona_anticipo(rs.getString("nombre_persona_anticipo"));
                Objeto.setTelefono_anticipo(rs.getString("telefono_anticipo"));
                Objeto.setFecha_entrega_persona_aticipo(rs.getString("fecha_entrega_persona_aticipo"));
                Objeto.setFecha_entrega_producto_anticipo(rs.getString("fecha_entrega_producto_anticipo"));
                Objeto.setFecha_uso_anticipo(rs.getString("fecha_uso_anticipo"));
                Objeto.setDetalle_anticipo(rs.getString("detalle_anticipo"));
                Objeto.setTotal_anticipo(rs.getDouble("total_anticipo"));
                Objeto.setComentarios_anticipo(rs.getString("comentarios_anticipo"));
                Objeto.setEstado_anticipo(rs.getString("estado_anticipo"));
                Objeto.setUsuario_registro_anticipo(rs.getString("usuario_registro_anticipo"));
                Objeto.setUsuario_modifico_anticipo(rs.getString("usuario_modifico_anticipo"));
                Objeto.setId_factura(rs.getInt("id_factura"));
                Objeto.setSaldo_anticipo(rs.getDouble("saldo_anticipo"));
                Objeto.setNit_anticipo(rs.getString("nit_anticipo"));
                listado.add(Objeto);
            }
        } catch (Exception error) {
            return null;
        }
        return listado;
        
    }
    
    public ObjetosAnticipo buscarAnticipoID(String pId, String pUsuario, String pTerminal) {
        
        ObjetosAnticipo Objeto = new ObjetosAnticipo();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT * FROM m_anticipo WHERE id_anticipo = " + pId;
        
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Anticipo", "Buscar Anticipo", pUsuario, pTerminal);
            while (rs.next()) {
                Objeto.setId_anticipo(rs.getInt("id_anticipo"));
                Objeto.setNombre_persona_anticipo(rs.getString("nombre_persona_anticipo"));
                Objeto.setTelefono_anticipo(rs.getString("telefono_anticipo"));
                Objeto.setFecha_entrega_persona_aticipo(rs.getString("fecha_entrega_persona_aticipo"));
                Objeto.setFecha_entrega_producto_anticipo(rs.getString("fecha_entrega_producto_anticipo"));
                Objeto.setFecha_uso_anticipo(rs.getString("fecha_uso_anticipo"));
                Objeto.setDetalle_anticipo(rs.getString("detalle_anticipo"));
                Objeto.setTotal_anticipo(rs.getDouble("total_anticipo"));
                Objeto.setComentarios_anticipo(rs.getString("comentarios_anticipo"));
                Objeto.setEstado_anticipo(rs.getString("estado_anticipo"));
                Objeto.setUsuario_registro_anticipo(rs.getString("usuario_registro_anticipo"));
                Objeto.setUsuario_registro_anticipo(rs.getString("usuario_modifico_anticipo"));
                Objeto.setId_factura(rs.getInt("id_factura"));
                Objeto.setSaldo_anticipo(rs.getDouble("saldo_anticipo"));
                Objeto.setNit_anticipo(rs.getString("nit_anticipo"));
            }
        } catch (Exception error) {
            return null;
        }
        return Objeto;
        
    }
    
    public String actualizarAnticipo(ObjetosAnticipo pAnticipo, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_anticipo "
                + " SET nombre_persona_anticipo = '" + pAnticipo.getNombre_persona_anticipo() + "', "
                + " telefono_anticipo = '" + pAnticipo.getTelefono_anticipo() + "', "
                + " detalle_anticipo = '" + pAnticipo.getDetalle_anticipo() + "', "
                + " total_anticipo = " + pAnticipo.getTotal_anticipo() + ", "
                + " saldo_anticipo = " + pAnticipo.getSaldo_anticipo() + ", "
                + " comentarios_anticipo = '" + pAnticipo.getComentarios_anticipo() + "', "
                + " estado_anticipo = '" + pAnticipo.getEstado_anticipo() + "', "
                + " usuario_modifico_anticipo = '" + pUsuario + "', fecha_entrega_producto_anticipo = '" + pAnticipo.getFecha_entrega_producto_anticipo() + "', "
                + " saldo_anticipo = " + pAnticipo.getSaldo_anticipo() + ", "
                + " id_factura =  " + pAnticipo.getId_factura() + ", nit_anticipo = '" + pAnticipo.getNit_anticipo() + "' "
                + " WHERE id_anticipo = " + pAnticipo.getId_anticipo();
        try {
            return Acceso.ejecutarConsulta(sql, "Anticipo", "Actualizar Anticipo", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarAnticipoDespachadoDevuelto(ObjetosAnticipo pAnticipo, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_anticipo SET fecha_uso_anticipo = NOW() WHERE id_anticipo = " + pAnticipo.getId_anticipo();
        try {
            return Acceso.ejecutarConsulta(sql, "Anticipo", "Actualizar Anticipo", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
}
