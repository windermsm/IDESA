package inventory.vistas;

import inventory.acceso.AccesoExcepciones;
import inventory.acceso.AccesoProducto;
import inventory.objetos.ObjetosProducto;
import inventory.servicios.ServicioLeerArchivoExcel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

public class wdwBaseDeDatosExportarCatalogoDeProductos extends javax.swing.JInternalFrame {

    /**
     * Creates new form wdwBaseDeDatosExportarCatalogoDeProductos
     */
    public wdwBaseDeDatosExportarCatalogoDeProductos() {
        initComponents();
        mostrarRegistrosGuardados();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatosExcel = new javax.swing.JTable();
        btnGuardarArchivo = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Exportar Catalogo de Productos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBaseDeDatosExportar.png"))); // NOI18N

        tblDatosExcel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Producto", "Marca", "Descripcion", "Linea", "Precio Compra", "Precio Estandar", "ID Proveedor", "Existencia", "Unidad Medida", "Minimo", "Maximo", "% Estandar", "% Minimo", "Precio Minimo", "Codigo Fabricante", "Ubicacion", "Factura", "Descuento", "Visible", "Serie", "Componente", "Garantia"
            }
        ));
        jScrollPane1.setViewportView(tblDatosExcel);

        btnGuardarArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnGuardarArchivo.setText("Guardar");
        btnGuardarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarArchivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGuardarArchivo)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardarArchivo)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mostrarRegistrosGuardados(){
        
        AccesoExcepciones Mensaje = new AccesoExcepciones();
        DefaultTableModel tabla = new DefaultTableModel();
        
        AccesoProducto Producto = new AccesoProducto();
        
        ArrayList<ObjetosProducto> listaProductos = new ArrayList();
        
        //VERIFICAR ALGUN ERROR AL CARGAR LA INFORMACION DE LOS CLIENTES
        try{
            listaProductos = Producto.seleccionarProducto(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), Inventory.lblRol.getText());
        }catch(Error error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Seleccionar Producto");
        }
        
        //MOSTRAR LA INFORMACION DE LOS PRODUCTOS EN LA TABLA DE LA VISTA
        if(listaProductos.isEmpty()){
            Mensaje.manipulacionExcepciones("critico", "No existen datos almacenados de productos.", "Mostrar Registros Guardados");
        } else {
            tabla.addColumn("ID Producto");
            tabla.addColumn("Marca");
            tabla.addColumn("Descripcion");
            tabla.addColumn("Linea");
            tabla.addColumn("Precio Compra");
            tabla.addColumn("Precio Estandar");
            tabla.addColumn("ID Proveedor");
            tabla.addColumn("Existencia");
            tabla.addColumn("Unidad Medida");
            tabla.addColumn("Minimo");
            tabla.addColumn("Maximo");
            tabla.addColumn("% Estandar");
            tabla.addColumn("% Minimo");
            tabla.addColumn("Precio Minimo");
            tabla.addColumn("Codigo Fabricante");
            tabla.addColumn("Ubicacion");
            tabla.addColumn("Factura");
            tabla.addColumn("Descuento");
            tabla.addColumn("Visible");
            tabla.addColumn("Serie");
            tabla.addColumn("Componente");
            tabla.addColumn("Garantia");
            
            
            tabla.setRowCount(listaProductos.size());
            int cProducto = 0;
            
            for(ObjetosProducto xProducto:listaProductos ){
                tabla.setValueAt(xProducto.getId_producto(), cProducto, 0);
                tabla.setValueAt(xProducto.getMarca_producto(), cProducto, 1);
                tabla.setValueAt(xProducto.getDesc_producto(), cProducto, 2);
                tabla.setValueAt(xProducto.getLinea_producto(), cProducto, 3);
                tabla.setValueAt(xProducto.getPrecio_compra_producto(), cProducto, 4);
                tabla.setValueAt(xProducto.getPrecio_est_producto(), cProducto, 5);
                tabla.setValueAt(xProducto.getId_proveedor(), cProducto, 6);
                tabla.setValueAt(xProducto.getExi_producto(), cProducto, 7);
                tabla.setValueAt(xProducto.getUnidad_medida_producto(), cProducto, 8);
                tabla.setValueAt(xProducto.getMinimo_producto(), cProducto, 9);
                tabla.setValueAt(xProducto.getMaximo_producto(), cProducto, 10);
                tabla.setValueAt(xProducto.getPrj_est_producto(), cProducto, 11);
                tabla.setValueAt(xProducto.getPrj_min_producto(), cProducto, 12);
                tabla.setValueAt(xProducto.getPrecio_min_producto(), cProducto, 13);
                tabla.setValueAt(xProducto.getCodigo_fabricante(), cProducto, 14);
                tabla.setValueAt(xProducto.getUbicacion_producto(), cProducto, 15);
                tabla.setValueAt(xProducto.getFactura_producto(), cProducto, 16);
                tabla.setValueAt(xProducto.getDescuento_producto(), cProducto, 17);
                tabla.setValueAt(xProducto.getVisible_producto(), cProducto, 18);
                tabla.setValueAt(xProducto.getSerie_producto(), cProducto, 19);
                tabla.setValueAt(xProducto.getComponente_producto(), cProducto, 20);
                tabla.setValueAt(xProducto.getGarantia_producto(), cProducto, 21);
                cProducto++;
            }
            tblDatosExcel.setModel(tabla);
        }
    }
    
    private void btnGuardarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarArchivoActionPerformed
        AccesoExcepciones Mensaje = new AccesoExcepciones();
        JFileChooser flcArchivo = new JFileChooser(".");
        flcArchivo.setApproveButtonText("Guardar");
        int estado = flcArchivo.showOpenDialog(null);        
        if (estado == JFileChooser.APPROVE_OPTION) {
            File archivo_seleccionado = flcArchivo.getSelectedFile();
            String direccion = archivo_seleccionado.getParent()+"\\"+archivo_seleccionado.getName();            
            try{
                ServicioLeerArchivoExcel archivo = new ServicioLeerArchivoExcel();
                archivo.escribir_archivo_excel(tblDatosExcel, new File(direccion));
                Mensaje.manipulacionExcepciones("informacion", "El archivo fue generado con exito.", "Escribir Archivo Excel");
            }catch(IOException error){
                Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Escribir Archivo Excel");
            }
        } else if (estado == JFileChooser.CANCEL_OPTION) {
            System.out.println("Se ha cancelado la operacion");
        }
    }//GEN-LAST:event_btnGuardarArchivoActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarArchivo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDatosExcel;
    // End of variables declaration//GEN-END:variables
}
