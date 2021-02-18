package lispparser;

import static java.util.logging.Level.SEVERE;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;



public class LISPParser {
	private static final Logger LOGGER = Logger.getLogger(LISPParser.class.getName());

	public LISPParser() {
		
	};
	
	public static void main(String[] arg) {
		if(arg == null || arg.length < 3) {
			String msg = String.format("String to parser, open and close characters are required arguments.");
			System.out.println(msg);
			LOGGER.log(SEVERE, msg);
			return;
		}
		
		String parserString = arg[0]; 
		char openChar = arg[1].charAt(0);
		char closeChar = arg[2].charAt(0);
		
		if (isBalance(parserString, openChar, closeChar)) 
			System.out.println("The " + openChar+ " "+ closeChar +" are properly closed and nested in LISP code.");
		else 
			System.out.println("The " + openChar+ " "+ closeChar +" are not properly closed in LISP code.");
		
	}
	
	
	public static boolean isBalance(String str, char openChar, char closeChar) {
		if (str == null) {
			throw new NullPointerException("String null");
		}else {
			char[] string = str.toCharArray();
			Stack<Character> stack  = new Stack<Character>();
			int lengthS = string.length;
			char stackPop;
			int i =0;
			boolean isTillBalanced = true;
			while(i<lengthS && isTillBalanced) { // until is not Balanced or end of the string
				if(string[i] == openChar ) stack.push(string[i]);
				else if (string[i] == closeChar) {
					isTillBalanced = ! stack.isEmpty();
					if (isTillBalanced) { 
						stackPop = stack.pop();
						isTillBalanced =  stackPop == openChar;
					} 
				}
				i++;		
			}		
			return isTillBalanced && stack.empty();
		}	
	}

}
