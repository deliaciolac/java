package BusinessLogic;

import DataModel.Client;
import DataModel.Queue;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Queue> queues; //pt a vedea toate cozile disponibile
    private int nrQueues; //cate cozi avem

    public Scheduler(int nrQueues) {
        this.queues = new ArrayList<Queue>();
        //creez nrQueues cozi care sa ruleze fiecare pe un thread separat
        for (int i = 0; i < nrQueues; i++) {
            Queue queue = new Queue(i + 1);
            queues.add(queue);
            new Thread(queue).start();
        }
    }

    public synchronized void dispatchClient(Client client) {
        //punem synchronized pt ca distribuirea clientilor sa fie thread-safe
        //caut coada ideala pt a trimite clientul
        Queue perfQ = queues.get(0); //initializez cu prima coada
        int minWaitingTime = perfQ.getWaitingTime();

        for (Queue queue : queues) {
            if (queue.getWaitingTime() < minWaitingTime) {
                //daca gasesc o coada cu un timp de asteptare mai redus
                minWaitingTime = queue.getWaitingTime();
                perfQ = queue;
            }
        }
        perfQ.addClient(client);
    }

    public synchronized List<Queue> getQueues() {
        return queues;
    }

    //se inchid toate thread-urile
    public synchronized void stopAllQueues() {
        for (Queue queue : queues) {
            queue.stop();
        }
    }
}

