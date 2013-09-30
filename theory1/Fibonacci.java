/**
 * @author Chris Mulligan <clm2186>
 * @course COMSW3137
 * @assignment Theory 1, Problem 3
 */
import java.math.BigInteger;
public class Fibonacci {
	static BigInteger TWO;
	public static BigInteger fib(BigInteger n) {
		if (n.compareTo(BigInteger.ONE) <= 0) {
			return n;
		}
		return fib(n.subtract(TWO)).add(fib(n.subtract(BigInteger.ONE)));
	}

	public static void main(String[] args) {
		TWO = new BigInteger("2");
		BigInteger n = new BigInteger(args[0]);
		System.out.print("Running fib\ttailed\tfor\t" + n + "\t...");
        long begin = System.currentTimeMillis();
		BigInteger result = fib(n);
		long end = System.currentTimeMillis();
		System.out.println(" = " + result + ". Took\t" + (end-begin)/1000.0 + "\tsecs");
	}
}