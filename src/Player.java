public class Player {
    int playerX, playerY;

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public void moveUp(){
        this.playerY -= 1;
    }
    public void moveDown(){
        this.playerY += 1;
    }
    public void moveLeft(){
        this.playerX -= 1;
    }
    public void moveRight(){
        this.playerX += 1;
    }
}
