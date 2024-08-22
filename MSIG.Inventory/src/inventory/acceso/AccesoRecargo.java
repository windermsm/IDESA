package inventory.acceso;

import inventory.objetos.ObjetosRecargo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccesoRecargo {

    private AccesoInventario acceso = new AccesoInventario();
    
    public ObjetosRecargo buscarRecargoPorId(String id) {
        PreparedStatement pst;
        ResultSet datos;
        ObjetosRecargo recargo = new ObjetosRecargo();; 
        String sql = "SELECT * FROM m_recargo WHERE id_recargo = '" + id + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();
            if (datos.first()) {
                recargo.setId_recargo(datos.getInt("id_recargo"));
                recargo.setAbreviatura(datos.getString("abreviatura"));
                recargo.setDescripcion(datos.getString("descripcion"));
                recargo.setEstado(datos.getString("estado"));
                recargo.setValor(datos.getDouble("valor"));
            }
        } catch (SQLException e) {
             return null;
        } finally {
            acceso.desconectar();
        }
        return recargo;
    }
    
    public ObjetosRecargo buscarRecargoPorAbreviatura(String abreviatura) {
        PreparedStatement pst;
        ResultSet datos;
        ObjetosRecargo recargo = new ObjetosRecargo();; 
        String sql = "SELECT * FROM m_recargo WHERE abreviatura = '" + abreviatura + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();
            if (datos.first()) {
                recargo.setId_recargo(datos.getInt("id_recargo"));
                recargo.setAbreviatura(datos.getString("abreviatura"));
                recargo.setDescripcion(datos.getString("descripcion"));
                recargo.setEstado(datos.getString("estado"));
                recargo.setValor(datos.getDouble("valor"));
            }
        } catch (SQLException e) {
             return null;
        } finally {
            acceso.desconectar();
        }
        return recargo;
    }
    
    public ArrayList<ObjetosRecargo> retornaRecargosActivos(String pUsuario, String pTerminal) {

        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_recargo where estado = 'Activo'";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Recargo", "Retorna todos los recargos", pUsuario, pTerminal);
            ObjetosRecargo registros;

            while (tabla.next()) {
                registros = new ObjetosRecargo();
                registros.setId_recargo(tabla.getInt("id_recargo"));
                registros.setAbreviatura(tabla.getString("abreviatura"));
                registros.setDescripcion(tabla.getString("descripcion"));
                registros.setValor(tabla.getDouble("valor"));
                lista.add(registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }
    
}