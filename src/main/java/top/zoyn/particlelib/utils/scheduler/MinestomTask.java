package top.zoyn.particlelib.utils.scheduler;



import com.github.zimablue.devoutserver.plugin.Plugin;
import net.minestom.server.timer.Task;

public class MinestomTask {
    private final Task task;
    private final Plugin plugin;

    public MinestomTask(Task task, Plugin plugin) {
        this.task = task;
        this.plugin = plugin;
    }

    public int getTaskId() {
        return task.id();
    }


    public Plugin getOwner() {
        return plugin;
    }


    public boolean isSync() {
        return false;
    }


    public boolean isCancelled() {
        return task.isAlive();
    }

    public void cancel() {
        task.cancel();
    }
}
