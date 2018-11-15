public class SetAssociativeLine{
    private boolean bv;
    String tag;
    private int[] block;

    public SetAssociativeLine(int blockSize){
        bv = false;
        block = new int[blockSize];
    }
    public void setBv(boolean bv) {
        this.bv = bv;
    }
    
    public boolean getBv(){
        return bv;
    }
    public String getTag() {
        return this.tag;
    }
    public void reset(){
        bv= false;
        tag = null;
        for(int i = 0; i<block.length;i++){
            block[i]=0;
        }
    }
    public void access(String tag,int word){
        bv = true;
        this.tag = tag;
        block[word]++;
    }
    public String toString(){
        StringBuilder result= new StringBuilder((bv? "1": "0") + "\t");
        result.append(tag + "\t");
        for(int i: block){
            result.append("A: "+i+" ");
        }
        return result.toString();
    }
    
}