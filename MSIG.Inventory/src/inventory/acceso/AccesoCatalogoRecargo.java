package inventory.acceso;

import inventory.objetos.ObjetosRecargo;
import inventory.vistas.wdwCatalogoMarcas;
import inventory.vistas.wdwCatalogoRecargo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AccesoCatalogoRecargo {

    private AccesoInventario acceso = new AccesoInventario();
    private wdwCatalogoRecargo wdw_recargo;
    private DefaultTableModel modeloTabla = new DefaultTableModel();
    
    public AccesoCatalogoRecargo(wdwCatalogoRecargo wdwRecargo) {
        try {
            this.wdw_recargo = wdwRecargo;
            modeloTabla.addColumn("ID");
            modeloTabla.addColumn("Abreviatura");
            modeloTabla.addColumn("Descripcion");
            modeloTabla.addColumn("Valor %");
            modeloTabla.addColumn("Estado");
            this.wdw_recargo.tabla_categ.setModel(modeloTabla);
        } catch (Exception error) {
            System.err.println("El parametro es nulo, posiblemente no se este utilizando el catalogo");
        }
    }

    public void guardarRecargo(String abreviatura, String descripcion, String valor, String estado, String pUsuario, String pTerminal) {
        if (!descripcion.equals("") || !valor.equals("")) {
            
            PreparedStatement pst;
            int exitoso = 0;
            String sql = "INSERT INTO m_recargo ( abreviatura, descripcion, valor, estado ) VALUES (?,?,?,?);";

            try {
                pst = acceso.prepararConsulta(sql, "Recargo", "Guardar", pUsuario, pTerminal);
                pst.setString(1, abreviatura);
                pst.setString(2, descripcion);
                pst.setString(3, valor);
                pst.setString(4, estado);
                exitoso = pst.executeUpdate();
                if (exitoso == 1) {
                    JOptionPane.showMessageDialog(wdw_recargo, "Recargo registrado con éxito.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_recargo, "Errror al registrar un Recargo. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }

        } else {
            JOptionPane.showMessageDialog(wdw_recargo, "Debe llenar todos los campos");
        }
    }

    public void modificarRecargo(String abreviatura, String id, String descripcion, String valor, String estado) {
            PreparedStatement pst;
            String sql = "UPDATE m_recargo SET abreviatura = '" + abreviatura + "', descripcion = '" + descripcion + "'"
                    + ",estado ='" + estado + "', valor = " + valor + " WHERE id_recargo ='" + id + "'";
            int modificado = 0;

            try {
                pst = acceso.conectar().prepareStatement(sql);
                modificado = pst.executeUpdate();
                if (modificado == 1) {
                    JOptionPane.showMessageDialog(wdw_recargo, "Modificación exitosa.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_recargo, "Error al modificar Recargo. \n" + e);
            } finally {
                acceso.desconectar();
            }
    }
    
    public void cargarDatosAlmacenados() {
        
        PreparedStatement pst;
        ResultSet resultados;
        String sql = "SELECT * FROM m_recargo ORDER BY id_recargo DESC";

        try {
            pst = acceso.conectar().prepareStatement(sql);
            resultados = pst.executeQuery();
            modeloTabla.setRowCount(0);
            while (resultados.next()) {
                modeloTabla.addRow(new Object[]{
                    resultados.getString("id_recargo"),
                    resultados.getString("abreviatura"),
                    resultados.getString("descripcion"),
                    resultados.getString("valor"),
                    resultados.getString("estado")});
            }
        } catch (SQLException e) {
        } finally {
            acceso.desconectar();
        }
        
    }

    public void buscarRecargoSeleccionado(String id) {
        PreparedStatement pst;
        ResultSet datos;
        String sql = "SELECT * FROM m_recargo WHERE id_recargo = '" + id + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();
            if (datos.first()) {
                wdw_recargo.txtIdRecargo.setText(datos.getString("id_recargo"));
                wdw_recargo.txtAbreviatura.setText(datos.getString("abreviatura"));
                wdw_recargo.txtDescripcion.setText(datos.getString("descripcion"));
                wdw_recargo.cbxEstado.setSelectedItem(datos.getString("estado"));
                wdw_recargo.txtValor.setText(datos.getString("valor"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_recargo, "Error al cargar Recargos. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
    }
    
    public void buscarRecargoPorDescripcion(String pDescripcion) {
        PreparedStatement pst;
        ResultSet datos;
        String sql = "SELECT * FROM m_recargo WHERE UPPER(descripcion) = '%" + pDescripcion.toUpperCase() + "%'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();
            if (datos.first()) {
                wdw_recargo.txtIdRecargo.setText(datos.getString("id_recargo"));
                wdw_recargo.txtAbreviatura.setText(datos.getString("abreviatura"));
                wdw_recargo.txtDescripcion.setText(datos.getString("descripcion"));
                wdw_recargo.cbxEstado.setSelectedItem(datos.getString("estado"));
                wdw_recargo.txtValor.setText(datos.getString("valor"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_recargo, "Error al cargar Recargos. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
    }
    
    public ObjetosRecargo buscarRecargoPorId(String id) {
        PreparedStatement pst;
        ResultSet datos;
        ObjetosRecargo recargo = new ObjetosRecargo();; 
        String sql = "SELECT * FROM m_recargo WHERE id_recargo = '" + id + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();
            if (datos.first()) {
                recargo.setId_recargo(datos.getInt("id_recargo"));
                recargo.setAbreviatura(datos.getString("abreviatura"));
                recargo.setDescripcion(datos.getString("descripcion"));
                recargo.setEstado(datos.getString("estado"));
                recargo.setValor(datos.getDouble("valor"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_recargo, "Error al buscar Recargos. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
        return recargo;
    }
    
    public ArrayList<ObjetosRecargo> retornaRecargosActivos(String pUsuario, String pTerminal) {

        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select * from m_recargo where estado = 'Activo'";

        try {
            ResultSet tabla = acceso.listarRegistros(sql, "Recargo", "Retorna todos los recargos", pUsuario, pTerminal);
            ObjetosRecargo registros;

            while (tabla.next()) {
                registros = new ObjetosRecargo();
                registros.setId_recargo(tabla.getInt("id_recargo"));
                registros.setAbreviatura(tabla.getString("abreviatura"));
                registros.setDescripcion(tabla.getString("descripcion"));
                registros.setValor(tabla.getDouble("valor"));
                lista.add(registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return lista;
    }
    
}