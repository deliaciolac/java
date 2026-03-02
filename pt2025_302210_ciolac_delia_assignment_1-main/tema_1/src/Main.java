import BusinessLogic.TaskManagement;
import BusinessLogic.Utility;
import DataAccess.SerializationManager;
import DataModel.ComplexTask;
import DataModel.Employee;
import DataModel.SimpleTask;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SimpleTask t1 = new SimpleTask(1, 12, 12);
        SimpleTask t2 = new SimpleTask(2, 14, 14);
        SimpleTask t3 = new SimpleTask(3, 22, 20);
        SimpleTask t4 = new SimpleTask(4, 23, 23);

        ComplexTask t5 = new ComplexTask(5);
        t5.addTask(t4);
        t5.addTask(t3);

        ComplexTask t6 = new ComplexTask(6);
        t6.addTask(t5);
        t6.addTask(t2);

        Employee e1 = new Employee(1, "Ana");
        Employee e2 = new Employee(2, "Maria");
        Employee e3 = new Employee(3, "Alexadru");

        TaskManagement tm1 = new TaskManagement();
        tm1.addEmployee(e1);
        tm1.assignTaskToEmployee(e1, t1);
        tm1.assignTaskToEmployee(e1, t2);
        tm1.modifiyTaskStatus(e1, t1);
        tm1.modifiyTaskStatus(e1, t2); //48
        System.out.println("Work duration pentru angajatul " + e1.getName() + ": " + tm1.calculateWorkDuration(e1));

        tm1.addEmployee(e2);
        tm1.assignTaskToEmployee(e2, t3);
        tm1.assignTaskToEmployee(e2, t4);
        tm1.modifiyTaskStatus(e2, t3);
        tm1.modifiyTaskStatus(e2, t4); //46

        tm1.addEmployee(e3);
        tm1.assignTaskToEmployee(e3, t6);
        tm1.modifiyTaskStatus(e3, t6); //66

        tm1.printEmployees();

        System.out.println();
        Utility.filterEmployees(tm1.getEmployees());

        System.out.println();
        tm1.printTaskStatusCounts();

        String fileName = "data.ser";
        SerializationManager.serializeData(tm1, fileName);

        TaskManagement tm2 = SerializationManager.deserializeData(fileName);

        System.out.println("\nDate deserializate:");
        tm2.printEmployees();
    }
}