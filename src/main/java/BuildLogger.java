import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class BuildLogger{
	
	private File index;
	private String directoryPath;
	
	public BuildLogger(){
		File directory = new File("build_history");
        if (! directory.exists()) {
			directory.mkdir();
		}
		
		File file = new File("build_history\\index.html");
		try{
			if(!file.exists())
				file.createNewFile();
				this.index = file;
				this.directoryPath = this.index.getParentFile().getAbsolutePath();
				try{
					String header = "<h1><strong>Build log</strong></h1>\n";
					BufferedWriter w = new BufferedWriter(new FileWriter(this.index, true));
					w.write(header);
					w.close();
				}catch(Exception e){
					e.printStackTrace();
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void store(HttpServletRequest request){
		ParsedData payload = new ParsedData(request);
		createBuildFile(payload);	
		updateIndexFile(payload);
	}
	
	public void store(ParsedData payload){
		createBuildFile(payload);	
		updateIndexFile(payload);
	}
	
	private void updateIndexFile(ParsedData buildData){	
		try{
			String entry = "";
			if(buildData.success){
				entry = "<p><a href='"+this.directoryPath+"\\build_"+ buildData.id +".html'>build_"+ buildData.id+"</a>			"
							+buildData.timestamp+"		<i style=\"color: green\">SUCCESS</i></p>\n";
			}else{
				entry = "<p><a href='"+this.directoryPath+"\\build_"+ buildData.id +".html'>build_"+ buildData.id+"</a>			"
							+buildData.timestamp+"		<i style=\"color: red\">FAILURE</i></p>\n";
			}	
			BufferedWriter w = new BufferedWriter(new FileWriter(this.index, true));
			w.write(entry);
			w.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void createBuildFile(ParsedData buildData) {	
		String backButton = "<a href='"+this.directoryPath+"\\index.html'><strong>Back to index</strong></a>";
		String buildNumber = "<p> Build SHA: " + buildData.id + "<p>";
		String author = "<p> Author: " + buildData.name + "<p>";
		String email = "<p> Email: " + buildData.email + "<p>";
		String timestamp = "<p> Timestamp: " + buildData.timestamp + "<p>";
		String repo = "<p> Repository: " + buildData.repository + "<p>";
		String branch = "<p> Branch: " + buildData.branch + "<p>";
		String success;
		if(buildData.success){
			success = "<p>Build outcome: <i style=\"color: green\">SUCCESS</i></p>";
		}else{
			success = "<p>Build outcome: <i style=\"color: red\">FAILURE</i></p>";
		}

		List<String> lines = Arrays.asList(backButton,buildNumber, author, email, timestamp, repo, branch, success);

		Path file = Paths.get("build_history\\build_" + buildData.id + ".html");
		try {
			Files.write(file, lines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}