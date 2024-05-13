package inventory.acceso;

import inventory.vistas.wdwCatalogoFallas;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AccesoFallas {

    private AccesoInventario acceso = new AccesoInventario();
    private wdwCatalogoFallas wdw_fallas;
    private DefaultTableModel tablaModelo = new DefaultTableModel();

    public AccesoFallas(wdwCatalogoFallas wdw_fallas) {
        this.wdw_fallas = wdw_fallas;
        tablaModelo.setColumnCount(0);
        tablaModelo.addColumn("ID Falla");
        tablaModelo.addColumn("Descripcion");
        tablaModelo.addColumn("Categoría");
        tablaModelo.addColumn("ID Categoría");
        tablaModelo.addColumn("Estado");
        this.wdw_fallas.tabla_fallas.setModel(tablaModelo);
        this.llenarCombo();
    }

    public void modificar_falla(String idFalla, String titulo, String estado) {
        if (camposVaciosModificar(idFalla, titulo) == 1) {
            PreparedStatement pst;
            int actualizar = 0;
            String sql = "UPDATE m_falla SET nombre_falla ='" + titulo + "',estado_falla ='" + estado + "' WHERE id_falla ='" + idFalla + "'";
            try {

                pst = acceso.conectar().prepareStatement(sql);
                actualizar = pst.executeUpdate();
                if (actualizar == 1) {
                    JOptionPane.showMessageDialog(wdw_fallas, "Modificación éxitosa.");
                    this.limpiarCasillas();
                }

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(wdw_fallas, "Error al modificar la Falla. \n" + e.getMessage());

            } finally {
                acceso.desconectar();
            }

        }
    }

    public void registrar_falla(String falla, String categoria, String estado) {
        if (camposVacios(falla) == 1) {
            PreparedStatement pst;
            int guardado = 0;
            String sql = "INSERT INTO m_falla (nombre_falla,categoria_producto,"
                    + "id_categoria,estado_falla) VALUES(?,?,?,?);";
            try {
                pst = acceso.conectar().prepareStatement(sql);
                pst.setString(1, falla);
                pst.setString(2, categoria);
                pst.setString(3, this.buscarIdCategoria(categoria));
                pst.setString(4, estado);
                guardado = pst.executeUpdate();
                if (guardado == 1) {
                    JOptionPane.showMessageDialog(wdw_fallas, "Registro éxitoso.");
                    limpiarCasillas();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_fallas, "Error al guardar Falla. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }
        }
    }

    public void buscar_falla(String dato) {
        PreparedStatement pst;
        ResultSet datos;
        String sql = "SELECT * FROM m_falla WHERE nombre_falla LIKE '"
                + dato + "%'";
        tablaModelo.setRowCount(0);
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();

            while (datos.next()) {
                this.tablaModelo.addRow(new Object[]{datos.getString("id_falla"), datos.getString("nombre_falla"),
                            datos.getString("categoria_producto"), datos.getString("id_categoria"), datos.getString("estado_falla")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_fallas, "Error al buscar Falla. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
    }

    public void buscar_falla_elegida(String id) {
        PreparedStatement pst;
        ResultSet rs;
        String sql = "SELECT * from m_falla WHERE id_falla ='" + id + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                wdw_fallas.txt_id_modificar_falla.setText(rs.getString("id_falla"));
                wdw_fallas.txt_tituloFalla.setText(rs.getString("nombre_falla"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_fallas, "error buscar Falla elejida en la lista. \n" + e.getMessage());
        }

    }

    //buscar el id de la categoria y sirve de validacion para no repetir categoria
    private String buscarIdCategoria(String categoria) {
        PreparedStatement busqueda;
        ResultSet rs;
        String sql = "SELECT * FROM m_categoria WHERE nombre_categoria = '" + categoria + "'";
        try {
            busqueda = acceso.conectar().prepareStatement(sql);
            rs = busqueda.executeQuery();
            if (rs.first()) {
                return rs.getString("id_categoria");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_fallas, "Error buscar ID Categoria. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
        return null;
    }

    private int camposVacios(String titulo) {
        if (titulo.equals("")) {
            JOptionPane.showMessageDialog(wdw_fallas, "Debes ingresar un titulo.");
            return 0;
        } else {
            return 1;
        }
    }

    private int camposVaciosModificar(String idFalla, String titulo) {
        if (idFalla.equals("") || titulo.equals("")) {
            JOptionPane.showMessageDialog(wdw_fallas, "Los campos estan vacios.");
            return 0;
        } else {
            return 1;
        }

    }

    private void limpiarCasillas() {
        wdw_fallas.txt_id_modificar_falla.setText("");
        wdw_fallas.txt_tituloFalla.setText("");
    }

    public void llenarCombo() {
        PreparedStatement pst;
        String sql = " SELECT * FROM m_categoria";
        ResultSet rs;

        try {
            pst = acceso.conectar().prepareStatement(sql);
            rs = pst.executeQuery();
            wdw_fallas.jcombo_categoria.removeAllItems();
            while (rs.next()) {
                wdw_fallas.jcombo_categoria.addItem(rs.getString("nombre_categoria"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_fallas, "Error llenar datos de Categorias. \n" + e.getMessage());
        }

    }
    
}