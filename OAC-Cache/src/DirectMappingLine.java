public class DirectMappingLine{
    private boolean bv;
    private String tag;
    private int[] block;

    public String getTag() {
        return this.tag;
    }

    public void setBv(boolean bv) {
        this.bv = bv;
    }
    
    public boolean getBv(){
        return bv;
    }

    public int getBlockSize(){
        return block.length;
    }
    
    DirectMappingLine(int blockSize){
        bv = false;
        block = new int[blockSize];
    }
    public String toString(){
        StringBuilder result= new StringBuilder((bv? "1": "0") + "\t");
        result.append(tag + "\t");
        for(int i: block){
            result.append("A: "+i+" ");
        }
        return result.toString();
    }
    public void acess(String tag,int word){
        bv = true;
        this.tag = tag;
        block[word]++;
    }
    public void reset(){
        bv= false;
        tag = null;
        for(int i = 0; i<block.length;i++){
            block[i]=0;
        }
    }
}