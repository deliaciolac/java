package BusinessLogic;

import DataModel.Employee;
import DataModel.Task;

import java.util.*;

public class Utility {
    static List<Employee> empWithWorkDurationGreaterThan40 = new ArrayList<>();

    public static void filterEmployees(ArrayList<Employee> employees) {
        empWithWorkDurationGreaterThan40.clear();
        for (Employee emp : employees) {
            if (emp.calculateWorkDurationEmployee() > 40) {
                empWithWorkDurationGreaterThan40.add(emp);
            }
        }
        empWithWorkDurationGreaterThan40.sort(null); // Sort the list
        System.out.println("Angajati cu work duration > 40: ");
        for (Employee emp : empWithWorkDurationGreaterThan40) {
            System.out.println(emp.getName());
        }
    }

    public static Map<String, Map<String, Integer>> calculateTaskStatusCount(List<Employee> employees) {
        Map<String, Map<String, Integer>> taskStatusMap = new HashMap<>();
        for (Employee emp : employees) {
            Map<String, Integer> statusCount = new HashMap<>();
            int completed = 0;
            int uncompleted = 0;
            for (Task task : emp.getTasks()) {
                if (task.getStatusTask().equals("Completed")) {
                    completed++;
                } else {
                    uncompleted++;
                }
            }
            statusCount.put("Completed", completed);
            statusCount.put("Uncompleted", uncompleted);
            taskStatusMap.put(emp.getName(), statusCount);
        }
        return taskStatusMap;
    }
}
