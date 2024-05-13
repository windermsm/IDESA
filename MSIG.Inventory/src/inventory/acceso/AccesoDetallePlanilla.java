package inventory.acceso;

import inventory.objetos.ObjetosDetallePlanilla;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccesoDetallePlanilla {
    
    public String insertaPlanilla(ObjetosDetallePlanilla pPlanilla, String pUsuario, String pTerminal) {

        String resultado = "";
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "INSERT INTO d_planilla VALUES ( "
                + "   " + pPlanilla.getId_d_planilla() + " "
                + "  ," + pPlanilla.getId_planilla() + " "
                + "  ," + pPlanilla.getId_empleado() + " "
                + "  ,'" + pPlanilla.getNombre_d_planilla() + "' "
                + "  ,'" + pPlanilla.getPuesto_d_planilla() + "' "
                + "  ," + pPlanilla.getSalario_d_planilla() + " "
                + "  ," + pPlanilla.getBonificacion_d_planilla() + " "
                + "  ," + pPlanilla.getIgsss_d_planilla() + " "
                + "  ," + pPlanilla.getComision_d_planilla() + " "
                + "  ," + pPlanilla.getHoras_extras_d_planilla() + " "
                + "  ," + pPlanilla.getDescuentos_d_planilla() + " "
                + "  ," + pPlanilla.getCreditos_d_planilla() + " "
                + "  ," + pPlanilla.getVales_d_planilla() + " "
                + "  ,'" + pPlanilla.getComentarios_d_planilla() + "' "
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
    
    
    public ArrayList<ObjetosDetallePlanilla> listarPlanilla(int pId, String pUsuario, String pTerminal) {
        
        ArrayList<ObjetosDetallePlanilla> listado = new ArrayList<>();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT * FROM d_planilla WHERE id_planilla = " + pId + " ORDER BY nombre_d_planilla";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Planilla", "Listar Planilla", pUsuario, pTerminal);
            while (tabla.next()) {
                ObjetosDetallePlanilla obj = new ObjetosDetallePlanilla();
                obj.setId_d_planilla(tabla.getInt("id_d_planilla"));
                obj.setId_planilla(tabla.getInt("id_planilla"));
                obj.setId_empleado(tabla.getInt("id_empleado"));
                obj.setNombre_d_planilla(tabla.getString("nombre_d_planilla"));
                obj.setPuesto_d_planilla(tabla.getString("puesto_d_planilla"));
                obj.setSalario_d_planilla(tabla.getDouble("salario_d_planilla"));
                obj.setBonificacion_d_planilla(tabla.getDouble("bonificacion_d_planillla"));
                obj.setIgsss_d_planilla(tabla.getDouble("igss_d_planilla"));
                obj.setComision_d_planilla(tabla.getDouble("comision_d_planilla"));
                obj.setHoras_extras_d_planilla(tabla.getDouble("horas_extras_d_planilla"));
                obj.setDescuentos_d_planilla(tabla.getDouble("descuentos_d_planilla"));
                obj.setCreditos_d_planilla(tabla.getDouble("creditos_d_planilla"));
                obj.setVales_d_planilla(tabla.getDouble("vales_d_planilla"));
                obj.setComentarios_d_planilla(tabla.getString("comentarios_d_planilla"));
                listado.add(obj);
            }
        } catch (Exception Error) {
            System.out.println(Error.getMessage());            
        } finally {
            Acceso.desconectar();
        }
        
        return listado;
        
    }
    
    public void eliminarDetalle(int pId, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "DELETE FROM d_planilla WHERE id_d_planilla = " + pId;
        try {
            Acceso.ejecutarConsulta(sql, "Planilla", "Eliminar Detalle", pUsuario, pTerminal);
        } catch (Error error) {
            error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public float quitarDecimalesExtra(double total, String pUsuario, String pTerminal) {
        
        float valor = 0;
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT ROUND(" + total + ", 2) AS total";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Planilla", "Listar Planilla", pUsuario, pTerminal);
            while (tabla.next()) {
                valor = tabla.getFloat("total");
            }
        } catch (SQLException Error) {
            System.out.println(Error.getMessage());
            valor = 0;
        } finally {
            Acceso.desconectar();
        }

        return valor;
        
    }
    
}
