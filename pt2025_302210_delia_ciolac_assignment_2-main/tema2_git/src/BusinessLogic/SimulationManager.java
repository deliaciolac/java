package BusinessLogic;

import DataModel.Client;
import DataModel.Queue;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable{
    private int timeLimit; //cat dureaza simularea
    private int minArrival; //timp minim sosire
    private int maxArrival; //timp maxim sosire
    private int minService; //timp minim servire
    private int maxService; //timp maxim servire
    private int nrClients; //cati clienti avem in simulare
    private Scheduler scheduler; //pt distribuirea clientilor la cozi
    private List<Client> randomClients; //lista de clienti geenrati aleatoriu
    private FileWriter log; //pt scriere in .txt
    private int currentTime = 0;
    private int totalWaitingTime = 0; //timp max de asteptare
    private JTextArea logArea;

    public SimulationManager(int numberOfClients, int numberOfQueues, int timeLimit,
                             int minArrival, int maxArrival, int minService, int maxService,
                             String logFileName, JTextArea logArea) throws IOException {
        this.timeLimit = timeLimit;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minService = minService;
        this.maxService = maxService;
        this.nrClients = numberOfClients;
        this.scheduler = new Scheduler(numberOfQueues);
        this.randomClients = generateRandomClients();
        this.log = new FileWriter(logFileName);
        this.logArea = logArea;
    }

    //generator de clienti random
    private List<Client> generateRandomClients() {
        List<Client> clients = new ArrayList<Client>();
        Random random = new Random();
        for (int i = 0; i < nrClients; i++) {
            int arrivalTime = minArrival + random.nextInt(maxArrival - minArrival + 1); //genereaza random timpul de sosire
            int serviceTime = minService + random.nextInt(maxService - minService + 1); //genereaza timpul de asteptare
            clients.add(new Client(i + 1, arrivalTime, serviceTime)); //adauga client in lista
        }
        clients.sort(Client.arrivalTimeComparator()); //sorteaza dupa timpul de sosire
        return clients;
    }

    //verifica daca toate cozile sunt goale
    private boolean allQueuesEmpty() {
        for (Queue queue : scheduler.getQueues()) {
            if (queue.isEmpty() == false) {
                return false;
            }
        }
        return true;
    }

    private void dispatchArrivedClients() {
        List<Client> toRemove = new ArrayList<Client>();
        for (Client client : randomClients) {
            if (client.getArrivalTime() <= currentTime) { //daca e momentul ca acel client sa mearga in coada
                scheduler.dispatchClient(client); //ii caut coada si il distribui
                for (Queue queue : scheduler.getQueues()) {
                    totalWaitingTime += queue.getWaitingTime(); //ca sa putem calcula average
                }
                toRemove.add(client); //il adaug la lista cu clientii pe care vreau sa ii sterg
            }
        }
        randomClients.removeAll(toRemove); //ii sterg din lista de asteptare pe cei care au fost distribuiti
    }

    private void logs() throws IOException {
        String res = "\nTime " + currentTime + "\n" + "Waiting clients: ";
        //log.write(res);

        if (randomClients.isEmpty() == true) {
            res += "\n";
        }
        else {
            for (Client client : randomClients){
                if(client.getArrivalTime() < currentTime){
                    int r = client.getRemainingServiceTime() - 1;
                    client.setRemainingServiceTime(r);
                }
                res = res + client.toString() + " ";
            }
            res = res + "\n";
        }

        int nrQ = 1;
        for (Queue queue : scheduler.getQueues()) {
            for(Client c: queue.getClients()){
                if(c.getArrivalTime() < currentTime && c.getRemainingServiceTime() > 0){
                    c.setRemainingServiceTime(c.getRemainingServiceTime() - 1);
                }
            }
            res = res + "Queue " + nrQ++ + ": " + queue.getStatus() + "\n";
        }
        log.write(res);
        String finalRes = res;
        SwingUtilities.invokeLater(() -> {
            logArea.append(finalRes);
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void statistics() throws IOException {
        log.write("Average: " +
                (float)totalWaitingTime / nrClients + "\n");
    }

    @Override
    public void run() {
        try {
            while (currentTime < timeLimit || (randomClients.isEmpty() == false || allQueuesEmpty() == false)) {
                dispatchArrivedClients();
                logs();
                Thread.sleep(1000);
                currentTime++;
            }
            statistics();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                log.close();
                scheduler.stopAllQueues();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

