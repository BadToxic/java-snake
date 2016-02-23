import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Snake extends Thread implements KeyListener{

	private Frame f;
    private TextArea ta;
    private char[][] field;
    private final int WIDTH = 50;
    private final int HEIGHT = 20;
    private SnakePart player;
    private SnakePart food;
    private int foodCounter;
    private final int FOOD_COUNTER_MAX=12;
    private int direction = 0;
    private final int RIGHT=0, UP=1, LEFT=2, DOWN=3;
    private final char KEY_RIGHT='d', KEY_UP='w', KEY_LEFT='a', KEY_DOWN='s';
    private final char EMPTY_FIELD='#', SNAKE='O', FOOD='*';
    
    public Snake(String str) {
    	super(str);
        f = new Frame("Snake");
        ta = new TextArea("");
        field = new char[HEIGHT][WIDTH];
        fieldReset();
        player = new SnakePart(3, HEIGHT/2);
        player.setNext(new SnakePart(player.getX()-1, player.getY()));
        player.getNext().setNext(new SnakePart(player.getNext().getX()-1, player.getNext().getY()));
        player.getNext().getNext().setNext(new SnakePart(player.getNext().getNext().getX()-1, player.getNext().getNext().getY()));
        food = null;
        foodCounter=3;
    }
    
    // Erstellt Fenster und alles noetige
    public void launchFrame() {
        // Hinzufügen der Komponenten zum Frame
        f.add(ta, BorderLayout.CENTER);
        
        // Hinzufuegen der Listener
        ta.addKeyListener(this);	// Damit Tastendruck bemerkt wird
        f.addWindowListener( new WindowAdapter(){	// Damit der Schliessen Button geht
            @Override
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        } );

        ta.setEditable(false);	// Damit man nicht ins Spielfeld schreiben kann
        ta.requestFocus();
        ta.setFont(new Font("Monospaced", Font.PLAIN, 20)); // Damit alle Zeichen gleich gross sind

        f.setSize(640, 525);
        
        f.setVisible(true);
        
        run();	// Startet Thread
        
        for(int i=0; i<HEIGHT; i++)
        	for(int j=0; j<WIDTH; j++)
        		field[i][j]='o';
    }

	public static void main(String[] args) {
		Snake game = new Snake("Snake");
		game.launchFrame();
	}
	
	public void run() {
        while(true){
        	try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	fieldReset();
        	movePlayer();
        	drawPlayer();
        	handleFood();
        	
        	ta.setText(field2String());
        }
    }

    /** Erstellt neues Fressen, wenn keins existiert.
	 *  Dazu wird erst gewartet bis der Counter abgelaufen ist.
	 *  Danach wird der Counter zurueckgesetzt.
	 *  Existiert das Fressen, wird dieses auch gezeichnet.
	 */
	private void handleFood(){
		if(food == null){
    		foodCounter--;
    		if(foodCounter<=0){
    			food = new SnakePart(-1, -1);
    			do{
    				food.setX((int)(Math.random()*WIDTH));
    				food.setY((int)(Math.random()*HEIGHT));
    			}while(field[food.getY()][food.getX()] != EMPTY_FIELD);
    			
    			foodCounter = FOOD_COUNTER_MAX;
    		}
    	}
		if(food != null) field[food.getY()][food.getX()] = FOOD;
	}
	
	// Das passiert wenn Spieler verloren hat
	private void dead(){
		
	}
	
	private void movePlayer(){	// Bewegt den Spieler
		int x_tmp = player.getX();
		int y_tmp = player.getY();
		
		if(direction == RIGHT){
			if(player.getX() < WIDTH-1) player.setX(player.getX()+1);
			else dead();
		}
		else if(direction == UP){
			if(player.getY() > 0) player.setY(player.getY()-1);
			else dead();
		}
		else if(direction == LEFT){
			if(player.getX() > 0) player.setX(player.getX()-1);
			else dead();
		}
		else if(direction == DOWN){
			if(player.getY() < HEIGHT-1) player.setY(player.getY()+1);
			else dead();
		}
		if(food != null){
			if(food.getY()==player.getY() && food.getX()==player.getX()){
				addSnakePart(player);
				//field[food.getY()][food.getX()] = EMPTY_FIELD;
				food = null;
			}
			
		}
		if(player.getNext() != null) moveSnakeParts(player.getNext(), x_tmp, y_tmp);
		if(field[player.getY()][player.getX()] == SNAKE) dead();
	}
	
	private void moveSnakeParts(SnakePart sp, int x, int y){
		if(sp.getNext() != null) moveSnakeParts(sp.getNext(), sp.getX(), sp.getY());
		sp.setX(x);
		sp.setY(y);
	}
	private void drawPlayer(){	// Startet rekursives Zeichnen
		drawPlayer(player);
	}
	private void drawPlayer(SnakePart p){	// Zeichnet rekursiv alle Koerperteile
		field[p.getY()][p.getX()] = SNAKE;
		if(p.getNext() != null) drawPlayer(p.getNext());
	}
	
	/** Laeuft die Schlange rekursiv durch bis zum Ende
	 *  und fuegt dort ein Schlangenteil hinzu.
	 */
	private void addSnakePart(SnakePart p) {
		if(p.getNext() == null) p.setNext(new SnakePart(p.getX(), p.getY()));
		else addSnakePart(p.getNext());
	}
	
	private void fieldReset() {
		for(int i=0; i<HEIGHT; i++)
        	for(int j=0; j<WIDTH; j++)
        		field[i][j] = EMPTY_FIELD;
	}
	
	private String field2String() {
		String fieldText = "";
		for(int i=0; i<HEIGHT; i++){
        	for(int j=0; j<WIDTH; j++){
        		fieldText+=field[i][j];
        	}
        	fieldText+='\n';
		}
		return fieldText;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KEY_RIGHT) {
	    	if(direction != LEFT) direction = RIGHT;
	    }
		else if (e.getKeyChar() == KEY_UP) {
	    	if(direction != DOWN) direction = UP;
	    }
		else if (e.getKeyChar() == KEY_LEFT) {
	    	if(direction != RIGHT) direction = LEFT;
	    }
		else if (e.getKeyChar() == KEY_DOWN) {
	    	if(direction != UP) direction = DOWN;
	    }
    }

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent arg0) {}
}
