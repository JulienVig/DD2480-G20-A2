import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;

import org.eclipse.jgit.api.*;
import org.gradle.tooling.*;
import java.util.concurrent.*;
import com.google.common.util.concurrent.*;

import java.util.stream.Collectors;
import org.json.JSONObject;




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
              GitUtil.setStatus(success, sha);
            } catch (Exception e){
              System.out.println(e.getMessage());
            }
          }
        });
        t.start();
    }



    private static boolean buildRepo(){
      boolean success = true;
      ProjectConnection connection = GradleConnector.newConnector()
      .forProjectDirectory(new File("DD2480-G20-A2"))
      .connect();

        Check c = new Check();
        Thread t = new Thread(c);
        Thread t = new Thread(new Runnable(){
          public void run() {
             GitUtil.gitClone("assessment");
             boolean success = GitUtil.buildRepo();
             //Link to the email /commit status method here
          }
        });
        t.start();
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
<<<<<<< HEAD
        Server server = new Server(8020);
        server.setHandler(new CI());
        server.start();
        server.join();
=======
        Thread t = new Thread(new Runnable(){
          public void run() {
             GitUtil.gitClone("assessment");
             boolean success = GitUtil.buildRepo();
             //Link the notify and build history here
          }
        });
        t.start();

        // Server server = new Server(8020);
        // server.setHandler(new CI());
        // server.start();
        // server.join();
>>>>>>> 232945d... Add unit tests #10
    }
}
