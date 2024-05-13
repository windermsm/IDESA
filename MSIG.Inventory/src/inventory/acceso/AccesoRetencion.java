package inventory.acceso;

import inventory.objetos.ObjetosRetencion;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoRetencion {

    public String eliminarRetencion(ObjetosRetencion pRetencion, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete m_retencion where id_retencion = " + pRetencion.getId_retencion();
        try {
            return Acceso.ejecutarConsulta(sql, "Retenciones", "Eliminar Retencion", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarRetencion(ObjetosRetencion pRetencion, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        
        String fechaEmision = pRetencion.getFecha_emision_retencion().equals("NULL") ? "NULL" : "'" + pRetencion.getFecha_emision_retencion() + "'";
        String fechaPago = pRetencion.getFecha_pago_retencion().equals("NULL") ? "NULL" : "'" + pRetencion.getFecha_pago_retencion() + "'";
        String fechaEnvio = pRetencion.getFecha_envio_retencion().equals("NULL") ? "NULL" : "'" + pRetencion.getFecha_envio_retencion() + "'";
        
        String sql = "insert into m_retencion values (NULL, " + fechaEmision + ", '"
                + pRetencion.getNumero_retencion() + "', " + pRetencion.getMonto_retencion() + ", '" 
                + pRetencion.getNit_retencion() + "', " + pRetencion.getId_compra() + ", " + fechaPago +", '"
                + pRetencion.getDatos_pago_retencion() + "', '" + pRetencion.getGuia_retencion() 
                + "', " + fechaEnvio + ")";
        
        try {
            return Acceso.ejecutarConsulta(sql, "Retenciones", "Insertar Retencion", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarBonificacion(ObjetosRetencion pRetencion, String pUsuario, String pTerminal) {
        
        String fechaEmision = pRetencion.getFecha_emision_retencion().equals("NULL") ? "NULL" : "'" + pRetencion.getFecha_emision_retencion() + "'";
        String fechaPago = pRetencion.getFecha_pago_retencion().equals("NULL") ? "NULL" : "'" + pRetencion.getFecha_pago_retencion() + "'";
        String fechaEnvio = pRetencion.getFecha_envio_retencion().equals("NULL") ? "NULL" : "'" + pRetencion.getFecha_envio_retencion() + "'";
        
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "update m_retencion set fecha_emision_retencion = " + fechaEmision
                + ", numero_retencion = '" + pRetencion.getNumero_retencion() + "', "
                + "monto_retencion = " + pRetencion.getMonto_retencion() + ", "
                + "nit_retencion = '" + pRetencion.getNit_retencion() + "', "
                + "id_compra = " + pRetencion.getId_compra() + ", "
                + "fecha_pago_retencion = " + fechaPago + ", "
                + "datos_pago_retencion = '" + pRetencion.getDatos_pago_retencion() + "', "
                + "guia_retencion = '" + pRetencion.getGuia_retencion() + "', "
                + "fecha_envio_retencion = " + fechaEnvio + " "
                + "where id_retencion = " + pRetencion.getId_retencion();
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Retenciones", "Actualizar Retencion", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosRetencion> seleccionarRetencion(String pCondicion, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "";
        if (pCondicion.equals("") || pCondicion.isEmpty()) {
            sql = "select * from m_retencion";
        } else {
            sql = "select * from m_retencion where " + pCondicion;
        }
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Retenciones", "Seleccionar Retencion", pUsuario, pTerminal);
            ObjetosRetencion Registros;
            while (tabla.next()) {
                Registros = new ObjetosRetencion();
                Registros.setId_retencion(tabla.getInt("id_retencion"));
                Registros.setFecha_emision_retencion(tabla.getString("fecha_emision_retencion"));
                Registros.setNumero_retencion(tabla.getString("numero_retencion"));
                Registros.setMonto_retencion(tabla.getDouble("monto_retencion"));
                Registros.setNit_retencion(tabla.getString("nit_retencion"));
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setFecha_pago_retencion(tabla.getString("fecha_pago_retencion"));
                Registros.setDatos_pago_retencion(tabla.getString("datos_pago_retencion"));
                Registros.setGuia_retencion(tabla.getString("guia_retencion"));
                Registros.setFecha_envio_retencion(tabla.getString("fecha_envio_retencion"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ObjetosRetencion buscarRetencion(String pCondicion, String pUsuario, String pTerminal) {
        ObjetosRetencion Registro = new ObjetosRetencion();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "";
        
        if (pCondicion.equals("") || pCondicion.isEmpty()) {
            sql = "select * from m_retencion";
        } else {
            sql = "select * from m_retencion " + pCondicion;
        }
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Retenciones", "Seleccionar Retencion", pUsuario, pTerminal);
            while (tabla.next()) {
                Registro.setId_retencion(tabla.getInt("id_retencion"));
                Registro.setFecha_emision_retencion(tabla.getString("fecha_emision_retencion"));
                Registro.setNumero_retencion(tabla.getString("numero_retencion"));
                Registro.setMonto_retencion(tabla.getDouble("monto_retencion"));
                Registro.setNit_retencion(tabla.getString("nit_retencion"));
                Registro.setId_compra(tabla.getInt("id_compra"));
                Registro.setFecha_pago_retencion(tabla.getString("fecha_pago_retencion"));
                Registro.setDatos_pago_retencion(tabla.getString("datos_pago_retencion"));
                Registro.setGuia_retencion(tabla.getString("guia_retencion"));
                Registro.setFecha_envio_retencion(tabla.getString("fecha_envio_retencion"));
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return Registro;
    }
    
}