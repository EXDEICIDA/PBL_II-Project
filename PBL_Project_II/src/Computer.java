public class Computer {
    private int score;
    private int life;
    private Coord coord;
    private Coord target;
    public Computer(Coord coord,Coord target){
        this.target = target;
        this.score = 0;
        this.coord = coord;
        this.life = 1000;
    }
    public int getLife(){
        return life;
    }
    public void setTarget(Coord c){
        target = c;
    }
    public Coord getTarget(){
        return target;
    }

    public void getHarm(){
        if(life > 0){
            life -= 50;
        }

    }
    public void move(){
        int x = coord.getX();
        int y = coord.getY();
        if(target.getX() < x){
            x--;
        }
        else if(target.getX() > x){
            x++;
        }

        else if(target.getY() < y){
            y--;
        }
        else if(target.getY() > y){
            y++;
        }

        if(Game.maze[x][y] == ' '){
            Game.maze[x][y] = 'C';
            Game.maze[coord.getX()][coord.getY()] = ' ';
            this.setCoord(new Coord(x,y));
        }
        else if(Game.maze[x][y] == '1'){
            this.collect1();
            this.setTarget(new Coord(0,0));
        }
        else if(Game.maze[x][y] == '2'){
            this.collect2();
            this.setTarget(new Coord(0,0));
        }
        else if(Game.maze[x][y] == '3'){
            this.setTarget(new Coord(0,0));
        }



    }
    public int getScore(){
        return score;
    }
    public Coord getCoord(){
        return coord;
    }
    public void setCoord(Coord c){
        coord = c;
    }
    public void updateCoord(Coord coordd){
        coord = coordd;
    }
    public void collect1(){
        this.score += 9;
    }
    public void collect2(){
        this.score += 30;
    }
    public void collect3(){
        this.score += 90;
    }
}
