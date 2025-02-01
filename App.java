import javax.swing.*; //import javax.swing package  to use JFrame class
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 750;
        int boardHeight = 250;

        JFrame frame = new JFrame("Chrome Dinosaur");  //create a window
        frame.setSize(boardWidth, boardHeight);  //set window size
        frame.setLocationRelativeTo(null);  //place window at the centre
        frame.setResizable(false); //disable window resizing    
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit on close
        frame.setVisible(true); //set window visible

        ChromeDinosaur chromeDinosaur = new ChromeDinosaur(); //create a ChromeDinosaur object
        frame.add(chromeDinosaur); //add ChromeDinosaur object to the window
        frame.pack(); //pack the window
        chromeDinosaur.requestFocus();
        frame.setVisible(true); //set window visible
    }
}
