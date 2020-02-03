import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.client.api.AuthenticationStore;
import org.eclipse.jetty.client.util.BasicAuthentication;
import java.net.URI;

<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
=======
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
>>>>>>> e54edfe... Compile and Test assessment #7 #10 (#11)

import org.eclipse.jgit.api.*;
import org.gradle.tooling.*;
import java.util.concurrent.*;
import com.google.common.util.concurrent.*;
<<<<<<< HEAD

import java.util.stream.Collectors;
import org.json.JSONObject;



=======
>>>>>>> e54edfe... Compile and Test assessment #7 #10 (#11)

/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.

*/
public class CI extends AbstractHandler
{
    /**
     * The method called by the github webhook.
     * @param  target
     * @param  baseRequest
     * @param  request
     * @param  response
     * @throws IOException
     * @throws ServletException
     */
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        System.out.println(target);
        response.getWriter().println("CI job done");
        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        gitClone();


        response.getWriter().println("CI job done");

        JSONObject jsonobject = new JSONObject(request.getReader().lines().collect(Collectors.joining()));
        String sha = jsonobject.getJSONObject("head_commit").getString("id");

        Thread t = new Thread(new Runnable(){
          public void run() {
            GitUtil.gitClone("assess_error");
            boolean success = GitUtil.buildRepo();
            //Link to the email /commit status method here

            try {
              setStatus(success, sha);
            } catch (Exception e){
              System.out.println(e.getMessage());
            }
          }
        });
        t.start();
    }

    private static void setStatus(String url, boolean success) throws Exception {
      String GitUserName = "CheckStatusDummy";
      String GitToken = "f63a5ac303318960ad9b200c29571b95df01bbff";
      String url = "https://api.github.com/repos/JulienVig/DD2480-G20-A2/statuses/".concat(sha);
      SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
      HttpClient httpClient = new HttpClient(sslContextFactory);

      httpClient.setFollowRedirects(false);

      String jsonStr;
      if(success){
        jsonStr = "{\"state\": \"success\", \"description\": \"Build successful!\"}";
      } else {
        jsonStr = "{\"state\": \"err\", \"description\": \"Build failed!\"}";
      }

      try {
        httpClient.start();

        AuthenticationStore auth = httpClient.getAuthenticationStore();
        URI uri = URI.create(url);
        auth.addAuthenticationResult(new BasicAuthentication.BasicResult(uri, GitUserName, GitToken));

        ContentResponse response = httpClient.POST(url)
                .header(HttpHeader.CONTENT_TYPE, "application/json; charset=utf-8")
                .content(new StringContentProvider(jsonStr))
                .send();

        System.out.println(response.getContentAsString());

        return;
      } finally {
          httpClient.stop();
      }

    }


    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        setStatus("{'state': 'success', 'description': 'It works!', 'target_url': 'http://localhost'}", true);
        /*
        Server server = new Server(8020);
        server.setHandler(new CI());
        server.start();
        server.join();
        */
    }
}
