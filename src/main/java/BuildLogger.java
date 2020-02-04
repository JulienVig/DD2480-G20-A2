import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class BuildLogger{
	
	public ParsedData store(HttpServletRequest request){
		
		ParsedData payload = new ParsedData(request);
		return payload;
		
	}

	public void createBuildFile(ParsedData buildData) {
		String buildNumber = "<p> Build Number: " + buildData.id + "<p>";
		String author = "<p> Author: " + buildData.name + "<p>";
		String email = "<p> Email: " + buildData.email + "<p>";
		String timestamp = "<p> Timestamp: " + buildData.timestamp + "<p>";
		String repo = "<p> Repository: " + buildData.repository + "<p>";
		String branch = "<p> Branch: " + buildData.branch + "<p>";

		List<String> lines = Arrays.asList(buildNumber, author, email, timestamp, repo, branch);

		File directory = new File("build_history");
        if (! directory.exists()) {
			directory.mkdir();
		}

		Path file = Paths.get("build_history/build" + buildData.id + ".html");
		try {
			Files.write(file, lines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}