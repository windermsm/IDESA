package inventory.acceso;

import inventory.objetos.ObjetosAbonoProveedor;
import inventory.vistas.wdwMovimientoAbonoProveedor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AccesoAbonoProveedor {

    private ObjetosAbonoProveedor objetoAbono;
    private ObjetosAbonoProveedor objetoDetalles;
    private wdwMovimientoAbonoProveedor wdw_abono;
    private AccesoInventario acceso = new AccesoInventario();
    private DefaultTableModel modeloTabla = new DefaultTableModel();
    private String idpersona;
    private String idproveedor;
    private String personaNit;
    private String nit;
    //respaldar los ids de compras registradas al proveedor buscado
    private ArrayList data = new ArrayList();
    //datos completos a presentar en pantalla
    private ArrayList<ObjetosAbonoProveedor> datosCompra = new ArrayList();
    private static AccesoExcepciones mensaje = new AccesoExcepciones();

    public AccesoAbonoProveedor(wdwMovimientoAbonoProveedor wdw_abono, ObjetosAbonoProveedor objeto) {
        this.wdw_abono = wdw_abono;
        this.objetoAbono = objeto;
        this.wdw_abono.txtFechaAbono.setDate(CovertirDate(this.fechaActual()));
        this.habilitarTablaAbono();
    }
    
    public Date CovertirDate(String valor) {
        Date fecha = null;
        try {
            if (valor == null || valor.equals("")) {
                fecha = null;
            } else {
                fecha = new SimpleDateFormat("yyyy-MM-dd").parse(valor);
            }
        } catch (Exception Error) {
            mensaje.manipulacionExcepciones("critico", "Error al convertir fecha. \n" + Error.getMessage(), "Convertir Fecha");
        }
        return fecha;
    }

    private void habilitarTablaDetalleAbono() {
        modeloTabla.setColumnCount(0);
        modeloTabla.addColumn("ID proveedor");
        modeloTabla.addColumn("NIT Proveedor");
        modeloTabla.addColumn("Proveedor");
        modeloTabla.addColumn("Saldo Pendiente");
        modeloTabla.addColumn("ID Compra");
        modeloTabla.addColumn("Fecha Abono");
        modeloTabla.addColumn("Serie");
        modeloTabla.addColumn("Numero");
        this.wdw_abono.tablaProveedor.setModel(modeloTabla);
    }
    
    private void habilitarTablaAbono() {
        modeloTabla.setColumnCount(0);
        //modeloTabla.addColumn("ID Abono");
        modeloTabla.addColumn("ID Proveedor");
        modeloTabla.addColumn("NIT proveedor");
        modeloTabla.addColumn("Proveedor");
        modeloTabla.addColumn("Saldo Pendiente");
        modeloTabla.addColumn("ID Compra");
        modeloTabla.addColumn("Fecha Compra");
        modeloTabla.addColumn("Serie");
        modeloTabla.addColumn("Numero");
        this.wdw_abono.tablaProveedor.setModel(modeloTabla);
    }

    private int validarPrimerAbono(String idprov, String nitprov, String idcompra) {
        PreparedStatement pst;
        ResultSet rs;
        String sql = "SELECT * from m_deuda WHERE id_proveedor='" + idprov + "' and nit_proveedor ='" + nitprov + "' "
                + "and id_compra = '" + idcompra + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.first()) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al validad el primer Abono. \n" + e);
            return 0;
        } finally {
            acceso.desconectar();
        }

    }

    public void registrarAbono(ObjetosAbonoProveedor objeto, String pUsuario, String pTerminal) {
        
        String sql = "INSERT INTO m_deuda (id_proveedor,id_compra,nombre_proveedor,"
                + "nit_proveedor,saldo_actual,monto_abonado,tipo_abono,fecha_abono) VALUES(?,?,?,?,?,?,?,?) ";
        
        String sql_actualizar = "UPDATE m_deuda SET id_proveedor ='" + objeto.getIdProveedor() + "',id_compra='"
                + objeto.getIdCompra() + "',nombre_proveedor ='" + objeto.getNombreProv() + "',nit_proveedor='" + objeto.getNitProveedor() + "',"
                + "saldo_actual ='" + objeto.getSaldoPendiente() + "',monto_abonado='" + objeto.getCantidadAbono() + "',tipo_abono='" + objeto.getTipoDoc() + "',"
                + "fecha_abono='" + objeto.getFechaAbono() + "' WHERE id_compra ='" + objeto.getIdCompra() + "'";
        
        PreparedStatement pst;
        int guardado = 0;
        int actualizado = 0;
        
        //verificar si ya existe el primer abono
        if (this.validarPrimerAbono(objeto.getIdProveedor(), objeto.getNitProveedor(),
                objeto.getIdCompra()) == 1) {

            try {
                pst = acceso.prepararConsulta(sql_actualizar, "Abono a Proveedores", "Guardar", pUsuario, pTerminal);
                actualizado = pst.executeUpdate();
                if (actualizado == 1) {
                    //limpiar casillas
                    this.limpiarCasillas();
                    //modificar el saldo en m_formas_pago
                    this.modificarSaldo(objeto.getIdCompra(), objeto.getSaldoPendiente());
                    //cargar la data de la tabla abono_proveedores
                    //this.buscarDetalleAbono(objeto.getIdProveedor(), objeto.getIdCompra(), objeto.getNitProveedor());
                    JOptionPane.showMessageDialog(wdw_abono, "La compra fue abonada con éxito.");
                    //registar historial de abono
                    this.registrarAbonoDetalle(objeto);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_abono, "Errora la actualizar Saldo. \n" + e);
            } finally {
                acceso.desconectar();
            }

        } else if (this.validarPrimerAbono(objeto.getIdProveedor(), objeto.getNitProveedor(), objeto.getIdCompra()) == 0) {
            //registrar el abono a la tabla y tambien modificar los saldos
            try {
                
                pst = acceso.prepararConsulta(sql, "Abono a Proveedores", "Guaradar", pUsuario, pTerminal);
                pst.setString(1, objeto.getIdProveedor());
                pst.setString(2, objeto.getIdCompra());
                pst.setString(3, objeto.getNombreProv());
                pst.setString(4, objeto.getNitProveedor());
                pst.setDouble(5, objeto.getSaldoPendiente());
                pst.setDouble(6, objeto.getCantidadAbono());
                pst.setString(7, objeto.getTipoDoc());
                pst.setString(8, objeto.getFechaAbono());
                guardado = pst.executeUpdate();
                if (guardado == 1) {
                    //limpiar casillas
                    this.limpiarCasillas();
                    //actualizar registro de saldo
                    this.modificarSaldo(objeto.getIdCompra(), objeto.getSaldoPendiente());
                    //cargar datos de la tabla de abonos
                    this.buscarDetalleAbono(objeto.getIdProveedor(), objeto.getIdCompra(), objeto.getNitProveedor());
                    JOptionPane.showMessageDialog(wdw_abono, "La cuenta fue abonada correctamente.");
                    //registrar el abono en la table de historiales
                    this.registrarAbonoDetalle(objeto);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_abono, "Error al ingresar el detalle del Abono. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }
        }

    }

    public void registrarAbonoDetalle(ObjetosAbonoProveedor objeto) {
        String sql = "INSERT INTO d_deuda (id_compra,id_proveedor,saldo_pendiente,"
                + "monto_abonado,fecha_abono,"
                + "banco_documento,num_documento,serie_documento,tipo_abono) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst;
        int guardado = 0;
        try {
            pst = acceso.conectar().prepareStatement(sql);
            pst.setString(1, objeto.getIdCompra());
            pst.setString(2, objeto.getIdProveedor());
            pst.setDouble(3, objeto.getSaldoPendiente());
            pst.setDouble(4, objeto.getCantidadAbono());
            pst.setString(5, objeto.getFechaAbono());
            pst.setString(6, objeto.getBanco());
            pst.setString(7, objeto.getNumeroDoc());
            pst.setString(8, objeto.getSerieDoc());
            pst.setString(9, objeto.getTipoDoc());
            guardado = pst.executeUpdate();
            if (guardado == 1) {
                System.out.println("Abono agregado al historial.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_abono, "Error al registrar el detalle del Abono. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
    }

    //actualizar el saldo de la tabla m_formas_pago
    private void modificarSaldo(String idCompra, double saldoP) {
        PreparedStatement pst;
        int modificado = 0;
        String sql = "UPDATE m_formas_pago SET credito ='" + saldoP + "' WHERE id_compra ='" + idCompra + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            modificado = pst.executeUpdate();
            if (modificado == 1) {
                //JOptionPane.showMessageDialog(wdw_abono, "saldo modificado");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_abono, "Error al modificar el Saldo. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
    }

    //cargar este metodo despues de insertar el abono
    private void buscarDetalleAbono(String idprov, String idcompra, String nit) {
        PreparedStatement pst;
        ResultSet rs;
        String sql = "SELECT * FROM m_deuda WHERE id_proveedor ='" + idprov + "'"
                + " and id_compra ='" + idcompra + "' and nit_proveedor ='" + nit + "'";

        modeloTabla.setRowCount(0);
        try {
            
            pst = acceso.conectar().prepareStatement(sql);
            rs = pst.executeQuery();
            
            if (rs.first()) {
                
                this.habilitarTablaDetalleAbono();
        
                modeloTabla.addRow(new Object[]{rs.getString("id_abono"), 
                    rs.getString("id_proveedor"),
                    rs.getString("nombre_proveedor"), 
                    rs.getString("nit_proveedor"), 
                    rs.getString("saldo_actual"),
                    rs.getString("monto_abonado"), 
                    rs.getString("id_compra"), 
                    rs.getString("tipo_abono"), 
                    rs.getString("fecha_abono")});
                
            } else {
                habilitarTablaAbono();
            }
            
        } catch (SQLException e) {
           
            JOptionPane.showMessageDialog(wdw_abono, "Error al buscar el detalle del Abono. \n" + e);
            habilitarTablaAbono();

        } finally {
            acceso.desconectar();
        }
        
    }

    //obtener el id persona e identificar el nombre del proveedor
    public void buscarPersona(String nit) {
        PreparedStatement pst;
        ResultSet registro;
        String sql = "SELECT * FROM m_persona WHERE nit_persona = '" + nit + "'";
        
        try {
            
            pst = acceso.conectar().prepareStatement(sql);
            registro = pst.executeQuery();
            
            if (registro.first()) {
                this.idpersona = registro.getString("id_persona");
                this.personaNit = registro.getString("nom_persona");
                this.nit = nit;
                this.buscarIdProveedor();
            } else {
                this.idpersona = null;
                this.personaNit = null;
                this.nit = null;
                JOptionPane.showMessageDialog(wdw_abono, "No se encontro el Proveedor.");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_abono, "Error al buscar Persona con este NIT. \n" + e);
        } finally {
            acceso.desconectar();
        }
    }

    //buscar el id del proveedor
    private void buscarIdProveedor() {
        PreparedStatement pst;
        ResultSet registro;
        String sql = "SELECT * FROM m_proveedor WHERE id_persona = '" + this.idpersona + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            registro = pst.executeQuery();
            if (registro.first()) {
                this.idproveedor = registro.getString("id_proveedor");
                this.buscarCompras(this.idproveedor);
            } else {
                this.idproveedor = null;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_abono, "Error al buscar al Proveedor. \n" + e);
        } finally {
            acceso.desconectar();
        }
    }

    //traer id de compras registradas al proveedor
    private void buscarCompras(String idprov) {
        
        PreparedStatement pst;
        String sql = "SELECT * FROM m_compra WHERE id_proveedor ='" + idprov + "'"
                + " AND total_factura_compra > 0";
        ResultSet compras;
        
        try {
            pst = acceso.conectar().prepareStatement(sql);
            compras = pst.executeQuery();
            data.clear();
            while (compras.next()) {
                data.add(compras.getString("id_compra"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_abono, "Ocurrio un error al buscar los registros de Compras. \n" + e);
        } finally {
            acceso.desconectar();
        }
    }

    //buscar saldo en todas las facturas encontradas de proveedor
    public void buscarFacturaPendiente() {
        
        PreparedStatement pst;
        ResultSet rs;
        datosCompra.clear();
        for (int i = 0; i < data.size(); i++) {
            
            String sql = "SELECT * FROM m_formas_pago WHERE id_compra ='" + data.get(i) + "' and credito >0";
            
            try {
                pst = acceso.conectar().prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.first()) {
                    objetoDetalles = new ObjetosAbonoProveedor();
                    objetoDetalles.setNitProveedor(this.nit);
                    objetoDetalles.setNombreProv(this.personaNit);
                    objetoDetalles.setIdProveedor(this.idproveedor);
                    objetoDetalles.setIdCompra(rs.getString("id_compra"));
                    objetoDetalles.setFechaCompra(this.buscarFechaCompra(rs.getString("id_compra")));
                    objetoDetalles.setSaldoPendiente(Double.parseDouble(rs.getString("credito")));
                    datosCompra.add(objetoDetalles);
                } else {
                    this.limpiarCasillas();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_abono, "Error al consultar Facturas pendientes. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }

        }
    }

    public void detallarCompra() {
        
        modeloTabla.setRowCount(0);
        this.habilitarTablaAbono();
        this.limpiarCasillas();
        
        for (int i = 0; i < this.datosCompra.size(); i++) {
            modeloTabla.addRow(new Object[]{
                datosCompra.get(i).getIdProveedor(),
                datosCompra.get(i).getNitProveedor(),
                datosCompra.get(i).getNombreProv(),
                datosCompra.get(i).getSaldoPendiente(), 
                datosCompra.get(i).getIdCompra(),
                datosCompra.get(i).getFechaCompra(),
                datosCompra.get(i).getSerieFactura(),
                datosCompra.get(i).getNumeroFactura()
            });
        }
        
    }

    private String buscarFechaCompra(String idcompra) {
        PreparedStatement pst;
        ResultSet rs;
        String sql = "SELECT * FROM m_compra WHERE id_compra ='" + idcompra + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.first()) {
                return rs.getString("fecha_factura_compra");
            } else {
                return "Sin Fecha";
            }
        } catch (SQLException e) {
            return null;
        } finally {
            acceso.desconectar();
        }

    }

    private String fechaActual() {
        Date date = new Date();
        String fecha = new SimpleDateFormat("YYYY-MM-dd").format(date);
        return String.valueOf(fecha);
    }

    private void limpiarCasillas() {
        wdw_abono.txtBancoDoc.setText("");
        wdw_abono.txtCantidadAbono.setText("");
        wdw_abono.txtIDproveedor.setText("");
        wdw_abono.txtNit.setText("");
        wdw_abono.txtIdcompra.setText("");
        wdw_abono.txtNoDoc.setText("");
        wdw_abono.txtNombre.setText("");
        wdw_abono.txtSaldo.setText("");
        wdw_abono.txtSerieDoc.setText("");

    }

    public int validarTipoAbono(String tipo, String banco, String numero) {
        if (tipo.equals("Cheque") && (banco.equals("") || numero.equals(""))) {
            JOptionPane.showMessageDialog(wdw_abono, "Completa la información del cheque.");
            return 0;
        } else if (tipo.equals("Tarjeta") && (banco.equals("") || numero.equals(""))) {
            JOptionPane.showMessageDialog(wdw_abono, "Completa la información de la tarjeta.");
            return 0;
        } else {
            return 1;
        }
    }
}