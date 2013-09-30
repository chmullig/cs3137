/**
 * @author Chris Mulligan <clm2186>
 * @course COMSW3137
 * @assignment Theory 1, Problem 3
 */
import java.math.BigInteger;
public class FibonacciTailess {
	public static BigInteger fib(int n) {
		return fibhelper(n, BigInteger.ZERO, BigInteger.ONE);
		}
	public static BigInteger fibhelper(int n, BigInteger nmin2, BigInteger nmin1) {
		if (n <= 1) {
			return nmin1;
		}
		return fibhelper(n-1, nmin1, nmin1.add(nmin2));
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		System.out.print("Running fib\ttailless\tfor\t" + n + "\t...");
        long begin = System.currentTimeMillis();
		BigInteger result = fib(n);
		long end = System.currentTimeMillis();
		System.out.println(" = " + result + ". Took\t" + (end-begin)/1000.0 + "\tsecs");
	}
}