import net.carmindy.kipmod.data.KIPModComponents;
import net.carmindy.kipmod.network.AbilityUsePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class AbilityPackets {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                AbilityUsePayload.ID,
                (payload, context) -> {
                    var player = context.player();
                    var component = KIPModComponents.ABILITIES.get(player);
                    if (component != null) {
                        component.tryUseAbility();
                    }
                }
        );
    }
}
