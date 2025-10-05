package top.zoyn.particlelib.utils.scheduler;

import com.github.zimablue.devoutserver.plugin.Plugin;
import net.minestom.server.MinecraftServer;

import net.minestom.server.utils.time.TimeUnit;

import java.util.function.Consumer;

public class MinestomScheduler {

    public MinestomTask runTask(Plugin plugin, Runnable task) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).schedule(), plugin);
    }

    public void runTask(Plugin plugin, Consumer<MinestomTask> task) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTask(Plugin plugin, MinestomRunnable task) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).schedule(), plugin);
    }

    public MinestomTask runTaskAsynchronously(Plugin plugin, Runnable task) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).schedule(), plugin);
    }

    public void runTaskAsynchronously(Plugin plugin, Consumer<MinestomTask> task) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskAsynchronously(Plugin plugin, MinestomRunnable task) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).schedule(), plugin);
    }

    public MinestomTask runTaskLater(Plugin plugin, Runnable task, long delay) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public void runTaskLater(Plugin plugin, Consumer<MinestomTask> task, long delay) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskLater(Plugin plugin, MinestomRunnable task, long delay) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public MinestomTask runTaskLaterAsynchronously(Plugin plugin, Runnable task, long delay) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public void runTaskLaterAsynchronously(Plugin plugin, Consumer<MinestomTask> task, long delay) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskLaterAsynchronously(Plugin plugin, MinestomRunnable task, long delay) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public MinestomTask runTaskTimer(Plugin plugin, Runnable task, long delay, long period) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).repeat(period, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public void runTaskTimer(Plugin plugin, Consumer<MinestomTask> task, long delay, long period) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskTimer(Plugin plugin, MinestomRunnable task, long delay, long period) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).repeat(period, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public MinestomTask runTaskTimerAsynchronously(Plugin plugin, Runnable task, long delay, long period) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public void runTaskTimerAsynchronously(Plugin plugin, Consumer<MinestomTask> task, long delay, long period) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskTimerAsynchronously(Plugin plugin, MinestomRunnable task, long delay, long period) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}