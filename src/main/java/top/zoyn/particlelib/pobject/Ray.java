package top.zoyn.particlelib.pobject;

import com.google.common.collect.Lists;
import net.minestom.server.coordinate.Pos;
import org.bukkit.entity.Entity;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;
import org.bukkit.util.Consumer;
import net.minestom.server.coordinate.Vec;
import top.zoyn.particlelib.ParticleLib;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 代表一个射线
 *
 * @author Zoyn IceCold
 */
public class Ray extends ParticleObject implements Playable {

    private Vec direction;
    private double maxLength;
    private double step;
    /**
     * 用于检测实体时获取周围实体的范围
     */
    private double range;
    private RayStopType stopType;
    private Consumer<Entity> hitEntityConsumer;
    private Predicate<Entity> entityFilter;

    private double currentStep = 0D;

    public Ray(Pos origin, Vec direction, double maxLength) {
        this(origin, direction, maxLength, 0.2D);
    }

    public Ray(Pos origin, Vec direction, double maxLength, double step) {
        this(origin, direction, maxLength, step, 0.5D, RayStopType.MAX_LENGTH, null);
    }

    public Ray(Pos origin, Vec direction, double maxLength, double step, double range, RayStopType stopType, Consumer<Entity> hitEntityConsumer) {
        this(origin, direction, maxLength, step, range, stopType, hitEntityConsumer, null);
    }

    public Ray(Pos origin, Vec direction, double maxLength, double step, double range, RayStopType stopType, Consumer<Entity> hitEntityConsumer, Predicate<Entity> entityFilter) {
        setOrigin(origin);
        this.direction = direction;
        this.maxLength = maxLength;
        this.step = step;
        this.range = range;
        this.stopType = stopType;
        this.hitEntityConsumer = hitEntityConsumer;
        this.entityFilter = entityFilter;
    }

    @Override
    public List<Pos> calculateLocations() {
        List<Pos> points = Lists.newArrayList();

        for (double i = 0; i < maxLength; i += step) {
            Vec vecTemp = direction.mul(i);
            Pos spawnPos = getOrigin().add(vecTemp);

            points.add(spawnPos);

            if (stopType.equals(RayStopType.HIT_ENTITY)) {
                Collection<Entity> nearbyEntities = spawnPos.getWorld().getNearbyEntities(spawnPos, range, range, range);
                List<Entity> entities = Lists.newArrayList();
                // 检测有无过滤器
                if (entityFilter != null) {
                    for (Entity entity : nearbyEntities) {
                        if (!entityFilter.test(entity)) {
                            entities.add(entity);
                        }
                    }
                } else {
                    entities = (List<Entity>) nearbyEntities;
                }

                // 获取首个实体
                if (entities.size() != 0) {
                    break;
                }
            }
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
        for (double i = 0; i < maxLength; i += step) {
            Vec vecTemp = direction.mul(i);
            Pos spawnPos = getOrigin().add(vecTemp);

            spawnParticle(spawnPos);

            if (stopType.equals(RayStopType.HIT_ENTITY)) {
                Collection<Entity> nearbyEntities = spawnPos.getWorld().getNearbyEntities(spawnPos, range, range, range);
                List<Entity> entities = Lists.newArrayList();
                // 检测有无过滤器
                if (entityFilter != null) {
                    for (Entity entity : nearbyEntities) {
                        if (!entityFilter.test(entity)) {
                            entities.add(entity);
                        }
                    }
                } else {
                    entities = (List<Entity>) nearbyEntities;
                }

                // 获取首个实体
                if (entities.size() != 0) {
                    hitEntityConsumer.accept(entities.get(0));
                    break;
                }
            }
        }
    }

    @Override
    public void play() {
        new MinestomRunnable() {
            @Override
            public void run() {
                // 进行关闭
                if (currentStep > maxLength) {
                    cancel();
                    return;
                }
                currentStep += step;
                Vec vecTemp = direction.mul(currentStep);
                Pos spawnPos = getOrigin().add(vecTemp);

                spawnParticle(spawnPos);

                if (stopType.equals(RayStopType.HIT_ENTITY)) {
                    Collection<Entity> nearbyEntities = spawnPos.getWorld().getNearbyEntities(spawnPos, range, range, range);
                    List<Entity> entities = Lists.newArrayList();
                    // 检测有无过滤器
                    if (entityFilter != null) {
                        for (Entity entity : nearbyEntities) {
                            if (!entityFilter.test(entity)) {
                                entities.add(entity);
                            }
                        }
                    } else {
                        entities = (List<Entity>) nearbyEntities;
                    }

                    // 获取首个实体
                    if (entities.size() != 0) {
                        hitEntityConsumer.accept(entities.get(0));
                        cancel();
                    }
                }
            }
        }.runTaskTimer(ParticleLib.getInstance(), 0, getPeriod());
    }

    @Override
    public void playNextPoint() {
        currentStep += step;
        Vec vecTemp = direction.mul(currentStep);
        Pos spawnPos = getOrigin().add(vecTemp);

        spawnParticle(spawnPos);

        if (stopType.equals(RayStopType.HIT_ENTITY)) {
            Collection<Entity> nearbyEntities = spawnPos.getWorld().getNearbyEntities(spawnPos, range, range, range);
            List<Entity> entities = Lists.newArrayList();
            // 检测有无过滤器
            if (entityFilter != null) {
                for (Entity entity : nearbyEntities) {
                    if (!entityFilter.test(entity)) {
                        entities.add(entity);
                    }
                }
            } else {
                entities = (List<Entity>) nearbyEntities;
            }

            // 获取首个实体
            if (entities.size() != 0) {
                hitEntityConsumer.accept(entities.get(0));
                return;
            }
        }

        if (currentStep > maxLength) {
            currentStep = 0D;
        }
    }

    public Vec getDirection() {
        return direction;
    }

    public Ray setDirection(Vec direction) {
        this.direction = direction;
        return this;
    }

    public double getMaxLength() {
        return maxLength;
    }

    public Ray setMaxLength(double maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public double getStep() {
        return step;
    }

    public Ray setStep(double step) {
        this.step = step;
        return this;
    }

    public double getRange() {
        return range;
    }

    public Ray setRange(double range) {
        this.range = range;
        return this;
    }

    public RayStopType getStopType() {
        return stopType;
    }

    public Ray setStopType(RayStopType stopType) {
        this.stopType = stopType;
        return this;
    }

    public Consumer<Entity> getHitEntityConsumer() {
        return hitEntityConsumer;
    }

    public Ray setHitEntityConsumer(Consumer<Entity> hitEntityConsumer) {
        this.hitEntityConsumer = hitEntityConsumer;
        return this;
    }

    public Predicate<Entity> getEntityFilter() {
        return entityFilter;
    }

    public Ray setEntityFilter(Predicate<Entity> entityFilter) {
        this.entityFilter = entityFilter;
        return this;
    }

    public enum RayStopType {
        /**
         * 固定长度(同时也是最大长度)
         */
        MAX_LENGTH,
        /**
         * 碰撞至实体时停止
         */
        HIT_ENTITY,
    }

}
