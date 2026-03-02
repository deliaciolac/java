package Presentation;

import DataAccess.BillDAO;
import DataAccess.ClientDAO;
import DataAccess.OrderDAO;
import DataAccess.ProductDAO;
import Model.Bill;
import Model.Client;
import Model.Orders;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * Fereastra principala pentru gestionarea comenzilor.
 * Permite selectarea unui client si produs, specificarea cantitatii si crearea comenzilor.
 * Comenzile sunt afisate intr-un tabel si salvate in baza de date.
 */

public class OrderWindow extends JFrame {
    private JTable clientsTable;
    private JTable productsTable;
    private JTable ordersTable;
    private DefaultTableModel clientsTableModel;
    private DefaultTableModel productsTableModel;
    private DefaultTableModel ordersTableModel;
    private JTextField quantityTextField;
    private JTextField orderIdTextField;

    /**
     * Constructor care initializeaza interfata grafica si incarca datele.
     */
    public OrderWindow() {
        setTitle("Orders Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        //panou principal
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Order Information"));
        inputPanel.add(new JLabel("Quantity:"));
        quantityTextField = new JTextField(10);
        inputPanel.add(quantityTextField); //adaug campul in panoul principal
        inputPanel.add(new JLabel("Order ID:"));
        orderIdTextField = new JTextField(10);
        inputPanel.add(orderIdTextField);
        add(inputPanel, BorderLayout.NORTH); //adauga panoul principal in interfata

        JButton createOrderButton = new JButton("Create Order");
        add(createOrderButton, BorderLayout.SOUTH); //adaug butonul in nterfata

        //aici afisez clientii pe care i am inserat in interfata client
        String[] clientColumns = {"ID", "Name", "Address", "Email"};
        clientsTableModel = new DefaultTableModel(clientColumns, 0);
        clientsTable = new JTable(clientsTableModel);
        JScrollPane scrollPaneClients = new JScrollPane(clientsTable);
        scrollPaneClients.setBorder(BorderFactory.createTitledBorder("Client List"));

        //aici afisez produsele pe care le am inserat in interfata product window
        String[] productColumns = {"ID", "Name", "Stock", "Price"};
        productsTableModel = new DefaultTableModel(productColumns, 0);
        productsTable = new JTable(productsTableModel);
        JScrollPane scrollPaneProducts = new JScrollPane(productsTable);
        scrollPaneProducts.setBorder(BorderFactory.createTitledBorder("Product List"));

        //afisez comenzile realizate
        String[] orderColumns = {"Order ID", "Client ID", "Product ID", "Quantity"};
        ordersTableModel = new DefaultTableModel(orderColumns, 0);
        ordersTable = new JTable(ordersTableModel);
        JScrollPane scrollPaneOrders = new JScrollPane(ordersTable);
        scrollPaneOrders.setBorder(BorderFactory.createTitledBorder("Order List"));

        //toate tabelele apar in interfata unul dupa altul
        JSplitPane splitPaneTop = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPaneClients, scrollPaneProducts);
        splitPaneTop.setResizeWeight(0.5);
        JSplitPane splitPaneMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneTop, scrollPaneOrders);
        splitPaneMain.setResizeWeight(0.5);
        add(splitPaneMain, BorderLayout.CENTER);

        populateTables(); //apelez metoda prin care listele de clienti i produse sunt afisate in tabelele de mai sus

        //create order
        createOrderButton.addActionListener(e -> {
            //pt a crea o comanda selectez un client si un produs si introduc cantitatea intr-un textField
            int selectedClientRow = clientsTable.getSelectedRow(); //clientul selectat
            int selectedProductRow = productsTable.getSelectedRow(); //produsul selectat

            if (selectedClientRow == -1 || selectedProductRow == -1) {
                JOptionPane.showMessageDialog(OrderWindow.this, "Select a client and a product.");
                return;
            }

            //extrag datele necesare pt comanda
            int clientId = (int) clientsTableModel.getValueAt(selectedClientRow, 0);
            int productId = (int) productsTableModel.getValueAt(selectedProductRow, 0);
            int stock = (int) productsTableModel.getValueAt(selectedProductRow, 2);
            int price = (int) productsTableModel.getValueAt(selectedProductRow, 3);
            int quantity;
            int orderId;
            try {
                quantity = Integer.parseInt(quantityTextField.getText()); //extrag ce s a introdus in textField
                orderId = Integer.parseInt(orderIdTextField.getText());
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(OrderWindow.this, "Enter a valid quantity.");
                return;
            }
            if (quantity > stock) {
                JOptionPane.showMessageDialog(OrderWindow.this, "Not enough stock");
                return;
            }
            productsTableModel.setValueAt(stock - quantity, selectedProductRow, 2); //modific stocul dupa efectuarea comenzii
            Object[] orderRow = {orderId, clientId, productId, quantity};
            Orders order = new Orders(orderId, clientId, productId, quantity);
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.insert(order); //inserez in bd
            ordersTableModel.addRow(orderRow); //inserez in interfata

            String productName = (String) productsTableModel.getValueAt(selectedProductRow, 1);
            Product updatedProduct = new Product(productId, productName, stock - quantity, price);
            ProductDAO productDAO = new ProductDAO();
            try {
                productDAO.update(updatedProduct);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            Bill bill = new Bill(orderId, clientId, productId, price, quantity, price * quantity);
            BillDAO billDAO = new BillDAO();
            billDAO.insert(bill);
            JOptionPane.showMessageDialog(null, "Order created successfully!");
            quantityTextField.setText("");
            orderIdTextField.setText("");
        });
        setVisible(true);
    }

    /**
     * Populeaza tabelele cu datele despre clienti si produse din baza de date.
     */
    private void populateTables() {
        ClientDAO clientDAO = new ClientDAO();
        java.util.List<Client> clientList = clientDAO.findAll(); //selectez toti clientii din bd
        clientsTableModel.setRowCount(0); //fac tabelul sa fie gol initial
        clientList.stream()
                .map(c -> new Object[]{c.getId(), c.getName(), c.getAddress(), c.getEmail()})
                .forEach(clientsTableModel::addRow); //pt fiecare client din lista adaug o linie noua in tabel

        productsTableModel.setRowCount(0);
        ProductDAO productDAO = new ProductDAO();
        java.util.List<Product> productList = productDAO.findAll();
        productList.stream()
                .map(p -> new Object[]{p.getId(), p.getName(), p.getStock(), p.getPrice()})
                .forEach(productsTableModel::addRow); //pt fiecare produs din lista adaug o linie noua in tabel
    }
    /**
     * Metoda principala care lanseaza aplicatia si creeaza o instanta
     * a ferestrei Interface.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderWindow());
    }
}

