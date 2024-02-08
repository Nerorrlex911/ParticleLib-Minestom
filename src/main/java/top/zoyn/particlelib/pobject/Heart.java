package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.ParticleLib;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;

import java.util.List;

/**
 * 表示一颗心
 *
 * @author Zoyn IceCold
 */
public class Heart extends ParticleObject implements Playable {

    private double xScaleRate;
    private double yScaleRate;
    /**
     * 表示步进的程度
     */
    private double step = 0.001D;
    private double currentT = -1.0D;


    /**
     * 构造一个小心心
     *
     * @param origin 原点
     */
    public Heart(Pos origin) {
        this(1, 1, origin);
    }

    /**
     * 构造一个心形线
     *
     * @param xScaleRate X轴缩放比率
     * @param yScaleRate Y轴缩放比率
     * @param origin     原点
     */
    public Heart(double xScaleRate, double yScaleRate, Pos origin) {
        this.xScaleRate = xScaleRate;
        this.yScaleRate = yScaleRate;
        setOrigin(origin);
    }

    public double getXScaleRate() {
        return xScaleRate;
    }

    public void setXScaleRate(double xScaleRate) {
        this.xScaleRate = xScaleRate;
    }

    public double getYScaleRate() {
        return yScaleRate;
    }

    public void setYScaleRate(double yScaleRate) {
        this.yScaleRate = yScaleRate;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        for (double t = -1.0D; t <= 1.0D; t += step) {
            double x = xScaleRate * Math.sin(t) * Math.cos(t) * Math.log(Math.abs(t));
            double y = yScaleRate * Math.sqrt(Math.abs(t)) * Math.cos(t);

            Pos showPos = getOrigin().add(x, 0, y);
            if (hasMatrix()) {
                Vec vec = new Vec(x, 0, y);
                Vec changed = getMatrix().applyVector(vec);

                showPos = getOrigin().add(changed);
            }

            showPos = showPos.add(getIncrementX(), getIncrementY(), getIncrementZ());
            points.add(showPos);
//            points.add(getOrigin().add(x, 0, y));
        }
        return points;
    }

    @Override
    public void show() {
        for (double t = -1.0D; t <= 1.0D; t += step) {
            double x = xScaleRate * Math.sin(t) * Math.cos(t) * Math.log(Math.abs(t));
            double y = yScaleRate * Math.sqrt(Math.abs(t)) * Math.cos(t);

            spawnParticle(getOrigin().add(x, 0, y));

        }
    }

    @Override
    public void play() {
        new MinestomRunnable() {
            @Override
            public void run() {
                if (currentT > 1.0D) {
                    cancel();
                    return;
                }
                currentT += step;
                double x = xScaleRate * Math.sin(currentT) * Math.cos(currentT) * Math.log(Math.abs(currentT));
                double y = yScaleRate * Math.sqrt(Math.abs(currentT)) * Math.cos(currentT);

                spawnParticle(getOrigin().add(x, 0, y));
            }
        }.runTaskTimer(ParticleLib.getInstance(), 0, getPeriod());
    }

    @Override
    public void playNextPoint() {
        currentT += step;
        double x = xScaleRate * Math.sin(currentT) * Math.cos(currentT) * Math.log(Math.abs(currentT));
        double y = yScaleRate * Math.sqrt(Math.abs(currentT)) * Math.cos(currentT);

        spawnParticle(getOrigin().add(x, 0, y));

        if (currentT > 1.0D) {
            currentT = -1.0D;
        }
    }
}