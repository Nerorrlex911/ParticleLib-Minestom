package top.zoyn.particlelib.utils.scheduler;


import com.github.zimablue.devoutserver.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import top.zoyn.particlelib.ParticleLib;

public abstract class MinestomRunnable implements Runnable {
    private MinestomTask task;

    public MinestomRunnable() {
    }

    public synchronized boolean isCancelled() throws IllegalStateException {
        this.checkScheduled();
        return this.task.isCancelled();
    }

    public synchronized void cancel() throws IllegalStateException {
        if(this.task != null)
            this.task.cancel();
    }

    public synchronized @NotNull MinestomTask runTask(@NotNull Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(ParticleLib.getScheduler().runTask(plugin, this));
    }

    public synchronized @NotNull MinestomTask runTaskAsynchronously(@NotNull Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(ParticleLib.getScheduler().runTaskAsynchronously(plugin, this));
    }

    public synchronized @NotNull MinestomTask runTaskLater(@NotNull Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(ParticleLib.getScheduler().runTaskLater(plugin, this, delay));
    }

    public synchronized @NotNull MinestomTask runTaskLaterAsynchronously(@NotNull Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(ParticleLib.getScheduler().runTaskLaterAsynchronously(plugin, this, delay));
    }

    public synchronized @NotNull MinestomTask runTaskTimer(@NotNull Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(ParticleLib.getScheduler().runTaskTimer(plugin, this, delay, period));
    }

    public synchronized @NotNull MinestomTask runTaskTimerAsynchronously(@NotNull Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(ParticleLib.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, period));
    }

    public synchronized int getTaskId() throws IllegalStateException {
        this.checkScheduled();
        return this.task.getTaskId();
    }

    private void checkScheduled() {
        if (this.task == null) {
            throw new IllegalStateException("Not scheduled yet");
        }
    }

    private void checkNotYetScheduled() {
        if (this.task != null) {
            throw new IllegalStateException("Already scheduled as " + this.task.getTaskId());
        }
    }

    private @NotNull MinestomTask setupTask(@NotNull MinestomTask task) {
        this.task = task;
        return task;
    }
}
