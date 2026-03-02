package DataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public non-sealed class ComplexTask extends Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Task> tasks;

    public ComplexTask(int idTask) {
        super(idTask);
        this.tasks = new ArrayList<Task>();
    }

    public int estimateDuration() {
        int duration = 0;
        for(Task t: tasks) {
            duration += t.estimateDuration();
        }
        return duration;
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

    @Override
    public String toString() {
        return "ComplexTask{" +
                "tasks=" + tasks +
                ", idTask=" + idTask +
                ", statusTask='" + statusTask + '\'' +
                '}';
    }
}

