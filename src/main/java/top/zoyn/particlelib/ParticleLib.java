package top.zoyn.particlelib;

import net.minestom.server.extensions.Extension;
import top.zoyn.particlelib.utils.scheduler.MinestomScheduler;

/**
 * 粒子库主类
 *
 * @author Zoyn
 */
public class ParticleLib extends Extension {

    private static ParticleLib instance;

    private final MinestomScheduler scheduler = new MinestomScheduler();

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
    public void initialize() {
        instance = this;
        sendLog("§a粒子库已成功加载");
    }

    @Override
    public void terminate() {
        sendLog("§a粒子库已成功卸载");
    }

}
