package inventory.acceso;

import inventory.objetos.ObjetosEstadoOrden;

public class AccesoEstadoOrden {

    public String EliminarEstado(String pIdOrdenEstado, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "DELETE FROM m_orde_estado WHERE id_d_orden_estado = " + pIdOrdenEstado;        
        try {
            return Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Eliminar Estado", pUsuario, pTerminal);
        } catch (Exception Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String InsertarEstado(ObjetosEstadoOrden pEstado, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "INSERT INTO d_orden_estado VALUES ( \n"
                + " NULL \n"
                + " , " + pEstado.getId_orden() + " \n"
                + " , '" + pEstado.getDescripcion_d_orden_estado() + "' \n"
                + " , NOW() \n"
                + " , '" + pEstado.getUsuario_d_orden_estado() + "' \n"
                + " )";
        try {
            return Acceso.ejecutarConsulta(sql, "Orden de Servicio", "Guardar Estado", pUsuario, pTerminal);
        } catch (Exception Error) {
            return Error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
}
