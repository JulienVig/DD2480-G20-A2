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
  @Ignore                                               //Junit ignores this test
  @Test
  public void TestGitClone() {
    GitUtil.gitClone("assessment");
    assertThat(repo.exists(),equalTo(true));
  }

  /**
   * Tests if build compiles and tests the repository
   *
   */
  @Ignore                                               //Junit ignores this test
  @Test
  public void TestBuildRepo(){
    GitUtil.gitClone("assessment");
    assertThat(GitUtil.buildRepo(), equalTo(true));

    GitUtil.gitClone("assess_error");
    boolean result = GitUtil.buildRepo();
    assertThat(result, equalTo(false));
  }
}
