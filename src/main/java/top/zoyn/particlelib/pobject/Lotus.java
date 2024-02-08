package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.utils.LocationUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Lotus extends ParticleObject {

    public Lotus(Pos origin) {
        setOrigin(origin);
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();
        // 外围花瓣
        for (double t = -0.15D; t <= 0.15D; t += 0.005D) {
            double x = 5 * Math.sin(t) * Math.cos(t) * Math.log(Math.abs(t));
            double y = 5 * Math.sqrt(Math.abs(t)) * Math.cos(t);
            y -= 5;

            Pos spawn = getOrigin().add(x, 0, y);
            for (int i = 0; i <= 8; i++) {
                Pos temp = LocationUtils.rotateLocationAboutPoint(spawn, 360D / 8D * i, getOrigin());
                points.add(temp);
            }

            points.add(spawn);
        }

        // 内圈花瓣
        for (double t = -0.2D; t <= 0.2D; t += 0.01D) {
            double x = 3 * Math.sin(t) * Math.cos(t) * Math.log(Math.abs(t));
            double y = 3 * Math.sqrt(Math.abs(t)) * Math.cos(t);
            y -= 3.65;

            Pos spawn = getOrigin().add(x, 0, y);
            spawn = LocationUtils.rotateLocationAboutPoint(spawn, 22D, getOrigin());

            for (int i = 0; i <= 8; i++) {
                Pos temp = LocationUtils.rotateLocationAboutPoint(spawn, 360D / 8D * i, getOrigin());
                points.add(temp);
            }

            points.add(spawn);
        }

        // 最外层小花瓣
        for (double t = -0.1D; t <= 0.1D; t += 0.01D) {
            double x = 2 * Math.sin(t) * Math.cos(t) * Math.log(Math.abs(t));
            double y = 2 * Math.sqrt(Math.abs(t)) * Math.cos(t);
            y -= 4.6;

            Pos spawn = getOrigin().add(x, 0, y);
            spawn = LocationUtils.rotateLocationAboutPoint(spawn, 22D, getOrigin());

            for (int i = 0; i <= 8; i++) {
                Pos temp = LocationUtils.rotateLocationAboutPoint(spawn, 360D / 8D * i, getOrigin());
                points.add(temp);
            }

            points.add(spawn);
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
        // 外围花瓣
        for (double t = -0.15D; t <= 0.15D; t += 0.005D) {
            double x = 5 * Math.sin(t) * Math.cos(t) * Math.log(Math.abs(t));
            double y = 5 * Math.sqrt(Math.abs(t)) * Math.cos(t);
            y -= 5;

            Pos spawn = getOrigin().add(x, 0, y);
            for (int i = 0; i <= 8; i++) {
                Pos temp = LocationUtils.rotateLocationAboutPoint(spawn, 360D / 8D * i, getOrigin());
                spawnParticle(temp);
            }

            spawnParticle(spawn);
        }

        // 内圈花瓣
        for (double t = -0.2D; t <= 0.2D; t += 0.01D) {
            double x = 3 * Math.sin(t) * Math.cos(t) * Math.log(Math.abs(t));
            double y = 3 * Math.sqrt(Math.abs(t)) * Math.cos(t);
            y -= 3.65;

            Pos spawn = getOrigin().add(x, 0, y);
            spawn = LocationUtils.rotateLocationAboutPoint(spawn, 22D, getOrigin());

            for (int i = 0; i <= 8; i++) {
                Pos temp = LocationUtils.rotateLocationAboutPoint(spawn, 360D / 8D * i, getOrigin());
                spawnParticle(temp);
            }

            spawnParticle(spawn);
        }

        // 最外层小花瓣
        for (double t = -0.1D; t <= 0.1D; t += 0.01D) {
            double x = 2 * Math.sin(t) * Math.cos(t) * Math.log(Math.abs(t));
            double y = 2 * Math.sqrt(Math.abs(t)) * Math.cos(t);
            y -= 4.6;

            Pos spawn = getOrigin().add(x, 0, y);
            spawn = LocationUtils.rotateLocationAboutPoint(spawn, 22D, getOrigin());

            for (int i = 0; i <= 8; i++) {
                Pos temp = LocationUtils.rotateLocationAboutPoint(spawn, 360D / 8D * i, getOrigin());
                spawnParticle(temp);
            }

            spawnParticle(spawn);
        }

    }


}
