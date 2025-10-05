package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.ParticleLib;
import top.zoyn.particlelib.utils.MathUtils;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cone extends ParticleObject implements Playable {

    /**
     * 黄金角度 约等于137.5度
     */
    private final double phi = Math.PI * (3D - Math.sqrt(5));
    private final List<Pos> pos;
    private int sample;
    private double radius;
    private final double height;
    private int currentSample = 0;

    public Cone(Pos origin) {
        this(origin, 50, 1.5, 3);
    }

    /**
     * 构造一个圆锥
     *
     * @param origin 圆锥底面圆的原点
     * @param sample 样本点个数(粒子的数量)
     * @param radius 球的半径
     */
    public Cone(Pos origin, int sample, double radius, double height) {
        setOrigin(origin);
        this.sample = sample;
        this.radius = radius;
        this.height = height;

        pos = new ArrayList<>();
        resetLocations();
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();

        List<Double> indices = MathUtils.arange(0, sample, 1);
        List<Double> phis = Lists.newArrayList();
        List<Double> thetas = Lists.newArrayList();
        for (double indice : indices) {
            double phi = Math.acos(1 - 2 * indice / sample);
            double theta = Math.PI * (1 + Math.sqrt(5)) * indice;
            phis.add(phi);
            thetas.add(theta);
        }

        for (int i = 0; i < sample; i++) {
            double theta = thetas.get(i);
            double phi = phis.get(i);
            double x = radius * Math.cos(theta) * Math.sin(phi);
            double y = height * -Math.sin(phi);
            double z = radius * Math.sin(theta) * Math.sin(phi);

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

    @Override
    public boolean hasNext() {
        return currentSample + 1 == pos.size();
    }

    public int getSample() {
        return sample;
    }

    public Cone setSample(int sample) {
        this.sample = sample;
        resetLocations();
        return this;
    }

    public double getRadius() {
        return radius;
    }

    public Cone setRadius(double radius) {
        this.radius = radius;
        resetLocations();
        return this;
    }

    public void resetLocations() {
        pos.clear();

        List<Double> indices = MathUtils.arange(0, sample, 1);
        List<Double> phis = Lists.newArrayList();
        List<Double> thetas = Lists.newArrayList();
        for (double indice : indices) {
            double phi = Math.acos(1 - 2 * indice / sample);
            double theta = Math.PI * (1 + Math.sqrt(5)) * indice;
            phis.add(phi);
            thetas.add(theta);
        }

        for (int i = 0; i < sample; i++) {
            double theta = thetas.get(i);
            double phi = phis.get(i);
            double x = radius * Math.cos(theta) * Math.sin(phi);
            double y = height * -Math.sin(phi);
            double z = radius * Math.sin(theta) * Math.sin(phi);

            pos.add(getOrigin().add(x, y, z));
        }
    }
}
