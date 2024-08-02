package inventory.acceso;

import inventory.objetos.ObjetosAjuste;

public class AccesoAjuste {

    public void insertarAjuste(ObjetosAjuste ajuste, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into d_ajuste values (NULL, '" + ajuste.getId_producto() + "','" + ajuste.getTipo_ajuste() + "'," + ajuste.getCantidad_ajuste() + ",'" + ajuste.getMotivo_ajuste() + "','" + ajuste.getUsuario_ajuste() + "', now())";
        try {
            Acceso.ejecutarConsulta(sql, "Ajuste Inventario", "Insertar Ajuste", pUsuario, pTerminal);
        } catch (Error error) {
            error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarAjuste(ObjetosAjuste ajuste, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "";
        if (ajuste.getTipo_ajuste().equals("Agregar")) {
            sql = "update m_producto set exi_producto = exi_producto + " + ajuste.getCantidad_ajuste() + " where id_producto = '" + ajuste.getId_producto() + "'";
        } else {
            sql = "update m_producto set exi_producto = exi_producto - " + ajuste.getCantidad_ajuste() + " where id_producto = '" + ajuste.getId_producto() + "'";
        }
        try {
            System.out.println("EJECUTANDO SQL: " + sql);
            return Acceso.ejecutarConsulta(sql, "Ajuste Inventario", "Actualizar Ajuste", pUsuario, pTerminal);
        } catch (Error error) {
            return "EC AccesoAjuste:ActualizarAjuste " + error;
        } finally {
            Acceso.desconectar();
        }
    }
}