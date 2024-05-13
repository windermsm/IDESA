/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.vistas;

import inventory.acceso.*;
import inventory.objetos.*;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;
import objetos.ObjetosCaja;

/**
 *
 * @author Megabyte Soluciones Integrales Guatemala
 */
public class wdwCatalogoSerie extends javax.swing.JInternalFrame {

    
    private String compra;
    private String venta;
    private String pantalla;
    private String estado;
    private String titulo_pantalla;
    
    private String Log;
    private String fecha_limite_garantia_proveedor = "";
    private String tiempo_limite_garantia_cliente = "";
    
    AccesoSerie acceso_serie = new AccesoSerie();
    AccesoDetalleSerie acceso_componente = new AccesoDetalleSerie();
    AccesoExcepciones mensaje = new AccesoExcepciones();
    
    ArrayList<ObjetosProducto> listado_productos = new ArrayList<>();
    ArrayList<ObjetosDetalleSerie> listado_componentes = new ArrayList<>();
    DefaultTableModel modelo_componente = new DefaultTableModel();
    DefaultTableModel modelo_equipos = new DefaultTableModel();

    /**
     * Creates new form wdwMovimientoVentaDeProductos
     *
     * @param articulos ArrayList<ObjetosListaProducto> listado de productos
     * @param p_Id_Compra recibe vacio "" si no está asociado a una compra
     * @param p_Id_Servicio recibe vacio "" si no está asociado a un servicio
     * @param p_Id_Venta recibe vacio "" si no está asociaco a una venta
     */
    public wdwCatalogoSerie(ArrayList<ObjetosListaProducto> articulos, String p_Id_Compra, String p_Id_Servicio, String p_Id_Venta, String p_pantalla) {
        
        initComponents();
        
        this.compra = p_Id_Compra;
        this.venta = p_Id_Venta;
        this.pantalla = p_pantalla;
        
        if("Compra".equals(pantalla)) {
            titulo_pantalla = "Ingreso de series para el ID Compra " + compra;
            btnEntregarEquipo.setEnabled(false);
            btnGuardarSerie.setEnabled(true);
            txtFechaLimiteGarantiaProveedor.setEnabled(true);
            txtCantidadTiempoLimiteCliente.setEnabled(true);
            cbxTipoLimiteCliente.setEnabled(true);
        }
        
        if("Venta".equals(pantalla)) {
            titulo_pantalla = "Venta de productos con serie para la venta " + venta;
            btnEntregarEquipo.setEnabled(true);
            btnGuardarSerie.setEnabled(false);
            txtFechaLimiteGarantiaProveedor.setEnabled(false);
            txtCantidadTiempoLimiteCliente.setEnabled(false);
            cbxTipoLimiteCliente.setEnabled(false);
        }
        
        lblTItuloPantalla.setText(titulo_pantalla);
        
        //Llenar la pantlla con los datos de los productos
        Iterator<ObjetosListaProducto> iterador = articulos.iterator();
        int contador = 0;
        
        try {
            while (iterador.hasNext()) {
                ObjetosListaProducto producto = iterador.next();
                tblListadoProductos.setValueAt(String.valueOf(contador), contador, 0);
                tblListadoProductos.setValueAt(producto.getIdproducto(), contador, 1);
                tblListadoProductos.setValueAt(producto.getDescripcionproducto(), contador, 2);
                tblListadoProductos.setValueAt(producto.getSerie(), contador, 3);
                tblListadoProductos.setValueAt(producto.getComponentes(), contador, 4);
                tblListadoProductos.setValueAt(producto.getGarantia(), contador, 5);
                tblListadoProductos.setValueAt(producto.getCantidad(), contador, 6);
                contador++;
            }
        } catch (Exception Error) {
            System.out.println("Ocurrio un error al listar productos:\n" + Error.toString());
        }
        
        //Deshabilitar la tabla de componentes
        tblComponentes.setEnabled(false);
        
        //Creo la columnas que tendra el modelo del detalle de las serie
        //en este caso sera para los componentes de cada producto seleccionado
        modelo_componente.addColumn("ID Componente");
        modelo_componente.addColumn("ID Serie");
        modelo_componente.addColumn("ID Producto");
        modelo_componente.addColumn("Serie");
        modelo_componente.addColumn("Descripcion");
        modelo_componente.addColumn("Estado");
        
        tblListadoProductos.getColumnModel().getColumn(2).setPreferredWidth(400);
        
        if(Inventory.lblRol.getText().equals("Administrador") || Inventory.lblRol.getText().equals("Jefe Sucursal")) {
            txtCorregirSerie.setEnabled(true);
            btnEditarSerie.setEnabled(true);
            btnEditarGarantiaCliente.setEnabled(true);
        } else {
            txtCorregirSerie.setEnabled(false);
            btnEditarSerie.setEnabled(false);
            btnEditarGarantiaCliente.setEnabled(false);
        }
        
        
    }
    
    private void agregarLog(String texto) {
        this.Log = this.Log + "SERIE" + ": " + texto + " \n";
        Inventory.txtLog.setText(Log);
    }
    
