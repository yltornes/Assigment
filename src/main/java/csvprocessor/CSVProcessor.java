package csvprocessor;

import static java.util.logging.Level.SEVERE;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVProcessor {
	private static final Logger LOGGER = Logger.getLogger(CSVProcessor.class.getName());
	
	public static void main(String[] arg) {
		if(arg == null || arg.length < 2) {
			String msg = String.format("Source path and destination folder are required arguments.");
			System.out.println(msg);
			LOGGER.log(SEVERE, msg);
			return;
		}
		
		Path sourcePath =   Paths.get(arg[0]);
		if(sourcePath == null || Files.notExists(sourcePath)) {
			String msg = String.format("Source path do not exist. Path: %s", sourcePath);
			System.out.println(msg);
			LOGGER.log(SEVERE, msg);		
			return;
		}
		
		Path destinationPath =   Paths.get(arg[1]);
		if(sourcePath == null || Files.notExists(destinationPath)) {
			String msg = String.format("Destination path do not exist. Path: %s", destinationPath);
			System.out.println(msg);
			LOGGER.log(SEVERE, msg);		
			return;
		}
		List<Enrollee> enrollees = loadFile(sourcePath);
		Map<String, Set<Enrollee>> readyForOutoput = processEnrollees(enrollees);
		saveToFiles(destinationPath, readyForOutoput);
	}
   
	public static List<Enrollee> loadFile(Path source) {
        if(source == null || Files.notExists(source)) return new ArrayList<>();
        try (Stream<String> stream = Files.lines(source)) {
        	List<Enrollee> enrollees = new ArrayList<>();
            stream.forEach(l -> enrollees.add(Enrollee.factoryEnrollees(l)));
            return enrollees;
        } catch (IOException e) {
        	LOGGER.log(SEVERE, e.getMessage(), e);
        }
        return new ArrayList<>();
    }
	
	public static Map<String, Set<Enrollee>> processEnrollees(List<Enrollee> enrollees) {
		Map<String, Set<Enrollee>> companies = new HashMap<>();
		
		for(Enrollee enrollee : enrollees) {
			Set<Enrollee> company = companies.get(enrollee.getInsuranceCompany());
			if(company == null) {
				company = new HashSet<Enrollee>();
				company.add(enrollee);
				companies.put(enrollee.getInsuranceCompany(), company);
			} else {
				add(company, enrollee);
			}
		}
		return companies;
	}
	
	public static void add(Set<Enrollee> currentInsuranceCompany, Enrollee newEnrollee) {
		Optional<Enrollee> existEnrolle = currentInsuranceCompany.stream().filter(e -> e.equalsNotVersion(newEnrollee)).findFirst();
		if(existEnrolle.isPresent()) {
			if(newEnrollee.getVersion() > existEnrolle.get().getVersion()) {
				currentInsuranceCompany.remove(existEnrolle.get());
				currentInsuranceCompany.add(newEnrollee);
			}
		} else {
			currentInsuranceCompany.add(newEnrollee);
		}
	}
	
	public static void saveToFiles(Path destinationPath, Map<String, Set<Enrollee>> readyForOutoput) {
		Comparator<Enrollee> orderByLastAndFirstName = (enroll1, enroll2) -> {  //Comparator to sort by Insurance Company(Asc), UserID(Asc) and Version (Desc) 
			int result = enroll1.getLastName().compareTo(enroll2.getLastName());
			if (result == 0) {
				result = enroll1.getFirstName().compareTo(enroll2.getFirstName());
			};
			return result;
		};		
		for(String companyName : readyForOutoput.keySet()) {
			Path exitPath = Paths.get(destinationPath.toAbsolutePath() + File.separator +  companyName + "-" + LocalDate.now().toString() + ".csv");
			try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(exitPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE))) {
				readyForOutoput.get(companyName).stream().sorted(orderByLastAndFirstName).forEach(e -> pw.println(
						e.getUserId() + "," +
						e.getFirstName() + "," + 
						e.getLastName() + "," + 
						e.getVersion() + "," + 
						e.getInsuranceCompany()
				));
			} catch (IOException e) {
				String msg = String.format("Error creating files into the destination path.");
				System.out.println(msg);
				LOGGER.log(SEVERE, msg, e);
			}
		}
	}
}