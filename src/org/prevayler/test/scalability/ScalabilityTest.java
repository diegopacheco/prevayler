// Prevayler(TM) - The Open-Source Prevalence Layer.
// Copyright (C) 2001 Klaus Wuestefeld.
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License version 2.1 as published by the Free Software Foundation. This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.

package org.prevayler.test.scalability;

import org.prevayler.test.scalability.prevayler.*;
import org.prevayler.test.scalability.jdbc.*;

import java.io.*;
import java.util.*;

public class ScalabilityTest {

	static private final Properties properties = new Properties();


	static public void main(String[] args) {

		out("\n=============================================================");
		out(  "             Prevayler vs JDBC Scalability Tests             ");
		out(  "=============================================================\n");
		out("If you have any trouble running the tests, just write to");
		out("prevayler-scalability@lists.sourceforge.net and we will be glad to help.\n");

		try {
			out("Reading the properties file:\n" + propertiesFile().getAbsolutePath());
			out("You can edit this file to configure the tests for the next run.\n");

			properties.load(new FileInputStream(propertiesFile()));

			if (isPrevaylerQueryChosen()) runPrevaylerQuery();
			if (isPrevaylerTransactionChosen()) runPrevaylerTransaction();
			if (isJdbcQueryChosen()) runJdbcQuery();
			if (isJdbcTransactionChosen()) runJdbcTransaction();

			out("\n\n\nFor better results, edit the properties file:");
			out(propertiesFile().getAbsolutePath());
			out("\nYou can publish your best results by mail to:");
			out("prevayler-scalability@lists.sourceforge.net. Please include info about your");
			out("processors (quantity, type, speed), compiler, VM, operating system and DBMS.");
			out("");
			out("Scalability test results are published on www.prevayler.org.");
			out("See you there.\n");
			out("Klaus Wuestefeld and Daniel Santos.\n\n");

		} catch (Exception ex) {
			ex.printStackTrace();
		} catch (OutOfMemoryError err) {
			ScalabilityTestRun.outOfMemory();
		}

	}


	static private void runPrevaylerQuery() throws Exception {
		new QueryTestRun(
			new PrevaylerQuerySubject(),
			numberOfObjects(),
			prevaylerQueryThreadsMin(),
			prevaylerQueryThreadsMax()
		);
	}

	static private void runPrevaylerTransaction() throws Exception {
		new TransactionTestRun(
			new PrevaylerTransactionSubject(prevaylerTransactionLogDirectory(), prevaylerTransactionLogs()),
			numberOfObjects(),
			prevaylerTransactionThreadsMin(),
			prevaylerTransactionThreadsMax()
		);
	}

	static private void runJdbcQuery() {
		new QueryTestRun(
			new JDBCQuerySubject(jdbcDriverClassName(), jdbcConnectionURL(), jdbcUser(), jdbcPassword()),
			numberOfObjects(),
			jdbcQueryThreadsMin(),
			jdbcQueryThreadsMax()
		);
	}

	static private void runJdbcTransaction() {
		new TransactionTestRun(
			new JDBCTransactionSubject(jdbcDriverClassName(), jdbcConnectionURL(), jdbcUser(), jdbcPassword()),
			numberOfObjects(),
			jdbcTransactionThreadsMin(),
			jdbcTransactionThreadsMax()
		);
	}


	static private File propertiesFile() throws IOException {
		File result = new File("ScalabilityTestProperties.txt");
		if (!result.exists()) {
			out("Creating the properties file.");
			createPropertiesFile(result);
		}
		return result;
	}

