public class Player {
    private String name;
    public int score;
    private int life;
    public int ice;

    public Player(String name, int score){
        this.name = name;
        this.score = score;
        this.life = 1000;
        this.ice = 0;
    }
    public boolean isAlive(){
        return life > 0;
    }
    public int getScore(){
        return score;
    }
    public int getLife(){
        return life;
    }


    public void getHarm(char c){
        if(c == 'C' && isAlive())
            life -= 50;
        if(c == '-' && isAlive())
            life -= 1;
    }
    public void setScore(int score){
        this.score = score;
    }

    public int getIce(){
        return ice;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void collectIce(){
        this.ice++;
    }
    public void collect1(){
        this.score += 3;
    }
    public void collect2(){
        this.score += 10;
    }
    public void collect3(){
        this.score += 30;
    }

}

