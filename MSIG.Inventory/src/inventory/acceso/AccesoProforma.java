package inventory.acceso;

import inventory.objetos.ObjetosProforma;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoProforma {

    public String eliminarProforma(ObjetosProforma pProforma, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete from m_proforma where id_proforma = " + pProforma.getId_proforma();
        try {
            return Acceso.ejecutarConsulta(sql, "Crear Proforma", "Eliminar Proforma", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarProforma(ObjetosProforma pProforma, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_proforma values (0, "
                + pProforma.getId_cliente() + ", "
                + pProforma.getId_empleado() + ", '"
                + pProforma.getFecha_proforma() + "', '"
                + pProforma.getDir_envio_proforma() + "', "
                + pProforma.getTotal_proforma() + ", now(), '" + pProforma.getNombre_proforma() + "')";
        try {
            return Acceso.ejecutarConsulta(sql, "Crear Proforma", "Insertar Proforma", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarProforma(ObjetosProforma pProforma, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_proforma set id_cliente = " + pProforma.getId_cliente()
                + ", fecha_proforma = '" + pProforma.getFecha_emision_proforma()
                + "', dir_envio_proforma = '" + pProforma.getDir_envio_proforma()
                + "', id_empleado = " + pProforma.getId_empleado() +  ", nombre_proforma = '" + pProforma.getNombre_proforma() + "'"
                + " where id_proforma = " + pProforma.getId_proforma();
        try {
            return Acceso.ejecutarConsulta(sql, "Crear Proforma", "Actualizar Proforma", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosProforma> seleccionarProforma(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_proforma";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Proforma", "Seleccionar Proforma", pUsuario, pTerminal);
            ObjetosProforma Registros;
            while (tabla.next()) {
                Registros = new ObjetosProforma();
                Registros.setId_proforma(tabla.getInt("id_proforma"));
                Registros.setId_cliente(tabla.getInt("id_cliente"));
                Registros.setId_empleado(tabla.getInt("id_empleado"));
                Registros.setFecha_proforma(tabla.getString("fecha_proforma"));
                Registros.setDir_envio_proforma(tabla.getString("dir_envio_proforma"));
                Registros.setTotal_proforma(tabla.getFloat("total_proforma"));
                Registros.setFecha_emision_proforma(tabla.getString("fecha_emision_proforma"));
                Registros.setNombre_proforma(tabla.getString("nombre_proforma"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosProforma> seleccionarIdProforma(ObjetosProforma pProforma, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_proforma where id_proforma = ( select max(id_proforma) from m_proforma where id_cliente = " + pProforma.getId_cliente()
                + " and id_empleado = " + pProforma.getId_empleado()
                + " and dir_envio_proforma = '" + pProforma.getDir_envio_proforma()
                + "' and total_proforma = " + pProforma.getTotal_proforma() + " AND nombre_proforma = '" + pProforma.getNombre_proforma() + "')";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Proforma", "Seleccionar ID Proforma", pUsuario, pTerminal);
            ObjetosProforma Registros;
            while (tabla.next()) {
                Registros = new ObjetosProforma();
                Registros.setId_proforma(tabla.getInt("id_proforma"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosProforma> seleccionarProformaFecha(String pFecha_inicial, String pFecha_final, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT  a.id_proforma, "
                + "a.id_cliente, "
                + "a.id_empleado, "
                + "date_format(a.fecha_proforma, '%Y-%m-%d') AS fecha_proforma, "
                + "a.dir_envio_proforma, "
                + "a.total_proforma, "
                + "a.fecha_emision_proforma, "
                + "c.nom_persona, a.nombre_proforma "
                + "FROM    m_proforma AS a, "
                + "m_cliente AS b, "
                + "m_persona AS c "
                + "WHERE   a.id_cliente = b.id_cliente "
                + "AND b.id_cliente = c.id_persona and date_format(fecha_emision_proforma, '%Y-%m-%d') BETWEEN date_format(str_to_date('" + pFecha_inicial + "', '%Y-%m-%d'), '%Y-%m-%d') and date_format(str_to_date('" + pFecha_final + "', '%Y-%m-%d'), '%Y-%m-%d') "
                + "ORDER BY fecha_emision_proforma";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Proforma", "Seleccionar Proforma por Fecha", pUsuario, pTerminal);
            ObjetosProforma Registros;
            while (tabla.next()) {
                Registros = new ObjetosProforma();
                Registros.setId_proforma(tabla.getInt("id_proforma"));
                Registros.setId_cliente(tabla.getInt("id_cliente"));
                Registros.setId_empleado(tabla.getInt("id_empleado"));
                Registros.setFecha_proforma(tabla.getString("fecha_proforma"));
                Registros.setDir_envio_proforma(tabla.getString("dir_envio_proforma"));
                Registros.setTotal_proforma(tabla.getFloat("total_proforma"));
                Registros.setFecha_emision_proforma(tabla.getString("fecha_emision_proforma"));
                Registros.setNombre_persona(tabla.getString("nom_persona"));
                Registros.setNombre_proforma(tabla.getString("nombre_proforma"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public int retornaIDEmpleado(String pNombre, String pUsuario, String pTerminal) {
        int registro = 0;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select id_empleado "
                + "from m_empleado "
                + "where nombre_empleado = '" + pNombre + "'";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Proforma", "Retorna ID Empleado", pUsuario, pTerminal);
            while (tabla.next()) {
                registro = tabla.getInt("id_empleado");
            }
        } catch (Exception error) {
            return 0;
        } finally {
            acceso.desconectar();
        }
        return registro;
    }

    public ArrayList<ObjetosProforma> retornaNombreEmpleados(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_empleado where estado_empleado = 'A' order by nombre_empleado";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Proforma", "Retorna Nombre Empleados", pUsuario, pTerminal);
            ObjetosProforma registros;
            while (tabla.next()) {
                registros = new ObjetosProforma();
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setNombre_empleado(tabla.getString("nombre_empleado"));
                lista.add(registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }
}