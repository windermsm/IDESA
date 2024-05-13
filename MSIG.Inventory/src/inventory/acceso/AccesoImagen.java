package inventory.acceso;

import java.awt.Image;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class AccesoImagen {

    private Image data;

    public String guardar(String producto, String url) {
        String resultado = "";
        FileInputStream fis = null;
        AccesoInventario conexion = new AccesoInventario();
        try {
            File file = new File(url);
            fis = new FileInputStream(file);
            PreparedStatement pstm = conexion.conectar().prepareStatement("insert into m_imagen (id_producto, url_imagen, file_imagen) values(?,?,?)");
            pstm.setString(1, producto);
            pstm.setString(2, url);
            pstm.setBinaryStream(3, fis, (int) file.length());
            pstm.execute();
            pstm.close();
            resultado = "Imagen cargada con exito";
        } catch (FileNotFoundException ex) {
            resultado = "Ocurrio un error al leer el archivo";
            System.out.println(ex.toString());
        } catch (SQLException e) {
            resultado = "Ocurrio un error al insertar el archivo";
            System.out.println(e.toString());
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                resultado = "Ocurrio un error al cerrar la conexion";
                System.out.println(ex.toString());
            }
        }
        return resultado;
    }

    //metodo  que dado un parametro "id" realiza una consulta y 
    //devuelve como resultado una imagen
    public Image buscar(int id, String producto) {
        AccesoInventario conexion = new AccesoInventario();
        try {
            PreparedStatement pstm = conexion.conectar().prepareStatement("select * from m_imagen where id_imagen = ? and id_producto = ?");
            pstm.setInt(1, id);
            pstm.setString(2, producto);
            ResultSet res = pstm.executeQuery();
            int i = 0;
            while (res.next()) {
                //se lee la cadena de bytes de la base de datos
                byte[] b = res.getBytes("file_imagen");
                // esta cadena de bytes sera convertida en una imagen
                data = ConvertirImagen(b);
                i++;
            }
            res.close();
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return data;
    }

    //metodo que dada una cadena de bytes la convierte en una imagen jpeg
    private Image ConvertirImagen(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
        ImageReader reader = (ImageReader) readers.next();
        Object source = bis; // File or InputStream
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        return reader.read(0, param);
    }

    public ArrayList<String> listarImagenes(String producto, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_imagen where id_producto = '" + producto + "'";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Imagen", "Listar Imagenes", pUsuario, pTerminal);
            while (tabla.next()) {
                lista.add(String.valueOf(tabla.getInt("id_imagen")));
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public int listarPrimeraImagen(String producto, String pUsuario, String pTerminal) {
        int valor = 0;
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select min(id_imagen) as id_imagen from m_imagen where id_producto = '" + producto + "'";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Imagen", "Listar Primera Imagen", pUsuario, pTerminal);
            while (tabla.next()) {
                valor = tabla.getInt("id_imagen");
            }
        } catch (Exception error) {
            return 0;
        } finally {
            Acceso.desconectar();
        }
        return valor;
    }
}
