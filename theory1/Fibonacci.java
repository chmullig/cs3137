/**
 * @author Chris Mulligan <clm2186>
 * @course COMSW3137
 * @assignment Theory 1, Problem 3
 */
public class Fibonacci {
	public static int fib(int n) {
		if (n <= 1) {
			return n;
		}
		return fib(n-2) + fib(n-1);
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		//System.out.print("Running fib for " + n + "...");
        long begin = System.currentTimeMillis();
		int result = fib(n);
		long end = System.currentTimeMillis();
		//System.out.println(" = " + result + ". Took " + (end-begin)/1000.0 + "s");
		System.out.println(n + " " + (end-begin)/1000.0);
	}
}