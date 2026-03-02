package DataModel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//fiecare coada functioneaza pe un thread separat
public class Queue implements Runnable {
    private BlockingQueue<Client> clients; //thread safe queue
    private volatile boolean running; //pt controlul thread-ului, asigura vizibilitate
    private int id; //id-ul cozii
    private int currentWaitingTime; //serviceTime total pt toti clientii din coada

    public Queue(int id) {
        this.id = id;
        this.clients = new LinkedBlockingQueue<Client>();
        this.running = true;
        this.currentWaitingTime = 0;
    }

    public synchronized void addClient(Client client) {
        clients.add(client);
        currentWaitingTime += client.getServiceTime();
    }

    public synchronized int getWaitingTime() {
        return currentWaitingTime;
    }

    public synchronized boolean isEmpty() {
        return clients.isEmpty();
    }

    public BlockingQueue<Client> getClients() {
        return clients;
    }

    //opreste thread
    public void stop() {
        running = false;
    }

    public synchronized String getStatus() {
        //cand nu mai sunt clienti in coada se inchide
        if (clients.isEmpty()) {
            return "closed";
        }
        String result = "";
        for (Client client : clients) {
            if(client.getRemainingServiceTime() > 0){
                result = result + "(" + client.getId() +", " + client.getArrivalTime() + ", " + client.getRemainingServiceTime()+ ") ";
            }
        }
        return result;
    }


    @Override
    public void run() {
        //cat timp inca mai sunt clienti de procesat
        while (running == true || clients.isEmpty() == false) {
            try {
                Client currentClient = clients.peek(); //putem verifica detaliile despre client
                //clientul ramane in coada pana terminam de procesat
                if (currentClient != null) {
                    Thread.sleep(1000); //daca avem client valid, asteapta 1s = 1000ms pt simulare
                    //adoarme temporar thread-ul ca sa nu consume procesor inutil

                    synchronized (this) {
                        //currentClient.setRemainingServiceTime(currentClient.getRemainingServiceTime() - 1);
                        if (currentClient.getRemainingServiceTime() == 0) {
                            //daca clientul nu mai are timp de stat in coada, este eliminat
                            clients.poll();
                            currentWaitingTime = currentWaitingTime - currentClient.getServiceTime();
                        }
                    }
                } else {
                    Thread.sleep(100); //asteapta inainte de a verifica iar
                    //ne permite sa adaugam clienti noi intre verificari
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}


