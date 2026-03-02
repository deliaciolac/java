package DataAccess;

import BusinessLogic.TaskManagement;
import DataModel.Employee;
import DataModel.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SerializationManager {

    public static void serializeData(TaskManagement taskManagement, String fileName) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(taskManagement.getEmployees());
            out.writeObject(taskManagement.getTasks());
            System.out.println("Datele au fost serializate în fisierul: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TaskManagement deserializeData(String fileName) {
        TaskManagement taskManagement = new TaskManagement();
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            List<Employee> employees = (ArrayList<Employee>) in.readObject();
            Map<Employee, List<Task>> tasks = (Map<Employee, List<Task>>) in.readObject();
            taskManagement.setEmployees(employees);
            taskManagement.setTasks(tasks);

            System.out.println("Datele au fost deserializate din fisierul: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return taskManagement;
    }
}