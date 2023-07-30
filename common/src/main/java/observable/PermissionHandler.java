package observable;

import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.minecraft.commands.CommandSourceStack;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class PermissionHandler {
    public static boolean hasPermission(UUID uuid, String node) {
        return getUser(uuid).getCachedData().getPermissionData().checkPermission(node).asBoolean();
    }

    public static boolean hasPermission(CommandSourceStack source, String node) {
        if (source.getEntity() == null) return true;

        return hasPermission(source.getEntity().getUUID(), node);
    }

    private static User getUser(UUID uuid) {
        User user = Observable.INSTANCE.getLUCKPERMS().getUserManager().getUser(uuid);
        if (user == null) {
            UserManager userManager = Observable.INSTANCE.getLUCKPERMS().getUserManager();
            CompletableFuture<User> userFuture = userManager.loadUser(uuid);

            user = userFuture.join();
        }

        return user;
    }
}
