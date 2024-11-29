public class Edge {
    //0 is row, 1 is col, 2 is box
    private int type;
    private Vertex v1;
    private Vertex v2;
    public Edge(Vertex v1, Vertex v2, int type){
        this.type = type;
        this.v1 = v1;
        this.v2 = v2;
    }
    public int getEdgeType(){
        return this.type;
    }
    public Vertex getv1(){
        return this.v1;
    }
    public Vertex getV2(){
        return this.v2;
    }
}
