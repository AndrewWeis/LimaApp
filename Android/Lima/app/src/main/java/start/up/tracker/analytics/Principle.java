package start.up.tracker.analytics;

public interface Principle {
    void setStatus(boolean isEnabled);

    boolean getStatus();

    int getTimeToRead();

    String getName();

    void logic();
}

/*
public abstract class Principle {
    private String name;
    //private String description;
    private boolean isEnabled;
    private int timeToRead;

    public void setStatus(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean getStatus() {
        return isEnabled;
    }

    public int getTimeToRead() {
        return timeToRead;
    }

    public String getName() {
        return name;
    }

    abstract public void logic();
}

 */