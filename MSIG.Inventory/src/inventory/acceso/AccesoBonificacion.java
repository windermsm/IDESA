package inventory.acceso;

import inventory.objetos.ObjetosBonificacion;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoBonificacion {

    public String eliminarBonificacion(ObjetosBonificacion pBonificacion, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete m_bonificacion where id_bonificacion = " + pBonificacion.getId_bonificacion();
        try {
            return Acceso.ejecutarConsulta(sql, "Bonificaciones", "Eliminar Bonificacion", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarBonificacion(ObjetosBonificacion pBonificacion, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_bonificacion values (0, '" + pBonificacion.getDescripcion_bonificacion() + "', "
                + pBonificacion.getCantidad_bonificacion() + ", '" + pBonificacion.getEstado_bonificacion() + "', " 
                + pBonificacion.getPorcentaje_bonificacion() + ", '" + pBonificacion.getTipo_empleado() + "', '" + pBonificacion.getId_producto() +"')";
        try {
            return Acceso.ejecutarConsulta(sql, "Bonificaciones", "Insertar Bonificacion", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarBonificacion(ObjetosBonificacion pBonificacion, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_bonificacion set descripcion_bonificacion = '" + pBonificacion.getDescripcion_bonificacion() + "', cantidad_bonificacion = "
                + pBonificacion.getCantidad_bonificacion() + ", estado_bonificacion = '" + pBonificacion.getEstado_bonificacion() + "', porcentaje_bonificacion = "
                + pBonificacion.getPorcentaje_bonificacion() + ", tipo_empleado = '" + pBonificacion.getTipo_empleado() + "', id_producto = '" + pBonificacion.getId_producto() + "' where id_bonificacion = " + pBonificacion.getId_bonificacion();

        try {
            return Acceso.ejecutarConsulta(sql, "Bonificaciones", "Actualizar Bonificacion", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosBonificacion> seleccionarBonificacion(String id_producto, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "";
        
        if (id_producto.equals("") || id_producto.isEmpty()) {
            sql = "select * from m_bonificacion";
        } else {
            sql = "select * from m_bonificacion where id_producto = '" + id_producto + "'";
        }
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Bonificacione", "Seleccionar Bonificacion", pUsuario, pTerminal);
            ObjetosBonificacion Registros;
            while (tabla.next()) {
                Registros = new ObjetosBonificacion();
                Registros.setId_bonificacion(tabla.getInt("id_bonificacion"));
                Registros.setDescripcion_bonificacion(tabla.getString("descripcion_bonificacion"));
                Registros.setCantidad_bonificacion(tabla.getFloat("cantidad_bonificacion"));
                Registros.setEstado_bonificacion(tabla.getString("estado_bonificacion"));
                Registros.setPorcentaje_bonificacion(tabla.getFloat("porcentaje_bonificacion"));
                Registros.setTipo_empleado(tabla.getString("tipo_empleado"));
                Registros.setId_producto(tabla.getString("id_producto"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosBonificacion> buscarBonificacion(String pIdBonificacion, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_bonificacion where descripcion_bonificacion = '" + pIdBonificacion + "'";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Bonificacione", "Buscar Bonificacion", pUsuario, pTerminal);
            ObjetosBonificacion Registros;
            while (tabla.next()) {
                Registros = new ObjetosBonificacion();
                Registros.setId_bonificacion(tabla.getInt("id_bonificacion"));
                Registros.setDescripcion_bonificacion(tabla.getString("descripcion_bonificacion"));
                Registros.setCantidad_bonificacion(tabla.getFloat("cantidad_bonificacion"));
                Registros.setEstado_bonificacion(tabla.getString("estado_bonificacion"));
                Registros.setPorcentaje_bonificacion(tabla.getFloat("porcentaje_bonificacion"));
                Registros.setTipo_empleado(tabla.getString("tipo_empleado"));
                Registros.setId_producto(tabla.getString("id_producto"));
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