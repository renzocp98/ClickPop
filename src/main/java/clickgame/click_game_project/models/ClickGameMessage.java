package clickgame.click_game_project.models;

public class ClickGameMessage {

    private int x;
    private int y;

    public ClickGameMessage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ClickGameMessage(){}

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
