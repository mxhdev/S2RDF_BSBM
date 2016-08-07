package benchmark.testdriver.custom;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

public class BSBMTranslateHelper {

	
	
	public static final String FP_PREFIX = "fp=";
	public static final boolean INCLUDE_WARMUPS = false;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmssSSSSSSSSSS");
	public static int pos = 1;
	
	
	/**
	 * Index list for the qmToQt function to access
	 * idxList[i] corresponds to the type id of the query in the
	 * query mix at position (i + 1)
	 */
	private static final int[] idxList = new int[]{1, 2, 2, 3, 2, 2, 4, 2, 2, 5, 7, 7, 5, 7, 7, 8, 9, 9, 8, 9, 9, 10, 10, 11, 12};
	
	/**
	 * List of some common keywords used as identifiers
	 * @see http://www.cloudera.com/content/www/en-us/documentation/enterprise/latest/topics/impala_identifiers.html
	 * @see https://cwiki.apache.org/confluence/display/Hive/LanguageManual+DDL
	 */
	private static final String[] keywords = new String[]{"date", "comment", "data"};
	
	
	
	/**
	 * Converts the id of a query inside a query mix to a id
	 * of the query type. This function works only for
	 * query mixes of the Explore Use-Case
	 * @param qmId A value between 1 - 25
	 * @return A value between 1 - 12
	 * @see http://wifo5-03.informatik.uni-mannheim.de/bizer/berlinsparqlbenchmark/spec/ExploreUseCase/index.html#querymixes
	 */
	public static int qmToQt(int qmId) {
		if ((qmId > 0) && (qmId <= idxList.length)) {
			// range is correct
			return idxList[qmId - 1];
		} else {
			// range is not correct
			return -1;
		}
		
	}
	
	
	/**
	 * Adds backticks to some common identifier names.
	 * It is recommended to use this function inside the 
	 * Translator.translateQuery method just before writing the SQL code 
	 * to the corresponding output file
	 * @see http://www.cloudera.com/content/www/en-us/documentation/enterprise/latest/topics/impala_identifiers.html
	 * @see https://cwiki.apache.org/confluence/display/Hive/LanguageManual+DDL
	 * @param sql The SQL code which should be escaped
	 * @return Escaped SQL code
	 */
	public static String escapeKeywords(String sql) {
		for (String kw : keywords) {
			// can either end with a space or with a comma
			// Impala & Hive: Identifiers are case-insensitive, so
			// they can also be matched as such
			// All identifiers which are found will become lower-case
			sql = sql.replaceAll("(?i)\\." + kw + ",", ".`" + kw + "`,");
			sql = sql.replaceAll("(?i)\\." + kw + " ", ".`" + kw + "` ");
		}
		return sql;
	}
	
	
	// EDIT Timo & Max Start
	// BSBMTranslateHelper.storeQuery(this.serviceURL, runNr, queryNr, queryString);
	
	public static void storeQuery(Logger logger, String endpoint, int runNr, int queryNr, String query) {
		// file path must end with /
		if (!endpoint.endsWith("/")) endpoint += "/";
		
		// escape the fp prefix
		if (endpoint.startsWith(FP_PREFIX))
			endpoint = endpoint.substring(FP_PREFIX.length());
		
		File newFile = new File(endpoint);
		if(!newFile.exists()){
			newFile.mkdirs();
		}
		
		/*
		// convert position in mix to type index
		int queryType = qmToQt(queryNr);
		
		String path = endpoint.substring(3) + "sparql_qm" + runNr + "_q" + queryNr + "_t" + queryType  + ".txt";
		*/
		
		Date time = new Date(System.nanoTime());
		String timeInString = sdf.format(time);
		
		
		String path = endpoint + "sparql_" + timeInString + "_qm" + runNr + "_q" + queryNr + "_pos" + Integer.toString(pos) + "__VP_SO-OS-SS-VP.txt";
		
		pos = pos + 1;
		
		ArrayList<String> contents = new ArrayList<String>();
		contents.add(query);
		OutputWriter writer = new OutputWriter(path, contents);
		try {
			writer.writeLines();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	// EDIT Timo & Max End
	
	
	public static void main(String[] args) {
		
		String tester = "SELECT OPTIONAL1.data, OPTIONAL2.database, OPTIONAL3.comment, OPTIONAL3.commentLine, OPTIONAL4.DATe, OPTIONAL4.datetime FROM xy WHERE TEST";
		System.out.println("INPUT: \n\t" + tester);

		tester = BSBMTranslateHelper.escapeKeywords(tester);
		
		System.out.println("OUTPUT: \n\t" + tester);
		
		
	}
	
}
