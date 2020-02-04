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
public class GitUtil {

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
        System.out.println("************************ Error -> "+e);
        success = false;
      }
      finally {
         connection.close();
         System.out.println("Build output: "+success);
      }
      return success;
    }

    /**
     * Creates a folder containing the branch assessment of the repo.
     */
    public static void gitClone(String branch) {
      try{
        String repo = "DD2480-G20-A2/";
        if(new File(repo).exists()) deleteRepo(repo);

        Git git = Git.cloneRepository()
        .setURI( "https://github.com/JulienVig/DD2480-G20-A2.git" )
        .setBranchesToClone( singleton( "refs/heads/"+branch ) );
        .setBranch( "refs/heads/"+branch )
        .call();
        //git.checkout().setName( "origin/" + branch).call();
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
}
