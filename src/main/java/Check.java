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

/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.

*/


public class Check{



  public static boolean get() {
     gitClone();
     boolean success = buildRepo();
     System.out.println("************************ Build output -> "+success);
     return success;
  }


    private static boolean buildRepo(){
      boolean success = true;
      ProjectConnection connection = GradleConnector.newConnector()
      .forProjectDirectory(new File("DD2480-G20-Assignment1"))
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
      }
      return success;
    }

    private static void runRepo(){
      ProjectConnection connection = GradleConnector.newConnector()
      .forProjectDirectory(new File("DD2480-G20-Assignment1"))
      .connect();

      try {
         connection.newBuild().forTasks("run")
         .setStandardOutput(System.out).run();
      } finally {
         connection.close();
      }
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
}
