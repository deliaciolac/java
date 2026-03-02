package DataModel;

import java.util.Comparator;

public class Client {
    private int id;
    private int arrivalTime; //momentul in care clientul poate face parte dintr-o coada
    private int serviceTime; //cat timp trebuie sa stea clientul in coada
    private int remainingServiceTime; //cat mai trebuie sa stea in coada

    public Client(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.remainingServiceTime = serviceTime; //initial clientul trebuie sa mai astepte serviceTime
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getRemainingServiceTime() {
        return remainingServiceTime;
    }

    public void setRemainingServiceTime(int remainingServiceTime) {
        this.remainingServiceTime = remainingServiceTime;
    }

    @Override
    public String toString() {
        return "(" + id + "," + arrivalTime + "," + remainingServiceTime + ")";
    }

    public static Comparator<Client> arrivalTimeComparator() {
        return Comparator.comparingInt(Client::getArrivalTime);
    }
}
