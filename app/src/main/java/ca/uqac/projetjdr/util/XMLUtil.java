package ca.uqac.projetjdr.util;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Lucas on 23/03/2018.
 */

public class XMLUtil {

    private InputStream is;


    public XMLUtil(InputStream chemin){
        is=chemin;
    }


   /* public boolean UpdateXML(ArrayList<NoeudXML> listNoeud) throws TransformerException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        final Document document = builder.newDocument();
        document.setXmlStandalone(true);


        String[] nomTemp = chemin.split("/");
        String nomFinal = (nomTemp[nomTemp.length-1]).split("\\.")[0];
        final Element racine = document.createElement(nomFinal);
        document.appendChild(racine);


        addElement(document,racine,listNoeud);


        final TransformerFactory transformerFactory = TransformerFactory.newInstance();

        final Transformer transformer = transformerFactory.newTransformer();


        final DOMSource source = new DOMSource(document);
        final StreamResult sortie = new StreamResult(new File(chemin));

        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(source, sortie);



        return true;
    }

    private void addElement(Document document, Element racine,ArrayList<NoeudXML> listNoeud){

        for (NoeudXML noeudXML : listNoeud) {
            Element element = document.createElement(noeudXML.getNom());


            if(noeudXML.getAttribute()!=null){
                element.setAttribute(noeudXML.getAttribute().getNom(), noeudXML.getAttribute().getDonne());
            }
            if(noeudXML.getDonne()!=null){
                element.setTextContent(noeudXML.getDonne());
            }


            racine.appendChild(element);


            if(noeudXML.haveSousElement()){
                addElement(document, element, noeudXML.getListNoeud());
            }
        }

    }*/



    public NoeudXML lireXML() throws SAXException, IOException, ParserConfigurationException{
        NoeudXML res ;
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        Element racine = document.getDocumentElement();
        res = getElement(racine);

        return res;
    }

    private NoeudXML getElement(Element element){
        NoeudXML res = new NoeudXML(element.getNodeName());

        NamedNodeMap n = element.getAttributes();
        for (int i = 0; i < n.getLength(); i++) {
            res.setAttribute(new AttributXML(n.item(i).getNodeName(), n.item(i).getNodeValue()));
        }

        if(element.getChildNodes().getLength()>1){
            NodeList child = element.getChildNodes();
            for (int i = 1; i < child.getLength()-1; i++) {
                if(child.item(i) instanceof Element){
                    res.addNoeud(getElement((Element) child.item(i)));
                }
            }
        }else{
            if(!element.getTextContent().equals("")){
                res.setDonne(element.getTextContent());
            }
        }
        return res;

    }
}
