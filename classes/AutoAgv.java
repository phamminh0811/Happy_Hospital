package classes;
import classes.node.Node2D;
import classes.states_of_auto_avg.HybridState;
import classes.states_of_auto_avg.IdleState;

public class AutoAgv extends Actor {
    public Graph graph;
    public Node2D[] path;
    public Node2D curNode;
    public Node2D endNode;
    public int cur;
    public int waitT;
    public int sobuocdichuyen;
    public int thoigiandichuyen;
    public HybridState hybridState;
    public int endX;
    public int endY;
    // public Text firstText;

    public int startX;
    public int startY;

    public AutoAgv(
        // Scene scene,

    ){
        
    }
    
}
