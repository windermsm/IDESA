package inventory.acceso;

import inventory.objetos.ObjetosPago;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoPago {

    public String insertarPago(ObjetosPago pago, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_pago values (0, '" + pago.getNumero_factura_pago()
                + "', '" + pago.getSerie_factura_pago()
                + "', " + pago.getNumero_doc_pago()
                + ", '" + pago.getTipo_doc_pago()
                + "', '" + pago.getFecha_doc_pago()
                + "', " + pago.getMonto_doc_pago()
                + ", '" + pago.getCuenta_pago()
                + "', '" + pago.getMotivo_pago()
                + "', now(), '" + pago.getEstado_pago() + "', '" + pago.getTipo_pago() + "')"; 
        try {
            return Acceso.ejecutarConsulta(sql, "Pago", "Insertar Pago", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarPago(ObjetosPago pago, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_pago set numero_factura_pago = " + pago.getNumero_factura_pago()
                + ", serie_factura_pago =  '" + pago.getSerie_factura_pago()
                + "', numero_doc_pago = " + pago.getNumero_doc_pago()
                + ", tipo_doc_pago = '" + pago.getTipo_doc_pago()
                + "', fecha_doc_pago =  '" + pago.getFecha_doc_pago()
                + "', monto_doc_pago = " + pago.getMonto_doc_pago()
                + ", cuenta_pago = '" + pago.getCuenta_pago()
                + "', motivo_pago = '" + pago.getMotivo_pago()
                + "', fecha_pago = now(), estado_pago = '" + pago.getEstado_pago()
                + "', tipo_pago =  '" + pago.getTipo_pago() + "' where id_pago = " + pago.getId_pago();
        try {
            return Acceso.ejecutarConsulta(sql, "Pago", "Actualizar Pago", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosPago> buscarPago(String valor, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "select * from m_pago where motivo_pago like '%" + valor + "%'";

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Pago", "Buscar Pago", pUsuario, pTerminal);
            ObjetosPago Registros;
            while (tabla.next()) {
                Registros = new ObjetosPago();
                Registros.setId_pago(tabla.getInt("id_pago"));
                Registros.setNumero_factura_pago(tabla.getString("numero_factura_pago"));
                Registros.setSerie_factura_pago(tabla.getString("serie_factura_pago"));
                Registros.setNumero_doc_pago(tabla.getString("numero_doc_pago"));
                Registros.setTipo_doc_pago(tabla.getString("tipo_doc_pago"));
                Registros.setFecha_doc_pago(tabla.getString("fecha_doc_pago"));
                Registros.setMonto_doc_pago(tabla.getFloat("monto_doc_pago"));
                Registros.setCuenta_pago(tabla.getString("cuenta_pago"));
                Registros.setMotivo_pago(tabla.getString("motivo_pago"));
                Registros.setFecha_pago(tabla.getString("fecha_pago"));
                Registros.setEstado_pago(tabla.getString("estado_pago"));
                Registros.setTipo_pago(tabla.getString("tipo_pago"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosPago> listarPago(String pUsuario, String pTerminal) {

        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_pago order by id_pago desc limit 50";

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Pago", "Listar Pago", pUsuario, pTerminal);
            ObjetosPago Registros;
            while (tabla.next()) {
                Registros = new ObjetosPago();
                Registros.setId_pago(tabla.getInt("id_pago"));
                Registros.setNumero_factura_pago(tabla.getString("numero_factura_pago"));
                Registros.setSerie_factura_pago(tabla.getString("serie_factura_pago"));
                Registros.setNumero_doc_pago(tabla.getString("numero_doc_pago"));
                Registros.setTipo_doc_pago(tabla.getString("tipo_doc_pago"));
                Registros.setFecha_doc_pago(tabla.getString("fecha_doc_pago"));
                Registros.setMonto_doc_pago(tabla.getFloat("monto_doc_pago"));
                Registros.setCuenta_pago(tabla.getString("cuenta_pago"));
                Registros.setMotivo_pago(tabla.getString("motivo_pago"));
                Registros.setFecha_pago(tabla.getString("fecha_pago"));
                Registros.setEstado_pago(tabla.getString("estado_pago"));
                Registros.setTipo_pago(tabla.getString("tipo_pago"));
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