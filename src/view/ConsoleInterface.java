package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ConsoleInterface {
	private static final String DEFAULT_LOG_FILENAME = "log.txt";
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private static PrintWriter out;
	
	static {
		try {
			out = new PrintWriter(DEFAULT_LOG_FILENAME);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void log(Object message) {
		System.out.println(message);
		out.println(message.toString());
		out.flush();
	}
	
	public static String read(Object message) {
		System.out.println(message);
		out.println(message.toString());
		out.flush();
		String result = null;
		try {
			result = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
