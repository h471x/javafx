import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("deprecation")
public class Setup {
  public static void main(String[] args) {
    String os = System.getProperty("os.name").toLowerCase();
    String osVersion = System.getProperty("os.version");
    String osArch = System.getProperty("os.arch");

    // Suppressed println statements for cleaner output
    // System.out.println("Operating System: " + os);
    // System.out.println("OS Version: " + osVersion);
    // System.out.println("OS Architecture: " + osArch);

    try {
      String scriptPath;
      String command;

      if (os.contains("win")) {
        // Windows-specific code
        scriptPath = "config\\builders\\run.bat";
        command = "cmd /c \"" + scriptPath + "\"";
      } else if (os.contains("nix") || os.contains("nux")) {
        // Unix/Linux-specific code
        scriptPath = "config/builders/run.sh";
        command = "sh \"" + scriptPath + "\"";
      } else if (os.contains("mac")) {
        // macOS-specific code
        scriptPath = "config/builders/run.sh";
        command = "sh \"" + scriptPath + "\"";
      } else {
        System.out.println("Unknown operating system.");
        return; // Exit if the OS is unknown
      }

      runCommand(command);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void runCommand(String command) throws IOException, InterruptedException {
    Process process = Runtime.getRuntime().exec(command);

    // Capture and display the output
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    }

    int exitCode = process.waitFor();

    if (exitCode == 0) {
      // Delete the Setup.class file after the script completes
      File classFile = new File("Setup.class");
      if (classFile.exists()) {
        classFile.delete();
      }
    } else {
      System.out.println("Script execution failed with exit code: " + exitCode);
    }
  }
}
