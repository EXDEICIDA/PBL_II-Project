import java.awt.*;
import java.io.Console;

public class Ice {
    private Coord coord;
    private Stack iceStack;

    public Ice(Coord coord){
        this.coord = coord;
        this.iceStack = new Stack(400);
    }


    public void spreadIce(int direction) throws InterruptedException {
        //direction       1
        //                ^
        //             2<   >3
        //                4

        iceStack.push(new Coord(coord.getX(),coord.getY()));


        while(iceStack.size() < 380 && !iceStack.isEmpty()){
            Coord coord = (Coord) iceStack.pop();
            if(Game.maze[coord.getX()][coord.getY()] == ' '){
                Game.maze[coord.getX()][coord.getY()] = '+';
                Game.duration[coord.getX()][coord.getY()] = 10;
                Game.printMaze();
                Thread.sleep(2);
            }


            if(direction == 1){
                if(Game.maze[coord.getX()-1][coord.getY()] != '#'){
                    if(Game.maze[coord.getX()][coord.getY()-1] != '#')
                        iceStack.push(new Coord(coord.getX(),coord.getY()-1));
                    if(Game.maze[coord.getX()][coord.getY()+1] != '#')
                        iceStack.push(new Coord(coord.getX(),coord.getY()+1));
                    iceStack.push(new Coord(coord.getX()-1,coord.getY()));


                }
                else
                    direction = 2;
            }
            else if(direction == 2){
                if(Game.maze[coord.getX()][coord.getY()-1] != '#'){
                    if(Game.maze[coord.getX()-1][coord.getY()] != '#')
                        iceStack.push(new Coord(coord.getX()-1,coord.getY()));
                    if(Game.maze[coord.getX()+1][coord.getY()] != '#')
                        iceStack.push(new Coord(coord.getX()+1,coord.getY()));
                    iceStack.push(new Coord(coord.getX(),coord.getY()-1));


                }
                else
                    direction = 4;
            }
            else if(direction == 4){
                if(Game.maze[coord.getX()+1][coord.getY()] != '#'){

                    if(Game.maze[coord.getX()][coord.getY()-1] != '#')
                        iceStack.push(new Coord(coord.getX(),coord.getY()-1));
                    if(Game.maze[coord.getX()][coord.getY()+1] != '#')
                        iceStack.push(new Coord(coord.getX(),coord.getY()+1));
                    iceStack.push(new Coord(coord.getX()+1,coord.getY()));


                }
                else
                    direction = 3;

            }
            else  if(direction == 3){
                if(Game.maze[coord.getX()][coord.getY()+1] != '#'){
                    if(Game.maze[coord.getX()-1][coord.getY()] != '#')
                        iceStack.push(new Coord(coord.getX()-1,coord.getY()));
                    if(Game.maze[coord.getX()+1][coord.getY()] != '#')
                        iceStack.push(new Coord(coord.getX()+1,coord.getY()));
                    iceStack.push(new Coord(coord.getX(),coord.getY()+1));

                }
                else
                    direction = 1;
            }


        }

    }
     /*public void spreadIce(int direction) throws InterruptedException {
        //direction       1
        //                ^
        //             2<   >3
        //                4

        iceStack.push(new Coord(coord.getX(),coord.getY()));


        while(iceStack.size() < 380 && !iceStack.isEmpty()){
                Coord coord = (Coord) iceStack.pop();
            if(Game.maze[coord.getX()][coord.getY()] == ' '){
                Game.maze[coord.getX()][coord.getY()] = '+';
                Game.duration[coord.getX()][coord.getY()] = 10;
                Game.printMaze();
                Thread.sleep(2);
            }


            if(direction == 1){
                if(Game.maze[coord.getX()][coord.getY()-1] != '#')
                    iceStack.push(new Coord(coord.getX(),coord.getY()-1));
                if(Game.maze[coord.getX()][coord.getY()+1] != '#')
                    iceStack.push(new Coord(coord.getX(),coord.getY()+1));
                if(Game.maze[coord.getX()-1][coord.getY()] != '#'){
                    iceStack.push(new Coord(coord.getX()-1,coord.getY()));
                }

                    direction = 2;
            }
            else if(direction == 2){
                if(Game.maze[coord.getX()][coord.getY()-1] != '#')
                    iceStack.push(new Coord(coord.getX(),coord.getY()-1));
                if(Game.maze[coord.getX()+1][coord.getY()] != '#')
                    iceStack.push(new Coord(coord.getX()+1,coord.getY()));

                if(Game.maze[coord.getX()][coord.getY()-1] != '#'){
                    iceStack.push(new Coord(coord.getX()-1,coord.getY()));

                }

                    direction = 4;
            }
            else if(direction == 4){
                if(Game.maze[coord.getX()][coord.getY()-1] != '#')
                    iceStack.push(new Coord(coord.getX(),coord.getY()-1));
                if(Game.maze[coord.getX()+1][coord.getY()] != '#')
                    iceStack.push(new Coord(coord.getX()+1,coord.getY()));
                if(Game.maze[coord.getX()+1][coord.getY()] != '#'){
                    iceStack.push(new Coord(coord.getX()+1,coord.getY()));
                }

                    direction = 3;

            }
           else  if(direction == 3){

                if(Game.maze[coord.getX()][coord.getY()+1] != '#')
                    iceStack.push(new Coord(coord.getX(),coord.getY()+1));

                if(Game.maze[coord.getX()+1][coord.getY()] != '#')
                    iceStack.push(new Coord(coord.getX()+1,coord.getY()));
                if(Game.maze[coord.getX()][coord.getY()+1] != '#'){

                    iceStack.push(new Coord(coord.getX(),coord.getY()+1));
                }

                    direction = 1;
            }


        }

    }

*/

}