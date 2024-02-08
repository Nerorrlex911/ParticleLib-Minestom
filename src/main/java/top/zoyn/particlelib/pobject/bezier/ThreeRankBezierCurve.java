package top.zoyn.particlelib.pobject.bezier;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.ParticleLib;
import top.zoyn.particlelib.pobject.ParticleObject;
import top.zoyn.particlelib.pobject.Playable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一条三阶贝塞尔曲线
 * <p>给定四点, 自动生成一条三阶贝塞尔曲线</p>
 *
 * @author Zoyn
 */
public class ThreeRankBezierCurve extends ParticleObject implements Playable {

    private final List<Pos> pos;
    private Pos p0;
    private Pos p1;
    private Pos p2;
    private Pos p3;
    private double step;
    private int currentSample = 0;

    public ThreeRankBezierCurve(Pos p0, Pos p1, Pos p2, Pos p3) {
        this(p0, p1, p2, p3, 0.05);
    }

    /**
     * 构造一个三阶贝塞尔曲线
     *
     * @param p0   连续点
     * @param p1   控制点
     * @param p2   控制点
     * @param p3   连续点
     * @param step 每个粒子的间隔(也即步长)
     */
    public ThreeRankBezierCurve(Pos p0, Pos p1, Pos p2, Pos p3, double step) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.step = step;
        pos = new ArrayList<>();

        resetLocations();
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();

        for (double t = 0; t < 1; t += step) {
            Vec v1 = p1.sub(p0).asVec();
            Pos t1 = p0.add(v1.mul(t));
            Vec v2 = p2.sub(p1).asVec();
            Pos t2 = p1.add(v2.mul(t));
            Vec v3 = p3.sub(p2).asVec();
            Pos t3 = p2.add(v3.mul(t));

            Vec dv1 = t2.sub(t1).asVec();
            Pos d1 = t1.add(dv1.mul(t));
            Vec dv2 = t3.sub(t2).asVec();
            Pos d2 = t2.add(dv2.mul(t));

            Vec f1 = d2.sub(d1).asVec();
            Pos destination = d1.add(f1.mul(t));
            points.add(destination);
        }

        // 做一个对 Matrix 和 Increment 的兼容
        return points.stream().map(location -> {
            Pos showPos = location;
            if (hasMatrix()) {
                Vec v = new Vec(location.x() - getOrigin().x(), location.y() - getOrigin().y(), location.z() - getOrigin().z());
                Vec changed = getMatrix().applyVector(v);

                showPos = getOrigin().add(changed);
            }

            showPos.add(getIncrementX(), getIncrementY(), getIncrementZ());
            return showPos;
        }).collect(Collectors.toList());
    }

    @Override
    public void show() {
        pos.forEach(loc -> {
            if (loc != null) {
                    spawnParticle(loc);
            }
        });
    }

    @Override
    public void play() {
        new MinestomRunnable() {
            @Override
            public void run() {
                // 进行关闭
                if (currentSample + 1 == pos.size()) {
                    cancel();
                    return;
                }
                currentSample++;

                spawnParticle(pos.get(currentSample));
            }
        }.runTaskTimer(ParticleLib.getInstance(), 0, getPeriod());
    }

    @Override
    public void playNextPoint() {
        if (currentSample + 1 == pos.size()) {
            currentSample = 0;
        }
        spawnParticle(pos.get(currentSample));
        currentSample++;
    }

    public Pos getP0() {
        return p0;
    }

    public ThreeRankBezierCurve setP0(Pos p0) {
        this.p0 = p0;
        resetLocations();
        return this;
    }

    public Pos getP1() {
        return p1;
    }

    public ThreeRankBezierCurve setP1(Pos p1) {
        this.p1 = p1;
        resetLocations();
        return this;
    }

    public Pos getP2() {
        return p2;
    }

    public ThreeRankBezierCurve setP2(Pos p2) {
        this.p2 = p2;
        resetLocations();
        return this;
    }

    public Pos getP3() {
        return p3;
    }

    public ThreeRankBezierCurve setP3(Pos p3) {
        this.p3 = p3;
        resetLocations();
        return this;
    }

    public double getStep() {
        return step;
    }

    public ThreeRankBezierCurve setStep(double step) {
        this.step = step;
        resetLocations();
        return this;
    }

    /**
     * 重新计算贝塞尔曲线上的点
     */
    public void resetLocations() {
        pos.clear();
        // 算法
        // 算了我知道很蠢这个算法...
        for (double t = 0; t < 1; t += step) {
            Vec v1 = p1.sub(p0).asVec();
            Pos t1 = p0.add(v1.mul(t));
            Vec v2 = p2.sub(p1).asVec();
            Pos t2 = p1.add(v2.mul(t));
            Vec v3 = p3.sub(p2).asVec();
            Pos t3 = p2.add(v3.mul(t));

            Vec dv1 = t2.sub(t1).asVec();
            Pos d1 = t1.add(dv1.mul(t));
            Vec dv2 = t3.sub(t2).asVec();
            Pos d2 = t2.add(dv2.mul(t));

            Vec f1 = d2.sub(d1).asVec();
            Pos destination = d1.add(f1.mul(t));
            pos.add(destination);
        }
    }
}
