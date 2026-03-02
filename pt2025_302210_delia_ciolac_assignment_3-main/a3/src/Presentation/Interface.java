package Presentation;

/**
 * Clasa Interface reprezinta o fereastra GUI pentru gestionarea
 * unei liste de clienti, permitand adaugarea, vizualizarea,
 * actualizarea si stergerea clientilor dintr-o baza de date.
 * Interfata utilizeaza Swing pentru componente grafice si include
 * tabele generate prin reflexie pentru afisarea datelor.
 */

import DataAccess.ClientDAO;
import Model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class Interface extends JFrame {

    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField addressTextField;
    private JTextField emailTextField;
    public static JTable clientsTable;
    public static DefaultTableModel tableModel;
    public static ArrayList<Client> clientsList = new ArrayList<Client>();

    /**
     * Constructorul clasei Interface care initializeaza toate componentele grafice,
     * configureaza layout-ul si ataseaza listeneri pentru butoane.
     * Permite adaugarea, vizualizarea, actualizarea si stergerea clientilor.
     */
    public Interface() {
        setTitle("Client Window");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        //panel principal
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Client Information"));
        GridBagConstraints gbc = new GridBagConstraints(); //seteaza cum arata componenta in JPanel
        gbc.insets = new Insets(5, 10, 5, 10); //da spatiere in jurul componentei
        gbc.anchor = GridBagConstraints.WEST; //aliniaza componenta la stanga

        idTextField = new JTextField(15);
        nameTextField = new JTextField(15);
        addressTextField = new JTextField(15);
        emailTextField = new JTextField(15);

        String[] labels = {"ID:", "Name:", "Address:", "Email:"};
        JTextField[] fields = {idTextField, nameTextField, addressTextField, emailTextField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; //coloana 0
            gbc.gridy = i; //rand i
            inputPanel.add(new JLabel(labels[i]), gbc); //pune eticheta in panel la coloana 0 rand i
            gbc.gridx = 1; //coloana 1
            inputPanel.add(fields[i], gbc); //pune campul in panel la dreapta etichetei
        }

        //panel pentru organizarea butoanelor in interfata
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton addClientButton = new JButton("Add Client");
        JButton viewClientsButton = new JButton("View Clients");
        JButton updateClientButton = new JButton("Update Client");
        JButton deleteClientButton = new JButton("Delete Client");
        JButton openProductWindowButton = new JButton("Open Product Window");
        JButton openOrderWindowButton = new JButton("Open Order Window");

        buttonPanel.add(openProductWindowButton);
        buttonPanel.add(openOrderWindowButton);
        buttonPanel.add(addClientButton);
        buttonPanel.add(viewClientsButton);
        buttonPanel.add(updateClientButton);
        buttonPanel.add(deleteClientButton);

        gbc.gridx = 0; //coloana 0
        gbc.gridy = labels.length; //randul de dupa campurile adaugate anterior
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc); // adauga panel-ul cu butoane in panel-ul principal
        add(inputPanel, BorderLayout.NORTH); //adauga panel-ul principal in interfata

        //creez tabelul pentru afisarea clientilor
        String[] columnNames = {"ID", "Name", "Address", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        clientsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Client List"));
        add(scrollPane, BorderLayout.CENTER); //adaug tabelul in interfata

        //butoane pentru navigare intre ferestre
        openProductWindowButton.addActionListener(e -> new ProductWindow());
        openOrderWindowButton.addActionListener(e -> new OrderWindow());

        //add client
        addClientButton.addActionListener(e -> {
            try {
                //salvez ce a fost introdus de catre utilizator in interfata
                int id = Integer.parseInt(idTextField.getText());
                String name = nameTextField.getText().trim();
                String address = addressTextField.getText().trim();
                String email = emailTextField.getText().trim();

                if (name.isEmpty() || address.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                //creez un obiect nou
                Client client = new Client(id, name, address, email);
                ClientDAO clientDAO = new ClientDAO();
                clientDAO.insert(client); //il inserez in bd
                JOptionPane.showMessageDialog(null, "Client added successfully!");
                clearFields(); //dupa ce adaug un client, field urile unde am introdus date devin din nou libere

            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //view client
        viewClientsButton.addActionListener(e -> {
            ClientDAO clientDAO = new ClientDAO();
            List<Client> clientList = clientDAO.findAll(); //selecteaza toti clientii pe care i am introdus in bd

            JScrollPane newScrollPane = createScrollPaneFromList(clientList, "Client List"); //creez tabel folosind reflection
            getContentPane().remove(1); //sterge componenta de pe poz 1
            add(newScrollPane, BorderLayout.CENTER); //adauga componenta noua modificata
            revalidate(); //tine cont de modificarile facute
            repaint(); //pt a vedea in interfata modificarile
        });

        //update client
        updateClientButton.addActionListener(e -> {
            try {
                //extrag ce a introdus utilizatorul
                int id = Integer.parseInt(idTextField.getText()); //id ul ramane neschimbat pt clientul pe care il editez
                String name = nameTextField.getText().trim();
                String address = addressTextField.getText().trim();
                String email = emailTextField.getText().trim();

                if (name.isEmpty() || address.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                //creez un obiect nou
                Client client = new Client(id, name, address, email);
                ClientDAO clientDAO = new ClientDAO();
                clientDAO.update(client); //metoda din AbstractDAO
                clientsList.removeIf(c -> c.getId() == id); //sterg clientul vechi
                clientsList.add(client); //adaug clientul modificat
                JOptionPane.showMessageDialog(null, "Client updated successfully!");
                clearFields(); //reinitializez campurile
            }
            catch (NumberFormatException | IllegalAccessException ex) {
                JOptionPane.showMessageDialog(null, "ID must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //delete client
        deleteClientButton.addActionListener(e -> {
            try {
                //scriu doar id ul clientului pe care vreau sa il sterg
                int id = Integer.parseInt(idTextField.getText());
                Client clientToDelete = new Client(id, "", "", "");
                ClientDAO clientDAO = new ClientDAO();
                clientDAO.delete(clientToDelete); // il sterg din bd
                clientsList.removeIf(c -> c.getId() == id); //il sterg din interfata
                JOptionPane.showMessageDialog(null, "Client deleted successfully!");

                //actualizez tabel
                tableModel.setRowCount(0); //sterg toate randurile din tabel
                clientsList.stream()
                        .map(c -> new Object[]{c.getId(), c.getName(), c.getAddress(), c.getEmail()})
                        .forEach(tableModel::addRow); //pun in tabel ce se afla in bd, de unde am sters deja clientul
                clearFields(); //reinitializez campuri
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        setVisible(true);
    }

    //reinitializez campurile
    /**
     * Metoda privata ce reinitializeaza campurile de text
     * pentru a pregati interfata pentru o noua introducere de date.
     */
    private void clearFields() {
        idTextField.setText("");
        nameTextField.setText("");
        addressTextField.setText("");
        emailTextField.setText("");
    }

    //tehnica reflexiei
    /**
     * Metoda statica ce creeaza un JScrollPane cu un JTable
     * dintr-o lista de obiecte, folosind reflexie pentru extragerea
     * dinamica a campurilor clasei obiectelor.
     */
    public static <T> JScrollPane createScrollPaneFromList(List<T> list, String borderTitle) {
        //daca lista cu ce vreau sa fie in tabel e goala afisez un tabel gol
        if (list == null || list.isEmpty()) {
            JTable t = new JTable(new DefaultTableModel());
            JScrollPane sp = new JScrollPane(t);
            sp.setBorder(BorderFactory.createTitledBorder(borderTitle));
            return sp;
        }
        try {
            //class<?> = clasa necunoscuta
            Class<?> c = list.get(0).getClass(); //extrag tipul primului element din lista
            Field[] allFields = c.getDeclaredFields(); //extrag toate campurile din clasa
            List<Field> instanceFields = new ArrayList<Field>();
            for (Field field : allFields) {
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) { //extrage numai campurile care nu sunt statice
                    field.setAccessible(true); //permisiuni pt campuri private sau protected
                    instanceFields.add(field); //pun campurile in lista
                }
            }
            //preiau numele campurilor si le pun in vect de string
            String[] columnNames = instanceFields.stream()
                    .map(Field::getName)
                    .toArray(String[]::new);
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            list.forEach(obj -> {
                Object[] rowData = instanceFields.stream()
                        .map(field -> {
                            try {
                                return field.get(obj); //extrag valoarea corespunzatoare campului
                            } catch (IllegalAccessException e) {
                                return "exc";
                            }
                        })
                        .toArray();
                model.addRow(rowData); //o pun in tabel
            });

            JTable table = new JTable(model);
            JScrollPane sp = new JScrollPane(table);
            sp.setBorder(BorderFactory.createTitledBorder(borderTitle));
            return sp;
        } catch (Exception e) {
            e.printStackTrace();
            JTable errT = new JTable(new DefaultTableModel());
            JScrollPane errSp = new JScrollPane(errT);
            errSp.setBorder(BorderFactory.createTitledBorder(borderTitle));
            return errSp;
        }
    }
    /**
     * Metoda principala care lanseaza aplicatia si creeaza o instanta
     * a ferestrei Interface.
     */
    public static void main(String[] args) {

        new Interface();
    }
}

