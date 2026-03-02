package GraphicalUserInterface;

import BusinessLogic.Utility;
import DataModel.ComplexTask;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class ProjectManager extends JFrame {
    private JTextField numeTextField, IDTextField, idTaskTextField;
    private JTextArea rezultatTextArea, taskTextArea, tasksAssignedTextArea, viewEmployeesTextArea, workDuration40TextArea, completedTasksTextArea;
    private JList<Employee> listaAngajati;
    private JList<Task> listaTasks;
    private JButton adaugaAngajatiButton, adaugaTaskButton, assignTaskButton, viewEmployeesAndTheirButton, modifyTheStatusOfButton, workDuration40Button, completedTasksButton, saveButton, loadButton;;

    ArrayList<Employee> listOfEmployees = new ArrayList<>();
    ArrayList<Task> listOfTasks = new ArrayList<>();
    DefaultListModel<Employee> defaultListModel = new DefaultListModel<>();
    DefaultListModel<Task> defaultListModel1 = new DefaultListModel<>();

    public ProjectManager() {
        setTitle("Project Manager");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        numeTextField = new JTextField(10);
        IDTextField = new JTextField(10);
        idTaskTextField = new JTextField(10);
        rezultatTextArea = new JTextArea(5, 20);
        taskTextArea = new JTextArea(5, 20);
        tasksAssignedTextArea = new JTextArea(5, 20);
        viewEmployeesTextArea = new JTextArea(5, 20);
        workDuration40TextArea = new JTextArea(5, 20);
        completedTasksTextArea = new JTextArea(5, 20);
        listaAngajati = new JList<>(defaultListModel);
        listaTasks = new JList<>(defaultListModel1);
        adaugaAngajatiButton = new JButton("Adauga Angajat");
        adaugaTaskButton = new JButton("Adauga Task");
        assignTaskButton = new JButton("Asigneaza Task");
        viewEmployeesAndTheirButton = new JButton("Vezi Angajati");
        modifyTheStatusOfButton = new JButton("Modifica Status Task");
        workDuration40Button = new JButton("Filtrare Durata >40h");
        completedTasksButton = new JButton("Completed tasks");
        saveButton = new JButton("Save Data");
        loadButton = new JButton("Load Data");

        add(new JLabel("Nume:"));
        add(numeTextField);
        add(new JLabel("ID Angajat:"));
        add(IDTextField);
        add(new JLabel("ID Task:"));
        add(idTaskTextField);
        add(adaugaAngajatiButton);
        add(adaugaTaskButton);
        add(assignTaskButton);
        add(viewEmployeesAndTheirButton);
        add(modifyTheStatusOfButton);
        add(workDuration40Button);
        add(completedTasksButton);
        add(saveButton);
        add(loadButton);
        add(new JScrollPane(listaAngajati));
        add(new JScrollPane(listaTasks));
        add(new JScrollPane(rezultatTextArea));
        add(new JScrollPane(taskTextArea));
        add(new JScrollPane(tasksAssignedTextArea));
        add(new JScrollPane(viewEmployeesTextArea));
        add(new JScrollPane(workDuration40TextArea));
        add(new JScrollPane(completedTasksTextArea));

        adaugaAngajatiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nume = numeTextField.getText();
                int ID = Integer.parseInt(IDTextField.getText());
                Employee employee = new Employee(ID, nume);
                defaultListModel.addElement(employee);
                listOfEmployees.add(employee);
                rezultatTextArea.append("\n" + nume + ", " + ID);
            }
        });

        adaugaTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int ID = Integer.parseInt(idTaskTextField.getText());
                String type = JOptionPane.showInputDialog("Simple or Complex Task:");
                if (type.equalsIgnoreCase("Simple")) {
                    int start = Integer.parseInt(JOptionPane.showInputDialog("Start Hour:"));
                    int end = Integer.parseInt(JOptionPane.showInputDialog("End Hour:"));
                    SimpleTask simpleTask = new SimpleTask(ID, start, end);
                    defaultListModel1.addElement(simpleTask);
                    listOfTasks.add(simpleTask);
                    taskTextArea.append("\nID: " + ID + " ,startHour: " + start + " ,endHour: " + end);
                } else if (type.equalsIgnoreCase("Complex")) {
                    ComplexTask complexTask = new ComplexTask(ID);
                    StringBuilder rezultat = new StringBuilder("ID: " + ID);
                    boolean moreSubtasks = true;
                    while (moreSubtasks) {
                        int subtaskID = Integer.parseInt(JOptionPane.showInputDialog("Subtask ID:"));
                        listOfTasks.stream().filter(t -> t.getIdTask() == subtaskID).findFirst().ifPresent(complexTask::addTask);
                        rezultat.append("\nSubtask: ").append(subtaskID);
                        moreSubtasks = JOptionPane.showConfirmDialog(null, "More subtasks?") == JOptionPane.YES_OPTION;
                    }
                    defaultListModel1.addElement(complexTask);
                    listOfTasks.add(complexTask);
                    taskTextArea.append("\n" + rezultat);
                }
            }
        });

        assignTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idEmployee = Integer.parseInt(JOptionPane.showInputDialog("Employee ID:"));
                int idTask = Integer.parseInt(JOptionPane.showInputDialog("Task ID:"));

                Employee employee = listOfEmployees.stream().filter(emp -> emp.getIdEmployee() == idEmployee).findFirst().orElse(null);
                Task task = listOfTasks.stream().filter(t -> t.getIdTask() == idTask).findFirst().orElse(null);

                if (employee == null) {
                    JOptionPane.showMessageDialog(null, "Invalid employee!");
                } else if (task == null) {
                    JOptionPane.showMessageDialog(null, "Invalid task!");
                } else {
                    employee.getTasks().add(task);
                    tasksAssignedTextArea.append("\nEmployee ID: " + employee.getIdEmployee() + " ,task ID: " + task.getIdTask());
                    JOptionPane.showMessageDialog(null, "Task assigned!");
                }
            }
        });

        this.viewEmployeesAndTheirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idEmployee = JOptionPane.showInputDialog("Employee ID: ");
                int idEmp = Integer.parseInt(idEmployee);
                String rezultat = "Employee ID: " + idEmp;
                Employee employee = null;
                for(Employee emp: listOfEmployees) {
                    if(emp.getIdEmployee() == idEmp) { //daca angajatul este valid
                        employee = emp;
                    }
                }
                if(employee == null) {
                    JOptionPane.showMessageDialog(null, "Invalid employee!");
                }
                else {
                    for(Task t : employee.getTasks()) {
                        rezultat = rezultat + "\nTask" + t.getIdTask() + ": " + t.estimateDuration() + "hours";
                    }
                }
                String textArea = ProjectManager.this.viewEmployeesTextArea.getText();
                ProjectManager.this.viewEmployeesTextArea.setText(textArea + "\n" + rezultat);
            }
        });

        this.modifyTheStatusOfButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Task task = null;
                String taskID = JOptionPane.showInputDialog("Task ID: ");
                int idTask = Integer.parseInt(taskID);
                for(Task t: listOfTasks) {
                    if(t.getIdTask() == idTask) {
                        task = t;
                    }
                }
                if(task == null) {
                    JOptionPane.showMessageDialog(null, "Invalid task!");
                }
                else {
                    String status = JOptionPane.showInputDialog("Status: ");
                    task.setStatusTask(status);
                    JOptionPane.showMessageDialog(null, "Status for task: "+task.getIdTask() + ": " + task.getStatusTask());
                }
            }
        });

        this.workDuration40Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Utility u = new Utility();
                Utility.filterEmployees(listOfEmployees);
                for(Employee emp: listOfEmployees) {
                    String rezultat = "ID: " + emp.getIdEmployee();
                    String textArea = ProjectManager.this.workDuration40TextArea.getText();
                    ProjectManager.this.workDuration40TextArea.setText(textArea + "\n" + rezultat);
                }
            }
        });

        this.completedTasksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Map<String, Map<String, Integer>> taskStatusMap = Utility.calculateTaskStatusCount(listOfEmployees);
                StringBuilder result = new StringBuilder("Task Status for Employees:\n");
                for (Map.Entry<String, Map<String, Integer>> entry : taskStatusMap.entrySet()) {
                    result.append("Employee: ").append(entry.getKey()).append("\n");
                    result.append("  Completed Tasks: ").append(entry.getValue().get("Completed")).append("\n");
                    result.append("  Uncompleted Tasks: ").append(entry.getValue().get("Uncompleted")).append("\n");
                }
                JOptionPane.showMessageDialog(null, result.toString());
            }
        });

        this.saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (FileOutputStream fileOut = new FileOutputStream("data.ser");
                     ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                    out.writeObject(listOfEmployees);
                    out.writeObject(listOfTasks);
                    JOptionPane.showMessageDialog(null, "Datele au fost salvate!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (FileInputStream fileIn = new FileInputStream("data.ser");
                     ObjectInputStream in = new ObjectInputStream(fileIn)) {
                    listOfEmployees = (ArrayList<Employee>) in.readObject();
                    listOfTasks = (ArrayList<Task>) in.readObject();

                    defaultListModel.clear();
                    defaultListModel1.clear();
                    for (Employee emp : listOfEmployees) {
                        defaultListModel.addElement(emp);
                    }
                    for (Task task : listOfTasks) {
                        defaultListModel1.addElement(task);
                    }
                    JOptionPane.showMessageDialog(null, "Datele au fost ancarcate!");
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new ProjectManager();
    }
}


