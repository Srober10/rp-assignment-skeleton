package rp.assignments.individual.ex2;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * This class gives an example of how to run all the tests for Individual
 * Exercise 2.
 * 
 * @author Nick Hawes
 *
 */

public class TestHarness {

	public static void main(String[] args) {

		JUnitCore core = new JUnitCore();
		// This line loads and runs the tests you have been provided with
		Result result = core.run(Ex2Tests.class);

		// This prints out the results
		System.out.println(String.format("%d/%d tests successful",
				result.getRunCount() - result.getFailureCount(),
				result.getRunCount()));
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

	}

}