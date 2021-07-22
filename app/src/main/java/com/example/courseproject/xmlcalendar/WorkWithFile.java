package com.example.courseproject.xmlcalendar;

import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class WorkWithFile {
    public static String readFile(File file) {
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            return String.valueOf(builder);
        } catch (FileNotFoundException e) {
            Log.d("Ошибка ", "упс");
        } catch (IOException e) {
            Log.d("Ошибка ", "упс");
        }
        return "";
    }

    public static void writeToFile(String text, File file) {
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.append(text);
            writer.flush();//очищ
            writer.close();
        } catch (IOException e) {
            Log.d("WorkWithFile", "IOException");
        }
    }

    public static class Xml {
        public static String convertToString(Document document) {
            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();

                DOMSource source = new DOMSource(document);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();

                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(document), new StreamResult(writer));
                return writer.getBuffer().toString();

            } catch (TransformerException e) {
                Log.d("Xml.convertToString", e.getMessage());
            }

            return "";
        }

        public static Document parseFromFile(File file) {
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                return builder.parse(file);
            } catch (ParserConfigurationException e) {
                Log.d("Xml.parseFromFile", e.getMessage());
            } catch (SAXException e) {
                Log.d("Xml.parseFromFile", e.getMessage());
            } catch (IOException e) {
                Log.d("Xml.parseFromFile", e.getMessage());
            }
            return null;
        }
    }
}
