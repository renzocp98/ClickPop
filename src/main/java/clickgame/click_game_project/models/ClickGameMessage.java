package clickgame.click_game_project.models;

public class ClickGameMessage {

    private int gameId;
    private int x;
    private int y;

    public ClickGameMessage(){}

    public ClickGameMessage(int gameId, int x, int y) {
        this.gameId = gameId;
        this.x = x;
        this.y = y;
    }

    public int getGameId() {
        return this.gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

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
