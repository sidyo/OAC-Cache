import java.io.Console;

public class SetAssociativeCache implements CacheMemoryInterface {
    private int accessTimeDelay, missPenalty, cacheSize, wordsPerBlock, wordSize, tagSize;
    private SetAssociativeSet[] sets;
    private int accessCounter;

    public SetAssociativeCache(int accessTimeDelay, int missPenalty, int cacheSize, int wordsPerBlock, int wordSize,
            int numberOfSets, int policy) {
        
        this.accessTimeDelay = accessTimeDelay;
        this.missPenalty = missPenalty;
        this.cacheSize = cacheSize;
        this.wordsPerBlock = wordsPerBlock;
        this.wordSize = wordSize;
        sets = new SetAssociativeSet[numberOfSets];
        
        accessCounter = 0;
                
        this.tagSize = wordSize-Double.valueOf(Math.log(wordsPerBlock) / Math.log(2)).intValue() - Double.valueOf(Math.log(numberOfSets) / Math.log(2)).intValue();;
        int cacheLineSize = 1 + tagSize + (wordsPerBlock * wordSize);
        int numberOfLines = Math.floorDiv(this.cacheSize, cacheLineSize);
        int numberOfLinesPerSet = Math.floorDiv(numberOfLines, numberOfSets);
        if(numberOfLinesPerSet == 0){
            throw new cacheSizeTooSmallException();
        }
        for (int i = 0; i < sets.length; i++) {
            sets[i] = new SetAssociativeSet(numberOfLinesPerSet, wordsPerBlock, policy);
        }
    }

    private int getMisses() {
        int result = 0;
        for (SetAssociativeSet s : sets) {
            result += s.getMissCounter();
        }
        return result;
    }

    private int getHits() {
        int result = 0;
        for (SetAssociativeSet s : sets) {
            result += s.getHitCounter();
        }
        return result;
    }

    @Override
    public void accessMemory(int address) {
        String binAd = Integer.toBinaryString(address);
        while (binAd.length() < wordSize) {
            binAd = '0' + binAd;
        }
       
        int bitsBlock = Double.valueOf(Math.log(wordsPerBlock) / Math.log(2)).intValue();
        int bitsSet = Double.valueOf(Math.log(sets.length) / Math.log(2)).intValue();
        String wordBin = binAd.substring(binAd.length() - bitsBlock, binAd.length());
        String setBin = binAd.substring(binAd.length() - bitsBlock - bitsSet, binAd.length() - bitsBlock);
        String tagBin = binAd.substring(0, binAd.length() - bitsBlock - bitsSet);
        int wordInt = Integer.parseInt(wordBin, 2);
        int setInt = Integer.parseInt(setBin, 2);
        accessCounter++;
        sets[setInt].access(tagBin, wordInt);
    }

    public String toString(){  
        int totalTime = (getHits()*accessTimeDelay+getMisses()*missPenalty);
        StringBuilder result = new StringBuilder("Access Time Delay: " + accessTimeDelay + ". Miss Penalty: " + missPenalty + ".\n");
        result.append("Total Accesses: "+accessCounter+". Hit Counter: "+getHits()+"("+(getHits()/(double)accessCounter)+"%). Miss Counter: "+getMisses()+"("+(getMisses()*100.0/(double)accessCounter)+"%).\n");
        result.append("Total Time: "+totalTime+"ms. Hit Time: "+getHits()*accessTimeDelay+"ms("+(getHits()*accessTimeDelay*100.0)/(double)totalTime+"%). Miss Time: "+getMisses()*missPenalty+"ms("+(getMisses()*missPenalty*100.0)/(double)totalTime+"%).\n");
        int bitsBlock = Double.valueOf(Math.log(wordsPerBlock)/Math.log(2.0)).intValue();
        int bitsSet = Double.valueOf(Math.log(sets.length) / Math.log(2)).intValue();
        result.append("Address: "+(wordSize-bitsSet-bitsBlock)+"(Tag) "+bitsSet+"(Set) "+bitsBlock+"(Word)\n");
        result.append("BV\tTag\tBlocks\n");
        for (SetAssociativeSet s : sets) {
            result.append(s.toString()+"\n------------------------------------\n");
        }
        return result.toString();
    }

}