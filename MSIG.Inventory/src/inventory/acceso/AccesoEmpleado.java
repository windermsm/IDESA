package inventory.acceso;

import inventory.objetos.ObjetosEmpleado;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoEmpleado {

    public String insertarEmpleado(ObjetosEmpleado pEmpleado, String pUsuario, String pTerminal) {

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "";
        String fecha_salida = ( pEmpleado.getFecha_salida() == null || pEmpleado.getFecha_salida().equals("") ) ? "null" : "'" + pEmpleado.getFecha_salida() + "'";
        sql = "insert into m_empleado values (null, " + pEmpleado.getId_sucursal() + ", '" + pEmpleado.getTipo_empleado() + "', '" + pEmpleado.getNombre_empleado() + "', '" + pEmpleado.getEstado_empleado() + "', " + pEmpleado.getSalario_empleado() + ", " + pEmpleado.getBono_empleado() + ", '" + pEmpleado.getFecha_ingreso() + "'," + fecha_salida + ", " + pEmpleado.getPorcentaje_comision() + ")";

        try {
            return Acceso.ejecutarConsulta(sql, "Empleados", "Insertar Empleado", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarEmpleado(ObjetosEmpleado pEmpleado, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        
        if (!pEmpleado.getFecha_ingreso().equals("NULL")) {
            pEmpleado.setFecha_ingreso("'" + pEmpleado.getFecha_ingreso() + "'");
        }
        
        if (!pEmpleado.getFecha_salida().equals("NULL")) {
            pEmpleado.setFecha_salida("'" + pEmpleado.getFecha_salida() + "'");
        }
        
        String sql = "update m_empleado"
                + " set tipo_empleado = '" + pEmpleado.getTipo_empleado() 
                + "', nombre_empleado = '" + pEmpleado.getNombre_empleado() 
                + "', estado_empleado = '" + pEmpleado.getEstado_empleado() 
                + "', bono_empleado = " + pEmpleado.getBono_empleado() 
                + ", fecha_ingreso_empleado = " + pEmpleado.getFecha_ingreso() 
                + ", fecha_salida_empleado = " + pEmpleado.getFecha_salida() 
                + ", id_sucursal = " + pEmpleado.getId_sucursal() 
                + ", salario_empleado = " + pEmpleado.getSalario_empleado() 
                + ", porcentaje_comision = " + pEmpleado.getPorcentaje_comision() 
                + " where id_empleado = " + pEmpleado.getId_empleado();
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Empleados", "Actuaizar Empleado", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosEmpleado> retornaNombreSucursal(String pUsuario, String pTerminal) {

        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_sucursal";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Empleado", "Retorna Nombre Sucursal", pUsuario, pTerminal);
            ObjetosEmpleado registros;

            while (tabla.next()) {
                registros = new ObjetosEmpleado();
                registros.setId_sucursal(tabla.getInt("id_sucursal"));
                registros.setNombre_sucursal(tabla.getString("nombre_sucursal"));
                lista.add(registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }

    public int retornaIdSucursal(String pNombre, String pUsuario, String pTerminal) {
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_sucursal where nombre_sucursal = '" + pNombre + "'";
        int registro = 0;

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Empleado", "Retorna ID Sucursal", pUsuario, pTerminal);
            while (tabla.next()) {
                registro = tabla.getInt("id_sucursal");
            }
        } catch (Exception error) {
            return 0;
        } finally {
            acceso.desconectar();
        }
        return registro;
    }

    public ArrayList<ObjetosEmpleado> retornaEmpleados(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select a.id_empleado, a.id_sucursal, a.tipo_empleado, a.nombre_empleado, a.estado_empleado, "
                + "a.bono_empleado, a.fecha_ingreso_empleado, ifnull(a.fecha_salida_empleado, '') as fecha_salida_empleado, "
                + "b.nombre_sucursal, a.salario_empleado, a.porcentaje_comision from m_empleado a, m_sucursal b where a.id_sucursal = b.id_sucursal";

        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Empleado", "Retorna Empleados", pUsuario, pTerminal);
            ObjetosEmpleado registros;

            while (tabla.next()) {
                registros = new ObjetosEmpleado();
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setTipo_empleado(tabla.getString("tipo_empleado"));
                registros.setNombre_empleado(tabla.getString("nombre_empleado"));
                registros.setEstado_empleado(tabla.getString("estado_empleado"));
                registros.setBono_empleado(tabla.getDouble("bono_empleado"));
                registros.setFecha_ingreso(tabla.getString("fecha_ingreso_empleado"));
                registros.setFecha_salida(tabla.getString("fecha_salida_empleado"));
                registros.setNombre_sucursal(tabla.getString("nombre_sucursal"));
                registros.setSalario_empleado(tabla.getDouble("salario_empleado"));
                registros.setPorcentaje_comision(tabla.getDouble("porcentaje_comision"));
                lista.add(registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosEmpleado> retornaIdEmpleado(String pNombre, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select a.id_empleado, a.id_sucursal, a.tipo_empleado, a.nombre_empleado, a.estado_empleado, a.bono_empleado, a.fecha_ingreso_empleado, ifnull(a.fecha_salida_empleado, '') as fecha_salida_empleado, b.nombre_sucursal, a.salario_empleado, a.porcentaje_comision from m_empleado a, m_sucursal b where a.id_sucursal = b.id_sucursal and a.nombre_empleado = '" + pNombre + "'";

        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Empleado", "Retorna ID Empleado", pUsuario, pTerminal);
            ObjetosEmpleado registros;

            while (tabla.next()) {
                registros = new ObjetosEmpleado();
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setTipo_empleado(tabla.getString("tipo_empleado"));
                registros.setNombre_empleado(tabla.getString("nombre_empleado"));
                registros.setEstado_empleado(tabla.getString("estado_empleado"));
                registros.setBono_empleado(tabla.getDouble("bono_empleado"));
                registros.setFecha_ingreso(tabla.getString("fecha_ingreso_empleado"));
                registros.setNombre_sucursal(tabla.getString("nombre_sucursal"));
                registros.setFecha_salida(tabla.getString("fecha_salida_empleado"));
                registros.setSalario_empleado(tabla.getDouble("salario_empleado"));
                registros.setPorcentaje_comision(tabla.getDouble("porcentaje_comision"));
                lista.add(registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosEmpleado> retornaEmpleado(String pNombre, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select a.id_empleado, a.id_sucursal, a.tipo_empleado, a.nombre_empleado, "
                + "a.estado_empleado, a.bono_empleado, a.fecha_ingreso_empleado, ifnull(a.fecha_salida_empleado, '') as fecha_salida_empleado, "
                + "b.nombre_sucursal, a.salario_empleado, a.porcentaje_comision from m_empleado a, m_sucursal b "
                + "where a.id_sucursal = b.id_sucursal and a.nombre_empleado like '%" + pNombre + "%'";

        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Empleado", "Retorna Empleado", pUsuario, pTerminal);
            ObjetosEmpleado registros;

            while (tabla.next()) {
                registros = new ObjetosEmpleado();
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setTipo_empleado(tabla.getString("tipo_empleado"));
                registros.setNombre_empleado(tabla.getString("nombre_empleado"));
                registros.setEstado_empleado(tabla.getString("estado_empleado"));
                registros.setBono_empleado(tabla.getDouble("bono_empleado"));
                registros.setFecha_ingreso(tabla.getString("fecha_ingreso_empleado"));
                registros.setNombre_sucursal(tabla.getString("nombre_sucursal"));
                registros.setFecha_salida(tabla.getString("fecha_salida_empleado"));
                registros.setSalario_empleado(tabla.getDouble("salario_empleado"));
                registros.setPorcentaje_comision(tabla.getDouble("porcentaje_comision"));
                lista.add(registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }

    public String buscarNombreEmpleado(int id, String pUsuario, String pTerminal) {
        String valor = null;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select nombre_empleado from m_empleado where id_empleado = " + id;
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Empleado", "Buscar Nombre Empleado", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getString("nombre_empleado");
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return valor;
    }
    
    public ArrayList<ObjetosEmpleado> listarEmpleadosActivos(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select a.id_empleado, a.id_sucursal, a.tipo_empleado, a.nombre_empleado, a.estado_empleado, "
                + "a.bono_empleado, a.fecha_ingreso_empleado, ifnull(a.fecha_salida_empleado, '') as fecha_salida_empleado, "
                + "b.nombre_sucursal, a.salario_empleado, a.porcentaje_comision from m_empleado a, m_sucursal b "
                + "where a.id_sucursal = b.id_sucursal and estado_empleado = 'A'";
        
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Empleado", "Retorna Empleados", pUsuario, pTerminal);
            ObjetosEmpleado registros;

            while (tabla.next()) {
                registros = new ObjetosEmpleado();
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setTipo_empleado(tabla.getString("tipo_empleado"));
                registros.setNombre_empleado(tabla.getString("nombre_empleado"));
                registros.setEstado_empleado(tabla.getString("estado_empleado"));
                registros.setBono_empleado(tabla.getDouble("bono_empleado"));
                registros.setFecha_ingreso(tabla.getString("fecha_ingreso_empleado"));
                registros.setFecha_salida(tabla.getString("fecha_salida_empleado"));
                registros.setNombre_sucursal(tabla.getString("nombre_sucursal"));
                registros.setSalario_empleado(tabla.getDouble("salario_empleado"));
                registros.setPorcentaje_comision(tabla.getDouble("porcentaje_comision"));
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