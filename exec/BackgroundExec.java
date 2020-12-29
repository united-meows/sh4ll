package sh4ll.exec;

public abstract class BackgroundExec extends Exec {

    private int sleepRate;
    private Thread thread = null;

    public BackgroundExec(String[] _triggers, String[] _expectedInputs, String _description, int _sleepRate) {
        super(_triggers, _expectedInputs, _description);
        sleepRate = _sleepRate;
    }

    @Override
    public byte runExec(String fullText, String[] splitted) {
        if (sleepRate > 0) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(sleepRate);
                    } catch (InterruptedException e) {

                    }
                    tick();
                }
            });

            thread.start();
        }
        return onExecute(fullText, splitted);
    }

    public abstract byte onExecute(String fullText, String[] splitted);
    public abstract void tick();

    public void stop() {
        if (thread != null) {
            thread.stop();
        }

    }

    @Override
    public abstract String usage();
}
