package chess;

// These shenanigans are required in order to "correctly" handle SIGINT in Java.
// This class will simply wrap an App instance and propagate the interrupt signal
// when received.

public class Main extends Thread {
    private static App app;

    @Override
    public void run() {
        if (Main.app.isAlive()) {
            try {
                // Send the interrupt signal and wait for the thread to exit.
                // Shutdown hooks should be quick, so don't wait forever. This is
                // still better than destroying the VM
                Main.app.interrupt();
                Main.app.join(1000);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public static void main(String[] args) {
        Thread hook = new Main();
        
        Runtime.getRuntime().addShutdownHook(hook);
        
        Main.app = new App();
        Main.app.setDaemon(false);
        Main.app.start();

        // The JVM will wait for the application thread to exit because it is not a
        // daemon, but will still receive signals
    }
}
