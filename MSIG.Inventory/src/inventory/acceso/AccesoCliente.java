package inventory.acceso;

import inventory.objetos.ObjetosCliente;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoCliente {

    public String eliminarCliente(ObjetosCliente pCliente, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete m_cliente where id_cliente = " + pCliente.getId_cliente();
        try {
            return Acceso.ejecutarConsulta(sql, "Clientes", "Eliminar Cliente", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarCliente(ObjetosCliente pCliente, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_cliente values (0," + pCliente.getId_persona() + ",'"
                + pCliente.getDir_cliente() + "'," + pCliente.getTel_cliente() + ", " + pCliente.getCelular_cliente() + ", '" + pCliente.getEmail_cliente() + "', " + pCliente.getLimite_credito() + ", " + pCliente.getDias_de_credito() + ", '" + pCliente.getTipo_cliente() + "')";
        try {
            return Acceso.ejecutarConsulta(sql, "Clientes", "Insertar Cliente", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarCliente(ObjetosCliente pCliente, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_cliente set dir_cliente = '" + pCliente.getDir_cliente() + "', tel_cliente = "
                + pCliente.getTel_cliente() + ", celular_cliente = " + pCliente.getCelular_cliente() + ", email_cliente = '"
                + pCliente.getEmail_cliente() + "', limite_credito = " + pCliente.getLimite_credito() + ", dias_de_credito = " + pCliente.getDias_de_credito()
                + ", tipo_cliente = '" + pCliente.getTipo_cliente() + "' where id_cliente = " + pCliente.getId_cliente()
                + " and id_persona = " + pCliente.getId_persona();
        try {
            return Acceso.ejecutarConsulta(sql, "Clientes", "Actualizar Cliente", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarDireccionCliente(String pId_cliente, String pDireccion, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_cliente set dir_cliente = '" + pDireccion.replaceAll("'", "´") + "' where id_cliente = " + pId_cliente;
        try {
            return Acceso.ejecutarConsulta(sql, pPantalla, pOpcion, pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarNombreFactura(String pId_factura, String pNombre, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_factura set nombre_factura = replace('" + pNombre.replaceAll("'", "´") + "', ',', ' ') where id_factura = " + pId_factura;
        try {
            return Acceso.ejecutarConsulta(sql, pPantalla, pOpcion, pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosCliente> seleccionarCliente(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_cliente";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Clientes", "Seleccionar Cliente", pUsuario, pTerminal);
            ObjetosCliente Registros;
            while (tabla.next()) {
                Registros = new ObjetosCliente();
                Registros.setId_cliente(tabla.getInt("id_cliente"));
                Registros.setId_persona(tabla.getInt("id_persona"));
                Registros.setDir_cliente(tabla.getString("dir_cliente"));
                Registros.setTel_cliente(tabla.getInt("tel_cliente"));
                Registros.setCelular_cliente(tabla.getInt("celular_cliente"));
                Registros.setEmail_cliente(tabla.getString("email_cliente"));
                Registros.setLimite_credito(tabla.getFloat("limite_credito"));
                Registros.setDias_de_credito(tabla.getInt("dias_de_credito"));
                Registros.setTipo_cliente(tabla.getString("tipo_cliente"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosCliente> buscarCliente(int pIdPersona, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_cliente where id_persona = " + pIdPersona;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Clientes", "Buscar Cliente", pUsuario, pTerminal);
            ObjetosCliente Registros;
            while (tabla.next()) {
                Registros = new ObjetosCliente();
                Registros.setId_cliente(tabla.getInt("id_cliente"));
                Registros.setId_persona(tabla.getInt("id_persona"));
                Registros.setDir_cliente(tabla.getString("dir_cliente"));
                Registros.setTel_cliente(tabla.getInt("tel_cliente"));
                Registros.setCelular_cliente(tabla.getInt("celular_cliente"));
                Registros.setEmail_cliente(tabla.getString("email_cliente"));
                Registros.setLimite_credito(tabla.getFloat("limite_credito"));
                Registros.setDias_de_credito(tabla.getInt("dias_de_credito"));
                Registros.setTipo_cliente(tabla.getString("tipo_cliente"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public String buscarNombreCliente(int id, String pUsuario, String pTerminal) {
        String valor = null;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select nom_persona from m_persona where id_persona = (select id_persona from m_cliente where id_cliente = " + id + ")";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nombre Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getString("nom_persona");
            }
        } catch (Exception error) {
            return null;
        }
        return valor;
    }

    public String buscarNitCliente(int id, String pUsuario, String pTerminal) {
        String valor = null;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select nit_persona from m_persona where id_persona = (select id_persona from m_cliente where id_cliente = " + id + ")";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Clientes", "Buscar Nit Cliente", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getString("nit_persona");
            }
        } catch (Exception error) {
        }
        return valor;
    }
}