    private void ObtenerFechaLimiteGarantia() {
        
        //Ingresar la fecha limite con el proveedor y tiempo limite de cliente
        agregarLog("Validar que el usuario ingrese la información solicitada.");
        if (txtFechaLimiteGarantiaProveedor.getDate().toString().isEmpty()) {
            mensaje.manipulacionExcepciones("critico", "Por favor ingrese la fecha límite de garantia con el Proveedor.", "Validar Serie");
        } else { 
            this.fecha_limite_garantia_proveedor = (txtFechaLimiteGarantiaProveedor.getDate().getYear() + 1900) + "-" + (txtFechaLimiteGarantiaProveedor.getDate().getMonth() + 1) + "-" + txtFechaLimiteGarantiaProveedor.getDate().getDate();
            agregarLog("Fecha Limite Proveedor: " + this.fecha_limite_garantia_proveedor);
        }

        //Concatenar la cantidad de tiempo y el tipo de medida de tiempo
        agregarLog("Concantenar la unidad de tiempo y la cantidad.");
        if (tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 5).toString().equals("SI")) {
            if (txtCantidadTiempoLimiteCliente.getText().isEmpty()) {
                mensaje.manipulacionExcepciones("critico", "Por favor ingresar el tiempo de Garantia para el Cliente.", "Obtener Fecha Limite Garantia");
            } else {
                this.tiempo_limite_garantia_cliente = txtCantidadTiempoLimiteCliente.getText() + " " + cbxTipoLimiteCliente.getSelectedItem().toString();
                agregarLog("Fecha Limite Cliente: " + this.tiempo_limite_garantia_cliente);
            }
        } else {
            this.tiempo_limite_garantia_cliente = "S/G";
            agregarLog("Se le colocón S/G porque dice NO en el campo garantía del Producto.");
        }
        
    }
    
    private void cargarSeries(){
        
        limpiarTablaDeListadoEquipos();
        
        ArrayList<ObjetosSerie> listado_series = new ArrayList<>();

        String id_producto = tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 1).toString(); 
        String descripcion = tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 2).toString(); 
        String componente = tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 4).toString(); 
        String garantia = tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 5).toString();
        int cantidad = Integer.parseInt(tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 6).toString());
        
        //Invalidar el ingreso de la fecha para usuario si el producto no tiene garantia
        if(tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 5).toString().equals("SI") && Integer.parseInt(venta) > 0) {
            txtFechaLimiteGarantiaProveedor.setEnabled(false);
            txtCantidadTiempoLimiteCliente.setEnabled(false);
            cbxTipoLimiteCliente.setEnabled(false);
            txtCantidadTiempoLimiteCliente.setText("");
        } else {
            txtFechaLimiteGarantiaProveedor.setEnabled(true);
            //Si el eproducto no tiene garantia se dehabilita el campo de dias de garantia para el cliente
            if (tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 5).toString().equals("SI")) {
                txtCantidadTiempoLimiteCliente.setEnabled(true);
            } else {
                txtCantidadTiempoLimiteCliente.setEnabled(false);
            }
            cbxTipoLimiteCliente.setEnabled(true);
        }
        
        //Definir la cantidad de lineas que tendra la tabla de equipos
        DefaultTableModel modelo_equipo_listado = new DefaultTableModel();
        modelo_equipo_listado.addColumn("ID Serie");
        modelo_equipo_listado.addColumn("ID Compra");
        modelo_equipo_listado.addColumn("ID Producto");
        modelo_equipo_listado.addColumn("ID Factura");
        modelo_equipo_listado.addColumn("ID O. Servicio");
        modelo_equipo_listado.addColumn("Descripcion");
        modelo_equipo_listado.addColumn("Serie");
        modelo_equipo_listado.addColumn("Componente");
        modelo_equipo_listado.addColumn("Estado");
        modelo_equipo_listado.addColumn("Garantia Proveedor");
        modelo_equipo_listado.addColumn("Garantia Cliente");
        modelo_equipo_listado.setRowCount(cantidad);

        //Llenar la tabla con los datos que se encontraron y luego validar
        //si no hay datos que falten de ingresar para crear lineas extras
        int cantidad_db = 0;
        if (Integer.parseInt(this.compra) > 0) {
            //mensaje.manipulacionExcepciones("informacion", "Ingresar series de la Compra", "Catalogo Series");
            listado_series = acceso_serie.listarSeriePorIdCompra(this.compra, id_producto, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            cantidad_db = acceso_serie.contarSeries(this.compra, id_producto, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        } else {
            if (Integer.parseInt(this.venta) > 0) {
                //mensaje.manipulacionExcepciones("informacion", "Ingresar series de la Venta", "Catalogo Series");
                listado_series = acceso_serie.listarSeriePorIdFactura(this.venta, id_producto, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            }
        }
        
        if(cantidad < cantidad_db && Integer.parseInt(this.compra) > 0 ) {
            mensaje.manipulacionExcepciones("critico", "La cantidad de la Factura no coincide con lo que Escanearon. Por favor verifique.", "Contar Series");
            modelo_equipo_listado.setRowCount(cantidad_db);
        }
        
        Iterator<ObjetosSerie> iterador = listado_series.iterator();
        int contador = 0;
        
        while(iterador.hasNext()) {
            ObjetosSerie serie = iterador.next();
            modelo_equipo_listado.setValueAt(serie.getId_serie(), contador, 0); 
            modelo_equipo_listado.setValueAt(serie.getId_compra(), contador, 1);
            modelo_equipo_listado.setValueAt(serie.getId_producto(), contador, 2);
            modelo_equipo_listado.setValueAt(serie.getId_factura(), contador, 3);
            modelo_equipo_listado.setValueAt(serie.getId_orden_servicio(), contador, 4);
            modelo_equipo_listado.setValueAt(serie.getDescripcion(), contador, 5); 
            modelo_equipo_listado.setValueAt(serie.getNumero_serie(), contador, 6);
            modelo_equipo_listado.setValueAt(componente, contador, 7);
            modelo_equipo_listado.setValueAt(serie.getEstado(), contador, 8);
            modelo_equipo_listado.setValueAt(serie.getFecha_limite_garantia_proveedor(), contador, 9);
            modelo_equipo_listado.setValueAt(serie.getFecha_limite_garantia_cliente(), contador, 10);
            contador++;
        } 

        //Rellenar las que hagan falta segun lo consultado
        if (contador == cantidad) {
            mensaje.manipulacionExcepciones("critica", "Todas series estan ingresadas para este Producto", "Validacion de Series");
        } else {
            try {
                
                for (int i = contador; i < cantidad; i++) {
                    modelo_equipo_listado.setValueAt("0", i, 0);
                    modelo_equipo_listado.setValueAt(compra, i, 1);
                    modelo_equipo_listado.setValueAt(id_producto, i, 2);
                    modelo_equipo_listado.setValueAt("0", i, 3);
                    modelo_equipo_listado.setValueAt("0", i, 4);
                    modelo_equipo_listado.setValueAt(descripcion, i, 5);
                    modelo_equipo_listado.setValueAt("0", i, 6);
                    modelo_equipo_listado.setValueAt(componente, i, 7);
                    modelo_equipo_listado.setValueAt("Pendiente", i, 8);
                }
                
            } catch (Exception Error) {
                
                mensaje.manipulacionExcepciones("critico", Error.getMessage(), "Limpiar Tabla Equipos");
                
            }
        }
        
        tblListadoEquipos.setModel(modelo_equipo_listado);
        
        tblListadoEquipos.getColumnModel().getColumn(5).setPreferredWidth(400);
        tblComponentes.getColumnModel().getColumn(4).setPreferredWidth(400);
        
    }
    
    private void validarComponente() {

        boolean repetido = false;

        //Validar si en toda la lista no esta este numero de serie para que no se repita
        try {
            for (int i = 0; i < tblComponentes.getModel().getRowCount(); i++) {
                if (tblComponentes.getValueAt(i, 3).toString().equals(txtSerieEquipo.getText().toUpperCase().replaceAll("'", "´"))) {
                    repetido = true;
                    i = tblComponentes.getModel().getRowCount() + 1; //esto para que se salga del ciclo
                } else {
                    repetido = false;
                }
            }
        } catch (Exception Error) {
            System.out.println("Error al recorrer listado de Equipos: \n" + Error.toString());
        }


        try {

            ObjetosDetalleSerie componente = acceso_componente.buscarDetalleSerie(txtEscaneoComponentes.getText().toUpperCase().replaceAll("'", "´"), txtIdProducto.getText(), tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            if (componente.getId_serie() == 0) {

                if (txtIdProducto.getText().isEmpty()) {
                    mensaje.manipulacionExcepciones("critico", "Por favor ingrese que tipo de Producto es este Componente.", "Agregar Componente");
                } else {

                    //validar si este numero esta en la lista porque no se pueden ingresar repetidor
                    if (repetido) {
                        mensaje.manipulacionExcepciones("critico", "Este numero de serie ya esta asignado. Revise el listado.", "Agregar Componente");
                    } else {
                        modelo_componente.addRow(new Object[]{"ID Componente", "ID Serie", "ID Producto", "Serie", "Descripcion", "Comentario", "Estado"});
                        modelo_componente.setValueAt("0", tblComponentes.getRowCount() - 1, 0);
                        modelo_componente.setValueAt(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0), tblComponentes.getRowCount() - 1, 1);
                        modelo_componente.setValueAt(txtIdProducto.getText(), tblComponentes.getRowCount() - 1, 2);
                        modelo_componente.setValueAt(txtEscaneoComponentes.getText().toUpperCase().replaceAll("'", "´´"), tblComponentes.getRowCount() - 1, 3);
                        modelo_componente.setValueAt(txtDescripcionProducto.getText(), tblComponentes.getRowCount() - 1, 4);
                        modelo_componente.setValueAt("Pendiente", tblComponentes.getRowCount() - 1, 5);
                    }

                    txtSerieEquipo.setText("");

                }

            } else {
                mensaje.manipulacionExcepciones("critico", "Este numero de serie ya fue registrado.", "Registro de Componentes");
            }

        } catch (Exception Error) {
            mensaje.manipulacionExcepciones("critico", "Seleccione uno de los Equipos", "Seleccion de Equipos");
        }
        
        tblListadoEquipos.getColumnModel().getColumn(5).setPreferredWidth(400);
        tblComponentes.getColumnModel().getColumn(4).setPreferredWidth(400);
        
    }

    private void calcularFechaGarantiaCliente() {
        
        //Consultar la fecha de garantia limite para el cliente
        if ("SI".equals(tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 5).toString())) {
            agregarLog("Iniciando el cálculo de la fecha de garantía para el Cliente.");
            //Dividir cadena para encontrar la cantidad y el tipo a calcular
            String string = tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 10).toString();
            agregarLog("Tiempo de garantía: " + string);
            String cantidad = "";
            String tipo = "";
            
            if (string.length() > 0) {
                
                try {
                    agregarLog("Dibibidr la cadena de carácteres para el cálculo.");
                    String[] parts = string.split(" ");
                    cantidad = parts[0];
                    agregarLog("Cantidad: " + cantidad);
                    tipo = parts[1];
                    agregarLog("Tipo: " + tipo);
                } catch (Exception Error) {
                    mensaje.manipulacionExcepciones("critico", "No se puede definir la garantía.", "Verificar Garantia");
                    agregarLog("Error al dividir datos de Tiempo. \nError: " + Error.toString());
                }
                
                try {
                    agregarLog("Consultando fecha límite de garantía.");
                    tiempo_limite_garantia_cliente = acceso_serie.sumarTiempoAFechaActual(cantidad, tipo, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    agregarLog("Limite Garantía: " + tiempo_limite_garantia_cliente);
                } catch (Exception Error) {
                    mensaje.manipulacionExcepciones("critico", "No se pudo calcular la fecha de Garantia para el Cliente", "Calcular Fecha Garantia");
                }
                
            } else {
                agregarLog("Se le colocó S/G porque no le aparecia tiempo de garantía.");
                tiempo_limite_garantia_cliente = "S/G";
            }
            
            agregarLog("Agregar el dato a la tabla de Equipos con garantía.");
            tblListadoEquipos.setValueAt(tiempo_limite_garantia_cliente, tblListadoEquipos.getSelectedRow(), 10);
        
        } else {
            agregarLog("Se le colocó S/G porque el campo garantía en el Producto dice NO.");
            tblListadoEquipos.setValueAt("S/G", tblListadoEquipos.getSelectedRow(), 10);            
        }
        
    }
    
    private void limpiarTablaDeComponentes() {
        //limpiar la tabla por si tiene informacion de otros componente
        try {
            for (int x=0; x < modelo_componente.getRowCount(); x++) {
                modelo_componente.removeRow(x);
            }
        } catch (Exception Error) {
            System.out.println("Error al limpira tabla de Componentes. \n" + Error.toString());
        }

        tblComponentes.setModel(modelo_componente);
    }
    
    private void limpiarTablaDeListadoEquipos() {
        //limpiar la tabla por si tiene informacion de otros componente
        try {
            for (int x=0; x < modelo_componente.getRowCount(); x++) { 
                modelo_equipos.removeRow(x);
            }
        } catch (Exception Error) {
            System.out.println("Error al limpira tabla de Equipos. \n" + Error.toString());
        }

        tblListadoEquipos.setModel(modelo_componente);
    }

    private void cargarListadoComponentes() {
        
        limpiarTablaDeComponentes();
        
        //Buscar si tiene ya información ingresadas de componentes
        //para la serie seleccionada, siempre y cuando tenga componente si.
        String tiene_compoente = "NO";
        try {
            tiene_compoente = tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 7).toString();
        } catch(Exception Error) {
            tiene_compoente = "NO";
        }
        
        if("SI".equals(tiene_compoente)) {
            
            //buscar componentes en la base de datos
            String id_serie = tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString();
            listado_componentes = acceso_componente.listarDetalleSerie(id_serie, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            Iterator<ObjetosDetalleSerie> iterador = listado_componentes.iterator(); 
            modelo_componente.setRowCount(listado_componentes.size());
            
            int contador = 0;
            
            while(iterador.hasNext()){
                ObjetosDetalleSerie componente = iterador.next();
                modelo_componente.setValueAt(componente.getId_d_serie(), contador, 0);
                modelo_componente.setValueAt(componente.getId_serie(), contador, 1);
                modelo_componente.setValueAt(componente.getId_producto(), contador, 2);
                modelo_componente.setValueAt(componente.getNumero_serie(), contador, 3);
                modelo_componente.setValueAt(componente.getDescripcion(), contador, 4);
                modelo_componente.setValueAt(componente.getEstado(), contador, 5);
                contador++;
            }
            
            tblComponentes.setModel(modelo_componente);
            tblComponentes.setEnabled(true);
            
            //Habilitar los botones para ingresar informacion
            txtEscaneoComponentes.setEnabled(true);
            btnEscanearComponentes.setEnabled(true);
            txtIdProducto.setEnabled(true);
            btnBuscarProducto.setEnabled(true);
            
        } else {
            
            tblComponentes.setEnabled(false);
            txtEscaneoComponentes.setEnabled(false);
            btnEscanearComponentes.setEnabled(false);
            txtIdProducto.setEnabled(false);
            btnBuscarProducto.setEnabled(false);
        
        }
        
        tblListadoEquipos.getColumnModel().getColumn(5).setPreferredWidth(400);
        tblComponentes.getColumnModel().getColumn(4).setPreferredWidth(400);
        
    }
    
    private void validarSerieEquipo() {
        
        boolean repetido = false;
        
        agregarLog("Buscar en toda la lista si ya fue ingresado el número de Serie.");
        
        //Validar si en toda la lista no esta este numero de serie para que no se repite
        try {
            for(int i=0; i<tblListadoEquipos.getModel().getRowCount(); i++) {
                if(tblListadoEquipos.getValueAt(i, 6).toString().equals(txtSerieEquipo.getText().toUpperCase().replaceAll("'", "´"))) {
                    agregarLog("El número de Serie " + txtSerieEquipo.getText().toUpperCase().replaceAll("'", "´") + " esta reptedido.");
                    repetido = true;
                    i = tblListadoEquipos.getModel().getRowCount() + 1; //esto para que se salga del ciclo
                } else {
                    repetido = false;
                }
            }
        } catch(Exception Error) {
            agregarLog("Error al recorrer listado de Equipos. \nError: " + Error.toString());
        }
        
        agregarLog("Buscar la Serie en la base de datos.");
        ObjetosSerie serie = acceso_serie.buscarSerie(txtSerieEquipo.getText().toUpperCase().replaceAll("'", "´"), tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 1).toString(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

        //Si el resultado es cero el numero de serie no existe en la base de datos
        if (serie.getId_serie() == 0) {

            agregarLog("No se encontró el número de Serie en la base de datos");
            
            //Se valida si estamos en la pantalla de la ventas al tener cero como resultado de la busqueda
            //de la serie, esto significa que este numero de serie no puede ser vendido porque no existe.
            agregarLog("Se valida que pantalla se está utilizando.");
            if (Integer.parseInt(this.venta) > 0) {
                
                mensaje.manipulacionExcepciones("informacion", "Este Producto no se encuentra en Inventario", "Agregar Serie");
            
            } else {
                
                //Antes de asignar el numero de serie validar si ya tiene alguno asignado y verificar si esta este numero
                //en algun otro elemento de la lista de series para no repetirlo y que de error al guardar dos datos iguales.
                agregarLog("Antes de asignar la Serie se busca si ya tiene asigando un número.");
                if (repetido == false) {
                    
                    try {
                        
                        tblListadoEquipos.setValueAt(txtSerieEquipo.getText().toUpperCase().replaceAll("'", "´").trim(), tblListadoEquipos.getSelectedRow(), 6);
                        tblListadoEquipos.setValueAt(fecha_limite_garantia_proveedor, tblListadoEquipos.getSelectedRow(), 9);
                        
                        //Validar si el producto debe tener tiempo de garantia para el cliente
                        agregarLog("Validar si este producto debe de tener garantía para el Cliente.");
                        if ("SI".equals(tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 5).toString())) {
                            ObtenerFechaLimiteGarantia(); 
                            agregarLog("Asignando valores a la tabla de Equipos.");
                            tblListadoEquipos.setValueAt(fecha_limite_garantia_proveedor, tblListadoEquipos.getSelectedRow(), 9); 
                            tblListadoEquipos.setValueAt(tiempo_limite_garantia_cliente, tblListadoEquipos.getSelectedRow(), 10);
                        } else {
                            if (txtCantidadTiempoLimiteCliente.getText().isEmpty()) {
                                tblListadoEquipos.setValueAt("S/G", tblListadoEquipos.getSelectedRow(), 10); 
                            } else {
                                mensaje.manipulacionExcepciones("informacion", "Usted tiene configurado este producto para no tener Garantia. /n"
                                        + "Revise el catalogo de Productos por favor.", "Producto sin Garantía");
                                tblListadoEquipos.setValueAt("S/G", tblListadoEquipos.getSelectedRow(), 10); 
                            }
                        }
                        
                        agregarLog("Limpiar el campo de texto Serie.");
                        txtSerieEquipo.setText("");
                        cambiarLineEquipos();
                        
                    } catch (Exception Error) {
                        mensaje.manipulacionExcepciones("critico", "Ocurrio un error al asignar los valores para la Seria comprada. \n"  + Error.getMessage(), "Validar Serie a Comprar");
                        agregarLog("Eror al asignar serie. \nError: " + Error.toString());
                    }
                    

                } else {
                    agregarLog("Este equipo ya tiene una Serie asignada.");
                    mensaje.manipulacionExcepciones("informacion", "Este Equipo ya teiene una Serie asignada, verifique el listado", "Agregar Serie");
                    
                } //FIn del codigo cuando es una compra y la serie existe en la base de datos

            } //FIn de la validación para saber si es una Venta y el Producto no existe.

        } else {

            //Si el ID es mayor que cero entonces este producto si existe en el inventario.
            //Validamos si esto es una venta o una compra de productos para saber que hacer.
            agregarLog("Este producto existe en el inventario. Validar si esto es una Venta.");
            if (Integer.parseInt(this.venta) > 0) {
                agregarLog("Es una Venta.");
                //Esto quiere decir que es una venta por lo cual se actualizara el estado
                //como vendido y se agregara el id_factura que corresponde mas el usuario
                //siempre y cuando su estdo sea "En Inventario" y agregar fecha limite para cliente
                agregarLog("Verificar que el producto este En Inventario.");
                if (serie.getEstado().equals("En Inventario")) {
                    
                    //Se valida que no este repetido en el listado de Equipos.
                    try {
                        agregarLog("Verificar que la serie no esté repetida.");
                        if (repetido == false) {
                            agregarLog("Asignar valores a la Serie para actualizar sus datos.");
                            tblListadoEquipos.setValueAt(serie.getId_serie(), tblListadoEquipos.getSelectedRow(), 0);
                            tblListadoEquipos.setValueAt(serie.getId_compra(), tblListadoEquipos.getSelectedRow(), 1);
                            tblListadoEquipos.setValueAt(serie.getId_producto(), tblListadoEquipos.getSelectedRow(), 2);
                            tblListadoEquipos.setValueAt(this.venta, tblListadoEquipos.getSelectedRow(), 3);
                            tblListadoEquipos.setValueAt("0", tblListadoEquipos.getSelectedRow(), 4);
                            tblListadoEquipos.setValueAt(serie.getDescripcion(), tblListadoEquipos.getSelectedRow(), 5);
                            tblListadoEquipos.setValueAt(serie.getNumero_serie(), tblListadoEquipos.getSelectedRow(), 6);
                            tblListadoEquipos.setValueAt(tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 4).toString(), tblListadoEquipos.getSelectedRow(), 7);
                            tblListadoEquipos.setValueAt(serie.getEstado(), tblListadoEquipos.getSelectedRow(), 8);
                            //Como es una venta no se modifica la fecha de garintía con proveedor pero se calcula la fecha 
                            //de garantia para el Cliente. Según lo que indique dicho campo fecha_limite_garantia_cliente.
                            agregarLog("Calcular fecha de garantía para el Cliente.");
                            tblListadoEquipos.setValueAt(serie.getFecha_limite_garantia_proveedor(), tblListadoEquipos.getSelectedRow(), 9);
                            agregarLog("Fecha Limite Cliente: " + serie.getFecha_limite_garantia_cliente());
                            tblListadoEquipos.setValueAt(serie.getFecha_limite_garantia_cliente(), tblListadoEquipos.getSelectedRow(), 10);
                            
                        } else {
                            agregarLog("Este equipo ya teiene una Serie asignada en la base de datos.");
                            mensaje.manipulacionExcepciones("informacion", "Este Equipo ya teiene una Serie asignada, verifique el listado", "Agregar Serie");                            
                        }

                        agregarLog("Se limpia el campo de la Serie.");
                        txtSerieEquipo.setText("");
                        cambiarLineEquipos();

                    } catch (Exception Error) {
                        mensaje.manipulacionExcepciones("critico", "Ocurrio un error al asignar los valores para la serie a vender. \n"
                                + Error.getMessage(), "Validar Serie a Vender");
                        agregarLog("Error al asignar los datos a la Serie. \nError: " + Error.toString());
                    }
                
                
                } else {
                    
                    //Si esto es una venta y el producto ya existe validamos si ya fue vendido dicho producto.
                    agregarLog("Esto es una Venta. Validar que el producto no este Vendido.");
                    if (serie.getEstado().equals("Vendido")) {
                        mensaje.manipulacionExcepciones("informacion", "Este producto ya fue Vendido, verifique la serie.", "Validar Serie Compra");
                    } else {
                        agregarLog("Validar que el Producto no esté eliminado.");
                        if (serie.getEstado().equals("Eliminado")) {
                            mensaje.manipulacionExcepciones("informacion", "Este producto fue eliminado por el Administrador", "Validar Serie Compra");
                        } else {
                            mensaje.manipulacionExcepciones("critico", "Este numero de Serie ya existe en el Inventario.", "Validar Serie Compra");
                        }
                    }
                    
                }

            } else {

                //Si esto es una compra validamos el estado de este producto que no este vendido, eliminado o en inventario.
                agregarLog("Esto es una Compra. Validar que el producto no este Vendido.");
                if (serie.getEstado().equals("Vendido")) {
                    mensaje.manipulacionExcepciones("informacion", "Este producto ya fue Vendido, verifique la serie.", "Valida Compra Serie");
                } else {
                    agregarLog("Validar que el Producto no esté En Inventario.");
                    if (serie.getEstado().equals("En Inventario")) {
                        mensaje.manipulacionExcepciones("critico", "Este numero de serie ya fue registrado anteriormente", "Valida Compra Serie");
                    } else {
                        agregarLog("Validar que el Producto no esté eliminado.");
                        if (serie.getEstado().equals("Eliminado")) {
                            mensaje.manipulacionExcepciones("critico", "Este numero de serie fue Eliminado por el Administrador", "Valida Compra Serie");
                        }
                    }
                }

            }

        }
    
        agregarLog("Cambiar el tamaño de las columnas.");
        tblListadoEquipos.getColumnModel().getColumn(5).setPreferredWidth(400);
        tblComponentes.getColumnModel().getColumn(4).setPreferredWidth(400);
        
    }
    
    public void cambiarLineEquipos() {
        agregarLog("Tratando de cambiar de linea.");
        int numero_de_filas = Integer.parseInt( tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 6).toString() ) - 1;
        int fila_actual = tblListadoEquipos.getSelectedRow();
        agregarLog("Linea Actual: " + fila_actual);
        if (fila_actual == numero_de_filas) {
            agregarLog("Regresar a la primer fila.");
            tblListadoEquipos.changeSelection(0, 0, false, false);
        } else {
            agregarLog("Mover linea a la siguiente fila.");
            tblListadoEquipos.changeSelection(tblListadoEquipos.getSelectedRow() + 1, 0, false, false);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        pnlEncabezado = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblListadoEquipos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtSerieEquipo = new javax.swing.JTextField();
        btnEscanear = new javax.swing.JButton();
        btnGuardarSerie = new javax.swing.JButton();
        btnEliminaSerie = new javax.swing.JButton();
        btnEntregarEquipo = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtFechaLimiteGarantiaProveedor = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        txtCantidadTiempoLimiteCliente = new javax.swing.JTextField();
        cbxTipoLimiteCliente = new javax.swing.JComboBox();
        txtFechaGarantiaLimiteCliente = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        btnEditarGarantiaCliente = new javax.swing.JButton();
        pnlBotones = new javax.swing.JPanel();
        btnImprimir = new javax.swing.JButton();
        txtImpresionCertificado = new javax.swing.JButton();
        cbxSello = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        pnlDetalle = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblComponentes = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtEscaneoComponentes = new javax.swing.JTextField();
        btnEscanearComponentes = new javax.swing.JButton();
        btnGuardarComponentes = new javax.swing.JButton();
        btnEliminarComponente = new javax.swing.JButton();
        txtIdProducto = new javax.swing.JTextField();
        btnBuscarProducto = new javax.swing.JButton();
        txtDescripcionProducto = new javax.swing.JTextField();
        txtCorregirSerie = new javax.swing.JTextField();
        btnEditarSerie = new javax.swing.JButton();
        pnlListadoProductos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListadoProductos = new javax.swing.JTable();
        lblTItuloPantalla = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Series");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonCodigoDeBarraMini.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1200, 650));
        setMinimumSize(new java.awt.Dimension(1200, 650));
        setPreferredSize(new java.awt.Dimension(1200, 650));

        pnlEncabezado.setBackground(new java.awt.Color(255, 255, 204));
        pnlEncabezado.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblListadoEquipos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Serie", "ID Compra", "ID Producto", "ID Factura", "ID O. Servicio", "Descripcion", "Serie", "Componente", "Estado", "Garantia Proveedor", "Garantia Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblListadoEquipos.getTableHeader().setReorderingAllowed(false);
        tblListadoEquipos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListadoEquiposMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblListadoEquipos);
        if (tblListadoEquipos.getColumnModel().getColumnCount() > 0) {
            tblListadoEquipos.getColumnModel().getColumn(0).setResizable(false);
            tblListadoEquipos.getColumnModel().getColumn(1).setResizable(false);
            tblListadoEquipos.getColumnModel().getColumn(2).setResizable(false);
            tblListadoEquipos.getColumnModel().getColumn(3).setResizable(false);
            tblListadoEquipos.getColumnModel().getColumn(4).setResizable(false);
            tblListadoEquipos.getColumnModel().getColumn(5).setResizable(false);
            tblListadoEquipos.getColumnModel().getColumn(6).setResizable(false);
            tblListadoEquipos.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel1.setText("Listado de equipos");

        txtSerieEquipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSerieEquipoKeyPressed(evt);
            }
        });

        btnEscanear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonCodigoDeBarraMini.png"))); // NOI18N
        btnEscanear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscanearActionPerformed(evt);
            }
        });

        btnGuardarSerie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnGuardarSerie.setText("Guardar");
        btnGuardarSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarSerieActionPerformed(evt);
            }
        });

        btnEliminaSerie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEliminar.png"))); // NOI18N
        btnEliminaSerie.setText("Eliminar");
        btnEliminaSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminaSerieActionPerformed(evt);
            }
        });

        btnEntregarEquipo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEntregarEquipo.png"))); // NOI18N
        btnEntregarEquipo.setText("Registrar Serie Vendida");
        btnEntregarEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntregarEquipoActionPerformed(evt);
            }
        });

        jLabel3.setText("Garantia Proveedor");

        jLabel4.setText("Garantia Cliente");

        cbxTipoLimiteCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dia", "Mes", "Año" }));

        jLabel6.setText("Garantía Cliente");

        btnEditarGarantiaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEditar.png"))); // NOI18N
        btnEditarGarantiaCliente.setText("Editar");
        btnEditarGarantiaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarGarantiaClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1140, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncabezadoLayout.createSequentialGroup()
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                .addComponent(txtSerieEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEscanear))
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFechaLimiteGarantiaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                .addComponent(txtCantidadTiempoLimiteCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxTipoLimiteCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(76, 76, 76)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFechaGarantiaLimiteCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditarGarantiaCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                                .addComponent(btnGuardarSerie)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminaSerie))
                            .addComponent(btnEntregarEquipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEscanear)
                            .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtSerieEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnEliminaSerie)
                                .addComponent(btnGuardarSerie)
                                .addComponent(jLabel3)
                                .addComponent(btnEditarGarantiaCliente))
                            .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel6)
                                .addComponent(txtFechaLimiteGarantiaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEntregarEquipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel4)
                                .addComponent(txtCantidadTiempoLimiteCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbxTipoLimiteCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addComponent(txtFechaGarantiaLimiteCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBotones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgPantallaReporte.png"))); // NOI18N
        btnImprimir.setToolTipText("Imprimir Envio");
        btnImprimir.setActionCommand("imprimirEnvio");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        txtImpresionCertificado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonImpresoraLaser.png"))); // NOI18N
        txtImpresionCertificado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImpresionCertificadoActionPerformed(evt);
            }
        });

        cbxSello.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ninguno", "Importado", "Reacondicionado", "Nuevo" }));

        jLabel5.setText("Sello");

        javax.swing.GroupLayout pnlBotonesLayout = new javax.swing.GroupLayout(pnlBotones);
        pnlBotones.setLayout(pnlBotonesLayout);
        pnlBotonesLayout.setHorizontalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxSello, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtImpresionCertificado)
                .addContainerGap(864, Short.MAX_VALUE))
        );
        pnlBotonesLayout.setVerticalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxSello, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(btnImprimir)
                    .addComponent(txtImpresionCertificado))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDetalle.setBackground(new java.awt.Color(204, 255, 204));
        pnlDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblComponentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Componente", "ID Serie", "ID Producto", "Serie", "Descripcion", "Comentario", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblComponentes.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblComponentes.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblComponentes);
        if (tblComponentes.getColumnModel().getColumnCount() > 0) {
            tblComponentes.getColumnModel().getColumn(0).setResizable(false);
            tblComponentes.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblComponentes.getColumnModel().getColumn(1).setResizable(false);
            tblComponentes.getColumnModel().getColumn(1).setPreferredWidth(20);
            tblComponentes.getColumnModel().getColumn(2).setResizable(false);
            tblComponentes.getColumnModel().getColumn(3).setResizable(false);
            tblComponentes.getColumnModel().getColumn(3).setPreferredWidth(20);
            tblComponentes.getColumnModel().getColumn(4).setResizable(false);
            tblComponentes.getColumnModel().getColumn(5).setResizable(false);
        }

        jLabel2.setText("Listado de componentes");

        txtEscaneoComponentes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEscaneoComponentesKeyReleased(evt);
            }
        });

        btnEscanearComponentes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonCodigoDeBarraMini.png"))); // NOI18N
        btnEscanearComponentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscanearComponentesActionPerformed(evt);
            }
        });

        btnGuardarComponentes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnGuardarComponentes.setText("Guardar");
        btnGuardarComponentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarComponentesActionPerformed(evt);
            }
        });

        btnEliminarComponente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEliminar.png"))); // NOI18N
        btnEliminarComponente.setText("Eliminar");
        btnEliminarComponente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarComponenteActionPerformed(evt);
            }
        });

        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        txtDescripcionProducto.setEditable(false);
        txtDescripcionProducto.setBackground(new java.awt.Color(102, 102, 102));
        txtDescripcionProducto.setForeground(new java.awt.Color(255, 255, 255));

        btnEditarSerie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEditar.png"))); // NOI18N
        btnEditarSerie.setText("Editar");
        btnEditarSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarSerieActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDetalleLayout = new javax.swing.GroupLayout(pnlDetalle);
        pnlDetalle.setLayout(pnlDetalleLayout);
        pnlDetalleLayout.setHorizontalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1140, Short.MAX_VALUE)
                    .addGroup(pnlDetalleLayout.createSequentialGroup()
                        .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDetalleLayout.createSequentialGroup()
                                .addComponent(txtEscaneoComponentes, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEscanearComponentes))
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDetalleLayout.createSequentialGroup()
                                .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarProducto)
                                .addGap(45, 45, 45)
                                .addComponent(txtCorregirSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEditarSerie)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnGuardarComponentes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEliminarComponente))
                            .addComponent(txtDescripcionProducto))))
                .addContainerGap())
        );
        pnlDetalleLayout.setVerticalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEscanearComponentes)
                    .addGroup(pnlDetalleLayout.createSequentialGroup()
                        .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnGuardarComponentes)
                                .addComponent(btnEliminarComponente)
                                .addComponent(txtCorregirSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnEditarSerie))
                            .addComponent(txtEscaneoComponentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarProducto)
                            .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlListadoProductos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblListadoProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Correlativo", "ID Producto", "Descripcion", "Serie", "Componentes", "Garantia", "Cantidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblListadoProductos.getTableHeader().setReorderingAllowed(false);
        tblListadoProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListadoProductosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblListadoProductos);
        if (tblListadoProductos.getColumnModel().getColumnCount() > 0) {
            tblListadoProductos.getColumnModel().getColumn(0).setResizable(false);
            tblListadoProductos.getColumnModel().getColumn(1).setResizable(false);
            tblListadoProductos.getColumnModel().getColumn(2).setResizable(false);
            tblListadoProductos.getColumnModel().getColumn(3).setResizable(false);
            tblListadoProductos.getColumnModel().getColumn(4).setResizable(false);
            tblListadoProductos.getColumnModel().getColumn(5).setResizable(false);
            tblListadoProductos.getColumnModel().getColumn(6).setResizable(false);
        }

        lblTItuloPantalla.setText("Listado de productos");

        javax.swing.GroupLayout pnlListadoProductosLayout = new javax.swing.GroupLayout(pnlListadoProductos);
        pnlListadoProductos.setLayout(pnlListadoProductosLayout);
        pnlListadoProductosLayout.setHorizontalGroup(
            pnlListadoProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListadoProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlListadoProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1140, Short.MAX_VALUE)
                    .addGroup(pnlListadoProductosLayout.createSequentialGroup()
                        .addComponent(lblTItuloPantalla)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlListadoProductosLayout.setVerticalGroup(
            pnlListadoProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlListadoProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTItuloPantalla)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlListadoProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlListadoProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  
    private void btnEliminarComponenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarComponenteActionPerformed
        try {
            //Eliminar el registro de base de datos
            acceso_componente.eliminarDetalleSerie(tblComponentes.getValueAt(tblComponentes.getSelectedRow(), 0).toString(),tblComponentes.getValueAt(tblComponentes.getSelectedRow(), 1).toString(), pantalla, "Eliminar Detalle Serie", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            //Eliminar fila de la tabla de Equipos
            DefaultTableModel modelo = (DefaultTableModel) tblComponentes.getModel();
            modelo.removeRow(tblComponentes.getSelectedRow());
            tblComponentes.setModel(modelo);
        } catch (Exception Error) {
            mensaje.manipulacionExcepciones("critico", "Ocurrio un error al eliminar el registro.", "Eliminar Detalle Serie");
        }
    }//GEN-LAST:event_btnEliminarComponenteActionPerformed

    private void tblListadoProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListadoProductosMouseClicked
        limpiarTablaDeComponentes();
        cargarSeries();
        txtCantidadTiempoLimiteCliente.setText("");
        txtFechaLimiteGarantiaProveedor.setDate(null);
    }//GEN-LAST:event_tblListadoProductosMouseClicked

    private void btnEscanearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscanearActionPerformed
  
        boolean deja_guardar = false;
        agregarLog("Verificando si es una Venta o una Compra.");
        
        if (Integer.parseInt(this.compra) > 0) {
            
            agregarLog("Es una Compra");
            
            if ("SI".equals(tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 5).toString())) {
                
                agregarLog("Verificando que el usuario haya ingresado el limimte de garantía.");
                
                if (txtCantidadTiempoLimiteCliente.getText().isEmpty()) {
                    mensaje.manipulacionExcepciones("critico", "Debe ingresar garantia de Proveedor y garantía para el Cliente.", "Valida Garntía");
                    deja_guardar = false;
                } else {
                    deja_guardar = true;
                }
                
            } else {
                deja_guardar = true;
            }
            
        } else {
            deja_guardar = true;
        }

        agregarLog("Deja guardar: " + deja_guardar);
        
        if (deja_guardar) {
            validarSerieEquipo();
            limpiarTablaDeComponentes();
        }

    }//GEN-LAST:event_btnEscanearActionPerformed

    private void tblListadoEquiposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListadoEquiposMouseClicked
        cargarListadoComponentes();
        if(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 8).toString().equals("Vendido")) {
            btnEntregarEquipo.setEnabled(false);
        } else {
            if(Integer.parseInt(this.venta) > 0) {
                btnEntregarEquipo.setEnabled(true);
            } else {
                btnEntregarEquipo.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tblListadoEquiposMouseClicked
    
    private void txtEscaneoComponentesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEscaneoComponentesKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            validarComponente();
        }
    }//GEN-LAST:event_txtEscaneoComponentesKeyReleased

    private void btnEscanearComponentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscanearComponentesActionPerformed
        validarComponente();
    }//GEN-LAST:event_btnEscanearComponentesActionPerformed

    private void btnGuardarComponentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarComponentesActionPerformed
        
        //Guardar la información del nuevo componente asociado a un numero de serie que se encuente en inventario
        if (tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 8).toString().equals("En Inventario") 
                || Inventory.lblRol.getText().equals("Administrador")) {
            try {
                String estado_actual = tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 8).toString();
                ObjetosDetalleSerie componente_nuevo = new ObjetosDetalleSerie();
                componente_nuevo = acceso_componente.buscarDetalleSerie(txtEscaneoComponentes.getText(), txtIdProducto.getText(),
                        tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                if (componente_nuevo.getId_d_serie() == 0) {
                    componente_nuevo.setId_d_serie(0);
                    componente_nuevo.setId_serie(Integer.parseInt(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString()));
                    componente_nuevo.setId_producto(txtIdProducto.getText());
                    componente_nuevo.setNumero_serie(txtEscaneoComponentes.getText().toUpperCase().replaceAll("'", "´"));
                    componente_nuevo.setDescripcion(txtDescripcionProducto.getText());
                    componente_nuevo.setEstado(estado_actual);
                    acceso_componente.guardarDetalleSerie(componente_nuevo, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                } else {
                    mensaje.manipulacionExcepciones("critico", "Ya fue ingresado de este nuemro de serie", "Guardar Detalle Serie");
                }
                cargarListadoComponentes();
            } catch (Exception Error) {
            }
        } else {
            mensaje.manipulacionExcepciones("critico", "Debe guardar la serie antes de agregar Componentes.", "Guardar Componente");
        }

    }//GEN-LAST:event_btnGuardarComponentesActionPerformed

    private void btnGuardarSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarSerieActionPerformed
        
        //Validar como se guardara el estado 
        if ("Compra".equals(this.pantalla)) {
            this.estado = "En Inventario";
        }
        
        //Validar como se guardara el estado 
        if ("Venta".equals(this.pantalla)) {
            this.estado = "Vendido";
        }
        
        for (int i = 0; i < tblListadoEquipos.getRowCount(); i++) {
            
            if (tblListadoEquipos.getValueAt(i, 6) != "0") {
                
                //Buscar si existe la serie en la base de datos;
                ObjetosSerie serie_a_guardar = new ObjetosSerie();
                serie_a_guardar = acceso_serie.buscarSerie(tblListadoEquipos.getValueAt(i, 6).toString(), tblListadoEquipos.getValueAt(i, 2).toString(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());        
                
                int id_almacenado = 0;

                serie_a_guardar.setId_serie(serie_a_guardar.getId_serie());
                serie_a_guardar.setId_compra(Integer.parseInt(compra));
                serie_a_guardar.setId_producto(tblListadoEquipos.getValueAt(i, 2).toString());
                serie_a_guardar.setId_factura(Integer.parseInt(tblListadoEquipos.getValueAt(i, 3).toString()));
                serie_a_guardar.setId_orden_servicio(Integer.parseInt(tblListadoEquipos.getValueAt(i, 4).toString()));
                serie_a_guardar.setDescripcion(tblListadoEquipos.getValueAt(i, 5).toString());
                serie_a_guardar.setNumero_serie(tblListadoEquipos.getValueAt(i, 6).toString().toUpperCase().replaceAll("'", "´"));
                serie_a_guardar.setEstado(estado);
                serie_a_guardar.setUsuarioregistra(Inventory.lblUsuario.getText());
                
                ObtenerFechaLimiteGarantia();
                
                serie_a_guardar.setFecha_limite_garantia_proveedor(fecha_limite_garantia_proveedor);
                serie_a_guardar.setFecha_limite_garantia_cliente(tiempo_limite_garantia_cliente);
                
                if (serie_a_guardar.getId_serie() == 0) {

                    //Si no existe esta serie la guarda en la base de datos
                    try {
                        id_almacenado = acceso_serie.guardarSerie(serie_a_guardar, "Catalogo Series", "Guardar Listado Equipos", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                        if(id_almacenado > 0) {
                            tblListadoEquipos.setValueAt(id_almacenado, i, 0);
                            tblListadoEquipos.setValueAt(estado, i, 8);
                        }
                    } catch(Exception Error) {
                        id_almacenado = 0;
                        tblListadoEquipos.setValueAt("Pendiente", i, 8);
                    }
                    
                } else {
                    
                    //Como ya existe el objeto, entonce actualizamos sus datos
                    try {
                        acceso_serie.actualizarSerie(serie_a_guardar, "Catalogo Series", "Actualizar Listado Equipos", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    } catch(Exception Error) {
                        mensaje.manipulacionExcepciones("critico", "Ocurrio un error al actualizar el registro.", "Actualizar Serie");
                    }
                    
                }
                
            } else {

                //Si ya existe este registro en la base de datos entoces actualiza estado
                //y tambien el id factura, validando que no este vendiendo el producto

                if (Integer.parseInt(this.venta) > 0) {

                    //Actualizar los registros y colocarlos como vendidos asignando el nuemro de venta que corresponde
                    String resultado = acceso_serie.actualizarEstadoProducto(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString(), this.venta, "Vendido",
                            "Catalogo Series", "Vender Equipo", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    mensaje.manipulacionExcepciones("informacion", resultado, "Actualizar Estado Serie");
                    cargarListadoComponentes();

                }
                
            }
        }
        
        cargarSeries();
        
    }//GEN-LAST:event_btnGuardarSerieActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        
        String sello = "";
        String tiempo = "";
        
        if(!cbxSello.getSelectedItem().toString().equals("Ninguno")) {
            sello = cbxSello.getSelectedItem().toString().toUpperCase();
        }
        
        if(cbxSello.getSelectedItem().toString().equals("Reacondicionado")) {
            tiempo = "6 Meses de Garantia";
        }
        
        ObjetosCertificado certificado = new ObjetosCertificado();
        AccesoCertificado acceso = new AccesoCertificado();
        
        try {
            certificado = acceso.buscarCertificado(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 6).toString(), 
                    tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 2).toString(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        } catch(Exception Error) {
            mensaje.manipulacionExcepciones("critico", "Ocurrio un error al buscar la serie. \n" + Error.getMessage(), "Buscar Datos Serie");
        }
        
        try{
            
            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();
            AccesoCaja acceso_caja = new AccesoCaja();
            ObjetosCaja objeto_caja = new ObjetosCaja();
            
            URL url_reporte = this.getClass().getResource("/inventory/reportes/rptCertificadoGarantiaGrupo.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            HashMap parametro = new HashMap();
            
            //parametros generales del encabezado
            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            //Parametros del reporte para los datos de la empresa.
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_NIT", objeto_sucursal.getNit_sucursal());
            parametro.put("P_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_INFORMACION", objeto_sucursal.getDescripcion_sucursal());
            parametro.put("P_TELEFONO", objeto_sucursal.getTelefonos_sucursal());
            parametro.put("P_USUARIO", Inventory.lblUsuario.getText().toUpperCase());

            //Parametros del reporte para el encabezado de los datos 
            parametro.put("P_ID_CLIENTE", certificado.getId_cliente());
            parametro.put("P_ID_SERIE", certificado.getId_serie());
            parametro.put("P_ID_FACTURA", this.venta);
            parametro.put("P_VENCE", certificado.getVence());
            
            parametro.put("P_NOMBRE", certificado.getNombre());
            parametro.put("P_TELEFONO", certificado.getTelefono());
            parametro.put("P_FECHA_CREACION", certificado.getFecha());
            parametro.put("P_USUARIO_RECIBE", Inventory.lblUsuario.getText());
            
            parametro.put("P_NUMERO_SERIE", certificado.getSerie());
            parametro.put("P_MARCA", certificado.getMarca());
            parametro.put("P_MODELO", certificado.getProducto());
            
            //Para la marca de agua o sello
            parametro.put("P_SELLO", sello);
            parametro.put("P_TIEMPO", tiempo);
            
            JasperPrint pantalla = JasperFillManager.fillReport(reporte, parametro, acceso_inventario.conectar());
            JInternalFrame ventana = new JInternalFrame("Inventory 3.0");
            ventana.getContentPane().add(new JRViewer(pantalla));
            ventana.setPreferredSize(new Dimension(900, 500));
            ventana.setClosable(true);
            ventana.setMaximizable(true);
            ventana.setResizable(true);
            ventana.setVisible(true);
            ventana.pack();
            
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
            int x  = (ancho  - ventana.getWidth() ) / 2;
            int y  = ( alto - ventana.getHeight() - 100 ) / 2;
            
            Inventory.pnlPrincipal.add(ventana);
            
            ventana.setLocation(x, y);
            ventana.setVisible(true);
            ventana.toFront();
            
        } catch (JRException error) {
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Certificado de Garantia");
        }
        
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void txtSerieEquipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerieEquipoKeyPressed
        
        boolean deja_guardar = false;
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            if (Integer.parseInt(this.compra) > 0) {

                if ("SI".equals(tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 5).toString())) {

                    if (txtCantidadTiempoLimiteCliente.getText().isEmpty() || txtFechaLimiteGarantiaProveedor.getDate() == null) {
                        mensaje.manipulacionExcepciones("critico", "Debe ingresar garantia de Proveedor y garantía para el Cliente.", "Valida Garntía");
                        deja_guardar = false;
                    } else {
                        deja_guardar = true;
                    }

                } else {

                    deja_guardar = true;

                }
                
            } else {
                
                deja_guardar = true;
            
            }
            
            if (deja_guardar) {
                validarSerieEquipo();
                limpiarTablaDeComponentes();
            }
            
        }
        
    }//GEN-LAST:event_txtSerieEquipoKeyPressed

    private void btnEliminaSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminaSerieActionPerformed
        if (tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 8).toString().equals("Vendido")) {
            mensaje.manipulacionExcepciones("critico", "No se puede eliminar un Equipo que ya fue vendido y entregado.", "Eliminar Numero de Serie");
        } else {
            try {
                //Eliminar el registro de base de datos
                acceso_serie.eliminarSerie(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString(), pantalla, "Eliminar Serie", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                //Eliminar fila de la tabla de Equipos
                DefaultTableModel modelo = (DefaultTableModel) tblListadoEquipos.getModel();
                modelo.removeRow(tblListadoEquipos.getSelectedRow());
                tblListadoEquipos.setModel(modelo);
            } catch (Exception Error) {
                mensaje.manipulacionExcepciones("critico", "Ocurrio un error al eliminar el registro.", "Eliminar Serie");
            }
        }
    }//GEN-LAST:event_btnEliminaSerieActionPerformed

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        wdwConsultaProductos ConsultaProductos = new wdwConsultaProductos("Catalogo Series");
        int ancho = Inventory.pnlPrincipal.getWidth();
        int alto = Inventory.pnlPrincipal.getHeight();
        int x  = (ancho / 2) - (ConsultaProductos.getWidth() / 2);
        int y  = ((alto / 2) - (ConsultaProductos.getHeight() / 2)) / 2;
        ConsultaProductos.setVisible(true);
        Inventory.pnlPrincipal.add(ConsultaProductos);
        ConsultaProductos.toFront();
        ConsultaProductos.setLocation(x, y);
        wdwConsultaProductos.btnAgregarEquipo.setEnabled(true);
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnEntregarEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntregarEquipoActionPerformed
        
        String resultado = "";
        
        if (tblListadoProductos.getValueAt(tblListadoProductos.getSelectedRow(), 4).toString().equals("SI")) {
            agregarLog("Actualizar el estado de la Serie.");
            String respuesta = acceso_componente.actualizarEstadoDetalleSerie(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString(),
                    "Vendido", "Catalogo Serie", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

            calcularFechaGarantiaCliente();

            resultado = acceso_serie.actualizarEstadoSerie(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString(),
                    "Vendido", this.venta, tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 10).toString(),
                    "Catalogo Serie", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

            cargarSeries();
            cargarListadoComponentes();
            
        } else {
            calcularFechaGarantiaCliente();
            resultado = acceso_serie.actualizarEstadoSerie(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString(),
                    "Vendido", this.venta, tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 10).toString(),
                    "Catalogo Serie", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            cargarSeries();
            cargarListadoComponentes();
        }
       
        mensaje.manipulacionExcepciones("informacion", resultado, "Entregar Equipo");
        
    }//GEN-LAST:event_btnEntregarEquipoActionPerformed

    private void txtImpresionCertificadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImpresionCertificadoActionPerformed
        
        String sello = "";
        String tiempo = "";
        
        if(!cbxSello.getSelectedItem().toString().equals("Ninguno")) {
            sello = cbxSello.getSelectedItem().toString().toUpperCase();
        }
        
        if(cbxSello.getSelectedItem().toString().equals("Reacondicionado")) {
            tiempo = "6 Meses de Garantia";
        }
         
        ObjetosCertificado certificado = new ObjetosCertificado();
        AccesoCertificado acceso = new AccesoCertificado();
        
        try {
            certificado = acceso.buscarCertificado(tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 6).toString(), 
                    tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 2).toString(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        } catch(Exception Error) {
            mensaje.manipulacionExcepciones("critico", "Ocurrio un error al buscar la serie. \n" + Error.getMessage(), "Buscar Datos Serie");
        }
        
        try{
            
            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();
            AccesoCaja acceso_caja = new AccesoCaja();
            ObjetosCaja objeto_caja = new ObjetosCaja();
            
            URL url_reporte = this.getClass().getResource("/inventory/reportes/rptCertificadoGarantia.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            HashMap parametro = new HashMap();
            
            //parametros generales del encabezado
            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            //Parametros del reporte para los datos de la empresa.
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_NIT", objeto_sucursal.getNit_sucursal());
            parametro.put("P_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_INFORMACION", objeto_sucursal.getDescripcion_sucursal());
            parametro.put("P_TELEFONO", objeto_sucursal.getTelefonos_sucursal());
            parametro.put("P_USUARIO", Inventory.lblUsuario.getText().toUpperCase());

            //Parametros del reporte para el encabezado de los datos 
            parametro.put("P_ID_CLIENTE", certificado.getId_cliente());
            parametro.put("P_ID_SERIE", certificado.getId_serie());
            parametro.put("P_ID_FACTURA", certificado.getId_factura());
            parametro.put("P_VENCE", certificado.getVence());
            
            parametro.put("P_NOMBRE", certificado.getNombre());
            parametro.put("P_TELEFONO", certificado.getTelefono());
            parametro.put("P_FECHA_CREACION", certificado.getFecha());
            parametro.put("P_USUARIO_RECIBE", Inventory.lblUsuario.getText());
            
            parametro.put("P_NUMERO_SERIE", certificado.getSerie());
            parametro.put("P_MARCA", certificado.getMarca());
            parametro.put("P_MODELO", certificado.getProducto());
            
            //parametro para la marca de agua o sello
            parametro.put("P_SELLO", sello);
            parametro.put("P_TIEMPO", tiempo);
            
            JasperPrint pantalla = JasperFillManager.fillReport(reporte, parametro, acceso_inventario.conectar());
            JInternalFrame ventana = new JInternalFrame("Inventory 3.0");
            ventana.getContentPane().add(new JRViewer(pantalla));
            ventana.setPreferredSize(new Dimension(900, 500));
            ventana.setClosable(true);
            ventana.setMaximizable(true);
            ventana.setResizable(true);
            ventana.setVisible(true);
            ventana.pack();
            
            int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
            int x  = (ancho  - ventana.getWidth() ) / 2;
            int y  = ( alto - ventana.getHeight() - 100 ) / 2;
            
            Inventory.pnlPrincipal.add(ventana);
            
            ventana.setLocation(x, y);
            ventana.setVisible(true);
            ventana.toFront();
            
        } catch (JRException error) {
            mensaje.manipulacionExcepciones("critico", error.getMessage(), "Certificado de Garantia");
        }
        
    }//GEN-LAST:event_txtImpresionCertificadoActionPerformed

    private void btnEditarSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarSerieActionPerformed
        if(txtCorregirSerie.getText().isEmpty()) {
            mensaje.manipulacionExcepciones("critico", "Por favor ingresar el nunero de Serie nuevo.", "Modificar Nunmero Serie");
        } else {
            
            String re = "";
            String id = tblComponentes.getValueAt(tblComponentes.getSelectedRow(), 0).toString();
            String pr = tblComponentes.getValueAt(tblComponentes.getSelectedRow(), 2).toString();
            String se = txtCorregirSerie.getText().trim().replaceAll("'", "´");
            
            AccesoDetalleSerie acceso_detalle = new AccesoDetalleSerie();
            boolean existe_serie = acceso_detalle.validarDetalleSerie(se, pr, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            if(existe_serie == false) {
                re = acceso_serie.modificarNumeroSerie(id, se, "Catalo de Series", "Modificar", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                if(re.equals("Operacion realizada con exito.")) {
                    tblComponentes.setValueAt(txtCorregirSerie.getText(), tblComponentes.getSelectedRow(), 3);
                }
            } else {
                re = "Este número de Serie ya esta registrado.";
            }
            
            mensaje.manipulacionExcepciones("informacion", re, "Editar Serie");
            
        }
    }//GEN-LAST:event_btnEditarSerieActionPerformed

    private void btnEditarGarantiaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarGarantiaClienteActionPerformed
        
        String resultado = "";
        boolean fecha_para_guardar = false;
        String id = tblListadoEquipos.getValueAt(tblListadoEquipos.getSelectedRow(), 0).toString();
        
        String f_g_cliente = "";
        
        try {
            f_g_cliente = (txtFechaGarantiaLimiteCliente.getDate().getYear() + 1900) + "-" + (txtFechaGarantiaLimiteCliente.getDate().getMonth() + 1) + "-" + txtFechaGarantiaLimiteCliente.getDate().getDate();
            agregarLog("Fecha Límite Cliente: " + f_g_cliente);
            fecha_para_guardar = true;
        } catch (Exception Error) {
            fecha_para_guardar = false;
            agregarLog("Fecha Límite Cliente: " + Error.toString());
        }

        if (fecha_para_guardar) {
            resultado = acceso_serie.modificarGarantiaClienteSerie(id, f_g_cliente, "Catalo de Series", "Modificar", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            if (resultado.equals("Operacion realizada con exito.")) {
                limpiarTablaDeComponentes();
                cargarSeries();
                txtCantidadTiempoLimiteCliente.setText("");
                txtFechaLimiteGarantiaProveedor.setDate(null);mensaje.manipulacionExcepciones("critico", resultado, "Cambiar Fecha Garantia Cliente");
            } else {
                mensaje.manipulacionExcepciones("critico", "No se pudo cambiar la Fecha de Garantia para Cliente.", "Cambiar Fecha Garantia Cliente");
            }
        }
        
    }//GEN-LAST:event_btnEditarGarantiaClienteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnEditarGarantiaCliente;
    private javax.swing.JButton btnEditarSerie;
    private javax.swing.JButton btnEliminaSerie;
    private javax.swing.JButton btnEliminarComponente;
    private javax.swing.JButton btnEntregarEquipo;
    private javax.swing.JButton btnEscanear;
    private javax.swing.JButton btnEscanearComponentes;
    private javax.swing.JButton btnGuardarComponentes;
    private javax.swing.JButton btnGuardarSerie;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JComboBox cbxSello;
    private javax.swing.JComboBox cbxTipoLimiteCliente;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblTItuloPantalla;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlListadoProductos;
    public static javax.swing.JTable tblComponentes;
    public static javax.swing.JTable tblListadoEquipos;
    public static javax.swing.JTable tblListadoProductos;
    private javax.swing.JTextField txtCantidadTiempoLimiteCliente;
    private javax.swing.JTextField txtCorregirSerie;
    public static javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtEscaneoComponentes;
    private com.toedter.calendar.JDateChooser txtFechaGarantiaLimiteCliente;
    private com.toedter.calendar.JDateChooser txtFechaLimiteGarantiaProveedor;
    public static javax.swing.JTextField txtIdProducto;
    private javax.swing.JButton txtImpresionCertificado;
    private javax.swing.JTextField txtSerieEquipo;
    // End of variables declaration//GEN-END:variables
}
