import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int cacheType = 0;
		long acessTimeDelay = -1;
		long missPenalty = -1;
		do {
			try{
				System.out.print(" 1 - Direct Mapping.\n 2 - Set Associative.\nSelect the cache policy: ");
				cacheType = in.nextInt();
				if(cacheType != 1 && cacheType != 2){
					System.out.println("Invalid Option");
				}
			}catch(InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		}while(cacheType != 1 && cacheType!= 2);
		do {
			try{
				System.out.print("Set the acess time delay: ");
				acessTimeDelay = in.nextLong();
				if(acessTimeDelay < 0 ){
					System.out.println("Delay time must be positive.");
				}
			}catch(InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		}while(acessTimeDelay < 0);
		do {
			try{
				System.out.print("Set the penalty for a miss(ms): ");
				missPenalty = in.nextLong();
				if(missPenalty < 0 ){
					System.out.println("Penalty time must be positive.");
				}
			}catch(InputMismatchException e) {
				System.out.println("Invalid Entry.");
				in.nextLine();
			}
		}while(missPenalty < 0);
		in.close();
	}

}
