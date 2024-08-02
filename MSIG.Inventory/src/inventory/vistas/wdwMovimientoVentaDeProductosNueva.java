package inventory.vistas;

import inventory.acceso.*;
import inventory.librerias.*;
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
import inventory.guatefacturas.*;

public class wdwMovimientoVentaDeProductosNueva extends javax.swing.JInternalFrame {
    
    private String Log = "";
    private String Trigger = "";
    private String UsuarioLogeado = null;
    private String rolUsuario = null;
    private String metodo_ingreso = null;
    private String metodo_factura = null;
    private String cantidad_decimales = null;
    private String unidad_medida = null;
    private String acepta_recargo = null;
    private String codigo_recargo = null;
    private String porcentaje_recargo = null;
    private String descripcion_recargo = null;
    private String redondea_decimales_recargo = null;
    private String iva = null;
    private String tamanio_preferido = null;
    private String maximo_permitido_cf = null;
    private AccesoArchivo archivo = new AccesoArchivo();
    private Matematicas matematica = new Matematicas();
    private DefaultTableModel detalle = new DefaultTableModel();
    private WindowController ventana = new WindowController();
    
    /**
     * Creates new form wdwMovimientoVentaDeProductos
     */
    public wdwMovimientoVentaDeProductosNueva() {
        
        initComponents();
        cargaEmpleados();
        cargaTipoVenta();
        cargarDetalle();
        
        this.Log = "";
        
        mostrarRegistrosGuardados();
        
        agregarLog("Inicializando variables globales.");
        //El campo pos se habilita unicamente si elije tipo venta Tarjeta
        txtPOS.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnGeneraFEL.setEnabled(false);

        //Obtengo el usuario logeado desde pantalla principal
        UsuarioLogeado = Inventory.lblUsuario.getText();
        
        //Obtengo el rol del usuario logeado
        rolUsuario = NuevaFactura.retornatRolUsuario(UsuarioLogeado, Inventory.lblTerminal.getText());
        
        agregarLog("Verificando la cantidad de decimales.");
        //obtengo el metodo de ingreso utilizado manual o codigo de barras
        try {
            cantidad_decimales = archivo.leer("[Decimales]");
        } catch (IOException error) {
            cantidad_decimales = "0";
            agregarLog("Error al obtener el metodo de entrada: " + error.toString());
        }
        
        agregarLog("Verificando los datos del recargo.");
        try {
            acepta_recargo = archivo.leer("[AceptaRecargo]");
            codigo_recargo = archivo.leer("[CodigoRecargo]");
            porcentaje_recargo = archivo.leer("[PorcentajeRecargo]");
            descripcion_recargo = archivo.leer("[DescripcionRecargo]");
            redondea_decimales_recargo = archivo.leer("[RedondearDecimales]");
        } catch (IOException error) {
            acepta_recargo = "NO";
            codigo_recargo = "0";
            porcentaje_recargo = "0";
            descripcion_recargo = "Recargo";
            redondea_decimales_recargo = "NO";
            agregarLog("Error al obtener los datos del recargo: " + error.toString());
        }
        
        agregarLog("Verificando el valor del iva.");
        try {
            iva = archivo.leer("[IVA]");
        } catch (IOException error) {
            iva = "0";
            agregarLog("Error al obtener el alor del iva: " + error.toString());
        }
        
        agregarLog("Verificando el método de ingreso de los productos.");
        //obtengo el metodo de ingreso utilizado manual o codigo de barras
        try {
            metodo_ingreso = archivo.leer("[MetodoEntrada]");
        } catch (IOException error) {
            metodo_ingreso = "Manual";
            agregarLog("Error al obtener el metodo de entrada: " + error.toString());
        }
        
        agregarLog("Verificando el tamaño preferido de impresion.");
        try {
            tamanio_preferido = archivo.leer("[TamanioPreferido]");
            agregarLog(tamanio_preferido);
            cbxTamanoImpresion.setSelectedItem(tamanio_preferido);
        } catch (IOException error) {
            tamanio_preferido = "Carta";
            agregarLog("Error al obtener el tamaño preferido de impresión: " + error.toString());
        } finally {
            cbxTamanoImpresion.setSelectedItem(tamanio_preferido);
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
            agregarLog("Error al obtener el metodo de facturacion: " + error.toString());
        }
        
        //Obtener el metodo de actualización inventario
        agregarLog("Obtener el metodo de actualizacion inventario.");
        try {
            Trigger = archivo.leer("[Trigger]");
        } catch (IOException error) {
            Trigger = "NO";
            agregarLog("Error al obtener el metodo de actualizacion: " + error.toString());
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

        //Colocar oculto el valor del precio de compra
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
        
        agregarLog("Verificando el maximo permitido para C/F.");
        try {
            maximo_permitido_cf = archivo.leer("[MaximoPermitidoCF]");
            agregarLog(maximo_permitido_cf);
        } catch (IOException error) {
            maximo_permitido_cf = "0.00";
            agregarLog("Error al obtener el maximo permitido para C/F: " + error.toString());
        }
        
    }
    
    private void cargarDetalle() {
        
        detalle.addColumn("No.");
        detalle.addColumn("Codigo");
        detalle.addColumn("Descripcion");
        detalle.addColumn("Cantidad");
        detalle.addColumn("P. Compra");
        detalle.addColumn("P. Venta");
        detalle.addColumn("P. Minimo");
        detalle.addColumn("P. Normal");
        detalle.addColumn("Total");
        detalle.addColumn("Comentario");
        detalle.addColumn("Arancel");
        detalle.addColumn("Guardado");
        detalle.setRowCount(0);
        tblDetalleFactura.setModel(detalle);
        
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
        btnReconectar = new javax.swing.JButton();
        btnGeneraFEL = new javax.swing.JButton();
        txtTipoConexionFEL = new javax.swing.JComboBox<>();
        btnRecargoPropina = new javax.swing.JButton();
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
        txtCantidadMinima = new javax.swing.JTextField();
        btnMostrarImagen = new javax.swing.JButton();
        txtPrecioCompra = new javax.swing.JFormattedTextField();
        txtPermiteDescuento = new javax.swing.JFormattedTextField();
        btnAutorizacion = new javax.swing.JButton();
        txtPrecioEspecial = new javax.swing.JTextField();
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
        txtCategoriaCliente = new javax.swing.JTextField();
        txtIdCliente = new javax.swing.JTextField();
        txtIdPersona = new javax.swing.JTextField();
        cbxTipoDocumento = new javax.swing.JComboBox<>();
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
        txtTotalBrutoFactura = new javax.swing.JTextField();
        pnlDetalle = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalleFactura = new javax.swing.JTable();

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

        btnReconectar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/bntBotonReconectar.png"))); // NOI18N
        btnReconectar.setToolTipText("");
        btnReconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReconectarActionPerformed(evt);
            }
        });

        btnGeneraFEL.setText("FEL");
        btnGeneraFEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneraFELActionPerformed(evt);
            }
        });

        txtTipoConexionFEL.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GFN", "GFC" }));

        btnRecargoPropina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgBotonPropina.png"))); // NOI18N
        btnRecargoPropina.setToolTipText("");
        btnRecargoPropina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecargoPropinaActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReconectar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRecargoPropina)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTipoConexionFEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGeneraFEL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBotonesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnReconectar)
                            .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnSerie, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnFactura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bntActivarPos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(pnlBotonesLayout.createSequentialGroup()
                        .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRecargoPropina)
                            .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnNuevo)
                                .addComponent(btnGuardar)
                                .addComponent(btnEliminar)
                                .addComponent(btnGeneraFEL)
                                .addComponent(txtTipoConexionFEL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbxTipoImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbxTamanoImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
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

        txtPrecioMinimo.setEditable(false);
        txtPrecioMinimo.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecioMinimo.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecioMinimo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPrecioMinimo.setToolTipText("Muestra el Precio mas bajo al qu se puede vender.");

        jLabel6.setText("Producto");

        txtIdProducto.setEditable(false);
        txtIdProducto.setBackground(new java.awt.Color(102, 102, 102));
        txtIdProducto.setForeground(new java.awt.Color(255, 255, 255));
        txtIdProducto.setToolTipText("Muestra el ID del producto seleccionado para la Venta.");

        txtDescripcionProducto.setEditable(false);
        txtDescripcionProducto.setBackground(new java.awt.Color(102, 102, 102));
        txtDescripcionProducto.setForeground(new java.awt.Color(255, 255, 255));
        txtDescripcionProducto.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDescripcionProducto.setToolTipText("Muestra la Descripcion del Producto seleccionado para la Venta.");

        jLabel7.setText("Precio");

        txtPrecioNormal.setEditable(false);
        txtPrecioNormal.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecioNormal.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecioNormal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPrecioNormal.setToolTipText("Muestra el Precio Normal del Producto");

        jLabel8.setText("Cantidad");

        txtCantidadVenta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#.##"))));
        txtCantidadVenta.setToolTipText("Ingresa la cantidad a vender, siempre y cuando tenga existencia disponible.");

        txtPrecioDescuento.setEditable(false);
        txtPrecioDescuento.setBackground(new java.awt.Color(255, 51, 0));
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

        txtCantidadMinima.setEditable(false);
        txtCantidadMinima.setBackground(new java.awt.Color(102, 102, 102));
        txtCantidadMinima.setForeground(new java.awt.Color(255, 255, 255));
        txtCantidadMinima.setToolTipText("Indica la Existencia del Producto");

        btnMostrarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory/imagenes/imgMostrarImagenes.png"))); // NOI18N
        btnMostrarImagen.setActionCommand("btnAgregar");
        btnMostrarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarImagenActionPerformed(evt);
            }
        });

        txtPrecioCompra.setEditable(false);
        txtPrecioCompra.setBackground(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setForeground(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtPrecioCompra.setToolTipText("Muestra el Precio mas bajo al qu se puede vender.");
        txtPrecioCompra.setCaretColor(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setDisabledTextColor(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setSelectedTextColor(new java.awt.Color(0, 102, 153));
        txtPrecioCompra.setSelectionColor(new java.awt.Color(0, 102, 153));

        txtPermiteDescuento.setEditable(false);
        txtPermiteDescuento.setBackground(new java.awt.Color(102, 102, 102));
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

        txtPrecioEspecial.setEditable(false);
        txtPrecioEspecial.setBackground(new java.awt.Color(51, 153, 0));
        txtPrecioEspecial.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecioEspecial.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

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
                .addGroup(pnlBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxDescuento, 0, 59, Short.MAX_VALUE)
                    .addComponent(txtPrecioEspecial))
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
                                        .addComponent(txtPrecioEspecial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetalleProductoLayout.createSequentialGroup()
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

        lblNitPersona.setText("Documento");

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

        txtTelCliente.setEditable(false);
        txtTelCliente.setBackground(new java.awt.Color(102, 102, 102));
        txtTelCliente.setForeground(new java.awt.Color(255, 255, 255));
        txtTelCliente.setToolTipText("Telefono del cliente almacenado en la Base de Datos.");

        lblNombrePersona.setText("Nombre");

        txtNombrePersona.setBackground(new java.awt.Color(255, 255, 204));
        txtNombrePersona.setToolTipText("Nombre del cliente, este puede ser modificado.");

        txtDirCliente.setEditable(false);
        txtDirCliente.setBackground(new java.awt.Color(102, 102, 102));
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

        txtMetodoEntrada.setEditable(false);
        txtMetodoEntrada.setBackground(new java.awt.Color(102, 102, 102));
        txtMetodoEntrada.setForeground(new java.awt.Color(255, 255, 255));
        txtMetodoEntrada.setToolTipText("Muestra si el sistema esta configurado para usar Lector de Barras o esta en el modo Normal.");

        lblTotalFactura2.setText("Modo");

        txtNitSinGuion.setBackground(new java.awt.Color(204, 204, 255));

        txtCategoriaCliente.setEditable(false);
        txtCategoriaCliente.setBackground(new java.awt.Color(255, 51, 0));
        txtCategoriaCliente.setForeground(new java.awt.Color(255, 255, 255));
        txtCategoriaCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtIdCliente.setEditable(false);
        txtIdCliente.setBackground(new java.awt.Color(51, 153, 0));
        txtIdCliente.setForeground(new java.awt.Color(255, 255, 255));
        txtIdCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtIdPersona.setEditable(false);
        txtIdPersona.setBackground(new java.awt.Color(51, 153, 0));
        txtIdPersona.setForeground(new java.awt.Color(255, 255, 255));
        txtIdPersona.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        cbxTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NIT", "DPI", "PAS" }));

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
                                .addComponent(txtNitPersona)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCategoriaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIdPersona, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNombrePersona)
                            .addGroup(pnlDatosClienteLayout.createSequentialGroup()
                                .addComponent(txtTelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTipoCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbxTipoDeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosClienteLayout.createSequentialGroup()
                                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDirEnvioFactura)
                                    .addComponent(txtMetodoEntrada))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnlDatosClienteLayout.createSequentialGroup()
                                        .addComponent(lblDiasCredito)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbxDiasCredito, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(txtNitSinGuion, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                    .addComponent(txtCategoriaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdPersona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(lblDirEnvioFactura)
                    .addComponent(txtNitSinGuion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDatosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMetodoEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalFactura2)
                    .addComponent(lblDiasCredito)
                    .addComponent(cbxDiasCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlVenta.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "VENTA", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));

        jLabel14.setText("ID Empleado");

        txtIdEmpleado.setEditable(false);
        txtIdEmpleado.setBackground(new java.awt.Color(102, 102, 102));
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

        lblContado.setText("Contado");

        txtCambio.setEditable(false);
        txtCambio.setBackground(new java.awt.Color(255, 51, 0));
        txtCambio.setForeground(new java.awt.Color(255, 255, 255));
        txtCambio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCambio.setToolTipText("Muestra cuanto debe de entregar de cambio segun lo ingresado en Pago al Contado.");

        lblSerieFactura1.setText("Cambio");

        txtPOS.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPOS.setToolTipText("Aqui debe de ingresar el ticket generado por el POS.");

        jLabel15.setText("POS");

        jLabel2.setText("Comision");

        txtComision.setEditable(false);
        txtComision.setBackground(new java.awt.Color(102, 102, 102));
        txtComision.setForeground(new java.awt.Color(255, 255, 255));
        txtComision.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComision.setToolTipText("Comision ganada por Empleado.");

        txtPagoContado.setBackground(new java.awt.Color(255, 255, 204));
        txtPagoContado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPagoContado.setToolTipText("Ingresa la cantidad que se pago al contado.");

        lblIVA.setText("IVA");

        txtIva.setEditable(false);
        txtIva.setBackground(new java.awt.Color(102, 102, 102));
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

        txtIdFactura.setEditable(false);
        txtIdFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtIdFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtIdFactura.setToolTipText("Codigo automatico generado por el sistema");

        lblFecha.setText("Fecha");

        txtFechaEmisionFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtFechaEmisionFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtFechaEmisionFactura.setToolTipText("Fecha de venta formato YYYY-MM-DD.");

        lblNumFactura.setText("Impresa");

        txtNumFacturaFEL.setEditable(false);
        txtNumFacturaFEL.setBackground(new java.awt.Color(102, 102, 102));
        txtNumFacturaFEL.setForeground(new java.awt.Color(255, 255, 255));
        txtNumFacturaFEL.setText("0");
        txtNumFacturaFEL.setToolTipText("Correlativo de factura segun serie configurada en el sistema.");

        lblSerieFactura.setText("Electronica");

        txtSerieFactura.setEditable(false);
        txtSerieFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtSerieFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtSerieFactura.setText("0");
        txtSerieFactura.setToolTipText("Numero de serie de factura, no es obligatorio excepto en facturacion electronica.");

        jLabel11.setText("Autorizacion");

        txtAutorizacionFEL.setEditable(false);
        txtAutorizacionFEL.setBackground(new java.awt.Color(102, 102, 102));
        txtAutorizacionFEL.setForeground(new java.awt.Color(255, 255, 255));
        txtAutorizacionFEL.setText("0");
        txtAutorizacionFEL.setToolTipText("Este valor unicamente se utiliza para facturacion FEL.");

        txtFolioFactura.setEditable(false);
        txtFolioFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtFolioFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtFolioFactura.setText("0");
        txtFolioFactura.setToolTipText("Numero de folio de las facturas, este dato no es obligatorio.");

        lblFolioFactura.setText("Folio");

        jLabel3.setText("Vale Para");

        txtRecibidoPor.setToolTipText("Se utiliza en caso que se entregue un vale por mercaderia.");

        lblTotalFactura.setText("Total");

        txtTotalFactura.setEditable(false);
        txtTotalFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtTotalFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalFactura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalFactura.setToolTipText("");

        txtSerieFacturaFEL.setEditable(false);
        txtSerieFacturaFEL.setBackground(new java.awt.Color(102, 102, 102));
        txtSerieFacturaFEL.setForeground(new java.awt.Color(255, 255, 255));
        txtSerieFacturaFEL.setText("0");
        txtSerieFacturaFEL.setToolTipText("Serie de factura configurado en el sistema.");

        txtNumFactura.setEditable(false);
        txtNumFactura.setBackground(new java.awt.Color(102, 102, 102));
        txtNumFactura.setForeground(new java.awt.Color(255, 255, 255));
        txtNumFactura.setText("0");
        txtNumFactura.setToolTipText("Numero de serie de factura, no es obligatorio excepto en facturacion electronica.");

        txtTotalBrutoFactura.setEditable(false);
        txtTotalBrutoFactura.setBackground(new java.awt.Color(255, 51, 0));
        txtTotalBrutoFactura.setForeground(new java.awt.Color(255, 51, 0));
        txtTotalBrutoFactura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalBrutoFactura.setToolTipText("");

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
                    .addComponent(txtRecibidoPor)
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addComponent(txtFolioFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotalBrutoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTotalFactura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotalFactura))
                    .addComponent(txtAutorizacionFEL, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
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
                    .addComponent(lblFolioFactura)
                    .addComponent(txtTotalBrutoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRecibidoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblDetalleFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblDetalleFactura);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlDatosCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlDetalleProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBusqueda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDetalle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    float total_bruto;
    float cantidad = 0;
    float precio = 0;
    
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

    private boolean calcularVuelto() {
        
        boolean esta_cabal = false;
        boolean ingreso_contado = txtPagoContado.getText().isEmpty() ? false : true;
        boolean ingreso_credito = txtPagoCredito.getText().isEmpty() ? false : true;
        
        //Calcular el vuelto para esta compra
        try {

            agregarLog("Creadon variables para los calculos del cmbio.");
            double credito = Double.parseDouble(txtPagoCredito.getText().isEmpty() ? "0" : txtPagoCredito.getText());
            double efectivo = Double.parseDouble(txtPagoContado.getText().isEmpty() ? "0" : txtPagoContado.getText());
            double total_venta = Double.parseDouble(txtTotalFactura.getText().isEmpty() ? "0" : txtTotalFactura.getText());
            double vuelto = 0;
            double total_pago = 0; 

            if (cbxTipoVenta.getSelectedItem().toString().equals("Contado")) {
                
                if (ingreso_contado) {

                    vuelto = matematica.aproxima((efectivo - total_venta), 2);
                    total_pago = matematica.aproxima((efectivo - vuelto), 2);

                    if (total_pago == total_venta) {
                        esta_cabal = true;
                        txtCambio.setText(String.valueOf(vuelto));
                        txtContado.setText(String.valueOf(matematica.aproxima(total_pago, 2)));
                        txtPagoCredito.setText("0.0");
                        txtCredito.setText("0.0");
                    } else {
                        esta_cabal = false;
                        Mensaje.manipulacionExcepciones("critico", "Verifique el valor ingresado como pago al contado.", "Calcular Cambio");

                    }

                } else {
                    esta_cabal = false;
                    Mensaje.manipulacionExcepciones("critico", "Por favor ingrese la cantidad al contado.", "Contado");
                }
                
            }
            
          
            if (cbxTipoVenta.getSelectedItem().toString().equals("Credito / Contado")) {

                if (ingreso_contado && ingreso_credito) {

                    agregarLog("Pago Contado: " + efectivo);
                    agregarLog("Pago Credito: " + credito);
                    agregarLog("Total Venta: " + total_venta);
                    
                    vuelto = matematica.aproxima(((efectivo + credito) - total_venta), 2);
                    
                    agregarLog("Cambio: " + vuelto);
                    
                    if(efectivo > credito) {
                        total_pago = matematica.aproxima((total_venta - credito), 2);
                    } else {
                        total_pago = matematica.aproxima((efectivo), 2);
                    }
                    
                    agregarLog("Contado: " + total_pago);
                    agregarLog("Credigo: " + credito);

                    if ((total_pago + credito) == total_venta) {
                        esta_cabal = true;
                        txtCambio.setText(String.valueOf(vuelto));
                        txtContado.setText(String.valueOf(matematica.aproxima(total_pago, 2)));
                        txtCredito.setText(String.valueOf(matematica.aproxima(credito, 2)));
                    } else {
                        esta_cabal = false;
                        Mensaje.manipulacionExcepciones("critico", "Verifique el valor ingresado como pago al contado.", "Calcular Cambio");

                    }

                } else {
                    esta_cabal = false;
                    Mensaje.manipulacionExcepciones("critico", "Por favor ingrese la cantidad al contado y al credito.", "Contado");
                }
                
            }
            
            if (cbxTipoVenta.getSelectedItem().toString().equals("Credito")) {
                txtPagoCredito.setText(txtTotalFactura.getText());
                txtCredito.setText(txtTotalFactura.getText());
                esta_cabal = true;
            }
            
            if (cbxTipoVenta.getSelectedItem().toString().equals("Tarjeta")) {
                txtPagoContado.setText(txtTotalFactura.getText());
                txtContado.setText(txtTotalFactura.getText());
                txtCredito.setText("0.0");
                txtCambio.setText("0.0");
                esta_cabal = true;
            }
            
        } catch (Exception Error) {
            agregarLog("Calculo de Cambio. \n" + Error.toString());
        }
        
        return esta_cabal;
    }
    
    private void generaFacturaElectronicaFEL() {
        
        Resultado resultado = new Resultado();
        
        if (Integer.parseInt(txtIdFactura.getText()) > 0 && txtAutorizacionFEL.getText().equals("0")
                && txtSerieFacturaFEL.getText().equals("0") && txtNumFacturaFEL.getText().endsWith("0")) {
            
            agregarLog("Intentando generar Factura Electronica");
            
            int id = Integer.parseInt(txtIdFactura.getText());
            
            agregarLog("ID Venta: " + id);
            
            GenerarDocumentoFEL felConDecimales = new GenerarDocumentoFEL();
            GenerarDocumentoSinDecimalesFEL felSinDecimales = new GenerarDocumentoSinDecimalesFEL();
            
            int decimales = Integer.parseInt(cantidad_decimales);
            if(decimales > 0) {
                agregarLog("Se utilizará el metodo con decimales");
                resultado = felConDecimales.generarFacturaElectronica(txtTipoConexionFEL.getSelectedItem().toString(), id, txtNombrePersona.getText(), 
                    "", "Venta de Productos", "FEL", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), cbxTipoDocumento.getSelectedItem().toString());
            } else {
                agregarLog("Se utilizará el metodo sin decimales");
                resultado = felSinDecimales.generarFacturaElectronica(txtTipoConexionFEL.getSelectedItem().toString(), id, txtNombrePersona.getText(), 
                    "", "Venta de Productos", "FEL", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText(), cbxTipoDocumento.getSelectedItem().toString());
            }
            
            String autorizacion_guatefactruas_fel = "";
            
            agregarLog("AUTORIZACION: " + resultado.getAutorizacion());
            
            if (resultado.getAutorizacion().equals("0")) {
                
                agregarLog("GeneraDocumento: " + resultado.getResultado());
                String respuesta_fel = resultado.getResultado().replaceAll("<Resultado>", "").replaceAll("</Resultado>", "").replaceAll("-", " ").replaceAll("&", "");
                Mensaje.manipulacionExcepciones("critico", respuesta_fel, "Error Generacion FEL");
            
            } else {
                
                String nombre_cliente_sat = resultado.getNombre().replaceAll("'", "´").replaceAll(",", " ");
                
                if(txtNombrePersona.equals(nombre_cliente_sat)) {
                    agregarLog("El nombre del cliente es correcto según SAT.");
                } else {
                    agregarLog("Actualizando nombre del Cliente para esta factura");
                    AccesoCliente acceso_cliente = new AccesoCliente();
                    //acceso_cliente.actualizarDireccionCliente(txtIdCliente.getText(), resultado.getDireccion(), "Venta de Producto", "Generar FEL", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    acceso_cliente.actualizarNombreFactura(txtIdFactura.getText(), resultado.getNombre(), "Venta de Producto", "Generar FEL", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                }
                
                agregarLog("ALmacenando informacion en Base de Datos FEL.");
                autorizacion_guatefactruas_fel = resultado.getAutorizacion();

                //almacenar datos de FEL en la base de datos
                AccesoFacturaFEL FEL = new AccesoFacturaFEL();
                ObjetosFacturaFel FAC = new ObjetosFacturaFel();
                FAC.setId_factura(Integer.parseInt(txtIdFactura.getText()));
                FAC.setSerie_factura_fel(resultado.getSerie());
                FAC.setNumero_factura_fel(resultado.getPreimpreso());
                FAC.setAutorizacion_factura_fel(resultado.getAutorizacion());
                
                String resultado_actualizacion_fel = FEL.actualizarDatosFEL(FAC, "Venta de Productos", "Generar FEL", Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

                agregarLog("Resultado: " + resultado_actualizacion_fel);
                
                if (resultado_actualizacion_fel.equals("Operacion realizada con exito.")) {
                    txtAutorizacionFEL.setText(resultado.getAutorizacion());
                    txtSerieFacturaFEL.setText(resultado.getSerie());
                    txtNumFacturaFEL.setText(resultado.getPreimpreso());
                    Mensaje.manipulacionExcepciones("informacion", "Numero de autorización:\n" + autorizacion_guatefactruas_fel, "FEL");
                    cbxTipoImpresion.setSelectedItem("Factura Electronica");
                } else {
                    Mensaje.manipulacionExcepciones("informacion", "No se pudeo actualizar los valores de FEL en la Base de Datos", "FEL");
                }
                
            }
        
        } else {
            
            Mensaje.manipulacionExcepciones("informacion", "No se puede generar Facutara a esta Venta", "FEL");
        
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
            tabla.addColumn("Precio Especial");
            
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
                                tabla.setValueAt(xProducto.getPrecio_especial_producto(), cProducto, 14);
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
                tabla.addColumn("Precio Especial");

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
                                        tabla.setValueAt(xProducto.getPrecio_especial_producto(), cProducto, 14);
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
                Object[] datos = new Object[11];
                datos[0] = detalle.getRowCount();
                datos[1] = txtIdProducto.getText();
                datos[2] = txtDescripcionProducto.getText();
                datos[3] = Float.parseFloat(txtCantidadVenta.getText());
                datos[4] = txtPrecioCompra.getText();
                datos[5] = txtPrecioDescuento.getText();
                datos[6] = Float.parseFloat(txtPrecioMinimo.getText());
                datos[7] = Float.parseFloat(txtPrecioNormal.getText());
                datos[8] = matematica.aproxima(Double.parseDouble(txtCantidadVenta.getText()) * Double.parseDouble(txtPrecioDescuento.getText()), 2);
                datos[9] = "S/C";
                datos[10] = 0.0;
                detalle.addRow(datos);
                tblDetalleFactura.setModel(detalle);
            }
        } catch (Exception error) {
            Mensaje.manipulacionExcepciones("Informacion",  error.getMessage(), "Incrementar Valor de Fila");
        }

    }
    
    private String descontarProducto(String pId_producto, float pCantidad){
        String resultado = "";
        ArrayList<ObjetosProducto> BuscarProducto = new ArrayList(); 
        try{
            BuscarProducto = NuevoProducto.buscarProducto(pId_producto, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
            if(BuscarProducto.isEmpty()){
                Mensaje.manipulacionExcepciones("critico", "No existe el Producto "+pId_producto+" en la Base de Datos", "Descontar Producto");
            } else {
                if(BuscarProducto.size()>1){
                    Mensaje.manipulacionExcepciones("critico", "Existe mas de un Producto con este ID " + pId_producto, "Descontar Producto");
                } else {
                    resultado = NuevoProducto.actualizarExistencia(pId_producto, BuscarProducto.get(0).getExi_producto() - pCantidad, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                }
            }
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", error.getMessage(), "Actualizar Existencia " + pId_producto);
        }
        return resultado;
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
            txtIdPersona.setText("0");
            persona_encontrada = false;
        } else {
            System.out.println("Cantidad de personas encontradas: " + BuscarPersona.size());
            if(BuscarPersona.size() > 1){
                Mensaje.manipulacionExcepciones("critico", "Existe mas de una persona con este numero de NIT.", "Obtener Datos Cliente");
                txtIdPersona.setText("0");
                persona_encontrada = false;
            } else {
                txtNombrePersona.setText(BuscarPersona.get(0).getNom_persona());
                txtIdPersona.setText(String.valueOf(BuscarPersona.get(0).getId_persona()));
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
            txtIdCliente.setText("0");
            
            if (Mensaje.valor == 0) {
                wdwCatalogoClientes CatalogoClientes = new wdwCatalogoClientes();
                ventana.abrirPantalla(CatalogoClientes);
                CatalogoClientes.txtNitPersona.setText(txtNitPersona.getText());
                Mensaje.valor = 2;
            } else {
                txtNitPersona.setText("C/F");
                txtCategoriaCliente.setText("C");
                txtNitPersona.requestFocus();
            }
            
        } else {
            
            if (BuscarCliente.size() > 1) {
                Mensaje.manipulacionExcepciones("critico", "Se encontro mas de un Cliente con este numero de NIT.", "Buscar Datos Cliente");
                txtIdCliente.setText("0");
            } else {
                
                txtIdCliente.setText(String.valueOf(BuscarCliente.get(0).getId_cliente()));
                
                txtCategoriaCliente.setText(BuscarCliente.get(0).getTipo_cliente());
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
        
        //Colocar oculto el valor del precio de compra
        if ("Administrador".equals(rolUsuario) || "Jefe Sucursal".equals(rolUsuario)) {
            this.tblDetalleFactura.setDefaultRenderer(Object.class, new SaveCellAdministrador());
        } else {
            tblDetalleFactura.setDefaultRenderer(Object.class, new SaveCellVendedor());
        }
        
        AccesoProducto acceso_producto_buscado = new AccesoProducto();
        
        if (cbxTipoBusqueda.getSelectedItem().equals("Factura")) {

            ArrayList<ObjetosDetalleFactura> lista_detalle_factura = new ArrayList<>();
            AccesoDetalleFactura acceso_detalle_factura = new AccesoDetalleFactura();

            int id_factura_buscada = Integer.parseInt(txtIdBusqueda.getText());
            lista_detalle_factura = acceso_detalle_factura.buscarDetalleFactura(id_factura_buscada, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());

            if (lista_detalle_factura.isEmpty()) {
                Mensaje.manipulacionExcepciones("informacion", "No existe una Venta con este ID Factura.", "Buscar Documento");
            } else {
                detalle.setRowCount(0);
                for (ObjetosDetalleFactura x : lista_detalle_factura) {
                    Object[] datos = new Object[12];
                    datos[0] = detalle.getRowCount() + 1;
                    datos[1] = x.getId_producto();
                    ObjetosProducto producto_buscado = new ObjetosProducto();
                    producto_buscado = acceso_producto_buscado.buscarInformacionProducto(x.getId_producto(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    datos[2] = producto_buscado.getDesc_producto();
                    datos[3] = x.getCantidad_d_factura();
                    datos[4] = x.getPrecio_compra_d_factura();
                    datos[5] = x.getPrecio_venta_d_factura();
                    datos[6] = producto_buscado.getPrecio_min_producto();
                    datos[7] = producto_buscado.getPrecio_est_producto();
                    datos[8] = matematica.aproxima((x.getCantidad_d_factura() * x.getPrecio_venta_d_factura()), 2);
                    datos[9] = x.getComentario_d_factura();
                    datos[10] = x.getArancel_d_factura();
                    try { datos[11] = "NO"; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Guardado \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    detalle.addRow(datos);
                }
                tblDetalleFactura.setModel(detalle);
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
                detalle.setRowCount(0);
                for (ObjetosDetalleProforma x : lista_detalle_proforma) {
                    Object[] datos = new Object[12];
                    datos[0] = detalle.getRowCount() + 1;
                    datos[1] = x.getId_producto();
                    ObjetosProducto producto_buscado = new ObjetosProducto();
                    producto_buscado = acceso_producto_buscado.buscarInformacionProducto(x.getId_producto(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    datos[2] = producto_buscado.getDesc_producto();
                    datos[3] = x.getCant_d_proforma();
                    datos[4] = producto_buscado.getPrecio_compra_producto();
                    datos[5] = x.getPrecio_venta_d_proforma();
                    datos[6] = producto_buscado.getPrecio_min_producto();
                    datos[7] = producto_buscado.getPrecio_est_producto();
                    datos[8] = matematica.aproxima((x.getCant_d_proforma() * x.getPrecio_venta_d_proforma()), 2);
                    datos[9] = "PFA";
                    datos[10] = 0.0;
                    try { datos[11] = "NO"; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Guardado \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    detalle.addRow(datos);
                }
                tblDetalleFactura.setModel(detalle);
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
                detalle.setRowCount(0);
                for (ObjetosDetalleOrden z : lista_detalle_orden) {
                    Object[] datos = new Object[12];
                    datos[0] =  detalle.getRowCount() + 1;
                    datos[1] =  z.getId_producto();
                    ObjetosProducto producto = new ObjetosProducto();
                    producto = acceso_producto_buscado.buscarInformacionProducto(z.getId_producto(), Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                    datos[2] = producto.getDesc_producto();
                    datos[3] = z.getCantidad_d_orden_produco();
                    datos[4] = producto.getPrecio_compra_producto();
                    datos[5] = z.getPrecio_d_orden_producto();
                    datos[6] = producto.getPrecio_min_producto();
                    datos[7] = producto.getPrecio_est_producto();
                    datos[8] = matematica.aproxima(z.getSub_total_d_orden_producto(), 2);
                    datos[9] = "ODS " + txtIdBusqueda.getText().trim();
                    datos[10] = 0.0;
                    try { datos[11] = "NO"; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Guardado \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    detalle.addRow(datos);
                }
                tblDetalleFactura.setModel(detalle);
            }

            
        }
        
        totalFactura();
    }
    
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
        totalFactura();
        totalIva();
        
        //Obtener el total segun el calculo efecturado de los productos
        double total_a_facturar = Double.parseDouble(txtTotalFactura.getText());
        
        //Obtener el total impuesto por la ley para emision de facturas con C/F
        double maximo_permitido = Double.parseDouble(maximo_permitido_cf);
        String numero_documento = txtNitPersona.getText().trim().toUpperCase();
        
        if(total_a_facturar >= maximo_permitido && (numero_documento.equals("C/F") || numero_documento.equals("CF"))) {
            Mensaje.manipulacionExcepciones("critico", "No puede emitir una Factura con este monto para Consumidor Final", "Validación Maximo CF");
        } else {
            GuardarVenta();
        }

    }//GEN-LAST:event_btnGuardarActionPerformed

    public void GuardarVenta() {
        
        boolean calculo_de_cambio = calcularVuelto();
        agregarLog("Calculo de Cambio: " + String.valueOf(calculo_de_cambio));
        
        
        if(calculo_de_cambio) {
        
        calcularComision();
        
        //Validacaciones para guardar la venta
        if(cbxEmpleado.getSelectedItem().toString().equals("Seleccione")){
            
            Mensaje.manipulacionExcepciones("informacion","Debe seleccionar el empleado que esta realizando la venta.", "Boton Guardar");
        
        } else {
            
            //Antes de guardar la factura y su detalle reviso que la suma del monto
            //credito y monto contado sea igual al total de la factura a generar
            float vContado = txtPagoContado.getText().trim().equals("") ? 0 : Float.parseFloat(txtPagoContado.getText().trim());
            float vCredito = txtPagoCredito.getText().trim().equals("") ? 0 : Float.parseFloat(txtPagoCredito.getText());
            float vVuelto = txtCambio.getText().trim().equals("") ? 0 : Float.parseFloat(txtCambio.getText().trim());
            float vTotalPago = vContado + vCredito;
            float vTotalFactura = txtTotalFactura.getText().equals("") ? 0 : Float.parseFloat(txtTotalFactura.getText());
            float vParcial = vTotalFactura <= vTotalPago ? 0 : 1;

            //verificar que los subtotales sean los correctos
            int error_detalle = 0;

            for (int c = 0; c <= tblDetalleFactura.getRowCount(); c++) {
                
                try {
                    
                    Double precio_venta = Double.parseDouble(tblDetalleFactura.getValueAt(c, 5).toString().trim());
                    Double cantidad_venta = Double.parseDouble(tblDetalleFactura.getValueAt(c, 3).toString().trim());
                    Double sub_total = matematica.aproxima(precio_venta * cantidad_venta, 2);
                    Double sub_total_actual = matematica.aproxima(Double.parseDouble(tblDetalleFactura.getValueAt(c, 8).toString()), 2);
                    
                    if ((sub_total - sub_total_actual) != 0) {
                        
                        agregarLog("Error en subtotal de factura linea " + c + ".");
                        agregarLog("Precio Venta: " + precio_venta);
                        agregarLog("Cantiad Venta: " + cantidad_venta);
                        agregarLog("Subtotal: " + sub_total);
                        agregarLog("Subtotal Factura: " + sub_total_actual);
                        agregarLog("Diferencia de Totales: " + (sub_total - sub_total_actual));
                        tblDetalleFactura.setValueAt("REVISAR SUB TOTAL", c, 9);
                        error_detalle++;
                        
                    }
                    
                } catch (Exception Error) {
                    agregarLog("Error al calcular el Subtotal No. " + c + " \nError: " + Error.toString());
                }
                
            }

            if (error_detalle > 0) {
                Mensaje.manipulacionExcepciones("informacion", "Revise el detalle no se pudo calcular el subtotal de una o mas filas.", "Boton Guardar");
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
                            
                        } catch (IOException Error) {
                            
                            agregarLog("Error al buscar correlativo de facturas Preimpresas. \nError: " + Error.toString());
                            Mensaje.manipulacionExcepciones("critico", Error.getMessage(), "Retorna Numero Factura");
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
                            
                            //Obtener ID Persona para almacenar datos de factura
                            Factura.setId_cliente(obtenerIdCliente(txtNitPersona.getText()));
                            Factura.setFecha_emision_factura(txtFechaEmisionFactura.getText());
                            Factura.setDir_envio_factura(txtDirEnvioFactura.getText());
                            Factura.setId_empleado(Integer.parseInt(txtIdEmpleado.getText()));
                            Factura.setEstado_factura(cbxEstadoVenta.getSelectedItem().toString());
                            Factura.setTotal_bruto_factura(Float.parseFloat(txtTotalBrutoFactura.getText()));
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
                            
                            if (Double.parseDouble(txtTotalFactura.getText()) > 0) {
                                
                                String resultado = NuevaFactura.insertarFactura(Factura, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                                
                                if (resultado.equals("Operacion realizada con exito.")) {
                                    error_grabar = false;
                                } else {
                                    error_grabar = true;
                                }
                                
                            } else {
                                Mensaje.manipulacionExcepciones("critico", "El total de la factura no puede ser cero. Por favor rectifique.", "Total Cero");
                            }
                            
                        } else {
                            Mensaje.manipulacionExcepciones("critico", "No se pudo insertar Factura por errores en el Encabezado.", "Boton Guardar");
                        }

                        //Obtener ID de la Factua ingresada despues de almacenarla
                        try {
                            
                            int BuscarFactura = NuevaFactura.seleccionarIdFactura(Factura, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                            
                            if (BuscarFactura == 0) {
                                
                                if (error_grabar != true) {
                                    Mensaje.manipulacionExcepciones("critico", "Existe un problema con los datos ingresados en el Encabezado.", "Boton Guardar");
                                }
                                
                            } else {
                                txtIdFactura.setText(String.valueOf(BuscarFactura));
                            }
                            
                        } catch(Exception Error) {
                            agregarLog("Ocurrio un error al buscar el ID de la Factura. \nError: " + Error.toString());
                            Mensaje.manipulacionExcepciones("critico", Error.getMessage(), "Seleccionar ID Factura");
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
                                            Detalle.setPrecio_estandar_d_factura(Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 7).toString()));
                                            Detalle.setSub_total_d_factura(Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 8).toString()));
                                            Detalle.setComentario_d_factura(tblDetalleFactura.getValueAt(cFilas, 9).toString());
                                            Detalle.setArancel_d_factura(Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 10).toString()));

                                            if (Float.parseFloat(txtTotalFactura.getText()) > 0.00) {
                                                
                                                String resultado_inserta_detalle = NuevoDetalleFactura.insertarDetalleFactura(Detalle, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                                                
                                                //Si el dato fue insertado en la base de datos entonces descontar producto
                                                if(resultado_inserta_detalle.equals("Operacion realizada con exito.")) {
                                                    
                                                    //Verificar si existe el trigger que actualiza la existencia
                                                    //en caso que exista se omite la acutalización por medio de la consulta
                                                    if (Trigger.equals("SI")) {
                                                        agregarLog("La existencia se actualizará por medio del Trigger");
                                                        tblDetalleFactura.setValueAt("SI", cFilas, 11);
                                                    } else {
                                                        String resultado = descontarProducto(Detalle.getId_producto(), Detalle.getCantidad_d_factura());
                                                        if (resultado.equals("Operacion realizada con exito.")) {
                                                            tblDetalleFactura.setValueAt("SI", cFilas, 11);
                                                        } else {
                                                            tblDetalleFactura.setValueAt("NO", cFilas, 11);
                                                        }
                                                    }
                                                    
                                                } else {
                                                    
                                                    Mensaje.manipulacionExcepciones("critio", "Ocurrio un error inesperado al insertar el detalle de la factura para el Producto " + Detalle.getId_producto() + ".", "Error al Insertar Detalle");
                                                    
                                                }
                                                
                                            }

                                        } catch (Exception Error) {
                                            agregarLog("Error al grabar el detalle. \nError: " + Error.toString());
                                            filas = filas + "[" + String.valueOf(cFilas + 1) + "] ";
                                            error_grabar = true;
                                        }
                                        
                                    }
                                    
                                }
                                
                            }
                            

                            if(error_grabar == false){
                                
                                boolean habilita_fel = false;
                                
                                if(archivo.leer("[FEL]").equals("TRUE")) {
                                    habilita_fel = true;
                                } else {
                                    habilita_fel = false;
                                }
                                
                                btnGeneraFEL.setEnabled(habilita_fel);
                                
                                
                                btnGuardar.setEnabled(false);
                                btnEliminar.setEnabled(false);
                                btnBuscar.setEnabled(false);
                                btnBuscarProducto.setEnabled(false);
                                txtBuscar.setEnabled(false);
                                btnAgregar.setEnabled(false);
                                Mensaje.manipulacionExcepciones("informacion","Venta guardada exitosamente.", "Boton Guardar");
                                
                                //ALmacenar los famosos datos de FEL siempre y cuando exista el ID Factura
                                if (!txtIdFactura.getText().isEmpty()) {
                                    AccesoFacturaFEL FEL = new AccesoFacturaFEL();
                                    ObjetosFacturaFel FAC = new ObjetosFacturaFel();
                                    FAC.setId_factura(Integer.parseInt(txtIdFactura.getText()));
                                    FAC.setSerie_factura_fel(txtSerieFacturaFEL.getText().trim());
                                    FAC.setNumero_factura_fel(txtNumFacturaFEL.getText().trim());
                                    FAC.setAutorizacion_factura_fel(txtAutorizacionFEL.getText().trim());
                                    FAC.setTipo_documento_factura_fel(cbxTipoDocumento.getSelectedItem().toString());
                                    FEL.insertarFacturaFEL(FAC, Inventory.lblUsuario.getText(), Inventory.lblTerminal.getText());
                                }
                                
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
        
        } else {
            Mensaje.manipulacionExcepciones("critico", "Ingreso los datos de Contado o de Credito en los campos correspondientes", "Calculo de Cambio");
        }
        
    }
    
    public void buscarInformacionPersonaPorNIT() {
        
        System.out.println("Validando datos del cliente");

        if (txtNitPersona.getText().trim().equals("c/f") || txtNitPersona.getText().equals("C/F") || txtNitPersona.getText().trim().equals("")) {

            System.out.println("El cliente fue consumidor final");
            
            try {
                System.out.println("Colocando valores para consumidor final");
                this.txtNitPersona.setText("C/F");
                this.txtCategoriaCliente.setText("C");
                this.txtNombrePersona.setText("Consumidor Final");
                this.txtTelCliente.setText("0");
                this.txtDirCliente.setText("Ciudad");
                this.txtFechaEmisionFactura.setText((1900 + fecha.getYear()) + "-" + (fecha.getMonth() + 1) + "-" + fecha.getDate());
                this.txtTotalFactura.setText("0.00");
                System.out.println("Colocando ID Persona 1");
                this.txtIdCliente.setText("1");
                this.txtIdPersona.setText("1");
                this.txtDirEnvioFactura.requestFocus();
                this.cbxDiasCredito.removeAllItems();
                this.cbxDiasCredito.addItem(0);
                this.cbxDiasCredito.setEnabled(false);
                this.cbxTipoVenta.setSelectedIndex(0);
                this.cbxTipoVenta.setEnabled(true);
                
            } catch (Exception e) {
                Mensaje.manipulacionExcepciones("critico", e.toString(), "Colocar ID Persona");
            }
            
        } else {

            obtenerDatosCliente(txtNitPersona.getText());

        }

        txtNitSinGuion.setText(txtNitPersona.getText().toUpperCase().replaceAll("-", ""));

        btnGuardar.setEnabled(true);

        totalFactura();
        
    }
    
    private void txtNitPersonaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNitPersonaFocusLost
        System.out.println("Inicia busqueda de informacion");
        buscarInformacionPersonaPorNIT();
    }//GEN-LAST:event_txtNitPersonaFocusLost

    public void totalFactura() {

        total_factura = 0;
        total_bruto = 0;
        
        float b_cantidad = 0;
        float b_precio_estandar = 0;
        float b_sub_total = 0; 
        float b_total_bruto = 0;
        
        agregarLog("Calculando el total de la Factura.");
        
        for (int cFilas = 0; cFilas < detalle.getRowCount(); cFilas++) {
            
            if (tblDetalleFactura.getValueAt(cFilas, 8) != null) {
                
                try {
                    
                    b_cantidad = Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 3).toString());
                    b_precio_estandar = Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 7).toString());
                    b_sub_total = b_cantidad * b_precio_estandar;
                    
                    total_factura = total_factura + Float.parseFloat(tblDetalleFactura.getValueAt(cFilas, 8).toString());
                    b_total_bruto = b_total_bruto + b_sub_total;
                    
                } catch (Exception error) {
                    
                    total_factura = total_factura + 0;
                    b_total_bruto = b_total_bruto + 0;
                    Mensaje.manipulacionExcepciones("critio", "Error al calcular el total de la Factura. \nError: " + error.getMessage(), "Calcular Total");
                    agregarLog("Error al calcular el total: " + error.toString());
                    
                }
                
            }
            
        }

        agregarLog("Total: " + total_factura);
        agregarLog("Total sin Descuento: " + b_sub_total);
        
        try {
            txtTotalFactura.setText(String.valueOf(matematica.redondear(total_factura)));
            txtTotalBrutoFactura.setText(String.valueOf(matematica.redondear(b_total_bruto)));
        } catch (Exception Error) {
            Mensaje.manipulacionExcepciones("critico", "Error al asinar el Total de la Factura. \nError: " + Error.getMessage(), "Asignar Total");
            agregarLog("Error al asignar el total: " + Error.toString());
        }

        totalIva();

    }
    
    private void totalIva(){
        
        String iva = "0.12";
        
        try {
            iva = archivo.leer("[IVA]");
        } catch(IOException exception) {
            System.out.println("No se pudieron leer los datos para el IVA");
            System.out.println(exception.toString());
        }
        
        
        try {
            agregarLog("Calculanto el IVA por pagar.");
            float v_iva = Float.parseFloat(iva) + 1;
            Double total_iva = total_factura - (total_factura / 1.12);
            txtIva.setText(String.valueOf(matematica.redondear(total_iva)));
            agregarLog("IVA: " + total_iva);
        } catch(Exception Error) {
            Mensaje.manipulacionExcepciones("critico", "Error al calcular el iva a pagar.", "Calcular IVA");
            agregarLog("Error calcular IVA: " + Error.toString());
        }
        
    }
    
    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        this.dispose();
        wdwMovimientoVentaDeProductosNueva VentaDeProductos = new wdwMovimientoVentaDeProductosNueva();
        ventana.abrirPantalla(VentaDeProductos);
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
            agregarLog("Removiendo fila " + tblDetalleFactura.getSelectedRow() + ".");
            detalle.removeRow(tblDetalleFactura.getSelectedRow());
            agregarLog("Fila removida.");

            agregarLog("Reacondicionando filas.");
            for (int c = 0; c < detalle.getRowCount(); c++) {
                detalle.setValueAt(c + 1, c, 0);
            }
            tblDetalleFactura.setModel(detalle);
            agregarLog("Fila eliminada exitosamente.");
        }catch(Exception error){
            Mensaje.manipulacionExcepciones("critico", "Se ha eliminado una fila.", "Boton Eliminar");
            agregarLog("Error al eliminar la fila: " + error.toString());
        }
        
        totalFactura();
        
    }//GEN-LAST:event_btnEliminarActionPerformed

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

        txtPagoContado.setText("");
        txtPagoCredito.setText("");
        txtContado.setText("");
        txtCredito.setText("");
        txtCambio.setText("");

        if(cbxTipoVenta.getSelectedItem().toString().equals("Contado")){
            
            txtPOS.setBackground(new java.awt.Color(255, 255, 255));
            txtPagoCredito.setBackground(new java.awt.Color(255, 255, 255));
            txtRecibidoPor.setBackground(new java.awt.Color(255, 255, 255));
            txtPagoContado.setBackground(new java.awt.Color(255, 255, 204));
            
            txtPOS.setText("");
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
                //txtRecibidoPor.setText(txtNombrePersona.getText());
               
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
                archivo_jasper = "rptValeMediaCartaNormal";
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
            this.tblProducto.setDefaultRenderer(Object.class, new NormalCell());
        } else {
            this.tblProducto.setDefaultRenderer(Object.class, new CustomCell());
        }
        
        this.txtIdProducto.setText(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 0).toString());
        this.txtDescripcionProducto.setText(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 2).toString());
        this.txtPrecioCompra.setText(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 7).toString());
        this.txtPrecioVenta.setText(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 8).toString());
        this.txtPrecioNormal.setText(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 8).toString());
        this.txtPrecioMinimo.setText(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 9).toString());
        this.txtPermiteDescuento.setText(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 13).toString());

        if (this.cbxTipoDeCliente.getSelectedItem().toString().equals("Cliente Mayorista")) {
            if (this.txtPermiteDescuento.getText().equals("SI")) {
                this.txtPrecioVenta.setText(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 9).toString());
            } else {
                this.txtPrecioVenta.setText(tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 8).toString());
            }
        }

        txtCantidadVenta.setText("1");

        float minimo_existencia = Float.parseFloat(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 11).toString());
        float existencia_actual = Float.parseFloat(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 5).toString());

        //validar si ya esta en el minimo este producto
        if (existencia_actual < minimo_existencia) {
            agregarLog("El Producto esta al mínimo.");
            this.txtCantidadMinima.setBackground(Color.RED);
            this.txtCantidadMinima.setText(String.valueOf(existencia_actual));
        } else {
            agregarLog("El Producto tiene existencia.");
            this.txtCantidadMinima.setBackground(Color.DARK_GRAY);
            this.txtCantidadMinima.setText(String.valueOf(existencia_actual));
        }
        
        //colocar el valor del precio especial
        this.txtPrecioEspecial.setText(this.tblProducto.getValueAt(this.tblProducto.getSelectedRow(), 14).toString());

        //calcular el valor del producto con el descuento seleccionado
        calcular_descuento();

    }//GEN-LAST:event_tblProductoMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        
        //Colocar oculto el valor del precio de compra
        if ("Administrador".equals(rolUsuario) || "Jefe Sucursal".equals(rolUsuario)) {
            this.tblDetalleFactura.setDefaultRenderer(Object.class, new SaveCellAdministrador());
        } else {
            tblDetalleFactura.setDefaultRenderer(Object.class, new SaveCellVendedor());
        }
        
        agregarLog("Presionó el botón para agregar Productos.");
        try {
            agregarLog("Iniciando el método para agregar Productos al detalle de la Venta.");
            agregarProductoAlDetalle();
        } catch(Exception Error) {
            agregarLog("Calculos para el detalle: " + Error.toString());
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
        
        //validaciones del precio ingresado que este en el rango excepto si el usuario no es administrador
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
                    precio_venta_v = Float.parseFloat(this.txtPrecioVenta.getText().trim());
                    agregarLog("Capturando el precio de Venta " + precio_venta_v + ".");
                } catch(Exception Error) {
                    Mensaje.manipulacionExcepciones("critcio", "Ocurrio un error al verificar el precio de Venta.", "Precio Venta");
                    agregarLog(Error.toString());
                }
                
                try {
                    precio_minimo_v = Float.parseFloat(this.txtPrecioMinimo.getText().trim());
                    agregarLog("Capturando el precio mínimo de la Venta " + precio_minimo_v + ".");
                } catch(Exception Error) {
                    Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al verficar el precio Minimo", "Precio Minimo");
                    agregarLog(Error.toString());
                }
                
                try {
                    precio_normal_v = Float.parseFloat(this.txtPrecioNormal.getText().trim());
                    agregarLog("Capturando el precio normal del Producto " + precio_normal_v + ".");
                } catch(Exception Error) {
                    Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al verficar el precio Nomral", "Precio Normal");
                    agregarLog(Error.toString());
                }
                
                if ( ( precio_venta_v >= precio_minimo_v && precio_venta_v <= precio_normal_v) 
                        || Inventory.lblRol.getText().equals("Administrador") 
                        || Inventory.lblRol.getText().equals("Jefe Sucursal") ) {

                    //calcular el valor del producto con el descuento seleccionado
                    agregarLog("Iniciando variables para agregar la linea nueva al Detalle.");
                    float descuento = 0;
                    double precio_normal = 0;
                    double precio_venta = 0;
                    double precio_descuento = 0;
                    
                    
                    try {
                        if(this.txtCategoriaCliente.getText().equals("A")) {
                            descuento = 1;
                        } else {
                            float valor_descuento = (Float.parseFloat(this.cbxDescuento.getSelectedItem().toString()) / 100);
                            descuento = 1 - valor_descuento;
                            agregarLog("Capturando el descuento para el Producto " + descuento);
                        }
                    } catch (Exception Error) {
                        Mensaje.manipulacionExcepciones("critico", "Ocurrio un errror al verificar el Descuento.", "Verificar Descuento");
                    }

                    try {
                        if (this.txtCategoriaCliente.getText().equals("A")) {
                            agregarLog("Al ser Cliente tipo A se le dará el precio especial.");
                            if (this.txtPrecioEspecial.getText().equals("0.0")) {
                                precio_venta = matematica.aproxima(Double.parseDouble(this.txtPrecioVenta.getText().trim()), 2);
                            } else {
                                precio_venta = matematica.aproxima(Double.parseDouble(this.txtPrecioEspecial.getText().trim()), 2);
                            }
                        } else {
                            precio_venta = matematica.aproxima(Double.parseDouble(this.txtPrecioVenta.getText().trim()), 2);
                        }
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
                        agregarLog("Cantidad: " + this.txtCantidadVenta.getText().trim());
                        double total = matematica.aproxima(Float.parseFloat(txtCantidadVenta.getText().trim()) * precio_descuento, 2);
                        agregarLog("Subtotal : " + total);

                        try {
                            //enviar datos al detalle de la pantalla de ventas con los datos del producto
                            agregarLog("Agregando la linea al detalle de la Venta.");
                            Object[] datos = new Object[12];
                            try { datos[0] = detalle.getRowCount() + 1; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Fila Seleccionada \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[1] = txtIdProducto.getText(); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "ID Producto \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[2] = txtDescripcionProducto.getText(); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Descripcion \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[3] = Float.parseFloat(txtCantidadVenta.getText().trim()); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Cantidad Venta \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[4] = Float.parseFloat(txtPrecioCompra.getText()); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Compra \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[5] = matematica.aproxima(precio_descuento, 2); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Descuento \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[6] = Float.parseFloat(txtPrecioMinimo.getText().trim()); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Minimo \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[7] = Float.parseFloat(txtPrecioNormal.getText().trim()); } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Normal \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[8] = total; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Total \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[9] = "S/C"; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Comentario \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[10] = 0.0; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Arancel \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            try { datos[11] = "NO"; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Guardado \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                            detalle.addRow(datos);
                            tblDetalleFactura.setModel(detalle);

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

    private void btnSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerieActionPerformed
        
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
        
    }//GEN-LAST:event_btnSerieActionPerformed

    private void txtNitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNitKeyPressed
        txtNitSinGuion.setText(txtNitPersona.getText().toUpperCase().replaceAll("-", ""));
    }//GEN-LAST:event_txtNitKeyPressed

    private void btnAutorizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutorizacionActionPerformed
        
        wdwMovimientoAutorizacionVenta autorizacion = new wdwMovimientoAutorizacionVenta(detalle);
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
        wdwMovimientoAutorizacionVenta.txtDescripcion.setText(txtDescripcionProducto.getText());
        wdwMovimientoAutorizacionVenta.txtPrecioCompra.setText(txtPrecioCompra.getText());
        wdwMovimientoAutorizacionVenta.txtMinimo.setText(txtPrecioMinimo.getText());
        wdwMovimientoAutorizacionVenta.txtNormal.setText(txtPrecioNormal.getText());
        
        totalFactura();
        
    }//GEN-LAST:event_btnAutorizacionActionPerformed

    private void bntActivarPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntActivarPosActionPerformed
        txtPOS.setEnabled(true);
        if(cbxTipoVenta.getSelectedItem().toString().equals("Contado")) {
            txtPagoContado.setText(txtTotalFactura.getText());
        }
    }//GEN-LAST:event_bntActivarPosActionPerformed

    private void btnReconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReconectarActionPerformed
        
        this.dispose();
        wdwMovimientoVentaDeProductosNueva VentaDeProductos = new wdwMovimientoVentaDeProductosNueva();
        ventana.abrirPantalla(VentaDeProductos);

        //Trasladar toda la informacion del ecabezado a la pantalla nueva
        VentaDeProductos.txtNitPersona.setText(this.txtNitPersona.getText());
        VentaDeProductos.buscarInformacionPersonaPorNIT();
        VentaDeProductos.txtCategoriaCliente.setText(this.txtCategoriaCliente.getText());
        //Los dias credito no se pueden enviar por el lost focus de txtNitPersona
        VentaDeProductos.txtNombrePersona.setText(this.txtNombrePersona.getText());
        VentaDeProductos.txtTelCliente.setText(this.txtTelCliente.getText());
        VentaDeProductos.cbxTipoDeCliente.setSelectedIndex(this.cbxTipoDeCliente.getSelectedIndex());
        VentaDeProductos.txtDirCliente.setText(this.txtDirCliente.getText());
        VentaDeProductos.txtDirEnvioFactura.setText(this.txtDirEnvioFactura.getText());
        VentaDeProductos.txtMetodoEntrada.setText(this.txtMetodoEntrada.getText());
        VentaDeProductos.txtNitSinGuion.setText(this.txtNitSinGuion.getText());
 
        VentaDeProductos.cbxEmpleado.setSelectedItem(this.cbxEmpleado.getSelectedItem());
        VentaDeProductos.cbxTipoVenta.setSelectedItem(this.cbxTipoVenta.getSelectedItem());
        VentaDeProductos.txtIdEmpleado.setText(this.txtIdEmpleado.getText());
        VentaDeProductos.cbxEstadoVenta.setSelectedItem(this.cbxEstadoVenta.getSelectedItem());
        VentaDeProductos.txtIva.setText(this.txtIva.getText());
        VentaDeProductos.txtPagoCredito.setText(this.txtPagoCredito.getText());
        VentaDeProductos.txtComision.setText(this.txtComision.getText());
        VentaDeProductos.txtPagoContado.setText(this.txtPagoContado.getText());
        VentaDeProductos.txtPOS.setText(this.txtPOS.getText());
        VentaDeProductos.txtCambio.setText(String.valueOf(calcularVuelto()));
        //VentaDeProductos.txtCredito.setText(this.txtCredito.getText());
        //VentaDeProductos.txtContado.setText(this.txtContado.getText());
        
        //VentaDeProductos.txtIdFactura.setText(this.txtIdFactura.getText());
        VentaDeProductos.txtFechaEmisionFactura.setText(this.txtFechaEmisionFactura.getText());
        VentaDeProductos.txtSerieFacturaFEL.setText(this.txtSerieFacturaFEL.getText());
        VentaDeProductos.txtNumFacturaFEL.setText(this.txtNumFacturaFEL.getText());
        VentaDeProductos.txtSerieFactura.setText(this.txtSerieFactura.getText());
        VentaDeProductos.txtNumFactura.setText(this.txtNumFactura.getText());
        VentaDeProductos.txtAutorizacionFEL.setText(this.txtAutorizacionFEL.getText());
        VentaDeProductos.txtFolioFactura.setText(this.txtFolioFactura.getText());
        VentaDeProductos.txtTotalFactura.setText(this.txtTotalFactura.getText());
        VentaDeProductos.txtRecibidoPor.setText(this.txtRecibidoPor.getText());
        
        //Trasladar toda la informacion del detalle para hacer los calculos
        VentaDeProductos.detalle = this.detalle;
        VentaDeProductos.tblDetalleFactura.setModel(this.detalle);
        
        //Coloar el cambio en blanco
        VentaDeProductos.txtCambio.setText("");
        
        VentaDeProductos.totalFactura();
        VentaDeProductos.totalIva();
        
        Mensaje.manipulacionExcepciones("informacion", "Recuerde seleccionar DIAS CREDITO si la venta no es al contado.", "Verifique Dias Crédito");
        
    }//GEN-LAST:event_btnReconectarActionPerformed

    private void btnGeneraFELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneraFELActionPerformed
        generaFacturaElectronicaFEL();
    }//GEN-LAST:event_btnGeneraFELActionPerformed

    private void btnRecargoPropinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecargoPropinaActionPerformed

        agregarLog("Porcentage Recargo: " + this.porcentaje_recargo);

        //Valida que se acepten recargos a la factura
        agregarLog("Verificando que se acepten recargos a la Venta.");
        if (acepta_recargo.equals("NO")) {

            Mensaje.manipulacionExcepciones("critico", "No se aceptan recargos en las ventas", "Boton Agregar Recargo");

        } else {

            try {

                agregarLog("Agregando la linea de Recargo de la Venta.");

                double valor_porcentaje_factura = Double.parseDouble(this.porcentaje_recargo) / 100;
                double total_recargo_factura_con_dcimales = matematica.aproxima(Double.parseDouble(String.valueOf(this.total_factura * valor_porcentaje_factura)), 2);

                float total_recargo_factura = 0;

                if (redondea_decimales_recargo.equals("SI")) {
                    agregarLog("Redondear Propína");
                    total_recargo_factura = (float) matematica.aproxima(total_recargo_factura_con_dcimales, 0);
                } else {
                    total_recargo_factura = (float) total_recargo_factura_con_dcimales;
                }

                try {
                    //enviar datos al detalle de la pantalla de ventas con los datos del producto
                    Object[] datos = new Object[12];
                    try { datos[0] = detalle.getRowCount() + 1; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Fila Seleccionada \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[1] = this.codigo_recargo; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "ID Producto \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[2] = this.descripcion_recargo; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Descripcion \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[3] = 1; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Cantidad Venta \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[4] = 0; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Compra \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[5] = total_recargo_factura; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Descuento \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[6] = 1; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Minimo \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[7] = total_recargo_factura; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Precio Normal \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[8] = total_recargo_factura; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Total \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[9] = "S/C"; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Comentario \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[10] = 0.0; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Arancel \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    try { datos[11] = "NO"; } catch (Exception Error) { Mensaje.manipulacionExcepciones("critico", "Guardado \nError: " + Error.getMessage(), "Dato No. " + detalle.getRowCount() + 1); }
                    detalle.addRow(datos);
                    tblDetalleFactura.setModel(detalle);

                } catch (Exception e) {
                    Mensaje.manipulacionExcepciones("critico", "Ocurrio al asignar los valores del recargo al detalle. \n" + e.getMessage(), "Error Asiganción");
                    agregarLog("Error al agregar Recargo al detalle: " + e.toString());
                }

            } catch (Exception Error) {
                Mensaje.manipulacionExcepciones("critico", "Ocurrio un error al validar el Recargo.", "Validar Recargo");
                agregarLog("Error al validar el recargo de la Venta");
                agregarLog(Error.getMessage());
            }

        }

        totalFactura();
        limpiarBusqueda();
    }//GEN-LAST:event_btnRecargoPropinaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntActivarPos;
    public static javax.swing.JButton btnAgregar;
    public static javax.swing.JButton btnAutorizacion;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnFactura;
    private javax.swing.JButton btnGeneraFEL;
    private javax.swing.JButton btnGuardar;
    public static javax.swing.JButton btnMostrarImagen;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnRecargoPropina;
    private javax.swing.JButton btnReconectar;
    private javax.swing.JButton btnSerie;
    private javax.swing.JComboBox cbxDescuento;
    private javax.swing.JComboBox cbxDiasCredito;
    private javax.swing.JComboBox cbxEmpleado;
    private javax.swing.JComboBox cbxEstadoVenta;
    private javax.swing.JComboBox cbxTamanoImpresion;
    private javax.swing.JComboBox cbxTipoBusqueda;
    private javax.swing.JComboBox cbxTipoDeCliente;
    private javax.swing.JComboBox<String> cbxTipoDocumento;
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
    private javax.swing.JTextField txtCategoriaCliente;
    public static javax.swing.JTextField txtComision;
    public static javax.swing.JTextField txtContado;
    public static javax.swing.JTextField txtCredito;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtDirCliente;
    private javax.swing.JTextField txtDirEnvioFactura;
    private javax.swing.JTextField txtFechaEmisionFactura;
    private javax.swing.JTextField txtFolioFactura;
    private javax.swing.JTextField txtIdBusqueda;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JTextField txtIdFactura;
    private javax.swing.JTextField txtIdPersona;
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
    private javax.swing.JTextField txtPrecioEspecial;
    private javax.swing.JFormattedTextField txtPrecioMinimo;
    private javax.swing.JFormattedTextField txtPrecioNormal;
    private javax.swing.JFormattedTextField txtPrecioVenta;
    public static javax.swing.JTextField txtRecibidoPor;
    private javax.swing.JTextField txtSerieFactura;
    private javax.swing.JTextField txtSerieFacturaFEL;
    private javax.swing.JTextField txtTelCliente;
    private javax.swing.JComboBox<String> txtTipoConexionFEL;
    public static javax.swing.JTextField txtTotalBrutoFactura;
    public static javax.swing.JTextField txtTotalFactura;
    // End of variables declaration//GEN-END:variables
}
