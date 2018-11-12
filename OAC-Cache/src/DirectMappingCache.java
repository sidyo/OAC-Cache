public class DirectMappingCache implements CacheMemoryInterface {
    int acessTimeDelay, missPenalty, cacheSize, wordsPerBlock, wordSize, tagSize;
    DirectMappingLine[] lines;

    DirectMappingCache(int acessTimeDelay, int missPenalty, int cacheSize, int wordsPerBlock, int wordSize) {
        this.acessTimeDelay = acessTimeDelay;
        this.missPenalty = missPenalty;
        this.cacheSize = cacheSize*8;
        this.wordsPerBlock = wordsPerBlock;
        this.wordSize = wordSize;
        int tagSize = 1;
        int aux = Double.valueOf((Math.pow(2, tagSize) * (1 + tagSize + (wordsPerBlock * wordSize)))).intValue();

        while (this.cacheSize >= Double.valueOf((Math.pow(2, tagSize) * (1 + tagSize + (wordsPerBlock * wordSize)))).intValue()) {
            aux = Double.valueOf((Math.pow(2, tagSize) * (1 + tagSize + (wordsPerBlock * wordSize)))).intValue();
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
        StringBuilder result = new StringBuilder("Acess Time Delay: " + acessTimeDelay + ". Miss Penalty: " + missPenalty + ". Total Cache Size: " + cacheSize + ". Used Size: " + usedSize()+ "("+((usedSize()*100.0)/(double)cacheSize) + "%)\n");
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
        System.out.println("binAd: "+binAd);
        int bitsBlock = Double.valueOf(Math.log(wordsPerBlock)/Math.log(2.0)).intValue();
        String word = binAd.substring(binAd.length()-bitsBlock, binAd.length());
        String line = binAd.substring(binAd.length()-bitsBlock-tagSize, binAd.length()-bitsBlock);
        String tag = binAd.substring(0, binAd.length()-bitsBlock-tagSize);
        int aux = Integer.parseInt(word, 2);
        int aux2= Integer.parseInt(line, 2);
        lines[aux2].acess(tag, aux );
        System.out.println("word: "+word);
        System.out.println("line: "+line);
        System.out.println("tag: "+tag);

    }

}