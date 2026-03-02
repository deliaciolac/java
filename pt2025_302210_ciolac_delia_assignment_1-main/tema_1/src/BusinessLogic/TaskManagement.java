package BusinessLogic;

import DataModel.Employee;
import DataModel.Task;

import java.util.*;

public class TaskManagement {
    private Map<Employee, List<Task>> tasks;
    private List<Employee> employees;

    public TaskManagement() {
        tasks = new HashMap<Employee, List<Task>>();
        employees = new ArrayList<Employee>();
    }

    public Map<Employee, List<Task>> getTasks() {
        return tasks;
    }

    public void setTasks(Map<Employee, List<Task>> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Employee> getEmployees() {
        return (ArrayList<Employee>) employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {

        employees.add(employee);
    }

    public boolean isEmployeed(int idEmployee) {
        for(Employee e: employees) {
            if(e.getIdEmployee() == idEmployee) {
                return true;
            }
        }
        return false;
    }

    public void assignTaskToEmployee(Employee e, Task task) {
        if(isEmployeed(e.getIdEmployee()) == true) {
            e.addTask(task);
            System.out.println("Task-ul a fost asignat angajatului " + e.getName());
        }
        else {
            System.out.println("Task-ul nu poate fi asignat pentru ca angajatul nu exista.");
        }
    }

    public int calculateWorkDuration(Employee e) {
        int workDuration = 0;
        for(Task task: e.getTasks()) {
            if(task.getStatusTask().equals("Completed")) {
                workDuration = task.estimateDuration() + workDuration;
            }
        }
        return workDuration;
    }

    public void modifiyTaskStatus(Employee e, Task t) {
        for( Task task: e.getTasks()) {
            if(t.getIdTask() == task.getIdTask()) {
                if(task.getStatusTask().equals("Completed")) {
                    task.setStatusTask("Uncompleted") ;
                }
                else {
                    task.setStatusTask("Completed") ;
                }
            }
        }
    }

    public void printEmployees() {
        for(Employee e: employees) {
            System.out.println(e.toString());
        }
    }

    public void printTaskStatusCounts() {
        Map<String, Map<String, Integer>> taskStatusMap = Utility.calculateTaskStatusCount(employees);
        for (Map.Entry<String, Map<String, Integer>> entry : taskStatusMap.entrySet()) {
            System.out.println("Employee: " + entry.getKey());
            System.out.println("  Completed Tasks: " + entry.getValue().get("Completed"));
            System.out.println("  Uncompleted Tasks: " + entry.getValue().get("Uncompleted"));
        }
    }


}