package inventory.servicios;

import inventory.acceso.AccesoExcepciones;
import inventory.librerias.CsvReader;
import inventory.objetos.ObjetosProducto;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class ServicioLeerArchivoExcel {

    public ArrayList<ObjetosProducto> leer_archivo_excel(String archivo){
        
        AccesoExcepciones Mensaje = new AccesoExcepciones();
        ArrayList lista_productos = new ArrayList();
        CsvReader LectorDeArchivo = null;

        try {
            LectorDeArchivo = new CsvReader(archivo,'\t');
        } catch (FileNotFoundException error) {
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Leer direccion archivo de Excel");
        }

        try {
            LectorDeArchivo.readHeaders();
        } catch (IOException error) {
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Leer encabezados archivo de Excel");
        }

        try { 
            
            ObjetosProducto Producto;
            
            while (LectorDeArchivo.readRecord()){ 
                Producto = new ObjetosProducto();
                Producto.setId_producto(String.valueOf(LectorDeArchivo.get("ID Producto"))); 
                Producto.setMarca_producto(String.valueOf(LectorDeArchivo.get("Marca")));
                Producto.setDesc_producto(String.valueOf(LectorDeArchivo.get("Descripcion")));
                Producto.setLinea_producto(String.valueOf(LectorDeArchivo.get("Linea")));
                Producto.setPrecio_compra_producto(Float.parseFloat(LectorDeArchivo.get("Precio Compra")));
                Producto.setPrecio_est_producto(Float.parseFloat(LectorDeArchivo.get("Precio Estandar")));
                Producto.setId_proveedor(Integer.parseInt(LectorDeArchivo.get("ID Proveedor")));
                Producto.setExi_producto(Float.parseFloat(LectorDeArchivo.get("Existencia")));
                Producto.setUnidad_medida_producto(String.valueOf(LectorDeArchivo.get("Unidad Medida")));
                Producto.setMaximo_producto(Float.parseFloat(LectorDeArchivo.get("Maximo")));
                Producto.setMinimo_producto(Float.parseFloat(LectorDeArchivo.get("Minimo")));
                Producto.setPrj_est_producto(Float.parseFloat(LectorDeArchivo.get("% Estandar")));
                Producto.setPrj_min_producto(Float.parseFloat(LectorDeArchivo.get("% Minimo")));
                Producto.setPrecio_min_producto(Float.parseFloat(LectorDeArchivo.get("Precio Minimo")));
                Producto.setCodigo_fabricante(String.valueOf(LectorDeArchivo.get("Codigo Fabricante")));
                Producto.setUbicacion_producto(String.valueOf(LectorDeArchivo.get("Ubicacion")));
                Producto.setFactura_producto(String.valueOf(LectorDeArchivo.get("Factura")));
                Producto.setDescuento_producto(String.valueOf(LectorDeArchivo.get("Descuento")));
                Producto.setVisible_producto(String.valueOf(LectorDeArchivo.get("Visible")));
                Producto.setSerie_producto(String.valueOf(LectorDeArchivo.get("Serie")));
                Producto.setComponente_producto(String.valueOf(LectorDeArchivo.get("Componente")));
                Producto.setGarantia_producto(String.valueOf(LectorDeArchivo.get("Garantia")));
                lista_productos.add(Producto);
            }
            
        } catch (IOException error) {
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Leer Archivo Excel");
            System.out.println(error.toString());
        }

        LectorDeArchivo.close();

        return lista_productos;
    }
    
    public void escribir_archivo_excel(JTable pTabla, File pArchivo) throws IOException{
        
        TableModel modelo = pTabla.getModel();
        FileWriter archivo = new FileWriter(pArchivo);
        BufferedWriter contenido = new BufferedWriter(archivo);
        
        for(int cFila = 0; cFila<modelo.getColumnCount(); cFila++){
            contenido.write(modelo.getColumnName(cFila).toString()+"\t");
        }
        
        contenido.write("\n");
        
        for(int cFila = 0; cFila<modelo.getRowCount(); cFila++){
            for(int cColumna = 0; cColumna<modelo.getColumnCount(); cColumna++){
                contenido.write(modelo.getValueAt(cFila, cColumna).toString()+"\t");
            }
            contenido.write("\n");
        }
        
        contenido.close();
    }
}