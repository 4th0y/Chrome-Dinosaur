import java.awt.*;  // Graphics, Color, Font, Image
import java.awt.event.*;  // ActionListener, ActionEvent
import java.util.ArrayList;  // ArrayList
import javax.swing.*; // JFrame, JPanel, JLabel, ImageIcon, Timer

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener {  
    int boardWidth = 750;
    int boardHeight = 250;

    //images
    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;

    class Block{
        int x, y, width, height;
        Image img;
        Block(int x, int y, int width, int height, Image img){  //constructor
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }

    }

    //dinosaur

    int dinosaurWidth = 88, dinosaurHeight = 94, dinosaurX = 50, dinosaurY = boardHeight - dinosaurHeight;

    Block Dinosaur; //creates image of dinosaur

    //cactus
    int cactus1Width = 34, cactus2Width = 69, cactus3Width = 102;

    int cactusHeight = 70, cactusX = 700, cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray;  //creates an array of cactus

    //physics
    int velocityX = -12, velocityY = 0, gravity = 1;

    boolean gameOver = false;
    int score = 0;
    
    //timer
    Timer gameLoop;
    Timer placeCactusTimer;


    public ChromeDinosaur() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true); //set focus to the panel
        addKeyListener(this);  //listens to key press

        dinosaurImg = new ImageIcon(getClass().getResource("./dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./dino-jump.png")).getImage();

        cactus1Img = new ImageIcon(getClass().getResource("./cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./cactus3.png")).getImage();
        
        //dinosaur
        Dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);

        //cactus
        cactusArray = new ArrayList<Block>();

        //gameTimer
        gameLoop = new Timer(1000/60, this); //60 times per second
        gameLoop.start();

        //place cactus timer
        placeCactusTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }

        });
        placeCactusTimer.start();
    }

    void placeCactus(){
        if(gameOver){
            return;    
        }
        
        double placeCactusChance = Math.random();  //0 to 0.99999999
        if(placeCactusChance > .90){
            Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance > 0.70){
            Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img);
            cactusArray.add(cactus);
        }
        else{
            Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img);
            cactusArray.add(cactus);
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //dinosaur
        g.drawImage(Dinosaur.img, Dinosaur.x, Dinosaur.y, Dinosaur.width, Dinosaur.height, null );
        
        //cactus
        for(int i = 0; i < cactusArray.size(); i++ ){
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }

        //scoreboard
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Gmae Over: " + String.valueOf(score), 10, 35);
        }
        else{
            g.drawString(String.valueOf(score), 10, 35);
        }
    }


    public void move(){
        //dinosaur
        velocityY += gravity;
        Dinosaur.y += velocityY;

        if(Dinosaur.y > dinosaurY){
            Dinosaur.y = dinosaurY;
            velocityY = 0;
            Dinosaur.img = dinosaurImg;
        }

        //cactus
        for(int i = 0; i < cactusArray.size(); i++){
            Block cactus = cactusArray.get(i);
            cactus.x += velocityX;
            
            if(collision(Dinosaur, cactus)){
                gameOver = true;   //stop the game
                Dinosaur.img = dinosaurDeadImg;
            }
        }
        //score
        score++;
    }


    boolean collision(Block a, Block b){
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            //System.out.println("Jump!");
            if(Dinosaur.y == dinosaurY){
                velocityY = -17;
                Dinosaur.img = dinosaurJumpImg;
            }

            if(gameOver){
                //restart game by resetting conditions
                Dinosaur.y = dinosaurY;
                Dinosaur.img = dinosaurImg;
                velocityY = 0;
                cactusArray.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeCactusTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placeCactusTimer.stop();
            gameLoop.stop();
        }
    }

   
}
