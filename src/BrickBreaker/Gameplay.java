package BrickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, Action  {
    private boolean play = false;
    private  int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -2;
    private int ballYDir = -3;
    JFrame obj = new JFrame();

    Font font = new Font("SansSerif", Font.PLAIN, 18);
    Font deathFont = new Font("SansSerif", Font.BOLD, 25);

    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // Map
        map.draw((Graphics2D) g);


        // Score
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString("score: " + score, 600, 20);

        // Die
        if (ballPosY >= 570) {
            ballYDir = 0;
            ballXDir = 0;
            play = false;
            g.setColor(Color.red);
            g.setFont(deathFont);
            g.drawString("You died! Press enter to restart!", 125, 300);
        }

        // Win
        if (totalBricks <= 0) {
            ballYDir = 0;
            ballXDir = 0;
            play = false;
            g.setColor(Color.green);
            g.setFont(deathFont);
            g.drawString("You win! Press enter to restart!", 125, 300);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            if (new  Rectangle(ballPosX, ballPosY, 15, 15).intersects(new Rectangle(playerX, 550, 100, 9))) {
                ballYDir = -ballYDir;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j<map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(rect)) {
                            map.setBrickValue(0, i, j);
                            ballXDir = -ballXDir;
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= rect.x || ballPosX + 1 >= rect.x + rect.width) {
                                ballXDir = -ballXDir;
                            } else {
                                ballYDir = -ballYDir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXDir;
            ballPosY += ballYDir;
            if (ballPosX<0) {
                ballXDir = -ballXDir;
            }
            if (ballPosY<0) {
                ballYDir = -ballYDir;
            }
            if (ballPosX>670) {
                ballXDir = -ballXDir;
            }
        }

        repaint();
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 595) {
                playerX = 595;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 15) {
                playerX = 15;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                playerX = 310;
                ballPosX = 120;
                ballPosY = 350;
                ballXDir = -2;
                ballYDir = -3;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
            }
        }
    }
    public void moveRight() {
        play = true;
        playerX +=20;
    }

    public void moveLeft() {
        play = true;
        playerX -=20;
    }


    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public void putValue(String key, Object value) {

    }
}
