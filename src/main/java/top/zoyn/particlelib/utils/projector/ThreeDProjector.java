package top.zoyn.particlelib.utils.projector;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

/**
 * 表示一个三维至三维投影器
 * <p>算法由 @Bryan33 提供</p>
 *
 * @author Zoyn
 * @since 2020/9/19
 */
public class ThreeDProjector {

    private final Pos origin;
    private final Vec n1;
    private final Vec n2;
    private final Vec n3;

    /**
     * @param origin 投影的原点
     * @param n      投影屏幕的法向量
     */
    public ThreeDProjector(Pos origin, Vec n) {
        this.origin = origin;
        Vec t = n;
        t = t.withY(t.y() + 1);
        this.n1 = n.cross(t).normalize();
        this.n2 = n1.cross(n).normalize();
        this.n3 = n.normalize();
    }

    public Pos apply(double x, double y, double z) {
        Vec r = n1.mul(x).add(n2.mul(z)).add(n3.mul(y));
        return origin.add(r);
    }

}
