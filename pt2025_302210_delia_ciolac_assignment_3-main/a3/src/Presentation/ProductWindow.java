package Presentation;

import DataAccess.ProductDAO;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Fereastra GUI pentru gestionarea produselor.
 * Permite adaugarea, vizualizarea, modificarea si stergerea produselor.
 * Foloseste un tabel pentru afisarea produselor si campuri de input pentru introducerea datelor.
 */

public class ProductWindow extends JFrame {

    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField stockTextField;
    private JTextField priceTextField;
    public static JTable productsTable;
    public static DefaultTableModel tableModel;
    public static ArrayList<Product> productsList = new ArrayList<>();

    /**
     * Constructor care initializeaza interfata grafica pentru gestionarea produselor.
     * Configureaza layout-ul, campurile de input, butoanele si comportamentul acestora.
     */
    public ProductWindow() {
        setTitle("Product Window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Product Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        idTextField = new JTextField(15);
        nameTextField = new JTextField(15);
        stockTextField = new JTextField(15);
        priceTextField = new JTextField(15);

        String[] labels = {"ID:", "Name:", "Stock", "Price:"};
        JTextField[] fields = {idTextField, nameTextField, stockTextField, priceTextField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            inputPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            inputPanel.add(fields[i], gbc);
        }
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton addProductButton = new JButton("Add Product");
        JButton viewProductsButton = new JButton("View Products");
        JButton updateProductButton = new JButton("Update Product");
        JButton deleteProductButton = new JButton("Delete Product");

        buttonPanel.add(addProductButton);
        buttonPanel.add(viewProductsButton);
        buttonPanel.add(updateProductButton);
        buttonPanel.add(deleteProductButton);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Stock", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Product List"));
        add(scrollPane, BorderLayout.CENTER);

        //add product
        addProductButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idTextField.getText());
                String name = nameTextField.getText().trim();
                int stock = Integer.parseInt(stockTextField.getText());
                int price = Integer.parseInt(priceTextField.getText());

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Product product = new Product(id, name, stock, price);
                ProductDAO productDAO = new ProductDAO();
                productDAO.insert(product);
                productsList.add(product);
                JOptionPane.showMessageDialog(null, "Product added successfully!");
                clearFields();
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //view products
        viewProductsButton.addActionListener(e -> {
            ProductDAO productDAO = new ProductDAO();
            java.util.List<Product> productList = productDAO.findAll();
            JScrollPane newScrollPane = Interface.createScrollPaneFromList(productList, "Product List");
            getContentPane().remove(1);
            add(newScrollPane, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        //update product
        updateProductButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idTextField.getText());
                String name = nameTextField.getText().trim();
                int stock = Integer.parseInt(stockTextField.getText());
                int price = Integer.parseInt(priceTextField.getText());

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Product product = new Product(id, name, stock, price);
                ProductDAO productDAO = new ProductDAO();
                productDAO.update(product); //modific produsul in bd
                productsList.removeIf(p -> p.getId() == id); //sterg produsul vechi
                productsList.add(product); //adaug produsul nou
                JOptionPane.showMessageDialog(null, "Product updated successfully!");
                clearFields();
            }
            catch (NumberFormatException | IllegalAccessException ex) {
                JOptionPane.showMessageDialog(null, "Invalid data!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //delete product
        deleteProductButton.addActionListener(e -> {
            try {
                //introduc doar id ul produsului ca sa il sterg
                int id = Integer.parseInt(idTextField.getText());
                Product productToDelete = new Product(id, "", 0, 0);

                ProductDAO productDAO = new ProductDAO();
                productDAO.delete(productToDelete);
                productsList.removeIf(p -> p.getId() == id);
                JOptionPane.showMessageDialog(null, "Product deleted successfully!");

                tableModel.setRowCount(0);
                List<Product> refreshedList = productDAO.findAll();
                tableModel.setRowCount(0); // Șterge toate rândurile din tabel
                for (Product p : refreshedList) {
                    tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getStock(), p.getPrice()});
                }
                clearFields();
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid data!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        setVisible(true);
    }
    /**
     * Metoda privata ce reinitializeaza campurile de text
     * pentru a pregati interfata pentru o noua introducere de date.
     */
    private void clearFields() {
        idTextField.setText("");
        nameTextField.setText("");
        stockTextField.setText("");
        priceTextField.setText("");
    }
    /**
     * Metoda principala care lanseaza aplicatia si creeaza o instanta
     * a ferestrei Interface.
     */
    public static void main(String[] args) {

        new ProductWindow();
    }
}

