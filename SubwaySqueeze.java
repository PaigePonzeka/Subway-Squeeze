import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.util.StringTokenizer;

public class SubwaySqueeze extends JApplet implements  KeyListener, MouseListener
{

     public static Dimension pieces=new Dimension(50,50);//pieces store the size of individual pieces
     public static Dimension boardSize=new Dimension(450,500);//boardsize stores the complete size of the gixen applet
     public static final String SEAT="images/empty.gif";//stores the image location of empty seats (a blank gif)
     public static final String LIVES="images/lives.gif";//Stores the Lives text and outline
     public static final String HEART="images/heart.gif";//Stores the individual hearts to represent lives
     public static final String UNLOAD="images/unload.gif";//stores the unload text
     public static final String LOAD="images/loading.gif";//stores the loading text
     public static final String GAMEOVER="images/gameover.gif";//Stores the Game Over Screen
     public static final String LOST="images/lost.gif";//Stores the -1 symbol for misplayed passengers in unloading
     public static final String THUMBS="images/success.gif";//Stores thumbs up for correctly played passengers in loading
     public static String TimeClock1="images/nums/red/3.gif";//initial value of timer (first digit)
     public static String TimeClock2="images/nums/red/0.gif";//intial value of timer(second digit)
     public static String there="";//Stores the String Location of a found position
     public static String lives[]=new String[5];//Lives Stores a String Array of hearts ro represent lives
     public static final int PIXEL_DISTANCE=50; //Full Array of entire board the stores the String Name of every character
     public static String setup[][]=new String[9][10];//Setup for the entire board stores the String Location of items on the board
     public static String timer[][]=new String[2][31];//2D array that stores the image locations of numbers
         //Timers the String Locations of the digits for the load and unload screen
     public static boolean gameOver=false;//boolean that stores whether the game has ended or not
     public static int clicks=0;//Stores the number of clicks from the user
     public static int xLocation=10;//stores the x location within the applet
     public static int yLocation=10;//stores the y location within the applet
     public static int state=0; //State represents the load or unloading state of the level
            /*if state is even we are unloading
             * if state is odd we are loading*/
     public int numOfPass;//based off random Int that stores the number of passengers for a given stop
     public int types[];//types is an int array that stores the character type one, two or three
     public int stops[];//Stops is an int array that stores the stops for each character
     public String passImage[];//passImage is an array that stores the the image location of each passenger
     public String passImageWOut[];//passImageWOut is an array that stores the image of each passenger without .gif or images/
     public int currentStop=0;//Stores the currentStop location of the Train Board
            //used to display the correct map
     public int life=5;//intial starting value of lives for the user
     public boolean CharacterClicked=false;//determines whether or not a chracter has been clicked and whether to print click.gif at location
     public static int seconds=30;//The seconds timer that is used in the action preformed timer
     public boolean start=false;//boolean that stores whether or not the user starts the game
     public String outsideDoor;
     public Image loadBack = (new ImageIcon(getClass().getResource("images/loadcount.gif"))).getImage();//background for loading/unloading counter
     public Image livesBack= (new ImageIcon(getClass().getResource("images/lives.gif"))).getImage();//Background for lives;
     public Image PassOff;
     public JPanel r;//JPanel that stores the entire game
     public int allStops=0;//For Score sets all the stops the user has done through
     public String ScoreNum1;//sets the first digit's string location
     public String ScoreNum2;//sets the second digit's string location
     public int levelDifficulty=4;
     public int minNumPass=1;
     ActionListener actionListener = new ActionListener()
     {
         //action lister connected to timer that counts down ever second
          public void actionPerformed(ActionEvent actionEvent)
          {
          // timer executes what is in here.
             seconds--;
             TimeClock1="images/nums/red/"+(seconds/10)+".gif";//sets the first digit's string location
             repaint(25,345,25,50);
             TimeClock2="images/nums/red/"+(seconds%10)+".gif";//sets the second digit's string location
             repaint(50,345,25,50);
             //to repaint the subway board, repaint from 100,0,350,500
             //repaint(100,0,350,500);
             if (seconds==0)
             { //when seconds changes to 0 change the state
               stateChange();    
             }
          }
      };
      public Timer time=new Timer(1000, actionListener);// creates a timer that does something every 1 sec 
/*
 * 1=red
 * 2=blue
 * 3=green
 * 4=yellow
 * 5=blue
 * 6=orange
 * */
      @Override
    public void init()
    {
        setSetup();//set the default images on the board as empty.gif
        setStopsSetup();//stores default value of zero on the stops array
        setLives();//sets the number of lives to 5 on the string array    
    }//End of Init Method
@Override
    public void start()
    {
       // requestFocusInWindow();
        state=0;
        r=new BackgroundPanel("images/start.gif");//creates the JPanel to store the default car string image value
        setSize(boardSize);//sets the board size
        r.setDoubleBuffered(false);
        r.setFocusable(true);
        addKeyListener(this);
        addMouseListener (this);
        add(r);//add the JPAnel to the applet       
        requestFocus();
        

    }//End of Start Method
    public void mouseClicked (MouseEvent e){}//end of mouseClicked Method
    public void mouseEntered(MouseEvent e){}//End of mouseEntered Method
    public void mouseExited(MouseEvent e){}//end of mouseExited Method
    public void mouseReleased(MouseEvent e){}//end of mouseReleased Method
    public void mousePressed(MouseEvent e)
    {
       setup[1][4]=SEAT;//changes this location to an empty seat
       repaint(50,200,50,50);
            //used to change the value if user gets success or a lose life icon during unloading
        if(start==false)
        {//changes the start value if the user clicks and the game hasn't started yet          
           // timed.start();
            start=true;
            r.addKeyListener(this);
            repaint();
        }
        else if (CharacterClicked==true)
        {
            int prevX=xLocation;
            int prevY=yLocation;
            int clickX=e.getX();//gets the x pixel location of what the user clicked
            int clickY=e.getY();//gets the y pixel location of what the user clicked
            xLocation=(clickX/50);//divides by 50 to get the array values
            yLocation=(clickY/50);//divides by 50 to get the array values
            if(((xLocation>=2)&&(xLocation<=8))&&((yLocation>=0)&&(yLocation<=9)))
            {
               there=SubwaySqueeze.setup[xLocation][yLocation];
               //if there is located on the board get the String name store it as there
                    if (!there.equals(SubwaySqueeze.SEAT))//if there is NOT a seat or empty.gif
                    {
                    CharacterClicked=true;//a character has been clicked set it to true
                    repaint((xLocation*50), (yLocation*50), 50,50);//repaint to show the image
                    repaint((prevX*50),(prevY*50),50,50);
                    }
            }
            else
            {
                xLocation=prevX;
                yLocation=prevY;
            }
        }
        else
        {
            int clickX=e.getX();//gets the x pixel location of what the user clicked
            int clickY=e.getY();//gets the y pixel location of what the user clicked
            xLocation=(clickX/50);//divides by 50 to get the array values
            yLocation=(clickY/50);//divides by 50 to get the array values
            if(((xLocation>=2)&&(xLocation<=8))&&((yLocation>=0)&&(yLocation<=9)))
            {
               there=SubwaySqueeze.setup[xLocation][yLocation];
               //if there is located on the board get the String name store it as there
            }
            if (!there.equals(SubwaySqueeze.SEAT))//if there is NOT a seat or empty.gif
            {
                CharacterClicked=true;//a character has been clicked set it to true
                repaint((xLocation*50), (yLocation*50), 50,50);//repaint to show the image
            }
        }
    }


