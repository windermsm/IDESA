package inventory.acceso;

import inventory.objetos.ObjetosSucursal;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoSucursal {

    AccesoExcepciones mensaje = new AccesoExcepciones();

    public String insertarSucursal(ObjetosSucursal pSucursal, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_sucursal values (NULL, '" + pSucursal.getNombre_sucursal() + "', '" + pSucursal.getDescripcion_sucursal() + "', '" + pSucursal.getDireccion_sucursal() + "', '" + pSucursal.getNit_sucursal() + "', '" + pSucursal.getTelefonos_sucursal() + "');";
        return Acceso.ejecutarConsulta(sql, "Sucursal", "Insertar Sucursal", pUsuario, pTerminal);
    }

    public String actualizarSucursal(ObjetosSucursal pSucursal, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_sucursal set descripcion_sucursal = '" + pSucursal.getDescripcion_sucursal() + "', direccion_sucursal= '" + pSucursal.getDireccion_sucursal() + "' "
                + ", nombre_sucursal = '" + pSucursal.getNombre_sucursal() + "', nit_sucursal = '" + pSucursal.getNit_sucursal() + "', telefonos_sucursal = '" + pSucursal.getTelefonos_sucursal() + "' where id_sucursal = " + pSucursal.getId_sucursal();
        try {
            return Acceso.ejecutarConsulta(sql, "Sucursal", "Actualizar Sucursal", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosSucursal> seleccionarSucursal(String pNombreSucursal, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        System.out.println("Entro a hacer la consulta");
        String sql = "";
        if (pNombreSucursal.equals("") || pNombreSucursal == null || pNombreSucursal.isEmpty()) {
            sql = "select * from m_sucursal";
        } else {
            sql = "select * from m_sucursal where nombre_sucursal like '%" + pNombreSucursal + "%'";
        }
        try {
            System.out.println("Ejecutar consulta");
            ResultSet tabla = acceso.listarRegistros(sql, "Sucursales", "Listar Registros", pUsuario, pTerminal);
            ObjetosSucursal registros;
            System.out.println("Verificar si hay datos encontrados");
            while (tabla.next()) {
                registros = new ObjetosSucursal();
                registros.setId_sucursal(tabla.getInt("id_sucursal"));
                registros.setNombre_sucursal(tabla.getString("nombre_sucursal"));
                registros.setDescripcion_sucursal(tabla.getString("descripcion_sucursal"));
                registros.setDireccion_sucursal(tabla.getString("direccion_sucursal"));
                registros.setNit_sucursal(tabla.getString("nit_sucursal"));
                registros.setTelefonos_sucursal(tabla.getString("telefonos_sucursal"));
                lista.add(registros);
            }
        } catch (Exception error) {
            System.out.println("Error al Seleccionar Sucursal: " + error.toString());
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosSucursal> retornaSucursales(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_sucursal";
        System.out.println(sql);
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Sucursales", "Retorna Sucursales", pUsuario, pTerminal);
            ObjetosSucursal registros;
            while (tabla.next()) {
                registros = new ObjetosSucursal();
                registros.setId_sucursal(tabla.getInt("id_sucursal"));
                registros.setNombre_sucursal(tabla.getString("nombre_sucursal"));
                registros.setDescripcion_sucursal(tabla.getString("descripcion_sucursal"));
                registros.setDireccion_sucursal(tabla.getString("direccion_sucursal"));
                registros.setNit_sucursal(tabla.getString("nit_sucursal"));
                registros.setTelefonos_sucursal(tabla.getString("telefonos_sucursal"));
                lista.add(registros);
            }
        } catch (Exception error) {
            System.out.println("Error en Retorna Sucursales" + error.toString());
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosSucursal> obtenerIdSuc(String pNombre, String pDescripcion, String pDireccion, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_sucursal where nombre_sucursal = '" + pNombre + "' and descripcion_sucursal = '" + pDescripcion + "' and direccion_sucursal = '" + pDireccion + "';";
        try {
            System.out.println("Ejecutando en ACCESO SUCURSAL SQL " + sql);
            ResultSet tabla = acceso.listarRegistros(sql, "Sucursales", "Obtener ID Sucursal", pUsuario, pTerminal);
            ObjetosSucursal registros;
            while (tabla.next()) {
                registros = new ObjetosSucursal();
                registros.setId_sucursal(tabla.getInt("id_sucursal"));
                lista.add(registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }

    public ObjetosSucursal buscarSucursales(int pIdSucursal, String pUsuario, String pTerminal) {
        AccesoInventario acceso = new AccesoInventario();
        ObjetosSucursal registro = new ObjetosSucursal();
        String sql = "select * from m_sucursal where id_sucursal = " + pIdSucursal;
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Reportes", "Busca Sucursal", pUsuario, pTerminal);
            while (tabla.next()) {
                registro.setId_sucursal(tabla.getInt("id_sucursal"));
                registro.setNombre_sucursal(tabla.getString("nombre_sucursal"));
                registro.setDescripcion_sucursal(tabla.getString("descripcion_sucursal"));
                registro.setDireccion_sucursal(tabla.getString("direccion_sucursal"));
                registro.setNit_sucursal(tabla.getString("nit_sucursal"));
                registro.setTelefonos_sucursal(tabla.getString("telefonos_sucursal"));
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return registro;
    }
    
     public ObjetosSucursal buscarSucursalPorNombre(String pNombre, String pUsuario, String pTerminal) {
        AccesoInventario acceso = new AccesoInventario();
        ObjetosSucursal registro = new ObjetosSucursal();
        String sql = "select * from m_sucursal where nombre_sucursal = '" + pNombre + "'";
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Reportes", "Busca Sucursal", pUsuario, pTerminal);
            while (tabla.next()) {
                registro.setId_sucursal(tabla.getInt("id_sucursal"));
                registro.setNombre_sucursal(tabla.getString("nombre_sucursal"));
                registro.setDescripcion_sucursal(tabla.getString("descripcion_sucursal"));
                registro.setDireccion_sucursal(tabla.getString("direccion_sucursal"));
                registro.setNit_sucursal(tabla.getString("nit_sucursal"));
                registro.setTelefonos_sucursal(tabla.getString("telefonos_sucursal"));
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return registro;
    }
}