package inventory.acceso;

import inventory.objetos.ObjetosPersona;
import inventory.objetos.ObjetosProveedor;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoProveedor {

    public String eliminarProveedor(ObjetosProveedor pProveedor, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete m_proveedor where id_proveedor = " + pProveedor.getId_proveedor();
        try {
            return Acceso.ejecutarConsulta(sql, "Proveedores", "Eliminar Proveedor", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarProveedor(ObjetosProveedor pProveedor, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_proveedor values (NULL," + pProveedor.getId_persona() + ",'"
                + pProveedor.getDir_proveedor() + "'," + pProveedor.getTel_proveedor() + ", '" + pProveedor.getCuenta_proveedor() + "')";
        try {
            return Acceso.ejecutarConsulta(sql, "Proveedores", "Insertar Proveedor", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarProveedor(ObjetosProveedor pProveedor, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_proveedor set dir_proveedor = '" + pProveedor.getDir_proveedor() + "', tel_proveedor = "
                + pProveedor.getTel_proveedor() + ", numero_cuenta_proveedor = '" + pProveedor.getCuenta_proveedor() + "'  where id_proveedor = " + pProveedor.getId_proveedor()
                + " and id_persona = " + pProveedor.getId_persona();
        try {
            return Acceso.ejecutarConsulta(sql, "Proveedores", "Actualizar Proveedor", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosProveedor> seleccionarProveedor(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_proveedor";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Proveedor", "Seleccionar Proveedor", pUsuario, pTerminal);
            ObjetosProveedor Registros;
            while (tabla.next()) {
                Registros = new ObjetosProveedor();
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setId_persona(tabla.getInt("id_persona"));
                Registros.setDir_proveedor(tabla.getString("dir_proveedor"));
                Registros.setTel_proveedor(tabla.getInt("tel_proveedor"));
                Registros.setCuenta_proveedor(tabla.getString("numero_cuenta_proveedor"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosProveedor> buscarProveedor(int pIdPersona, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_proveedor where id_persona = " + pIdPersona;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Proveedor", "Buscar Proveedor", pUsuario, pTerminal);
            ObjetosProveedor Registros;
            while (tabla.next()) {
                Registros = new ObjetosProveedor();
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setId_persona(tabla.getInt("id_persona"));
                Registros.setDir_proveedor(tabla.getString("dir_proveedor"));
                Registros.setTel_proveedor(tabla.getInt("tel_proveedor"));
                Registros.setCuenta_proveedor(tabla.getString("numero_cuenta_proveedor"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosPersona> listarNombreDeProveedores(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select distinct b.nom_persona as nombre from m_proveedor as a, m_persona as b where a.id_persona = b.id_persona";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Proveedor", "Buscar Proveedor", pUsuario, pTerminal);
            ObjetosPersona Registros;
            while (tabla.next()) {
                Registros = new ObjetosPersona();
                Registros.setNom_persona(tabla.getString("nombre"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
}