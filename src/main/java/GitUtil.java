import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
<<<<<<< HEAD
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.client.api.AuthenticationStore;
import org.eclipse.jetty.client.util.BasicAuthentication;
import java.net.URI;
=======
>>>>>>> 3409424... Create GitUtil class #10

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
<<<<<<< HEAD

        System.out.println("************************ Error -> "+e);
=======
        //System.out.println("************************ Error -> "+e);
>>>>>>> 3409424... Create GitUtil class #10
        success = false;
      }
      finally {
         connection.close();
<<<<<<< HEAD
         System.out.println("Build output: "+success);
=======
         System.out.println(success);
>>>>>>> 3409424... Create GitUtil class #10
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
        .call();
        git.checkout().setName( "origin/" + branch).call();
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
<<<<<<< HEAD

    /* This function taking in a sha of a commit and a boolean
    * The boolean value represents if that specific commit lead to a succesful build or not
    * The commit status is then updated on github by using their API
    * A dummy account is used for this purpose
    */
    public static void setStatus(boolean success, String sha) throws Exception {
      String GitUserName = "CheckStatusDummy";
      String GitToken = "44eac86cd8f6efbd5cb7f8dd26c301798464bc80";
      String url = "https://api.github.com/repos/JulienVig/DD2480-G20-A2/statuses/".concat(sha);

      SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
      HttpClient httpClient = new HttpClient(sslContextFactory);

      httpClient.setFollowRedirects(false);

      String jsonStr;
      if(success){
        jsonStr = "{\"state\": \"success\", \"description\": \"Build successful!\"}";
      } else {
        jsonStr = "{\"state\": \"error\", \"description\": \"Build failed!\"}";
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

        //System.out.println(response.getContentAsString());

        return;
      } finally {
          httpClient.stop();
      }


    }
=======
>>>>>>> 3409424... Create GitUtil class #10
}
