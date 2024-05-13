package inventory.acceso;

import inventory.objetos.ObjetosDetalleProforma;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoDetalleProforma {

    public String eliminarDetalleProforma(ObjetosDetalleProforma pDetalleProforma, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete d_proforma where id_d_proforma = " + pDetalleProforma.getId_d_proforma()
                + "id_factura = " + pDetalleProforma.getId_d_proforma();
        try {
            return Acceso.ejecutarConsulta(sql, "Crear Proforma", "Eliminar Detalle Proforma", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarDetalleProforma(ObjetosDetalleProforma pDetalleProforma, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into d_proforma values (0," + pDetalleProforma.getId_proforma() + ",'"
                + pDetalleProforma.getId_producto() + "'," + pDetalleProforma.getCant_d_proforma()
                + "," + pDetalleProforma.getPrecio_venta_d_proforma() + "," + pDetalleProforma.getSub_total_proforma() + ")";
        try {
            return Acceso.ejecutarConsulta(sql, "Crear Proforma", "Insertar Detalle Proforma", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarDetalleProforma(ObjetosDetalleProforma pDetalleProforma, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update d_proforma set id_producto = '" + pDetalleProforma.getId_producto()
                + "', cant_d_proforma = " + pDetalleProforma.getCant_d_proforma() + ", precio_venta_d_proforma = "
                + pDetalleProforma.getPrecio_venta_d_proforma() + ", sub_total_d_proforma = " + pDetalleProforma.getSub_total_proforma()
                + " where id_d_proforma = " + pDetalleProforma.getId_d_proforma() + " and id_proforma = " + pDetalleProforma.getId_proforma();
        try {
            return Acceso.ejecutarConsulta(sql, "Crear Proforma", "Actualizar Detalle Proforma", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosDetalleProforma> seleccionarDetalleProforma(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from d_proforma";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Proforma", "Seleccionar Detalle Proforma", pUsuario, pTerminal);
            ObjetosDetalleProforma Registros;
            while (tabla.next()) {
                Registros = new ObjetosDetalleProforma();
                Registros.setId_d_proforma(tabla.getInt("id_d_proforma"));
                Registros.setId_proforma(tabla.getInt("id_proforma"));
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setCant_d_proforma(tabla.getFloat("cant_d_proforma"));
                Registros.setPrecio_venta_d_proforma(tabla.getFloat("precio_venta_d_proforma"));
                Registros.setSub_total_proforma(tabla.getFloat("sub_total_d_proforma"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosDetalleProforma> buscarDetalleProforma(int pIdProforma, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from d_proforma where id_proforma = " + pIdProforma;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Detalle Proforma", "Buscar Detalle Proforma", pUsuario, pTerminal);
            ObjetosDetalleProforma Registros;
            while (tabla.next()) {
                Registros = new ObjetosDetalleProforma();
                Registros.setId_d_proforma(tabla.getInt("id_d_proforma"));
                Registros.setId_proforma(tabla.getInt("id_proforma"));
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setCant_d_proforma(tabla.getFloat("cant_d_proforma"));

                Registros.setPrecio_venta_d_proforma(tabla.getFloat("precio_venta_d_proforma"));
                Registros.setSub_total_proforma(tabla.getFloat("sub_total_d_proforma"));
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
