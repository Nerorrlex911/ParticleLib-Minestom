package top.zoyn.particlelib.pobject.bezier;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.ParticleLib;
import top.zoyn.particlelib.pobject.ParticleObject;
import top.zoyn.particlelib.pobject.Playable;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一条n阶贝塞尔曲线
 * <p>给定 n + 1 点, 绘制一条平滑的曲线</p>
 *
 * @author Zoyn
 */
public class NRankBezierCurve extends ParticleObject implements Playable {

    /**
     * 用于保存将要播放的粒子的点位
     */
    private final List<Pos> points = Lists.newLinkedList();
    private final double step;
    /**
     * 用于计算贝塞尔曲线上的点
     */
    private final List<Pos> pos;
    private int currentSample = 0;

    public NRankBezierCurve(Pos... pos) {
        this(Arrays.asList(pos));
    }

    public NRankBezierCurve(List<Pos> pos) {
        this(pos, 0.05D);
    }

    /**
     * 构造一个N阶贝塞尔曲线
     *
     * @param pos 所有的点
     * @param step      T的步进数
     */
    public NRankBezierCurve(List<Pos> pos, double step) {
        this.pos = pos;
        this.step = step;
        resetLocations();
    }

    private static Pos calculateCurve(List<Pos> locList, double t) {
        if (locList.size() == 2) {
            return locList.get(0).add(locList.get(1).sub(locList.get(0)).asVec().mul(t));
        }

        List<Pos> locListTemp = Lists.newArrayList();
        for (int i = 0; i < locList.size(); i++) {
            if (i + 1 == locList.size()) {
                break;
            }
            Pos p0 = locList.get(i);
            Pos p1 = locList.get(i + 1);

            // 降阶处理
            locListTemp.add(p0.add(p1.sub(p0).asVec().mul(t)));
        }
        return calculateCurve(locListTemp, t);
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        for (double t = 0; t < 1; t += step) {
            Pos pos = calculateCurve(this.pos, t);
            points.add(pos);
        }

        // 做一个对 Matrix 和 Increment 的兼容
        return points.stream().map(location -> {
            Pos showPos = location;
            if (hasMatrix()) {
                Vec v = new Vec(location.x() - getOrigin().x(), location.y() - getOrigin().y(), location.z() - getOrigin().z());
                Vec changed = getMatrix().applyVector(v);

                showPos = getOrigin().add(changed);
            }

            showPos = showPos.add(getIncrementX(), getIncrementY(), getIncrementZ());
            return showPos;
        }).collect(Collectors.toList());
    }

    @Override
    public void show() {
        points.forEach(loc -> {
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
                if (currentSample + 1 == points.size()) {
                    cancel();
                    return;
                }
                currentSample++;

                spawnParticle(points.get(currentSample));
            }
        }.runTaskTimer(ParticleLib.getInstance(), 0, getPeriod());
    }

    @Override
    public void playNextPoint() {
        if (currentSample + 1 == points.size()) {
            currentSample = 0;
        }
        spawnParticle(points.get(currentSample));
        currentSample++;
    }

    public void resetLocations() {
        points.clear();

        for (double t = 0; t < 1; t += step) {
            Pos pos = calculateCurve(this.pos, t);
            points.add(pos);
        }
    }
}
