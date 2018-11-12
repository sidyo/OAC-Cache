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
            lines[i] = new DirectMappingLine(wordsPerBlock, tagSize);
        }
    }

    private int usedSize() {
        return lines.length * (1 + tagSize + (wordsPerBlock * wordSize));
    }

    public String toString() {
        String result = "Acess Time Delay: " + acessTimeDelay + ". Miss Penalty: " + missPenalty + ". Total Cache Size: " + cacheSize + ". Used Size: " + usedSize() + "\n";
        result+= "BV\tTag\tBlocks\n";
        for (DirectMappingLine l : lines) {
            result += l.toString()+"\n";
        }
        return result;
    }

    @Override
    public void acessMemory(int adress) {

    }

}