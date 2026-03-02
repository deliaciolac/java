package Model;

public record Bill(int id, int orderId, int clientId, int productId, int productPrice, int quantity, int total) {
    public Bill(int orderId, int clientId, int productId, int productPrice, int quantity, int total) {
        this(0, orderId, clientId, productId, productPrice, quantity, total);
    }
}

