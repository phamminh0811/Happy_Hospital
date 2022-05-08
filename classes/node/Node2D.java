package classes.node;
import classes.node.StateOfNode2D;
import constant.Constant;
import constant.ModeOfPathPlanning;

public class Node2D{
    static final double LAMBDA = 0.4;
    
    public double x; // 0 <= x <= 52
    public double y;

    public Node2D nodeW;
    public Node2D nodeN;
    public Node2D nodeS;
    public Node2D nodeE;


// ????
    public double w_edge_W = Double.MAX_VALUE;
    public double w_edge_N = Double.MAX_VALUE;
    public double w_edge_S = Double.MAX_VALUE;
    public double w_edge_E = Double.MAX_VALUE;

    private double w = 0; // thời gian dự đoán dừng (ms)
    private double u = 0; // thời gian dừng thực tế (ms)

    public StateOfNode2D state; // trạng thái nút
    public double p_random; //xác xuất nút chuyển sang trạng thái Busy
    public double t_min; //thời gian tối thiểu nút ở trạng thái busy (ms)
    public double t_max; //thời gian tối đa nút ở trạng thái busy (ms)

    public Node2D nodeVW;
    public Node2D nodeVN;
    public Node2D nodeVS;
    public Node2D nodeVE;

    public double w_edge_VW = Double.MAX_VALUE;
    public double w_edge_VN = Double.MAX_VALUE;
    public double w_edge_VS = Double.MAX_VALUE;
    public double w_edge_VE = Double.MAX_VALUE;

    public boolean isVirtualNode = false;
    public double _weight = 0;

    public Node2D(
        double x,
        double y,
        boolean isVirtualNode,
        StateOfNode2D state,
        double p_random,
        int t_min,
        int t_max
    ){
        this.x = x;
        this.y = y;
        this.isVirtualNode = isVirtualNode;
        this.state = state;
        this.p_random = p_random;
        this.t_min = t_min;
        this.t_max = t_max;
    }

    public Node2D(
        double x,
        double y,
        boolean isVirtualNode
    ){
        this(x,y,isVirtualNode,StateOfNode2D.NOT_ALLOW,0.05,2000,3000);
    }

    public double getW(){
        if (Constant.MODE() ==  ModeOfPathPlanning.FRANSEN){
            return this.w;
        } else {
            return this.getWeight();
        }
    }

    public double getWeight(){ 
        return this._weight; 
    }

    public void setWeight(int value){
        this._weight = value;
    }

    public void setNeighbor(Node2D node){
        if(node == null)
          return;
        if(node.isVirtualNode) {
          if (this.x + 1 == node.x && this.y == node.y) {
              this.nodeVE = node;
              this.w_edge_VE = 1;
          } else if (this.x == node.x && this.y + 1 == node.y) {
              this.nodeVS = node;
              this.w_edge_VS = 1;
          } else if (this.x - 1 == node.x && this.y == node.y) {
              this.nodeVW = node;
              this.w_edge_VW = 1;
          } else if (this.x == node.x && this.y - 1 == node.y) {
              this.nodeVN = node;
              this.w_edge_VN = 1;
          }
          return;
        }
        this.setRealNeighbor(node);
        return;        
      }
    
    private void setRealNeighbor(Node2D node) {
        if (this.x + 1 == node.x && this.y == node.y) {
          this.nodeE = node;
          this.w_edge_E = 1;
        } else if (this.x == node.x && this.y + 1 == node.y) {
          this.nodeS = node;
          this.w_edge_S = 1;
        } else if (this.x - 1 == node.x && this.y == node.y) {
          this.nodeW = node;
          this.w_edge_W = 1;
        } else if (this.x == node.x && this.y - 1 == node.y) {
          this.nodeN = node;
          this.w_edge_N = 1;
        }
    }
    
    public void setState(StateOfNode2D state) {
        this.state = state;
    }
    
    public boolean equal(Node2D node) {
        if(node.isVirtualNode != this.isVirtualNode)
          return false;
        return this.x == node.x && this.y == node.y;
    }
    
    public  boolean madeOf(Node2D node) {
        return this.equal(node);
    }
    
    public void setU(double u) {
        this.u = Math.floor(u);
        this.updateW();
      }
    
    public void updateW() {
        this.w = (1 - LAMBDA) * this.w + LAMBDA * this.u;
    }
}
