import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;

import org.eclipse.jgit.api.*;
import org.gradle.tooling.*;
import java.util.concurrent.*;
import com.google.common.util.concurrent.*;

/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.

*/
public class CI extends AbstractHandler
{

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

        Thread t = new Thread(new Runnable(){
          public void run() {
             gitClone();
             boolean success = buildRepo();
             //Link the notify and build history here
          }
        });
        t.start();
    }

    private static boolean buildRepo(){
      boolean success = true;
      ProjectConnection connection = GradleConnector.newConnector()
      .forProjectDirectory(new File("DD2480-G20-A2"))
      .connect();

      try {
         connection.newBuild().forTasks("build")
         .setStandardOutput(System.out).run();
      } catch(Exception e){
        //System.out.println("************************ Error -> "+e);
        success = false;
      }
      finally {
         connection.close();
         System.out.println(success);
      }
      return success;
    }

    private static void gitClone() {
      try{
      Git git = Git.cloneRepository()
      .setURI( "https://github.com/JulienVig/DD2480-G20-A2.git" )
      .call();
      git.checkout().setName( "origin/" + "assessment").call();
      } catch(Exception e) {
        System.err.println("Clone exception !!");
      }
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        // Thread t = new Thread(new Runnable(){
        //   public void run() {
        //      gitClone();
        //      boolean success = buildRepo();
        //      //Link the notify and build history here
        //   }
        // });
        // t.start();

        Server server = new Server(8020);
        server.setHandler(new CI());
        server.start();
        server.join();
    }
}
