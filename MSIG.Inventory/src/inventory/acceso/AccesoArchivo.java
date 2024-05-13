package inventory.acceso;

import java.io.*;

public class AccesoArchivo {

    public String leer(String Identificador) throws IOException {
        String valor = null;
        FileReader LectorArchivo = null;
        try {
            //obtener el string de conexcion del archivo confi.ini
            File Archivo = new File("C://MSIG Inventory/config.ini");
            LectorArchivo = new FileReader(Archivo);
            BufferedReader PilaLectora = new BufferedReader(LectorArchivo);
            //lectura del fichero
            String linea = null;
            while ((linea = PilaLectora.readLine()) != null) {
                if (linea.contains(Identificador)) {
                    valor = linea.substring(Identificador.length() + 1, linea.length());
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error leer archivo: " + ex);
        } finally {
            try {
                LectorArchivo.close();
            } catch (IOException ex) {
                System.out.println(Identificador + ":" + valor);
                System.out.println("Error al buscar en archivo: " + ex);
            }
        }
        return valor;
    }
}
