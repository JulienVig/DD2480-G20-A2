import javax.servlet.http.HttpServletRequest;
import org.json.*;
import java.util.stream.Collectors;

public class ParsedData{
	
	public String repository;
	public String branch;
	public String id;
    public String timestamp;
	public String name;
	public String email;
			
	public ParsedData(HttpServletRequest request){
		try{
			String payload = request.getReader().lines().collect(Collectors.joining());
		JSONObject data = new JSONObject(payload);
		JSONArray arr = data.getJSONArray("commits");
		
		this.repository = data.getJSONObject("repository").getString("html_url");
		this.branch = data.getString("ref");
		this.id = arr.getJSONObject(0).getString("id");
		this.timestamp = arr.getJSONObject(0).getString("timestamp");
		this.name = arr.getJSONObject(0).getJSONObject("author").getString("name");
		this.email = arr.getJSONObject(0).getJSONObject("author").getString("email");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
		
}