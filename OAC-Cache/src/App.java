import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int cacheType = 0;
		do {
			try{
				System.out.print(" 1 - Direct Mapping.\n 2 - Set Associative.\nSelect the cache policy: ");
				cacheType = in.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		}while(cacheType != 1 && cacheType!= 2);
		in.close();
	}

}
