package benchmark.testdriver.custom;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OutputWriter {

	private ArrayList<String> lines = new ArrayList<String>();
	
	private String fileName = "unknown.txt";
	
	public OutputWriter(String fname, ArrayList<String> data) {
		this.lines = new ArrayList<String>();
		this.lines.addAll(data);
		
		this.fileName = fname;
		// make sure the file exists and is empty
		File f = new File(this.fileName);
		// try to create new file
		try {
			f.createNewFile();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	public void writeLines() throws IOException {
		PrintWriter f0 = new PrintWriter(new FileWriter(fileName));
		// write to file
		for (int i = 0; i < lines.size(); i++) {
		    f0.println(lines.get(i));
		}
		// close the stream
		f0.close();
	}
	
	
	/**
	 * Generates a unique version of a file by adding date/time info to its name
	 * @param fn The file name which should become unique (e.g. summary.csv)
	 * 
	 * @return A unique version of the given filename
	 * (e.g. summary_20151001155919.csv) by using the 
	 * yyyyMMddHHmmss date-time format
	 */
	public static String makeUnique(String fn) {
		
		String[] tokens = fn.split("\\.");
		
		String suffix = tokens[tokens.length - 1];
		String name = "";
		
		for (int i = 0; i < tokens.length-1; i++) {
			name += tokens[i] + ".";
		}
		name = name.substring(0, name.length() - 1);
		
		
		String dtimeFormat = "yyyyMMddHHmmss";
		DateFormat dateFormat = new SimpleDateFormat(dtimeFormat);
		Date date = new Date();
		String dateStr = dateFormat.format(date);
		
		/*
		System.out.println("suffix=" + suffix);
		System.out.println("name=" + name);
		System.out.println("dateTime=" + dateStr);
		*/
		
		String fn_new = name + "_" + dateStr + "." + suffix;

		return fn_new;
	}
	
	
	
	
	public static void main(String[] args) {
		/*
		String name = "c/benutzer/max/data/123/summary.1.2.3.csv";
		String newName = OutputWriter.makeUnique(name);
		System.out.println("\n\tnewName=" + newName);
		*/
		/*
		String path = "data/assignment1/summary_123555.csv";
		File f = new File(path);
		System.out.println(f.exists());
		try {
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		*/
	}
	
	
	
	
}
