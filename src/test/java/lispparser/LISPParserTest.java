package lispparser;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


import org.junit.Test;



public class LISPParserTest {
	
	private static final String LISP_CODE_BALANCE = "(defun queue-next (queue ptr) " + 
			"  Increment a queue pointer by 1 and wrap around if needed. " + 
			"  (let ((length (length (queue-elements queue))) " + 
			"        (try (the fixnum (1+ ptr)))) " + 
			"    (if (= try length) 0 try)))";
	
	private static final String LISP_CODE_NOT_NESTED = "(let ((length (length (queue-elements)( queue)))";
	private static final String LISP_CODE_NOT_CLOSED = "(let ((length (length (queue-elements( queue)))";
	private static final String LISP_CODE_START_CLOSING = ")(let ((length (length (queue-elements( queue)))";
	private static final char OPEN_CHAR = '(';
	private static final char CLOSE_CHAR = ')';

	@Test 
    public void testOk() {
		boolean result = LISPParser.isBalance(LISP_CODE_BALANCE,OPEN_CHAR,CLOSE_CHAR);
    	assertThat(result,is(true) );
    }
    
    @Test 
    public void testFails_Not_Nested() {
    	boolean result = LISPParser.isBalance(LISP_CODE_NOT_NESTED,OPEN_CHAR,CLOSE_CHAR);
    	assertThat(result,is(false) );
    }
	
    @Test 
    public void testFails_Not_Closed() {
    	boolean result = LISPParser.isBalance(LISP_CODE_NOT_CLOSED,OPEN_CHAR,CLOSE_CHAR);
    	assertThat(result,is(false) );
    }
    @Test 
    public void testFails_Start_Closing() {
    	boolean result = LISPParser.isBalance(LISP_CODE_START_CLOSING,OPEN_CHAR,CLOSE_CHAR);
    	assertThat(result,is(false) );
    }
}
