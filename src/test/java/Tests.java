import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

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
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.client.api.AuthenticationStore;
import org.eclipse.jetty.client.util.BasicAuthentication;
import java.net.URI;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Tests {
   private File repo = new File("DD2480-G20-A2");
  /**
   * Tests if effectively delete file
   * Creates a directory and checks that deleteRepo()
   * deletes it.
   */

  @Test
  public void TestDeleteRepo() throws IOException{
    try {
      File f = new File("test_delete");
      boolean created = f.mkdir();
      System.out.print("Folder to delte created? "+created);

      GitUtil.deleteRepo("test_delete");
      assertThat(f.exists(), equalTo(false));
      } catch(Exception e) {
         // if any error occurs
         e.printStackTrace();
      }
  }
  /**
   * Tests if effectively clone the repository
   * Clone the repo and checks if the folder exits.
   */
  //@Ignore                                               //Junit ignores this test
  @Test
  public void TestGitClone() {
    GitUtil.gitClone("assessment");
    assertThat(repo.exists(),equalTo(true));
  }

  /**
   * Tests if build compiles and tests the repository
   *
   */
  //@Ignore                                               //Junit ignores this test
  @Test
  public void TestBuildRepo(){
    GitUtil.gitClone("assessment");
    assertThat(GitUtil.buildRepo(), equalTo(true));

    GitUtil.gitClone("assess_error");
    boolean result = GitUtil.buildRepo();
    assertThat(result, equalTo(false));
  }

  /*
  @Test
  public void testSetStatus(){
    String sha = "b5b0d6fe6beec006a8909fc3ced4bb89deb6715f";
    try {   
      GitUtil.setStatus(true, sha);
    } catch (Exception e){
      System.out.println(e.getMessage());
    }
    
    String GitUserName = "CheckStatusDummy";
    String GitToken = "f63a5ac303318960ad9b200c29571b95df01bbff";
    String url = "https://api.github.com/repos/JulienVig/DD2480-G20-A2/statuses/".concat(sha);
    
    SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
    HttpClient httpClient = new HttpClient(sslContextFactory);
    
    httpClient.setFollowRedirects(false);

    
    try {
      httpClient.start();

      AuthenticationStore auth = httpClient.getAuthenticationStore();
      URI uri = URI.create(url);
      auth.addAuthenticationResult(new BasicAuthentication.BasicResult(uri, GitUserName, GitToken));

      ContentResponse response = httpClient.GET(url);
      String jsonStr = response.getContentAsString();
      System.out.println(jsonStr);
      assertThat(jsonStr, equalTo("test"));

      System.out.println(response.getContentAsString());

      return;
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    
  }
  */
}
