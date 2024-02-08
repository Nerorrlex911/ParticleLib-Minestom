package top.zoyn.particlelib.pobject;

import net.kyori.adventure.audience.Audience;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import top.zoyn.particlelib.ParticleLib;
import top.zoyn.particlelib.utils.ParticleSpawner;
import top.zoyn.particlelib.utils.matrix.Matrix;
import top.zoyn.particlelib.utils.scheduler.MinestomRunnable;
import top.zoyn.particlelib.utils.scheduler.MinestomTask;

import java.util.List;

/**
 * 表示一个特效对象
 *
 * @author Zoyn IceCold
 */
public abstract class ParticleObject {

    private Pos origin;

    private ShowType showType = ShowType.NONE;
    private MinestomTask task;
    private long period;
    private boolean running = false;

    private ParticleSpawner particleSpawner;
    
    private Entity entity;

    private Audience audience;
    /**
     * X的变化量
     */
    private double incrementX;
    private double incrementY;
    private double incrementZ;
    /**
     * 表示该特效对象所拥有的矩阵
     */
    private Matrix matrix;

    /**
     * 将计算好的粒子展示位置以列表的方式返回
     *
     * @return 粒子位置列表
     */
    public abstract List<Pos> calculateLocations();

    /**
     * 将特效对象展示
     */
    public abstract void show();

    /**
     * 将特效对象持续地展示
     */
    public void alwaysShow() {
        turnOffTask();

        // 此处的延迟 2tick 是为了防止turnOffTask还没把特效给关闭时的缓冲
        ParticleLib.getScheduler().runTaskLater(ParticleLib.getInstance(), () -> {
            running = true;
            task = new MinestomRunnable() {
                @Override
                public void run() {
                    if (!running) {
                        return;
                    }
                    show();
                }
            }.runTaskTimer(ParticleLib.getInstance(), 0L, period);

            setShowType(ShowType.ALWAYS_SHOW);
        }, 2L);
    }

    /**
     * 将特效对象持续地异步地展示
     */
    public void alwaysShowAsync() {
        turnOffTask();

        // 此处的延迟 2tick 是为了防止turnOffTask还没把特效给关闭时的缓冲
        ParticleLib.getScheduler().runTaskLater(ParticleLib.getInstance(), () -> {
            running = true;
            task = new MinestomRunnable() {
                @Override
                public void run() {
                    if (!running) {
                        return;
                    }
                    show();
                }
            }.runTaskTimerAsynchronously(ParticleLib.getInstance(), 0L, period);

            setShowType(ShowType.ALWAYS_SHOW_ASYNC);
        }, 2L);
    }

    /**
     * 将特效对象持续地播放
     */
    public void alwaysPlay() {
        if (!(this instanceof Playable)) {
            try {
                throw new NoSuchMethodException("该粒子特效不支持播放!");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return;
            }
        }
        Playable playable = (Playable) this;
        turnOffTask();

        // 此处的延迟 2tick 是为了防止turnOffTask还没把特效给关闭时的缓冲
        ParticleLib.getScheduler().runTaskLater(ParticleLib.getInstance(), () -> {
            running = true;
            task = new MinestomRunnable() {
                @Override
                public void run() {
                    if (!running) {
                        return;
                    }
                    playable.playNextPoint();
                }
            }.runTaskTimer(ParticleLib.getInstance(), 0L, period);

            setShowType(ShowType.ALWAYS_PLAY);
        }, 2L);
    }

    /**
     * 将特效对象持续地异步地播放
     */
    public void alwaysPlayAsync() {
        if (!(this instanceof Playable)) {
            try {
                throw new NoSuchMethodException("该粒子特效不支持播放!");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return;
            }
        }
        Playable playable = (Playable) this;
        turnOffTask();

        // 此处的延迟 2tick 是为了防止turnOffTask还没把特效给关闭时的缓冲
        ParticleLib.getScheduler().runTaskLater(ParticleLib.getInstance(), () -> {
            running = true;
            task = new MinestomRunnable() {
                @Override
                public void run() {
                    if (!running) {
                        return;
                    }
                    playable.playNextPoint();
                }
            }.runTaskTimerAsynchronously(ParticleLib.getInstance(), 0L, period);

            setShowType(ShowType.ALWAYS_PLAY_ASYNC);
        }, 2L);
    }

    /**
     * 将正在展示或播放的特效关闭
     */
    public void turnOffTask() {
        if (task != null) {
            running = false;
            task.cancel();
            setShowType(ShowType.NONE);
        }
    }

    /**
     * 给该特效对象叠加一个矩阵
     *
     * @param matrix 给定的矩阵
     * @return {@link ParticleObject}
     */
    public ParticleObject addMatrix(Matrix matrix) {
        if (this.matrix == null) {
            setMatrix(matrix);
            return this;
        }
        this.matrix = matrix.multiply(this.matrix);
        return this;
    }

    /**
     * 移除该特效对象的矩阵
     *
     * @return {@link ParticleObject}
     */
    public ParticleObject removeMatrix() {
        matrix = null;
        return this;
    }

    /**
     * 判断该特效对象是否拥有矩阵
     *
     * @return 当拥有矩阵时返回 true
     */
    public boolean hasMatrix() {
        return matrix != null;
    }

