package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import org.bukkit.Color;
import net.minestom.server.coordinate.Pos;
import org.bukkit.Particle;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.ParticleLib;

import java.util.List;

/**
 * 表示一条线
 *
 * @author Zoyn IceCold
 */
public class Line extends ParticleObject implements Playable {

    private Vec vec;
    private Pos start;
    private Pos end;
    /**
     * 步长
     */
    private double step;
    /**
     * 向量长度
     */
    private double length;
    private double currentStep = 0D;

    public Line(Pos start, Pos end) {
        this(start, end, 0.1);
    }

    /**
     * 构造一个线
     *
     * @param start 线的起点
     * @param end   线的终点
     * @param step  每个粒子之间的间隔 (也即步长)
     */
    public Line(Pos start, Pos end, double step) {
        this(start, end, step, 20L);
    }

    /**
     * 构造一个线
     *
     * @param start  线的起点
     * @param end    线的终点
     * @param step   每个粒子之间的间隔 (也即步长)
     * @param period 特效周期(如果需要可以使用)
     */
    public Line(Pos start, Pos end, double step, long period) {
        this.start = start;
        this.end = end;
        this.step = step;
        setPeriod(period);

        // 对向量进行重置
        resetVector();
    }


    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        for (double i = 0; i < length; i += step) {
            Vec vecTemp = vec.mul(i);
            points.add(start.add(vecTemp));
        }
        return points;
    }

    @Override
    public void show() {
        for (double i = 0; i < length; i += step) {
            Vec vecTemp = vec.mul(i);
            spawnParticle(start.add(vecTemp));
        }
    }

    @Override
    public void play() {
        new MinestomRunnable() {
            @Override
            public void run() {
                // 进行关闭
                if (currentStep > length) {
                    cancel();
                    return;
                }
                currentStep += step;
                Vec vecTemp = vec.mul(currentStep);
                spawnParticle(start.add(vecTemp));
            }
        }.runTaskTimer(ParticleLib.getInstance(), 0, getPeriod());
    }

    @Override
    public void playNextPoint() {
        currentStep += step;
        Vec vecTemp = vec.mul(currentStep);
        spawnParticle(start.add(vecTemp));

        if (currentStep > length) {
            currentStep = 0D;
        }
    }

    public Pos getStart() {
        return start;
    }

    public Line setStart(Pos start) {
        this.start = start;
        resetVector();
        return this;
    }

    public Pos getEnd() {
        return end;
    }

    public Line setEnd(Pos end) {
        this.end = end;
        resetVector();
        return this;
    }

    public double getStep() {
        return step;
    }

    public Line setStep(double step) {
        this.step = step;
        resetVector();
        return this;
    }

    public void resetVector() {
        vec = end.sub(start).asVec();
        length = vec.length();
        vec = vec.normalize();
    }

}
