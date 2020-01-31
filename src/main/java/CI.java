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
     * The method called by the github webhook
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
             gitClone();
             boolean success = buildRepo();
             //Link to the email /commit status method here
          }
        });
        t.start();
    }

    /**
     * Compiles and tests the assessment branch
     * @return a boolean, the output of the build
     */
    public static boolean buildRepo(){
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

    /**
     * Creates a folder containing the branch assessment of the repo.
     */
    public static void gitClone() {
      try{
        deleteRepo("DD2480-G20-A2/");

        Git git = Git.cloneRepository()
        .setURI( "https://github.com/JulienVig/DD2480-G20-A2.git" )
        .call();
        git.checkout().setName( "origin/" + "assessment").call();
      } catch(Exception e) {
        System.err.println("Clone exception !! "+e);
      }
    }
    /**
     * Deletes recursively the named directory
     * @param  name        the name of the directory
     * @throws IOException
     */
    public static void deleteRepo(String name) throws IOException{
      Path directory = Paths.get(name);
      Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
           Files.deleteIfExists(file);
           return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
           Files.deleteIfExists(dir);
           return FileVisitResult.CONTINUE;
        }
      });
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Thread t = new Thread(new Runnable(){
          public void run() {
             gitClone();
             boolean success = buildRepo();
             //Link the notify and build history here
          }
        });
        t.start();

        // Server server = new Server(8020);
        // server.setHandler(new CI());
        // server.start();
        // server.join();
    }
}