	static private void createPropertiesFile(File file) throws IOException {
		PrintStream stream = new PrintStream(new FileOutputStream(file));
		stream.println(
			"###########################################################\n" +
			"#                                                         #\n" +
			"#      PREVAYLER VS JDBC SCALABILITY TEST PROPERTIES      #\n" +
			"#                                                         #\n" +
			"###########################################################\n" +
			"\n" +
			"NumberOfObjects = ONE_HUNDRED_THOUSAND\n" +
			"# NumberOfObjects = ONE_MILLION\n" +
			"# NumberOfObjects = TEN_MILLION\n" +
			"# NumberOfObjects = TWENTY_MILLION\n" +
			"#\n" +
			"# The results are only valid if both Prevayler and the\n" +
			"# database can run the tests without paging memory to disk.\n" +
			"#\n" +
			"# Running the tests with one hundred thousand objects\n" +
			"# (default option) requires approx. 64MB free RAM.\n" +
			"# Running the tests with one million objects requires\n" +
			"# approx. 470MB free RAM. The VM must be started with a\n" +
			"# sufficient maximum heap size or you will get an\n" +
			"# OutOfMemoryError.\n" +
			"# Example for Linux and Windows:  java -Xmx512000000 ...\n" +
			"#\n" +
			"# Running the tests with ten million objects requires\n" +
			"# approx. 4.7GB free RAM and a 64bit VM.\n" +
			"#\n" +
			"# IMPORTANT: Remember to shutdown all other non-vital\n" +
			"# processes before running the tests. Even the database\n" +
			"# process should be down while running the Prevayler tests\n" +
			"# that do not use it.\n" +
			"\n" +	
			"\n" +	
			"###########################################################\n" +
			"# PREVAYLER QUERY TEST\n" +
			"\n" +
			"RunPrevaylerQueryTest = YES\n" +
			"# RunPrevaylerQueryTest = NO\n" +
			"\n" +
			"PrevaylerQueryThreadsMinimum = 1\n" +
			"PrevaylerQueryThreadsMaximum = 5\n" +
			"# More threads can produce better results on\n" +
			"# multi-processor machines.\n" +
			"\n" +
			"\n" +
			"###########################################################\n" +
			"# PREVAYLER TRANSACTION TEST\n" +
			"\n" +
			"RunPrevaylerTransactionTest = YES\n" +
			"# RunPrevaylerTransactionTest = NO\n" +
			"\n" +
			"PrevaylerTransactionThreadsMinimum = 1\n" +
			"PrevaylerTransactionThreadsMaximum = 5\n" +
			"# More threads can produce better results on machines with\n" +
			"# multiple disks.\n" +
			"\n" +
			"TransactionLogDirectory = TransactionTest\n" +
			"#\n" +
			"# The full path name can be used. Example for Windows:\n" +
			"# TransactionLogDirectory1 = c:\\\\temp\\\\TransactionTest\n" +
			"# The back-slash (\\) is the escape character so you must\n" +
			"# use two back-slashes (\\\\).\n" +
			"\n" +
			"\n" +
			"###########################################################\n" +
			"# JDBC QUERY TEST\n" +
			"\n" +
			"RunJdbcQueryTest = NO\n" +
			"# RunJdbcQueryTest = YES\n" +
			"\n" +
			"JdbcQueryThreadsMinimum = 1\n" +
			"JdbcQueryThreadsMaximum = 5\n" +
			"# More threads can produce better results on some machines.\n" +
			"\n" +
			"\n" +
			"###########################################################\n" +
			"# JDBC TRANSACTION TEST\n" +
			"\n" +
			"RunJdbcTransactionTest = NO\n" +
			"# RunJdbcTransactionTest = YES\n" +
			"\n" +
			"JdbcTransactionThreadsMinimum = 1\n" +
			"JdbcTransactionThreadsMaximum = 5\n" +
			"# More threads can produce better results on some machines.\n" +
			"\n" +
			"\n" +
			"###########################################################\n" +
			"# JDBC CONNECTION\n" +
			"# (necessary to run the JDBC tests)\n" +
			"\n" +
			"JdbcDriverClassName =\n" +
			"JdbcConnectionURL =\n" +
			"JdbcUser =\n" +
			"JdbcPassword =\n" +
			"# These two tables are necessary for the JDBC tests:\n" +
			"# QUERY_TEST and TRANSACTION_TEST.\n" +
			"# Both tables have the same column structure:\n" +
			"#    ID DECIMAL,\n" +
			"#    NAME VARCHAR2(8),\n" +
			"#    STRING1 VARCHAR2(1000),\n" +
			"#    BIGDECIMAL1 DECIMAL,\n" +
			"#    BIGDECIMAL2 DECIMAL,\n" +
			"#    DATE1 DATE,\n" +
			"#    DATE2 DATE.\n" +
			"\n" +
			"# IMPORTANT: For best results, create indices on the\n" +
			"# QUERY_TEST.NAME and TRANSACTION_TEST.ID columns.\n" +
			"# Do not create indices on any other column.\n"
		);
	}


