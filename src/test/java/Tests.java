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
import org.json.JSONArray;
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
    //GitUtil.gitClone("assessment");
    //assertThat(GitUtil.buildRepo(), equalTo(true));

    GitUtil.gitClone("assess_error");
    boolean result = GitUtil.buildRepo();
    assertThat(result, equalTo(false));
  }


  /**
   * Tests if its works to set the status of a commit
   * First the status of the commit is set to success and verified and then to error
   * This is done by calling the api to get all statuses on the specific commit
   */
  @Test
  public void testSetStatus(){
    String sha = "37d1658db56342eafe6f7319f52ce64fd6c930a3";
    //Sets commit status to success
    try {
      GitUtil.setStatus(true, sha);
    } catch (Exception e){
      System.out.println(e.getMessage());
    }

    String GitUserName = "CheckStatusDummy";
    String GitToken = "";
    String url = "https://api.github.com/repos/JulienVig/DD2480-G20-A2/statuses/".concat(sha);

    SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
    HttpClient httpClient = new HttpClient(sslContextFactory);
    httpClient.setFollowRedirects(false);
    String state = "";

    //Gets the status of the latest commit
    try {
      httpClient.start();

      ContentResponse response = httpClient.GET(url);
      JSONArray jsonArray = new JSONArray(response.getContentAsString());
      JSONObject latestcommit = (JSONObject) jsonArray.get(0);
      state = latestcommit.getString("state");
      System.out.println(state);

      //System.out.println(response.getContentAsString());
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    assertThat(state, equalTo("success"));

    //Sets commit status to error
    try {
      GitUtil.setStatus(false, sha);
    } catch (Exception e){
      System.out.println(e.getMessage());
    }

    //Gets the status of the latest commit
    try {
      httpClient.start();

      ContentResponse response = httpClient.GET(url);
      JSONArray jsonArray = new JSONArray(response.getContentAsString());
      JSONObject latestcommit = (JSONObject) jsonArray.get(0);
      state = latestcommit.getString("state");
      System.out.println(state);
      assertThat(state, equalTo("error"));

      //System.out.println(response.getContentAsString());
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    assertThat(state, equalTo("error"));
  }
}
