package top.zoyn.particlelib.utils.scheduler;


import net.minestom.server.extensions.Extension;
import net.minestom.server.timer.Task;

public class MinestomTask {
    private final Task task;
    private final Extension plugin;

    public MinestomTask(Task task, Extension plugin) {
        this.task = task;
        this.plugin = plugin;
    }

    public int getTaskId() {
        return task.id();
    }


    public Extension getOwner() {
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
