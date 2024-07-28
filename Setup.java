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
            if (os.contains("win")) {
                // Windows-specific code
                String scriptPath = "run.bat";
                String command = "cmd /c \"" + scriptPath + "\"";
                runCommand(command);
            } else if (os.contains("nix") || os.contains("nux")) {
                // Unix/Linux-specific code
                String scriptPath = "run.sh";
                String command = "sh \"" + scriptPath + "\"";
                runCommand(command);
            } else if (os.contains("mac")) {
                // macOS-specific code
                String scriptPath = "run.sh";
                String command = "sh \"" + scriptPath + "\"";
                runCommand(command);
            } else {
                System.out.println("Unknown operating system.");
            }
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
            classFile.delete();
        } else {
            System.out.println("Script execution failed with exit code: " + exitCode);
        }
    }
}
