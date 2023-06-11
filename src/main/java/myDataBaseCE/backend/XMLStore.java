
package myDataBaseCE.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.xml.sax.SAXException;
import org.w3c.dom.*;

public class XMLStore {
    public XMLStore() {
        next = null;
        numRows = 0;
    }

    XMLStore next;


    private int numRows;

    public void setNext(XMLStore next) {
        this.next = next;
    }

    /**
     * Función para crear un nuevo XML
     * @param name nombre del XML
     * @param attributes columnas del XML
     */
    public void createXML(String name, String[] attributes) {
        numRows =0;
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement(name);
            document.appendChild(root);

            for (String attribute : attributes) {
                Element newCol = document.createElement(attribute);
                root.appendChild(newCol);

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            new File(".//XMLFolder//" + name).mkdirs();
            //new File("/home/dadu/Documents/GitHub/myDataBaseCE-backend/XMLFolder/"+name).mkdirs();
            StreamResult streamResult = new StreamResult(new File(".//XMLFolder//" + name + "//" + name + ".xml"));
            transformer.transform(domSource, streamResult);


        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    /**
     * Función para insertar datos a un XML
     * @param name name nombre del XML que voy a modificar
     * @param newValues nuevos valores que voy a agregar
     */
    void insert(String name, String[] newValues) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        try (InputStream is = new FileInputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();

            Document document = db.parse(is);

            NodeList xml = document.getElementsByTagName(name);
            NodeList attributes = xml.item(0).getChildNodes();

            int counter = 0;
            for (int v = 0; v < attributes.getLength() && counter < newValues.length; v++) {
                Node attribute = attributes.item(v);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    Element nData = document.createElement("X" + numRows);

                    nData.appendChild(document.createTextNode(newValues[counter]));

                    attribute.appendChild(nData);

                    counter++;

                }
            }
            try (FileOutputStream output =
                         new FileOutputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(output);
                transformer.transform(source, result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            numRows++;
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Función para eliminar todo un XML
     * @param name nombre del XML
     */
    public void deleteSupreme(String name) {
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();
            Element root = document.createElement(name);
            document.appendChild(root);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            new File(".//XMLFolder//" + name).mkdirs();
            StreamResult streamResult = new StreamResult(new File(".//XMLFolder//" + name + "//" + name + ".xml"));
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    /**
     * Función para buscar el valor a eliminar de un XML
     * @param name nombre del XML
     * @param Colum columna del xml
     * @param value valor a eliminar
     * @return id ubicación de lo que se desea eliminar
     */
    public String searchID(String name, String Colum, String value) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        String id = "";
        try (InputStream is = new FileInputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();

            Document document = db.parse(is);

            NodeList xml = document.getElementsByTagName(name);
            NodeList attributes = xml.item(0).getChildNodes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    if (attribute.getNodeName().equals(Colum)) {
                        NodeList rows = attribute.getChildNodes();
                        for (int j = 0; j < rows.getLength(); j++) {
                            Node row = rows.item(j);
                            if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                                if (Objects.equals(row.getTextContent(), value)) {
                                    id = row.getNodeName();
                                    break;
                                }
                            }
                        }
                    }

                }
            }

            return id;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Función para eliminar um dato de un XML
     * @param name nombre del XML
     * @param Colum columna del xml
     * @param value valor a eliminar
     */
    public void delete(String name, String Colum, String value) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        String id = searchID(name, Colum, value);
        try (InputStream is = new FileInputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();

            Document document = db.parse(is);

            NodeList xml = document.getElementsByTagName(name);
            NodeList attributes = xml.item(0).getChildNodes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList rows = attribute.getChildNodes();
                    for (int j = 0; j < rows.getLength(); j++) {
                        Node row = rows.item(j);
                        if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                            if (row.getNodeName().equals(id)) {
                                attribute.removeChild(row);
                            }
                        }
                    }
                }
            }
            try (FileOutputStream output =
                         new FileOutputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(output);
                transformer.transform(source, result);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Función para buscar las columnas a eliminar de un XML
     * @param name nombre del XML
     * @param Colum columna del xml
     * @param value valor a eliminar
     * @param columns
     * @param newValues
     */
    public void update(String name, String Colum, String value, String[] columns, String[] newValues) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        String id = searchID(name, Colum, value);
        int counter = 0;
        try (InputStream is = new FileInputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();

            Document document = db.parse(is);

            NodeList xml = document.getElementsByTagName(name);
            NodeList attributes = xml.item(0).getChildNodes();
            for (int i = 0; i < attributes.getLength(); i++) {
                if (counter >= columns.length) {
                    break;
                }
                Node attribute = attributes.item(i);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    for (int j = 0; j < columns.length; j++) {
                        if (attribute.getNodeName().equals(columns[j])) {
                            NodeList rows = attribute.getChildNodes();
                            for (int z = 0; z < rows.getLength(); z++) {
                                Node row = rows.item(z);
                                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                                    if (row.getNodeName().equals(id)) {
                                        row.setTextContent(newValues[j]);
                                        //attribute.removeChild(row);
                                        //Element nData = document.createElement(id);
                                        //nData.appendChild(document.createTextNode(newValues[j]));
                                        //attribute.appendChild(nData);
                                    }
                                }
                            }
                            counter++;
                        }
                    }

                }
            }
            try (FileOutputStream output =
                         new FileOutputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(output);
                transformer.transform(source, result);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Función
     * @param initialT
     * @param secondT
     * @param attribute1
     * @param attribute2
     * @param attributes
     */
    public void innerJoin(String initialT, String secondT, String[] attribute1, String[] attribute2,String[] attributes, String[] conditionals) {
        try {
            //ArrayList<ArrayList<String>> nRows;

            if (checkConditionalsJ(initialT,secondT,attribute1,attribute2,conditionals)) {
                //nRows = extractRowsJ(attributtes);
                numRows = 0;
                createTemporalJ(attributes, extractRowsJ(attributes));
            }
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Función
     * @param initialT
     * @param secondT
     * @param attribute1
     * @param attribute2
     * @param conditionals
     * @return              poner que se devuelve
     */
    public boolean checkConditionalsJ(String initialT, String secondT, String[] attribute1, String[] attribute2, String[] conditionals){
        if(attribute1.length==1 && attribute2.length==1){
            return equalsJ(getComAttJ(initialT, secondT, attribute1[0], attribute2[0]));
        }
        else if(attribute1.length==2 && attribute2.length==2 && conditionals.length==1){
            if(Objects.equals(conditionals[0], "&&")){
                if(equalsJ(getComAttJ(initialT, secondT, attribute1[0], attribute2[0])) && equalsJ(getComAttJ(initialT, secondT, attribute1[1], attribute2[1]))){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(Objects.equals(conditionals[0], "||")){
                if (equalsJ(getComAttJ(initialT, secondT, attribute1[0], attribute2[0])) || equalsJ(getComAttJ(initialT, secondT, attribute1[1], attribute2[1]))){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else if(attribute1.length==3 && attribute2.length==3 && conditionals.length==2){
            if(Objects.equals(conditionals[0], "&&") && Objects.equals(conditionals[1], "&&")){
                if(equalsJ(getComAttJ(initialT, secondT, attribute1[0], attribute2[0])) && equalsJ(getComAttJ(initialT, secondT, attribute1[1],
                        attribute2[1])) && equalsJ(getComAttJ(initialT, secondT, attribute1[2], attribute2[2]))){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(Objects.equals(conditionals[0], "||") && Objects.equals(conditionals[1], "||")){
                if(equalsJ(getComAttJ(initialT, secondT, attribute1[0], attribute2[0])) || equalsJ(getComAttJ(initialT, secondT, attribute1[1],
                        attribute2[1])) || equalsJ(getComAttJ(initialT, secondT, attribute1[2], attribute2[2]))){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(Objects.equals(conditionals[0], "&&") && Objects.equals(conditionals[1], "||")){
                if(equalsJ(getComAttJ(initialT, secondT, attribute1[0], attribute2[0])) && equalsJ(getComAttJ(initialT, secondT, attribute1[1],
                        attribute2[1])) || equalsJ(getComAttJ(initialT, secondT, attribute1[2], attribute2[2]))){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(Objects.equals(conditionals[0], "||") && Objects.equals(conditionals[1], "&&")){
                if(equalsJ(getComAttJ(initialT, secondT, attribute1[0], attribute2[0])) || equalsJ(getComAttJ(initialT, secondT, attribute1[1],
                        attribute2[1])) && equalsJ(getComAttJ(initialT, secondT, attribute1[2], attribute2[2]))){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        return false;
    }

    /**
     * Función
     * @param initialT
     * @param secondT
     * @param attribute1
     * @param attribute2
     * @return      poner aqui que devuelve
     */
    public ArrayList<ArrayList<String>> getComAttJ(String initialT, String secondT, String attribute1, String attribute2) {
        ArrayList<ArrayList<String>> maze = new ArrayList<>(2);
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        try (InputStream is = new FileInputStream(".//XMLFolder//" + initialT + "//" + initialT + ".xml")) {

            DocumentBuilder db = documentFactory.newDocumentBuilder();
            Document document = db.parse(is);
            NodeList xml = document.getElementsByTagName(initialT);
            NodeList attributes = xml.item(0).getChildNodes();
            maze.add(new ArrayList<>());
            for (int i = 0; i < attributes.getLength(); i++) {

                Node attribute = attributes.item(i);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    if (attribute.getNodeName().equals(attribute1)) {

                        NodeList rows = attribute.getChildNodes();
                        for (int j = 0; j < rows.getLength(); j++) {
                            Node row = rows.item(j);
                            if (row.getNodeType() == Node.ELEMENT_NODE) {
                                maze.get(0).add(row.getTextContent());
                            }
                        }
                    }

                }
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        try (InputStream is = new FileInputStream(".//XMLFolder//" + secondT + "//" + secondT + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();
            Document document = db.parse(is);
            NodeList xml = document.getElementsByTagName(secondT);
            NodeList attributes = xml.item(0).getChildNodes();
            maze.add(new ArrayList<>());
            for (int i = 0; i < attributes.getLength(); i++) {

                Node attribute = attributes.item(i);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    if (attribute.getNodeName().equals(attribute2)) {
                        NodeList rows = attribute.getChildNodes();
                        for (int j = 0; j < rows.getLength(); j++) {
                            Node row = rows.item(j);
                            if (row.getNodeType() == Node.ELEMENT_NODE) {
                                maze.get(1).add(row.getTextContent());
                            }
                        }
                    }

                }
            }


        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        return maze;

    }

    /**
     * Función
     * @param maze
     * @return      poner aqui que devuelve
     */
    public boolean equalsJ(ArrayList<ArrayList<String>> maze) {

        if (maze.get(0).size() != maze.get(1).size()) {
            return false;
        }
        for (int j = 0; j < maze.get(0).size(); j++) {
            if (!Objects.equals(maze.get(0).get(j), maze.get(1).get(j))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Función
     * @param oldAttributes
     * @return      poner aqui que devuelve
     */
    public ArrayList<ArrayList<String>> extractRowsJ(String[] oldAttributes) {
        ArrayList<ArrayList<String>> maze = new ArrayList<>();
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        for (int i = 0; i < oldAttributes.length; i++) {
            String[] oldAttribute = oldAttributes[i].split("\\.");
            maze.add(new ArrayList<>());
            try (InputStream is = new FileInputStream(".//XMLFolder//" + oldAttribute[0] + "//" + oldAttribute[0] + ".xml")) {
                DocumentBuilder db = documentFactory.newDocumentBuilder();
                Document document = db.parse(is);
                NodeList xml = document.getElementsByTagName(oldAttribute[0]);
                NodeList attributes = xml.item(0).getChildNodes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    Node attribute = attributes.item(j);
                    if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                        if (attribute.getNodeName().equals(oldAttribute[1])) {
                            NodeList rows = attribute.getChildNodes();
                            for (int z = 0; z < rows.getLength(); z++) {
                                Node row = rows.item(z);
                                if (row.getNodeType() == Node.ELEMENT_NODE) {
                                    maze.get(i).add(row.getTextContent());
                                }
                            }
                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                throw new RuntimeException(e);
            }

        }
        return maze;
    }

    /**
     * Función
     * @param attributes
     * @param maze
     * @return      poner aqui que devuelve
     */
    public void createTemporalJ(String[] attributes, ArrayList<ArrayList<String>> maze) throws TransformerException, ParserConfigurationException {

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("TemporalTable");
            document.appendChild(root);
            for (int i = 0; i < attributes.length; i++) {
                String[] attribute = attributes[i].split("\\.");
                Element newCol = document.createElement(attribute[1]);
                root.appendChild(newCol);

                for (int j = 0; j < maze.get(0).size(); j++) {
                    Element nData = document.createElement("X" + numRows);

                    nData.appendChild(document.createTextNode(maze.get(i).get(j)));

                    newCol.appendChild(nData);

                    numRows++;
                }
                numRows = 0;
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            new File(".//XMLFolder//TemporalTable").mkdirs();
            StreamResult streamResult = new StreamResult(new File(".//XMLFolder//TemporalTable//TemporalTable.xml"));

            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    /**
     * Función
     * @param name
     * @param attributes
     * @param attributeC
     * @param conditionals
     */
    public void select(String name, String[] attributes, String attributeC, String rowC, String[] conditionals) {
        numRows = 0;
        createTemporalTS(getMazeS(name, attributes, collectRowsS(name, attributeC, rowC)));
    }

    /**
     * Función
     * @param name
     * @param attributeC
     * @param rowC
     * @return maze     poner aqui que devuelve
     */
    public ArrayList<String> collectRowsS(String name, String attributeC, String rowC) {
        ArrayList<String> maze = new ArrayList<>();
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        try (InputStream is = new FileInputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();
            Document document = db.parse(is);
            NodeList xml = document.getElementsByTagName(name);
            NodeList attributes = xml.item(0).getChildNodes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    if (attribute.getNodeName().equals(attributeC)) {
                        NodeList rows = attribute.getChildNodes();
                        for (int z = 0; z < rows.getLength(); z++) {
                            Node row = rows.item(z);
                            if (row.getNodeType() == Node.ELEMENT_NODE) {
                                if (Objects.equals(row.getTextContent(), rowC)) {
                                    maze.add(row.getNodeName());

                                }
                            }
                        }
                    }
                }
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
        return maze;
    }

    /**
     * Función
     * @param name
     * @param oldAttributes
     * @param oldRows
     * @return maze     poner aqui que devuelve
     */
    public ArrayList<ArrayList<String>> getMazeS(String name, String[] oldAttributes, ArrayList<String> oldRows) {
        ArrayList<ArrayList<String>> maze = new ArrayList<>();
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        for (int i = 0; i < oldAttributes.length; i++) {
            try (InputStream is = new FileInputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
                DocumentBuilder db = documentFactory.newDocumentBuilder();
                Document document = db.parse(is);
                NodeList xml = document.getElementsByTagName(name);
                NodeList attributes = xml.item(0).getChildNodes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    Node attribute = attributes.item(j);
                    if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                        if (attribute.getNodeName().equals(oldAttributes[i])) {
                            NodeList rows = attribute.getChildNodes();
                            maze.add(new ArrayList<>());
                            maze.get(i).add(attribute.getNodeName());
                            for (int z = 0; z < rows.getLength(); z++) {
                                Node row = rows.item(z);
                                if (row.getNodeType() == Node.ELEMENT_NODE) {
                                    for (int y = 0; y < oldRows.size(); y++) {
                                        if (row.getNodeName().equals(oldRows.get(y))) {
                                            maze.get(i).add(row.getTextContent());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                throw new RuntimeException(e);
            }
        }
        return maze;
    }

    /**
     * Función para buscar las columnas a eliminar de un XML
     * @param maze
     */
    public void createTemporalTS(ArrayList<ArrayList<String>> maze) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("TemporalTable");
            document.appendChild(root);
            for (int i = 0; i < maze.size(); i++) {
                Element newCol = document.createElement(maze.get(i).get(0));
                root.appendChild(newCol);

                for (int j = 1; j < maze.get(0).size(); j++) {
                    Element nData = document.createElement("X" + numRows);

                    nData.appendChild(document.createTextNode(maze.get(i).get(j)));

                    newCol.appendChild(nData);

                    numRows++;
                }
                numRows = 0;
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            new File(".//XMLFolder//TemporalTable").mkdirs();
            StreamResult streamResult = new StreamResult(new File(".//XMLFolder//TemporalTable//TemporalTable.xml"));

            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    /**
     * Función
     * @return foldersInDirectory  poner aqui que devuelve
     */
    public List<String> findFoldersInDirectory() {
        File directory = new File(".//XMLFolder//");

        FileFilter directoryFileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        List<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
        for (File directoryAsFile : directoryListAsFile) {
            foldersInDirectory.add(directoryAsFile.getName());
        }

        return foldersInDirectory;
    }

    /**
     * Función para contar columnas
     * @param name
     */
    public void countRows(String name) {
        numRows = 0;
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        try (InputStream is = new FileInputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();
            Document document = db.parse(is);
            NodeList xml = document.getElementsByTagName(name);
            NodeList attributes = xml.item(0).getChildNodes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList rows = attribute.getChildNodes();
                    for (int z = 0; z < rows.getLength(); z++) {
                        Node row = rows.item(z);
                        if (row.getNodeType() == Node.ELEMENT_NODE) {
                            numRows++;
                        }
                    }
                    break;
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Función para buscar las columnas a eliminar de un XML
     * @param name
     * @return maze poner aqui que devuelve
     */
    public ArrayList<ArrayList<String>> sendTable(String name){
        ArrayList<ArrayList<String>> maze = new ArrayList<>();
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        try (InputStream is = new FileInputStream(".//XMLFolder//" + name + "//" + name + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();
            Document document = db.parse(is);
            NodeList xml = document.getElementsByTagName(name);
            NodeList attributes = xml.item(0).getChildNodes();
            int counter = 0;
            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    maze.add(new ArrayList<>());
                    maze.get(counter).add(attribute.getNodeName());
                    NodeList rows = attribute.getChildNodes();
                    for (int z = 0; z < rows.getLength(); z++) {
                        Node row = rows.item(z);
                        if (row.getNodeType() == Node.ELEMENT_NODE) {
                            maze.get(counter).add(row.getTextContent());
                        }
                    }
                    counter++;
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
        return maze;
    }
}
