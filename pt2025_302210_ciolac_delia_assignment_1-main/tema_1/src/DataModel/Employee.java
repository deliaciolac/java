package DataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Employee implements Comparable, Serializable {
    private static final long serialVersionUID = 1L;
    private int idEmployee;
    private String name;
    private List<Task> tasks;


    public Employee(int idEmployee, String name) {
        this.idEmployee = idEmployee;
        this.name = name;
        this.tasks = new ArrayList<Task>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

    public int calculateWorkDurationEmployee() {
        int workDuration = 0;
        for(Task task: this.getTasks()) {
            if(task.getStatusTask() == "Completed") {
                workDuration = task.estimateDuration() + workDuration;
            }
        }
        return workDuration;
    }



    @Override
    public String toString() {
        return "Employee{" +
                "idEmployee=" + idEmployee +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Employee e = (Employee) o;
        return this.calculateWorkDurationEmployee() - e.calculateWorkDurationEmployee() ;
    }
}

