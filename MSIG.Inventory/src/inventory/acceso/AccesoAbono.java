package inventory.acceso;

import inventory.objetos.ObjetosAbono;
import inventory.servicios.Matematicas;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccesoAbono {
    
    private Matematicas matematicas = new Matematicas();

    public String insertarAbono(ObjetosAbono pAbono, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        double monto = matematicas.redondear(pAbono.getSaldo_pendiente() - pAbono.getMonto_abono());
        String sql = "INSERT INTO d_abono VALUES (0, " + pAbono.getId_factura() + ", " + pAbono.getMonto_abono() + ", " 
                + monto + ", now(), " + pAbono.getComision_sobre_abono() + ", '" + pUsuario + "', '" + pAbono.getTipo_abono() 
                + "', '" + pAbono.getNumero_abono() + "', '" + pAbono.getBanco_abono() + "')";
        
        try {
            return Acceso.ejecutarConsulta(sql, "Abono de Clientes", "Insertar Abono", pUsuario, pTerminal);
        } catch (Error error) {
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
        
    }
    
    public ArrayList<ObjetosAbono> buscarCreditosClienteId(String pId, String pUsuario, String pTerminal) {

        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "select  a.id_factura as id_factura, "
                + "        c.nit_persona as nit, "
                + "        ifnull(a.nombre_factura, c.nom_persona) as nombre,  "
                + "        concat(a.serie_factura, ' ', a.num_factura) as factura, "
                + "        a.total_factura as total,  "
                + "        a.monto_credito as credito,  "
                + "        a.monto_contado as contado, "
                + "        a.dias_de_credito as dias_credito,  "
                + "        d.monto as total_abonado, "
                + "        b.id_cliente as id_cliente,  "
                + "        c.id_persona as id_persona, "
                + "        a.monto_credito - d.monto as saldo_pendiente, "
                + "        date_format(DATE_ADD(a.fecha_emision_factura, INTERVAL a.dias_de_credito DAY), '%d-%m-%Y') as fecha_limite "
                + "from    m_factura as a, m_cliente as b, m_persona as c, "
                + "        ( "
                + "        select  id_factura, "
                + "                sum(monto_abono) as monto, "
                + "                sum(saldo_pendiente) as saldo "
                + "        from m_abono group by id_factura "
                + "        ) as d "
                + "where   a.tipo_venta in ('Credito', 'Credito / Contado') "
                + "    and a.id_cliente = b.id_cliente "
                + "    and b.id_persona = c.id_persona "
                + "    and a.id_factura = d.id_factura "
                + "    and a.estado_factura = 'Credito'"
                + "    and a.monto_credito > d.monto and a.id_factura = " + pId.trim() + " "
                + "order by c.nit_persona, a.id_factura asc";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Abono de Clientes", "Listar Registros", pUsuario, pTerminal);
            ObjetosAbono Registros;
            while (tabla.next()) {
                Registros = new ObjetosAbono();
                Registros.setId_factura(tabla.getInt(1));
                Registros.setNit_persona(tabla.getString(2));
                Registros.setNom_persona(tabla.getString(3));
                Registros.setFactura(tabla.getString(4));
                Registros.setTotal(tabla.getFloat(5));
                Registros.setMonto_credito(tabla.getFloat(6));
                Registros.setMonto_contado(tabla.getInt(7));
                Registros.setDias_de_credito(tabla.getInt(8));
                Registros.setTotal_abonado(tabla.getFloat(9));
                Registros.setId_cliente(tabla.getInt(10));
                Registros.setId_persona(tabla.getInt(11));
                Registros.setSaldo_pendiente(tabla.getFloat(12));
                Registros.setFecha_limite(tabla.getString(13));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public ArrayList<ObjetosAbono> buscarCreditosClienteNombre(String pNombre, String pUsuario, String pTerminal) {

        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "select  a.id_factura as id_factura, "
                + "        c.nit_persona as nit, "
                + "        ifnull(a.nombre_factura, c.nom_persona) as nombre,  "
                + "        concat(a.serie_factura, ' ', a.num_factura) as factura, "
                + "        a.total_factura as total,  "
                + "        a.monto_credito as credito,  "
                + "        a.monto_contado as contado, "
                + "        a.dias_de_credito as dias_credito,  "
                + "        d.monto as total_abonado, "
                + "        b.id_cliente as id_cliente,  "
                + "        c.id_persona as id_persona, "
                + "        a.monto_credito - d.monto as saldo_pendiente, "
                + "        date_format(DATE_ADD(a.fecha_emision_factura, INTERVAL a.dias_de_credito DAY), '%d-%m-%Y') as fecha_limite "
                + "from    m_factura as a, m_cliente as b, m_persona as c, "
                + "        ( "
                + "        select  id_factura, "
                + "                sum(monto_abono) as monto, "
                + "                sum(saldo_pendiente) as saldo "
                + "        from m_abono group by id_factura "
                + "        ) as d "
                + "where   a.tipo_venta in ('Credito', 'Credito / Contado') "
                + "    and a.id_cliente = b.id_cliente "
                + "    and b.id_persona = c.id_persona "
                + "    and a.id_factura = d.id_factura "
                + "    and a.estado_factura = 'Credito'"
                + "    and a.monto_credito > d.monto and UPPER(a.nombre_factura) like ('%" + pNombre.toUpperCase() + "%') "
                + "order by c.nit_persona, a.id_factura asc";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Abono de Clientes", "Listar Registros", pUsuario, pTerminal);
            ObjetosAbono Registros;
            while (tabla.next()) {
                Registros = new ObjetosAbono();
                Registros.setId_factura(tabla.getInt(1));
                Registros.setNit_persona(tabla.getString(2));
                Registros.setNom_persona(tabla.getString(3));
                Registros.setFactura(tabla.getString(4));
                Registros.setTotal(tabla.getFloat(5));
                Registros.setMonto_credito(tabla.getFloat(6));
                Registros.setMonto_contado(tabla.getInt(7));
                Registros.setDias_de_credito(tabla.getInt(8));
                Registros.setTotal_abonado(tabla.getFloat(9));
                Registros.setId_cliente(tabla.getInt(10));
                Registros.setId_persona(tabla.getInt(11));
                Registros.setSaldo_pendiente(tabla.getFloat(12));
                Registros.setFecha_limite(tabla.getString(13));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosAbono> buscarCreditosCliente(String pNit, String pUsuario, String pTerminal) {

        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();
        String parametro = "";


        if (pNit == null || pNit.equals("")) {
            parametro = "";
        } else {
            parametro = "    and c.nit_persona = '" + pNit + "' ";
        }

        String sql = "select  a.id_factura as id_factura, "
                + "        c.nit_persona as nit, "
                + "        ifnull(a.nombre_factura, c.nom_persona) as nombre,  "
                + "        concat(a.serie_factura, ' ', a.num_factura) as factura, "
                + "        a.total_factura as total,  "
                + "        a.monto_credito as credito,  "
                + "        a.monto_contado as contado, "
                + "        a.dias_de_credito as dias_credito,  "
                + "        d.monto as total_abonado, "
                + "        b.id_cliente as id_cliente,  "
                + "        c.id_persona as id_persona, "
                + "        a.monto_credito - d.monto as saldo_pendiente, "
                + "        date_format(DATE_ADD(a.fecha_emision_factura, INTERVAL a.dias_de_credito DAY), '%d-%m-%Y') as fecha_limite, "
                + "        e.serie_factura_fel as serie_fel, "
                + "        e.numero_factura_fel as numero_fel "
                + "from    m_factura as a left join m_factura_fel as e on a.id_factura = e.id_factura , "
                + "        m_cliente as b, "
                + "        m_persona as c, "
                + "        ( "
                + "        select  id_factura, "
                + "                sum(monto_abono) as monto, "
                + "                sum(saldo_pendiente) as saldo "
                + "        from m_abono group by id_factura "
                + "        ) as d "
                + "where   a.tipo_venta in ('Credito', 'Credito / Contado') "
                + "    and a.id_cliente = b.id_cliente "
                + "    and b.id_persona = c.id_persona "
                + "    and a.id_factura = d.id_factura "
                + "    and a.estado_factura in ('Credito', 'Pendiente')"
                + "    and a.monto_credito > d.monto " + parametro
                + "order by c.nit_persona, a.id_factura asc";
        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Abono de Clientes", "Listar Registros", pUsuario, pTerminal);
            ObjetosAbono Registros;
            while (tabla.next()) {
                Registros = new ObjetosAbono();
                Registros.setId_factura(tabla.getInt(1));
                Registros.setNit_persona(tabla.getString(2));
                Registros.setNom_persona(tabla.getString(3));
                Registros.setFactura(tabla.getString(4));
                Registros.setTotal(tabla.getFloat(5));
                Registros.setMonto_credito(tabla.getFloat(6));
                Registros.setMonto_contado(tabla.getInt(7));
                Registros.setDias_de_credito(tabla.getInt(8));
                Registros.setTotal_abonado(tabla.getFloat(9));
                Registros.setId_cliente(tabla.getInt(10));
                Registros.setId_persona(tabla.getInt(11));
                Registros.setSaldo_pendiente(tabla.getFloat(12));
                Registros.setFecha_limite(tabla.getString(13));
                Registros.setSerie_fel(tabla.getString(14));
                Registros.setNumero_fel(tabla.getString(15));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public float obtieneComisionEmpleado(Integer pId_factura, String pUsuario, String pTerminal) {

        float vPorcentaje_comision = 0;

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "select a.porcentaje_comision from m_empleado a, m_factura b where a.id_empleado = b.id_empleado and b.id_Factura = " + pId_factura;

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Abono", "Obtener Comision Empleado", pUsuario, pTerminal);
            while (tabla.next()) {
                vPorcentaje_comision = tabla.getFloat("porcentaje_comision");
            }

        } catch (Exception error) {
            System.out.println("EC ACCESO_ABONO:obtieneComisionEmpleado " + error);
            return 0;
        }

        return vPorcentaje_comision;
    }

    public ArrayList<ObjetosAbono> listadoFacturasCredito(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "select id_factura, id_cliente, fecha_emision_factura,"
                + " id_empleado, tipo_venta, total_factura, monto_credito,"
                + " monto_contado, comision_factura, dias_de_credito, sysdate() "
                + "from m_factura where estado_factura = 'Cancelado'"
                + " and tipo_venta <> 'Contado'";

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Abono", "Listar Facturas Credito", pUsuario, pTerminal);
            ObjetosAbono Registros;
            while (tabla.next()) {
                Registros = new ObjetosAbono();
                Registros.setId_factura(tabla.getInt("id_factura"));
                Registros.setId_cliente(tabla.getInt("id_cliente"));
                Registros.setFecha_emision_factura(tabla.getString("fecha_emision_factura"));
                Registros.setId_empleado(tabla.getInt("id_empleado"));
                Registros.setTipo_venta(tabla.getString("tipo_venta"));
                Registros.setTotal_factura(tabla.getFloat("total_factura"));
                Registros.setMonto_credito(tabla.getFloat("monto_credito"));
                Registros.setMonto_contado(tabla.getFloat("monto_contado"));
                Registros.setComision_factura(tabla.getFloat("comision_factura"));
                Registros.setDias_de_credito(tabla.getInt("dias_de_credito"));
                Registros.setFecha(tabla.getString("fecha"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosAbono> listadoCreditosPagados(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "select id_factura, fecha_abono, monto_abono, saldo_pendiente, "
                + "dias_transcurridos, comision_sobre_abono from m_abono "
                + "where saldo_pendiente = 0";

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Abono", "Listar Creditos Pagados", pUsuario, pTerminal);
            ObjetosAbono Registros;
            while (tabla.next()) {
                Registros = new ObjetosAbono();
                Registros.setId_factura(tabla.getInt("id_factura"));
                Registros.setFecha_abono(tabla.getString("fecha_abono"));
                Registros.setMonto_abono(tabla.getFloat("monto_abono"));
                Registros.setSaldo_pendiente(tabla.getFloat("saldo_pendiente"));
                Registros.setDias_transcurridos(tabla.getInt("dias_transcurridos"));
                Registros.setComision_sobre_abono(tabla.getFloat("comision_sobre_abono"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }

    public ArrayList<ObjetosAbono> listadoCreditosPendientes(String pUsuario, String pTerminal) {
        ArrayList lista = new ArrayList();
        AccesoInventario Acceso = new AccesoInventario();

        String sql = "select id_factura, fecha_abono, monto_abono, saldo_pendiente, dias_transcurridos, comision_sobre_abono from m_abono where saldo_pendiente > 0";

        try {
            ResultSet tabla = Acceso.listarRegistros(sql, "Abono", "Listar Creditos Pendientes", pUsuario, pTerminal);
            ObjetosAbono Registros;
            while (tabla.next()) {
                Registros = new ObjetosAbono();
                Registros.setId_factura(tabla.getInt("id_factura"));
                Registros.setFecha_abono(tabla.getString("fecha_abono"));
                Registros.setMonto_abono(tabla.getFloat("monto_abono"));
                Registros.setSaldo_pendiente(tabla.getFloat("saldo_pendiente"));
                Registros.setDias_transcurridos(tabla.getInt("dias_transcurridos"));
                Registros.setComision_sobre_abono(tabla.getFloat("comision_sobre_abono"));
                lista.add(Registros);
            }
        } catch (Exception error) {
            return null;
        } finally {
            Acceso.desconectar();
        }
        return lista;
    }
    
    public String anularAbono(String id, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        String resultado = "";
        String sql  = "UPDATE m_abono SET saldo_pendiente = 0, comision_sobre_abono = 0 WHERE monto_abono = 0 AND id_factura =  " + id;

        try {
            resultado = Acceso.ejecutarConsulta(sql, "Ajuste Inventario", "Actualizar Ajuste", pUsuario, pTerminal);
        } catch (Error error) {
            resultado = "Error en la anulacion del Abono. \n" + error;
        } finally {
            Acceso.desconectar();
        }
        
        return resultado;
        
    }
    
}