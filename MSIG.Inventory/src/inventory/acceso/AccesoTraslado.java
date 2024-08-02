/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.acceso;

import inventory.objetos.ObjetosTraslado;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Derwin Santa Cruz
 */
public class AccesoTraslado {
    
    public String eliminarTraslado(ObjetosTraslado traslado, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete from m_traslado where id_traslado = " + traslado.getId_traslado();

        try {
            return Acceso.ejecutarConsulta(sql, "Traslado", "Eliminar", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String insertarTraslado(ObjetosTraslado traslado, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_traslado values (NULL, "
                + traslado.getId_sucursal() + ", '"
                + traslado.getFecha_trasaldo() + "', "
                + traslado.getTotal_traslado() + ", '"
                + traslado.getUsuario_traslado() + "', now())";
        try {
            return Acceso.ejecutarConsulta(sql, "Traslado", "Guardar", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosTraslado> listarTrasladosPorFecha(String fecha_inicial, String fecha_final, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * "
                + "from m_traslado "
                + "where fecha_traslado between str_to_date('" + fecha_inicial + "', '%Y-%m-%d') AND str_to_date('" + fecha_final + "', '%Y-%m-%d')";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Historial de Traslados", "Mostrar", pUsuario, pTerminal);
            ObjetosTraslado Registros;
            while (tabla.next()) {
                Registros = new ObjetosTraslado();
                Registros.setId_traslado(tabla.getInt("id_traslado"));
                Registros.setId_sucursal(tabla.getInt("id_sucursal"));
                Registros.setFecha_trasaldo(tabla.getString("fecha_traslado"));
                Registros.setTotal_traslado(tabla.getFloat("total_traslado"));
                Registros.setUsuario_traslado(tabla.getString("usuario_traslado"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public int obtenerIdTraslado(ObjetosTraslado traslado, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        int numero = 0;
        String sql = "select max(id_traslado) as id_traslado "
                + "from m_traslado "
                + "where usuario_traslado = '" + traslado.getUsuario_traslado() + "'"
                + " and id_sucursal = " + traslado.getId_sucursal() + " "
                + " and fecha_traslado = str_to_date('" + traslado.getFecha_trasaldo() + "', '%Y-%m-%d')"
                + " and total_traslado = " + traslado.getTotal_traslado();
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Traslado", "Guardar", pUsuario, pTerminal);
            while (tabla.next()) {
                numero =  tabla.getInt("id_traslado");
            }
        } catch (Exception error) {
            numero = 0;
        } finally {
            Acceso.desconectar();
        }
        return numero;
    }

}