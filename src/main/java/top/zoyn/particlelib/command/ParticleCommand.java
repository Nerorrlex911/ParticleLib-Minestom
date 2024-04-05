package top.zoyn.particlelib.command;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import top.zoyn.particlelib.pobject.Astroid;

public class ParticleCommand extends Command {
    public ParticleCommand() {
        super("particlelib", "particle", "plib");
        ArgumentString particleId = ArgumentType.String("particleId");
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player)) return;
            switch (context.get(particleId)) {
                case "astroid":
                    Astroid astroid = new Astroid(1, ((Player) sender).getPosition());
                    astroid.setParticleSpawner((pos, audience) -> {
                        ParticlePacket particlePacket = new ParticlePacket(
                                Particle.END_ROD, true, pos.x(), pos.y(), pos.z(),
                                0.0f, 0.0f, 0.0f, 0.1f, 10
                        );
                        audience.sendPacketToViewers(particlePacket);
                    });
            }
        }, particleId);
    }
}
