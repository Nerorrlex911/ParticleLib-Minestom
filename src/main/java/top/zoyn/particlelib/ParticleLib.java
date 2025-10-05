package top.zoyn.particlelib;

import com.github.zimablue.devoutserver.plugin.Plugin;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;
import top.zoyn.particlelib.command.ParticleCommand;
import top.zoyn.particlelib.utils.scheduler.MinestomScheduler;

/**
 * 粒子库主类
 *
 * @author Zoyn
 */
public class ParticleLib extends Plugin {

    private static ParticleLib instance;

    private final MinestomScheduler scheduler = new MinestomScheduler();

    private final Scheduler asyncScheduler = Scheduler.newScheduler();

    public static ParticleLib getInstance() {
        return instance;
    }

    /**
     * 往控制台上发送一条支持颜色代码的信息
     *
     * @param message 信息
     */
    public static void sendLog(String message) {
        getInstance().getLogger().info("§e[§6ParticleLib§e] " + message);
    }

    public static MinestomScheduler getScheduler() {
        return getInstance().scheduler;
    }

    @Override
    public void onEnable() {
        instance = this;
        MinecraftServer.getCommandManager().register(new ParticleCommand());
        sendLog("§a粒子库已成功加载");
    }

    @Override
    public void onDisable() {
        sendLog("§a粒子库已成功卸载");
    }

}
