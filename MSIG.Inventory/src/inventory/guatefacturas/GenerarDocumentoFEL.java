/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.guatefacturas;

import inventory.acceso.AccesoArchivo;
import inventory.acceso.AccesoInventario;
import inventory.servicios.Matematicas;
import inventory.vistas.Inventory;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.net.ssl.SSLSession;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author LIBRERIA SAN JUAN
 */public class GenerarDocumentoFEL {
    
    private static String Log = "";
    private static Matematicas matematica = new Matematicas();
    private static AccesoArchivo archivo = new AccesoArchivo();
   
    private void agregarLog(String texto) {
        this.Log = this.Log + "GENERAR DOCUMENTO" + ": " + texto + " \n";
        Inventory.txtLog.setText(Log);
    }
    
    public  Resultado generarFacturaElectronica(String direccion, int pIdFactura, String pNombreVenta, String pEnlaceConexion, 
            String pPantalla, String pOpcion, String pUsuario, String pTerminal, String pTipoDocumento) {
        
        Resultado resultado = new Resultado();
        boolean firma_electronica = false;
        String direcion_de_envio = "";
        String result = "";
        String cadena = "";
        
        try {

            //solicitar la factura a guatefacturas a travez del servicio web
            BigDecimal x = new BigDecimal(archivo.leer("[Establecimiento]"));
            BigDecimal y = new BigDecimal(1);
            System.out.println("Ejecutando Web Service");
            System.setProperty("javax.net.ssl.keyStore", archivo.leer("[KeyStore]"));
            System.setProperty("javax.net.ssl.trustStore", archivo.leer("[KeyStore]"));
            System.setProperty("javax.net.ssl.keyStorePassword", archivo.leer("[KeyStorePassword]"));
            System.setProperty("javax.net.ssl.trustStorePassword", archivo.leer("[KeyStorePassword]"));
            System.out.println("Enviando informacion");

            //obtener cadena que se enviara a gautefacturas
            cadena = generaFacturaXML(pIdFactura, pNombreVenta, pPantalla, pOpcion, pUsuario, pTerminal, pTipoDocumento);
            System.out.println(cadena);

            agregarLog("Solicita DTE a GuateFacturas ID Factura ");
            
            //obtener firma electronica y generar factura
            try {
                String usuario = archivo.leer("[GuateFacturasUser]");
                String clave = archivo.leer("[GuateFacturasPassword]");
                String nit = archivo.leer("[NIT]");
                String estacion = archivo.leer("[Station]");
                System.out.println("Cadena FEL: " + cadena);
                result = generaDocumento(direccion, usuario, clave, nit, x, y, estacion, "R", cadena);
                System.out.println("Resultado:" + result);
                LeerXML l = new LeerXML();
                resultado = l.procesarXMLSimple(result);
                resultado.setResultado(result); //guardar el resultado para saber si hay errores
                firma_electronica = true;
            } catch (IOException error_xml) {
                firma_electronica = false;
                agregarLog("No se pudo generar factura electronica.");
                agregarLog("ID Factura: " + pIdFactura);
                agregarLog("Candena Enviada: " + cadena );
                agregarLog("Error producido: " + result);
            }
            
        } catch (IOException Error) {
            agregarLog("Generar Documento FEL: " + Error.toString());
            agregarLog(result);
            agregarLog(cadena);
        }
        
        return resultado;

    }
    
    private String generaFacturaXML(int pIdFactura, String pNombreVenta, String pPantalla, String pOpcion, 
            String pUsuario, String pTerminal, String pTipoDocumento){
        
        String correo_electronico = "";
        
        String xml_2 = "";
        
        double total_bruto = 0;
        double total_neto = 0;
        double total_iva = 0;
        double total_descuento = 0;
        double total_subtotal = 0;
        
        Detalles detalle = buscarDetalle(pIdFactura, pPantalla, pOpcion, pUsuario, pTerminal);
        
        for(int x=0; x<detalle.getProductos().size(); x++) {
            
            Productos producto  = detalle.getProductos().get(x);
            
            Double neto = producto.getImpBruto() - producto.getImpDescuento() - producto.getImpIva();
            
            String xml_t =  "		<Productos>\n" +
                            "			<Producto>" + producto.getProducto() + "</Producto>\n" +
                            "			<Descripcion>" + producto.getDescripcion() + "</Descripcion>\n" +
                            "			<Medida>" + producto.getMedida() + "</Medida>\n" +
                            "		 	<Cantidad>" + producto.getCantidad() + "</Cantidad>\n" +
                            "			<Precio>" + producto.getPrecio() + "</Precio>\n" +
                            "			<PorcDesc>" + producto.getPorcDesc() + "</PorcDesc>\n" +
                            "			<ImpBruto>" + producto.getImpBruto() + "</ImpBruto>\n" +
                            "			<ImpDescuento>" + producto.getImpDescuento() + "</ImpDescuento>\n" +
                            "			<ImpExento>" + producto.getImpExento() + "</ImpExento>\n" +
                            "			<ImpOtros>" + producto.getImpOtros() + "</ImpOtros>\n" +
                            "			<ImpNeto>" + producto.getImpNeto() + "</ImpNeto>\n" +
                            "			<ImpIsr>" + producto.getImpIsr() + "</ImpIsr>\n" +
                            "			<ImpIva>" + producto.getImpIva() + "</ImpIva>\n" +
                            "			<ImpTotal>" + producto.getImpTotal() + "</ImpTotal>\n" +
                            "			<TipoVentaDet>" + producto.getTipoVenta() + "</TipoVentaDet>\n" + 
                            "			<DatosAdicionalesProd>" + producto.getDatosAdicionalesProd() + "</DatosAdicionalesProd>\n" +
                            "		</Productos>\n";
            
            xml_2 += xml_t;
            
            //sumatoria de totales del dtalle para el encabezdo
            total_bruto = matematica.aproxima(total_bruto + producto.getImpBruto(), 2);
            total_descuento = matematica.aproxima(total_descuento +  producto.getImpDescuento(), 2);
            total_neto = matematica.aproxima(total_neto + producto.getImpNeto(), 2);
            total_iva = matematica.aproxima(total_iva + producto.getImpIva(), 2);
            total_subtotal = matematica.aproxima(total_subtotal + producto.getImpTotal(), 2);
            
        }
        
        System.out.println("---------------------------------------");
        System.out.println("Totales Generales Enviados");
        System.out.println("---------------------------------------");
        System.out.println("Bruto: " + total_bruto);
        System.out.println("Descuento: " + total_descuento);
        System.out.println("Neto: " + total_neto);
        System.out.println("IVA: " + total_iva);
        System.out.println("Total: "+ total_subtotal);
        System.out.println("---------------------------------------");
        
        Encabezado encabezado = buscarEncabezado(pIdFactura, pPantalla, pOpcion, pUsuario, pTerminal);
        
        //Buscar las diferencia que exitan en el detalle de la factura con el encabezado
        double diferencia_bruto = total_bruto - Double.parseDouble(encabezado.getTotales().getBruto());
        double diferencia_descuento = total_descuento - Double.parseDouble(encabezado.getTotales().getDescuento());
        double diferencia_neto = total_neto - Double.parseDouble(encabezado.getTotales().getNeto());
        double diferencia_iva = total_iva - Double.parseDouble(encabezado.getTotales().getIva());
        double diferencia_total = total_subtotal - Double.parseDouble(encabezado.getTotales().getTotal());
        
        System.out.println("---------------------------------------");
        System.out.println("Diferencias entre datos de Factura");
        System.out.println("---------------------------------------");
        System.out.println("Bruto: " + diferencia_bruto);
        System.out.println("Descuento: " + diferencia_descuento);
        System.out.println("Neto: " + diferencia_neto);
        System.out.println("IVA: " + diferencia_iva);
        System.out.println("Total: " + diferencia_total);
        System.out.println("---------------------------------------");
        
        if(encabezado.getReceptor().getNITReceptor().equals("C/F") || encabezado.getReceptor().getNITReceptor().equals("c/f")) {
            encabezado.getReceptor().setNombre(pNombreVenta);
        }
        
        //Validar el tipo de documento para enviar el identificador que solicita guatefactruas
        //4 = NIT
        //2 = CUI / DPI
        //3 = Numero de Pasaporte
        
        String tipo_receptor = "";
        
        if(pTipoDocumento.equals("NIT")) {
            tipo_receptor = "<TipoReceptor>4</TipoReceptor>";
        }
        
        if(pTipoDocumento.equals("DPI")) {
            tipo_receptor = "<TipoReceptor>2</TipoReceptor>";
        }
        
        if(pTipoDocumento.equals("PAS")) {
            tipo_receptor = "<TipoReceptor>3</TipoReceptor>";
        }
        
        String xml_1 =  "<DocElectronico>\n" +
                        "	<Encabezado>\n" +
                        "		<Receptor>\n" +
                        "			<NITReceptor>" + encabezado.getReceptor().getNITReceptor() + "</NITReceptor>\n" +
                        "			<Nombre>" + encabezado.getReceptor().getNombre() + "</Nombre>\n" +
                        "			<Direccion>" + encabezado.getReceptor().getDireccion()+ "</Direccion>\n" +
                        "		</Receptor>\n" +
                        "		<InfoDoc>\n" +
                        "			<TipoVenta>" + encabezado.getInfoDoc().getTipoVenta() + "</TipoVenta>\n" +
                        "			<DestinoVenta>" + encabezado.getInfoDoc().getDestinoVenta() + "</DestinoVenta>\n" +
                        "			<Fecha>" + encabezado.getInfoDoc().getFecha() + "</Fecha>\n" +
                        "			<Moneda>" + encabezado.getInfoDoc().getMoneda() + "</Moneda>\n" +
                        "			<Tasa>" + encabezado.getInfoDoc().getTasa() + "</Tasa>\n" +
                        "			<Referencia>" + encabezado.getInfoDoc().getReferencia() + "</Referencia>\n" +
                        "			<NumeroAcceso></NumeroAcceso>\n" + 
                        "			<SerieAdmin>" + encabezado.getInfoDoc().getSerieAdmin() + "</SerieAdmin>\n" + 
                        "			<NumeroAdmin>" + encabezado.getInfoDoc().getNumeroAdmin() + "</NumeroAdmin>\n" + 
                        "			<Reversion></Reversion>\n" +
                        "		</InfoDoc>\n" +
                        "		<Totales>\n" +
                        "			<Bruto>" + encabezado.getTotales().getBruto() + "</Bruto>\n" +
                        "			<Descuento>" + encabezado.getTotales().getDescuento() + "</Descuento>\n" +
                        "			<Exento>" + encabezado.getTotales().getExento() + "</Exento>\n" +
                        "			<Otros>" + encabezado.getTotales().getOtros() + "</Otros>\n" +
                        "			<Neto>" + total_neto + "</Neto>\n" +
                        "			<Isr>" + encabezado.getTotales().getIsr() + "</Isr>\n" +
                        "			<Iva>" + total_iva + "</Iva>\n" +
                        "			<Total>" + encabezado.getTotales().getTotal() + "</Total>\n" +
                        "		</Totales>\n" +
                        "		<DatosAdicionales>\n" +
                        "                   <Email>" + encabezado.getDatosAdicionales().getEmail() + "</Email>\n" +
                        "                   <Enviar>" + encabezado.getDatosAdicionales().getEnviar() + "</Enviar>\n"+
                        "                   <Observaciones>" + encabezado.getDatosAdicionales().getObservaciones() + "</Observaciones>\n"+
                        "                   " + tipo_receptor + "\n"+
                        "		</DatosAdicionales>\n" +
                        "	</Encabezado>\n" +
                        "	<Detalles>\n";
        
        
        //continuacion del xml para el envio de la factura 
        String xml_3 =  "		<DocAsociados>\n" +
                        "			<DASerie></DASerie>\n" +
                        "			<DAPreimpreso></DAPreimpreso>\n" +
                        "		</DocAsociados>\n" +
                        "	</Detalles>\n" +
                        "</DocElectronico>";
        

        return xml_1 + xml_2 + xml_3;
        
    }
    
    
    private static String generaDocumento(String direccion, java.lang.String pUsuario, java.lang.String pPassword, java.lang.String pNitEmisor, 
            java.math.BigDecimal pEstablecimiento, java.math.BigDecimal pTipoDoc, java.lang.String pIdMaquina, 
            java.lang.String pTipoRespuesta, java.lang.String pXml) {
        
       String response = "";

        System.out.println("Enviando información a Guatefacturas");
        
        try {
            
            System.out.println("Buscando ubicacion WSDL");
            dbguatefac.guatefac.Guatefac_Service service = new dbguatefac.guatefac.Guatefac_Service();
            System.out.println("Se obtuvo con exito el archivo WSDL para la conexion");
            dbguatefac.guatefac.Guatefac port = service.getGuatefacPort();
            String newAddress = "";
            if(direccion.equals("GFN")) {
                newAddress = archivo.leer("[WebService]");
            } else {
                newAddress = archivo.leer("[WebServiceContingencia]");
            }
            ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, newAddress);
            System.out.println("Verificando URL del servidor");
            
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession ssls) {
                   return hostname.equals("192.220.23.42");
                }
            });
            
            System.out.println("Ingresando credenciales al servicio");
            
            try { 
                BindingProvider prov = (BindingProvider) port;
                prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, archivo.leer("[UserWS]"));
                prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, archivo.leer("[PassWS]"));
                System.out.println("Ingreso autorizado en Guatefacturas");
            } catch (Exception e) {
                System.out.println("Error en ingreso: " + e.getMessage());
            }
            
            System.out.println("Llamando metodo generaDocumento par la generacion del DTE");
            System.out.println("Establecimiento: " + pEstablecimiento + " Maquina: " + pIdMaquina);
            
            try {
                response = port.generaDocumento(pUsuario, pPassword, pNitEmisor, pEstablecimiento, pTipoDoc, pIdMaquina, 
                        pTipoRespuesta, pXml);
            } catch(Exception error) {
                System.out.println("Error en el metododo generaDocumento.\n" + error.toString());
            }

        } catch (IOException Error) {
            System.out.println(Error.getMessage());
        }

        return response; 
        
        //return port.generaDocumento(pUsuario, pPassword, pNitEmisor, pEstablecimiento, pTipoDoc, pIdMaquina, pTipoRespuesta, pXml);
    }
    
    private Encabezado buscarEncabezado(int pIdFactura, String pPantalla, String pOpcion, String pUsuario, String pTerminal){
        
        System.out.println("Buscar Encabezado Id Factura: " + pIdFactura);

        Encabezado encabezado = new Encabezado();
        Receptor receptor = new Receptor();
        InfoDoc infodoc =  new InfoDoc();
        Totales totales = new Totales();
        DatosAdicionales datosadicionales = new DatosAdicionales();
        
        String sql = "";
        
        //obtener valores para datos basicos de guatefacturas
        //desde del archivo de configuraciones
        String tipoventa = "";
        String destinoventa = "";
        String moneda = "";
        String tasa = "";
        String enviar = "";
        String iva = "";
        
        try {
            tipoventa = archivo.leer("[TipoVenta]");
            destinoventa = archivo.leer("[DestinoVenta]");
            moneda = archivo.leer("[Moneda]");
            tasa = archivo.leer("[Tasa]");
            enviar = archivo.leer("[Enviar]");
            iva = archivo.leer("[IVA]");
        } catch(IOException exception) {
            System.out.println("No se pudieron leer los datos para la consulta del encabezado de Guatefacturas");
            System.out.println(exception.toString());
        }
        
        try {
            
            sql = "SELECT  c.nit_persona AS nit,\n"
                    + "        c.nom_persona AS nombre,\n"
                    + "        b.dir_cliente AS direccion,\n"
                    + "        '" + tipoventa + "' AS tipoventa,\n"
                    + "        '" + destinoventa + "' AS destinoventa,\n"
                    + "        DATE_FORMAT(NOW(), '%d/%m/%Y') as fecha,\n"
                    + "        '" + moneda + "' AS moneda,\n"
                    + "        '" + tasa + "' AS tasa,\n"
                    + "        a.id_factura AS referencia,\n"
                    + "        '' AS numeroacceso,\n"
                    + "        a.serie_factura AS serieadmin,\n"
                    + "        a.num_factura AS numeroadmin,\n"
                    + "        '' AS reversion,\n"
                    + "        ROUND(a.total_bruto_factura, 2) AS bruto,\n"
                    + "        ROUND((total_bruto_factura - a.total_factura), 2) AS descuento,\n"
                    + "        0 AS exento,\n"
                    + "        0 AS otros,\n"
                    + "        ROUND((a.total_factura / 1." + iva + "), 2) AS neto,\n"
                    + "        0 AS isr,\n"
                    + "        ROUND((a.total_factura - (a.total_factura / 1." + iva + ")), 2) as iva,\n"
                    + "        ROUND(a.total_factura, 2) AS total,\n"
                    + "        b.email_cliente AS email,\n"
                    + "        '" + enviar + "' AS enviar,\n"
                    + "        CONCAT(a.serie_factura, '-', a.num_factura) AS observaciones\n"
                    + " FROM   m_factura AS a,\n"
                    + "        m_cliente AS b,\n"
                    + "        m_persona AS c\n"
                    + " WHERE  a.id_cliente = b.id_cliente\n"
                    + "    AND b.id_persona = c.id_persona\n"
                    + "    AND a.id_factura = " + pIdFactura;
            
            //obtener la fecha desde JAVA
            Calendar fecha = Calendar.getInstance();
            String str_dia   = fecha.get(Calendar.DATE) < 10 ? "0" + String.valueOf(fecha.get(Calendar.DATE)) : String.valueOf(fecha.get(Calendar.DATE));
            String str_mes   = (fecha.get(Calendar.MONTH)+1) < 10 ? "0" + String.valueOf(fecha.get(Calendar.MONTH)+1) : String.valueOf(fecha.get(Calendar.MONTH)+1);
            String str_fecha = str_dia + "/" + str_mes  + "/" + String.valueOf(fecha.get(Calendar.YEAR));
           
            System.out.println("Consultar encabezado de la factura: \n" + sql);
            
            AccesoInventario Acceso = new AccesoInventario();
            ResultSet rs = Acceso.listarRegistros(sql, pPantalla, pOpcion, pUsuario, pTerminal);
            
            while(rs.next()){
                
                System.out.println("Fecha computadora: " + str_fecha);
                
                //datos receptor
                receptor.setNITReceptor(rs.getString("NIT").replace(" ", ""));
                receptor.setNombre(rs.getString("NOMBRE").replaceAll("  ", ""));
                receptor.setDireccion(rs.getString("DIRECCION").replaceAll("  ", "").replaceAll("-", "/"));
                
                //datos infodoc
                infodoc.setTipoVenta(rs.getString("TIPOVENTA"));
                infodoc.setDestinoVenta(rs.getString("DESTINOVENTA"));
                infodoc.setFecha(rs.getString("FECHA"));
                infodoc.setMoneda(rs.getString("MONEDA"));
                infodoc.setTasa(rs.getString("TASA"));
                infodoc.setReferencia(rs.getString("REFERENCIA"));
                infodoc.setNumeroAcceso(rs.getString("NUMEROACCESO"));
                infodoc.setSerieAdmin(rs.getString("SERIEADMIN"));
                infodoc.setNumeroAdmin(rs.getString("NUMEROADMIN"));
                infodoc.setReversion(rs.getString("REVERSION"));
                
                //totales
                totales.setBruto(rs.getString("BRUTO"));
                totales.setDescuento(rs.getString("DESCUENTO"));
                totales.setExento(rs.getString("EXENTO"));
                totales.setOtros(rs.getString("OTROS"));
                totales.setNeto(rs.getString("NETO"));
                totales.setIsr(rs.getString("ISR"));
                totales.setIva(rs.getString("IVA"));
                totales.setTotal(rs.getString("TOTAL"));
                
                //datos adicionales
                datosadicionales.setEmail(rs.getString("EMAIL"));
                datosadicionales.setEnviar(rs.getString("ENVIAR"));
                datosadicionales.setObservaciones(rs.getString("OBSERVACIONES"));
            }
            
            encabezado.setReceptor(receptor);
            encabezado.setInfoDoc(infodoc);
            encabezado.setTotales(totales);
            
            Acceso.desconectar();
            
        } catch (SQLException Error) {
            
            System.out.println("Ocurrio un error al buscar el encabezado de la Factura");
            System.out.println(Error.toString());
            
        }
        
        return encabezado;
    
    }
    
    private Detalles buscarDetalle(int pIdFactura, String pPantalla, String pOpcion, String pUsuario, String pTerminal){
        
        System.out.println("Buscar Detalle Id Factura: " + pIdFactura);
        
        Encabezado encabezado = new Encabezado();
        Detalles detalle = new Detalles();
        
        ArrayList<Productos> listado = new ArrayList<>();
        
        String iva = "";
        String medida = "";
        String tipoventa = "";
        
        try {
            iva = archivo.leer("[IVA]");
            medida = archivo.leer("[UnidadMedida]");
        } catch(IOException exception) {
            System.out.println("No se pudieron leer los datos para el calculo de IVA en Guatefacturas");
            System.out.println(exception.toString());
        }
        
        String sql = "";
        
        try {
            
            sql = "SELECT  a.id_producto AS Producto,\n"
                    + "        b.desc_producto AS Descripcion,\n"
                    + "        " + medida + " AS Medida,\n"
                    + "        ROUND(a.cantidad_d_factura, 2) AS Cantidad,\n"
                    + "        IF(a.id_producto='RECPRO', ROUND(a.precio_venta_d_factura, 2), ROUND(b.precio_est_producto, 2)) AS Precio,\n"
                    + "        IF(a.id_producto='RECPRO', 0, ROUND((1 - (a.precio_venta_d_factura / b.precio_est_producto)) * 100, 2)) AS PorcDesc,\n"
                    + "        IF(a.id_producto='RECPRO', ROUND(a.precio_venta_d_factura, 2), ROUND((b.precio_est_producto * a.cantidad_d_factura ), 2)) AS ImpBruto,\n"
                    + "        IF(a.id_producto='RECPRO', 0, ROUND((b.precio_est_producto * a.cantidad_d_factura ) - (a.precio_venta_d_factura * a.cantidad_d_factura), 2)) AS ImpDescuento,\n"
                    + "        ROUND(0, 4) AS ImpExento,\n"
                    + "        ROUND(0, 4) AS ImpOtros,\n"
                    + "        ROUND(((b.precio_est_producto * a.cantidad_d_factura ) - ((b.precio_est_producto * a.cantidad_d_factura ) - (a.precio_venta_d_factura * a.cantidad_d_factura))) / 1." + iva + ", 2) AS ImpNeto,\n"
                    + "        ROUND(0, 4) AS ImpIsr,\n"
                    + "        ROUND((a.precio_venta_d_factura * a.cantidad_d_factura), 2) - ROUND(((b.precio_est_producto * a.cantidad_d_factura ) - ((b.precio_est_producto * a.cantidad_d_factura ) - (a.precio_venta_d_factura * a.cantidad_d_factura))) / 1." + iva + ", 2) AS ImpIva,\n"
                    + "        ROUND((a.precio_venta_d_factura * a.cantidad_d_factura), 2) AS ImpTotal,\n"
                    + "        b.tipo_sat_producto AS TipoVentaDet,\n"
                    + "        a.comentario_d_factura AS DatosAdicionalesProd\n"
                    + "FROM    d_factura AS a,\n"
                    + "        m_producto AS b\n"
                    + "WHERE   a.id_producto = b.id_producto\n"
                    + "    AND a.id_factura = " + pIdFactura;
           
            System.out.println("Consultar detalle de la factura: \n" + sql);
            
            AccesoInventario Acceso = new AccesoInventario();
            ResultSet rs = Acceso.listarRegistros(sql, pPantalla, pOpcion, pUsuario, pTerminal);
            
            while(rs.next()){
                Productos producto = new Productos();
                producto.setProducto(rs.getString("PRODUCTO"));
                producto.setDescripcion(rs.getString("DESCRIPCION"));
                producto.setMedida(rs.getString("MEDIDA"));
                producto.setCantidad(rs.getDouble("CANTIDAD"));
                producto.setPrecio(rs.getDouble("PRECIO"));
                producto.setPorcDesc(rs.getDouble("PORCDESC"));
                producto.setImpBruto(rs.getDouble("IMPBRUTO"));
                producto.setImpDescuento(rs.getDouble("IMPDESCUENTO"));
                producto.setImpExento(rs.getDouble("IMPEXENTO"));                
                producto.setImpOtros(rs.getDouble("IMPOTROS"));
                producto.setImpNeto(rs.getDouble("IMPNETO"));
                producto.setImpIsr(rs.getDouble("IMPISR"));
                producto.setImpIva(rs.getDouble("IMPIVA"));
                producto.setImpTotal(rs.getDouble("IMPTOTAL"));
                producto.setTipoVenta(rs.getString("TIPOVENTADET"));
                producto.setDatosAdicionalesProd(rs.getString("DATOSADICIONALESPROD"));
                listado.add(producto);
            }
            
            detalle.setProductos(listado);
            
            Acceso.desconectar();
            
        } catch (SQLException Error) {
            
            System.out.println("Ocurrio un error al buscar el encabezado de la Factura");
            System.out.println(Error.toString());
            
        }
        
        System.out.println("Se encontraron " + detalle.getProductos().size() + " Productos");
        
        return detalle;
    
    }
    
}
