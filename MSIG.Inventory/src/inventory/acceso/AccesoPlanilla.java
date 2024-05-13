package inventory.acceso;

import inventory.objetos.ObjetosPlanilla;
import java.sql.ResultSet;

public class AccesoPlanilla {

    public String insertaPlanilla(ObjetosPlanilla pPlanilla, String pUsuario, String pTerminal) {

        String resultado = "";
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "INSERT INTO m_planilla VALUES ( "
                + "   NULL "
                + "  ," + pPlanilla.getId_sucursal() + " "
                + "  ," + pPlanilla.getAnio_planilla() + " "
                + "  ,'" + pPlanilla.getMes_planilla() + "' "
                + "  ," + pPlanilla.getPeriodo_planilla() + "  "
                + "  ,'" + pPlanilla.getFecha_inicial_planilla() + "' "
                + "  ,'" + pPlanilla.getFecha_inicial_planilla() + "' "
                + "  ,NOW() "
                + "  ,'" + pPlanilla.getUsuario_elaboro_planilla() + "' "
                + "  ,'" + pPlanilla.getTipo_planilla() + "' "
                + "  ,'" + pPlanilla.getEstado_planilla() + "' "
                + "  ,'" + pPlanilla.getComentario_planilla() + "' "
                + ")";
        try {
            resultado = Acceso.ejecutarConsulta(sql, "Generar Planilla", "Guardar", pUsuario, pTerminal);
        } catch (Error error) {
            resultado = error.getMessage();
        } finally {
            Acceso.desconectar();
        }

        return resultado;
    }
    
    public String actualizarPlanilla(ObjetosPlanilla pPlanilla, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_planilla SET "
                + "   id_planilla = " + pPlanilla.getId_planilla() + " "
                + "  ,id_sucursal = " + pPlanilla.getId_sucursal() + " "
                + "  ,anio_planilla = " + pPlanilla.getAnio_planilla() + " "
                + "  ,mes_planilla = '" + pPlanilla.getMes_planilla() + "' "
                + "  ,periodo_planilla = " + pPlanilla.getPeriodo_planilla() + " "
                + "  ,fecha_inicial_planilla = '" + pPlanilla.getFecha_inicial_planilla() + "' "
                + "  ,fecha_final_planilla = '" + pPlanilla.getFecha_final_planilla() + "' "
                + "  ,fecha_elaboro_planilla = NOW() "
                + "  ,usuario_elaboro_planilla = '" + pPlanilla.getUsuario_elaboro_planilla() + "' "
                + "  ,tipo_planilla = '" + pPlanilla.getTipo_planilla() + "' "
                + "  ,estado_planilla = '" + pPlanilla.getEstado_planilla() + "' "
                + "  ,comentario_planilla = '" + pPlanilla.getComentario_planilla() + "' "
                + "WHERE id_planilla = " + pPlanilla.getId_planilla();
        try {
            return Acceso.ejecutarConsulta(sql, "Generar Planilla", "Actualizar", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public ObjetosPlanilla seleccionarPlanillaPorPeriodo(int pAnio, String pMes, int pPeriodo, String pUsuario, String pTerminal) { 
        ObjetosPlanilla Registro = new ObjetosPlanilla();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT * FROM m_planilla WHERE anio_planilla = " + pAnio + " AND mes_planilla = '" + pMes + "' AND periodo_planilla = " + pPeriodo;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Generar Planilla", "Buscar", pUsuario, pTerminal);
            while (tabla.next()) {
                Registro.setId_planilla(tabla.getInt("id_planilla"));
                Registro.setId_sucursal(tabla.getInt("id_sucursal"));
                Registro.setAnio_planilla(tabla.getInt("anio_planilla"));
                Registro.setMes_planilla(tabla.getString("mes_planilla"));
                Registro.setPeriodo_planilla(tabla.getInt("periodo_planilla"));
                Registro.setFecha_inicial_planilla(tabla.getString("fecha_inicial_planilla"));
                Registro.setFecha_final_planilla(tabla.getString("fecha_final_planilla"));
                Registro.setFecha_elaboro_planilla(tabla.getString("fecha_elaboro_planilla"));
                Registro.setUsuario_elaboro_planilla(tabla.getString("usuario_elaboro_planilla"));
                Registro.setTipo_planilla(tabla.getString("tipo_planilla"));
                Registro.setEstado_planilla(tabla.getString("estado_planilla"));
                Registro.setComentario_planilla(tabla.getString("comentario_planilla"));
            }
        } catch (Exception error) {
            Registro = null;
        } finally {
            Acceso.desconectar();
        }
        return Registro;
    }
    
    public ObjetosPlanilla seleccionarPlanillaPorId(int id, String pUsuario, String pTerminal) { 
        ObjetosPlanilla Registro = new ObjetosPlanilla();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT * FROM m_planilla WHERE id_planilla = " + id;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Generar Planilla", "Buscar", pUsuario, pTerminal);
            while (tabla.next()) {
                Registro.setId_planilla(tabla.getInt("id_planilla"));
                Registro.setId_sucursal(tabla.getInt("id_sucursal"));
                Registro.setAnio_planilla(tabla.getInt("anio_planilla"));
                Registro.setMes_planilla(tabla.getString("mes_planilla"));
                Registro.setPeriodo_planilla(tabla.getInt("periodo_planilla"));
                Registro.setFecha_inicial_planilla(tabla.getString("fecha_inicial_planilla"));
                Registro.setFecha_final_planilla(tabla.getString("fecha_final_planilla"));
                Registro.setFecha_elaboro_planilla(tabla.getString("fecha_elaboro_planilla"));
                Registro.setUsuario_elaboro_planilla(tabla.getString("usuario_elaboro_planilla"));
                Registro.setTipo_planilla(tabla.getString("tipo_planilla"));
                Registro.setEstado_planilla(tabla.getString("estado_planilla"));
                Registro.setComentario_planilla(tabla.getString("comentario_planilla"));
            }
        } catch (Exception error) {
            Registro = null;
        } finally {
            Acceso.desconectar();
        }
        return Registro;
    }
    
}