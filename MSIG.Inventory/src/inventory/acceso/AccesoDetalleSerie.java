/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.acceso;

import inventory.objetos.ObjetosDetalleSerie;
import inventory.objetos.ObjetosSerie;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Will Rod
 */
public class AccesoDetalleSerie {
    
    AccesoInventario Acceso = new AccesoInventario();

    public ObjetosDetalleSerie buscarDetalleSerie(String serie_detalle, String id_producto, String id_serie, String pUsuario, String pTerminal) {
        
        ObjetosDetalleSerie obj = new ObjetosDetalleSerie();

        String sql = " select ds.*, \n" +
"        mp.desc_producto as descripcionproducto\n" + 
" from   d_serie ds,\n" +
"        m_serie ms,\n" +
"        m_producto mp  \n" +
" where  ds.id_serie = ms.id_serie \n" +
"    and ds.id_producto = mp.id_producto \n" +
"    and ds.numero_serie = '" + serie_detalle + "' \n" +
"    and ms.id_serie = " + id_serie + " \n" +
"    and ds.id_producto = '" + id_producto + "'";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalgo Series", "Buscar Detalle Serie", pUsuario, pTerminal);
            while (tabla.next()) {
                obj.setId_d_serie(tabla.getInt("id_d_serie"));
                obj.setId_serie(tabla.getInt("id_serie"));
                obj.setId_producto(tabla.getString("id_producto"));
                obj.setNumero_serie(tabla.getString("numero_serie"));
                obj.setDescripcion(tabla.getString("descripcion"));
                obj.setFechacreacion(new Date(tabla.getTimestamp("fechacreacion").getTime()));
                obj.setUsuarioregistra(tabla.getString("usuarioregistra"));
                obj.setEstado(tabla.getString("estado"));
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return obj;
    }

    public String guardarDetalleSerie(ObjetosDetalleSerie obj, String pUsuario, String pTerminal) {
        
        String sql = "INSERT INTO d_serie VALUES (NULL, " + obj.getId_serie() + ", '" + obj.getId_producto() + "', "
                + "'" + obj.getNumero_serie().replaceAll("'", "´") + "', '" + obj.getDescripcion() + "', NOW(), '" + pUsuario + "', '', '" + obj.getEstado() + "')";
        
        try {
            return Acceso.ejecutarConsulta(sql, "Detalle Series", "Guardar Detalle Serie", pUsuario, pTerminal);
        } catch (Error Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
    }

    public ArrayList<ObjetosDetalleSerie> buscarDetallesSerie(String id_serie, String id_producto, String pUsuario, String pTerminal) {
        ArrayList<ObjetosDetalleSerie> lista = new ArrayList();

        AccesoSerie acceso_serie = new AccesoSerie();
        ObjetosSerie obj = acceso_serie.buscarSerie(id_serie, id_producto, pUsuario, pTerminal);

        String sql = "select ds.*,ms.descripcion as descripcionproducto from d_serie ds inner join m_serie ms on (ds.id_serie = ms.id_serie) where ds.id_serie = " + obj.getId_serie();
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalgo Series", "Buscar Detalle Serie", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosDetalleSerie Registros = new ObjetosDetalleSerie();
                Registros.setId_d_serie(tabla.getInt("id_d_serie"));
                Registros.setId_serie(tabla.getInt("id_serie"));
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setNumero_serie(tabla.getString("numero_serie"));
                Registros.setDescripcion(tabla.getString("descripcion"));
                Registros.setUsuarioregistra(tabla.getString("usuarioregistra"));
                Registros.setUsuariomodifica(tabla.getString("usuariomodifica"));
                Registros.setEstado(tabla.getString("estado"));
                lista.add(Registros);
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosDetalleSerie> listarDetalleSerie(String id_serie, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosDetalleSerie> lista = new ArrayList();

        String sql = "SELECT  a.id_d_serie,\n"
                + "        a.id_serie,\n"
                + "        a.id_producto,\n"
                + "        a.numero_serie,\n"
                + "        b.desc_producto,\n"
                + "        a.descripcion,\n"
                + "        a.usuarioregistra,\n"
                + "        a.usuariomodifica,\n"
                + "        a.estado\n"
                + "FROM    d_serie AS a,\n"
                + "        m_producto AS b\n"
                + "WHERE   a.id_producto = b.id_producto\n"
                + "    AND a.id_serie = " + id_serie;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Catalgo Series", "Listar Detalle Serie", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosDetalleSerie Registros = new ObjetosDetalleSerie();
                Registros.setId_d_serie(tabla.getInt("id_d_serie"));
                Registros.setId_serie(tabla.getInt("id_serie"));
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setNumero_serie(tabla.getString("numero_serie"));
                Registros.setDescripcion(tabla.getString("desc_producto"));
                Registros.setUsuarioregistra(tabla.getString("usuarioregistra"));
                Registros.setUsuariomodifica(tabla.getString("usuariomodifica"));
                Registros.setEstado(tabla.getString("estado"));
                lista.add(Registros);
            }
        } catch (Exception Error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public boolean validarDetalleSerie(String numero_serie, String id_producto, String pUsuario, String pTerminal) {

        boolean r = false;
        String sql = "select * from d_serie where numero_serie = '" + numero_serie + "' and id_producto = '" + id_producto + "'";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Series", "Validar Detalle Serie", pUsuario, pTerminal);
            while (tabla.next()) {
                r = true;
            }
        } catch (Exception Error) {
            r = false;
        } finally {
            Acceso.desconectar();
        }
        return r;
    }

    public String eliminarDetalleSerie(String numero_serie, String id_producto, String pUsuario, String pTerminal) {

        String sql = "delete from d_serie where numero_serie = '" + numero_serie + "' and id_producto = '" + id_producto + "' ";
        try {
            return Acceso.ejecutarConsulta(sql, "Detalle Series", "Eliminar Detalle Serie", pUsuario, pTerminal);
        } catch (Error Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public boolean validarDetalleSerieKey(int key, String pUsuario, String pTerminal) {

        boolean r = false;
        String sql = "select * from d_serie where id_serie = " + key;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Series", "Validar Detalle Serie Key", pUsuario, pTerminal);
            while (tabla.next()) {
                r = true;
            }
        } catch (Exception Error) {
            r = false;
        } finally {
            Acceso.desconectar();
        }
        return r;
    }
    
    public String eliminarDetalleSerie(String pId_d_Serie, String pId_Serie, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {

        String sql = "DELETE FROM d_serie WHERE id_d_serie = " + pId_d_Serie + " AND id_serie = " + pId_Serie;
        
        try {
            return Acceso.ejecutarConsulta(sql, "Catalogo Series", "Eliminar Detalle Serie", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarEstadoDetalleSerie(String pId_serie, String pEstado, String pPantalla, String pUsuario, String pTerminal) {

        String respuesta = "";
        String sql = "UPDATE d_serie SET estado = '" + pEstado + "', usuariomodifica = '" + pUsuario + "' WHERE id_serie = " + pId_serie;
        
        try {
            respuesta =  Acceso.ejecutarConsulta(sql, "Catalogo Series", "Entregar Producto", pUsuario, pTerminal);
        } catch(Exception Error) {
            respuesta = Error.getMessage();
        }
        
        return respuesta;
    }
    
    public String actualizarEstadoDetalleSerieEntregada(String pId_serie, String pEstado, String pPantalla, String pUsuario, String pTerminal) {

        String respuesta = "";
        String sql = "UPDATE d_serie SET estado = '" + pEstado + "', usuariomodifica = '" + pUsuario + "' WHERE id_serie = " + pId_serie;
        
        try {
            respuesta =  Acceso.ejecutarConsulta(sql, "Catalogo Series", "Entregar Producto", pUsuario, pTerminal);
        } catch(Exception Error) {
            respuesta = Error.getMessage();
        }
        
        return respuesta;
    }
    
}
