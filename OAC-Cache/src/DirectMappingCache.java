public class DirectMappingCache implements CacheMemoryInterface {
    private int accessTimeDelay, missPenalty, cacheSize, wordsPerBlock, wordSize, tagSize;
    private int hitCounter, missCounter, accessCounter;
    private DirectMappingLine[] lines;

    public DirectMappingCache(int accessTimeDelay, int missPenalty, int cacheSize, int wordsPerBlock, int wordSize) {
        this.accessTimeDelay = accessTimeDelay;
        this.missPenalty = missPenalty;
        this.cacheSize = cacheSize*8;
        this.wordsPerBlock = wordsPerBlock;
        this.wordSize = wordSize;
        hitCounter = missCounter = accessCounter= 0;
        int tagSize = 1;

        while (this.cacheSize >= Double.valueOf((Math.pow(2, tagSize) * (1 + tagSize + (wordsPerBlock * wordSize)))).intValue()) {
            tagSize++;
        }
        tagSize--;
        if (tagSize == 0) {
            throw new cacheSizeTooSmallException();
        }
        this.tagSize = tagSize;
        int CacheLines = Double.valueOf(Math.pow(2, tagSize)).intValue();
        lines = new DirectMappingLine[CacheLines];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new DirectMappingLine(wordsPerBlock);
        }
    }

    private int usedSize() {
        return lines.length * (1 + tagSize + (wordsPerBlock * wordSize));
    }

    public String toString() {
        int totalTime = (hitCounter*accessTimeDelay+missCounter*missPenalty);
        StringBuilder result = new StringBuilder("Access Time Delay: " + accessTimeDelay + ". Miss Penalty: " + missPenalty + ".\n");
        result.append("Total Cache Size: " + cacheSize + ". Used Size: " + usedSize()+ "("+((usedSize()*100.0)/(double)cacheSize) + "%)\n");
        result.append("Total Accesses: "+accessCounter+". Hit Counter: "+hitCounter+"("+(hitCounter*100.0/(double)accessCounter)+"%). Miss Counter: "+missCounter+"("+(missCounter*100.0/(double)accessCounter)+"%).\n");
        result.append("Total Time: "+totalTime+"ms. Hit Time: "+hitCounter*accessTimeDelay+"ms("+(hitCounter*accessTimeDelay*100.0)/(double)totalTime+"%). Miss Time: "+missCounter*missPenalty+"ms("+((missCounter*missPenalty*100.0)/(double)totalTime+"%).\n"));
        int bitsBlock = Double.valueOf(Math.log(wordsPerBlock)/Math.log(2.0)).intValue();
        result.append("Address: "+(wordSize-tagSize-bitsBlock)+"(Tag) "+tagSize+"(Line) "+bitsBlock+"(Word)\n");
        result.append("BV\tTag\tBlocks\n");
        for (DirectMappingLine l : lines) {
            result.append(l.toString()+"\n");
        }
        return result.toString();
    }

    @Override
    public void accessMemory(int address) {
        String binAd = Integer.toBinaryString(address);
        while(binAd.length() < wordSize){
            binAd = '0'+binAd;
        }
        //System.out.println("binAd: "+binAd);
        int bitsBlock = Double.valueOf(Math.log(wordsPerBlock)/Math.log(2)).intValue();
        String wordBin = binAd.substring(binAd.length()-bitsBlock, binAd.length());
        String lineBin = binAd.substring(binAd.length()-bitsBlock-tagSize, binAd.length()-bitsBlock);
        String tagBin = binAd.substring(0, binAd.length()-bitsBlock-tagSize);
        int wordInt = Integer.parseInt(wordBin, 2);
        int lineInt= Integer.parseInt(lineBin, 2);
        accessCounter++;
        if(lines[lineInt].getBv()){
            if(lines[lineInt].getTag().equals(tagBin)){
                lines[lineInt].access(tagBin, wordInt);
                hitCounter++;
            }else{
                lines[lineInt].reset();
                lines[lineInt].access(tagBin, wordInt);
                missCounter++;
            }
        }else{
            lines[lineInt].access(tagBin, wordInt);
            missCounter++;
        }
    }

}