package inventory.guatefacturas;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LeerXML {

    public String devolverDatoString(Node dato) {

        String x = "";

        try {
            if (dato.getNodeType() == Node.ELEMENT_NODE) {
                Node datoContenido = dato.getFirstChild();
                if (datoContenido != null && datoContenido.getNodeType() == Node.TEXT_NODE) {
                    x = datoContenido.getNodeValue();
                }
            }
        } catch (Exception Error) {
            System.out.println("Error al mostrar valor de la etiqueta " + dato.toString());
        }

        System.out.println("Dato: " + dato.toString() + " Valor: " + x);

        //si encuentro el simbolo que representa un
        //amperson devolverlo como tal
        return x.replaceAll("#", "&");
    }

    public Resultado procesarXMLSimple(String valor) {

        Resultado rs = new Resultado();

        //si viene un amperson cambiar por la palabra amperson
        //despues regresar el simbolo original
        String xml = valor.replaceAll("&", "#");

        try {

            InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory.newInstance();
            DocumentBuilder creadorDocumento = fábricaCreadorDocumento.newDocumentBuilder();
            Document documento = creadorDocumento.parse(stream);
            Element raiz = documento.getDocumentElement();
            NodeList lista = raiz.getChildNodes();

            //obtener datos del xml por posicion e ingresarlo en un objeto resultado
            rs.setResultado(valor);
            try { Node serie = lista.item(0); rs.setSerie(devolverDatoString(serie)); } catch(Exception Error) { rs.setSerie("0"); }
            try { Node preimpreso = lista.item(1); rs.setPreimpreso(devolverDatoString(preimpreso)); } catch(Exception Error) { rs.setPreimpreso("0"); }
            try { Node nombre = lista.item(2); rs.setNombre(devolverDatoString(nombre)); } catch(Exception Error) { rs.setNombre("0"); }
            try { Node direccion = lista.item(3); rs.setDireccion(devolverDatoString(direccion)); } catch(Exception Error) { rs.setDireccion("0"); }
            try { Node telefono = lista.item(4); rs.setTelefono(devolverDatoString(telefono)); } catch(Exception Error) { rs.setTelefono("0"); }
            try { Node autorizacion = lista.item(5); rs.setAutorizacion(devolverDatoString(autorizacion)); } catch(Exception Error) { rs.setAutorizacion("0"); }
            try { Node referencia = lista.item(6); rs.setReferencia(devolverDatoString(referencia)); } catch(Exception Error) { rs.setReferencia("0"); }

        } catch (SAXException Error) {
            System.out.println("El formato XML del fichero no es correcto");
        } catch (IOException Error) {
            System.out.println("Se ha producido un error el leer el fichero");
        } catch (ParserConfigurationException Error) {
            System.out.println("No se ha podido crear el generador de documentos XML"); 
        }
        
        return rs;
    }

}
