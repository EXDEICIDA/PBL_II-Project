import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import enigma.console.TextAttributes;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import java.util.LinkedList;

public class Game {
   public static enigma.console.Console cn = Enigma.getConsole("asd",100,26,20,0);
   public TextMouseListener tmlis;
   public KeyListener klis;

   // ------ Standard variables for mouse and keyboard ------
   public int mousepr;          // mouse pressed?
   public int mousex, mousey;   // mouse text coords.
   public int keypr;   // key pressed?
   public int rkey;    // key   (for press/release)
   // ----------------------------------------------------
   static ArrayList<Computer> computerList = new ArrayList<>();
   static Player player;
   static LinkedList<Player> playerList = new LinkedList<>();
   static ArrayList<Coord> treasureList = new ArrayList<>();
   Queue inputQueue = new Queue(10);
   static char[][] maze = new char[23][53];
   static int[][] duration = new int[23][53];
   int fireCount = 0;
   int s = 1;
   int m;
   int k;
   private static TextAttributes black = new TextAttributes(Color.white, Color.BLACK);
   private static TextAttributes color = black;
   Random random = new Random();
   int computerScore = 0;



   public void createMaze(){
      String fileName = "src\\maze.txt";

      try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
         String line;
         int row = 0;
         while ((line = br.readLine()) != null) {
            char[] chars = line.toCharArray();
            for (int col = 0; col < chars.length; col++) {
               maze[row][col] = chars[col];
            }
            row++;
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void createInputQueue(){
      while(!(inputQueue.isFull())){
         int prob = (int)(Math.random()*30+1);
         if(prob <= 5){inputQueue.enqueue('1');}
         else if(prob > 5 && prob <= 10){inputQueue.enqueue('2');}
         else if(prob > 10 && prob <= 15){inputQueue.enqueue('3');}
         else if(prob > 15 && prob <= 21){inputQueue.enqueue('-');}
         else if(prob > 21 && prob <= 27){inputQueue.enqueue('@');}
         else{inputQueue.enqueue('C');}
      }
   }


   public void createPlayerList(){
      String fileName = "src\\score.txt";

      try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
         String line;
         while ((line = br.readLine()) != null) {
               String data[] = line.split(",");
               Player p = new Player(data[0],Integer.parseInt(data[1]));
               playerList.add(p);

         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void printQueue(){
      //StatScreen
      cn.getTextWindow().setCursorPosition(56,0);
      cn.getTextWindow().output("Input");
      cn.getTextWindow().setCursorPosition(56,1);
      cn.getTextWindow().output("<<<<<<<<<<<<<<<<<<<");
      Queue temp = new Queue(10);

      int j = 56;
      while(!(inputQueue.isEmpty())) {
         cn.getTextWindow().setCursorPosition(j,2);
         cn.getTextWindow().output(inputQueue.Peek().toString());
         temp.enqueue(inputQueue.dequeue());

         j += 2;
      }
      inputQueue = temp;
      cn.getTextWindow().setCursorPosition(56,3);
      cn.getTextWindow().output("<<<<<<<<<<<<<<<<<<<");
   }

   public static void printMaze(){
      //Maze
      for (int i = 0; i < maze.length; i++) {
         for (int j = 0; j < maze[i].length; j++) {
            if(maze[i][j] == 'C'){
               color = new TextAttributes(Color.WHITE, Color.RED);
               cn.getTextWindow().output(j,i,maze[i][j],color);
               color = black;
            }
            else if(maze[i][j] == '@'){
               color = new TextAttributes(Color.white,Color.blue);
               cn.getTextWindow().output(j,i,maze[i][j],color);
               color = black;
            }
            else if(maze[i][j] == '-'){
               color = new TextAttributes(Color.red,Color.black);
               cn.getTextWindow().output(j,i,maze[i][j],color);
               color = black;
            }
            else if(maze[i][j] == '+'){
               color = new TextAttributes(Color.cyan,Color.black);
               cn.getTextWindow().output(j,i,maze[i][j],color);
               color = black;
            }
            else if(maze[i][j] == '1' ||maze[i][j] == '2'||maze[i][j] == '3' ){
               color = new TextAttributes(Color.yellow,Color.black);
               cn.getTextWindow().output(j,i,maze[i][j],color);
               color = black;
            }

            else{
               cn.getTextWindow().setCursorPosition(j,i);
               cn.getTextWindow().output(maze[i][j]);
            }

         }

      }
   }

   public void timer(){
      int end;
      // s is a variable that keeps seconds.

      if (s < 60) {
         if (s < 10) {
            cn.getTextWindow().setCursorPosition(56, 5);
            cn.getTextWindow().output("Time: 0" + s + "s");
            end = s;
         } else {
            cn.getTextWindow().setCursorPosition(56, 5);
            cn.getTextWindow().output("Time: " + s + "s");

            end = s;
         }
         s++;
      } else {
         m = s / 60;
         k = s % 60;  //  k is a variable that keeps seconds.
         if (k < 10) {
            cn.getTextWindow().setCursorPosition(56, 5);
            cn.getTextWindow().output("Time: " + m + "m 0" + k + "s");
            end = m * 60 + k;
         } else {
            cn.getTextWindow().setCursorPosition(56, 5);
            cn.getTextWindow().output("Time: " + m + "m" + k + "s");
            end = m * 60 + k;
         }
         s++;
      }
   }

   public Coord findCoord(){
      int x = (int)(Math.random()*23);
      int y = (int)(Math.random()*53);
      if(maze[x][y] == ' '){
         return new Coord(x,y);
      } else {
         while (maze[x][y] != ' ') {
            x = (int) (Math.random() * 23);
            y = (int) (Math.random() * 53);
         }
         return new Coord(x,y);
      }
   }

   public void insertElementIntoMaze(char c,Coord coord){
      maze[coord.getX()][coord.getY()] = c;
   }

   public void deleteIceAndFire(){

      for(int i = 0; i < maze.length; i++){
         for(int j = 0; j < maze[i].length; j++){
            if (maze[i][j] == '-' || maze[i][j] == '+'){
               if(duration[i][j] == 0){
                  maze[i][j] = ' ';
               }

            }
         }
      }
   }

   public void reduceDuration(){
      for(int i = 0; i < duration.length; i++)
         for(int j = 0; j < duration[i].length; j++){
            if(duration[i][j] > 0)
               duration[i][j] = duration[i][j] - 1 ;
         }
   }



   public void printStats(){
      cn.getTextWindow().setCursorPosition(56, 7);
      cn.getTextWindow().output("P.Score: " + player.getScore());
      cn.getTextWindow().setCursorPosition(56, 8);
      if(player.getLife() == 1000){cn.getTextWindow().output("P.Life: " + player.getLife());}
      else{cn.getTextWindow().output("P.Life: " + player.getLife()); cn.getTextWindow().setCursorPosition(67, 8);cn.getTextWindow().output(" ");}


      cn.getTextWindow().setCursorPosition(56, 9);
      cn.getTextWindow().output("P.Ice " + player.getIce());

      cn.getTextWindow().setCursorPosition(56, 11);
      cn.getTextWindow().output("C.Score: " + computerScore);

      cn.getTextWindow().setCursorPosition(56, 12);
      cn.getTextWindow().output("C.Robots: " + computerList.size());

   }


   public void computerMoves(){
      computerScore = 0;
      for(int i = 0; i < computerList.size(); i++){
         Computer temp = computerList.get(i);
         computerScore = temp.getScore();




         int x = temp.getCoord().getX();
         int y = temp.getCoord().getY();
         int random = (int)(Math.random()*4);


         if(random == 0 && maze[x+1][y] == ' '){maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x+1,y));}
         else if(random == 0 && maze[x+1][y] == '1'){temp.collect1();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x+1,y));}
         else if(random == 0 && maze[x+1][y] == '2'){temp.collect2();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x+1,y));}
         else if(random == 0 && maze[x+1][y] == '3'){temp.collect2();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x+1,y));}

         //upwards
         if(random == 1 &&maze[x-1][y] == ' ' ){maze[x][y] = ' ';maze[x-1][y] = 'C';temp.updateCoord(new Coord(x-1,y));}
         else if(random == 1 && maze[x-1][y] == '1'){temp.collect1();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x-1,y));}
         else if(random == 1 && maze[x-1][y] == '2'){temp.collect2();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x-1,y));}
         else if(random == 1 && maze[x-1][y] == '3'){temp.collect2();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x-1,y));}

         if(random == 2 && maze[x][y+1] == ' '){maze[x][y] = ' ';maze[x][y+1] = 'C';temp.updateCoord(new Coord(x,y+1));}
         else if(random == 2 && maze[x][y+1] == '1'){temp.collect1();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x,y+1));}
         else if(random == 2 && maze[x][y+1] == '2'){temp.collect2();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x,y+1));}
         else if(random == 2 && maze[x][y+1] == '3'){temp.collect2();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x,y+1));}

         if(random == 3 && maze[x][y - 1] == ' '){maze[x][y] = ' ';maze[x][y-1] = 'C';temp.updateCoord(new Coord(x,y-1));}
         else if(random == 3 && maze[x][y-1] == '1'){temp.collect1();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x,y-1));}
         else if(random == 3 && maze[x][y-1] == '2'){temp.collect2();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x,y-1));}
         else if(random == 3 && maze[x][y-1] == '3'){temp.collect2();maze[x][y] = ' ';maze[x+1][y] = 'C';temp.updateCoord(new Coord(x,y-1));}

      }




   }
   public void end(){
      if(!player.isAlive()){
         playerList.add(player);
         playerList.sort(Comparator.comparingInt(Player::getScore).reversed());
         String fileName = "src//score.txt"; // The file to write to


         try (FileWriter fw = new FileWriter(fileName, false)) {

         } catch (IOException e) {
            e.printStackTrace();
         }

         for(Player p : playerList){
            String content = p.getName() + "," + p.getScore();
            try (FileWriter fw = new FileWriter(fileName, true);
                 PrintWriter pw = new PrintWriter(fw)) {
               pw.println(content);
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         for(int i = 0; i < 80; i++){
            for(int y = 0; y < 60; y++){
               cn.getTextWindow().setCursorPosition(i, y);
               cn.getTextWindow().output(" ");
            }
         }
         for(int z = 0; z < playerList.size(); z++){
            cn.getTextWindow().setCursorPosition(0, z);
            cn.getTextWindow().output(playerList.get(z).getName() + " " +playerList.get(z).getScore());
         }



      }
   }
   public void playerName(){

      cn.getTextWindow().setCursorPosition(0, 0);
      cn.getTextWindow().output("Player name: " );
      Scanner scan = new Scanner(System.in);

      String name = scan.nextLine();
      player = new Player(name,0);
      cn.getTextWindow().setCursorPosition(55, 20);
      cn.getTextWindow().output(player.getName());

   }


   public void playerHarming(int px,int py){
      if(maze[py][px-1] == 'C'){player.getHarm('C'); end();}
      if(maze[py][px+1] == 'C'){player.getHarm('C'); end();}
      if(maze[py-1][px] == 'C'){player.getHarm('C'); end();}
      if(maze[py+1][px] == 'C'){player.getHarm('C'); end();}
      if(maze[py][px-1] == '-'){player.getHarm('-'); end();}
      if(maze[py][px+1] == '-'){player.getHarm('-'); end();}
      if(maze[py-1][px] == '-'){player.getHarm('-'); end();}
      if(maze[py+1][px] == '-'){player.getHarm('-'); end();}

   }

   public void computerHarming(){
      if(!computerList.isEmpty()){
         for(int i = 0; i < computerList.size(); i++){
            Computer c = computerList.get(i);
            Coord coord = c.getCoord();
            if(maze[coord.getX()+1][coord.getY()] == '+'){c.getHarm();}
            if(maze[coord.getX()-1][coord.getY()] == '+'){c.getHarm();}
            if(maze[coord.getX()][coord.getY()+1] == '+'){c.getHarm();}
            if(maze[coord.getX()][coord.getY()-1] == '+'){c.getHarm();}

            if(c.getLife() == 0){
               computerList.remove(c);
               maze[coord.getX()][coord.getY()] = ' ';
               player.score += 50;
            }


         }
      }

   }


   //rapor
   //sunum
   Game() throws Exception {
      playerName();
      createPlayerList();
      createMaze();
      createInputQueue();
      // ------ Standard code for mouse and keyboard ------ Do not change
      tmlis=new TextMouseListener() {
         public void mouseClicked(TextMouseEvent arg0) {}
         public void mousePressed(TextMouseEvent arg0) {
            if(mousepr==0) {
               mousepr=1;
               mousex=arg0.getX();
               mousey=arg0.getY();
            }
         }
         public void mouseReleased(TextMouseEvent arg0) {}
      };
      cn.getTextWindow().addTextMouseListener(tmlis);

      klis=new KeyListener() {
         public void keyTyped(KeyEvent e) {}
         public void keyPressed(KeyEvent e) {
            if(keypr==0) {
               keypr=1;
               rkey=e.getKeyCode();
            }
         }
         public void keyReleased(KeyEvent e) {}
      };
      cn.getTextWindow().addKeyListener(klis);
      // ----------------------------------------------------

      int px=5,py=5;
      maze[py][px] = 'P';

      int direction = -1;
      int time = 0;
      while(true) {

         printMaze();
         printQueue();
         printStats();
         playerHarming(px,py);
         if(player.getLife() <= 0){
            break;
         }
         computerHarming();
         if(keypr==1) {// if keyboard button pressed
            int previousX = px;
            int previousY = py;
            if(rkey==KeyEvent.VK_LEFT && maze[py][px-1] == ' ') {px--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze(); direction = 2;}
            else if(rkey==KeyEvent.VK_LEFT && maze[py][px-1] == '1') {px--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze(); player.collect1();direction = 2;}
            else if(rkey==KeyEvent.VK_LEFT && maze[py][px-1] == '2') {px--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze(); player.collect2();direction = 2;}
            else if(rkey==KeyEvent.VK_LEFT && maze[py][px-1] == '3') {px--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze(); player.collect3();direction = 2;}
            else if(rkey==KeyEvent.VK_LEFT && maze[py][px-1] == '@') {px--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze(); player.collectIce();direction = 2;}

            if(rkey==KeyEvent.VK_RIGHT && maze[py][px+1] == ' ') {px++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();direction = 3;}
            else if(rkey==KeyEvent.VK_RIGHT && maze[py][px+1] == '1') {px++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze(); player.collect1();direction = 3;}
            else if(rkey==KeyEvent.VK_RIGHT && maze[py][px+1] == '2') {px++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze(); player.collect2();direction = 3;}
            else if(rkey==KeyEvent.VK_RIGHT && maze[py][px+1] == '3') {px++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze(); player.collect3();direction = 3;}
            else if(rkey==KeyEvent.VK_RIGHT && maze[py][px+1] == '@') {px++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze(); player.collectIce();direction = 3;}

            if(rkey==KeyEvent.VK_UP && maze[py-1][px] == ' ') {py--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();direction = 1;}
            else if(rkey==KeyEvent.VK_UP && maze[py-1][px] == '1') {py--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();player.collect1();direction = 1;}
            else if(rkey==KeyEvent.VK_UP && maze[py-1][px] == '2') {py--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();player.collect2();direction = 1;}
            else if(rkey==KeyEvent.VK_UP && maze[py-1][px] == '3') {py--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();player.collect3();direction = 1;}
            else if(rkey==KeyEvent.VK_UP && maze[py-1][px] == '@') {py--; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();player.collectIce();direction = 1;}

            if(rkey==KeyEvent.VK_DOWN && maze[py+1][px] == ' ') {py++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();direction = 4;}
            else if(rkey==KeyEvent.VK_DOWN && maze[py+1][px] == '1') {py++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();player.collect1();direction = 4;}
            else if(rkey==KeyEvent.VK_DOWN && maze[py+1][px] == '2') {py++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();player.collect2();direction = 4;}
            else if(rkey==KeyEvent.VK_DOWN && maze[py+1][px] == '3') {py++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();player.collect3();direction = 4;}
            else if(rkey==KeyEvent.VK_DOWN && maze[py+1][px] == '@') {py++; maze[py][px] = 'P'; maze[previousY][previousX] =' '; printMaze();player.collectIce();direction = 4;}




            char rckey=(char)rkey;

            /*if(rkey==KeyEvent.VK_SPACE && player.getIce() > 0) {

               //direction       1
               //                ^
               //             2<   >3
               //                4

            }*/
               if(rkey==KeyEvent.VK_SPACE && player.ice > 0 &&  maze[py-1][px] == ' ' && direction == 1) {
                  Ice ice = new Ice(new Coord(py-1,px));
                  ice.spreadIce(1);
                  player.ice--;
               }
               if(rkey==KeyEvent.VK_SPACE && player.ice > 0&& maze[py+1][px] == ' ' && direction == 4) {
                  Ice ice = new Ice(new Coord(py+1,px));
                  ice.spreadIce(4);
                  player.ice--;
               }
               if(rkey==KeyEvent.VK_SPACE && player.ice > 0&& maze[py][px-1] == ' ' && direction == 2) {
                  Ice ice = new Ice(new Coord(py,px-1));
                  ice.spreadIce(2);
                  player.ice--;
               }
               if(rkey==KeyEvent.VK_SPACE && player.ice > 0&&maze[py][px+1] == ' ' && direction == 3) {
                  Ice ice = new Ice(new Coord(py,px+1));
                  ice.spreadIce(3);
                  player.ice--;
               }



            keypr=0;    // last action
         }
         if(time % 10 == 0){
            reduceDuration();
            timer();
         }
         if(time % 4 == 0){
            computerMoves();
            printMaze();
         }
         if(time % 20 == 0){
            Coord coord = findCoord();
            char temp = inputQueue.dequeue().toString().charAt(0);
            if(temp == '-'){
               Fire fire = new Fire(coord,maze);
               fire.createFireQueue();
               printMaze();
            }

            else if(temp == 'C'){
               Computer computer = new Computer(coord, new Coord(0,0));
               computerList.add(computer);
            }
            if(temp == '1' && temp == '2' && temp == '3'){
               treasureList.add(coord);
            }

            insertElementIntoMaze(temp,coord);

            createInputQueue();
         }
         if(time % 2 == 0){
            deleteIceAndFire();
         }

         time++;
         Thread.sleep(100);
      }




      }
   }

