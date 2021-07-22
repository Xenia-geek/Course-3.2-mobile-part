package com.example.courseproject.xmlcalendar;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class Serializator {
    public static Document getDocument() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = documentBuilder.newDocument();

            Element categories = doc.createElement("Categories");
            for (Category item : Share.Categories.getList()) {
                Element category = doc.createElement("Category");
                category.setAttribute("Name", item.getName());

                for (Task task : Share.Tasks.getArrayByCategory(item)) {
                    Element taskElement = doc.createElement("Task");
                    taskElement.setAttribute("Date", task.getDate());
                    taskElement.setAttribute("Text", task.getText());

                    category.appendChild(taskElement);
                }

                categories.appendChild(category);
            }
            doc.appendChild(categories);

            return doc;

        } catch (ParserConfigurationException e) {
            Log.d("Serialisator", e.getMessage());
        }

        return null;
    }

    public static void parse(Document doc) {
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        ArrayList<Task> taskArrayList = new ArrayList<>();

        NodeList elements = doc.getElementsByTagName("Category");

        for (int i = 0; i < elements.getLength(); i++) {
            Node categoryNode = elements.item(i);
            Category category = new Category(categoryNode.getAttributes().getNamedItem("Name").getTextContent());
            categoryArrayList.add(category);

            NodeList tasksList = categoryNode.getChildNodes();
            for (int a = 0; a < tasksList.getLength(); a++) {
                Node taskNode = tasksList.item(a);
                Task task = new Task(taskNode.getAttributes().getNamedItem("Text").getTextContent(),
                        taskNode.getAttributes().getNamedItem("Date").getTextContent(),
                        category);
                taskArrayList.add(task);
            }
        }

        Share.Tasks.setList(taskArrayList);
        Share.Categories.setList(categoryArrayList);
    }

    public static void updateDoc(Context context) {
        Share.document = Serializator.getDocument();
        String xml = WorkWithFile.Xml.convertToString(Share.document);
        File file = new File(context.getFilesDir(), "task.xml");
        WorkWithFile.writeToFile(xml, file);
    }
}

