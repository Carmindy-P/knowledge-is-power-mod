package net.carmindy.kipmod.abilities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;

public class ThornsAbility implements Abilities {

    @Override public String getId()   { return "thorns"; }
    @Override public String getName() { return "Thorns"; }
    @Override public String getDescription() { return "Thorns burst from the ground around you."; }
    @Override public boolean isOneTimeUse() { return false; }

    @Override
    public void activate(ServerPlayerEntity player) {
        if (player.getWorld().isClient()) return;

        ServerWorld world = player.getServerWorld();
        AbilitySettings cfg = AbilityRegistry.settings(getId());
        double radius = cfg.radius();
        int count     = cfg.times();

        double centerX = player.getX();
        double centerZ = player.getZ();

        for (int i = 0; i < count; i++) {
            double angle = (Math.PI * 2.0 * i) / count;
            double x = centerX + Math.cos(angle) * radius;
            double z = centerZ + Math.sin(angle) * radius;

            int bx = MathHelper.floor(x);
            int bz = MathHelper.floor(z);
            int by = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(bx, 0, bz)).getY();

            float yaw = (float) Math.toDegrees(angle);

            EvokerFangsEntity fangs = new EvokerFangsEntity(world, x, by, z, yaw, 0, player);
            world.spawnEntity(fangs);
        }

        player.sendMessage(Text.literal("Thorns erupt from the ground!"), false);
    }

    @Override public void tick(ServerPlayerEntity player) { }

    @Override public int getCooldownTicks() {
        return AbilityRegistry.settings(getId()).cooldownTicks();
    }

    @Override public void deactivate(ServerPlayerEntity player) { }
}