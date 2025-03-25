package clickgame.click_game_project.models;

public class ClickGameResponse {

    private boolean valid;
    private int points;
    
    public ClickGameResponse() {}

    public ClickGameResponse(boolean valid, int points) {
        this.valid = valid;
        this.points = points;
    }

    public boolean getMessage() {
        return this.valid;
    }

    public void setMessage(boolean valid) {
        this.valid = valid;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    


}
