package DataModel;

import java.io.Serializable;

public non-sealed class SimpleTask extends Task implements Serializable {
    private static final long serialVersionUID = 1L;
    int startHour;
    int endHour;

    public SimpleTask(int idTask, int startHour, int endHour) {
        super(idTask);
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int estimateDuration() {
        if(startHour <0 || endHour <0 || startHour > 23 || endHour > 23) {
            System.out.println("Interval de timp invalid");
            return -1;
        }
        if(endHour <= startHour)
        {
            return 24-startHour + endHour;
        }
        else {
            return endHour - startHour;
        }
    }

    @Override
    public String toString() {
        return "SimpleTask{" +
                "startHour=" + startHour +
                ", endHour=" + endHour +
                ", idTask=" + idTask +
                ", statusTask='" + statusTask + '\'' +
                '}';
    }
}
