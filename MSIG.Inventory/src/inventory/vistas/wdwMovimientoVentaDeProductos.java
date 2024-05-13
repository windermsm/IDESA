package inventory.vistas;

import inventory.acceso.*;
import inventory.librerias.CustomCell;
import inventory.librerias.DisableCell;
import inventory.librerias.NormalCell;
import inventory.librerias.WindowController;
import inventory.objetos.*;
import inventory.servicios.Matematicas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

public class wdwMovimientoVentaDeProductos extends javax.swing.JInternalFrame {
    
    String Log = "";
    String UsuarioLogeado = null;
    String rolUsuario = null;
    String metodo_ingreso = null;
    String metodo_factura = null;
    AccesoArchivo archivo = new AccesoArchivo();
    Matematicas matematica = new Matematicas();
    
    private WindowController ventana = new WindowController();
    
    /**
     * Creates new form wdwMovimientoVentaDeProductos
     */
    public wdwMovimientoVentaDeProductos() {
        
        initComponents();
        cargaEmpleados();
        cargaTipoVenta();
        
        this.Log = "";
        
        mostrarRegistrosGuardados();
        
        //Inicializar la fila seleccionada
        agregarLog("Inicializando el valor de la fila seleccionda.");
        fila_seleccionada = 0;
        
        agregarLog("Inicializando variables globales.");
        //El campo pos se habilita unicamente si elije tipo venta Tarjeta
        txtPOS.setEnabled(false);
        btnGuardar.setEnabled(false);

        //Obtengo el usuario logeado desde pantalla principal
        UsuarioLogeado = Inventory.lblUsuario.getText();
        
        //Obtengo el rol del usuario logeado
        rolUsuario = NuevaFactura.retornatRolUsuario(UsuarioLogeado, Inventory.lblTerminal.getText());
        
        agregarLog("Verificando el método de ingreso de los productos.");
        //obtengo el metodo de ingreso utilizado manual o codigo de barras
        try {
            metodo_ingreso = archivo.leer("[MetodoEntrada]");
        } catch (IOException error) {
            metodo_ingreso = "Manual";
            agregarLog("Error al obtener el metodo de entrada: " + error.toString());
        }
        
        if(metodo_ingreso.equals("Manual")) {
            txtMetodoEntrada.setText("INGRESO MANUAL");
        } else {
           txtMetodoEntrada.setText("CODIGO DE BARRAS"); 
        }
        
        //Obtener el metodo de facturacion
        agregarLog("Obtener el metodo de facturacion.");
        try {
            metodo_factura = archivo.leer("[FacturaImpresa]");
        } catch (IOException error) {
            metodo_factura = "NO";
            agregarLog("Error al obtener el metodo de entrada: " + error.toString());
        }
        
        if(metodo_factura.equals("SI") || metodo_factura.equals("si") || metodo_factura.equals("Si") || metodo_factura.equals("sI")) {
            txtSerieFactura.setEditable(true);
            txtSerieFactura.setBackground(Color.WHITE);
            txtSerieFactura.setForeground(Color.RED);
            txtNumFactura.setEditable(true);
            txtNumFactura.setBackground(Color.WHITE);
            txtNumFactura.setForeground(Color.RED);
            txtSerieFacturaFEL.setEditable(true);
            txtSerieFacturaFEL.setBackground(Color.WHITE);
            txtSerieFacturaFEL.setForeground(Color.RED);
            txtNumFacturaFEL.setEditable(true);
            txtNumFacturaFEL.setBackground(Color.WHITE);
            txtNumFacturaFEL.setForeground(Color.RED);
            txtAutorizacionFEL.setEditable(true);
            txtAutorizacionFEL.setBackground(Color.WHITE);
            txtAutorizacionFEL.setForeground(Color.RED);
            txtFolioFactura.setEditable(true);
            txtFolioFactura.setBackground(Color.WHITE);
            txtFolioFactura.setForeground(Color.RED);
        } else {
            txtSerieFactura.setEditable(false);
            txtSerieFactura.setBackground(new Color(102, 102, 102));
            txtSerieFactura.setForeground(Color.WHITE);
            txtNumFactura.setEditable(false);
            txtNumFactura.setBackground(new Color(102, 102, 102));
            txtNumFactura.setForeground(Color.WHITE);
            txtSerieFacturaFEL.setEditable(false);
            txtSerieFacturaFEL.setBackground(new Color(102, 102, 102));
            txtSerieFacturaFEL.setForeground(Color.WHITE);
            txtNumFacturaFEL.setEditable(false);
            txtNumFacturaFEL.setBackground(new Color(102, 102, 102));
            txtNumFacturaFEL.setForeground(Color.WHITE);
            txtAutorizacionFEL.setEditable(false);
            txtAutorizacionFEL.setBackground(new Color(102, 102, 102));
            txtAutorizacionFEL.setForeground(Color.WHITE);
            txtFolioFactura.setEditable(false);
            txtFolioFactura.setBackground(new Color(102, 102, 102));
            txtFolioFactura.setForeground(Color.WHITE);
        }
        
        //tratar de asignar automaticamente el empleado a la venta
        try {
            cbxEmpleado.setSelectedItem(Inventory.lblNombre.getText());
        } catch(Exception error) {
            Mensaje.manipulacionExcepciones("informacion", "No se encontro al empleado que esta activo", "Asignar Empleado");
        }

        //Colocar oculto en teoria el valor del precio de compra
        if ("Administrador".equals(rolUsuario) || "Jefe Sucursal".equals(rolUsuario)) {
            tblProducto.setDefaultRenderer(Object.class, new NormalCell());
            txtPrecioCompra.setBackground(new Color(102, 102, 102));
            txtPrecioCompra.setForeground(new Color(255, 255, 255));
        } else {
            tblProducto.setDefaultRenderer(Object.class, new CustomCell());
            tblDetalleFactura.setDefaultRenderer(Object.class, new DisableCell());
        }
        
        //Validar los porcentajes de descuento
        if("Administrador".equals(rolUsuario) || "Jefe Sucursal".equals(rolUsuario)) {
            cbxDescuento.addItem("0");
            cbxDescuento.addItem("5");
            cbxDescuento.addItem("10");
            cbxDescuento.addItem("20");
            cbxDescuento.addItem("40");
        } else {
            cbxDescuento.addItem("0");
            cbxDescuento.addItem("5");
            cbxDescuento.addItem("10");
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

        pnlBotones = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnFactura = new javax.swing.JButton();
        txtIdBusqueda = new javax.swing.JTextField();
        cbxTipoBusqueda = new javax.swing.JComboBox();
        cbxTipoImpresion = new javax.swing.JComboBox();
        cbxTamanoImpresion = new javax.swing.JComboBox();
        btnSerie = new javax.swing.JButton();
        bntActivarPos = new javax.swing.JButton();
        pnlDetalle = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalleFactura = new javax.swing.JTable();
        pnlBusqueda = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        btnBuscarProducto = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPrecioVenta = new javax.swing.JFormattedTextField();
        txtPrecioMinimo = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        txtIdProducto = new javax.swing.JTextField();
        txtDescripcionProducto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPrecioNormal = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCantidadVenta = new javax.swing.JFormattedTextField();
        txtPrecioDescuento = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        cbxDescuento = new javax.swing.JComboBox();
        txtFilaActual = new javax.swing.JFormattedTextField();
        txtCantidadMinima = new javax.swing.JTextField();
        btnMostrarImagen = new javax.swing.JButton();
        txtPrecioCompra = new javax.swing.JFormattedTextField();
        txtPermiteDescuento = new javax.swing.JFormattedTextField();
        btnAutorizacion = new javax.swing.JButton();
        pnlDetalleProducto = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        pnlDatosCliente = new javax.swing.JPanel();
        lblNitPersona = new javax.swing.JLabel();
        txtNitPersona = new javax.swing.JTextField();
        lblTelCliente = new javax.swing.JLabel();
        txtTelCliente = new javax.swing.JTextField();
        lblNombrePersona = new javax.swing.JLabel();
        txtNombrePersona = new javax.swing.JTextField();
        txtDirCliente = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtDirEnvioFactura = new javax.swing.JTextField();
        lblDirEnvioFactura = new javax.swing.JLabel();
        lblDiasCredito = new javax.swing.JLabel();
        cbxDiasCredito = new javax.swing.JComboBox();
        lblTipoCliente = new javax.swing.JLabel();
        cbxTipoDeCliente = new javax.swing.JComboBox();
        txtMetodoEntrada = new javax.swing.JTextField();
        lblTotalFactura2 = new javax.swing.JLabel();
        txtNitSinGuion = new javax.swing.JTextField();
        pnlVenta = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtIdEmpleado = new javax.swing.JTextField();
        cbxEmpleado = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        cbxTipoVenta = new javax.swing.JComboBox();
        lblEstado = new javax.swing.JLabel();
        cbxEstadoVenta = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        txtPagoCredito = new javax.swing.JTextField();
        lblContado = new javax.swing.JLabel();
        txtCambio = new javax.swing.JTextField();
        lblSerieFactura1 = new javax.swing.JLabel();
        txtPOS = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtComision = new javax.swing.JTextField();
        txtPagoContado = new javax.swing.JTextField();
        lblIVA = new javax.swing.JLabel();
        txtIva = new javax.swing.JTextField();
        txtContado = new javax.swing.JTextField();
        txtCredito = new javax.swing.JTextField();
        pnlFactura = new javax.swing.JPanel();
        lblIdFactura = new javax.swing.JLabel();
        txtIdFactura = new javax.swing.JTextField();
        lblFecha = new javax.swing.JLabel();
        txtFechaEmisionFactura = new javax.swing.JTextField();
        lblNumFactura = new javax.swing.JLabel();
        txtNumFacturaFEL = new javax.swing.JTextField();
        lblSerieFactura = new javax.swing.JLabel();
        txtSerieFactura = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtAutorizacionFEL = new javax.swing.JTextField();
        txtFolioFactura = new javax.swing.JTextField();
        lblFolioFactura = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtRecibidoPor = new javax.swing.JTextField();
        lblTotalFactura = new javax.swing.JLabel();
        txtTotalFactura = new javax.swing.JTextField();
        txtSerieFacturaFEL = new javax.swing.JTextField();
        txtNumFactura = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Venta de Productos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMovimientoVentaDeProductos.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1200, 720));
        setMinimumSize(new java.awt.Dimension(1200, 720));
        setPreferredSize(new java.awt.Dimension(1200, 720));

