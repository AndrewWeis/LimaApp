package start.up.tracker.analytics.principles;

import androidx.annotation.NonNull;

import start.up.tracker.analytics.Principle;

public class Pareto implements Principle {
    private final String name = "Pareto Principle";
    private final String description = "ABCDEFG";
    private final int timeToRead = 5;

    private boolean isEnabled;

    @Override
    public void setStatus(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean getStatus() {
        return isEnabled;
    }

    @Override
    public int getTimeToRead() {
        return timeToRead;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void logic() {

    }
}
