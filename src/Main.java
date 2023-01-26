package mc.ymn.rocket;

import org.bukkit.ChatColor;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, CommandExecutor {
    @Override
            public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("takebomber").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used in-game.");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("takebomber")) {
            ItemStack rocketBow = new ItemStack(Material.BOW);
            ItemMeta rocketBowMeta = rocketBow.getItemMeta();
            rocketBowMeta.setDisplayName(ChatColor.YELLOW + "Bomber Bow");
            rocketBowMeta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
            rocketBow.setItemMeta(rocketBowMeta);
            player.getInventory().addItem(rocketBow);
            player.sendMessage(ChatColor.GREEN + "Your Bomber Bow has been successfully delivered!");
            return true;
        }
        return false;
    }
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            ItemStack item = player.getInventory().getItemInHand();
            if (item.getType() == Material.BOW && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                    && item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Bomber Bow")) {
                event.getEntity().setMetadata("rocket", new FixedMetadataValue(this, true));
                event.getEntity().getWorld().playSound(event.getEntity().getLocation(), org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
            }
        }
    }


    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().hasMetadata("rocket")) {
            event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 4.0F);
        }
    }



}