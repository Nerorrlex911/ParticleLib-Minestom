package top.zoyn.particlelib.utils.projector;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

import java.util.function.BiFunction;

/**
 * 表示一个二维至三维投影器
 * <p>算法由 @Bryan33 提供</p>
 *
 * @author Zoyn
 * @since 2020/9/19
 */
public class TwoDProjector {

    private final Pos origin;
    private final Vec n1;
    private final Vec n2;

    /**
     * @param origin 投影的原点
     * @param n      投影屏幕的法向量
     */
    public TwoDProjector(Pos origin, Vec n) {
        this.origin = origin;
        Vec t = n;
        t = t.withY(t.y() + 1);
        this.n1 = n.cross(t).normalize();
        this.n2 = this.n1.cross(n).normalize();
    }

    /**
     * 创建二维至三维投影器
     * 此方法返回的是BiFunction, 可以不用直接调用构造器
     *
     * @param loc 投影的原点
     * @param n   投影屏幕的法向量
     * @return {@link BiFunction}
     */
    public static BiFunction<Double, Double, Pos> create2DProjector(Pos loc, Vec n) {
        Vec t = n;
        t = t.withY(t.y() + 1);
        Vec n1 = n.cross(t).normalize();
        Vec n2 = n1.cross(n).normalize();
        return (x, y) -> {
            Vec r = n1.mul(x).add(n2.mul(y));
            return loc.add(r);
        };
    }

    public Pos apply(double x, double y) {
        Vec r = n1.mul(x).add(n2.mul(y));
        return origin.add(r);
    }

}