    /**
     * 得到该特效对象的矩阵
     *
     * @return {@link Matrix}
     */
    public Matrix getMatrix() {
        return matrix;
    }

    /**
     * 给该特效对象设置一个矩阵
     * <p>
     * 该方法将会直接覆盖之前所有已经变换好的矩阵
     *
     * @param matrix 给定的矩阵
     * @return {@link ParticleObject}
     */
    public ParticleObject setMatrix(Matrix matrix) {
        this.matrix = matrix;
        return this;
    }

    /**
     * 得到 X 轴上的增量
     *
     * @return X 轴的增量
     */
    public double getIncrementX() {
        return incrementX;
    }

    /**
     * 设置 {@link ParticleObject#spawnParticle(Pos)} 时 X 轴上的增量
     * <p>
     * 换言之是在 X 轴上固定移动 incrementX 个单位
     *
     * @param incrementX X 轴的增量
     * @return {@link ParticleObject}
     */
    public ParticleObject setIncrementX(double incrementX) {
        this.incrementX = incrementX;
        return this;
    }

    /**
     * 得到 Y 轴上的增量
     *
     * @return Y 轴的增量
     */
    public double getIncrementY() {
        return incrementY;
    }

    /**
     * 设置 {@link ParticleObject#spawnParticle(Pos)} 时 Y 轴上的增量
     * <p>
     * 换言之是在 Y 轴上固定移动 incrementY 个单位
     *
     * @param incrementY Y 轴的增量
     * @return {@link ParticleObject}
     */
    public ParticleObject setIncrementY(double incrementY) {
        this.incrementY = incrementY;
        return this;
    }

    /**
     * 得到 Z 轴上的增量
     *
     * @return Z 轴的增量
     */
    public double getIncrementZ() {
        return incrementZ;
    }

    /**
     * 设置 {@link ParticleObject#spawnParticle(Pos)} 时 Z 轴上的增量
     * <p>
     * 换言之是在 Z 轴上固定移动 incrementZ 个单位
     *
     * @param incrementZ Z 轴的增量
     * @return {@link ParticleObject}
     */
    public ParticleObject setIncrementZ(double incrementZ) {
        this.incrementZ = incrementZ;
        return this;
    }

    /**
     * 获取该特效对象的原点
     * <p>
     * 当特效对象内已经 attachEntity 时则会返回该实体的 Pos
     *
     * @return {@link Pos}
     */
    public Pos getOrigin() {
        if (entity != null) {
            return entity.getPosition();
        }
        return origin;
    }

    /**
     * 设置特效对象的原点
     *
     * @param origin 给定的原点
     * @return {@link ParticleObject}
     */
    public ParticleObject setOrigin(Pos origin) {
        this.origin = origin;
        return this;
    }

    /**
     * 得到特效对象的 周期(period) 参数
     *
     * @return 单位为 tick
     */
    public long getPeriod() {
        return period;
    }

    /**
     * 设置特效对象的 周期(period) 参数
     *
     * @param period 给定的周期参数
     * @return {@link ParticleObject}
     */
    public ParticleObject setPeriod(long period) {
        this.period = period;
        return this;
    }

    /**
     * 得到该特效对象的展示类型
     *
     * @return {@link ShowType}
     */
    public ShowType getShowType() {
        return showType;
    }

    /**
     * 设置该特效对象的展示类型
     *
     * @param showType 给定的展示类型
     * @return {@link ParticleObject}
     */
    public ParticleObject setShowType(ShowType showType) {
        this.showType = showType;
        return this;
    }

    public ParticleSpawner getParticleSpawner() {
        return particleSpawner;
    }

    public void setParticleSpawner(ParticleSpawner particleSpawner) {
        this.particleSpawner = particleSpawner;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    /**
     * 得到当前特效对象的所连接的实体
     *
     * @return {@link Entity}
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * 设置当前特效对象的所连接的实体
     * <p>
     * 该方法将会在 {@link ParticleObject#getOrigin()} 时自动替换成实体的实时 Pos
     *
     * @param entity 给定的实体
     * @return {@link ParticleObject}
     */
    public ParticleObject attachEntity(Entity entity) {
        this.entity = entity;
        return this;
    }

    /**
     * 通过给定一个坐标就可以使用已经指定的参数来播放粒子
     *
     * @param pos 坐标
     */
    public void spawnParticle(Pos pos) {
        Pos showPos = pos;
        if (hasMatrix()) {
            Vec vec = pos.sub(origin).asVec();
            Vec changed = matrix.applyVector(vec);

            showPos = origin.add(changed);
        }

        // 在这里可以设置一个XYZ的变化量
        showPos = showPos.add(incrementX, incrementY, incrementZ);
        
        particleSpawner.spawn(showPos,audience);
    }

    public void spawnColorParticle(Pos pos, int r, int g, int b) {
        Pos showPos = pos;
        if (hasMatrix()) {
            Vec vec = pos.sub(origin).asVec();
            Vec changed = matrix.applyVector(vec);

            showPos = origin.add(changed);
        }

        // 在这里可以设置一个XYZ的变化量
        showPos = showPos.add(incrementX, incrementY, incrementZ);
    }


}