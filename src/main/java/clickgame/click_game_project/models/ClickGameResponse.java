package clickgame.click_game_project.models;

public class ClickGameResponse {

    private boolean valid;
    private int points;
    private boolean finished;

    public ClickGameResponse() {}

    public ClickGameResponse(boolean valid, int points, boolean finished) {
        this.valid = valid;
        this.points = points;
        this.finished = finished;
    }

    public boolean getValid() {
        return this.valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean getFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }



}
