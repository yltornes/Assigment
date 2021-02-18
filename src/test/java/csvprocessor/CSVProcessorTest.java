package csvprocessor;

import org.junit.Test;

import com.google.common.collect.Sets;

import static org.junit.Assert.*;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.Matchers.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVProcessorTest {
	
	private static final Path CSV_FILE = Paths.get("src/test/resources/enrollesTest.csv");
	
    @Test 
    public void testLoadFileHappyPath() {
    	List<Enrollee> result = CSVProcessor.loadFile(CSV_FILE);
    	assertThat(result, notNullValue());
    	assertThat(result.size(), equalTo(19));
    }
    
    @Test 
    public void testLoadFile_InvalidPath() {
    	List<Enrollee> result = CSVProcessor.loadFile(Paths.get("/InvalidPath"));
    	assertThat(result, notNullValue());
    	assertThat(result, hasSize(equalTo(0)));
    }
    
    @Test 
    public void testLoadFile_Add_HappyPath() {
    	Set<Enrollee> enrollees = Sets.newHashSet( //
    				Enrollee.factoryEnrollees("george,George,Cherokee Siox,1,Infinity"), //
    				Enrollee.factoryEnrollees("george5,George,Cherokee Siox,8,Infinity")
    			);
    	Enrollee newEnrolle = Enrollee.factoryEnrollees("george2,George,Cherokee Siox,1,Infinity");
    	CSVProcessor.add(enrollees, newEnrolle);
    	assertThat(enrollees, hasSize(equalTo(3)));
    }
    
    @Test 
    public void testLoadFile_Add_SameVersion() {
    	Set<Enrollee> enrollees = Sets.newHashSet( //
    				Enrollee.factoryEnrollees("george,George,Cherokee Siox,1,Infinity"), //
    				Enrollee.factoryEnrollees("george5,George,Cherokee Siox,8,Infinity")
    			);
    	Enrollee newEnrolle = Enrollee.factoryEnrollees("george5,George,Cherokee Siox,8,Infinity");
    	CSVProcessor.add(enrollees, newEnrolle);
    	assertThat(enrollees, hasSize(equalTo(2)));
    }
    
    @Test 
    public void testLoadFile_Add_SuperiorVersion() {
    	Enrollee remplasable = Enrollee.factoryEnrollees("george5,George,Cherokee Siox,8,Infinity");
    	
    	Set<Enrollee> enrollees = Sets.newHashSet( //
    				Enrollee.factoryEnrollees("george,George,Cherokee Siox,1,Infinity"), //
    				remplasable
    			);
    	Enrollee newEnrolle = Enrollee.factoryEnrollees("george5,George,Cherokee Siox,9,Infinity");
    	
    	CSVProcessor.add(enrollees, newEnrolle);
    	
    	assertThat(enrollees, hasSize(equalTo(2)));
    	assertThat(enrollees, hasItem(newEnrolle));
    	assertThat(enrollees, not(hasItem(remplasable)));
    }
    
    @Test 
    public void testLoadFile_Add_InferriorVersion() {
    	Enrollee remplasable = Enrollee.factoryEnrollees("george5,George,Cherokee Siox,8,Infinity");
    	
    	Set<Enrollee> enrollees = Sets.newHashSet( //
    				Enrollee.factoryEnrollees("george,George,Cherokee Siox,1,Infinity"), //
    				remplasable
    			);
    	Enrollee newEnrolle = Enrollee.factoryEnrollees("george5,George,Cherokee Siox,2,Infinity");
    	CSVProcessor.add(enrollees, newEnrolle);
    	assertThat(enrollees, hasSize(equalTo(2)));
    	assertThat(enrollees, hasItem(remplasable));
    	assertThat(enrollees, not(hasItem(newEnrolle)));
    }
    
    @Test 
    public void testSaveFile() {
    	Enrollee remplasable = Enrollee.factoryEnrollees("george5,George,Cherokee Siox,8,Infinity");
    	
    	Set<Enrollee> enrollees = Sets.newHashSet( //
    				Enrollee.factoryEnrollees("george,George,Cherokee Siox,1,Infinity"), //
    				remplasable
    			);
    	Enrollee newEnrolle = Enrollee.factoryEnrollees("george5,George,Cherokee Siox,2,Infinity");
    	CSVProcessor.add(enrollees, newEnrolle);
    	assertThat(enrollees, hasSize(equalTo(2)));
    	assertThat(enrollees, hasItem(remplasable));
    	assertThat(enrollees, not(hasItem(newEnrolle)));
    }
}