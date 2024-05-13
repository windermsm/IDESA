/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.servicios;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import inventory.acceso.AccesoArchivo;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Megabytes Soluciones Integrales Guatemala
 */
public class Impresion {

    private Font fuenteTitulo = new Font(Font.FontFamily.COURIER, 10f, Font.NORMAL);
    private Font fuenteTextos = new Font(Font.FontFamily.COURIER, 10f, Font.NORMAL);
    private Font fuenteEtiquetas = new Font(Font.FontFamily.COURIER, 10f, Font.NORMAL);
    private Matematicas matematicas = new Matematicas();
    private Factura factura;
    
    //Obtener los parametros del archivo de configuracion
    AccesoArchivo Archivo = new AccesoArchivo();

    public Impresion(Factura factura) {
        this.factura = factura;
    }

    private void agregarTextoNormal(Document document, String string,
            Font font, int align) {
        try {
            Chunk espacio = new Chunk(string, font);
            Paragraph p = new Paragraph(espacio);
            p.setAlignment(align);
            document.add(p);
        } catch (DocumentException ex) {
        }
    }

    private void agregarSeparacion(Document document) {
        try {
            Font fuente = new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.NORMAL);
            Chunk espacio = new Chunk("\n", fuente);
            document.add(new Paragraph(espacio));
        } catch (DocumentException ex) {
        }
    }

    private void agregarEtiqueta(PdfPTable table, String string, int align, boolean isBorder, int colspan) {
        Chunk c = new Chunk(string, fuenteEtiquetas);
        PdfPCell cell = new PdfPCell(new Phrase(c));
        if (!isBorder) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        cell.setColspan(colspan);
        cell.setVerticalAlignment(align);
        cell.setHorizontalAlignment(align);
        table.addCell(cell);
    }

    private void agregarTexto(PdfPTable table, String string, int align, boolean isBorder, int colspan) {
        
        Chunk c = new Chunk(string, fuenteTextos);
        PdfPCell cell = new PdfPCell(new Phrase(c));
        
        if (!isBorder) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(align);
        table.addCell(cell);
    
    }

    private PdfPTable crearMaestroFactura() throws DocumentException {
        
        PdfPTable table = new PdfPTable(16); //numero de columnas
        table.setWidthPercentage(100f);
        
        //datos de la fila 1, validar los datos que se veran y la ubicacion
        agregarTexto(table, "", Element.ALIGN_LEFT, false, 2);
        agregarTexto(table, factura.getMaestro().getFecha(), Element.ALIGN_LEFT, false, 4);
        agregarTexto(table, factura.getMaestro().getHora(), Element.ALIGN_LEFT, false, 4);
        try {
            if(Archivo.leer("[ImprimeNoPedido]").toString().equals("SI")){
                agregarTexto(table, factura.getMaestro().getNo_pedido(), Element.ALIGN_LEFT, false, 3);
                agregarTexto(table, factura.getMaestro().getNo_pedido(), Element.ALIGN_CENTER, false, 3);
            } else {
                agregarTexto(table, "", Element.ALIGN_LEFT, false, 3);
                agregarTexto(table, "", Element.ALIGN_CENTER, false, 3);
            }
        } catch (IOException error) {
            System.out.println("Error al mostrar el numero de pedido:");
            System.out.println(error.toString());
        }
        
        //espacio entre las lineas de la tabla
        agregarTexto(table, "", Element.ALIGN_LEFT, false, 16);
        
        //datos de la fila 2
        agregarTexto(table, "", Element.ALIGN_LEFT, false, 1);
        agregarTexto(table, factura.getMaestro().getNombre_cliente(), Element.ALIGN_LEFT, false, 9);
        agregarTexto(table, factura.getMaestro().getNit(), Element.ALIGN_LEFT, false, 3);
        
         //validar si se imprime el codigo del cliente o no
        try {
            if(Archivo.leer("[ImprimeCodigoCliente]").toString().equals("SI")){
                agregarTexto(table, factura.getMaestro().getCodigo(), Element.ALIGN_CENTER, false, 3);
            } else {
                agregarTexto(table, "", Element.ALIGN_CENTER, false, 3);
            }
        } catch (IOException error) {
            System.out.println("Error al mostrar el codigo del empleado:");
            System.out.println(error.toString());
        }
        
         //espacio
        agregarTexto(table, "", Element.ALIGN_LEFT, false, 16);
        
        agregarTexto(table, "", Element.ALIGN_LEFT, false, 1);
        agregarTexto(table, factura.getMaestro().getDireccion(), Element.ALIGN_LEFT, false, 9);
        
        //validar si se imprime el codigo del empleado en la factura
        try {
            if(Archivo.leer("[ImprimeCodigoCliente]").toString().equals("SI")){
                agregarTexto(table, factura.getMaestro().getVendedor(), Element.ALIGN_LEFT, false, 3);
            } else {
                agregarTexto(table, "", Element.ALIGN_LEFT, false, 3);
            }
        } catch (IOException ex) {
            Logger.getLogger(Impresion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        agregarTexto(table, "", Element.ALIGN_LEFT, false, 3);
        
        return table;
    }

    private PdfPTable crearDetalleFactura() throws DocumentException {
        
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100f);
        List<Producto> productos = factura.getProductos();

        double total = 0d;
        int linea = 0;
        
        for (Producto producto : productos) {
            agregarTexto(table, "" + producto.getCantidad(), Element.ALIGN_LEFT, false, 1);
            if(producto.getDescripcion().length() >= 40){
                agregarTexto(table, producto.getDescripcion().substring(0, 40) + "...", Element.ALIGN_LEFT, false, 6);
            } else {
                agregarTexto(table, producto.getDescripcion(), Element.ALIGN_LEFT, false, 6);
            }
            agregarTexto(table, "" + matematicas.redondear(producto.getPrecioUnitario()), Element.ALIGN_RIGHT, false, 1);
            agregarTexto(table, "" , Element.ALIGN_RIGHT, false, 1);
            agregarTexto(table, "" + matematicas.redondear(producto.getPrecioParcial()), Element.ALIGN_RIGHT, false, 1);
            total += producto.getPrecioParcial();
            linea++;
        }
        
        //agregar lineas en blanco hasta llegar al final
        //se cambio para hacerla hoja carta anteriormente tenia 18
        for(int x=linea; linea<38; linea++){
            agregarTexto(table, "-", Element.ALIGN_LEFT, false,  1);
            agregarTexto(table, "-", Element.ALIGN_LEFT, false,  9);
        }
        
        ConvertirNumeroLetras describir = new ConvertirNumeroLetras();
        
        //agregar el total al final de la factura
        total = matematicas.redondear(total);
        agregarTexto(table, describir.Convertir(String.valueOf(total), true), Element.ALIGN_LEFT, false,  8);
        agregarTexto(table, "", Element.ALIGN_RIGHT, false,  1);
        agregarTexto(table, "" + total, Element.ALIGN_RIGHT, false,  1);
        
        return table;
    }

    public void generarArchivoPDF() {
        try {
            //parametros para la imprsion de la factura directos del archivo de configuracion
            int PageSizeX = Integer.parseInt(Archivo.leer("[PageSizeX]").toString());
            int PageSizeY = Integer.parseInt(Archivo.leer("[PageSizeY]").toString());
            int DocumentMarginLeft = Integer.parseInt(Archivo.leer("[DocumentMarginLeft]").toString());
            int DocumentMarginRight = Integer.parseInt(Archivo.leer("[DocumentMarginRight]").toString());
            int DocumentMarginTop = Integer.parseInt(Archivo.leer("[DocumentMarginTop]").toString());
            int DocumentMarginButtom = Integer.parseInt(Archivo.leer("[DocumentMarginButtom]").toString());
            System.out.println("Parametros optenidos " + PageSizeX + " " +  DocumentMarginLeft);
            
            //se coloco en lugar de un largo variable (10f por cada linea del detalle) un largo fijo
            //Rectangle pagesize = new Rectangle(300f, 150f + (10f * factura.getProductos().size()));
            
            //prueba en impresora lacer
            Rectangle pagesize = new Rectangle(PageSizeX, PageSizeY);
            System.out.println("Se creo el rectagunlo para el tamaño de la pagina");
            Document document = new  Document(pagesize, DocumentMarginLeft, DocumentMarginRight, DocumentMarginTop, DocumentMarginButtom);
            System.out.println("Se creo el margen del documento a imprimir en PDF");
            
            PdfWriter.getInstance(document, new FileOutputStream("factura_orden_template.pdf"));
            document.open();
            
            //parametros para el encabezado de la factura, titulo y subtitulo
            String Title = Archivo.leer("[Title]");
            String SubTitle = Archivo.leer("[SubTitle]");
            
            //aqui se agrega el encabezado de la factura, titulo y subtitulo
            agregarTextoNormal(document, Title, fuenteTitulo, Element.ALIGN_CENTER);
            agregarTextoNormal(document, SubTitle, fuenteTitulo, Element.ALIGN_CENTER);
            
            int TitleSpace = Integer.parseInt(Archivo.leer("[TitleSpace]").toString());
            
            for (int x = 0; x < TitleSpace; x++) {
                agregarSeparacion(document); //dejar espacios
            }
            
            document.add(crearMaestroFactura());
            
            int MasterSpace = Integer.parseInt(Archivo.leer("[MasterSpace]").toString());
            
            for (int y = 0; y < MasterSpace; y++) {
                agregarSeparacion(document); //dejar espacios
            }
            
            document.add(crearDetalleFactura());
            document.close();
            
            //abri archivo generado en pdf de la factura
            Runtime.getRuntime().exec("cmd.exe /c start factura_orden_template.pdf");
            
        } catch (IOException | NumberFormatException | DocumentException e) {
            
        }
    }
}

