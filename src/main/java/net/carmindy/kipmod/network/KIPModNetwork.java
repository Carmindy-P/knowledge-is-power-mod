import net.carmindy.kipmod.network.AbilityUsePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class KIPModNetwork {
    public static void register() {
        PayloadTypeRegistry.playC2S().register(
                AbilityUsePayload.ID,
                AbilityUsePayload::decode,
                AbilityUsePayload::encode
        );
    }
}
