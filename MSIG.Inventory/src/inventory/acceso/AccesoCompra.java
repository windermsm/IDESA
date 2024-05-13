package inventory.acceso;

import inventory.objetos.ObjetosCompra;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoCompra {

    public String eliminarCompra(ObjetosCompra pCompra, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "delete from m_compra where id_compra = " + pCompra.getId_compra();
        try {
            return Acceso.ejecutarConsulta(sql, "Compra de Productos", "Eliminar Compra", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String insertarCompra(ObjetosCompra pCompra, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "insert into m_compra values (" + pCompra.getId_compra() + ", "
                + pCompra.getId_proveedor() + ", '"
                + pCompra.getFecha_factura_compra() + "', "
                + pCompra.getNumero_factura_compra() + ", '"
                + pCompra.getSerie_factura_compra() + "', '"
                + pCompra.getFolio_factura_compra() + "', "
                + pCompra.getTotal_factura_compra() + ")";
        try {
            return Acceso.ejecutarConsulta(sql, "Compra de Productos", "Insertar Compra", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarCompra(ObjetosCompra pCompra, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "update m_compra set fecha_factura_compra = '" + pCompra.getFecha_factura_compra()
                + "', num_factura_compra = " + pCompra.getNumero_factura_compra()
                + ", serie_factura_compra = '" + pCompra.getSerie_factura_compra()
                + "', folio_factura_compra = '" + pCompra.getFolio_factura_compra()
                + "', total_factura = " + pCompra.getTotal_factura_compra()
                + " where id_compra = " + pCompra.getId_compra();
        try {
            return Acceso.ejecutarConsulta(sql, "Compra de Productos", "Actualizar Compra", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public ArrayList<ObjetosCompra> seleccionarCompra(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_compra";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar Compra", pUsuario, pTerminal);
            ObjetosCompra Registros;
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosCompra> seleccionarIdCompra(ObjetosCompra pCompra, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * from m_compra "
                + " where num_factura_compra = " + pCompra.getNumero_factura_compra()
                + " and serie_factura_compra = '" + pCompra.getSerie_factura_compra() + "' "
                + " and folio_factura_compra = '" + pCompra.getFolio_factura_compra() + "' "
                + " and id_proveedor = " + pCompra.getId_proveedor()
                + " and total_factura_compra = " + pCompra.getTotal_factura_compra();
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar ID Compra", pUsuario, pTerminal);
            ObjetosCompra Registros;
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosCompra> seleccionarCompraFecha(String pFecha_inicial, String pFecha_final, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * "
                + "from m_compra "
                + "where date_format(fecha_factura_compra, '%Y-%m-%d') between str_to_date('"
                + pFecha_inicial
                + "', '%Y-%m-%d') and str_to_date('"
                + pFecha_final
                + "', '%Y-%m-%d') order by fecha_factura_compra desc";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar Compra por Fecha", pUsuario, pTerminal);
            ObjetosCompra Registros;
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    
    public ArrayList<ObjetosCompra> seleccionarCompraFEL(String pSerie, String pNumero, String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select * "
                + "from m_compra "
                + "where serie_factura_compra = '" + pSerie + "' and num_factura_compra = " + pNumero;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar Compra por Fecha", pUsuario, pTerminal);
            ObjetosCompra Registros;
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ObjetosCompra seleccionarCompraPorID(String pId_compra, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        ObjetosCompra Registros = null;
        String sql = "select * "
                + "from m_compra "
                + "where id_compra = " + pId_compra;
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar Compra por ID", pUsuario, pTerminal);
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return Registros;
    }
    
    public ArrayList<ObjetosCompra> buscarComprasAlCreditoPorNombre(String pNombre, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        ArrayList<ObjetosCompra> lista = new ArrayList<>();
        ObjetosCompra Registros = null;
        
        String sql = "select  b.id_proveedor, "
                + "        c.nom_persona, "
                + "        c.nit_persona, "
                + "        a.id_compra, "
                + "        a.id_proveedor, "
                + "        a.fecha_factura_compra, "
                + "        a.num_factura_compra, "
                + "        a.serie_factura_compra, "
                + "        a.folio_factura_compra, "
                + "        a.total_factura_compra, "
                + "        d.id_forma_pago, "
                + "        d.credito as saldo "
                + "from    m_compra as a, "
                + "        m_proveedor as b, "
                + "        m_persona as c, "
                + "        m_formas_pago as d "
                + "where   a.id_proveedor = b.id_proveedor "
                + "    and b.id_persona = c.id_persona "
                + "    and a.id_compra = d.id_compra "
                + "    and d.credito > 0 "
                + "    and c.nom_persona like '%" + pNombre + "%'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar Compra por Nombre", pUsuario, pTerminal);
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setNit_proveedor(tabla.getString("nit_persona"));
                Registros.setNombre_proveedor(tabla.getString("nom_persona"));
                Registros.setSaldo_pendiente(tabla.getString("saldo"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosCompra> buscarComprasPorId(String pId, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        ArrayList<ObjetosCompra> lista = new ArrayList<>();
        ObjetosCompra Registros = null;
        
        String sql = "select  b.id_proveedor, "
                + "        c.nom_persona, "
                + "        c.nit_persona, "
                + "        a.id_compra, "
                + "        a.id_proveedor, "
                + "        a.fecha_factura_compra, "
                + "        a.num_factura_compra, "
                + "        a.serie_factura_compra, "
                + "        a.folio_factura_compra, "
                + "        a.total_factura_compra, "
                + "        d.id_forma_pago, "
                + "        d.credito as saldo "
                + "from    m_compra as a, "
                + "        m_proveedor as b, "
                + "        m_persona as c, "
                + "        m_formas_pago as d "
                + "where   a.id_proveedor = b.id_proveedor "
                + "    and b.id_persona = c.id_persona "
                + "    and a.id_compra = d.id_compra "
                + "    and d.credito > 0 "
                + "    and a.id_compra = " + pId;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Buscar Compras por ID", pUsuario, pTerminal);
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setNit_proveedor(tabla.getString("nit_persona"));
                Registros.setNombre_proveedor(tabla.getString("nom_persona"));
                Registros.setSaldo_pendiente(tabla.getString("saldo"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosCompra> buscarComprasAlCreditoPorNit(String pNit, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        ArrayList<ObjetosCompra> lista = new ArrayList<>();
        ObjetosCompra Registros = null;
        
        String sql = "select  b.id_proveedor, "
                + "        c.nom_persona, "
                + "        c.nit_persona, "
                + "        a.id_compra, "
                + "        a.id_proveedor, "
                + "        a.fecha_factura_compra, "
                + "        a.num_factura_compra, "
                + "        a.serie_factura_compra, "
                + "        a.folio_factura_compra, "
                + "        a.total_factura_compra, "
                + "        d.id_forma_pago, "
                + "        d.credito as saldo "
                + "from    m_compra as a, "
                + "        m_proveedor as b, "
                + "        m_persona as c, "
                + "        m_formas_pago as d "
                + "where   a.id_proveedor = b.id_proveedor "
                + "    and b.id_persona = c.id_persona "
                + "    and a.id_compra = d.id_compra "
                + "    and d.credito > 0 "
                + "    and c.nit_persona like '%" + pNit + "%'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar Compra por NIT", pUsuario, pTerminal);
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setNit_proveedor(tabla.getString("nit_persona"));
                Registros.setNombre_proveedor(tabla.getString("nom_persona"));
                Registros.setSaldo_pendiente(tabla.getString("saldo"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosCompra> buscarComprasAlCreditoPorFactura(String pSerie, String pNumero, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        ArrayList<ObjetosCompra> lista = new ArrayList<>();
        ObjetosCompra Registros = null;
        
        String sql = "select  b.id_proveedor, "
                + "        c.nom_persona, "
                + "        c.nit_persona, "
                + "        a.id_compra, "
                + "        a.id_proveedor, "
                + "        a.fecha_factura_compra, "
                + "        a.num_factura_compra, "
                + "        a.serie_factura_compra, "
                + "        a.folio_factura_compra, "
                + "        a.total_factura_compra, "
                + "        d.id_forma_pago, "
                + "        d.credito as saldo "
                + "from    m_compra as a, "
                + "        m_proveedor as b, "
                + "        m_persona as c, "
                + "        m_formas_pago as d "
                + "where   a.id_proveedor = b.id_proveedor "
                + "    and b.id_persona = c.id_persona "
                + "    and a.id_compra = d.id_compra "
                + "    and d.credito > 0 "
                + "    and a.serie_factura_compra = '" + pSerie + "' "
                + "    and a.num_factura_compra = " + pNumero;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar Compra por ID", pUsuario, pTerminal);
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setNit_proveedor(tabla.getString("nit_persona"));
                Registros.setNombre_proveedor(tabla.getString("nom_persona"));
                Registros.setSaldo_pendiente(tabla.getString("saldo"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosCompra> buscarComprasAlCreditoPorNumeroFacturaFEL(String pNumero, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        ArrayList<ObjetosCompra> lista = new ArrayList<>();
        ObjetosCompra Registros = null;
        
        String sql = "select  b.id_proveedor, "
                + "        c.nom_persona, "
                + "        c.nit_persona, "
                + "        a.id_compra, "
                + "        a.id_proveedor, "
                + "        a.fecha_factura_compra, "
                + "        a.num_factura_compra, "
                + "        a.serie_factura_compra, "
                + "        a.folio_factura_compra, "
                + "        a.total_factura_compra, "
                + "        d.id_forma_pago, "
                + "        d.credito as saldo "
                + "from    m_compra as a, "
                + "        m_proveedor as b, "
                + "        m_persona as c, "
                + "        m_formas_pago as d "
                + "where   a.id_proveedor = b.id_proveedor "
                + "    and b.id_persona = c.id_persona "
                + "    and a.id_compra = d.id_compra "
                + "    and d.credito > 0 "
                + "    and a.num_factura_compra = " + pNumero;
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar Compra por ID", pUsuario, pTerminal);
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setNit_proveedor(tabla.getString("nit_persona"));
                Registros.setNombre_proveedor(tabla.getString("nom_persona"));
                Registros.setSaldo_pendiente(tabla.getString("saldo"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosCompra> buscarComprasAlCreditoPorSerieFacturaFEL(String pSerie, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        ArrayList<ObjetosCompra> lista = new ArrayList<>();
        ObjetosCompra Registros = null;
        
        String sql = "select  b.id_proveedor, "
                + "        c.nom_persona, "
                + "        c.nit_persona, "
                + "        a.id_compra, "
                + "        a.id_proveedor, "
                + "        a.fecha_factura_compra, "
                + "        a.num_factura_compra, "
                + "        a.serie_factura_compra, "
                + "        a.folio_factura_compra, "
                + "        a.total_factura_compra, "
                + "        d.id_forma_pago, "
                + "        d.credito as saldo "
                + "from    m_compra as a, "
                + "        m_proveedor as b, "
                + "        m_persona as c, "
                + "        m_formas_pago as d "
                + "where   a.id_proveedor = b.id_proveedor "
                + "    and b.id_persona = c.id_persona "
                + "    and a.id_compra = d.id_compra "
                + "    and d.credito > 0 "
                + "    and a.serie_factura_compra = '" + pSerie + "'";
        
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Compras", "Seleccionar Compra por ID", pUsuario, pTerminal);
            while (tabla.next()) {
                Registros = new ObjetosCompra();
                Registros.setId_compra(tabla.getInt("id_compra"));
                Registros.setId_proveedor(tabla.getInt("id_proveedor"));
                Registros.setNit_proveedor(tabla.getString("nit_persona"));
                Registros.setNombre_proveedor(tabla.getString("nom_persona"));
                Registros.setSaldo_pendiente(tabla.getString("saldo"));
                Registros.setFecha_factura_compra(tabla.getString("fecha_factura_compra"));
                Registros.setNumero_factura_compra(tabla.getString("num_factura_compra"));
                Registros.setSerie_factura_compra(tabla.getString("serie_factura_compra"));
                Registros.setFolio_factura_compra(tabla.getString("folio_factura_compra"));
                Registros.setTotal_factura_compra(tabla.getFloat("total_factura_compra"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
}