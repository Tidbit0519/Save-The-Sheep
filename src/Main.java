
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;

public class Main extends JPanel implements KeyListener {

    ArrayList<Tree> forest;
    ArrayList<Sheep> flock;
    ArrayList<Robber> thugs;
    ArrayList<Water> sebus;
    ArrayList<Sprite> drawables;

    Ammon hero;
    int numRows;
    int numColumns;
    int numSheep;
    int numRobber;
    String direction;

    public static final int rowSize = 50;
    public static final int columnSize = 50;

    public Main() {

        forest = new ArrayList<>();
        thugs = new ArrayList<>();
        flock = new ArrayList<>();
        sebus = new ArrayList<>();
        drawables = new ArrayList<>();
        hero = new Ammon(0,0);
        numRows = 7;
        numColumns = 7;
        numRobber = 4;
        addKeyListener(this);
        reset();
    }

    @Override
    public void paintComponent(Graphics g) {
        requestFocusInWindow();

        //fill in the background color
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        //draw the grass
        g.setColor(new Color(200, 255, 200));
        g.fillRect(10, 10, 50 * numColumns, 50 * numRows);

        //draw border around field perimeter
        g.setColor(Color.BLACK);
        g.drawRect(10, 10, 50 * numColumns, 50 * numRows);

        //displays the number of sheep and robbers left.
        g.drawString("Sheep Remaining:" + numSheep,10,380);
        g.drawString("Robbers Remaining: " + numRobber,220,380);

        //draw all the sprites.
        for (Sprite s : drawables) {
            s.draw(g);
        }
    }

//    Accept the user's keyboard input.
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        Point nextPos = new Point(hero.getLocation());

//        if (key == KeyEvent.VK_W) {
//            direction = "north";
//            shoot(direction);
//        }
//        if (key == KeyEvent.VK_S) {
//            direction = "south";
//            shoot(direction);
//        }
//        if (key == KeyEvent.VK_A) {
//            direction = "west";
//            shoot(direction);
//        }
//        if (key == KeyEvent.VK_D) {
//            direction = "east";
//            shoot(direction);
//        }
        //shoots something to kill.
        switch (key) {
            case KeyEvent.VK_W:
                direction = "north";
                shoot(direction);
                break;
            case KeyEvent.VK_S:
                direction = "south";
                shoot(direction);
                break;
            case KeyEvent.VK_A:
                direction = "west";
                shoot(direction);
                break;
            case KeyEvent.VK_D:
                direction = "east";
                shoot(direction);
                break;
            default:
                break;
        }


        //moves ammon back to his previous position if touch border or hits a tree.
        if (key == KeyEvent.VK_UP) {
            nextPos.y = nextPos.y - 1;
            if (touch(nextPos) || hitATree(nextPos)) {
                nextPos.y = nextPos.y + 1;
            }
        }
        if (key == KeyEvent.VK_DOWN) {
            nextPos.y = nextPos.y + 1;
            if (touch(nextPos) || hitATree(nextPos)) {
                nextPos.y = nextPos.y - 1;
            }
        }
        if (key == KeyEvent.VK_LEFT) {
            nextPos.x = nextPos.x - 1;
            if (touch(nextPos) || hitATree(nextPos)) {
                nextPos.x = nextPos.x + 1;
            }
        }
        if (key == KeyEvent.VK_RIGHT) {
            nextPos.x = nextPos.x + 1;
            if (touch(nextPos) || hitATree(nextPos)) {
                nextPos.x = nextPos.x - 1;
            }
        }
         hero.setLocation(nextPos);

        var rescue = false;
        var hitWater = false;
        var hitRobber = false;

        //if ammon touches the sheep, the sheep disappears.
        for (var f : flock) {
            if (hero.getLocation().equals(f.getLocation())) {
                f.setLocation(null);
                numSheep--;
                if (numSheep == 0) {
                    JOptionPane.showMessageDialog(null,"Congratulations! You have rescued all the sheeps!");
                    JOptionPane.showConfirmDialog(null, "Do you want to play again?");
                    rescue = true;
                    break;
                }
            }
        }
        if(rescue) {
            reset();
        }

        //ammon dies if he touches water.
        for (var s : sebus) {
            if (hero.getLocation().equals(s.getLocation())) {
                JOptionPane.showMessageDialog(null,"Oh no! You died!");
                JOptionPane.showConfirmDialog(null, "Do you want to play again?");
                hitWater = true;
                break;
            }
        }
        if (hitWater) {
            reset();
        }

        //ammon dies if he is near the robbers.
        for (var t : thugs) {
            if (t.getLocation() != null && hero.isNear(t)) {
                JOptionPane.showMessageDialog(null, "Oh no! You died!");
                JOptionPane.showConfirmDialog(null, "Do you want to play again?");
                hitRobber = true;
                break;
            }
        }
        if (hitRobber) {
            reset();
        }

