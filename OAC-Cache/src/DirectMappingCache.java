public class DirectMappingCache implements CacheMemoryInterface {
    int accessTimeDelay, missPenalty, cacheSize, wordsPerBlock, wordSize, tagSize;
    int hitCounter, missCounter, accessCounter;
    DirectMappingLine[] lines;

    DirectMappingCache(int accessTimeDelay, int missPenalty, int cacheSize, int wordsPerBlock, int wordSize) {
        this.accessTimeDelay = accessTimeDelay;
        this.missPenalty = missPenalty;
        this.cacheSize = cacheSize*8;
        this.wordsPerBlock = wordsPerBlock;
        this.wordSize = wordSize;
        hitCounter = missCounter = accessCounter= 0;
        int tagSize = 1;
        //int aux = Double.valueOf((Math.pow(2, tagSize) * (1 + tagSize + (wordsPerBlock * wordSize)))).intValue();

        while (this.cacheSize >= Double.valueOf((Math.pow(2, tagSize) * (1 + tagSize + (wordsPerBlock * wordSize)))).intValue()) {
          //  aux = Double.valueOf((Math.pow(2, tagSize) * (1 + tagSize + (wordsPerBlock * wordSize)))).intValue();
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
        StringBuilder result = new StringBuilder("Acess Time Delay: " + accessTimeDelay + ". Miss Penalty: " + missPenalty + ".\n");
        result.append("Total Cache Size: " + cacheSize + ". Used Size: " + usedSize()+ "("+((usedSize()*100.0)/(double)cacheSize) + "%)\n");
        result.append("Total Acesses: "+accessCounter+". Hit Counter: "+hitCounter+"("+(double)(hitCounter/accessCounter)+"%). Miss Counter: "+missCounter+"("+(double)(missCounter*100/accessCounter)+"%).\n");
        result.append("Total Time: "+totalTime+"ms. Hit Time: "+hitCounter*accessTimeDelay+"ms("+(double)(hitCounter*accessTimeDelay*100)/totalTime+"%). Miss Time: "+missCounter*missPenalty+"ms("+(double)(missCounter*missPenalty*100)/totalTime+"%).\n");
        int bitsBlock = Double.valueOf(Math.log(wordsPerBlock)/Math.log(2.0)).intValue();
        result.append("Address: "+(wordSize-tagSize-bitsBlock)+"(Tag) "+tagSize+"(Line) "+bitsBlock+"(Word)\n");
        result.append("BV\tTag\tBlocks\n");
        for (DirectMappingLine l : lines) {
            result.append(l.toString()+"\n");
        }
        return result.toString();
    }

    //@Override
    public void acessMemory(int address) {
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
        int tagInt= Integer.parseInt(lineBin, 2);
        accessCounter++;
        if(lines[tagInt].getBv()){
            if(lines[tagInt].getTag().equals(tagBin)){
                lines[tagInt].acess(tagBin, wordInt);
                hitCounter++;
            }else{
                lines[tagInt].reset();
                lines[tagInt].acess(tagBin, wordInt);
                missCounter++;
            }
        }else{
            lines[tagInt].acess(tagBin, wordInt);
            missCounter++;
        }
        //System.out.println("word: "+wordBin);
        //System.out.println("line: "+lineBin);
        //System.out.println("tag: "+tagBin);

    }

}