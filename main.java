import javax.swing.*;
public class Main extends JFrame {
  public Main(){
    setTitle("Snake"); 
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
    setSize(320,345);
    setLocation(400,400);
    add(new GameField());
    setVisible(true);
  } 
  public static void main(String[] args) {
    Main mw = new Main(); 
    } 
}



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField  extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    // чтобы всё работало заносим сюды
    public GameField() {
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }
// включение игры с таймером на создание яблок
    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();
    }
// создания яблока в рандомном месте
    public void createApple() {
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }
// загрузка картинок
    public void loadImages() {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dots.png");
        dot = iid.getImage();
    }
// отрисовка игрового поля, drawImage apple отрисовывает яблоко на его координатах, а drawImage dot отрисовывает змейку точками
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        }else {
            String str = "Game Over";
            Font f = new Font("Arial",Font.BOLD,14);
            g.setColor(Color.white);
            g.setFont(f);
            g.drawString(str, 150,SIZE/2);
        }
    }
    // движение по 1 точке в направлении
    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }if (left) {
            x[0] -= DOT_SIZE;
        }if(right) {
            x[0] += DOT_SIZE;
        }if(up) {
            y[0] -= DOT_SIZE;
        }if(down) {
            y[0] += DOT_SIZE;
        }
    }
    // проверка на то задеваем ли мы яблоко и увеличение змейки
    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            dots++;
            createApple();
        }
    }
    // проверка столкновения со стенкой
    public void checkCollissions(){
        for (int i = dots; i > 0; i--) {
            if(i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }if(x[0]>SIZE){
            inGame = false;
        }if(x[0]<0){
            inGame = false;
        }if(y[0]>SIZE){
            inGame = false;
        }if(y[0]<0){
            inGame = false;
        }
    }
    // запускает 2 проверки и движение если в игре
        @Override
        public void actionPerformed (ActionEvent e){
            if (inGame) {
                checkApple();
                checkCollissions();
                move();
            }
            repaint();
        }
        // смена направления движения нажатием клавиш
        class FieldKeyListener extends KeyAdapter{
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_LEFT && ! right){
                    left = true;
                    up = false;
                    down = false;
                }
                if(key == KeyEvent.VK_RIGHT && ! left){
                    right = true;
                    up = false;
                    down = false;
                }
                if(key == KeyEvent.VK_UP && ! down){
                    up = true;
                    left = false;
                    right = false;
                }
                if(key == KeyEvent.VK_DOWN && ! up){
                    down = true;
                    left = false;
                    right = false;
                }
            }

        }
    }
