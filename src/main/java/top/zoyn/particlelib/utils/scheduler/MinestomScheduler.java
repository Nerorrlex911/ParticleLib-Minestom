package top.zoyn.particlelib.utils.scheduler;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import net.minestom.server.utils.time.TimeUnit;

import java.util.function.Consumer;

public class MinestomScheduler {

    public MinestomTask runTask(Extension plugin, Runnable task) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).schedule(), plugin);
    }

    public void runTask(Extension plugin, Consumer<MinestomTask> task) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTask(Extension plugin, MinestomRunnable task) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).schedule(), plugin);
    }

    public MinestomTask runTaskAsynchronously(Extension plugin, Runnable task) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).schedule(), plugin);
    }

    public void runTaskAsynchronously(Extension plugin, Consumer<MinestomTask> task) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskAsynchronously(Extension plugin, MinestomRunnable task) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).schedule(), plugin);
    }

    public MinestomTask runTaskLater(Extension plugin, Runnable task, long delay) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public void runTaskLater(Extension plugin, Consumer<MinestomTask> task, long delay) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskLater(Extension plugin, MinestomRunnable task, long delay) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public MinestomTask runTaskLaterAsynchronously(Extension plugin, Runnable task, long delay) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public void runTaskLaterAsynchronously(Extension plugin, Consumer<MinestomTask> task, long delay) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskLaterAsynchronously(Extension plugin, MinestomRunnable task, long delay) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public MinestomTask runTaskTimer(Extension plugin, Runnable task, long delay, long period) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).repeat(period, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public void runTaskTimer(Extension plugin, Consumer<MinestomTask> task, long delay, long period) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskTimer(Extension plugin, MinestomRunnable task, long delay, long period) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).repeat(period, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public MinestomTask runTaskTimerAsynchronously(Extension plugin, Runnable task, long delay, long period) {
        return new MinestomTask(MinecraftServer.getSchedulerManager().buildTask(task).delay(delay, TimeUnit.SERVER_TICK).schedule(), plugin);
    }

    public void runTaskTimerAsynchronously(Extension plugin, Consumer<MinestomTask> task, long delay, long period) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public MinestomTask runTaskTimerAsynchronously(Extension plugin, MinestomRunnable task, long delay, long period) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}