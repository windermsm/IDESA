package inventory.acceso;

import inventory.objetos.ObjetosPersona;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoPersona {

    public String eliminarPersona(ObjetosPersona pPersona, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete m_persona where id_persona = " + pPersona.getId_persona();
        try {
            return Acceso.ejecutarConsulta(sql, "Persona", "Eliminar Persona", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarPersona(ObjetosPersona pPersona, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_persona values (0,'" + pPersona.getNit_persona()
                + "','" + pPersona.getNom_persona() + "')";
        try {
            return Acceso.ejecutarConsulta(sql, "Persona", "Inserta Persona", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarPersona(ObjetosPersona pPersona, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_persona set nit_persona = '" + pPersona.getNit_persona()
                + "', nom_persona = '" + pPersona.getNom_persona() + "' where id_persona = "
                + pPersona.getId_persona();
        try {
            return Acceso.ejecutarConsulta(sql, "Persona", "Actualizar Persona", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosPersona> seleccionarPersona(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_persona";

        try {
            ResultSet tablaPersona = Acceso.listarRegistros(sql, "Persona", "Selecionar Persona", pUsuario, pTerminal);
            ObjetosPersona RegistrosPersona;
            while (tablaPersona.next()) {
                RegistrosPersona = new ObjetosPersona();
                RegistrosPersona.setId_persona(tablaPersona.getInt("id_persona"));
                RegistrosPersona.setNit_persona(tablaPersona.getString("nit_persona"));
                RegistrosPersona.setNom_persona(tablaPersona.getString("nom_persona"));
                lista.add(RegistrosPersona);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosPersona> buscarPersona(String pNitPersona, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_persona where UPPER(nit_persona) = UPPER('" + pNitPersona + "')";
        
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Persona", "Buscar Persona", pUsuario, pTerminal);
            if (tabla != null) {
                ObjetosPersona Registros;
                while (tabla.next()) {
                    Registros = new ObjetosPersona();
                    Registros.setId_persona(tabla.getInt("id_persona"));
                    Registros.setNit_persona(tabla.getString("nit_persona"));
                    Registros.setNom_persona(tabla.getString("nom_persona"));
                    lista.add(Registros);
                }
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosPersona> buscarPersonaPorNombre(String pNombre, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_persona where nom_persona = '" + pNombre + "'";
        
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Persona", "Buscar Persona", pUsuario, pTerminal);
            if (tabla != null) {
                ObjetosPersona Registros;
                while (tabla.next()) {
                    Registros = new ObjetosPersona();
                    Registros.setId_persona(tabla.getInt("id_persona"));
                    Registros.setNit_persona(tabla.getString("nit_persona"));
                    Registros.setNom_persona(tabla.getString("nom_persona"));
                    lista.add(Registros);
                }
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }
    
}