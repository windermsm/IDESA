package inventory.vistas;

import inventory.acceso.AccesoExcepciones;
import inventory.acceso.AccesoSucursal;
import inventory.librerias.NormalCell;
import inventory.objetos.ObjetosSucursal;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class wdwCatalogoSucursales extends javax.swing.JInternalFrame {

    /**
     * Creates new form CatalogoAdmonProductos
     */
    AccesoExcepciones Mensaje = new AccesoExcepciones();
    AccesoSucursal nuevaSucursal = new AccesoSucursal();
    ObjetosSucursal sucursal = new ObjetosSucursal();
    String seleccion = "";

//AccesoProveedor nuevoProveedor = new AccesoProveedor();
    public wdwCatalogoSucursales() {
        initComponents();
        buscarEnRegistrosGuardados();
        tblSucursal.setDefaultRenderer(Object.class, new NormalCell());
    }
    
    private void guardarRegistro() {
        // Si se ingresan todos los datos del producto asigno valor al objeto
        if (verificarDatosProducto()) {
            sucursal.setNombre_sucursal(txtNombreSucursal.getText().toUpperCase());
            sucursal.setDescripcion_sucursal(txtDescripcionSucursal.getText());
            sucursal.setDireccion_sucursal(txtDireccionSucursal.getText());
            sucursal.setNit_sucursal(txtNIT.getText());
            sucursal.setTelefonos_sucursal(txtTelefonos.getText());
        }
        
        try {
            nuevaSucursal.insertarSucursal(sucursal, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        } catch (Exception error) {
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Insertar Sucursal");
        }
    }
    
    private boolean verificarDatosProducto() {
        boolean vDatosCorrectos = true;
        
        if (txtNombreSucursal.getText().isEmpty()) {
            Mensaje.manipulacionExcepciones("critico", "Ingrese el nombre de la sucursal.", "Verificar Datos Producto");
            vDatosCorrectos = false;
        }
        if (txtDescripcionSucursal.getText().isEmpty()) {
            Mensaje.manipulacionExcepciones("critico", "Ingrese la descripcion de la sucursal.", "Verificar Datos Producto");
            vDatosCorrectos = false;
        }
        if (txtDireccionSucursal.getText().isEmpty()) {
            Mensaje.manipulacionExcepciones("critico", "Ingrese la direccion de la sucursal.", "Verificar Datos Producto");
            vDatosCorrectos = false;
        }
        if (txtNIT.getText().isEmpty()) {
            Mensaje.manipulacionExcepciones("critico", "Ingrese el nit de la empresa.", "Verificar Datos Producto");
            vDatosCorrectos = false;
        }
        if (txtTelefonos.getText().isEmpty()) {
            Mensaje.manipulacionExcepciones("critico", "Ingrese los telefonos.", "Verificar Datos Producto");
            vDatosCorrectos = false;
        }
        
        return vDatosCorrectos;
    }
    
    public void limpiarCampos() {
        btnGuardar.setEnabled(true);
        txtIdSucursal.setText("");
        txtNombreSucursal.setText("");
        txtDescripcionSucursal.setText("");
        txtDireccionSucursal.setText("");
        txtNIT.setText("");
        txtTelefonos.setText("");
        txtBuscar.setText("");
    }
    
    private void buscarEnRegistrosGuardados() {
        
        String busqueda = txtBuscar.getText();
         System.out.println(1);
        if (busqueda != null || !busqueda.equals("")) {
             System.out.println(2);
            DefaultTableModel tabla = new DefaultTableModel();
            ArrayList<ObjetosSucursal> listaSucursales = new ArrayList();
            
            int contadorFilas = 1;
            int c = 0;
            
            try {
                listaSucursales = nuevaSucursal.seleccionarSucursal(busqueda, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());                
            } catch (Exception error) {
                Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Seleccionar Sucursal");
            }

            if (listaSucursales.isEmpty()) {
                Mensaje.manipulacionExcepciones("critico", "No existen datos de sucursales almacenados.", "Buscar en Registros Guardados");
            } else {
                tabla.addColumn("Codigo");
                tabla.addColumn("Nombre");
                tabla.addColumn("Descripcion");
                tabla.addColumn("Direccion");
                tabla.addColumn("NIT");
                tabla.addColumn("Telefonos");
                for (ObjetosSucursal xSuc : listaSucursales) {
                    tabla.setRowCount(contadorFilas);                
                    tabla.setValueAt(xSuc.getId_sucursal(), c, 0); 
                    tabla.setValueAt(xSuc.getNombre_sucursal(), c, 1);  
                    tabla.setValueAt(xSuc.getDescripcion_sucursal(), c, 2); 
                    tabla.setValueAt(xSuc.getDireccion_sucursal(), c, 3); 
                    tabla.setValueAt(xSuc.getNit_sucursal(), c, 4); 
                    tabla.setValueAt(xSuc.getTelefonos_sucursal(), c, 5); 
                    c++;
                    contadorFilas++;
                 }
                tblSucursal.setModel(tabla);
            }
        } else {
             System.out.println("Entro a mostrar registros guardados");
            mostrarRegistrosGuardados();
        }
    }
    
    public void mostrarRegistrosGuardados() {
        
        DefaultTableModel tabla = new DefaultTableModel();
        ArrayList<ObjetosSucursal> listaSucursales = new ArrayList();
        
        int contadorFilas = 1;
        int c = 0;
        
        try {
            listaSucursales = nuevaSucursal.retornaSucursales(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        } catch (Exception error) {
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Retorna Sucursales");
        }
        
        if (listaSucursales.isEmpty()) {
            Mensaje.manipulacionExcepciones("critico", "No existen datos de sucursales almacenados.", "Mostrar Registros Guardados");
        } else {
            tabla.addColumn("Codigo");
            tabla.addColumn("Nombre");
            tabla.addColumn("Descripcion");
            tabla.addColumn("Direccion");
            tabla.addColumn("NIT");
            tabla.addColumn("Telefonos");
            
            for (ObjetosSucursal xSuc : listaSucursales) {
                tabla.setRowCount(contadorFilas);
                tabla.setValueAt(xSuc.getId_sucursal(), c, 0);
                tabla.setValueAt(xSuc.getNombre_sucursal(), c, 1);
                tabla.setValueAt(xSuc.getDescripcion_sucursal(), c, 2);
                tabla.setValueAt(xSuc.getDireccion_sucursal(), c, 3);
                tabla.setValueAt(xSuc.getNit_sucursal(), c, 4);
                tabla.setValueAt(xSuc.getTelefonos_sucursal(), c, 5);
                c++;
                contadorFilas++;
            }
            tblSucursal.setModel(tabla);
        }
    }
    
    private int obtenerIdUsuario(String pNombre, String pDescripcion, String pDireccion) {
        
        int vId_producto = 0;
        ArrayList<ObjetosSucursal> BuscarUsuario = new ArrayList();
        
        try {
            BuscarUsuario = nuevaSucursal.obtenerIdSuc(pNombre, pDescripcion, pDireccion, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        } catch (Exception error) {
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Obtener ID Sucursal");
        }
        
        if (BuscarUsuario.isEmpty()) {
            Mensaje.manipulacionExcepciones("informacion", "Debe crear la sucursal en el sistema.", "Obtener ID Usuario");
        } else {
            if (BuscarUsuario.size() > 1) {
                Mensaje.manipulacionExcepciones("critico", "Este ID esta repetido.", "Obtener ID Usuario");
            } else {
                vId_producto = BuscarUsuario.get(0).getId_sucursal();
            }
        }
        
        return vId_producto;
        
    }
    
    private void actualizarRegistro() {
        
        sucursal.setId_sucursal(Integer.parseInt(txtIdSucursal.getText()));
        sucursal.setNombre_sucursal(txtNombreSucursal.getText());
        sucursal.setDescripcion_sucursal(txtDescripcionSucursal.getText());
        sucursal.setDireccion_sucursal(txtDireccionSucursal.getText());
        sucursal.setNit_sucursal(txtNIT.getText());
        sucursal.setTelefonos_sucursal(txtTelefonos.getText());
        
        try {
            Mensaje.manipulacionExcepciones("informacion", nuevaSucursal.actualizarSucursal(sucursal, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText()), "Actualizar Sucursal");
        } catch (Exception error) {
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Actualizar Sucursal");
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

        pnlEncabezado = new javax.swing.JPanel();
        lblIdSucursal = new javax.swing.JLabel();
        txtIdSucursal = new javax.swing.JTextField();
        lblNombreSucursal = new javax.swing.JLabel();
        lblDescripcionSucursal = new javax.swing.JLabel();
        txtDescripcionSucursal = new javax.swing.JTextField();
        lblDireccionSucursal = new javax.swing.JLabel();
        txtDireccionSucursal = new javax.swing.JTextField();
        txtNombreSucursal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtTelefonos = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNIT = new javax.swing.JTextField();
        pnlBotones = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        pnlDetalle = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSucursal = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Sucursales");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgCatalogoSucursales.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(700, 600));
        setMinimumSize(new java.awt.Dimension(700, 600));
        setPreferredSize(new java.awt.Dimension(700, 600));

        pnlEncabezado.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblIdSucursal.setText("ID Sucursal");

        txtIdSucursal.setBackground(new java.awt.Color(102, 102, 102));
        txtIdSucursal.setEditable(false);
        txtIdSucursal.setForeground(new java.awt.Color(255, 255, 255));
        txtIdSucursal.setToolTipText("Este campo es colocado automaticamente por el sistema");

        lblNombreSucursal.setText("Nombre");

        lblDescripcionSucursal.setText("Descripcion");
        lblDescripcionSucursal.setToolTipText("");

        txtDescripcionSucursal.setBackground(new java.awt.Color(255, 255, 204));
        txtDescripcionSucursal.setToolTipText("Ingrese una descripcion para la sucursal");

        lblDireccionSucursal.setText("Direccion");

        txtDireccionSucursal.setBackground(new java.awt.Color(255, 255, 204));
        txtDireccionSucursal.setToolTipText("Ingrese la direccion de la sucursal");

        txtNombreSucursal.setBackground(new java.awt.Color(255, 255, 204));
        txtNombreSucursal.setToolTipText("Ingrese un nombre para la sucursal");

        jLabel1.setText("Telefonos");

        txtTelefonos.setBackground(new java.awt.Color(255, 255, 204));

        jLabel2.setText("NIT");

        txtNIT.setBackground(new java.awt.Color(255, 255, 204));

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIdSucursal)
                    .addComponent(lblDescripcionSucursal)
                    .addComponent(lblDireccionSucursal)
                    .addComponent(lblNombreSucursal))
                .addGap(18, 18, 18)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreSucursal)
                    .addComponent(txtDireccionSucursal)
                    .addComponent(txtDescripcionSucursal, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                    .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                        .addComponent(txtIdSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNIT, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTelefonos)))
                .addContainerGap())
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdSucursal)
                    .addComponent(txtIdSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtTelefonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtNIT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombreSucursal)
                    .addComponent(txtNombreSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccionSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDireccionSucursal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescripcionSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDescripcionSucursal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtDireccionSucursal.getAccessibleContext().setAccessibleName("txtDireccionSucursal");

        pnlBotones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblBuscar.setText("Buscar");

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonLimpiar.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEditar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonGuardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscar.setActionCommand("buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        txtBuscar.setToolTipText("Ingrese el nombre del producto que desea buscar");

        javax.swing.GroupLayout pnlBotonesLayout = new javax.swing.GroupLayout(pnlBotones);
        pnlBotones.setLayout(pnlBotonesLayout);
        pnlBotonesLayout.setHorizontalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlBotonesLayout.setVerticalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscar)
                    .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblBuscar)
                        .addComponent(btnLimpiar)
                        .addComponent(btnModificar)
                        .addComponent(btnGuardar)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblSucursal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblSucursal.getTableHeader().setReorderingAllowed(false);
        tblSucursal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSucursalMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSucursal);

        javax.swing.GroupLayout pnlDetalleLayout = new javax.swing.GroupLayout(pnlDetalle);
        pnlDetalle.setLayout(pnlDetalleLayout);
        pnlDetalleLayout.setHorizontalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnlDetalleLayout.setVerticalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEncabezado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
       guardarRegistro();
       limpiarCampos();
       buscarEnRegistrosGuardados();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarEnRegistrosGuardados();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void tblSucursalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSucursalMouseClicked
        btnGuardar.setEnabled(false);        
        txtIdSucursal.setText(String.valueOf(tblSucursal.getValueAt(tblSucursal.getSelectedRow(), 0)));
        txtNombreSucursal.setText(String.valueOf(tblSucursal.getValueAt(tblSucursal.getSelectedRow(), 1)));
        txtDescripcionSucursal.setText(String.valueOf(tblSucursal.getValueAt(tblSucursal.getSelectedRow(), 2)));
        txtDireccionSucursal.setText(String.valueOf(tblSucursal.getValueAt(tblSucursal.getSelectedRow(), 3)));  
        txtNIT.setText(tblSucursal.getValueAt(tblSucursal.getSelectedRow(), 4).toString());
        txtTelefonos.setText(tblSucursal.getValueAt(tblSucursal.getSelectedRow(), 5).toString());
    }//GEN-LAST:event_tblSucursalMouseClicked

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        if(txtDescripcionSucursal.getText().isEmpty()){
            Mensaje.manipulacionExcepciones("informacion", "Debe seleccionar el registro a modificar.", "Boton Modificar");
        }else{
            actualizarRegistro();
            buscarEnRegistrosGuardados();
            limpiarCampos();
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblDescripcionSucursal;
    private javax.swing.JLabel lblDireccionSucursal;
    private javax.swing.JLabel lblIdSucursal;
    private javax.swing.JLabel lblNombreSucursal;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JTable tblSucursal;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDescripcionSucursal;
    private javax.swing.JTextField txtDireccionSucursal;
    private javax.swing.JTextField txtIdSucursal;
    private javax.swing.JTextField txtNIT;
    private javax.swing.JTextField txtNombreSucursal;
    private javax.swing.JTextField txtTelefonos;
    // End of variables declaration//GEN-END:variables
}
