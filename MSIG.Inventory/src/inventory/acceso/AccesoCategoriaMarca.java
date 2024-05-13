package inventory.acceso;

import inventory.vistas.wdwCatalogoCategoriaProducto;
import inventory.vistas.wdwCatalogoMarcas;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AccesoCategoriaMarca {

    private AccesoInventario acceso = new AccesoInventario();
    private wdwCatalogoCategoriaProducto wdw_categoria;
    private wdwCatalogoMarcas wdw_marca;
    private wdwCatalogoMarcas wdw_editar_marca;
    private DefaultTableModel modeloTabla = new DefaultTableModel();

    //ventana principa solo de registros categoria
    public AccesoCategoriaMarca(wdwCatalogoCategoriaProducto wdw_categoria) {
        this.wdw_categoria = wdw_categoria;
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Categoria");
        modeloTabla.addColumn("Estado");
        this.wdw_categoria.tabla_categ.setModel(modeloTabla);
    }

    //constructor para registros de marca
    public AccesoCategoriaMarca(wdwCatalogoMarcas wdw_marca) {
        this.wdw_marca = wdw_marca;
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Marca");
        modeloTabla.addColumn("Estado");
        this.wdw_marca.tabla_marcas.setModel(modeloTabla);
    }

    public void registrar_categoria(String categoria, String estado, String pUsuario, String pTerminal) {
        if (campo_categ_vacio(categoria) == 1) {
            PreparedStatement pst;
            int exitoso = 0;
            String sql = "INSERT INTO m_categoria(nombre_categoria,estado) VALUES (?,?);";

            try {
                pst = acceso.prepararConsulta(sql, "Categorias", "Guardar", pUsuario, pTerminal);
                pst.setString(1, categoria);
                pst.setString(2, estado);
                exitoso = pst.executeUpdate();
                if (exitoso == 1) {
                    JOptionPane.showMessageDialog(wdw_categoria, "Categoría registrada con éxito.");

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_categoria, "Errror al registrar una categoria. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }

        }
    }

    public void registrar_marca(String marca, String estado) {
        if (campo_marca_vacio(marca) == 1) {
            PreparedStatement pst;
            int exitoso = 0;
            String sql = "INSERT INTO m_marca (marca_producto,estado) VALUES (?,?);";

            try {
                pst = acceso.conectar().prepareStatement(sql);
                pst.setString(1, marca);
                pst.setString(2, estado);
                exitoso = pst.executeUpdate();
                if (exitoso == 1) {
                    JOptionPane.showMessageDialog(wdw_categoria, "Marca registrada con éxito.");

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_categoria, "Error al registrar Marca. \n" + e.getMessage());
            } finally {
                acceso.desconectar();
            }
        }
    }

    private int campo_marca_vacio(String dato) {
        if (dato.equals("")) {
            JOptionPane.showMessageDialog(wdw_categoria, "El campo esta vacío.");
            return 0;
        } else {
            return 1;
        }
    }

    private int campo_categ_vacio(String dato) {
        if (dato.equals("")) {
            JOptionPane.showMessageDialog(wdw_categoria, "El campo esta vacío.");
            return 0;
        } else {
            return 1;
        }
    }

    public void modificar_categoria(String id, String categoria, String estado) {
        if (campo_categ_vacio(categoria) == 1) {
            PreparedStatement pst;
            String sql = "UPDATE m_categoria SET nombre_categoria = '" + categoria + "'"
                    + ",estado ='" + estado + "' WHERE id_categoria ='" + id + "'";
            int modificado = 0;

            try {
                pst = acceso.conectar().prepareStatement(sql);
                modificado = pst.executeUpdate();
                if (modificado == 1) {
                    JOptionPane.showMessageDialog(wdw_categoria, "Modificación exitosa.");
                    wdw_categoria.txt_id_modificar_categ.setText("");
                    wdw_categoria.txt_nuevo_nombre_cat.setText("");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_categoria, "Error al modificar categoria. \n" + e);
            } finally {
                acceso.desconectar();
            }
        }

    }

    public void modificar_marca(String id, String marca, String estado) {
        if (campo_marca_vacio(marca) == 1) {
            PreparedStatement pst;
            String sql = "UPDATE m_marca SET marca_producto = '" + marca + "'"
                    + ",estado ='" + estado + "' WHERE id_marca_producto ='" + id + "'";
            int modificado = 0;

            try {
                pst = acceso.conectar().prepareStatement(sql);
                modificado = pst.executeUpdate();
                if (modificado == 1) {
                    JOptionPane.showMessageDialog(wdw_categoria, "Modificado con exito.");
                    wdw_marca.txt_id_modificar_marca.setText("");
                    wdw_marca.txt_nuevo_nombre_marca.setText("");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(wdw_marca, "Error modificar Marca. " + e);
            } finally {
                acceso.desconectar();
            }
        }

    }

    public void cargar_todas_categorias() {
        
        PreparedStatement pst;
        ResultSet resultados;
        String sql = "SELECT * FROM m_categoria";

        try {
            pst = acceso.conectar().prepareStatement(sql);
            resultados = pst.executeQuery();
            modeloTabla.setRowCount(0);
            while (resultados.next()) {
                modeloTabla.addRow(new Object[]{resultados.getString("id_categoria"),
                            resultados.getString("nombre_categoria"), resultados.getString("estado")});
            }
        } catch (SQLException e) {
        } finally {
            acceso.desconectar();
        }
        
    }

    public void cargar_todas_marcas() {
        
        PreparedStatement pst;
        ResultSet resultados;
        String sql = "SELECT * FROM m_marca";

        try {
            pst = acceso.conectar().prepareStatement(sql);
            resultados = pst.executeQuery();
            modeloTabla.setRowCount(0);
            while (resultados.next()) {
                modeloTabla.addRow(new Object[]{resultados.getString("id_marca_producto"), resultados.getString("marca_producto"), resultados.getString("estado")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_categoria, "Error al cargar Marcas. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
        
    }

    public void cargar_categoria_seleccionada(String id) {
        PreparedStatement pst;
        ResultSet datos;
        String sql = "SELECT * FROM m_categoria WHERE id_categoria = '" + id + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();
            if (datos.first()) {
                wdw_categoria.txt_id_modificar_categ.setText(datos.getString("id_categoria"));
                wdw_categoria.txt_nuevo_nombre_cat.setText(datos.getString("nombre_categoria"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_categoria, "Error al cargar Categorias. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
    }

    public void cargar_marca_seleccionada(String id) {
        PreparedStatement pst;
        ResultSet datos;
        String sql = "SELECT * FROM m_marca WHERE id_marca_producto = '" + id + "'";
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();
            if (datos.first()) {
                wdw_marca.txt_id_modificar_marca.setText(datos.getString("id_marca_producto"));
                wdw_marca.txt_nuevo_nombre_marca.setText(datos.getString("marca_producto"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_marca, "Error al cargar Marca seleccionada. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
    }

    public void buscarMarca(String marca) {
        PreparedStatement pst;
        ResultSet datos;
        String sql = "SELECT * FROM m_marca WHERE marca_producto LIKE '" + marca + "%'";
        modeloTabla.setRowCount(0);
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();
            while (datos.next()) {
                this.modeloTabla.addRow(new Object[]{datos.getString("id_marca_producto"),
                            datos.getString("marca_producto"), datos.getString("estado")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_categoria, "Error al buscar Marca. \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
    }

    public void buscarCategoria(String categoria) {
        PreparedStatement pst;
        ResultSet datos;
        String sql = "SELECT * FROM m_categoria WHERE nombre_categoria LIKE '" + categoria + "%'";
        modeloTabla.setRowCount(0);
        try {
            pst = acceso.conectar().prepareStatement(sql);
            datos = pst.executeQuery();
            while (datos.next()) {
                this.modeloTabla.addRow(new Object[]{datos.getString("id_categoria"), datos.getString("nombre_categoria"), datos.getString("estado")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(wdw_categoria, "Error al buscar Categoria \n" + e.getMessage());
        } finally {
            acceso.desconectar();
        }
    }
    
}