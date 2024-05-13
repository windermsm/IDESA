package inventory.acceso;

import inventory.objetos.ObjetosListaProducto;
import inventory.objetos.ObjetosProducto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccesoProducto {

    public String eliminarProducto(ObjetosProducto pProducto, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete m_producto where id_producto = " + pProducto.getId_producto();
        try {
            return Acceso.ejecutarConsulta(sql, "Producto", "Eliminar Producto", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarProducto(ObjetosProducto pProducto, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_producto values ('" + pProducto.getId_producto()
                + "', '" + pProducto.getMarca_producto() + "','" + pProducto.getDesc_producto() + "', '" + pProducto.getLinea_producto()
                + "', " + pProducto.getPrecio_compra_producto() + "," + pProducto.getPrecio_est_producto()
                + ", " + pProducto.getId_proveedor() + "," + pProducto.getExi_producto()
                + ", '" + pProducto.getUnidad_medida_producto() + "'," + pProducto.getMinimo_producto()
                + ", " + pProducto.getMaximo_producto() + "," + pProducto.getPrj_est_producto()
                + ", " + pProducto.getPrj_min_producto() + "," + pProducto.getPrecio_min_producto()
                + ", '" + pProducto.getCodigo_fabricante() + "','" + pProducto.getUbicacion_producto()
                + "', '" + pProducto.getFactura_producto() + "', '" + pProducto.getDescuento_producto()
                + "', '" + pProducto.getVisible_producto() + "', '" + pProducto.getSerie_producto() 
                + "', '" + pProducto.getComponente_producto() + "', '" + pProducto.getGarantia_producto() + "', " 
                + pProducto.getPrecio_especial_producto() + ", '" + pProducto.getTipo_sat_producto() + "')";
        try {
            return Acceso.ejecutarConsulta(sql, "Productos", "Insertar Producto", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarProducto(ObjetosProducto pProducto, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_producto set marca_producto = '" + pProducto.getMarca_producto()
                + "', desc_producto = '" + pProducto.getDesc_producto() + "', precio_compra_producto = " + pProducto.getPrecio_compra_producto()
                + ", linea_producto = '" + pProducto.getLinea_producto()
                + "', precio_est_producto = " + pProducto.getPrecio_est_producto()
                + ", exi_producto = " + pProducto.getExi_producto() + ", id_proveedor = " + pProducto.getId_proveedor()
                + ", unidad_medida_producto = '" + pProducto.getUnidad_medida_producto()
                + "', minimo_producto = " + pProducto.getMinimo_producto() + ", maximo_producto = " + pProducto.getMaximo_producto()
                + ", prj_est_producto = " + pProducto.getPrj_est_producto() + ", prj_min_producto = " + pProducto.getPrj_min_producto()
                + ", precio_min_producto = " + pProducto.getPrecio_min_producto()
                + ", codigo_fabricante = '" + pProducto.getCodigo_fabricante() + "', ubicacion_producto = '" + pProducto.getUbicacion_producto() 
                + "', factura_producto = '" + pProducto.getFactura_producto() + "', descuento_producto = '" + pProducto.getDescuento_producto() 
                + "', visible_producto = '" + pProducto.getVisible_producto() + "', serie_producto = '" + pProducto.getSerie_producto() 
                + "', componente_producto = '" + pProducto.getComponente_producto() + "', garantia_producto = '" + pProducto.getGarantia_producto() + "' "
                + ", precio_especial_producto = " + pProducto.getPrecio_especial_producto() + ", tipo_sat_producto = '" + pProducto.getTipo_sat_producto() + "'  "
                + " where id_producto = '" + pProducto.getId_producto() + "'";
        try {
            return Acceso.ejecutarConsulta(sql, "Productos", "Actualizar Producto", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosProducto> seleccionarProducto(String pUsuario, String pTerminal, String Rol) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "";
        if(Rol.equals("Administrador") || Rol.equals("Jefe Sucursal") ) {
           sql = "select * from m_producto order by id_producto";
        } else {
           sql = "select * from m_producto where visible_producto = 'SI' order by id_producto";
        }
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Seleccionar Producto", pUsuario, pTerminal);
            ObjetosProducto Registros;
            while (tabla.next()) {
                Registros = new ObjetosProducto();
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setMarca_producto(tabla.getString("marca_producto"));
                Registros.setDesc_producto(tabla.getString("desc_producto"));
                Registros.setLinea_producto(tabla.getString("linea_producto"));
                Registros.setPrecio_compra_producto(tabla.getFloat("precio_compra_producto"));
                Registros.setPrecio_est_producto(tabla.getFloat("precio_est_producto"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setExi_producto(tabla.getFloat("exi_producto"));
                Registros.setUnidad_medida_producto(tabla.getString("unidad_medida_producto"));
                Registros.setMinimo_producto(tabla.getFloat("minimo_producto"));
                Registros.setMaximo_producto(tabla.getFloat("maximo_producto"));
                Registros.setPrj_est_producto(tabla.getFloat("prj_est_producto"));
                Registros.setPrj_min_producto(tabla.getFloat("prj_min_producto"));
                Registros.setPrecio_min_producto(tabla.getFloat("precio_min_producto"));
                Registros.setCodigo_fabricante(tabla.getString("codigo_fabricante"));
                Registros.setUbicacion_producto(tabla.getString("ubicacion_producto"));
                Registros.setFactura_producto(tabla.getString("factura_producto"));
                Registros.setDescuento_producto(tabla.getString("descuento_producto"));
                Registros.setVisible_producto(tabla.getString("visible_producto"));
                Registros.setSerie_producto(tabla.getString("serie_producto"));
                Registros.setComponente_producto(tabla.getString("componente_producto"));
                Registros.setGarantia_producto(tabla.getString("garantia_producto"));
                Registros.setPrecio_especial_producto(tabla.getFloat("precio_especial_producto"));
                Registros.setTipo_sat_producto(tabla.getString("tipo_sat_producto"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosProducto> seleccionarProductoVisible(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_producto where visible_producto = 'SI' order by id_producto";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Seleccionar Producto", pUsuario, pTerminal);
            ObjetosProducto Registros;
            while (tabla.next()) {
                Registros = new ObjetosProducto();
                Registros.setId_producto(tabla.getString("id_producto"));
                Registros.setMarca_producto(tabla.getString("marca_producto"));
                Registros.setDesc_producto(tabla.getString("desc_producto"));
                Registros.setLinea_producto(tabla.getString("linea_producto"));
                Registros.setPrecio_compra_producto(tabla.getFloat("precio_compra_producto"));
                Registros.setPrecio_est_producto(tabla.getFloat("precio_est_producto"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setExi_producto(tabla.getFloat("exi_producto"));
                Registros.setUnidad_medida_producto(tabla.getString("unidad_medida_producto"));
                Registros.setMinimo_producto(tabla.getFloat("minimo_producto"));
                Registros.setMaximo_producto(tabla.getFloat("maximo_producto"));
                Registros.setPrj_est_producto(tabla.getFloat("prj_est_producto"));
                Registros.setPrj_min_producto(tabla.getFloat("prj_min_producto"));
                Registros.setPrecio_min_producto(tabla.getFloat("precio_min_producto"));
                Registros.setCodigo_fabricante(tabla.getString("codigo_fabricante"));
                Registros.setUbicacion_producto(tabla.getString("ubicacion_producto"));
                Registros.setFactura_producto(tabla.getString("factura_producto"));
                Registros.setDescuento_producto(tabla.getString("descuento_producto"));
                Registros.setVisible_producto(tabla.getString("visible_producto"));
                Registros.setSerie_producto(tabla.getString("serie_producto"));
                Registros.setComponente_producto(tabla.getString("componente_producto"));
                Registros.setGarantia_producto(tabla.getString("garantia_producto"));
                Registros.setPrecio_especial_producto(tabla.getFloat("precio_especial_producto"));
                Registros.setTipo_sat_producto(tabla.getString("tipo_sat_producto"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosProducto> buscarProducto(String pId_producto, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_producto where id_producto = '" + pId_producto + "'";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Buscar Producto", pUsuario, pTerminal);
            
            if (tabla != null) {
                ObjetosProducto Registros;

                while (tabla.next()) {
                    Registros = new ObjetosProducto();
                    Registros.setId_producto(tabla.getString("id_producto"));
                    Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                    Registros.setMarca_producto(tabla.getString("marca_producto"));
                    Registros.setDesc_producto(tabla.getString("desc_producto"));
                    Registros.setLinea_producto(tabla.getString("linea_producto"));
                    Registros.setPrecio_compra_producto(tabla.getFloat("precio_compra_producto"));
                    Registros.setPrecio_est_producto(tabla.getFloat("precio_est_producto"));
                    Registros.setExi_producto(tabla.getFloat("exi_producto"));
                    Registros.setUnidad_medida_producto(tabla.getString("unidad_medida_producto"));
                    Registros.setMaximo_producto(tabla.getFloat("maximo_producto"));
                    Registros.setMinimo_producto(tabla.getFloat("minimo_producto"));
                    Registros.setPrj_est_producto(tabla.getFloat("prj_est_producto"));
                    Registros.setPrj_min_producto(tabla.getFloat("prj_min_producto"));
                    Registros.setPrecio_min_producto(tabla.getFloat("precio_min_producto")); 
                    Registros.setCodigo_fabricante(tabla.getString("codigo_fabricante"));
                    Registros.setUbicacion_producto(tabla.getString("ubicacion_producto"));
                    Registros.setFactura_producto(tabla.getString("factura_producto"));
                    Registros.setDescuento_producto(tabla.getString("descuento_producto"));
                    Registros.setVisible_producto(tabla.getString("visible_producto"));
                    Registros.setSerie_producto(tabla.getString("serie_producto"));
                    Registros.setComponente_producto(tabla.getString("componente_producto"));
                    Registros.setGarantia_producto(tabla.getString("garantia_producto"));
                    Registros.setPrecio_especial_producto(tabla.getFloat("precio_especial_producto"));
                    Registros.setTipo_sat_producto(tabla.getString("tipo_sat_producto"));
                    lista.add(Registros);
                }
                
            }

        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ObjetosProducto buscarProductoPorSerie(String pSerie, String pUsuario, String pTerminal) {
        
        ObjetosProducto Registros = new ObjetosProducto();        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "SELECT a.* FROM m_producto AS a, m_serie AS b WHERE a.id_producto = b.id_producto AND b.numero_serie = '" + pSerie + "'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Buscar Producto", pUsuario, pTerminal);

            if (tabla != null) {       

                while (tabla.next()) {
                    Registros.setId_producto(tabla.getString("id_producto"));
                    Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                    Registros.setMarca_producto(tabla.getString("marca_producto"));
                    Registros.setDesc_producto(tabla.getString("desc_producto"));
                    Registros.setLinea_producto(tabla.getString("linea_producto"));
                    Registros.setPrecio_compra_producto(tabla.getFloat("precio_compra_producto"));
                    Registros.setPrecio_est_producto(tabla.getFloat("precio_est_producto"));
                    Registros.setExi_producto(tabla.getFloat("exi_producto"));
                    Registros.setUnidad_medida_producto(tabla.getString("unidad_medida_producto"));
                    Registros.setMaximo_producto(tabla.getFloat("maximo_producto"));
                    Registros.setMinimo_producto(tabla.getFloat("minimo_producto"));
                    Registros.setPrj_est_producto(tabla.getFloat("prj_est_producto"));
                    Registros.setPrj_min_producto(tabla.getFloat("prj_min_producto"));
                    Registros.setPrecio_min_producto(tabla.getFloat("precio_min_producto"));
                    Registros.setCodigo_fabricante(tabla.getString("codigo_fabricante"));
                    Registros.setUbicacion_producto(tabla.getString("ubicacion_producto"));
                    Registros.setFactura_producto(tabla.getString("factura_producto"));
                    Registros.setDescuento_producto(tabla.getString("descuento_producto"));
                    Registros.setVisible_producto(tabla.getString("visible_producto"));
                    Registros.setSerie_producto(tabla.getString("serie_producto"));
                    Registros.setComponente_producto(tabla.getString("componente_producto"));
                    Registros.setGarantia_producto(tabla.getString("garantia_producto"));
                    Registros.setPrecio_especial_producto(tabla.getFloat("precio_especial_producto"));
                    Registros.setTipo_sat_producto(tabla.getString("tipo_sat_producto"));
                }
            }

        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return Registros;
    }

    public ObjetosProducto buscarInformacionProducto(String pId_producto, String pUsuario, String pTerminal) {

        ObjetosProducto Registro = new ObjetosProducto();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_producto where visible_producto = 'SI' and id_producto = '" + pId_producto + "'";

        try {

            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Buscar Informacion Producto", pUsuario, pTerminal);

            if (tabla != null) {
                while (tabla.next()) {
                    Registro.setId_producto(tabla.getString("id_producto"));
                    Registro.setId_proveedor(tabla.getInt("id_proveedor"));
                    Registro.setMarca_producto(tabla.getString("marca_producto"));
                    Registro.setDesc_producto(tabla.getString("desc_producto"));
                    Registro.setLinea_producto(tabla.getString("linea_producto"));
                    Registro.setPrecio_compra_producto(tabla.getFloat("precio_compra_producto"));
                    Registro.setPrecio_est_producto(tabla.getFloat("precio_est_producto"));
                    Registro.setExi_producto(tabla.getFloat("exi_producto"));
                    Registro.setUnidad_medida_producto(tabla.getString("unidad_medida_producto"));
                    Registro.setMaximo_producto(tabla.getFloat("maximo_producto"));
                    Registro.setMinimo_producto(tabla.getFloat("minimo_producto"));
                    Registro.setPrj_est_producto(tabla.getFloat("prj_est_producto"));
                    Registro.setPrj_min_producto(tabla.getFloat("prj_min_producto"));
                    Registro.setPrecio_min_producto(tabla.getFloat("precio_min_producto"));
                    Registro.setCodigo_fabricante(tabla.getString("codigo_fabricante"));
                    Registro.setUbicacion_producto(tabla.getString("ubicacion_producto"));
                    Registro.setFactura_producto(tabla.getString("factura_producto"));
                    Registro.setDescuento_producto(tabla.getString("descuento_producto"));
                    Registro.setVisible_producto(tabla.getString("visible_producto"));
                    Registro.setSerie_producto(tabla.getString("serie_producto"));
                    Registro.setComponente_producto(tabla.getString("componente_producto"));
                    Registro.setGarantia_producto(tabla.getString("garantia_producto"));
                    Registro.setPrecio_especial_producto(tabla.getFloat("precio_especial_producto"));
                    Registro.setTipo_sat_producto(tabla.getString("tipo_sat_producto"));
                }
            }

        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return Registro;
    }
    
    public ArrayList<ObjetosListaProducto>  buscarListadoSeries(String pId_factura, String pUsuario, String pTerminal) {

        ArrayList<ObjetosListaProducto> lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        
        String sql = "SELECT  a.id_producto AS id_producto, "
                + "        b.desc_producto AS descripcion, "
                + "        b.serie_producto AS serie, "
                + "        b.componente_producto AS componentes, "
                + "        b.garantia_producto AS garantia, "
                + "        SUM(a.cantidad_d_factura) AS cantidad "
                + "FROM    d_factura AS a, "
                + "        m_producto AS b "
                + "WHERE   a.id_producto = b.id_producto "
                + "    AND a.id_factura = " + pId_factura + " "
                + "    AND b.serie_producto = 'SI' "
                + "GROUP BY a.id_producto, "
                + "         b.desc_producto, "
                + "         b.serie_producto, "
                + "         b.componente_producto, "
                + "         b.garantia_producto";

        try {

            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Buscar Informacion Producto", pUsuario, pTerminal);

            if (tabla != null) {
                while (tabla.next()) {
                    ObjetosListaProducto Registro = new ObjetosListaProducto();
                    Registro.setIdproducto(tabla.getString("id_producto"));
                    Registro.setDescripcionproducto(tabla.getString("descripcion"));
                    Registro.setSerie(tabla.getString("serie"));
                    Registro.setComponentes(tabla.getString("componentes"));
                    Registro.setGarantia(tabla.getString("garantia"));
                    Registro.setCantidad(tabla.getInt("cantidad"));
                    lista.add(Registro);
                }
            }

        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        
        return lista;
        
    }
    
    public String actualizarExistencia(String pId_producto, float pCantidad, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_producto set exi_producto = " + pCantidad + " where id_producto = '" + pId_producto + "'";
        try {
            return Acceso.ejecutarConsulta(sql, "Productos", "Actualizar Existencia", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarPrecioCompra(String pId_producto, double pPrecioCompra, int pId_proveedor, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_producto "
                + "set precio_compra_producto = " + pPrecioCompra
                + ", precio_est_producto = " + pPrecioCompra + " * (1 + (prj_est_producto/100))"
                + ", precio_min_producto = " + pPrecioCompra + " * (1 + (prj_min_producto/100))"
                + ", id_proveedor = " + pId_proveedor + " "
                + " where id_producto = '" + pId_producto + "'";
        try {
            return Acceso.ejecutarConsulta(sql, "Productos", "Actualizar Precio Compra", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarPrecioCompraConPrecioVenta(String pId_producto, float pPrecioCompra, float pPrecioMinimo, float pPrecioNormal, int pId_proveedor, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_producto "
                + "SET precio_compra_producto = " + pPrecioCompra
                + ", precio_est_producto = " + pPrecioNormal + ", prj_est_producto = ROUND ( ( ( " + pPrecioNormal + " / " + pPrecioCompra + " ) - 1 ) * 100, 4 )  "
                + ", precio_min_producto = " + pPrecioMinimo + ", prj_min_producto = ROUND ( ( ( " + pPrecioMinimo + " / " + pPrecioCompra + " ) - 1 ) * 100, 4 )  "
                + ", id_proveedor = " + pId_proveedor + " "
                + " where id_producto = '" + pId_producto + "'";
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Productos", "Actualizar Precio Compra", pUsuario, pTerminal);
        } catch (Error error) { 
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String buscarProveedor(int pIdProveedor, String pUsuario, String pTerminal) {
        String cProveedor = null;
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select b.nom_persona from m_proveedor a, m_persona b where a.id_proveedor = "
                + pIdProveedor + " and b.id_persona = a.id_persona";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Buscar Proveedor", pUsuario, pTerminal);
            while (tabla.next()) {
                cProveedor = tabla.getString("nom_persona");

            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return cProveedor;
    }

    public Integer buscarIdProveedor(String pNomProveedor, String pUsuario, String pTerminal) {

        Integer cProveedor = null;
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select  a.id_proveedor from m_proveedor a, m_persona b where   b.nom_persona = '"
                + pNomProveedor + "' and a.id_persona = b.id_persona";

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Producto", "Buscar ID Proveedor", pUsuario, pTerminal);
            while (tabla.next()) {
                cProveedor = tabla.getInt("id_proveedor");

            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return cProveedor;
    }

    public String buscarDescripcion(String id, String pUsuario, String pTerminal) {
        String valor = null;
        AccesoInventario acceso = new AccesoInventario();
        String sql = "select desc_producto from m_producto where id_producto = '" + id + "'";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Producto", "Buscar Descripcion", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getString("desc_producto");
            }
        } catch (Exception error) {
        }
        return valor;
    }
    
    public String disminuirExistencia(String pId_producto, float pCantidad, String pUsuario, String pTerminal){
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_producto set exi_producto = exi_producto - " + pCantidad 
                + " where id_producto = '" + pId_producto + "'";
        try{
            return Acceso.ejecutarConsulta(sql, "Traslados", "Guardar", pUsuario, pTerminal);
        }catch(Error error){
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
        
    public String buscarSiguienteCodigo(String pUsuario, String pTerminal) {
        String valor = "0";
        AccesoInventario acceso = new AccesoInventario();
        String sql ="SELECT MAX(x.codigo) + 1 AS siguiente "
                + "FROM ( "
                + "  SELECT CAST(id_producto AS UNSIGNED) AS codigo "
                + "  FROM   m_producto "
                + ") AS x";
        try {
            ResultSet rs = acceso.listarRegistros(sql, "Producto", "Buscar Siguiente Codigo", pUsuario, pTerminal);
            while (rs.next()) {
                valor = rs.getString("siguiente");
            }
        } catch (Exception error) {
        }
        return valor;
    }
    
    public int buscarCodigoDisponible(String pUsuario, String pTerminal) {
        int valor = 0;
        int anterior = 0;
        int actual = 0;
        int diferencia = 0;
        
        AccesoInventario acceso = new AccesoInventario();
        
        String sql = "SELECT	codigo AS id_producto\n"
                + "FROM 	(\n"
                + "SELECT 	CONVERT(SUBSTRING_INDEX(id_producto,'-',-1),UNSIGNED INTEGER) AS codigo\n"
                + "FROM 	m_producto\n"
                + ") AS X\n"
                + "ORDER BY X.codigo";
        
        try {
            
            ResultSet rs = acceso.listarRegistros(sql, "Producto", "Buscar Codigo Disponible", pUsuario, pTerminal);
            boolean fin = false;
            
            int contador = 1;
            
            while (rs.next() && fin == false) {
                
                try {
                    int x = Integer.parseInt(rs.getString("id_producto")); 
                    actual = x;
                    System.out.println(x);
                } catch(NumberFormatException | SQLException Error) {
                    System.out.println(Error.toString());
                    //actual = anterior;
                }
                
                if(contador == 1) {
                    anterior = actual;
                } 
                
                System.out.println("------------------");
                System.out.println("Actual: " + actual);
                System.out.println("Anterior: " + anterior);
                
                diferencia = actual - anterior;
                
                System.out.println("Diferencia: " + diferencia);
                
                if(diferencia > 1) {
                    System.out.println("La diferencia es mayor a 1");
                    valor = anterior + 1;
                    System.out.println("Valor: " + valor);
                    fin = true;
                } else {
                    anterior = actual;
                    fin = false;
                }
                
                contador++;
                
            }
        } catch (SQLException error) {
            System.out.println("No puder realizar la busqueda.\n" + error.toString());
        }
        
        if (anterior == actual || diferencia == 1) {
            System.out.println("Termino y no encontro el numero siguiente");
            valor = actual + 1;
        }
        
        return valor;
        
    }
    
}