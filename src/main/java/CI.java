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

import org.eclipse.jgit.api.*;

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

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        gitClone();

        response.getWriter().println("CI job done");
    }

    private static void gitClone() {
      try{
      Git git = Git.cloneRepository()
      .setURI( "https://github.com/filhed97/DD2480-G20-Assignment1.git" )
      .call();
      git.checkout().setName( "origin/" + "gradle").call();
      } catch(Exception e) {
        System.err.println("Clone exception !!");
      }
    }


    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {

        Server server = new Server(8020);
        server.setHandler(new CI());
        server.start();
        server.join();
    }
}