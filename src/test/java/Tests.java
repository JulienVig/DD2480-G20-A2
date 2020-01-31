import static org.junit.Assert.*;
import org.junit.Test;
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

  /**
   * Tests if effectively delete file
   * Creates a directory and checks that deleteRepo()
   * deletes it.
   */
  @Test
  public void deleteRepo() throws IOException{
    try {
      File f = new File("test_delete");
      boolean created = f.mkdir();
      System.out.print("Folder to delte created? "+created);

      CI.deleteRepo("test_delete");
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
  @Test
  public void gitClone() {
    File f = new File("DD2480-G20-A2");
    CI.gitClone();
    assertThat(f.exists(),equalTo(true));
  }
}
