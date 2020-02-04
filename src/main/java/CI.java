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

        Thread t = new Thread(new Runnable(){
          public void run() {
             GitUtil.gitClone("assess_error");
             boolean success = GitUtil.buildRepo();
             //Link to the email /commit status method here
          }
        });
        t.start();
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        // Thread t = new Thread(new Runnable(){
        //   public void run() {
        //      GitUtil.gitClone("assess_error");
        //      boolean success = GitUtil.buildRepo();
        //      //Link the notify and build history here
        //   }
        // });
        // t.start();
		
		/*
		//Use this for testing build storage
		BuildLogger log = new BuildLogger();
		
		ParsedData bogey = new ParsedData();
		bogey.repository = "www.github.org/examplerepo/";
		bogey.branch = "www.github.org/examplerepo/branch/";
		bogey.id = "1hx937dhsk29d491ne49jz1mx109k";
		bogey.timestamp ="2020-02-04:16:41";
		bogey.name = "Filip Hedlund";
		bogey.email = "filhed97@gmail.com";
		bogey.success = true;
		log.store(bogey);
		
		ParsedData bogey2 = new ParsedData();
		bogey2.repository = "www.github.org/examplerepo/";
		bogey2.branch = "www.github.org/examplerepo/branch/";
		bogey2.id = "a109hx29zwq1ne49317dhskjz1mxh";
		bogey2.timestamp ="2020-02-04:17:51";
		bogey2.name = "Filip Hedlund";
		bogey2.email = "filhed97@gmail.com";
		bogey2.success = false;
		log.store(bogey2);
		*/
	
        Server server = new Server(8020);
        server.setHandler(new CI());
        server.start();
        server.join();
		
		
    }
}
