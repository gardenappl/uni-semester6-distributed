package ua.yuriih.task10.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import ua.yuriih.task10.model.Group;
import ua.yuriih.task10.model.Student;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.stream.Collectors;

public class UniversityDao {
    private final HashMap<Integer, Student> students = new HashMap<>();
    private final HashMap<Integer, Group> groups = new HashMap<>();

    // element types
    private static final String UNIVERSITY = "university";
    private static final String GROUP = "group";
    private static final String STUDENT = "student";

    // attributes
    private static final String ID = "id";
    private static final String GROUP_ID = "group";
    private static final String NAME = "name";
    private static final String DATE_OF_BIRTH = "birthdate";
    private static final String AVERAGE_SCORE = "avgscore";
    private static final String HAS_SCHOLARSHIP = "scholarship";

    private final Transformer transformer;
    private final DocumentBuilder documentBuilder;

    public UniversityDao() {
        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema;
        try {
            schema = schemaFactory.newSchema(new File("university.xsd"));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        DocumentBuilderFactory documentBuilderFactory =
                DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setSchema(schema);
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        documentBuilder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException e) {
                System.err.println("Warning");
                e.printStackTrace();
            }

            @Override
            public void error(SAXParseException e) throws SAXException {
                throw e;
            }

            @Override
            public void fatalError(SAXParseException e) throws SAXException {
                throw e;
            }
        });

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Student getStudent(int id) {
        return students.get(id).clone();
    }

    public synchronized void addStudent(Student student) {
        if (students.containsKey(student.getId()))
            throw new IllegalArgumentException("Already have a student with ID " + student.getId());
        if (!groups.containsKey(student.getGroupId()))
            throw new IllegalArgumentException("No group with ID " + student.getGroupId());
        students.put(student.getId(), student);
    }

    public synchronized int getFreeStudentId() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!students.containsKey(i))
                return i;
        }
        throw new IndexOutOfBoundsException("No free index!");
    }

    public synchronized void updateStudent(Student student) {
        if (!students.containsKey(student.getId()))
            throw new IllegalArgumentException("No student with ID " + student.getId());
        if (!groups.containsKey(student.getGroupId()))
            throw new IllegalArgumentException("No group with ID " + student.getGroupId());
        students.put(student.getId(), student);
    }

    public synchronized boolean deleteStudent(int id) {
        return students.remove(id) != null;
    }

    public synchronized ArrayList<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public synchronized ArrayList<Student> getAllStudentsFromGroup(int groupId) {
        return students.values().stream().filter(student ->
                student.getGroupId() == groupId).collect(Collectors.toCollection(ArrayList::new));
    }


    public synchronized Group getGroup(int id) {
        return groups.get(id).clone();
    }

    public synchronized void addGroup(Group group) {
        if (groups.containsKey(group.getId()))
            throw new IllegalArgumentException("Already have a group wtih ID " + group.getId());
        groups.put(group.getId(), group);
    }

    public synchronized int getFreeGroupId() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!groups.containsKey(i))
                return i;
        }
        throw new IndexOutOfBoundsException("No free index!");
    }

    public synchronized void updateGroup(Group group) {
        if (!groups.containsKey(group.getId()))
            throw new IllegalArgumentException("No group with ID " + group.getId());
        groups.put(group.getId(), group);
    }

    public synchronized boolean deleteGroup(int id) {
        boolean deleted = groups.remove(id) != null;
        if (deleted)
            students.values().removeIf(student -> student.getGroupId() == id);
        return deleted;
    }

    public synchronized ArrayList<Group> getAllGroups() {
        return new ArrayList<>(groups.values());
    }



    public synchronized void saveToFile(String fileName) throws IOException {
        Document document = documentBuilder.newDocument();

        Element root = document.createElement(UNIVERSITY);
        groups.values().forEach(group ->
                root.appendChild(makeGroupElement(document, group)));
        document.appendChild(root);

        Source domSource = new DOMSource(document);
        Result fileResult = new StreamResult(new File(fileName));

        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.toString());
        try {
            transformer.transform(domSource, fileResult);
        } catch (TransformerException e) {
            if (e.getCause() instanceof IOException)
                throw (IOException) e.getCause();
            else
                throw new RuntimeException(e);
        }
    }

    private Element makeStudentElement(Document doc, Student student) {
        Element element = doc.createElement(STUDENT);
        element.setAttribute(ID, Integer.toString(student.getId()));

        element.setAttribute(NAME, student.getName());
        element.setAttribute(GROUP_ID, Integer.toString(student.getGroupId()));
        element.setAttribute(DATE_OF_BIRTH, student.getBirthDate().toString());
        element.setAttribute(AVERAGE_SCORE, Float.toString(student.getAverageScore()));
        element.setAttribute(HAS_SCHOLARSHIP, Boolean.toString(student.hasScholarship()));

        return element;
    }

    private Element makeGroupElement(Document doc, Group group) {
        Element element = doc.createElement(GROUP);
        element.setAttribute(ID, Integer.toString(group.getId()));
        element.setAttribute(NAME, group.getName());

        students.values().forEach(student -> {
            if (student.getGroupId() == group.getId())
                element.appendChild(makeStudentElement(doc, student));
        });

        return element;
    }



    public synchronized void readFromFile(String fileName) throws IOException, SAXException {
        groups.clear();
        students.clear();

        Document document;
        try {
            document = documentBuilder.parse(fileName);
        } catch (SAXException e) {
            if (e.getException() instanceof IOException)
                throw (IOException) e.getException();
            else
                throw e;
        }

        Element root = document.getDocumentElement();
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            parseGroup((Element) children.item(i));
        }
    }

    private void parseGroup(Element element) {
        Group group = new Group(
                Integer.parseInt(element.getAttribute(ID)),
                element.getAttribute(NAME)
        );
        groups.put(group.getId(), group);

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            parseStudent((Element) children.item(i));
        }
    }

    private void parseStudent(Element element) {
        Student student = new Student(
                Integer.parseInt(element.getAttribute(ID)),
                Integer.parseInt(element.getAttribute(GROUP_ID)),
                element.getAttribute(NAME),
                LocalDate.parse(element.getAttribute(DATE_OF_BIRTH)),
                Boolean.parseBoolean(element.getAttribute(HAS_SCHOLARSHIP))
        );
        student.setAverageScore(Float.parseFloat(element.getAttribute(AVERAGE_SCORE)));
        addStudent(student);
    }
}
