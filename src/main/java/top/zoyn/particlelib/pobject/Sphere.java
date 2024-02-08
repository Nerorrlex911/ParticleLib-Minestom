package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.bukkit.Color;
import top.zoyn.particlelib.ParticleLib;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示一个球
 * <p>算法来源: https://stackoverflow.com/questions/9600801/evenly-distributing-n-points-on-a-sphere/26127012#26127012</p>
 *
 * @author Zoyn IceCold
 */
public class Sphere extends ParticleObject implements Playable {

    /**
     * 黄金角度 约等于137.5度
     */
    private final double phi = Math.PI * (3D - Math.sqrt(5));
    private final List<Pos> pos;
    private int sample;
    private double radius;
    private int currentSample = 0;

    public Sphere(Pos origin) {
        this(origin, 50, 1);
    }

    /**
     * 构造一个球
     *
     * @param origin 球的圆点
     * @param sample 样本点个数(粒子的数量)
     * @param radius 球的半径
     */
    public Sphere(Pos origin, int sample, double radius) {
        setOrigin(origin);
        this.sample = sample;
        this.radius = radius;

        pos = new ArrayList<>();
        resetLocations();
    }

    public Sphere(Pos origin, int sample, double radius, Color color) {
        this(origin, sample, radius);
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();

        for (int i = 0; i < sample; i++) {
            // y goes from 1 to -1
            double y = 1 - (i / (sample - 1f)) * 2;
            // radius at y
            double yRadius = Math.sqrt(1 - y * y);
            // golden angle increment
            double theta = phi * i;
            double x = Math.cos(theta) * radius * yRadius;
            double z = Math.sin(theta) * radius * yRadius;
            y *= radius;

            points.add(getOrigin().add(x, y, z));
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
        // 重置
        if (currentSample + 1 == pos.size()) {
            currentSample = 0;
        }
        spawnParticle(pos.get(currentSample));
        currentSample++;
    }

    public int getSample() {
        return sample;
    }

    public Sphere setSample(int sample) {
        this.sample = sample;
        resetLocations();
        return this;
    }

    public double getRadius() {
        return radius;
    }

    public Sphere setRadius(double radius) {
        this.radius = radius;
        resetLocations();
        return this;
    }

    public void resetLocations() {
        pos.clear();

        for (int i = 0; i < sample; i++) {
            // y goes from 1 to -1
            double y = 1 - (i / (sample - 1f)) * 2;
            // radius at y
            double yRadius = Math.sqrt(1 - y * y);
            // golden angle increment
            double theta = phi * i;
            double x = Math.cos(theta) * radius * yRadius;
            double z = Math.sin(theta) * radius * yRadius;
            y *= radius;

            pos.add(getOrigin().add(x, y, z));
        }
    }
}
