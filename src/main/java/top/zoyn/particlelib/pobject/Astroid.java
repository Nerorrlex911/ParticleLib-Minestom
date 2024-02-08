package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.ParticleLib;

import java.util.List;

/**
 * 表示一个星形线
 *
 * @author Zoyn IceCold
 */
public class Astroid extends ParticleObject implements Playable {

    private double radius;
    private double step;

    private double currentT = 0D;

    /**
     * 构造一个星形线
     *
     * @param origin 原点
     */
    public Astroid(Pos origin) {
        this(1D, origin);
    }

    /**
     * 构造一个星形线
     *
     * @param radius 半径
     * @param origin 原点
     */
    public Astroid(double radius, Pos origin) {
        this(1D, origin, 10);
    }

    public Astroid(double radius, Pos origin, double step) {
        this.radius = radius;
        this.step = step;
        setOrigin(origin);
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        for (double t = 0.0D; t < 360.0D; t += step) {
            double radians = Math.toRadians(t);
            // 计算公式
            double x = Math.pow(this.radius * Math.cos(radians), 3.0D);
            double z = Math.pow(this.radius * Math.sin(radians), 3.0D);

            Pos showPos = getOrigin().add(x, 0, z);
            if (hasMatrix()) {
                Vec vec = new Vec(x, 0, z);
                Vec changed = getMatrix().applyVector(vec);

                showPos = getOrigin().add(changed);
            }

            showPos = showPos.add(getIncrementX(), getIncrementY(), getIncrementZ());
            points.add(showPos);
        }
        return points;
    }

    @Override
    public void show() {
        for (double t = 0.0D; t < 360.0D; t += step) {
            double radians = Math.toRadians(t);
            // 计算公式
            double x = Math.pow(this.radius * Math.cos(radians), 3.0D);
            double z = Math.pow(this.radius * Math.sin(radians), 3.0D);

            spawnParticle(getOrigin().add(x, 0, z));
        }
    }

    @Override
    public void play() {
        new MinestomRunnable() {
            @Override
            public void run() {
                // 重置
                if (currentT > 360D) {
                    cancel();
                    return;
                }
                currentT += step;
                double radians = Math.toRadians(currentT);
                // 计算公式
                double x = Math.pow(getRadius() * Math.cos(radians), 3.0D);
                double z = Math.pow(getRadius() * Math.sin(radians), 3.0D);

                spawnParticle(getOrigin().add(x, 0, z));
            }
        }.runTaskTimer(ParticleLib.getInstance(), 0, getPeriod());
    }

    @Override
    public void playNextPoint() {
        currentT += step;
        double radians = Math.toRadians(currentT);
        // 计算公式
        double x = Math.pow(this.radius * Math.cos(radians), 3.0D);
        double z = Math.pow(this.radius * Math.sin(radians), 3.0D);

        spawnParticle(getOrigin().add(x, 0, z));
        // 重置
        if (currentT > 360D) {
            currentT = 0D;
        }
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }
}
