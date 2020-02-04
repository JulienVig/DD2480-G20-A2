import javax.servlet.http.HttpServletRequest;
import org.json.*;
import java.util.stream.Collectors;

/**
 *	This class receives a servlet request from a webhook and parses the relevant build
 *	info which we wish to store in a log.
 * @param repository		The repository which generated the webhook.
 * @param branch			The branch which was pushed.
 * @param id					The SHA which identifies the commit.
 * @param timestamp		The time when the webhook(/build) was generated.
 * @param name 			Name of the github user which pushed the commit.
 * @param email				E-mail of the github user.
 * @param success			Boolean value stating whether the build was a success(true) or failure(false). 
 */
public class ParsedData{
	
	public String repository;
	public String branch;
	public String id;
    public String timestamp;
	public String name;
	public String email;
	
	public boolean success;		//Passed to this class outside of the constructor.
			
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
	
	//constructor for passing bogey values during testing
	public ParsedData(){
	}
		
}