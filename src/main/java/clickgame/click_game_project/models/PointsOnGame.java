package clickgame.click_game_project.models;

import java.util.List;

public class PointsOnGame {
    
    //coordenada (x,y) de los puntos usados para iniciar el juego
    private List<int[]> points;

    public PointsOnGame(List<int[]> points) {
        this.points = points;
    }

    public List<int[]> getPoints() {
        return points;
    }

    public void setPoints(List<int[]> points) {
        this.points = points;
    }
     

}
