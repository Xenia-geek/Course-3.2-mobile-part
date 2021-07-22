package com.example.courseproject.xmlcalendar;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class Share {
    public static Document document;

    public static class Tasks {
        private static ArrayList<Task> tasks = new ArrayList<>();

        public static void add(Task task) {
            tasks.add(task);
        }

        public static void delete(Task task) {
            tasks.remove(task);
        }

        public static void delete_all() {
            tasks.clear();
        }


        public static ArrayList<Task> getList() {
            return tasks;
        }

        public static void setList(ArrayList<Task> list) {
            tasks = list;
        }

        public static ArrayList<Task> getArrayByDate(String date) {
            ArrayList<Task> list = new ArrayList<>();
            for (Task item : tasks) {
                if (item.getDate() == date)
                    list.add(item);
            }
            return list;
        }

        public static ArrayList<Task> getArrayByCategory(Category category) {
            ArrayList<Task> list = new ArrayList<>();
            for (Task item : tasks) {
                if (item.getCategory() == category)
                    list.add(item);
            }
            return list;
        }
    }

    public static class Categories {
        private static ArrayList<Category> categories = new ArrayList<>();

        public static void add(Category category) {
            categories.add(category);
        }

        public static void delete(Category category) {
            categories.remove(category);
        }

        public static ArrayList<Category> getList() {
            return categories;
        }

        public static void setList(ArrayList<Category> list) {
            categories = list;
        }

        public static Category getByName(String name) {
            for (Category item : categories) {
                if (item.getName() == name)
                    return item;
            }
            return null;
        }

    }
}
