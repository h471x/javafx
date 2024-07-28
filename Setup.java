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

      if (os.contains("win")) {
        // System.out.println("You are using Windows.");
        try {
          // Get the current working directory
          String currentDir = new java.io.File(".").getCanonicalPath();
          // Construct the absolute path for the batch script
          // String scriptPath = currentDir + "\\controllers\\builders\\run.bat";
          String scriptPath = "run.bat";
          String command = "cmd /c \"" + scriptPath + "\"";
          // System.out.println("Executing command: " + command);

          Process process = Runtime.getRuntime().exec(command);

          // Capture and display the output
          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
          String line;
          while ((line = reader.readLine()) != null) {
              System.out.println(line);
          }

          int exitCode = process.waitFor();

          if (exitCode == 0) {
            // Delete the Setup.class file after the batch script completes
            File classFile = new File("Setup.class");
            classFile.delete();
          }
          // System.out.println("Batch file executed with exit code: " + exitCode);
      } catch (IOException | InterruptedException e) {
          e.printStackTrace();
      }
    } else if (os.contains("mac")) {
        // System.out.println("You are using macOS.");
    } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
        // System.out.println("You are using Unix/Linux.");
    } else {
        // System.out.println("Unknown operating system.");
    }
  }
}
