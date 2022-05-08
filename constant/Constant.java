package constant;
import constant.ModeOfPathPlanning;
import classes.node.Node2D;
import classes.Actor;
public class Constant {
    public static int DURATION(){
        //thời gian AutoAgv đợi để nhận/dỡ hàng khi đến đích
        return 4;
    }

    public double getLateness(double x){
        //hàm tính chi phí thiệt hại nếu đến quá sớm hoặc quá trễ
        return x*5;
    }

    public static double SAFE_DISTANCE(){
        return 46;
    }
    public static double DELTA_T(){
        return 10;
    }

    public static ModeOfPathPlanning MODE(){
        return ModeOfPathPlanning.FRANSEN;
    }
    public static String secondsToHMS(int seconds) {
        double h = Math.floor(seconds % (3600*24) /3600);
        double m = Math.floor(seconds % 3600 /60);
        double s = Math.floor(seconds % 60);

        String hDisplay = h >= 10 ? (""+h) : ("0" + h);
        String mDisplay = m >= 10 ? (""+m) : ("0" + m);
        String sDisplay = s >= 10 ? (""+s) : ("0" + s);

        return hDisplay +":"+ mDisplay +":"+ sDisplay;
    }


    public static boolean validDestination(int destX,int destY,int x,int y) {
        if((destY == 14 || destY == 13) && ((destX >= 0 && destX <= 5) || (destX >= 45 && destX <= 50)))
            return false; 
        var d = Math.sqrt(Math.pow((destX - x),2) + Math.pow((destY - y),2));
        if(d*32 < 10)
            return false;
        return true;
    }

    public static double minDistance(Actor actor,Actor[] otherActors){
        double dist = Double.MAX_VALUE;
        // TODO:
        // for (int i = 0; i < otherActors.length; i ++){
        //     Actor actor_i = otherActors[i];
        //     double smaller = Math.sqrt(Math.pow((actor_i.x - actor.x),2) + Math.pow((actor_i.y - actor.y),2));
        // };
        return dist;
    }

    public static int numberOfEdges(int width,int height,Node2D[][] nodes) {
        int count = 0;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                count += (nodes[i][j].nodeE != null) ? 1 : 0;
                count += (nodes[i][j].nodeS != null) ? 1 : 0;
                count += (nodes[i][j].nodeW != null) ? 1 : 0;
                count += (nodes[i][j].nodeN != null) ? 1 : 0;
                count += (nodes[i][j].nodeVE != null) ? 1 : 0;
                count += (nodes[i][j].nodeVS != null) ? 1 : 0;
                count += (nodes[i][j].nodeVW != null) ? 1 : 0;
                count += (nodes[i][j].nodeVN != null) ? 1 : 0;
            }
        }
        return count;
    }
}