        pnlBotones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonNuevo.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
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
        btnBuscar.setActionCommand("buscarProducto");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonEliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonImpresoraLaser.png"))); // NOI18N
        btnFactura.setToolTipText("Imprimir Factura");
        btnFactura.setActionCommand("imprimirEnvio");
        btnFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturaActionPerformed(evt);
            }
        });

        cbxTipoBusqueda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Factura", "Proforma", "Servicio" }));

        cbxTipoImpresion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Comprobante", "Factura Electronica", "Vale de Mercaderia" }));

        cbxTamanoImpresion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Carta", "1/2 Carta", "TMU 2.5" }));

        btnSerie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonCodigoDeBarraMini.png"))); // NOI18N
        btnSerie.setActionCommand("btnReporte");
        btnSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerieActionPerformed(evt);
            }
        });

        bntActivarPos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonActivarPos.png"))); // NOI18N
        bntActivarPos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntActivarPosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBotonesLayout = new javax.swing.GroupLayout(pnlBotones);
        pnlBotones.setLayout(pnlBotonesLayout);
        pnlBotonesLayout.setHorizontalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtIdBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxTipoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbxTipoImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxTamanoImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSerie)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bntActivarPos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addContainerGap())
        );
        pnlBotonesLayout.setVerticalGroup(
            pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIdBusqueda)
                        .addComponent(cbxTipoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBotonesLayout.createSequentialGroup()
                        .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnNuevo)
                                .addComponent(btnGuardar)
                                .addComponent(btnEliminar))
                            .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbxTipoImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbxTamanoImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBotonesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSerie, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnFactura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bntActivarPos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pnlDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblDetalleFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Codigo", "Descripcion", "Cantidad", "P. Compra", "P. Venta", "P. Minimo", "P. Normal", "Total", "Comentario", "Arancel"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, false, true, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDetalleFactura.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblDetalleFactura.setFocusable(false);
        tblDetalleFactura.setGridColor(new java.awt.Color(102, 102, 102));
        tblDetalleFactura.getTableHeader().setReorderingAllowed(false);
        tblDetalleFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetalleFacturaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDetalleFactura);
        tblDetalleFactura.getColumnModel().getColumn(0).setResizable(false);
        tblDetalleFactura.getColumnModel().getColumn(0).setPreferredWidth(10);
        tblDetalleFactura.getColumnModel().getColumn(1).setResizable(false);
        tblDetalleFactura.getColumnModel().getColumn(1).setPreferredWidth(20);
        tblDetalleFactura.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblDetalleFactura.getColumnModel().getColumn(3).setResizable(false);
        tblDetalleFactura.getColumnModel().getColumn(3).setPreferredWidth(20);
        tblDetalleFactura.getColumnModel().getColumn(4).setResizable(false);
        tblDetalleFactura.getColumnModel().getColumn(4).setPreferredWidth(20);
        tblDetalleFactura.getColumnModel().getColumn(5).setResizable(false);
        tblDetalleFactura.getColumnModel().getColumn(5).setPreferredWidth(30);
        tblDetalleFactura.getColumnModel().getColumn(6).setResizable(false);
        tblDetalleFactura.getColumnModel().getColumn(6).setPreferredWidth(20);
        tblDetalleFactura.getColumnModel().getColumn(7).setResizable(false);
        tblDetalleFactura.getColumnModel().getColumn(7).setPreferredWidth(20);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBusqueda.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblBuscar.setText("Buscar");

        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonBuscar.png"))); // NOI18N
        btnBuscarProducto.setActionCommand("buscar");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        txtBuscar.setToolTipText("Ingresa los datos de busqueda");
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarkeyPressedEnter(evt);
            }
        });

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonAgregar.png"))); // NOI18N
        btnAgregar.setActionCommand("btnAgregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        jLabel4.setText("Normal");

        jLabel5.setText("Minimo");

        txtPrecioVenta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("######0.###"))));
        txtPrecioVenta.setToolTipText("Aqui puede modificar el precio siempre y cuando coincida con el rango permitido.");
        txtPrecioVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioVentaReleased(evt);
            }
        });

        txtPrecioMinimo.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecioMinimo.setEditable(false);
        txtPrecioMinimo.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecioMinimo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPrecioMinimo.setToolTipText("Muestra el Precio mas bajo al qu se puede vender.");

        jLabel6.setText("Producto");

        txtIdProducto.setBackground(new java.awt.Color(102, 102, 102));
        txtIdProducto.setEditable(false);
        txtIdProducto.setForeground(new java.awt.Color(255, 255, 255));
        txtIdProducto.setToolTipText("Muestra el ID del producto seleccionado para la Venta.");

        txtDescripcionProducto.setBackground(new java.awt.Color(102, 102, 102));
        txtDescripcionProducto.setEditable(false);
        txtDescripcionProducto.setForeground(new java.awt.Color(255, 255, 255));
        txtDescripcionProducto.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDescripcionProducto.setToolTipText("Muestra la Descripcion del Producto seleccionado para la Venta.");

        jLabel7.setText("Precio");

        txtPrecioNormal.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecioNormal.setEditable(false);
        txtPrecioNormal.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecioNormal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPrecioNormal.setToolTipText("Muestra el Precio Normal del Producto");

        jLabel8.setText("Cantidad");

        txtCantidadVenta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtCantidadVenta.setToolTipText("Ingresa la cantidad a vender, siempre y cuando tenga existencia disponible.");

        txtPrecioDescuento.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecioDescuento.setEditable(false);
        txtPrecioDescuento.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecioDescuento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPrecioDescuento.setToolTipText("Precio correspondiente segun descuento ingresado arrriba.");

        jLabel10.setText("Descuento");

        cbxDescuento.setMaximumRowCount(4);
        cbxDescuento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxDescuentoChange(evt);
            }
        });

        txtFilaActual.setBackground(new java.awt.Color(102, 102, 102));
        txtFilaActual.setEditable(false);
        txtFilaActual.setForeground(new java.awt.Color(255, 255, 255));
        txtFilaActual.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtFilaActual.setToolTipText("Fila que esta siendo modificada en este momento.");

        txtCantidadMinima.setBackground(new java.awt.Color(102, 102, 102));
        txtCantidadMinima.setEditable(false);
        txtCantidadMinima.setForeground(new java.awt.Color(255, 255, 255));
        txtCantidadMinima.setToolTipText("Indica con un color Rojo si este Producto llego a la Cantidad Minima.");

        btnMostrarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMostrarImagenes.png"))); // NOI18N
        btnMostrarImagen.setActionCommand("btnAgregar");
        btnMostrarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarImagenActionPerformed(evt);
            }
        });

        txtPrecioCompra.setBackground(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setEditable(false);
        txtPrecioCompra.setForeground(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPrecioCompra.setToolTipText("Muestra el Precio mas bajo al qu se puede vender.");
        txtPrecioCompra.setCaretColor(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setDisabledTextColor(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setSelectedTextColor(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setSelectionColor(new java.awt.Color(0, 102, 153));

        txtPermiteDescuento.setBackground(new java.awt.Color(102, 102, 102));
        txtPermiteDescuento.setEditable(false);
        txtPermiteDescuento.setForeground(new java.awt.Color(255, 255, 255));
        txtPermiteDescuento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPermiteDescuento.setToolTipText("Muestra el Precio mas bajo al qu se puede vender.");

        btnAutorizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonAutorizar.png"))); // NOI18N
        btnAutorizacion.setActionCommand("btnAgregar");
        btnAutorizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAutorizacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBusquedaLayout = new javax.swing.GroupLayout(pnlBusqueda);
        pnlBusqueda.setLayout(pnlBusquedaLayout);
        pnlBusquedaLayout.setHorizontalGroup(
            pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBusquedaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBusquedaLayout.createSequentialGroup()
                        .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescripcionProducto))
                    .addComponent(txtBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarProducto)
                .addGap(18, 18, 18)
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecioNormal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBusquedaLayout.createSequentialGroup()
                        .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPermiteDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtPrecioMinimo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecioCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioDescuento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCantidadMinima, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxDescuento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFilaActual, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMostrarImagen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAutorizacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregar)
                .addGap(6, 6, 6))
        );
        pnlBusquedaLayout.setVerticalGroup(
            pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBusquedaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlBusquedaLayout.createSequentialGroup()
                        .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlBusquedaLayout.createSequentialGroup()
                                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(txtPrecioMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPrecioNormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10)
                                        .addComponent(cbxDescuento)
                                        .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5))
                                    .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblBuscar)
                                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6))
                                    .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8)
                                        .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPrecioDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtCantidadMinima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPermiteDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtFilaActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(btnBuscarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnMostrarImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAutorizacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        pnlDetalleProducto.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblProducto.getTableHeader().setReorderingAllowed(false);
        tblProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblProducto);

        javax.swing.GroupLayout pnlDetalleProductoLayout = new javax.swing.GroupLayout(pnlDetalleProducto);
        pnlDetalleProducto.setLayout(pnlDetalleProductoLayout);
        pnlDetalleProductoLayout.setHorizontalGroup(
            pnlDetalleProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        pnlDetalleProductoLayout.setVerticalGroup(
            pnlDetalleProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlDatosCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "CLIENTE", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));

        lblNitPersona.setText("NIT");

        txtNitPersona.setBackground(new java.awt.Color(255, 255, 204));
        txtNitPersona.setToolTipText("NIT del Cliente, si se deja en blanco el sistema usa C/F.");
        txtNitPersona.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNitPersonaFocusLost(evt);
            }
        });
        txtNitPersona.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNitKeyPressed(evt);
            }
        });

        lblTelCliente.setText("Telefono");

        txtTelCliente.setBackground(new java.awt.Color(102, 102, 102));
        txtTelCliente.setEditable(false);
        txtTelCliente.setForeground(new java.awt.Color(255, 255, 255));
        txtTelCliente.setToolTipText("Telefono del cliente almacenado en la Base de Datos.");

        lblNombrePersona.setText("Nombre");

        txtNombrePersona.setBackground(new java.awt.Color(255, 255, 204));
        txtNombrePersona.setToolTipText("Nombre del cliente, este puede ser modificado.");

        txtDirCliente.setBackground(new java.awt.Color(102, 102, 102));
        txtDirCliente.setEditable(false);
        txtDirCliente.setForeground(new java.awt.Color(255, 255, 255));
        txtDirCliente.setToolTipText("Direccion del Cliente almacenada en la Base de Datos.");

        jLabel13.setText("Direccion");

        txtDirEnvioFactura.setToolTipText("Ingresa la direccion de entrega de producto a domicilio.");

        lblDirEnvioFactura.setText("Enviar a");

        lblDiasCredito.setText("Dias Credito");

        cbxDiasCredito.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0" }));

        lblTipoCliente.setText("Tipo");

        cbxTipoDeCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cliente Normal", "Cliente Mayorista" }));
        cbxTipoDeCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTipoDeClienteActionPerformed(evt);
            }
        });

        txtMetodoEntrada.setBackground(new java.awt.Color(102, 102, 102));
        txtMetodoEntrada.setEditable(false);
        txtMetodoEntrada.setForeground(new java.awt.Color(255, 255, 255));
        txtMetodoEntrada.setToolTipText("Muestra si el sistema esta configurado para usar Lector de Barras o esta en el modo Normal.");

        lblTotalFactura2.setText("Modo");

        txtNitSinGuion.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout pnlDatosClienteLayout = new javax.swing.GroupLayout(pnlDatosCliente);
        pnlDatosCliente.setLayout(pnlDatosClienteLayout);
        pnlDatosClienteLayout.setHorizontalGroup(
            pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDatosClienteLayout.createSequentialGroup()
                        .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNitPersona)
                            .addComponent(lblTelCliente)
                            .addComponent(lblTotalFactura2)
                            .addComponent(jLabel13)
                            .addComponent(lblDirEnvioFactura))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDirCliente)
                            .addGroup(pnlDatosClienteLayout.createSequentialGroup()
                                .addComponent(txtNitPersona, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblDiasCredito)
                                .addGap(18, 18, 18)
                                .addComponent(cbxDiasCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNombrePersona)
                            .addGroup(pnlDatosClienteLayout.createSequentialGroup()
                                .addComponent(txtTelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblTipoCliente)
                                .addGap(18, 18, 18)
                                .addComponent(cbxTipoDeCliente, 0, 239, Short.MAX_VALUE))
                            .addComponent(txtDirEnvioFactura, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlDatosClienteLayout.createSequentialGroup()
                                .addComponent(txtMetodoEntrada)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNitSinGuion, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlDatosClienteLayout.createSequentialGroup()
                        .addComponent(lblNombrePersona)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlDatosClienteLayout.setVerticalGroup(
            pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNitPersona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNitPersona)
                    .addComponent(lblDiasCredito)
                    .addComponent(cbxDiasCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombrePersona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombrePersona))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelCliente)
                    .addComponent(cbxTipoDeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTipoCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDirCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDirEnvioFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDirEnvioFactura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMetodoEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalFactura2)
                    .addComponent(txtNitSinGuion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlVenta.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "VENTA", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));

        jLabel14.setText("ID Empleado");

        txtIdEmpleado.setBackground(new java.awt.Color(102, 102, 102));
        txtIdEmpleado.setEditable(false);
        txtIdEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        txtIdEmpleado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIdEmpleado.setToolTipText("Muestra el codigo del empmleado.");

        cbxEmpleado.setToolTipText("Selecciona el Empleado que hizo la venta");
        cbxEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxEmpleadoActionPerformed(evt);
            }
        });

        jLabel12.setText("Tipo Venta");

        cbxTipoVenta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Contado", "Credito", "Credito / Contado", "Tarjeta" }));
        cbxTipoVenta.setToolTipText("Selecciona como cancelara el Cliente");
        cbxTipoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTipoVentaActionPerformed(evt);
            }
        });

        lblEstado.setText("Estado");

        cbxEstadoVenta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pendiente" }));

        jLabel9.setText("Credito");

        txtPagoCredito.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPagoCredito.setToolTipText("Ingresa la cantidad pendiente de pago.");
        txtPagoCredito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPagoCreditoKeyReleased(evt);
            }
        });

        lblContado.setText("Contado");

        txtCambio.setBackground(new java.awt.Color(255, 51, 0));
        txtCambio.setEditable(false);
        txtCambio.setForeground(new java.awt.Color(255, 255, 0));
        txtCambio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCambio.setToolTipText("Muestra cuanto debe de entregar de cambio segun lo ingresado en Pago al Contado.");

        lblSerieFactura1.setText("Cambio");

        txtPOS.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPOS.setToolTipText("Aqui debe de ingresar el ticket generado por el POS.");

        jLabel15.setText("POS");

        jLabel2.setText("Comision");

        txtComision.setBackground(new java.awt.Color(102, 102, 102));
        txtComision.setEditable(false);
        txtComision.setForeground(new java.awt.Color(255, 255, 255));
        txtComision.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComision.setToolTipText("Comision ganada por Empleado.");

        txtPagoContado.setBackground(new java.awt.Color(255, 255, 204));
        txtPagoContado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPagoContado.setToolTipText("Ingresa la cantidad que se pago al contado.");
        txtPagoContado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPagoKeyReleased(evt);
            }
        });

        lblIVA.setText("IVA");

        txtIva.setBackground(new java.awt.Color(102, 102, 102));
        txtIva.setEditable(false);
        txtIva.setForeground(new java.awt.Color(255, 255, 255));
        txtIva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtContado.setBackground(new java.awt.Color(0, 102, 153));
        txtContado.setForeground(new java.awt.Color(255, 255, 255));
        txtContado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtContado.setToolTipText("Cantidad pagada al contado.");
        txtContado.setCaretColor(new java.awt.Color(240, 240, 240));
        txtContado.setDisabledTextColor(new java.awt.Color(240, 240, 240));
        txtContado.setSelectedTextColor(new java.awt.Color(240, 240, 240));
        txtContado.setSelectionColor(new java.awt.Color(240, 240, 240));

        txtCredito.setBackground(new java.awt.Color(0, 102, 153));
        txtCredito.setForeground(new java.awt.Color(255, 255, 255));
        txtCredito.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCredito.setToolTipText("Comision ganada por Empleado.");
        txtCredito.setCaretColor(new java.awt.Color(240, 240, 240));
        txtCredito.setDisabledTextColor(new java.awt.Color(240, 240, 240));
        txtCredito.setSelectedTextColor(new java.awt.Color(240, 240, 240));
        txtCredito.setSelectionColor(new java.awt.Color(240, 240, 240));

        javax.swing.GroupLayout pnlVentaLayout = new javax.swing.GroupLayout(pnlVenta);
        pnlVenta.setLayout(pnlVentaLayout);
        pnlVentaLayout.setHorizontalGroup(
            pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel12)
                    .addComponent(jLabel9)
                    .addComponent(lblSerieFactura1)
                    .addComponent(lblContado)
                    .addComponent(lblEstado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVentaLayout.createSequentialGroup()
                        .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlVentaLayout.createSequentialGroup()
                                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPagoCredito, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(txtCambio)
                                    .addComponent(txtPagoContado)
                                    .addComponent(cbxEstadoVenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnlVentaLayout.createSequentialGroup()
                                        .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblIVA)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel15))
                                        .addGap(24, 24, 24))
                                    .addGroup(pnlVentaLayout.createSequentialGroup()
                                        .addComponent(txtCredito, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                            .addGroup(pnlVentaLayout.createSequentialGroup()
                                .addComponent(cbxTipoVenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtComision, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(txtIdEmpleado)
                            .addComponent(txtIva)
                            .addComponent(txtContado)
                            .addComponent(txtPOS, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(cbxEmpleado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlVentaLayout.setVerticalGroup(
            pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cbxEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxTipoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIVA)
                    .addComponent(lblEstado)
                    .addComponent(cbxEstadoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPagoCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtComision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(lblContado)
                    .addComponent(txtPagoContado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCambio)
                    .addComponent(lblSerieFactura1)
                    .addComponent(txtContado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnlFactura.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "FACTURA", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));

        lblIdFactura.setText("ID Factura");

        txtIdFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtIdFactura.setEditable(false);
        txtIdFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtIdFactura.setToolTipText("Codigo automatico generado por el sistema");

        lblFecha.setText("Fecha");

        txtFechaEmisionFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtFechaEmisionFactura.setEditable(false);
        txtFechaEmisionFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtFechaEmisionFactura.setToolTipText("Fecha de venta formato YYYY-MM-DD.");

        lblNumFactura.setText("Impresa");

        txtNumFacturaFEL.setBackground(new java.awt.Color(102, 102, 102));
        txtNumFacturaFEL.setEditable(false);
        txtNumFacturaFEL.setForeground(new java.awt.Color(255, 255, 255));
        txtNumFacturaFEL.setText("0");
        txtNumFacturaFEL.setToolTipText("Correlativo de factura segun serie configurada en el sistema.");

        lblSerieFactura.setText("Electronica");

        txtSerieFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtSerieFactura.setEditable(false);
        txtSerieFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtSerieFactura.setText("0");
        txtSerieFactura.setToolTipText("Numero de serie de factura, no es obligatorio excepto en facturacion electronica.");

        jLabel11.setText("Autorizacion");

        txtAutorizacionFEL.setBackground(new java.awt.Color(102, 102, 102));
        txtAutorizacionFEL.setEditable(false);
        txtAutorizacionFEL.setForeground(new java.awt.Color(255, 255, 255));
        txtAutorizacionFEL.setText("0");
        txtAutorizacionFEL.setToolTipText("Este valor unicamente se utiliza para facturacion FEL.");

        txtFolioFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtFolioFactura.setEditable(false);
        txtFolioFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtFolioFactura.setText("0");
        txtFolioFactura.setToolTipText("Numero de folio de las facturas, este dato no es obligatorio.");

        lblFolioFactura.setText("Folio");

        jLabel3.setText("Vale Para");

        txtRecibidoPor.setToolTipText("Se utiliza en caso que se entregue un vale por mercaderia.");

        lblTotalFactura.setText("Total");

        txtTotalFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalFactura.setEditable(false);
        txtTotalFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalFactura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalFactura.setToolTipText("");

        txtSerieFacturaFEL.setBackground(new java.awt.Color(102, 102, 102));
        txtSerieFacturaFEL.setEditable(false);
        txtSerieFacturaFEL.setForeground(new java.awt.Color(255, 255, 255));
        txtSerieFacturaFEL.setText("0");
        txtSerieFacturaFEL.setToolTipText("Serie de factura configurado en el sistema.");

        txtNumFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtNumFactura.setEditable(false);
        txtNumFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtNumFactura.setText("0");
        txtNumFactura.setToolTipText("Numero de serie de factura, no es obligatorio excepto en facturacion electronica.");

        javax.swing.GroupLayout pnlFacturaLayout = new javax.swing.GroupLayout(pnlFactura);
        pnlFactura.setLayout(pnlFacturaLayout);
        pnlFacturaLayout.setHorizontalGroup(
            pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIdFactura)
                    .addComponent(jLabel11)
                    .addComponent(jLabel3)
                    .addComponent(lblFolioFactura)
                    .addComponent(lblNumFactura)
                    .addComponent(lblSerieFactura))
                .addGap(18, 18, 18)
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addComponent(txtFolioFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(lblTotalFactura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotalFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addComponent(txtSerieFacturaFEL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNumFacturaFEL))
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addComponent(txtIdFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblFecha)
                        .addGap(18, 18, 18)
                        .addComponent(txtFechaEmisionFactura))
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addComponent(txtSerieFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNumFactura))
                    .addComponent(txtAutorizacionFEL)
                    .addComponent(txtRecibidoPor))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlFacturaLayout.setVerticalGroup(
            pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdFactura)
                    .addComponent(txtIdFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFecha)
                    .addComponent(txtFechaEmisionFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSerieFactura)
                    .addComponent(txtSerieFacturaFEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumFacturaFEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumFactura)
                    .addComponent(txtNumFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSerieFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAutorizacionFEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFolioFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalFactura)
                    .addComponent(lblFolioFactura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRecibidoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDetalleProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlDatosCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlVenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlFactura, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDatosCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDetalleProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    //Variables globales en la pantalla
    ObjetosFactura Factura = new ObjetosFactura();
    ObjetosProducto Producto = new ObjetosProducto();
    ObjetosDetalleFactura Detalle = new ObjetosDetalleFactura();
    AccesoCliente NuevoCliente = new AccesoCliente();
    AccesoFactura NuevaFactura = new AccesoFactura();
    AccesoPersona NuevaPersona = new AccesoPersona();
    AccesoProducto NuevoProducto = new AccesoProducto();
    AccesoProveedor NuevoProveedor = new AccesoProveedor();
    AccesoDetalleFactura NuevoDetalleFactura = new AccesoDetalleFactura();
    AccesoExcepciones Mensaje = new AccesoExcepciones();
    
    Date fecha = new Date();
    float total_factura = 0;
    float cantidad = 0;
    float precio = 0;
    
    //Variables publicas para uso externo
    public static int fila_seleccionada = 0;
    public int columna_seleccionada = 0;
    
    private void agregarLog(String texto) {
        this.Log = this.Log + "VENTA" + ": " + texto + " \n";
        Inventory.txtLog.setText(Log);
    }
    
    private void limpiarEncabezadoFactura(){
        txtIdFactura.setText("");
        txtFechaEmisionFactura.setText("");
        txtSerieFacturaFEL.setText("");
        txtNumFacturaFEL.setText("");
        txtSerieFactura.setText("");
        txtFolioFactura.setText("");
        txtNitPersona.setText("");
        txtNombrePersona.setText("");
        txtAutorizacionFEL.setText("");
        txtTelCliente.setText("");
        txtDirCliente.setText("");
        txtRecibidoPor.setText("");
        txtIdEmpleado.setText("");
        txtDirEnvioFactura.setText("");
        cbxDiasCredito.setSelectedIndex(0);
        txtIva.setText("");
        txtTotalFactura.setText("");
        cbxEstadoVenta.setSelectedIndex(0);
        cbxTipoVenta.setSelectedIndex(0);
        txtComision.setText("");
        cbxTipoDeCliente.setSelectedIndex(1);
        txtPagoCredito.setText("");
        txtPagoContado.setText("");
        txtPOS.setText("");
        txtCambio.setText("");
    }

    private void limpiarDetalleFactura() {
        String[] columnas = {"No", "Codigo", "Descripcion", "Cantidad", "P. Compra", "P. Estandar", "P. Minimo", "P. Normal", "Total", "Comentario", "Arancel"};
        DefaultTableModel modelo_tabla = new DefaultTableModel(columnas, 200);
        tblDetalleFactura.setModel(modelo_tabla);
        cantidad = 0;
    }

    private void calcularComision() {

        float venta_contado = 0;
        float venta_credito = 0;
        
        try {
            
            if (txtContado.getText().equals("") || txtContado.getText().equals("0")) {
                venta_contado = 0;
            } else {
                venta_contado = Float.parseFloat(txtContado.getText());
            }
            
            if (txtCredito.getText().equals("") || txtCredito.getText().equals("0")) {
                venta_credito = 0;
            } else {
                venta_credito = Float.parseFloat(txtCredito.getText());
            }
            
            float venta_bruta = venta_contado + venta_credito;
            float iva = Float.parseFloat("0.12");
            float iva_por_pagar = venta_bruta * iva;
            float venta_neta = venta_bruta - iva_por_pagar;
            float total_comision = 0;

            //Consultar el porcentaje de comision del empleado por medio del ID este
            float comision = NuevaFactura.retornaComisionEmpleado(Integer.parseInt(txtIdEmpleado.getText()), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

            //Al monto contado le descuento el iva
            total_comision = (venta_contado - (venta_contado * iva)) * (comision / 100);

            //Calculo la comision sobre la venta al contado en base al porcentaje obtenido antes
            txtComision.setText(String.valueOf(matematica.redondear(total_comision)));
            
        } catch (Exception Error) {
            agregarLog("Calcular Comision. \n" + Error.toString());
        }
        
    }
    
    private void limpiarBusqueda(){
        try {
            agregarLog("Limpiando todos los campos del Producto.");
            txtBuscar.setText("");
            txtPrecioNormal.setText("");
            txtPrecioMinimo.setText("");
            txtIdProducto.setText("");
            txtDescripcionProducto.setText("");
            txtPrecioVenta.setText("");
            txtCantidadVenta.setText("");
            txtPrecioDescuento.setText("");
            txtCantidadMinima.setText("");
            txtPrecioCompra.setText("");
        } catch (Exception Error) {
            Mensaje.manipulacionExcepciones("critico", "Error al limpiar los campos para agregar Productos.", "Limpiar Busqueda");
            agregarLog("Error al limpiar campos: " + Error.toString());
        }
    }

    private void calcularVuelto() {
        
        double v_contado = 0;
        double v_credito = 0;
        double v_total_pagado = 0;
        double v_vuelto = 0;
        
        try {
            
            double v_total_factura = Double.parseDouble(txtTotalFactura.getText());
            
            if (!txtContado.getText().equals("") && !txtContado.getText().equals("0")) {
                v_contado = Double.parseDouble(txtContado.getText());
            }
            
            if (!txtCredito.getText().equals("") && !txtCredito.getText().equals("0")) {
                v_credito = Double.parseDouble(txtCredito.getText());
            }
            
            v_total_pagado = v_contado + v_credito;
            
            if (v_total_pagado > v_total_factura) {
                v_vuelto = v_total_pagado - v_total_factura;
                txtCambio.setText(String.valueOf(v_vuelto));
            } else {
                txtCambio.setText("0");
            }
            
        } catch (Exception Error) {
            agregarLog("Cacular Vuelto. \n" + Error.toString());
        }
        
    }
    
    private void mostrarRegistrosGuardados(){
        
        ArrayList<ObjetosProducto> listaProductos = new ArrayList();
        ArrayList<ObjetosProveedor> listaProveedores = new ArrayList();
        ArrayList<ObjetosPersona> listaPersonas = new ArrayList();
        
        DefaultTableModel tabla = new DefaultTableModel();
        
        try{
            listaProductos = NuevoProducto.seleccionarProductoVisible(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            listaProveedores = NuevoProveedor.seleccionarProveedor(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            listaPersonas = NuevaPersona.seleccionarPersona(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Error error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Buscar Persona");
        }

        if(listaProductos.isEmpty()){
            Mensaje.manipulacionExcepciones("critico", "No existen datos almacenados de productos.", "Mostrar Registros Guardados");
        } else {
            tabla.addColumn("ID Producto");
            tabla.addColumn("Marca");
            tabla.addColumn("Descripcion");
            tabla.addColumn("Familia");
            tabla.addColumn("Proveedor");
            tabla.addColumn("Existencias");
            tabla.addColumn("Medida");
            tabla.addColumn("P. Compra");
            tabla.addColumn("P. Estandar");
            tabla.addColumn("P. Minimo");
            tabla.addColumn("Codigo Fabrica");
            tabla.addColumn("Minimo");
            tabla.addColumn("Ubicacion");
            tabla.addColumn("Descuento");
            
            tabla.setRowCount(listaProductos.size());
            int cProducto = 0;
            
            //Recorrer la llista de productos consultaados
            for(ObjetosProducto xProducto:listaProductos ){
                
                //Consultar cada productos por la lista de proveedores
                for(ObjetosProveedor xProveedor:listaProveedores){
                    
                    //Verificar ID Proveeddor para obtener su descripcion
                    if(xProducto.getId_proveedor() == xProveedor.getId_proveedor()){
                        
                        //Si existe el ID Proveedor buscar su nombre en la tabla personas
                        for(ObjetosPersona xPersona:listaPersonas){
                            
                            //Si los datos coinsiden mostrar toda la informacion en pantalla
                            if(xProveedor.getId_persona() == xPersona.getId_persona()){
                                tabla.setValueAt(xProducto.getId_producto(), cProducto, 0);
                                tabla.setValueAt(xProducto.getMarca_producto(), cProducto, 1);
                                tabla.setValueAt(xProducto.getDesc_producto(), cProducto, 2);
                                tabla.setValueAt(xProducto.getLinea_producto(), cProducto, 3);
                                tabla.setValueAt(xPersona.getNom_persona(), cProducto, 4);
                                tabla.setValueAt(xProducto.getExi_producto(), cProducto, 5);   
                                tabla.setValueAt(xProducto.getUnidad_medida_producto(), cProducto, 6);
                                tabla.setValueAt(xProducto.getPrecio_compra_producto(), cProducto, 7);
                                tabla.setValueAt(xProducto.getPrecio_est_producto(), cProducto, 8);
                                tabla.setValueAt(xProducto.getPrecio_min_producto(), cProducto, 9);
                                tabla.setValueAt(xProducto.getCodigo_fabricante(), cProducto, 10);
                                tabla.setValueAt(xProducto.getMinimo_producto(), cProducto, 11);
                                tabla.setValueAt(xProducto.getUbicacion_producto(), cProducto, 12);
                                tabla.setValueAt(xProducto.getDescuento_producto(), cProducto, 13);
                                cProducto++;
                            }
                        }
                    }
                }
            }
            
            tblProducto.setModel(tabla);
            tblProducto.getColumnModel().getColumn(2).setPreferredWidth(300);
            
        }
    }
    
    private void buscarEnRegistrosGuardados(){
        
        String busqueda = txtBuscar.getText().toUpperCase().replaceAll("'", "´");
        
        if(busqueda != null){
            
            DefaultTableModel tabla = new DefaultTableModel();
            ArrayList<ObjetosProducto> listaProductos = new ArrayList();
            ArrayList<ObjetosProveedor> listaProveedores = new ArrayList();
            ArrayList<ObjetosPersona> listaPersonas = new ArrayList();

            try{
                listaProductos = NuevoProducto.seleccionarProductoVisible(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                listaProveedores = NuevoProveedor.seleccionarProveedor(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                listaPersonas = NuevaPersona.seleccionarPersona(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            }catch(Error error){
                Mensaje.manipulacionExcepciones("critico",  error.getMessage(), "Buscar en Registros Guardados");
            }

            if(listaProductos.isEmpty()){
                Mensaje.manipulacionExcepciones("critico", "No existen datos almacenados de productos.", "Lista de Registros Vacia");
            } else {
                
                tabla.addColumn("ID Producto");
                tabla.addColumn("Marca");
                tabla.addColumn("Descripcion");
                tabla.addColumn("Familia");
                tabla.addColumn("Proveedor");
                tabla.addColumn("Existencias");
                tabla.addColumn("Medida");
                tabla.addColumn("P. Compra");
                tabla.addColumn("P. Estandar");
                tabla.addColumn("P. Minimo");
                tabla.addColumn("Codigo Fabrica");
                tabla.addColumn("Minimo");
                tabla.addColumn("Ubicacion");
                tabla.addColumn("Descuento");

                int cProducto = 0;
                int contadorFilas = 1;

                for (ObjetosProducto xProducto : listaProductos) {
                    for (ObjetosProveedor xProveedor : listaProveedores) {
                        if (xProducto.getId_proveedor() == xProveedor.getId_proveedor()) {
                            for (ObjetosPersona xPersona : listaPersonas) {
                                if (xProveedor.getId_persona() == xPersona.getId_persona()) {
                                   
                                    //busca conincidencia entre varia columnas y muestro tadas ellas
                                    //en la tabla del listado del producto de la parte inferior
                                    String[] tokens = busqueda.split("%");
                                    int cuenta_coincidencias = tokens.length;
                                    cuenta_coincidencias = 0;
                                    
                                    for(int x = 0; x < tokens.length; x++) {
                                        
                                        if (xProducto.getId_producto().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xProducto.getMarca_producto().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xProducto.getDesc_producto().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xPersona.getNom_persona().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xProducto.getLinea_producto().toUpperCase().indexOf(tokens[x]) >= 0
                                            || xProducto.getCodigo_fabricante().toUpperCase().indexOf(tokens[x]) >= 0) {

                                            cuenta_coincidencias++;

                                        } //fin de la validacion de la palabra

                                    } //fin del while que recorre los tokens
                                    
                                    if (cuenta_coincidencias == tokens.length) {
                                        tabla.setRowCount(contadorFilas);
                                        tabla.setValueAt(xProducto.getId_producto(), cProducto, 0);
                                        tabla.setValueAt(xProducto.getMarca_producto(), cProducto, 1);
                                        tabla.setValueAt(xProducto.getDesc_producto(), cProducto, 2);
                                        tabla.setValueAt(xProducto.getLinea_producto(), cProducto, 3);
                                        tabla.setValueAt(xPersona.getNom_persona(), cProducto, 4);
                                        tabla.setValueAt(xProducto.getExi_producto(), cProducto, 5);
                                        tabla.setValueAt(xProducto.getUnidad_medida_producto(), cProducto, 6);
                                        tabla.setValueAt(xProducto.getPrecio_compra_producto(), cProducto, 7);
                                        tabla.setValueAt(xProducto.getPrecio_est_producto(), cProducto, 8);
                                        tabla.setValueAt(xProducto.getPrecio_min_producto(), cProducto, 9);
                                        tabla.setValueAt(xProducto.getCodigo_fabricante(), cProducto, 10);
                                        tabla.setValueAt(xProducto.getMinimo_producto(), cProducto, 11);
                                        tabla.setValueAt(xProducto.getUbicacion_producto(), cProducto, 12);
                                        tabla.setValueAt(xProducto.getDescuento_producto(), cProducto, 13);
                                        cProducto++;
                                        contadorFilas++;
                                    }
                                    
                                }
                            }
                        }
                    }
                }
                
                tblProducto.setModel(tabla);
                tblProducto.getColumnModel().getColumn(2).setPreferredWidth(300);
                
            }
        } else {
            mostrarRegistrosGuardados();
        } 
    }
    
    
    private void buscarCodigoDeBarras(String codigo) {
        
        ObjetosProducto producto = new ObjetosProducto();
        AccesoProducto acceso = new AccesoProducto();
        
        try {
            
            producto = acceso.buscarInformacionProducto(codigo, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            txtDescripcionProducto.setText(producto.getDesc_producto());
            txtIdProducto.setText(producto.getId_producto());
            txtPrecioNormal.setText(String.valueOf(producto.getPrecio_est_producto()));
            txtPrecioMinimo.setText(String.valueOf(producto.getPrecio_min_producto()));
            txtPrecioVenta.setText(String.valueOf(producto.getPrecio_est_producto()));
            txtCantidadVenta.setText("1");
            txtPrecioDescuento.setText(String.valueOf(producto.getPrecio_est_producto()));
            txtPrecioCompra.setText(String.valueOf(producto.getPrecio_compra_producto()));
            
            //validar si el producto ha llegado a la cantidad minima
            if (producto.getExi_producto() <= producto.getMinimo_producto()) {
                txtCantidadMinima.setText("Minimo");
                txtCantidadMinima.setBackground(Color.RED);
            } else {
                txtCantidadMinima.setText("");
                txtCantidadMinima.setBackground(Color.DARK_GRAY);
            }
            
        } catch(Exception error) {
            Mensaje.manipulacionExcepciones("Informacion", "Existe un error con este codigo de producto.", "Buscar Codigo de Barras");
        }
        
        
        //enviar datos al detalle de la pantalla de ventas con los datos del producto
        try {
            if (txtIdProducto.getText().equals("")) {
                Mensaje.manipulacionExcepciones("critico", "No existe este codigo de Producto.", "Buscar Codigo de Barras");
            } else {
                tblDetalleFactura.setValueAt(fila_seleccionada + 1, fila_seleccionada, 0);
                tblDetalleFactura.setValueAt(txtIdProducto.getText(), fila_seleccionada, 1);
                tblDetalleFactura.setValueAt(txtDescripcionProducto.getText(), fila_seleccionada, 2);
                tblDetalleFactura.setValueAt(Float.parseFloat(txtCantidadVenta.getText()), fila_seleccionada, 3);
                tblDetalleFactura.setValueAt(Float.parseFloat(txtPrecioCompra.getText()), fila_seleccionada, 4);
                tblDetalleFactura.setValueAt(Float.parseFloat(txtPrecioDescuento.getText()), fila_seleccionada, 5);
                tblDetalleFactura.setValueAt(Float.parseFloat(txtPrecioMinimo.getText()), fila_seleccionada, 6);
                tblDetalleFactura.setValueAt(Float.parseFloat(txtPrecioNormal.getText()), fila_seleccionada, 7);
                tblDetalleFactura.setValueAt(matematica.aproxima(Double.parseDouble(txtCantidadVenta.getText()) * Double.parseDouble(txtPrecioDescuento.getText()), 2), fila_seleccionada, 8);
                tblDetalleFactura.setValueAt("S/C", fila_seleccionada, 9);
                tblDetalleFactura.setValueAt(0.0, fila_seleccionada, 10);

                // Si el producto existe entonces incrementamos el valor de fila actual en pantalla
                fila_seleccionada++;
                txtFilaActual.setText(String.valueOf(fila_seleccionada));
            }
        } catch (Exception error) {
            Mensaje.manipulacionExcepciones("Informacion",  error.getMessage(), "Incrementar Valor de Fila");
        }

    }
    
    private void descontarProducto(String pId_producto, float pCantidad){
        ArrayList<ObjetosProducto> BuscarProducto = new ArrayList(); 
        try{
            BuscarProducto = NuevoProducto.buscarProducto(pId_producto, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            if(BuscarProducto.isEmpty()){
                Mensaje.manipulacionExcepciones("critico", "No existe el Producto "+pId_producto+" en la Base de Datos", "Descontar Producto");
            } else {
                if(BuscarProducto.size()>1){
                    Mensaje.manipulacionExcepciones("critico", "Existe mas de un Producto con este ID " + pId_producto, "Descontar Producto");
                } else {
                    NuevoProducto.actualizarExistencia(pId_producto, BuscarProducto.get(0).getExi_producto() - pCantidad, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                }
            }
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Actualizar Existencia " + pId_producto);
        }
    }
    
    private ObjetosProducto obtenerDatosProducto(String pId_producto){
        
        ArrayList<ObjetosProducto> BuscarProducto = new ArrayList();
        ObjetosProducto Producto = new ObjetosProducto();
        
        //Buscar el producto
        try{
            BuscarProducto = NuevoProducto.buscarProducto(pId_producto, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Buscar Producto");
        }
        
        //Validar el resultado de la busqueda
        if(BuscarProducto.isEmpty()){
            Mensaje.manipulacionExcepciones("informacion", "Este Codigo de Producto no existe en su Base de Datos.", "Obtener Datos Producto");
        } else {
            if(BuscarProducto.size() > 1){
                Mensaje.manipulacionExcepciones("critico", "Existe mas de un producto con este codigo.", "Obtener Datos Producto");
            } else {
                Producto.setId_producto(BuscarProducto.get(0).getId_producto());
                Producto.setId_proveedor(BuscarProducto.get(0).getId_proveedor());
                Producto.setMarca_producto(BuscarProducto.get(0).getMarca_producto());
                Producto.setDesc_producto(BuscarProducto.get(0).getDesc_producto());
                Producto.setPrecio_compra_producto(BuscarProducto.get(0).getPrecio_compra_producto());
                Producto.setPrecio_est_producto(BuscarProducto.get(0).getPrecio_est_producto());
                Producto.setExi_producto(BuscarProducto.get(0).getExi_producto());
                Producto.setUnidad_medida_producto(BuscarProducto.get(0).getUnidad_medida_producto());
                Producto.setMaximo_producto(BuscarProducto.get(0).getMaximo_producto());
                Producto.setMinimo_producto(BuscarProducto.get(0).getMinimo_producto());
                Producto.setPrj_est_producto(BuscarProducto.get(0).getPrj_est_producto());
                Producto.setPrj_min_producto(BuscarProducto.get(0).getPrj_min_producto());
                Producto.setPrecio_min_producto(BuscarProducto.get(0).getPrecio_min_producto());
                Producto.setCodigo_fabricante(BuscarProducto.get(0).getCodigo_fabricante());
                Producto.setUbicacion_producto(BuscarProducto.get(0).getUbicacion_producto());
            }
        }
        
        return Producto;
    }
    
    private void obtenerDatosCliente(String pNit_persona){
        
        ArrayList<ObjetosPersona> BuscarPersona = new ArrayList();
        ArrayList<ObjetosCliente> BuscarCliente = new ArrayList();
        
        boolean persona_encontrada = false;
        
        //Consultar NIT de la persona
        try{
            BuscarPersona = NuevaPersona.buscarPersona(pNit_persona, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Buscar Persona");
        }
        
        //Validar el resultado de la busqueda
        if(BuscarPersona.isEmpty()){
            Mensaje.manipulacionExcepciones("critico", "No existen datos de esta Persona en la Base de Datos.", "Obtener Datos Cliente");
            persona_encontrada = false;
        } else {
            if(BuscarPersona.size() > 1){
                Mensaje.manipulacionExcepciones("critico", "Existe mas de una persona con este numero de NIT.", "Obtener Datos Cliente");
                persona_encontrada = false;
            } else {
                txtNombrePersona.setText(BuscarPersona.get(0).getNom_persona());
                persona_encontrada = true;
            }
        }
        
        //Consultar la informacion del cliente con el ID Persona encontrado
        try{
            if(persona_encontrada == true) {
                BuscarCliente = NuevoCliente.buscarCliente(BuscarPersona.get(0).getId_persona(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            }
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Buscar Cliente");
        }
        
        if (BuscarCliente.isEmpty()) {
            
            Mensaje.manipulacionExcepciones("interrogante", "Desea crear este cliente en la Base de Datos.", "Buscar Datos Cliente");
            
            if (Mensaje.valor == 0) {
                wdwCatalogoClientes CatalogoClientes = new wdwCatalogoClientes();
                ventana.abrirPantalla(CatalogoClientes);
                CatalogoClientes.txtNitPersona.setText(txtNitPersona.getText());
                Mensaje.valor = 2;
            } else {
                txtNitPersona.setText("C/F");
                txtNitPersona.requestFocus();
            }
            
        } else {
            
            if (BuscarCliente.size() > 1) {
                Mensaje.manipulacionExcepciones("critico", "Se encontro mas de un Cliente con este numero de NIT.", "Buscar Datos Cliente");
            } else {
                txtTelCliente.setText(String.valueOf(BuscarCliente.get(0).getTel_cliente()));
                txtDirCliente.setText(BuscarCliente.get(0).getDir_cliente());
                txtFechaEmisionFactura.setText((1900 + fecha.getYear()) + "-" + (fecha.getMonth() + 1) + "-" + fecha.getDate());
                String fecha_string = (1900 + fecha.getYear()) + "-" + (fecha.getMonth() + 1) + "-" + fecha.getDate();
                txtTotalFactura.setText("0.00");
                txtDirEnvioFactura.requestFocus();
                
                //Modificar la lista de dias credito segun lo indicado
                //en el maestro de clientes, mostrar de la lista todos
                //los valores menores a los dias credito de dicho 
                int lista_dias_credito[] = {0,15,30,90,180,365};
                int maximo_dias_creidto = BuscarCliente.get(0).getDias_de_credito();
                cbxDiasCredito.removeAllItems();
                for(int i=0; i<6; i++){
                    if(lista_dias_credito[i] <= maximo_dias_creidto ) {
                        cbxDiasCredito.addItem(lista_dias_credito[i]);
                    }
                }
                if(maximo_dias_creidto > 0) {
                    cbxDiasCredito.setEnabled(true);
                    cbxTipoVenta.setEnabled(true);
                } else {
                    cbxDiasCredito.setEnabled(false);
                    txtPagoCredito.setEnabled(false);
                    txtPagoContado.setEnabled(true);
                    cbxTipoVenta.setSelectedItem("Contado");
                    cbxTipoVenta.setEnabled(false);
                }
            }
            
        }
        
        BuscarPersona.removeAll(BuscarCliente);
        BuscarCliente.removeAll(BuscarCliente);
    }
    
    private void cargaTipoVenta(){
        cbxDiasCredito.setSelectedIndex(0);
        cbxDiasCredito.setEnabled(false);
        txtPagoCredito.setEnabled(false);
    }
    
    private void cargaEmpleados(){
        
        ArrayList<ObjetosEmpleado> lista_empleados = new ArrayList();
        AccesoEmpleado acceso_empleados = new AccesoEmpleado();
            
        try{
            lista_empleados = acceso_empleados.listarEmpleadosActivos(Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Retorna Nombre Empleado");
        }
        
        //Lleno el combobox con los registros de empleados que existen
        for(ObjetosEmpleado empleados : lista_empleados){
            cbxEmpleado.addItem(empleados.getNombre_empleado());
        }
        
    }
    
    private int obtenerIdCliente(String pNit_cliente){
        
        ArrayList<ObjetosPersona> BuscarPersona = new ArrayList();
        ArrayList<ObjetosCliente> BuscarCliente = new ArrayList();
        int vId_cliente = 0;
        
        //Consultar el NIT de la persona
        try{
            BuscarPersona = NuevaPersona.buscarPersona(pNit_cliente, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Buscar Persona");
        }
        
        //Validando datos de la persona enocntrada
        if(BuscarPersona.isEmpty()){
            Mensaje.manipulacionExcepciones("critico", "No existe este Cliente en la Base de Datos.", "Carga Empleados");
        } else {
            if(BuscarPersona.size()>1){
                Mensaje.manipulacionExcepciones("critico", "Existen mas personas con este Numero de NIT.", "Carga Empleados");
            }
        }
                
        //Buscar datos del client con el ID Persona
        try{
            BuscarCliente = NuevoCliente.buscarCliente(BuscarPersona.get(0).getId_persona(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("informacion", error.getMessage(), "Buscar Cliente");
        }

        //Validar los resultados de la busqueda
        if (BuscarCliente.isEmpty()) {
            Mensaje.manipulacionExcepciones("critico", "Debe crear este cliente en la base de datos.", "Obtener ID Cliente");
        } else {
            if (BuscarCliente.size() > 1) {
                Mensaje.manipulacionExcepciones("critico", "Este numero de Nit esta repetido.", "Obtener ID Cliente");
            } else {
                vId_cliente = BuscarCliente.get(0).getId_cliente();
            }
        }

        return vId_cliente;
    }

    private boolean verificarDatosFactura(ObjetosFactura pFactura) {

        boolean vDatos_correctos = false;

        //Validar todos los datos obligatorios de la pantalla de ventas
        if ("".equals(txtNitPersona.getText())) {
            Mensaje.manipulacionExcepciones("critico", "Por favor ingrese el numero de NIT.", "Verificar Datos Factura");
        } else {
            if ("".equals(pFactura.getFecha_emision_factura())) {
                Mensaje.manipulacionExcepciones("critico", "Ingrese la fecha de ese envio.", "Verificar Datos Factura");
            } else {
                if ("".equals(txtNumFacturaFEL.getText())) {
                    Mensaje.manipulacionExcepciones("critico", "Por favor ingrese el numero de FACTURA.", "Verificar Datos Factura");
                } else {
                    if ("".equals(txtSerieFactura.getText())) {
                        Mensaje.manipulacionExcepciones("critico", "Por favor ingrese el numero de SERIE.", "Verificar Datos Factura");
                    } else {
                        if ("".equals(txtFolioFactura.getText())) {
                            Mensaje.manipulacionExcepciones("critico", "Por favor ingrese el numero de FOLIO.", "Verificar Datos Factura");
                        } else {
                            if (cbxTipoVenta.getSelectedItem().toString().equals("Tarjeta") && txtPOS.getText().equals("")) {
                                Mensaje.manipulacionExcepciones("critico", "Favor ingrese el numero de comprobante del POS..", "Verificar Datos Factura");
                            } else {
                                if (!txtPOS.getText().isEmpty()) {
                                    if (NuevaFactura.buscarComprobantePOS(txtPOS.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText()).equals("0")) {
                                        vDatos_correctos = true;
                                    } else {
                                        vDatos_correctos = false;
                                        Mensaje.manipulacionExcepciones("critico", "Ya existe este numero de Comprobante para el campo POS.", "Verificar Datos Factura");
                                    }
                                } else {
                                    vDatos_correctos = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return vDatos_correctos;
    }

    private void calcular_descuento() {
        try {
            
            float precio_venta = Float.parseFloat(txtPrecioVenta.getText());
            agregarLog("Calculando el precio con descuento.");
            if (txtPermiteDescuento.getText().equals("SI")) {
                float descuento = 1 - (Float.parseFloat(cbxDescuento.getSelectedItem().toString()) / 100);
                float precio_descuento = precio_venta * descuento;
                this.txtPrecioDescuento.setText(String.valueOf(matematica.aproxima(precio_descuento, 2)));
            } else {
                this.txtPrecioDescuento.setText(String.valueOf(matematica.aproxima(precio_venta, 2)));
            }
            agregarLog("Precio con descuento: " + precio_venta);
        } catch(Exception e) {
            if(!txtPrecioVenta.getText().isEmpty()){
                Mensaje.manipulacionExcepciones("critico", "Ingreso un caracter no valido.", "Calcular Descuento");
                agregarLog("Error: " + e.toString());
            }
        }
    }
    
    private void buscarDocumento() {
        
        AccesoProducto acceso_producto_buscado = new AccesoProducto();
        
        if (cbxTipoBusqueda.getSelectedItem().equals("Factura")) {

            ArrayList<ObjetosDetalleFactura> lista_detalle_factura = new ArrayList<>();
            AccesoDetalleFactura acceso_detalle_factura = new AccesoDetalleFactura();

            int id_factura_buscada = Integer.parseInt(txtIdBusqueda.getText());
            lista_detalle_factura = acceso_detalle_factura.buscarDetalleFactura(id_factura_buscada, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

            if (lista_detalle_factura.isEmpty()) {
                Mensaje.manipulacionExcepciones("informacion", "No existe una Venta con este ID Factura.", "Buscar Documento");
            } else {
                int posicion = 0;
                for (ObjetosDetalleFactura x : lista_detalle_factura) {
                    tblDetalleFactura.setValueAt(posicion + 1, posicion, 0);
                    tblDetalleFactura.setValueAt(x.getId_producto(), posicion, 1);
                    ObjetosProducto producto_buscado = new ObjetosProducto();
                    producto_buscado = acceso_producto_buscado.buscarInformacionProducto(x.getId_producto(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    tblDetalleFactura.setValueAt(producto_buscado.getDesc_producto(), posicion, 2);
                    tblDetalleFactura.setValueAt(x.getCantidad_d_factura(), posicion, 3);
                    tblDetalleFactura.setValueAt(x.getPrecio_compra_d_factura(), posicion, 4);
                    tblDetalleFactura.setValueAt(x.getPrecio_venta_d_factura(), posicion, 5);
                    tblDetalleFactura.setValueAt(producto_buscado.getPrecio_min_producto(), posicion, 6);
                    tblDetalleFactura.setValueAt(producto_buscado.getPrecio_est_producto(), posicion, 7);
                    tblDetalleFactura.setValueAt(matematica.aproxima((x.getCantidad_d_factura() * x.getPrecio_venta_d_factura()), 2), posicion, 8);
                    tblDetalleFactura.setValueAt(x.getComentario_d_factura(), posicion, 9);
                    tblDetalleFactura.setValueAt(x.getArancel_d_factura(), posicion, 10);
                    posicion++;
                }
                Mensaje.manipulacionExcepciones("informacion", "La Venta fue encontrada con exito.", "Buscar Documento");
            }

        }

        if (cbxTipoBusqueda.getSelectedItem().equals("Proforma")) {

            ArrayList<ObjetosDetalleProforma> lista_detalle_proforma = new ArrayList<>();
            AccesoDetalleProforma acceso_detalle_proforma = new AccesoDetalleProforma();

            int id_proforma_buscada = Integer.parseInt(txtIdBusqueda.getText());
            lista_detalle_proforma = acceso_detalle_proforma.buscarDetalleProforma(id_proforma_buscada, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

            if (lista_detalle_proforma.isEmpty()) {
                Mensaje.manipulacionExcepciones("informacion", "No existe una Proforma con este numero.", "Buscar Documento");
            } else {
                int posicion = 0;
                for (ObjetosDetalleProforma x : lista_detalle_proforma) {
                    tblDetalleFactura.setValueAt(posicion + 1, posicion, 0);
                    tblDetalleFactura.setValueAt(x.getId_producto(), posicion, 1);
                    ObjetosProducto producto_buscado = new ObjetosProducto();
                    producto_buscado = acceso_producto_buscado.buscarInformacionProducto(x.getId_producto(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    tblDetalleFactura.setValueAt(producto_buscado.getDesc_producto(), posicion, 2);
                    tblDetalleFactura.setValueAt(x.getCant_d_proforma(), posicion, 3);
                    tblDetalleFactura.setValueAt(producto_buscado.getPrecio_compra_producto(), posicion, 4);
                    tblDetalleFactura.setValueAt(x.getPrecio_venta_d_proforma(), posicion, 5);
                    tblDetalleFactura.setValueAt(producto_buscado.getPrecio_min_producto(), posicion, 6);
                    tblDetalleFactura.setValueAt(producto_buscado.getPrecio_est_producto(), posicion, 7);
                    tblDetalleFactura.setValueAt(matematica.aproxima((x.getCant_d_proforma() * x.getPrecio_venta_d_proforma()), 2), posicion, 8);
                    tblDetalleFactura.setValueAt("PFA" + x.getId_d_proforma(), posicion, 9);
                    tblDetalleFactura.setValueAt(0.0, posicion, 10);
                    posicion++;
                }
                Mensaje.manipulacionExcepciones("informacion", "La Proforma fue encontrada con exito.", "Buscar Documento");
            }

        }

        if (cbxTipoBusqueda.getSelectedItem().equals("Servicio")) {

            ArrayList<ObjetosDetalleOrden> lista_detalle_orden = new ArrayList<>();
            AccesoDetalleOrden acceso_detalle_orden = new AccesoDetalleOrden();

            int id_orden_buscada = Integer.parseInt(txtIdBusqueda.getText());
            lista_detalle_orden = acceso_detalle_orden.listarProductosOrdenFinalizada(id_orden_buscada, Inventory.lblSucursal.getText(), Inventory.lblTerminal.getText());

            if (lista_detalle_orden.isEmpty()) {
                Mensaje.manipulacionExcepciones("informacion", "No hay datos de esta Orden de Servicio.", "Buscar Orden de Servicio");
            } else {
                
                int pos = 0;
                
                for (ObjetosDetalleOrden z : lista_detalle_orden) {
                    tblDetalleFactura.setValueAt(pos + 1, pos, 0);
                    tblDetalleFactura.setValueAt(z.getId_producto(), pos, 1);
                    ObjetosProducto producto = new ObjetosProducto();
                    producto = acceso_producto_buscado.buscarInformacionProducto(z.getId_producto(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    tblDetalleFactura.setValueAt(producto.getDesc_producto(), pos, 2);
                    tblDetalleFactura.setValueAt(z.getCantidad_d_orden_produco(), pos, 3);
                    tblDetalleFactura.setValueAt(producto.getPrecio_compra_producto(), pos, 4);
                    tblDetalleFactura.setValueAt(z.getPrecio_d_orden_producto(), pos, 5);
                    tblDetalleFactura.setValueAt(producto.getPrecio_min_producto(), pos, 6);
                    tblDetalleFactura.setValueAt(producto.getPrecio_est_producto(), pos, 7);
                    tblDetalleFactura.setValueAt(matematica.aproxima(z.getSub_total_d_orden_producto(), 2), pos, 8);
                    tblDetalleFactura.setValueAt("ODS" + z.getId_orden(), pos, 9);
                    tblDetalleFactura.setValueAt(0.0, pos, 10);
                    pos++;
                }

                //Buscar el costo de la reparacion colocarlo en la ultima linea
                //Esto se quito ya que siempre eligira el trabajo realizado en el catálogo de productos
                /*AccesoOrden acceso_orden = new AccesoOrden();
                ObjetosOrden orden_servicio = new ObjetosOrden();

                try {
                    
                    orden_servicio = acceso_orden.buscarOrdenPorId(id_orden_buscada, Inventory.lblSucursal.getText(), Inventory.lblTerminal.getText());
                    
                    if (orden_servicio.getId_orden() == 0) {
                        Mensaje.manipulacionExcepciones("informacion", "No se encontro el valor de esta Orden de Servicio.", "Buscar Costo Orden de Servicio");
                    } else {
                        tblDetalleFactura.setValueAt(pos + 1, pos, 0);
                        tblDetalleFactura.setValueAt("REPARACION", pos, 1);
                        ObjetosProducto costo = new ObjetosProducto();
                        costo = acceso_producto_buscado.buscarInformacionProducto("REPARACION", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                        tblDetalleFactura.setValueAt(costo.getDesc_producto(), pos, 2);
                        tblDetalleFactura.setValueAt(1, pos, 3);
                        tblDetalleFactura.setValueAt(0.0, pos, 4);
                        tblDetalleFactura.setValueAt(orden_servicio.getValor_presupuesto_orden(), pos, 5);
                        tblDetalleFactura.setValueAt(0.0, pos, 6);
                        tblDetalleFactura.setValueAt(orden_servicio.getValor_presupuesto_orden(), pos, 7);
                        tblDetalleFactura.setValueAt(matematica.aproxima(orden_servicio.getValor_presupuesto_orden(), 2), pos, 8);
                        tblDetalleFactura.setValueAt("ODS" + orden_servicio.getId_orden(), pos, 9);
                        tblDetalleFactura.setValueAt(0.0, pos, 10);
                    }
                
                } catch (Exception error) {
                    Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al buscar el costo de la reparacion.", "Buscar Costo Reparacion");
                }*/

            }

            
        }
        
        totalFactura();
    }
    
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        if(cbxEmpleado.getSelectedItem().toString().equals("Seleccione")){
            Mensaje.manipulacionExcepciones("informacion","Debe seleccionar el empleado que esta realizando la venta.", "Boton Guardar");
        }else{
            
            //Antes de guardar la factura y su detalle reviso que la suma del monto
            //credito y monto contado sea igual al total de la factura a generar
            float vContado = txtPagoContado.getText().equals("") ? 0 : Float.parseFloat(txtPagoContado.getText());
            float vCredito = txtPagoCredito.getText().equals("") ? 0 : Float.parseFloat(txtPagoCredito.getText());
            float vVuelto = txtCambio.getText().equals("") ? 0 : Float.parseFloat(txtCambio.getText());
            float vTotalPago = vContado + vCredito;
            float vTotalFactura = txtTotalFactura.getText().equals("") ? 0 : Float.parseFloat(txtTotalFactura.getText());
            float vParcial = vTotalFactura <= vTotalPago ? 0 : 1;

            // verificar que los subtotales sean los correctos
            int error_detalle = 0;

            for (int c = 0; c <= tblDetalleFactura.getRowCount(); c++) {
                try {
                    Double precio_venta = Double.parseDouble(tblDetalleFactura.getValueAt(c, 5).toString());
                    Double cantidad_venta = Double.parseDouble(tblDetalleFactura.getValueAt(c, 3).toString());
                    Double sub_total = matematica.aproxima(precio_venta * cantidad_venta, 2);
                    Double sub_total_actual = matematica.aproxima(Double.parseDouble(tblDetalleFactura.getValueAt(c, 8).toString()), 2);
                    if ((sub_total - sub_total_actual) != 0) {
                        System.out.println("Se encontro un error en el total de la factura");
                        tblDetalleFactura.setValueAt("REVISAR SUB TOTAL", c, 8);
                        error_detalle++;
                    }
                } catch (Exception error) {
                }
            }

            if (error_detalle > 0) {
                Mensaje.manipulacionExcepciones("informacion", "Revise el detalle, una o mas filas no fueron actualizadas.", "Boton Guardar");
            }

            // El total y los montos deben restar cero si no, no podra pagar
            // ya que no coinciden las cantidades ingresadas por el usuario
            if (vParcial == 0 && error_detalle == 0) {

                boolean bandera = true;
                float credito_disponible = 0;
                float vPermite_venta = 0;

                if (cbxTipoVenta.getSelectedItem().toString().equals("Credito") || cbxTipoVenta.getSelectedItem().toString().equals("Credito / Contado")) {

                    credito_disponible = NuevaFactura.retornaCreditoDisponible(txtNitPersona.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    vPermite_venta = credito_disponible - vCredito;

                    // Si vPermite_venta es mayor a 0 significa que el credito del cliente es mayor al total que se desea facturar
                    if (vPermite_venta >= 0) {
                        bandera = true;
                    } else {
                        bandera = false;

                        // Si el usuario logeado tiene rol administrador, solo le muestro una alerta pero SI le permito realizar la venta
                        if (rolUsuario.equals("Administrador")) {
                            bandera = true;
                            Mensaje.manipulacionExcepciones("informacion", "La venta excede el limite de credito. Credito disponible Q." + credito_disponible, "Boton Guardar");
                        }

                    }
                }


                if (bandera) {

                    boolean error_grabar = false;
                    float total_de_factura = Float.parseFloat(txtTotalFactura.getText());

                    //Verifica que la factura no haya sido ya guardada
                    //anteriormete para no duplicar facturas de una misma venta
                    if (txtIdFactura.getText().equals("") && total_de_factura > 0) {


                        //buscar el siguiente numero de factura cuando se
                        //requiera que la factura sea impresa por el sistema
                        String siguiente_numero_factura = "0";
                        try {
                            if (archivo.leer("[FacturaImpresa]").equals("SI")) {
                                txtSerieFactura.setText(archivo.leer("[Serie]"));
                                txtFolioFactura.setText(archivo.leer("[Folio]"));
                                siguiente_numero_factura = NuevaFactura.retornatNumeroFactura(archivo.leer("[Serie]"), archivo.leer("[Folio]"), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                                txtNumFactura.setText(siguiente_numero_factura);
                            } else {
                                txtSerieFactura.setEditable(true);
                                txtSerieFactura.setBackground(new java.awt.Color(255, 255, 255));
                                txtSerieFactura.setForeground(new java.awt.Color(0, 0, 0));
                                txtNumFactura.setEditable(true);
                                txtNumFactura.setBackground(new java.awt.Color(255, 255, 255));
                                txtNumFactura.setForeground(new java.awt.Color(0, 0, 0));
                            }
                        } catch (IOException error) {
                            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Retorna Numero Factura");
                            txtSerieFactura.setText("0");
                            txtFolioFactura.setText("0");
                            txtNumFactura.setText("0");
                        }

                        try {

                            float comision = 0;
                            
                            if(!txtComision.getText().equals("")){
                                comision = Float.parseFloat(txtComision.getText());
                            }
                            
                            Factura.setNumero_factura(Integer.parseInt(txtNumFactura.getText()));
                            Factura.setSerie_factura(txtSerieFactura.getText());
                            Factura.setFolio_factura(txtFolioFactura.getText());
                            //Obtener ID Persona
                            Factura.setId_cliente(obtenerIdCliente(txtNitPersona.getText()));
                            Factura.setFecha_emision_factura(txtFechaEmisionFactura.getText());
                            Factura.setDir_envio_factura(txtDirEnvioFactura.getText());
                            Factura.setId_empleado(Integer.parseInt(txtIdEmpleado.getText()));
                            Factura.setEstado_factura(cbxEstadoVenta.getSelectedItem().toString());
                            Factura.setTotal_factura(Float.parseFloat(txtTotalFactura.getText()));
                            Factura.setTipo_venta(cbxTipoVenta.getSelectedItem().toString());
                            Factura.setDias_de_credito(cbxDiasCredito.getSelectedItem().toString());
                            Factura.setMonto_credito(vCredito);
                            Factura.setMonto_contado(vContado - vVuelto);
                            
                            if (txtIva.getText().equals("")) {
                                Factura.setIva_factura(0);
                            } else {
                                Factura.setIva_factura(Float.parseFloat(txtIva.getText()));
                            }

                            Factura.setComision_factura(comision);
                            Factura.setCajero_factura(""); //no ha pasado por el cajero
                            Factura.setNombre_factura(txtNombrePersona.getText());
                            Factura.setPos_factura(txtPOS.getText());
                            Factura.setId_terminal(Integer.parseInt(Inventory.lblTerminal.getText())); //ID Terminal (Debe tener licencia

                        }catch(Error error){
                            Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al leer los datos de Factura : " + error.getMessage(), "Boton Guardar");
                        }

                        if (verificarDatosFactura(Factura)) {
                            String resultado = NuevaFactura.insertarFactura(Factura, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                            if (resultado.equals("Operacion realizada con exito.")) {
                                error_grabar = false;
                            } else {
                                error_grabar = true;
                            }
                        }else{
                            Mensaje.manipulacionExcepciones("critico", "No se pudo insertar Factura por errores en el Encabezado.", "Boton Guardar");
                        }

                        //Obtener ID de la Factua ingresada despues de almacenala
                        try {
                            int BuscarFactura = NuevaFactura.seleccionarIdFactura(Factura, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                            if (BuscarFactura == 0) {
                                if (error_grabar != true) {
                                    Mensaje.manipulacionExcepciones("critico", "Existe un problema con los datos ingresados en el Encabezado.", "Boton Guardar");
                                }
                            } else {
                                txtIdFactura.setText(String.valueOf(BuscarFactura));
                            }
                        }catch(Exception error){
                            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Seleccionar ID Factura");
                        }

                        //Almacenar factura y actualizar catalogo de productos
                        try {
                            
                            String filas = "";
                            
                            if (error_grabar != true) {

                                for (int cFilas = 0; cFilas < tblDetalleFactura.getRowCount(); cFilas++) {

                                    //Insertar detalle de factura
                                    if (tblDetalleFactura.getValueAt(cFilas, 0) != null) {

                                        try {

                                            Detalle.setId_d_factura((Integer) tblDetalleFactura.getValueAt(cFilas, 0));
                                            Detalle.setId_factura(Integer.parseInt(txtIdFactura.getText()));
                                            Detalle.setId_producto(tblDetalleFactura.getValueAt(cFilas, 1).toString());
                                            Detalle.setCantidad_d_factura(Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 3).toString()));
                                            Detalle.setPrecio_compra_d_factura(Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 4).toString()));
                                            Detalle.setPrecio_venta_d_factura(Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 5).toString()));
                                            Detalle.setSub_total_d_factura(Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 8).toString()));
                                            Detalle.setComentario_d_factura(tblDetalleFactura.getValueAt(cFilas, 9).toString());
                                            Detalle.setArancel_d_factura(Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 10).toString()));

                                            if (Float.parseFloat(txtTotalFactura.getText()) > 0.00) {
                                                NuevoDetalleFactura.insertarDetalleFactura(Detalle, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                                                descontarProducto(Detalle.getId_producto(), Detalle.getCantidad_d_factura());
                                            }

                                        } catch (Exception error) {
                                            filas = filas + "[" + String.valueOf(cFilas + 1) + "] ";
                                            error_grabar = true;
                                        }
                                    }
                                }
                            }
                            
                            if (error_grabar == true && !txtFilaActual.getText().isEmpty()) {
                                Mensaje.manipulacionExcepciones("critico", "Ocurrio un error en Fila " + filas + ".", "Boton Guardar");
                            }

                            if(error_grabar == false){
                                btnGuardar.setEnabled(false);
                                btnBuscar.setEnabled(false);
                                btnBuscarProducto.setEnabled(false);
                                txtBuscar.setEnabled(false);
                                btnAgregar.setEnabled(false);
                                Mensaje.manipulacionExcepciones("informacion","Venta guardada exitosamente.", "Boton Guardar");
                            }

                        } catch (Exception error) {
                            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Insertar Detalle Factura");
                        }
                        
                    } else {
                        Mensaje.manipulacionExcepciones("critico", "Esta factura ya fue guardada o tiene monto cero.", "Boton Guardar");
                    }
                
                } else {
                    Mensaje.manipulacionExcepciones("informacion","Ha excedido el limite, tiene disponible Q." + credito_disponible, "Boton Guardar");
                }
            } else {
                Mensaje.manipulacionExcepciones("informacion","La suma del pago al credito y al contado no es igual al monto total de la factura.", "Boton Guardar");
            }
        }
        
        //ALmacenadr los famos datos de FEL siempre y cuando exista el ID Factura
        if(!txtIdFactura.getText().isEmpty()) {
            AccesoFacturaFEL FEL = new AccesoFacturaFEL();
            ObjetosFacturaFel FAC = new ObjetosFacturaFel();
            FAC.setId_factura(Integer.parseInt(txtIdFactura.getText()));
            FAC.setSerie_factura_fel(txtSerieFacturaFEL.getText().trim());
            FAC.setNumero_factura_fel(txtNumFacturaFEL.getText().trim());
            FAC.setAutorizacion_factura_fel(txtAutorizacionFEL.getText().trim());
            FEL.insertarFacturaFEL(FAC, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        }
        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtNitPersonaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNitPersonaFocusLost
        
        if (txtNitPersona.getText().equals("c/f") || txtNitPersona.getText().equals("C/F") || txtNitPersona.getText().equals("")) {
            txtNitPersona.setText("C/F");
            txtNombrePersona.setText("Consumidor Final");
            txtTelCliente.setText("0");
            txtDirCliente.setText("Ciudad");
            txtFechaEmisionFactura.setText((1900 + fecha.getYear()) + "-" + (fecha.getMonth()+1) + "-" + fecha.getDate());
            txtTotalFactura.setText("0.00");
            txtDirEnvioFactura.requestFocus();
            cbxDiasCredito.removeAllItems();
            cbxDiasCredito.addItem(0);
            cbxDiasCredito.setEnabled(false);
            cbxTipoVenta.setSelectedIndex(0);
            cbxTipoVenta.setEnabled(false);
        } else {
            obtenerDatosCliente(txtNitPersona.getText());
        }
        
        txtNitSinGuion.setText(txtNitPersona.getText().toUpperCase().replaceAll("-", ""));
        btnGuardar.setEnabled(true);
        
        totalFactura();
        
    }//GEN-LAST:event_txtNitPersonaFocusLost

    public void totalFactura() {

        total_factura = 0;
        
        agregarLog("Calculando el total de la Factura.");
        
        for (int cFilas = 0; cFilas < 200; cFilas++) {
            if (tblDetalleFactura.getValueAt(cFilas, 8) != null) {
                try {
                    total_factura = total_factura + Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 8).toString());
                } catch (Exception error) {
                    total_factura = total_factura + 0;
                    Mensaje.manipulacionExcepciones("critio", "Error al calcular el total de la Factura. \nError: " + error.getMessage(), "Calcular Total");
                    agregarLog("Error al calcular el total: " + error.toString());
                }
            }
        }

        agregarLog("Total: " + total_factura);
        
        try {
            txtTotalFactura.setText(String.valueOf(matematica.redondear(total_factura)));
        } catch (Exception Error) {
            Mensaje.manipulacionExcepciones("critico", "Error al asinar el Total de la Factura. \nError: " + Error.getMessage(), "Asignar Total");
            agregarLog("Error al asignar el total: " + Error.toString());
        }

        totalIva();

    }
    
    private void totalIva(){
        try {
            agregarLog("Calculanto el IVA por pagar.");
            Double total_iva = total_factura * 0.12;
            txtIva.setText(String.valueOf(matematica.redondear(total_iva)));
            agregarLog("IVA: " + total_iva);
        } catch(Exception Error) {
            Mensaje.manipulacionExcepciones("critico", "Error al calcular el iva a pagar.", "Calcular IVA");
            agregarLog("Error calcular IVA: " + Error.toString());
        }
    }
    
    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        this.dispose();
        fila_seleccionada = 0;
        wdwMovimientoVentaDeProductos VentaDeProductos = new wdwMovimientoVentaDeProductos();
        int ancho = Inventory.pnlPrincipal.getWidth();
        int alto = Inventory.pnlPrincipal.getHeight();
        int x  = (ancho / 2) - (VentaDeProductos.getWidth() / 2);
        int y  = (alto / 2) - (VentaDeProductos.getHeight() / 2);
        VentaDeProductos.setVisible(true);
        Inventory.pnlPrincipal.add(VentaDeProductos);
        VentaDeProductos.toFront();
        VentaDeProductos.setLocation(x, y); 
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

        if (txtNitPersona.getText().isEmpty()) {
            Mensaje.manipulacionExcepciones("critico", "Llene el Encabezado de la venta, por favor.", "Boton Buscar");
        } else {
            try {
                if (txtIdBusqueda.getText().isEmpty()) {
                    Mensaje.manipulacionExcepciones("critico", "Ingrese un numero de documento, por favor.", "Boton Buscar");
                } else {
                    buscarDocumento();
                }
            } catch (Exception e) {
                Mensaje.manipulacionExcepciones("critico", "Ingrese un numero valido.", "Boton Buscar");
            }
        }
        
        //calcular comisión del empleado
        txtComision.setText("0.0");
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        
        try{
            agregarLog("Eliminar fila seleccionada.");
            DefaultTableModel modelo = (DefaultTableModel) tblDetalleFactura.getModel();
            Object nueva_fila[] = null;
            modelo.addRow(nueva_fila);
            agregarLog("Removiendo fila " + fila_seleccionada + ".");
            modelo.removeRow(fila_seleccionada);
            agregarLog("Fila removida.");
            
            agregarLog("Reacondicionando filas.");
            fila_seleccionada = 0;
            for(int cFilas = 0; cFilas < 200; cFilas++){
                if (tblDetalleFactura.getValueAt(cFilas, 0) != null) {
                    tblDetalleFactura.setValueAt(cFilas + 1, cFilas, 0);
                    fila_seleccionada++;
                }
            }
            
            agregarLog("Fila actual despues de eliminación : " + fila_seleccionada);
            txtFilaActual.setText(String.valueOf(fila_seleccionada));

        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", "Se ha eliminado una fila.", "Boton Eliminar");
            agregarLog("Error al eliminar la fila: " + error.toString());
        }
        
        totalFactura();
        
        agregarLog("Moviendo el cursor hacia la fila seleccionada.");
        tblDetalleFactura.requestFocus();
        tblDetalleFactura.changeSelection(fila_seleccionada, 0, false, false);
        agregarLog("Fila eliminada exitosamente.");
        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tblDetalleFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetalleFacturaMouseClicked
        fila_seleccionada = tblDetalleFactura.getSelectedRow();
        columna_seleccionada = tblDetalleFactura.getSelectedColumn();
        txtFilaActual.setText(String.valueOf(fila_seleccionada));
    }//GEN-LAST:event_tblDetalleFacturaMouseClicked

    private void cbxEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxEmpleadoActionPerformed
        String seleccion = "";
        seleccion = cbxEmpleado.getSelectedItem().toString();
        
        int idEmpleado = 0;
            
        try{
            idEmpleado = NuevaFactura.retornaIDEmpleado(seleccion, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            txtIdEmpleado.setText(String.valueOf(idEmpleado));
            
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Retorna ID Empleado");
        }
    }//GEN-LAST:event_cbxEmpleadoActionPerformed

    private void cbxTipoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTipoVentaActionPerformed

        if(cbxTipoVenta.getSelectedItem().toString().equals("Contado")){
            
            txtPOS.setBackground(new java.awt.Color(255, 255, 255));
            txtPagoCredito.setBackground(new java.awt.Color(255, 255, 255));
            txtRecibidoPor.setBackground(new java.awt.Color(255, 255, 255));
            txtPagoContado.setBackground(new java.awt.Color(255, 255, 204));
            
            txtPOS.setText("");
            txtPagoCredito.setText("");
            cbxDiasCredito.setSelectedItem(0);
            txtRecibidoPor.setText("");
            
            txtPOS.setEnabled(false);
            cbxDiasCredito.setEnabled(false);
            txtPagoCredito.setEnabled(false); 
            txtRecibidoPor.setEnabled(false);
            txtPagoContado.setEnabled(true);
            
        }
        
        if(cbxTipoVenta.getSelectedItem().toString().equals("Tarjeta")){
            
            txtPOS.setBackground(new java.awt.Color(255, 255, 204));
            txtPagoCredito.setBackground(new java.awt.Color(255, 255, 255));
            txtRecibidoPor.setBackground(new java.awt.Color(255, 255, 255));
            txtPagoContado.setBackground(new java.awt.Color(255, 255, 204));
            
            txtPOS.setText("");
            txtPagoCredito.setText("");
            cbxDiasCredito.setSelectedItem(0);
            txtRecibidoPor.setText("");
            
            txtPOS.setEnabled(true);
            cbxDiasCredito.setEnabled(false);
            txtPagoCredito.setEnabled(false); 
            txtRecibidoPor.setEnabled(false);
            txtPagoContado.setEnabled(true);
            
        }
        

        //Valido el credito disponible de dicho cliente
        String tipo_cliente = NuevaFactura.retornaCategoriaCliente(txtNitPersona.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        if(cbxTipoVenta.getSelectedItem().toString().equals("Credito")){
            
            if(tipo_cliente.equals("A") || tipo_cliente.equals("B")) {
                
                txtPOS.setBackground(new java.awt.Color(255, 255, 255));
                txtPagoCredito.setBackground(new java.awt.Color(255, 255, 204));
                txtRecibidoPor.setBackground(new java.awt.Color(255, 255, 204));
                txtPagoContado.setBackground(new java.awt.Color(255, 255, 255));
                
                txtPOS.setText("");
                txtPagoContado.setText("");
                txtRecibidoPor.setText(txtNombrePersona.getText());
               
                txtPOS.setEnabled(false);
                txtPagoCredito.setEnabled(true);
                cbxDiasCredito.setEnabled(true);
                txtRecibidoPor.setEnabled(true);
                txtPagoContado.setEnabled(false);
            
            } else {
                
                if(!rolUsuario.equals("Administrador")){
                    btnGuardar.setEnabled(false);
                } else {
                    Mensaje.manipulacionExcepciones("informacion", "Este cliente es categoria " + tipo_cliente + " no se puede dar credito.", "Tipo Venta");
                    cbxDiasCredito.setSelectedItem("0");
                    cbxTipoVenta.setSelectedItem("Contado");
                }
                
            }
            
        }
        
        // Si el tipo de venta es contado/credito se valida igualmente la categoria del cliente
        if(cbxTipoVenta.getSelectedItem().toString().equals("Credito / Contado")){
            
            if(tipo_cliente.equals("A") || tipo_cliente.equals("B")) {
                
                txtPOS.setBackground(new java.awt.Color(255, 255, 204));
                txtPagoCredito.setBackground(new java.awt.Color(255, 255, 204));
                txtRecibidoPor.setBackground(new java.awt.Color(255, 255, 204));
                txtPagoContado.setBackground(new java.awt.Color(255, 255, 204));
                
                txtPOS.setText("");
                txtRecibidoPor.setText(txtNombrePersona.getText());
                
                txtPOS.setEnabled(true);
                txtPagoCredito.setEnabled(true);
                cbxDiasCredito.setEnabled(true);
                txtRecibidoPor.setEnabled(true);
                txtPagoContado.setEnabled(true);
            
            } else {
                
                if(!rolUsuario.equals("Administrador")){
                    btnGuardar.setEnabled(false);
                } else {
                    Mensaje.manipulacionExcepciones("informacion", "Este cliente es categoria " + tipo_cliente + " no se puede dar credito.", "Tipo Venta");
                    cbxDiasCredito.setSelectedItem("0");
                    cbxTipoVenta.setSelectedItem("Contado");
                }
                
            }
            
        }
        
        totalFactura();
        calcularVuelto();
        
    }//GEN-LAST:event_cbxTipoVentaActionPerformed

    private void btnFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturaActionPerformed
        
        String archivo_jasper = "";
        String regimen_empresa = "";
      
        //El mensaje del regimen de la factura se toma del archivo de texto
        try {
            regimen_empresa = archivo.leer("[Regimen]");
        } catch (IOException ex) {
            regimen_empresa = "";
        }
        
        if (cbxTipoImpresion.getSelectedItem().equals("Comprobante")) {
            if (cbxTamanoImpresion.getSelectedItem().equals("Carta"))
                archivo_jasper = "rptComprobanteCarta";
            if(cbxTamanoImpresion.getSelectedItem().equals("1/2 Carta"))
                archivo_jasper = "rptComprobanteMediaCarta";
            if(cbxTamanoImpresion.getSelectedItem().equals("TMU 2.5"))
                archivo_jasper = "rptComprobanteTMU";
        }
        
        if (cbxTipoImpresion.getSelectedItem().equals("Factura Electronica")) {
            if (cbxTamanoImpresion.getSelectedItem().equals("Carta"))
                archivo_jasper = "rptFacturaCarta";
            if (cbxTamanoImpresion.getSelectedItem().equals("1/2 Carta"))
                archivo_jasper = "rptFacturaMediaCarta";
            if (cbxTamanoImpresion.getSelectedItem().equals("TMU 2.5"))
                archivo_jasper = "rptFacturaTMU";
        }
        
        if (cbxTipoImpresion.getSelectedItem().equals("Vale de Mercaderia")) {
            if (cbxTamanoImpresion.getSelectedItem().equals("Carta"))
                archivo_jasper = "rptValeCarta";
            if (cbxTamanoImpresion.getSelectedItem().equals("1/2 Carta"))
                archivo_jasper = "rptValeMediaCarta";
            if (cbxTamanoImpresion.getSelectedItem().equals("TMU 2.5"))
                archivo_jasper = "rptValeTMU";
        }
        
        //ejecución del repote seleccionado
        try{
            
            AccesoInventario acceso_inventario = new AccesoInventario();
            AccesoSucursal acceso_sucursal = new AccesoSucursal();
            ObjetosSucursal objeto_sucursal = new ObjetosSucursal();
            
            URL url_reporte = this.getClass().getResource("/inventory/reportes/" + archivo_jasper + ".jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObject(url_reporte);
            HashMap parametro = new HashMap();
            
            //parametros generales del encabezado
            objeto_sucursal = acceso_sucursal.buscarSucursales(1, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            
            //parametros del reporte
            parametro.put("P_SERIE_FACTURA", txtSerieFactura.getText());
            parametro.put("P_NUMERO_FACTURA", txtNumFactura.getText());
            parametro.put("P_ID_FACTURA", Integer.parseInt(txtIdFactura.getText()));
            parametro.put("P_DIRECCION_SUCURSAL", objeto_sucursal.getDireccion_sucursal());
            parametro.put("P_NIT", objeto_sucursal.getNit_sucursal());
            parametro.put("P_NOMBRE_EMPRESA", objeto_sucursal.getNombre_sucursal());
            parametro.put("P_SUCURSAL", objeto_sucursal.getDescripcion_sucursal());
            parametro.put("P_TELEFONO", objeto_sucursal.getTelefonos_sucursal());
            
            //parametros configuracion sistema
            parametro.put("P_REGIMEN_EMPRESA", regimen_empresa);
            
            //parametro para la impresión de vales
            parametro.put("P_RECIBIDO", txtRecibidoPor.getText());
            
            //parametros facturación FEL implementarlos solo si se desea
            //obtener facturacion electronica de la venta realizada
            parametro.put("P_SERIE_FEL", txtSerieFacturaFEL.getText());
            parametro.put("P_NUMERO_FEL", txtNumFacturaFEL.getText());
            parametro.put("P_AUTORIZACION_FEL", txtAutorizacionFEL.getText());
            
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
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Reporte " + archivo_jasper.replaceAll("rpt", "").toUpperCase());
        }
        
    }//GEN-LAST:event_btnFacturaActionPerformed

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        buscarEnRegistrosGuardados();
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void txtBuscarkeyPressedEnter(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarkeyPressedEnter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtBuscar.getText().equals("")) {
                Mensaje.manipulacionExcepciones("critico", "Ingrese el valor a buscar.", "Boton Buscar");
            } else {
                if (txtMetodoEntrada.getText().equals("INGRESO MANUAL")) {
                    buscarEnRegistrosGuardados();
                    totalFactura();
                    txtBuscar.setText("");
                } else {
                    buscarCodigoDeBarras(txtBuscar.getText().toUpperCase().replaceAll("'",  "´"));
                    totalFactura();
                    txtBuscar.setText("");
                }
            }
        }
    }//GEN-LAST:event_txtBuscarkeyPressedEnter

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked

        //Colocar oculto en teoria el valor del precio de compra
        if ("Administrador".equals(rolUsuario) || "Jefe Sucursal".equals(rolUsuario)) {
            tblProducto.setDefaultRenderer(Object.class, new NormalCell());
        } else {
            tblProducto.setDefaultRenderer(Object.class, new CustomCell());
        }
        
        if (String.valueOf(tblProducto.getSelectedColumn()).equals("0")) {
 
            txtIdProducto.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 0).toString());
            txtDescripcionProducto.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 2).toString());
            txtPrecioCompra.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 7).toString());
            txtPrecioVenta.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 8).toString());
            txtPrecioNormal.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 8).toString());
            txtPrecioMinimo.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 9).toString());
            txtPermiteDescuento.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 13).toString());
            
            if (cbxTipoDeCliente.getSelectedItem().toString().equals("Cliente Mayorista")) {
                if (txtPermiteDescuento.getText().equals("SI")) {
                    txtPrecioVenta.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 9).toString());
                } else {
                    txtPrecioVenta.setText(tblProducto.getValueAt(tblProducto.getSelectedRow(), 8).toString());
                }
            }
            
            txtCantidadVenta.setText("1");
            
            float minimo_existencia = Float.parseFloat(tblProducto.getValueAt(tblProducto.getSelectedRow(), 11).toString());
            float existencia_actual = Float.parseFloat(tblProducto.getValueAt(tblProducto.getSelectedRow(),  5).toString()); 
            
            //validar si ya esta en el minimo este producto
            if(existencia_actual < minimo_existencia) {
                agregarLog("El Producto esta al mínimo.");
                txtCantidadMinima.setBackground(Color.RED);
                txtCantidadMinima.setText(String.valueOf(existencia_actual));
            } else {
                agregarLog("El Producto tiene existencia.");
                txtCantidadMinima.setBackground(Color.DARK_GRAY);
                txtCantidadMinima.setText(String.valueOf(existencia_actual));
            }
            
            //calcular el valor del producto con el descuento seleccionado
            calcular_descuento();
        }
    }//GEN-LAST:event_tblProductoMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregarLog("Presionó el botón para agregar Productos.");
        try {
            agregarLog("Iniciando el método para agregar Productos al detalle de la Venta.");
            agregarProductoAlDetalle();
        } catch(Exception Error) {
            Mensaje.manipulacionExcepciones("critico", "Error al hacer los calculos necesarios para agregar el detalle. \n" + Error.getMessage(), "Agregar Detalle");
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    public void agregarProductoAlDetalle() {
        
        //validar si el producto ya esta al minimo para mostrar el mensaje
        try {
            agregarLog("Verificando la cantidad minima que se deber tener en existencia.");
            if (txtCantidadMinima.getText().equals("SI")) {
                Mensaje.manipulacionExcepciones("informacion", "El producto ya ha llegado al minimo.", "Boton Agregar");
            }
        } catch (Exception Error) {
            Mensaje.manipulacionExcepciones("informacion", "Ocurrio un error al validar si esta el Producto al mínimo.", "Producto al mínimo");
            agregarLog(Error.toString());
        }
        
        agregarLog("Mover linea = FALSE");
        boolean mover_linea = false;
        
        //validaciones del precio ingresado que este en el rango excepto si el usuario es administrador
        agregarLog("Verificando que el Usuario haya ingresado el precio de Venta.");
        if (txtPrecioVenta.getText().isEmpty()) {
            Mensaje.manipulacionExcepciones("critico", "Por favor ingrese el precio del articulo.", "Boton Agregar");
        } else {
            
            try {
                
                agregarLog("Inicializando variables para el manejo de los precios y descuentos.");
                float precio_venta_v  = 0;
                float precio_minimo_v = 0;
                float precio_normal_v = 0;
                
                try {
                    precio_venta_v = Float.parseFloat(txtPrecioVenta.getText());
                    agregarLog("Capturando el precio de Venta " + precio_venta_v + ".");
                } catch(Exception Error) {
                    Mensaje.manipulacionExcepciones("critcio", "Ocurrio un error al verificar el precio de Venta.", "Precio Venta");
                    agregarLog(Error.toString());
                }
                
                try {
                    precio_minimo_v = Float.parseFloat(txtPrecioMinimo.getText());
                    agregarLog("Capturando el precio mínimo de la Venta " + precio_minimo_v + ".");
                } catch(Exception Error) {
                    Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al verficar el precio Minimo", "Precio Minimo");
                    agregarLog(Error.toString());
                }
                
                try {
                    precio_normal_v = Float.parseFloat(txtPrecioNormal.getText());
                    agregarLog("Capturando el precio normal del Producto " + precio_normal_v + ".");
                } catch(Exception Error) {
                    Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al verficar el precio Nomral", "Precio Normal");
                    agregarLog(Error.toString());
                }
                
                if ( (precio_venta_v >= precio_minimo_v && precio_minimo_v <= precio_normal_v) || Inventory.lblRol.getText().equals("Administrador") ) {

                    //calcular el valor del producto con el descuento seleccionado
                    agregarLog("Iniciando variables para agregar la linea nueva al Detalle.");
                    float descuento = 0;
                    double precio_normal = 0;
                    double precio_venta = 0;
                    double precio_descuento = 0;
                    
                    
                    try {
                        descuento = 1 - (Float.parseFloat(cbxDescuento.getSelectedItem().toString()) / 100);
                        agregarLog("Capturando el descueto para el Producto " + descuento);
                    } catch (Exception Error) {
                        Mensaje.manipulacionExcepciones("critico", "Ocurrio un errror al verificar el Descuento.", "Verificar Descuento");
                    }

                    try {
                        precio_venta = matematica.aproxima(Double.parseDouble(txtPrecioVenta.getText()), 2);
                        agregarLog("Precio de venta: " + precio_venta);
                    } catch (Exception Error) {
                        Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al aproximar el Precio Normal.", "Aproximar Precio Normal");
                    }
                    
                    try {
                        precio_normal = Float.parseFloat(String.valueOf(precio_normal));
                        agregarLog("Precio normal: " + precio_normal);
                    } catch(Exception Error) {
                        Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al verificar el Precio de Venta.", "Calcular Precio Venta");
                    }
                    
                    try {
                       precio_descuento = matematica.aproxima(precio_venta * descuento, 2); 
                       agregarLog("Precio con descuento: " + precio_descuento);
                    } catch (Exception Error) {
                        Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al calcular el Precio de Venta.", "Calcular co Descuento");
                    }
                    
                    try {

                        //sacar el total vendido para colocarlo en el detalle de la venta
                        agregarLog("Cantidad: " + txtCantidadVenta.getText());
                        double total = matematica.aproxima(Float.parseFloat(txtCantidadVenta.getText()) * precio_descuento, 2);
                        agregarLog("Subtotal : " + total);

                        try {
                            //enviar datos al detalle de la pantalla de ventas con los datos del producto
                            agregarLog("Agregando la linea al detalle de la Venta.");
                            try { tblDetalleFactura.setValueAt(fila_seleccionada + 1, fila_seleccionada, 0); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Fila Seleccionada \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt(txtIdProducto.getText(), fila_seleccionada, 1); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "ID Producto \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt(txtDescripcionProducto.getText(), fila_seleccionada, 2); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Descripcion \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt(Float.parseFloat(txtCantidadVenta.getText()), fila_seleccionada, 3); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Cantidad Venta \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt(Float.parseFloat(txtPrecioCompra.getText()), fila_seleccionada, 4); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Compra \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt(matematica.aproxima(precio_descuento, 2), fila_seleccionada, 5); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Descuento \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt(Float.parseFloat(txtPrecioMinimo.getText()), fila_seleccionada, 6); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Minimo \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt(Float.parseFloat(txtPrecioNormal.getText()), fila_seleccionada, 7); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Normal \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt(total, fila_seleccionada, 8); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Total \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt("S/C", fila_seleccionada, 9); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Comentario \nError: " + Error.getMessage(), "Dato No. 1"); }
                            try { tblDetalleFactura.setValueAt(0.0, fila_seleccionada, 10); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Arancel \nError: " + Error.getMessage(), "Dato No. 1"); }
                            agregarLog("Mover linea TRUE");
                            mover_linea = true;
                        } catch (Exception e) {
                            Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al enviar los datos al Detalle de la Venta. \n" + e.getMessage(), "Error Asiganción");
                            agregarLog("Error al mover datos al detalle: " + e.toString());
                        }

                    } catch (Exception e) {
                        Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al calcular el Sub Total del Producto.", "Calcular Sub Total");
                        agregarLog("Error al calcular Total: " + e.toString());
                    }

                } else {
                    Mensaje.manipulacionExcepciones("critico", "El precio no corresponde al rango aceptado.", "Boton Agregar");
                    agregarLog("El Precio no corresponde al rango aceptado.");
                }
                
            } catch (Exception Error) {
                Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al validar el Precio del Producto.", "Validar Precio");
                agregarLog("Error al validar el precio del Producto: " + Error.toString());
            }
            
        }

        //moverse a la siguiente fila para continuar insertando productos
        try {
            agregarLog("Mover el cursor a la siguiente fila.");
            if (fila_seleccionada >= 200) {
                Mensaje.manipulacionExcepciones("critico", "Llego al final de la factura.", "Boton Agregar");
            } else {
                if (mover_linea) {
                    wdwMovimientoVentaDeProductos.tblDetalleFactura.requestFocus();
                    wdwMovimientoVentaDeProductos.tblDetalleFactura.changeSelection(fila_seleccionada, 0, false, false);
                    wdwMovimientoVentaDeProductos.fila_seleccionada++;
                    wdwMovimientoVentaDeProductos.txtFilaActual.setText(String.valueOf(fila_seleccionada));
                }
            }
        } catch (Exception Error) {
            Mensaje.manipulacionExcepciones("critico", "Error al cambiar de liene en el detalle. \nFila Seleccionada por el usuario: "
                    + fila_seleccionada + " Detalle del Error: \n" + Error.getMessage(), "Cambio de Linea");
            agregarLog("Error al mover el cursor: " + Error.toString());
        }
        
        totalFactura();
        limpiarBusqueda();
    
    }
    
    
    private void cbxDescuentoChange(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxDescuentoChange
        if (txtPermiteDescuento.getText().equals("SI")) {
            calcular_descuento();
        }
    }//GEN-LAST:event_cbxDescuentoChange

    private void txtPrecioVentaReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioVentaReleased
        calcular_descuento();
    }//GEN-LAST:event_txtPrecioVentaReleased

    private void btnMostrarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarImagenActionPerformed
        wdwMostrarImagenes pantalla = new wdwMostrarImagenes();
        int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        int x  = (ancho / 2) - (pantalla.getWidth() / 2);
        int y  = (alto / 2) -  (pantalla.getHeight() / 2) / 2;
        pantalla.setLocation(x, y);
        pantalla .setVisible(true);
        Inventory.pnlPrincipal.add(pantalla);
        pantalla .toFront();
        
        //cargar los id's de las imagenes asociadas al producto seleccionado
        ArrayList<String> lista = new ArrayList();
        AccesoImagen acceso = new AccesoImagen();
        String id = txtIdProducto.getText();
        String pro = txtDescripcionProducto.getText();
        wdwMostrarImagenes.txtIdProducto.setText(id);
        wdwMostrarImagenes.txtDescripcionProducto.setText(pro);
        lista = acceso.listarImagenes(id, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
        for(String c : lista){
            wdwMostrarImagenes.cbxIdProducto.addItem(c);
        }
    }//GEN-LAST:event_btnMostrarImagenActionPerformed

    private void cbxTipoDeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTipoDeClienteActionPerformed
        if( cbxTipoDeCliente.getSelectedItem().toString().equals("Cliente Mayorista") ) {
            cbxDescuento.setSelectedIndex(0);
            cbxDescuento.setEnabled(false);
        } else {
            cbxDescuento.setSelectedIndex(0);
            cbxDescuento.setEnabled(true);
        }
    }//GEN-LAST:event_cbxTipoDeClienteActionPerformed

    private void txtPagoCreditoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPagoCreditoKeyReleased
        txtCredito.setText(txtPagoCredito.getText());
        calcularVuelto();
        calcularComision();
    }//GEN-LAST:event_txtPagoCreditoKeyReleased

    private void btnSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerieActionPerformed

        //Validar si la factura no ha sido anulada para agregar series
        String estado = NuevaFactura.buscarEstadoFactua(txtIdFactura.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
     
        if (estado.equals("Anulado")) {
            Mensaje.manipulacionExcepciones("critico", "No puede asignar seire a una factura Anulada.", "Agregar Serie");
        } else {
            if (txtIdFactura.getText().isEmpty()) {
                Mensaje.manipulacionExcepciones("critico", "Debe guardar la compra para escanear los numeros de serie.", "Serie");
            } else {

                //Armar el listado de productos 
                ArrayList<ObjetosListaProducto> listado_productos = new ArrayList<>();
                AccesoProducto acceso = new AccesoProducto();
                listado_productos = acceso.buscarListadoSeries(txtIdFactura.getText(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

                //Llamar pantalla para el ingreso de series
                wdwCatalogoSerie Serie = new wdwCatalogoSerie(listado_productos, "0", "0", txtIdFactura.getText(), "Venta");
                int ancho = Inventory.pnlPrincipal.getWidth();
                int alto = Inventory.pnlPrincipal.getHeight();
                int x = (ancho / 2) - (Serie.getWidth() / 2);
                int y = (alto / 2) - (Serie.getHeight() / 2);
                Serie.setVisible(true);
                Inventory.pnlPrincipal.add(Serie);
                Serie.toFront();
                Serie.setLocation(x, y);
            }
        }
    }//GEN-LAST:event_btnSerieActionPerformed

    private void txtNitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNitKeyPressed
        txtNitSinGuion.setText(txtNitPersona.getText().toUpperCase().replaceAll("-", ""));
    }//GEN-LAST:event_txtNitKeyPressed

    private void btnAutorizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutorizacionActionPerformed
        
        wdwMovimientoAutorizacionVenta autorizacion = new wdwMovimientoAutorizacionVenta(null);
        int ancho = Inventory.pnlPrincipal.getWidth();
        int alto = Inventory.pnlPrincipal.getHeight();
        int x  = (ancho / 2) - (autorizacion.getWidth() / 2);
        int y  = ((alto / 2) - (autorizacion.getHeight() / 2));
        Inventory.pnlPrincipal.add(autorizacion);
        autorizacion.setVisible(true);
        autorizacion.toFront();
        autorizacion.setLocation(x, y);
        
        wdwMovimientoAutorizacionVenta.txtIdProducto.setText(txtIdProducto.getText());
        wdwMovimientoAutorizacionVenta.txtCantidad.setText(txtCantidadVenta.getText());
        wdwMovimientoAutorizacionVenta.txtPrecio.setText(txtPrecioVenta.getText());
        
    }//GEN-LAST:event_btnAutorizacionActionPerformed

    private void txtPagoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPagoKeyReleased
        try {
            txtContado.setText(txtPagoContado.getText());
        } catch(Exception Error) {
            agregarLog("Calculo de Cambio. \n" + Error.toString());
        }
        calcularVuelto();
        calcularComision();
    }//GEN-LAST:event_txtPagoKeyReleased

    private void bntActivarPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntActivarPosActionPerformed
        txtPOS.setEnabled(true);
    }//GEN-LAST:event_bntActivarPosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntActivarPos;
    public static javax.swing.JButton btnAgregar;
    public static javax.swing.JButton btnAutorizacion;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnFactura;
    private javax.swing.JButton btnGuardar;
    public static javax.swing.JButton btnMostrarImagen;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSerie;
    private javax.swing.JComboBox cbxDescuento;
    private javax.swing.JComboBox cbxDiasCredito;
    private javax.swing.JComboBox cbxEmpleado;
    private javax.swing.JComboBox cbxEstadoVenta;
    private javax.swing.JComboBox cbxTamanoImpresion;
    private javax.swing.JComboBox cbxTipoBusqueda;
    private javax.swing.JComboBox cbxTipoDeCliente;
    private javax.swing.JComboBox cbxTipoImpresion;
    private javax.swing.JComboBox cbxTipoVenta;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblContado;
    private javax.swing.JLabel lblDiasCredito;
    private javax.swing.JLabel lblDirEnvioFactura;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFolioFactura;
    private javax.swing.JLabel lblIVA;
    private javax.swing.JLabel lblIdFactura;
    private javax.swing.JLabel lblNitPersona;
    private javax.swing.JLabel lblNombrePersona;
    private javax.swing.JLabel lblNumFactura;
    private javax.swing.JLabel lblSerieFactura;
    private javax.swing.JLabel lblSerieFactura1;
    private javax.swing.JLabel lblTelCliente;
    private javax.swing.JLabel lblTipoCliente;
    private javax.swing.JLabel lblTotalFactura;
    private javax.swing.JLabel lblTotalFactura2;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlBusqueda;
    private javax.swing.JPanel pnlDatosCliente;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlDetalleProducto;
    private javax.swing.JPanel pnlFactura;
    private javax.swing.JPanel pnlVenta;
    public static javax.swing.JTable tblDetalleFactura;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtAutorizacionFEL;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCambio;
    private javax.swing.JTextField txtCantidadMinima;
    private javax.swing.JFormattedTextField txtCantidadVenta;
    public static javax.swing.JTextField txtComision;
    public static javax.swing.JTextField txtContado;
    public static javax.swing.JTextField txtCredito;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtDirCliente;
    private javax.swing.JTextField txtDirEnvioFactura;
    private javax.swing.JTextField txtFechaEmisionFactura;
    public static javax.swing.JFormattedTextField txtFilaActual;
    private javax.swing.JTextField txtFolioFactura;
    private javax.swing.JTextField txtIdBusqueda;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JTextField txtIdFactura;
    private javax.swing.JTextField txtIdProducto;
    public static javax.swing.JTextField txtIva;
    private javax.swing.JTextField txtMetodoEntrada;
    private javax.swing.JTextField txtNitPersona;
    private javax.swing.JTextField txtNitSinGuion;
    private javax.swing.JTextField txtNombrePersona;
    private javax.swing.JTextField txtNumFactura;
    private javax.swing.JTextField txtNumFacturaFEL;
    private javax.swing.JTextField txtPOS;
    private javax.swing.JTextField txtPagoContado;
    private javax.swing.JTextField txtPagoCredito;
    private javax.swing.JFormattedTextField txtPermiteDescuento;
    private javax.swing.JFormattedTextField txtPrecioCompra;
    private javax.swing.JFormattedTextField txtPrecioDescuento;
    private javax.swing.JFormattedTextField txtPrecioMinimo;
    private javax.swing.JFormattedTextField txtPrecioNormal;
    private javax.swing.JFormattedTextField txtPrecioVenta;
    public static javax.swing.JTextField txtRecibidoPor;
    private javax.swing.JTextField txtSerieFactura;
    private javax.swing.JTextField txtSerieFacturaFEL;
    private javax.swing.JTextField txtTelCliente;
    public static javax.swing.JTextField txtTotalFactura;
    // End of variables declaration//GEN-END:variables
}
