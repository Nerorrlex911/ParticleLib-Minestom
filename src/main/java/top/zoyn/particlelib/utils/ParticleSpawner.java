package top.zoyn.particlelib.utils;


import net.kyori.adventure.audience.Audience;
import net.minestom.server.Viewable;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ParticleSpawner {
    void spawn(@NotNull Pos pos,@NotNull Viewable audience);
}
