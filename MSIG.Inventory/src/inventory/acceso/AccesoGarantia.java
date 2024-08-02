package inventory.acceso;

import inventory.objetos.ObjetosFalla;
import inventory.objetos.ObjetosGarantiaCliente;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class AccesoGarantia {

    public int insertarReclamo(ObjetosGarantiaCliente pGarantia, String pUsuario, String pTerminal) {

        AccesoInventario Acceso = new AccesoInventario();
        String sql = "";
        sql = "insert into m_reclamo_garantias values(NULL,'" + pGarantia.getDescripcion() + "','" + pGarantia.getMarca() + "','"
                + pGarantia.getIdFactura() + "','" + pGarantia.getIdProducto() + "','" + pGarantia.getSerieproducto() + "','" + pGarantia.getCliente()
                + "','" + pGarantia.getNit() + "','" + pGarantia.getFalla() + "','" + pGarantia.getFechaGuardar() + "','"
                + pUsuario + "','" + pTerminal + "','" + pGarantia.getObservaciones() + "','" + pGarantia.getTiempoGarantia() + "','"
                + pGarantia.getSolucion() + "','" + pGarantia.getIdProductoEntregar() + "','" + pGarantia.getIdFacturaEntregar() + "','" + pGarantia.getSerieEntregada()
                + "','" + pGarantia.getProductoEntregar() + "','" + pGarantia.getMarcaEntregar() + "','" + pGarantia.getComentarioEntregar() + "')";
        
        try {
            return Acceso.ejecutarConsultaKey(sql, "Garantias", "Insertar Reclamo", pUsuario, pTerminal);
        } catch (Error error) {
            error.getMessage();
            return 0;
        } finally {
            Acceso.desconectar();
        }
        
    }

    public ArrayList<ObjetosFalla> listarFallas(String pUsuario, String pTerminal) {
        
        ArrayList lista = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();
        String sql = "SELECT * FROM m_falla WHERE estado_falla = 'Activo'";
        
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias", "listarFallas", pUsuario, pTerminal);
            ObjetosFalla objetofallas;

            while (tabla.next()) {
                objetofallas = new ObjetosFalla();
                objetofallas.setId_falla(tabla.getInt("id_falla"));
                objetofallas.setEstado_categoria(tabla.getString("estado_falla"));
                objetofallas.setNombre_falla(tabla.getString("nombre_falla"));

                lista.add(objetofallas);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        
        return lista;
        
    }

    public ArrayList<ObjetosGarantiaCliente> buscarSerie(String pSerie, String condicion,  String pUsuario, String pTerminal) {
        ArrayList list = new ArrayList();
        AccesoInventario acceso = new AccesoInventario();

        String sql = "SELECT * FROM m_serie WHERE numero_serie = '" + pSerie + "' AND estado IN (" + condicion + ")";
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias", "buscarSerie", pUsuario, pTerminal);
            ObjetosGarantiaCliente detalles;
            if (tabla.first()) {
                detalles = new ObjetosGarantiaCliente();
                String idcliente = this.buscarIdCliente(tabla.getString("id_factura"), pUsuario, pTerminal);
                String idpersona = this.buscarIdPersona(idcliente, pUsuario, pTerminal);
                String nit = this.buscarNit(idpersona, pUsuario, pTerminal);
                detalles.setSerieproducto(tabla.getString("numero_serie"));
                detalles.setIdFactura(tabla.getString("id_factura"));
                detalles.setIdProducto(tabla.getString("id_producto"));
                detalles.setDescripcion(tabla.getString("descripcion"));
                detalles.setNit(nit);
                detalles.setEstadoRegistro(tabla.getString("estado"));
                detalles.setCliente(this.buscarPersona(nit, pUsuario, pTerminal));
                detalles.setMarca(this.buscarMarca(tabla.getString("id_producto"), pUsuario, pTerminal));
                detalles.setFechaInicio(this.buscarFechaInicioGarantia(pSerie, pUsuario, pTerminal));
                detalles.setFechaFin(this.buscarFechaFinGarantia(pSerie, pUsuario, pTerminal));
                detalles.setTiempoGarantia(String.valueOf(this.calcularGarantia(this.obtenerFechaActual(), this.buscarFechaFinGarantia(pSerie, pUsuario, pTerminal), pUsuario, pTerminal)));
                tabla.getString("id_factura");
                detalles.setIdSerie(tabla.getInt("id_serie"));
                list.add(detalles);
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return list;
    }

    private String buscarMarca(String idProducto, String pUsuario, String pTerminal) {

        AccesoInventario acceso = new AccesoInventario();
        String marca = null;
        String sql = "SELECT marca_producto FROM m_producto WHERE id_producto = '" + idProducto + "'";
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias", "buscarMarca", pUsuario, pTerminal);

            while (tabla.next()) {
                marca = tabla.getString("marca_producto");

            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return marca;
    }
    
    public int buscarCantidadDetalleSerie(String numeroSerie, String pUsuario, String pTerminal) {

        AccesoInventario acceso = new AccesoInventario();
        int total = 0;
        String sql = "select ifnull(count(id_serie),0) as total from d_serie where id_serie in (select id_serie from m_serie where numero_serie = '" + numeroSerie + "')";
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias", "buscarCantidadDetalleSerie", pUsuario, pTerminal);

            while (tabla.next()) {
                total = tabla.getInt("TOTAL");

            }
        } catch (Exception error) {
            total = 0;
        } finally {
            acceso.desconectar();
        }
        return total;
    }

    public String buscarIdCliente(String idFactura, String pUsuario, String pTerminal) {

        AccesoInventario acceso = new AccesoInventario();
        String idcliente = null;
        String sql = "SELECT id_cliente FROM m_factura WHERE id_factura = '" + idFactura + "'";
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias", "buscarCliente", pUsuario, pTerminal);

            if (tabla.first()) {
                idcliente = tabla.getString("id_cliente");
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return idcliente;
    }

    private String buscarIdPersona(String pIdcliente, String pUsuario, String pTerminal) {

        AccesoInventario acceso = new AccesoInventario();
        String query = "Select id_persona from m_cliente where id_cliente ='" + pIdcliente + "'";
        String idpersona = null;
        try {
            ResultSet tablaPersona = acceso.listarRegistros(query, "Garantias", "buscarIdPersona", pUsuario, pTerminal);
            if (tablaPersona.first()) {
                idpersona = tablaPersona.getString("id_persona");
            }
        } catch (Exception e) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return idpersona;

    }

    private String buscarNit(String pIdpersona, String pUsuario, String pTerminal) {
        AccesoInventario acceso = new AccesoInventario();
        String nit = null;
        String sqlcliente = "SELECT * FROM m_persona WHERE id_persona = '" + pIdpersona + "'";
        try {
            ResultSet tabla = acceso.listarRegistros(sqlcliente, "Garantias", "buscarNit", pUsuario, pTerminal);
            if (tabla.first()) {
                nit = tabla.getString("nit_persona");
            }
        } catch (Exception e) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return nit;

    }

    private String buscarPersona(String pNit, String pUsuario, String pTerminal) {
        AccesoInventario acceso = new AccesoInventario();
        String persona = null;
        String sqlcliente = "SELECT * FROM m_persona WHERE nit_persona = '" + pNit + "'";
        try {
            ResultSet tabla = acceso.listarRegistros(sqlcliente, "Garantias", "buscarPersona", pUsuario, pTerminal);
            if (tabla.first()) {
                persona = tabla.getString("nom_persona");
            }
        } catch (Exception e) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return persona;
    }

    private String buscarFechaFinGarantia(String pSerie, String pUsuario, String pTerminal) {

        AccesoInventario acceso = new AccesoInventario();
        String fechaGarantia = null;
        String sql = "SELECT fecha_limite_garantia_cliente FROM m_serie WHERE numero_serie = '" + pSerie + "'";
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias", "buscarFechaGarantia", pUsuario, pTerminal);

            if (tabla.first()) {
                fechaGarantia = tabla.getString("fecha_limite_garantia_cliente");
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return fechaGarantia;
    }

    private String calcularGarantia(String fechaInicio, String FechaFin, String pUsuario, String pTerminal) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        AccesoInventario acceso = new AccesoInventario();
        int meses = 0;
        int dias = 0;
        int anios = 0;
        String garantia = null;
        String sqlMes = "SELECT TIMESTAMPDIFF(MONTH, '" + fechaInicio + "', '" + FechaFin + "') AS meses";
        String sqlDia = "SELECT TIMESTAMPDIFF(DAY, '" + fechaInicio + "', '" + FechaFin + "') AS dias";
        String sqlAnio = "SELECT TIMESTAMPDIFF(YEAR, '" + fechaInicio + "', '" + FechaFin + "') AS Anio";
        try {

            ResultSet tabla = acceso.listarRegistros(sqlMes, "Garantias", "CalculaGarantia", pUsuario, pTerminal);

            if (tabla.first()) {
                meses = tabla.getInt("meses");
                if (meses < 1) {
                    tabla = acceso.listarRegistros(sqlDia, "Garantias", "CalculoGarantia", pUsuario, pTerminal);
                    if (tabla.first()) {
                        dias = tabla.getInt("dias");
                        if (dias >= 1) {
                            garantia = dias + " dias";

                        }
                        if (dias < 1) {
                            garantia = "Sin garantia";
                        }
                    }
                } else if (meses >= 1 & meses < 12) {
                    int diasextra = meses - (dias * 30);
                    garantia = meses + " Meses y " + diasextra + " dias";

                } else if (meses > 12) {

                    tabla = acceso.listarRegistros(sqlAnio, "Garantias", "CalculaGarantia", pUsuario, pTerminal);
                    if (tabla.first()) {
                        anios = tabla.getInt("Anio");
                        if (anios >= 1) {
                            int extra = meses - (anios * 12);
                            garantia = anios + " Años y " + extra + " Meses";
                        }
                    }
                }

            } else {
                return null;
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return garantia;
    }

    private String obtenerFechaActual() {
        try {
            Date date = new Date();
            String fecha = new SimpleDateFormat("YYYY-MM-dd").format(date);
            return String.valueOf(fecha);
        } catch (NullPointerException ex) {
            return null;
        }


    }

    public String actualizarSerieRecibidaID(String pEstado, String pIdSerie, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_serie SET id_factura = 0, estado = '" + pEstado + "', fecha_limite_garantia_cliente = 'S/G' WHERE id_serie = " + pIdSerie;
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Garantia Cliente", "Actualizar Serie Recibida ID", pUsuario, pTerminal);
        } catch (Error error) {
            System.out.println(error.getMessage());
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarSerieRecibidaNumero(String pEstado, String pNumeroSerie, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_serie SET estado = '" + pEstado + "' WHERE estado = 'Garantia Proveedor' AND numero_serie = '" + pNumeroSerie + "'";
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Garantia Proveedor", "Actualizar Serie Recibida Numero", pUsuario, pTerminal);
        } catch (Error error) {
            System.out.println(error.getMessage());
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    public String actualizarSerieNuevaRecibida(String pEstado, String pNuevaSerie, String pNumeroSerie, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        System.out.println("Ejecutando consulta en base de datos");
        String sql = "UPDATE m_serie SET numero_serie = '" + pNuevaSerie + "', estado = '" + pEstado + "' WHERE estado = 'Garantia Proveedor' AND  numero_serie = '" + pNumeroSerie + "'";
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Garantia Proveedor", "Actualizar Nueva Serie Recibida", pUsuario, pTerminal);
        } catch (Error error) {
            System.out.println(error.getMessage());
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    public String actualizarSerieEntregada(String pEstado, String pIdSerie, String fechaGarantia, int idFactura, String pUsuario, String pTerminal) {
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE m_serie SET estado = '" + pEstado + "',id_factura =" + idFactura + ",fecha_limite_garantia_cliente = '" + fechaGarantia + "' WHERE id_serie=" + pIdSerie + ";";

        try {
            return Acceso.ejecutarConsulta(sql, "Garantia", "Actualizar Serie Entregada", pUsuario, pTerminal);
        } catch (Error error) {
            System.out.println(error.getMessage());
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }
    
    
    public String actualizarDetalleSerieGarantia(String pEstado, String pIdSerie, String pUsuario, String pTerminal) {
        
        AccesoInventario Acceso = new AccesoInventario();
        String sql = "UPDATE d_serie SET estado = '" + pEstado + "' WHERE id_serie=" + pIdSerie + ";";
        System.out.println(sql);
        try {
            return Acceso.ejecutarConsulta(sql, "Garantia", "Actualizar Detalle Serie", pUsuario, pTerminal);
        } catch (Error error) {
            System.out.println(error.getMessage());
            return error.getMessage();
        } finally {
            Acceso.desconectar();
        }
    }

    private String buscarFechaInicioGarantia(String pSerie, String pUsuario, String pTerminal) {
        String sql = "Select str_to_date(date_format(fechacreacion, "
                + "'%Y-%m-%d'),'%Y-%m-%d') as fecha from m_serie where numero_serie = '" + pSerie + "'";

        System.out.println(sql);
        AccesoInventario acceso = new AccesoInventario();
        String fechacreacion = null;
        try {

            ResultSet tabla = acceso.listarRegistros(sql, "Garantias", "buscarFechaGarantia", pUsuario, pTerminal);

            if (tabla.first()) {
                fechacreacion = tabla.getString("fecha");
            }
        } catch (Exception error) {
            return null;
        } finally {
            acceso.desconectar();
        }
        return fechacreacion;
    }
    
}
