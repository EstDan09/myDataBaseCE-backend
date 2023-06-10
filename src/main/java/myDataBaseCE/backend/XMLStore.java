import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
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


    private int numColums;

    private int numRows;

    public XMLStore getNext() {
        return next;
    }

    public void setNext(XMLStore next) {
        this.next = next;
    }

    public void createXML(String name, String[] attributes) {

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement(name);
            document.appendChild(root);

            for (String attribute : attributes) {
                Element newCol = document.createElement(attribute);
                root.appendChild(newCol);
                numColums++;

            }
            // root element
            /*

            // employee element
            Element employee = document.createElement("employee");

            root.appendChild(employee);

            // set an attribute to staff element
            Attr attr = document.createAttribute("id");
            attr.setValue("10");
            employee.setAttributeNode(attr);

            //you can also use staff.setAttribute("id", "1") for this

            // firstname element

            employee.appendChild(firstName);

            // lastname element
            Element lastname = document.createElement("lastname");
            lastname.appendChild(document.createTextNode("Harley"));
            employee.appendChild(lastname);

            // email element
            Element email = document.createElement("email");
            email.appendChild(document.createTextNode("james@example.org"));
            employee.appendChild(email);

            // department elements
            Element department = document.createElement("department");
            department.appendChild(document.createTextNode("Human Resources"));
            employee.appendChild(department);
            */
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            new File(".\\XMLFolder\\" + name).mkdirs();
            StreamResult streamResult = new StreamResult(new File(".\\XMLFolder\\" + name + "\\" + name + ".xml"));


            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);


        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    void insert(String name, String[] newValues) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        try (InputStream is = new FileInputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + name + "/" + name + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();

            Document document = db.parse(is);

            NodeList xml = document.getElementsByTagName(name);
            NodeList attributes = xml.item(0).getChildNodes();

            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {

                    Element nData = document.createElement("X" + numRows);

                    nData.appendChild(document.createTextNode(newValues[i]));

                    attribute.appendChild(nData);

                }
            }
            try (FileOutputStream output =
                         new FileOutputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + name + "/" + name + ".xml")) {
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
            new File("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + name).mkdirs();
            StreamResult streamResult = new StreamResult(new File("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + name + "/" + name + ".xml"));
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    public String searchID(String name, String Colum, String value) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        String id = "";
        try (InputStream is = new FileInputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + name + "/" + name + ".xml")) {
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

    public void delete(String name, String Colum, String value) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        String id = searchID(name, Colum, value);
        try (InputStream is = new FileInputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + name + "/" + name + ".xml")) {
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
                         new FileOutputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + name + "/" + name + ".xml")) {
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

    public void update(String name, String Colum, String value, String[] columns, String[] newValues) {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        String id = searchID(name, Colum, value);
        int counter = 0;
        try (InputStream is = new FileInputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + name + "/" + name + ".xml")) {
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
                         new FileOutputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + name + "/" + name + ".xml")) {
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

    public void innerJoin(String[] attributes, String initialT, String secondT, String attribute1, String attribute2, String[] conditionals) {
        try {
            //ArrayList<ArrayList<String>> nRows;
            System.out.println(getComAttJ(initialT, secondT, attribute1, attribute2));
            System.out.println(equalsJ(getComAttJ(initialT, secondT, attribute1, attribute2)));
            if (equalsJ(getComAttJ(initialT, secondT, attribute1, attribute2))) {
                //nRows = extractRowsJ(attributtes);
                //System.out.println(nRows);
                numRows=0;
                createTemporalJ(attributes, extractRowsJ(attributes));
            }
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ArrayList<String>> getComAttJ(String initialT, String secondT, String attribute1, String attribute2) {
        ArrayList<ArrayList<String>> maze = new ArrayList<>(2);
        ArrayList<String> array = new ArrayList<>();

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        try (InputStream is = new FileInputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + initialT + "/" + initialT + ".xml")) {

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

        try (InputStream is = new FileInputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + secondT + "/" + secondT + ".xml")) {
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

    public ArrayList<ArrayList<String>> extractRowsJ(String[] oldAttributes) {
        ArrayList<ArrayList<String>> maze = new ArrayList<>();
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        for (int i = 0; i < oldAttributes.length; i++) {
            System.out.println("Entré");
            String[] oldAttribute = oldAttributes[i].split("\\.");
            maze.add(new ArrayList<>());
            try (InputStream is = new FileInputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + oldAttribute[0] + "/" + oldAttribute[0] + ".xml")) {
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
    public void createTemporalJ(String[] attributes, ArrayList<ArrayList<String>> maze) throws TransformerException, ParserConfigurationException {

        try{
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("TemporalTable");
        document.appendChild(root);
        for (int i = 0; i<attributes.length; i++) {
            String[] attribute = attributes[i].split("\\.");
            Element newCol = document.createElement(attribute[1]);
            root.appendChild(newCol);

            for(int j = 0; j<maze.get(0).size();j++){
                Element nData = document.createElement("X" + numRows);

                nData.appendChild(document.createTextNode(maze.get(i).get(j)));

                newCol.appendChild(nData);

                numRows++;
            }
            numRows=0;
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        new File("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/TemporalTable").mkdirs();
        StreamResult streamResult = new StreamResult(new File("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/TemporalTable/TemporalTable.xml"));

        transformer.transform(domSource, streamResult);
    } catch (ParserConfigurationException | TransformerException pce) {
        pce.printStackTrace();
    }
    }

    public void select(String nombre, String[]attributes, String attributeC, String rowC, String[] conditionals ){

    }
    public ArrayList<ArrayList<String>> collectRows(String nombre,String attributeC, String rowC) {
        ArrayList<ArrayList<String>> maze = new ArrayList<>();
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        int counter = 0;
        try (InputStream is = new FileInputStream("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/" + nombre + "/" + nombre + ".xml")) {
            DocumentBuilder db = documentFactory.newDocumentBuilder();
            Document document = db.parse(is);
            NodeList xml = document.getElementsByTagName(nombre);
            NodeList attributes = xml.item(0).getChildNodes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                if (attribute.getNodeType() == Node.ELEMENT_NODE) {
                    if (attribute.getNodeName().equals(attributeC)) {
                        maze.add(new ArrayList<>());
                        NodeList rows = attribute.getChildNodes();
                        boolean verify = false;
                        for (int z = 0; z < rows.getLength(); z++) {
                            Node row = rows.item(z);
                            if (row.getNodeType() == Node.ELEMENT_NODE) {
                                if (row.getTextContent() == rowC) {
                                    maze.get(counter).add(row.getNodeName());
                                    verify = true;
                                }
                            }
                        }
                        if(verify){
                            counter++;
                        }
                        else{
                            maze.remove(counter);
                        }
                    }
                }
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
        return maze;
    }




}