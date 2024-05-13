package inventory.acceso;

import inventory.objetos.ObjetosLoggin;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoLoggin {

    public ArrayList<ObjetosLoggin> seleccionarLoggin(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from loggin";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Loggin", "Seleccionar Loggin", pUsuario, pTerminal);
            ObjetosLoggin Registros;
            while (tabla.next()) {
                Registros = new ObjetosLoggin();
                Registros.setId_loggin(tabla.getInt("id_loggin"));
                Registros.setUser_loggin(tabla.getString("user_loggin"));
                Registros.setEstado_loggin(tabla.getString("estado_loggin"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public String actualizar(ObjetosLoggin pLoggin, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update loggin set user_loggin = '" + pLoggin.getUser_loggin()
                + "', estado_loggin = '" + pLoggin.getEstado_loggin() + "' where id_loggin = " + pLoggin.getId_loggin();
        try {
            return Acceso.ejecutarConsulta(sql, "Loggin", "Actualizar", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
}