	static private int numberOfObjects() {
		String property = property("NumberOfObjects");
		if ("ONE_HUNDRED_THOUSAND".equals(property)) return   100000;
		if ("ONE_MILLION"         .equals(property)) return  1000000;
		if ("TEN_MILLION"         .equals(property)) return 10000000;
		if ("TWENTY_MILLION"      .equals(property)) return 20000000;
		throw new RuntimeException("NumberOfObjects property must be equal to ONE_HUNDRED_THOUSAND, ONE_MILLION, TEN_MILLION or TWENTY_MILLION.");
	}

	static private boolean isPrevaylerQueryChosen() {
		return booleanProperty("RunPrevaylerQueryTest");
	}

	static private int prevaylerQueryThreadsMin() {
		return intProperty("PrevaylerQueryThreadsMinimum");
	}

	static private int prevaylerQueryThreadsMax() {
		return intProperty("PrevaylerQueryThreadsMaximum");
	}


	static private boolean isPrevaylerTransactionChosen() {
		return booleanProperty("RunPrevaylerTransactionTest");
	}

	static private int prevaylerTransactionThreadsMin() {
		return intProperty("PrevaylerTransactionThreadsMinimum");
	}

	static private int prevaylerTransactionThreadsMax() {
		return intProperty("PrevaylerTransactionThreadsMaximum");
	}

	static private String prevaylerTransactionLogDirectory() {
		String result = property("TransactionLogDirectory");
		out("\n\nPrevayler TransactionLog Directory: " + result);
		return result;
	}

	static private int prevaylerTransactionLogs() {
		return prevaylerTransactionThreadsMax();   // A log file for each thread.
	}


	static private boolean isJdbcQueryChosen() {
		return booleanProperty("RunJdbcQueryTest");
	}

	static private int jdbcQueryThreadsMin() {
		return intProperty("JdbcQueryThreadsMinimum");
	}

	static private int jdbcQueryThreadsMax() {
		return intProperty("JdbcQueryThreadsMaximum");
	}


	static private boolean isJdbcTransactionChosen() {
		return booleanProperty("RunJdbcTransactionTest");
	}

	static private int jdbcTransactionThreadsMin() {
		return intProperty("JdbcTransactionThreadsMinimum");
	}

	static private int jdbcTransactionThreadsMax() {
		return intProperty("JdbcTransactionThreadsMaximum");
	}


	static private String jdbcDriverClassName() {
		return property("JdbcDriverClassName");
	}

	static private String jdbcConnectionURL() {
		return property("JdbcConnectionURL");
	}

	static private String jdbcUser() {
		return property("JdbcUser");
	}

	static private String jdbcPassword() {
		return property("JdbcPassword");
	}


	static private String property(String name) {
		String result = properties.getProperty(name);
		if (result == null) throw new RuntimeException("Property " + name + " not found.");
		return result;
	}

	static private int intProperty(String name) {
		try {
			return Integer.valueOf(property(name)).intValue();
		} catch (NumberFormatException nfx) {
			out("NumberFormatException reading property " + name);
			throw nfx;
		}
	}

	static private boolean booleanProperty(String name) {
		boolean result = "yes".equalsIgnoreCase(property(name));
		if (result) return true;
		out("\n\n\n" + name + " property is set to " + property(name) + ".");
		out("This test will be skipped (see properties file).");
		return false;
	}


	static private void out(Object message) {
		System.out.println(message);
	}
}
