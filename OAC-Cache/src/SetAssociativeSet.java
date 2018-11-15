import java.util.Random;

public class SetAssociativeSet {
    private SetAssociativeLine[] lines;
    private int policy;
    private int lastAccessed;
    private int hitCounter, missCounter;

    public int getHitCounter() {
        return this.hitCounter;
    }
    public int getMissCounter() {
        return this.missCounter;
    }
    

    public SetAssociativeSet(int setSize,int blockSize, int policy){
        hitCounter = missCounter = 0;
        lines = new SetAssociativeLine[setSize];
        for(int i = 0; i < lines.length; i++){
            lines[i] = new SetAssociativeLine(blockSize);
        }
        lastAccessed= -1;
        this.policy = policy;
    }

    public void access(String tag, int word){
        for(int i = 0; i < lines.length ; i++){
            if(lines[i].getBv()){
                if(lines[i].getTag().equals(tag)){
                    lines[i].access(tag, word);
                    lastAccessed = i;
                    hitCounter++;
                    return;
                }
            }else{
                lines[i].access(tag, word);
                lastAccessed = i;
                missCounter++;
                return;
            }
        }
        if(policy == 1){
            policySequential(tag, word);
        }else if (policy == 2){
            policyRandom(tag, word);
        }
    }

    private void policySequential(String tag,int word){
        lastAccessed = lastAccessed == lines.length-1? 0 : lastAccessed+1;
        lines[lastAccessed].reset(); 
        lines[lastAccessed].access(tag, word);
    }
    private void policyRandom(String tag,int word){
        Random r = new Random();
        int pos = r.nextInt(lines.length);
        lines[pos].reset();
        lines[pos].access(tag, word);
    }

    public String toString(){
        StringBuilder result = new StringBuilder("");
        for(SetAssociativeLine l : lines){
            result.append(l.toString()+"\n");
        }
        return result.toString().trim();
    }



}