package ca.uqac.projetjdr.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Lucas on 23/03/2018.
 */

public class XMLUtil {

    private File file;


    public XMLUtil(File chemin){
        file = chemin;
    }

    public NoeudXML lireXML() throws SAXException, IOException, ParserConfigurationException{
        NoeudXML res ;
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
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
