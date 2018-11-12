public class DirectMappingLine{
    boolean bv;
    int tag;
    int tagSize;
    int[] block;

    public void setBv(boolean bv) {
        this.bv = bv;
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getTagSize() {
        return this.tagSize;
    }

    public int getBlockSize(){
        return block.length;
    }
    
    DirectMappingLine(int blockSize,int tagSize){
        bv = false;
        this.tagSize = tagSize;
        block = new int[blockSize];
    }
    public String toString(){
        String result = bv? "1": "0" + "\t"+ tag + "\t";
        for(int i: block){
            result += "word ";
        }
        return result;
    }
}