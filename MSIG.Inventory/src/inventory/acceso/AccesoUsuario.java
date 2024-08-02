package inventory.acceso;

import inventory.objetos.ObjetosUsuario;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoUsuario {

    AccesoExcepciones mensaje = new AccesoExcepciones();

    public String insertarUsuario(ObjetosUsuario pUsuario, String xUsuario, String xTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_usuario(id_usuario, id_empleado, tipo_usuario, nombre_usuario, contrasenia_usuario) "
                + "values (NULL," + pUsuario.getId_empleado() + ", '" + pUsuario.getTipo_usuario() + "', '" + pUsuario.getNombre_usuario() + "', '" + pUsuario.getContrasenia_usuario() + "')";

        try {
            return Acceso.ejecutarConsulta(sql, "Usuarios", "Insertar Usuario", xUsuario, xTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
     public String insertarFondoUsuario(ObjetosUsuario pUsuario, String xUsuario, String xTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "INSERT INTO m_fondo VALUES ('" + pUsuario.getNombre_usuario() + "', 'imgFondoPantalla.png')";

        try {
            return Acceso.ejecutarConsulta(sql, "Usuarios", "Insertar Fondo Usuario", xUsuario, xTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarUsuario(ObjetosUsuario pUsuario, String xUsuario, String xTerminal) {
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "update m_usuario set nombre_usuario = '" + pUsuario.getNombre_usuario() + "', contrasenia_usuario= '" + pUsuario.getContrasenia_usuario() + "', tipo_usuario = '" + pUsuario.getTipo_usuario() + "' "
                + "where id_usuario = " + pUsuario.getId_usuario();

        try {
            return Acceso.ejecutarConsulta(sql, "Usuarios", "Actualizar Usuario", xUsuario, xTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarClaveUsuario(String pClave, int pId_Usuario, String xUsuario, String xTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_usuario SET contrasenia_usuario = '" + pClave + "' WHERE id_usuario = " + pId_Usuario;
        
        try {
            return Acceso.ejecutarConsulta(sql, "Usuarios", "Actualizar Usuario", xUsuario, xTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
    }

    public ArrayList<ObjetosUsuario> seleccionarUsuario(String pNombre_usuario, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();

        String sql = "select a.id_usuario, a.id_empleado, a.nombre_usuario, a.contrasenia_usuario, a.tipo_usuario, b.nombre_empleado from m_usuario a, m_empleado b where a.id_empleado = b.id_empleado and a.nombre_usuario like '%" + pNombre_usuario + "%'";

        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Usuario", "Seleccionar Usuario", pUsuario, pTerminal);
            ObjetosUsuario registros;

            while (tabla.next()) {
                registros = new ObjetosUsuario();
                registros.setId_usuario(tabla.getInt("id_usuario"));
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setNombre_usuario(tabla.getString("nombre_usuario"));
                registros.setContrasenia_usuario(tabla.getString("contrasenia_usuario"));
                registros.setTipo_usuario(tabla.getString("tipo_usuario"));
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

    public ArrayList<ObjetosUsuario> seleccionarCajeros(String tipos, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();

        String sql = "select * from m_usuario a, "
                + "m_empleado b where a.id_empleado = b.id_empleado "
                + "and a.tipo_usuario in (" + tipos + ") and b.estado_empleado = 'A' "
                + "order by nombre_usuario";

        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Usuario", "Seleccionar Cajeros", pUsuario, pTerminal);
            ObjetosUsuario registros;

            while (tabla.next()) {
                registros = new ObjetosUsuario();
                registros.setId_usuario(tabla.getInt("id_usuario"));
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setNombre_usuario(tabla.getString("nombre_usuario"));
                registros.setContrasenia_usuario(tabla.getString("contrasenia_usuario"));
                registros.setTipo_usuario(tabla.getString("tipo_usuario"));
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

    public int retornaIdUsuario(String pNombre_usuario, String pUsuario, String pTerminal) {
        int registro = 0;
        AccesoInventario acceso = new AccesoInventario();

        String sql = "select a.id_usuario, a.id_empleado, a.nombre_usuario, a.contrasenia_usuario, a.tipo_usuario, b.nombre_empleado from m_usuario a, m_empleado b where a.id_empleado = b.id_empleado and a.nombre_usuario like '%" + pNombre_usuario + "%'";

        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Usuario", "Retorna ID Usuario", pUsuario, pTerminal);

            while (tabla.next()) {
                registro = tabla.getInt("id_usuario");

            }
        } catch (Exception error) {
            return 0;
        } finally {
            acceso.desconectar();
        }
        return registro;
    }
    
    public String retornaTipoUsuario(String pNombre, String pClave, String pUsuario, String pTerminal) {
        String registro = "";
        AccesoInventario acceso = new AccesoInventario();

        String sql = "select * from m_usuario where nombre_usuario = '" + pNombre + "' and contrasenia_usuario = '" + pClave + "'";

        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Usuario", "Retorna Tipo Usuario", pUsuario, pTerminal);

            while (tabla.next()) {
                registro = tabla.getString("tipo_usuario");

            }
        } catch (Exception error) {
            return "";
        } finally {
            acceso.desconectar();
        }
        return registro;
    }

    public ArrayList<ObjetosUsuario> retornaUsuario(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select a.id_usuario, a.id_empleado, a.nombre_usuario, a.contrasenia_usuario, a.tipo_usuario, b.nombre_empleado from m_usuario a, m_empleado b where a.id_empleado = b.id_empleado";
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Usuario", "Retorna Usuario", pUsuario, pTerminal);
            ObjetosUsuario registros;
            while (tabla.next()) {
                registros = new ObjetosUsuario();
                registros.setId_usuario(tabla.getInt("id_usuario"));
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setTipo_usuario(tabla.getString("tipo_usuario"));
                registros.setNombre_usuario(tabla.getString("nombre_usuario"));
                registros.setContrasenia_usuario(tabla.getString("contrasenia_usuario"));
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

    public ArrayList<ObjetosUsuario> retornaNombreEmpleados(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_empleado order by nombre_empleado asc";

        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Usuario", "Retorna Nombre Empleado", pUsuario, pTerminal);
            ObjetosUsuario registros;

            while (tabla.next()) {
                registros = new ObjetosUsuario();
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

    public int retornaIDEmpleado(String pNombre, String pUsuario, String pTerminal) {
        int registro = 0;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select id_empleado from m_empleado where nombre_empleado = '" + pNombre + "'";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Usuario", "Retorna ID Empleado", pUsuario, pTerminal);
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

    public ArrayList<ObjetosUsuario> validaCredenciales(String usuario, String clave, String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        
        String sql = "select  a.*,\n"
                + "        b.nombre_empleado as nombre_empleado,\n"
                + "        c.nombre_sucursal as nombre_sucursal\n"
                + "from    m_usuario as a,\n"
                + "        m_empleado as b,\n"
                + "        m_sucursal as c\n"
                + "where   a.id_empleado = b.id_empleado\n"
                + "    and b.id_sucursal = c.id_sucursal\n"
                + "    and a.nombre_usuario =  '" + usuario + "'\n"
                + "    and a.contrasenia_usuario = '" + clave + "'";
        
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Usuario", "Valida Credenciales", pUsuario, pTerminal);
            ObjetosUsuario registros;
            while (tabla.next()) {
                registros = new ObjetosUsuario();
                registros.setId_usuario(tabla.getInt("id_usuario"));
                registros.setId_empleado(tabla.getInt("id_empleado"));
                registros.setTipo_usuario(tabla.getString("tipo_usuario"));
                registros.setNombre_usuario(tabla.getString("nombre_usuario"));
                registros.setContrasenia_usuario(tabla.getString("contrasenia_usuario"));
                registros.setNombre_empleado(tabla.getString("nombre_empleado"));
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
    
    public String retornaFondoUsuario(String pUsuario, String pTerminal) {
        String archivo = "/inventory/imagenes/imgFondoPantalla.png";
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_fondo where usuario_fondo = '" + pUsuario + "'";
        
        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Usuario", "Retorna Fondo Usuario", pUsuario, pTerminal);
            while (tabla.next()) {
                archivo = "/inventory/fondo/" + tabla.getString("archivo_fondo");
            }
        } catch (Exception error) {
            return "/inventory/imagenes/imgFondoPantalla.png";
        } finally {
            acceso.desconectar();
        }
        
        return archivo;
    }
    
    public String actualizarFondoUsuario(String pFondo, String pUsuario, String xTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_fondo set archivo_fondo = '" + pFondo + "' where usuario_fondo = '" + pUsuario + "' ";
        
        try {
            return Acceso.ejecutarConsulta(sql, "Usuarios", "Actualizar Fondo", pUsuario, xTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
}