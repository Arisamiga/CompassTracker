package me.arisamiga.CompassTracker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		System.out.println("Successfully loaded CompassTracker!!");
	}

	@Override
	public void onDisable() {
		System.out.println("Successfully shutdown CompassTracker!!");
	}

	public static LinkedList<Player> hider = new LinkedList<Player>();
	public static LinkedList<Player> finders = new LinkedList<Player>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// help command
		if (label.equals("help-ct")) {
			if (sender instanceof Player) {
				// player
				Player player = (Player) sender;
				player.sendMessage("Hello " + player.getDisplayName());
				player.sendMessage("-----------------------------------");
				player.sendMessage("Use /hider " + ChatColor.GRAY + "" + "(username)" + ChatColor.WHITE + ""
						+ " to add a person for the compass to follow");
				player.sendMessage("Use /finder " + ChatColor.GRAY + "" + "(username)" + ChatColor.WHITE + ""
						+ " to add the person that has the compass");
				player.sendMessage(
						"Use /players" + ChatColor.WHITE + "" + " to see who is a Hider and who is a Finder.");
				player.sendMessage(ChatColor.GRAY + "" + "To reset the roles you should use /reload.");
				return true;
			} else {
				// console
				sender.sendMessage("You cant use this plugin in console.");
				return true;
			}

		}
		if (label.equals("finder")) {
			if (sender instanceof Player) {
				// player
				Player player = (Player) sender;
				ItemStack[] items = { new ItemStack(Material.COMPASS) };
				if (finders.contains(Bukkit.getPlayerExact(args[0]))) {
					player.sendMessage(ChatColor.RED + "" + "You are already a finder.");
				} else {
					if (args.length == 0) {
						if (hider.isEmpty()) {
							player.sendMessage(ChatColor.RED + "" + "You need to first add a hider.");
						} else {
							player.sendMessage(ChatColor.RED + "" + "You are a finder!");
							player.getInventory().addItem(items);
							Bukkit.broadcastMessage(ChatColor.RED + "" + player.getName() + " Is now a Finder!");
							new Timer().scheduleAtFixedRate(new TimerTask() {
								@Override
								public void run() {
									player.setCompassTarget(hider.get(0).getLocation());
								}
							}, 0, 1000);
						}
					} else {
						// Player typed something more
						Player target = Bukkit.getPlayerExact(args[0]);
						if (target == null) {
							// Target is not online
							player.sendMessage(args[0] + " is not online!");
						} else {
							// Targets online
							if (hider.isEmpty()) {
								player.sendMessage(ChatColor.RED + "" + "You need to first add a hider.");
							} else {
								target.sendMessage(ChatColor.RED + "" + "You are a finder!");
								target.getInventory().addItem(items);
								Bukkit.broadcastMessage(ChatColor.RED + "" + target.getName() + " Is now a Finder!");
								finders.push(target);
								new Timer().scheduleAtFixedRate(new TimerTask() {
									@Override
									public void run() {
										target.setCompassTarget(hider.get(0).getLocation());
									}
								}, 0, 1000);
							}
						}
					}
				}
			} else {
				// console
				sender.sendMessage("You cant use this plugin in console.");
				return true;
			}

		}
		if (label.equals("hider")) {
			Player player = (Player) sender;
			if (sender instanceof Player) {
				if (hider.isEmpty() != true) {
					player.sendMessage(ChatColor.RED + "" + "You cannot have more than 1 hider.");
				} else {
					if (args.length == 0) {
						// player
						player.sendMessage(ChatColor.RED + "" + "You are a Hider!");
						hider.push(player);
						Bukkit.broadcastMessage(ChatColor.RED + "" + player.getName() + " Is now a hider!");
					} else {
						// Player typed something more
						Player target = Bukkit.getPlayerExact(args[0]);
						if (target == null) {
							// Target is not online
							player.sendMessage(args[0] + " is not online!");
						} else {
							// Targets online
							// player
							target.sendMessage(ChatColor.RED + "" + "You are a Hider!");
							hider.push(target);
							Bukkit.broadcastMessage(ChatColor.RED + "" + target.getName() + " Is now a hider!");
						}
					}
				}

			} else {
				// console
				sender.sendMessage("You cant use this plugin in console.");
				return true;
			}

		}
		// /player list!
		if (label.equals("players")) {
			if (sender instanceof Player) {
				// player
				Player player = (Player) sender;
				player.sendMessage("-----------------------------------");
				if (hider.isEmpty() == true) {
					player.sendMessage(ChatColor.GRAY + "" + "(Hiders) " + ChatColor.WHITE + "" + "No Hiders");
				} else {
					player.sendMessage(ChatColor.GRAY + "" + "(Hiders) ");
					player.sendMessage(ChatColor.WHITE + "" + hider.get(0).getDisplayName());
				}
				if (finders.isEmpty() == true) {
					player.sendMessage(ChatColor.GRAY + "" + "(Finders) " + ChatColor.WHITE + "" + "No Finders");
				} else {
					player.sendMessage(ChatColor.GRAY + "" + "(Finders) ");
					for (int i = 0; i < finders.size(); i++) {
						player.sendMessage(ChatColor.WHITE + "" + finders.get(i).getDisplayName());
					}
				}
				player.sendMessage(ChatColor.GRAY + "" + "To reset the roles you should use /reload.");
				return true;
			} else {
				// console
				sender.sendMessage("You cant use this plugin in console.");
				return true;
			}

		}

		return false;

	}
}
