import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
	// 1 1 2 32 8 4 test.txt
	public static void main(String[] args) {
		CacheMemoryInterface cache = null;
		try {
			cache = createCache();
		} catch (cacheSizeTooSmallException e) {
			System.out.println("Cache Size too Small. Try Again.");
		}
		accessFile(cache);


		System.out.println(cache.toString());
	}

	private static CacheMemoryInterface createCache() {
		Scanner in = new Scanner(System.in);
		int cacheType = 0;
		int acessTimeDelay = -1;
		int missPenalty = -1;
		int cacheSize = -1;
		int wordsPerBlock = -1;
		int wordSize = -1;
		do {
			try {
				System.out.print(" 1 - Direct Mapping.\n 2 - Set Associative.\nSelect the cache policy: ");
				cacheType = in.nextInt();
				if (cacheType != 1 && cacheType != 2) {
					System.out.println("Invalid Option");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		} while (cacheType != 1 && cacheType != 2);
		do {
			try {
				System.out.print("Set the acess time delay(ms): ");
				acessTimeDelay = in.nextInt();
				if (acessTimeDelay < 0) {
					System.out.println("Delay time must be positive.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		} while (acessTimeDelay < 0);
		do {
			try {
				System.out.print("Set the penalty for a miss(ms): ");
				missPenalty = in.nextInt();
				if (missPenalty < 0) {
					System.out.println("Penalty time must be positive.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		} while (missPenalty < 0);
		do {
			try {
				System.out.print("Set the cache size(Bytes): ");
				cacheSize = in.nextInt();
				if (cacheSize < 1) {
					System.out.println("Cache size must be higher than 0.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		} while (cacheSize < 0);
		do {
			try {
				System.out.print("Set the word size (bits): ");
				wordSize = in.nextInt();
				if (wordSize < 1) {
					System.out.println("Word size must be higher than 0.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		} while (wordSize < 1);
		do {
			try {
				System.out.print("Set the number of word per block: ");
				wordsPerBlock = in.nextInt();
				if (wordsPerBlock < 1) {
					System.out.println("Words per block must be higher than 0.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		} while (wordsPerBlock < 1);
		if (cacheType == 1) {
			return new DirectMappingCache(acessTimeDelay, missPenalty, cacheSize, wordsPerBlock, wordSize);
		} else {
			return null;
		}
	}

	private static void accessFile(CacheMemoryInterface cache) {
		Scanner in = new Scanner(System.in);
		File file = null;
		do {
			System.out.print("Type the path to the file you want to read: ");
			String filename = in.next();
			file = new File(filename);
			if (!file.canRead()) {
				System.out.println("Cannot find the file " + filename + ". Try again.");
			}
		} while (!file.canRead());
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine())!=null){
				cache.acessMemory(Integer.parseInt(line));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