    public void setSetup()
        //Intializes setUp Array to store empty seats
       //The Playable Board is from (0,2)-->(0,6) & (9,2)-->(9,6) in The setupArray
    {
        for(int i=0;i<9;i++)
        {
           if(i<3)
           {
                for(int j=4; j<=5;j++)
                {
                    setup[i][j]=SEAT;
                }
           }
               for(int j=0; j<10;j++)
                {

                    setup[i][j]=SEAT;
                }
        }
    }//End of setSetup Method
    public void setLives()
        //Initializes Lives Array with 5 hearts
    {
        for(int i=0;i<5;i++)
            {
                lives[i]=HEART;
            }
    }//end of setLives Method
    public void setPassengers()
       //Create a Method that sets the number of passengers and a loading stop using random
    {
        numOfPass=(1+(int)(Math.random()*9));
    }
    public void setStops()
      //Generates a Random int Array between 1-12 for stops for each character
    {
        stops=new int[numOfPass];
        for(int x=0;x<numOfPass;x++)
        {
            int randomNum=(1+(int)(Math.random()*6));
            stops[x]=randomNum;
        }
    }//End of setStops Method
    public void setPassImage()
      //Pass Image is a method that sets the location String for the random passengers
    {
        passImage=new String[numOfPass];
        passImageWOut=new String[numOfPass];
       for(int x=0;x<numOfPass;x++)
       {
          passImageWOut[x]=""+stops[x];
          passImage[x]="images/characters/"+stops[x]+".gif";
       }
    }//End of setPassImage Method
    public void insertPass()
      //Creates a Method that inserts passengers into the setUp Array at the given spaces
    {
       for(int x=0;x<numOfPass;x++)
       {
           setup[8][(x)]="images/characters/"+passImageWOut[x]+".gif";
       }
       repaint(400,0,50,500);//repaint the passengers platform
       repaint(350,0,50,500);//repaint the platform next to the characters for loading scene
       //repaint(x-coordinate, ycoordinate,width, height);
    }//End of insertPass Method
    public void setStopsSetup()
     //Intializes stopsSetup Array to store a default value of zero
    {
       //The Playable Board is from (0,2)-->(0,6) & (9,2)-->(9,6) in The setupArray
        for(int i=2;i<9;i++)
        {
                for(int j=0; j<10;j++)
                {
                }
        }
     }//end of setStopsSetup
    public void keyReleased(KeyEvent e){}//end of KeyReleased Method
    public void keyTyped(KeyEvent e){}//end of the keyTyped Method
    public void keyPressed(KeyEvent e )
    {
     //Move Piece Up (-Y)
        if((e.getKeyChar() == 'w')&&(!setup[xLocation][yLocation].equals(SEAT))
                &&(yLocation-1>=0)&&(setup[xLocation][yLocation-1].equals(SEAT)))
        {
            ChangeLocationW();
        }
        //Move Piece Left (-X)
        else if((e.getKeyChar()== 'a')&&(!setup[xLocation][yLocation].equals(SEAT))
                &&(xLocation-1>=0)&&((setup[xLocation-1][yLocation].equals(SEAT))
                ||((setup[xLocation-1][yLocation].equals(LOST)))||((setup[xLocation-1][yLocation].equals(THUMBS)))))
        {
              //Check Characters stop if stop does not equal stop then you lose a life and display lose life image
             //if It is stop display thumbs up and get right of old character
            //if state%2=1 we are loading and allow users to pass from xLocation 7
           //if State%2=0 We are Unloading and allow users to pass from xLocation2
            if(state%2==0)//if the state is unloading
            {
                if(xLocation==2)//if user is in the doorwar
                {
                    if((yLocation==4))//if user moves pass the exit door
                    {
                       // System.out.println("currentStop: " +currentStop);
                       // System.out.println("setup :"+setup[xLocation][yLocation]);
                        ChangeLocationA();
                        String lastToken="";
                        String m;
                        int nowStop;
                        StringTokenizer Gif = new StringTokenizer(setup[xLocation][yLocation]);
                        while (Gif.hasMoreTokens()) {
                         lastToken=Gif.nextToken("/");
                        }
                         StringTokenizer f = new StringTokenizer(lastToken);
                         m=f.nextToken(".");
                         nowStop=Integer.parseInt(m);
                        if (nowStop!=currentStop)//if the passenger gets off at the wrong stop
                        {
                            life-=1;//the passenger loses a life
                            if (life==0)
                            {
                                repaint();
                            }
                            setup[xLocation--][yLocation]=LOST;//change the location to a lose life symbol
                            //repaint(50,200,50,50);//Print the area right outside the door for unloading feedback
                            repaint(0,250,100,50);//repaint players lives area
                            Exit();
                        }
                    else
                        {
                            setup[xLocation--][yLocation]=THUMBS;//change the location to a thumns up symbol
                            
                            Exit();
                        }
                        repaint(50,200,50,50);//repaint the area right outside the door for unloading feedback
                    }
                }
            else if(xLocation==7) {}
            else
                {
                    ChangeLocationA();
                }
            }//end of state%2=0 if statement
            else if(state%2==1)//if the state is loading
            {
                if(xLocation==7)//if user it outside the door platform
                {
                    if(yLocation==4)//if user passes through the entrance doorway
                    {
                       ChangeLocationA();
                    }
                }
                else if(xLocation==2){}//don't allow user to pass through exit door
                else
                {
                    ChangeLocationA();
                }
            }
            else
            {
                ChangeLocationA();
            }
        }//end of A key location
        //Move Piece Down (+Y)
        else if((e.getKeyChar()=='s')&&(!setup[xLocation][yLocation].equals(SEAT))
                &&(yLocation+1<=9)&&(setup[xLocation][yLocation+1].equals(SEAT)))
        {
           ChangeLocationS();
        }//end of S key location
        //Move Piece Down (+X)
        else if((e.getKeyChar()=='d')&&(!setup[xLocation][yLocation].equals(SEAT))
                &&(xLocation+1>2)&&(xLocation+1<7)&&(setup[xLocation+1][yLocation].equals(SEAT)))
        {
           ChangeLocationD();
        }//end of D key location
         else if((e.getKeyChar()==' ')&&(state==0))
        {
             if(currentStop==0)
             {
                 repaint();
             }
            stateChange();
        }//end of   key location and state =0
        else if((e.getKeyChar()==' ')&&(state>0))
        {
            /* Allows the users to his the SPACEBAR through the stops*/
           stateChange();
        }//end of   key location and state greater and 0
    }//End of keyPressed Method
    public void ChangeLocationA()
     //Changes the location of the A key
    {
         String Temp=setup[xLocation][yLocation];
         setup[xLocation][yLocation]=SEAT;
         repaint((xLocation*50),(yLocation*50),50,50);
         xLocation-=1;
         setup[xLocation][yLocation]=Temp;
         repaint((xLocation*50),(yLocation*50),50,50);
     }
    public void ChangeLocationS()
    //Changes the location of the S key
    {
            String Temp=setup[xLocation][yLocation];
            setup[xLocation][yLocation]=SEAT;
            repaint((xLocation*50),(yLocation*50),50,50);
            yLocation+=1;
            setup[xLocation][yLocation]=Temp;
            repaint((xLocation*50),(yLocation*50),50,50);
     }
    public void ChangeLocationD()
    //Changes the location of the D key
    {
            String Temp=setup[xLocation][yLocation];
            setup[xLocation][yLocation]=SEAT;
            repaint((xLocation*50),(yLocation*50),50,50);
            xLocation+=1;
            setup[xLocation][yLocation]=Temp;
            repaint((xLocation*50),(yLocation*50),50,50);
     }
    public void ChangeLocationW()
     //Changes the location of the W key
    {
        String Temp=setup[xLocation][yLocation];
        setup[xLocation][yLocation]=SEAT;
        repaint((xLocation*50),(yLocation*50),50,50);
        yLocation=yLocation-1;
        setup[xLocation][yLocation]=Temp;
        repaint(100,0,350,500);
     }//end of ChangeLocation
    public void CheckPlatform()
    //Checks The Right Side platforms to see if any passengers are left
    {
        /* if there are any left take away a life, and clear the passengers for the
        * next set of lives*/
        for (int y=0;y<10;y++)
        {
            if((!setup[8][y].equals(SEAT))||(!setup[7][y].equals(SEAT)))
            {
              life--;
              repaint();
              if(life<0)
              {
                  life=0;
                  repaint();
              }
              else if (life==0)
              {
                  repaint();
              }
              setup[8][y]=SEAT;
              setup[7][y]=SEAT;
            }
        }
    }
    public void stateChange()
    //changes the state (loading or unloading)
     {
        if (state>0)
            seconds=30;//Restart the Timer by resetting the seconds
        else if(state==0)
            time.start();//intialize the timer
        state++;
           //If State%2=1 then We are Unloading
            //doors are open and allow user to Exit Train
            if(state%2==0)
            {
                allStops++;
                if((allStops > 4) && (allStops< 18)){
                levelDifficulty= (int)(allStops/2);
                }//endif
                //if we reach twenty stops this begins to increment the initial number
                //but decreases the seed number to mathramdon
                if((allStops>19) && (allStops%10==0) &&(minNumPass<10))
                {
                   minNumPass=(int)(allStops/10);
                   if((allStops%20==0) &&(levelDifficulty>0)){
                   levelDifficulty= levelDifficulty-(allStops/10);
                   }
                }
              //  System.out.println(levelDifficulty);
             //   System.out.println(minNumPass);
             //           System.out.println(allStops);

              CheckPlatform();
               currentStop++;

                if (currentStop>6)
                {
                    currentStop=1;
                }
               repaint();
            }
            else
            {
                setPassengers();
                repaint();
                setStops ();
                repaint();
                repaint();
                setPassImage();
                repaint();
                insertPass();
                repaint();
            }
    }
    public void Exit()
    { 
        outsideDoor=setup[1][4];
              PassOff = (new ImageIcon(getClass().getResource(outsideDoor))).getImage();
              //Prints a success image or a loss life image if player moves a pass location
              repaint(50,200,50,50);
    }//end of Exit Method
    @Override
    public void update(Graphics g)
    {
        paint(g);
    }
    public void paint(Graphics g)
    {
      // super.paint(g);
        if (start==true)
        {
             Image Platform=(new ImageIcon(getClass().getResource("images/platforms/"+currentStop+(state%2)+".gif"))).getImage();
             g.drawImage(Platform, 0,0, this);
             int dy=0;
             int xx=0;
              g.drawImage(livesBack, 0,250, this);//paints lives backgrounds
              g.drawImage(loadBack, 0,300, this);//paints loading backgrounds
              g.drawImage(PassOff, 50,200, this);
              Image scoreBoard = (new ImageIcon(getClass().getResource("images/score.gif"))).getImage();
              g.drawImage(scoreBoard, 0,400, this);
              
            for(int i=0;i<10;i++)
            {
                int dx=100;
                for(int j=2; j<9;j++)
                {
                    String location=setup[j][i];
                    Image image = (new ImageIcon(getClass().getResource(location))).getImage();
                     g.drawImage(image, dx,dy, this);
                     //where[j][i]=new Point(dx,dy);
                    dx+=PIXEL_DISTANCE;
                }
                dy+=PIXEL_DISTANCE;
            }
            for(int i=0;i<life;i++)
            {
                String location=lives[i];
                Image image = (new ImageIcon(getClass().getResource(location))).getImage();
                g.drawImage(image, xx,279, this);
                xx+=20;
            }
             //Determines whether to display loading or unload for the State
             if(state%2==0)
             {
              Image img = (new ImageIcon(getClass().getResource(UNLOAD))).getImage();
                     g.drawImage(img, 4,304, this);
             }
             else if(state%2==1)
             {
               Image img = (new ImageIcon(getClass().getResource(LOAD))).getImage();
                    g.drawImage(img, 4,304, this);
             }
            String stopMap="images/maps/"+currentStop+".gif";
            Image map=(new ImageIcon(getClass().getResource(stopMap))).getImage();
            g.drawImage(map, 0,0, this);
        
           Image firstNum=(new ImageIcon(getClass().getResource(TimeClock1))).getImage();
           g.drawImage(firstNum, 25,345, this);
           Image secondNum=(new ImageIcon(getClass().getResource(TimeClock2))).getImage();
           g.drawImage(secondNum, 50,345, this);
           Image Clicked=((new ImageIcon(getClass().getResource("images/clicked.gif"))).getImage());
           g.drawImage(Clicked, (xLocation*50), (yLocation*50), this);
           ScoreNum1="images/nums/gold/"+(allStops/10)+".gif";//sets the first score digit's string location
           ScoreNum2="images/nums/gold/"+(allStops%10)+".gif";//sets the second score digit's string location
           Image scoreDigit1 = (new ImageIcon(getClass().getResource(ScoreNum1))).getImage();
           g.drawImage(scoreDigit1, 25,450, this);
           Image scoreDigit2 = (new ImageIcon(getClass().getResource(ScoreNum2))).getImage();
           g.drawImage(scoreDigit2, 50,450, this);
           if(life==0)
            {
            Image over=(new ImageIcon(getClass().getResource(GAMEOVER))).getImage();
                g.drawImage(over, 0,0, this);
                time.stop();
                //inset code to ask user to reset or wait a moment then restart the app
            }
        }
        else
        {
           Image Start=(new ImageIcon(getClass().getResource("images/start.gif"))).getImage();
             g.drawImage(Start, 0,0, this);  
        }
        
    }
    public void stop()
    {

    }
}//End of SubwaySqueeze Class
class BackgroundPanel extends JPanel
  //Creates a JPanel with an image background w/ The STring Location cast in the contructor
{
    Image background;
    public BackgroundPanel(String location)
    {
        try
        {
            background = (new ImageIcon(getClass().getResource(location))).getImage();
        }
        catch(Exception e){/*handled in paintComponent()*/}
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(background != null) g.drawImage(background, (this.getWidth()/2) -
                (background.getWidth(this) / 2),(this.getHeight()/2) -
                (background.getHeight(this) / 2),
                background.getWidth(this),background.getHeight(this),this);
    }
}//end of BackgroundPanel Class
