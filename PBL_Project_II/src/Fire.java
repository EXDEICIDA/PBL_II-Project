import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Fire {
    private Coord coord;
    public Queue fireQueue;
    private int count;


    public Fire(Coord coord,char[][] maze){
        this.coord = coord;
        this.count = 0;
        this.fireQueue = new Queue(550);


    }
    public Queue getFireQueue(){
        return fireQueue;
    }

    public void enqueueFireQueue(Object o){
        fireQueue.enqueue(o);
    }
    public Object dequeueFireQueue(){
        return fireQueue.dequeue();
    }
    public Coord getFireCoord(){
        return this.coord;
    }
    public int getCount(){
        return count;
    }



    public void createFireQueue() throws InterruptedException {
        fireQueue.enqueue(coord);
        while(fireQueue.size()<496){
            Coord coord = (Coord) fireQueue.dequeue();
            if (Game.maze[coord.getX()][coord.getY()] == ' '){
                Game.maze[coord.getX()][coord.getY()] = '-';
                Game.duration[coord.getX()][coord.getY()] = 10;
                Game.printMaze();
                Thread.sleep(2);
            }

            if(coord.getX() != 0 && coord.getY() != 0 && coord.getX() != 22 && coord.getY() != 52){
                fireQueue.enqueue(new Coord(coord.getX()-1,coord.getY()));
                fireQueue.enqueue(new Coord(coord.getX(),coord.getY()-1));
                fireQueue.enqueue(new Coord(coord.getX()+1,coord.getY()));
                fireQueue.enqueue(new Coord(coord.getX(),coord.getY()+1));
            }




        }

    }


}