        //checks ammon's position near water, sheeps, and robbers.
            if (!rescue && !hitWater && !hitRobber) {
                hero.setLocation(nextPos);
                // repaint();
            }
        repaint();
    }

    //condition for ammon if he touches the border.
    private boolean touch(Point v) {
        return v.x <= -1 || v.x >= 7 || v.y <= -1 || v.y >= 7;
    }

    private boolean hitATree(Point p) {
        for (var t : forest) {
            if (t.getLocation().equals(p)) {
                return true;
            }
        }
        return false;
    }

    private void shoot(String sling) {
        boolean killSheep = false;
        //conditions for shooting different directions and compare the position of Sprite with Ammon's.
        if (sling.equals("north")) {
            for (int i = hero.getLocation().y; i > 0; i--) {
                for (var t : thugs) {
                    if (t.getLocation() != null && t.getLocation().x == hero.getLocation().x && t.getLocation().y == i) {
                        t.setLocation(null);
                        return;
                    }
                }
                for (var s : flock) {
                    if (s.getLocation() != null && s.getLocation().x == hero.getLocation().x && s.getLocation().y == i) {
                        s.setLocation(null);
                        JOptionPane.showMessageDialog(this, "You killed a sheep! Restart!");
                        killSheep = true;
                        break;
                    }
                }
                if (killSheep) {
                    reset();
                    return;
                }
                for (var f : forest) {
                    if (f.getLocation().x == hero.getLocation().x && f.getLocation().y == i) {
                        return;
                    }
                }
            }
        }
        if (sling.equals("south")) {
            for (int i = hero.getLocation().y; i < numRows; i++) {
                for (var t : thugs) {
                    if (t.getLocation() != null && t.getLocation().x == hero.getLocation().x && t.getLocation().y == i) {
                        t.setLocation(null);
                        numRobber--;
                        return;
                    }
                }
                for (var s : flock) {
                    if (s.getLocation() != null && s.getLocation().x == hero.getLocation().x && s.getLocation().y == i) {
                        s.setLocation(null);
                        JOptionPane.showMessageDialog(this, "You killed a sheep! Restart!");
                        killSheep = true;
                        break;
                    }
                }
                if (killSheep) {
                    reset();
                    return;
                }
                for (var f : forest) {
                    if (f.getLocation().x == hero.getLocation().x && f.getLocation().y == i) {
                        return;
                    }
                }
            }
        }
        if (sling.equals("east")) {
            for (int i = hero.getLocation().x; i < numColumns; i++) {
                for (var t : thugs) {
                    if (t.getLocation() != null && t.getLocation().y == hero.getLocation().y && t.getLocation().x == i) {
                        t.setLocation(null);
                        numRobber--;
                        return;
                    }
                }

                for (var s : flock) {
                    if (s.getLocation() != null && s.getLocation().y == hero.getLocation().y && s.getLocation().x == i) {
                        s.setLocation(null);
                        JOptionPane.showMessageDialog(this, "You killed a sheep! Restart!");
                        killSheep = true;
                        break;
                }
            }
                if (killSheep) {
                    reset();
                    return;
                }
                for (var f : forest) {
                    if (f.getLocation().y == hero.getLocation().y && f.getLocation().x == i) {
                        return;
                    }
                }
            }
        }

        if (sling.equals("west")) {
            for (int i = hero.getLocation().x; i > 0; i--) {
                for (var t : thugs) {
                    if (t.getLocation() != null && t.getLocation().y == hero.getLocation().y && t.getLocation().x == i) {
                        t.setLocation(null);
                        numRobber--;
                        return;
                    }
                }
                for (var s : flock) {
                    if (s.getLocation() != null && s.getLocation().y == hero.getLocation().y && s.getLocation().x == i) {
                        s.setLocation(null);
                        JOptionPane.showMessageDialog(this, "You killed a sheep! Restart!");
                        killSheep = true;
                        break;
                    }
                }
                if (killSheep) {
                    reset();
                    return;
                }
                for (var f : forest) {
                    if (f.getLocation().y == hero.getLocation().y && f.getLocation().x == i) {
                        return;
                    }
                }
            }
        }
    }

    private void reset() {

        drawables.removeAll(flock);
        drawables.removeAll(thugs);
        drawables.clear();
        sebus.clear();
        forest.clear();
        flock.clear();
        thugs.clear();
        hero.setLocation(0, 0);
        sebus.add(new Water(3,2));
        sebus.add(new Water(4,2));
        sebus.add(new Water(2,3));
        sebus.add(new Water(3,3));
        sebus.add(new Water(4,3));
        sebus.add(new Water(5,3));
        sebus.add(new Water(3,4));
        sebus.add(new Water(4,4));
        forest.add(new Tree(2,1));
        forest.add(new Tree(5,1));
        forest.add(new Tree(1,4));
        flock.add(new Sheep(3,1));
        flock.add(new Sheep(2,2));
        flock.add(new Sheep(1,3));
        flock.add(new Sheep(2,4));
        flock.add(new Sheep(5,2));
        thugs.add(new Robber(5,0));
        thugs.add(new Robber(0,3));
        thugs.add(new Robber(1,6));
        thugs.add(new Robber(4,5));
        numSheep = 5;
        numRobber = 4;


        //point the sprites to the same memory address of the other array list by using add all.
        drawables.add(hero);
        drawables.addAll(sebus);
        drawables.addAll(forest);
        drawables.addAll(flock);
        drawables.addAll(thugs);
//        invalidate();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        var window = new JFrame("Save the Sheep");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(450,450);
        window.setContentPane(new Main());
        window.setVisible(true);
    }



}
