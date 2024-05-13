package inventory.acceso;

import java.io.IOException;
import java.sql.*;

public class AccesoInventario {

    AccesoArchivo archivo = new AccesoArchivo();
    Connection conexion = null;
    AccesoExcepciones mensaje = new AccesoExcepciones();

    public Connection conectar() {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://" + archivo.leer("[IP]") + ":" + archivo.leer("[Port]") + "/" + archivo.leer("[DataBase]");
            String usuario = archivo.leer("[User]");
            String contrasenia = archivo.leer("[Password]");
            Class.forName(driver).newInstance();
            @SuppressWarnings("LocalVariableHidesMemberVariable")
            Connection conexion = DriverManager.getConnection(url, usuario, contrasenia);
            return conexion;
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException error) {
            System.out.println(error.getMessage());
            return null;
        }
    }

    public ResultSet listarRegistros(String pConsulta, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        try {
            conexion = conectar();
            PreparedStatement acceso_datos = conexion.prepareStatement(pConsulta);
            ResultSet registros = acceso_datos.executeQuery();
            return registros;
        } catch (Exception error) {
            auditoria(pConsulta, pPantalla, pOpcion, pUsuario, pTerminal, error.getMessage());
            mensaje.mostrarError(pPantalla, pOpcion, pConsulta, error.toString(), pUsuario, pTerminal);
            return null;
        }
    }

    public String ejecutarConsulta(String pConsulta, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        String valor = null;
        try {
            conexion = conectar();
            PreparedStatement acceso_datos = conexion.prepareStatement(pConsulta);
            int registros = acceso_datos.executeUpdate();
            if (registros > 0) {
                valor = "Operacion realizada con exito.";
            } else {
                valor = "No se ha podido almacenar el registro.";
            }
        } catch (Exception error) {
            valor = error.getMessage();
            auditoria(pConsulta, pPantalla, pOpcion, pUsuario, pTerminal, error.getMessage());
            mensaje.mostrarError(pPantalla, pOpcion, pConsulta, error.toString(), pUsuario, pTerminal);
        }
        return valor;
    }
    
    public PreparedStatement prepararConsulta(String pConsulta, String pPantalla, String pOpcion, String pUsuario, String pTerminal) {
        PreparedStatement preparador = null;
        try {
            conexion = conectar();
            preparador = conexion.prepareStatement(pConsulta);
        } catch (Exception error) {
           auditoria(pConsulta, pPantalla, pOpcion, pUsuario, pTerminal, error.getMessage());
            mensaje.mostrarError(pPantalla, pOpcion, pConsulta, error.toString(), pUsuario, pTerminal); 
        }
        return preparador;
    }

    public void desconectar() {
        try {
            conexion.close();
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
    }

    public void auditoria(String pConsulta, String pPantalla, String pOpcion, String pUsuario, String pTerminal, String pError) {
        String consulta = "INSERT INTO d_auditoria VALUES (0, '"
                + pOpcion + "', '" + pPantalla + "', '" + pUsuario
                + "', '" + pTerminal + "', '" + pConsulta.replaceAll("'", "") + "', '" + pError.replace("'", "") + "', NOW())";
        try {
            conexion = conectar();
            PreparedStatement acceso_datos = conexion.prepareStatement(consulta);
            int registros = acceso_datos.executeUpdate();
        } catch (Exception error) {
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Insertar Auditoria");
        } finally {
            desconectar();
        }
    }
    
    public int ejecutarConsultaKey(String pConsulta, String pPantalla, String pOpcion, String pUsuario, String pTerminal){
        int llave = 0;
        try {            
            conexion = conectar();
            PreparedStatement ps = conexion.prepareStatement(pConsulta,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ResultSet result = ps.getGeneratedKeys();
            if (result != null && result.next()){
                llave = result.getInt(1);
            }
            return llave;
        } catch (SQLException error) {            
            llave = 0;
            auditoria(pConsulta, pPantalla, pOpcion, pUsuario, pTerminal, error.getMessage());
            mensaje.mostrarError(pPantalla, pOpcion, pConsulta, error.toString(), pUsuario, pTerminal);
        }
        return llave;
    }
}