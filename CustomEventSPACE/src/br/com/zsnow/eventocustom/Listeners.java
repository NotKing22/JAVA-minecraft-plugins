package br.com.zsnow.eventocustom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import net.md_5.bungee.api.ChatColor;

public class Listeners implements Listener{

	@EventHandler
	public void pvpDisable(EntityDamageByEntityEvent e) {
		Entity damager = (Entity) e.getDamager();
		if (damager instanceof Player) {
			Player playerDamager = (Player) damager;
				
		if ((Main.EventoOcorrendo == true) && 
				(Main.pvp == false) && 
					(Main.participantes.contains(playerDamager))) {	
						if (!(playerDamager.hasPermission("customevent.staff"))) {
							playerDamager.sendMessage(Main.prefix + "§7§l: §cHey, o pvp do evento foi desativado.");
								e.setCancelled(true);
							}
						}
					}
				}
	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if ((Main.EventoOcorrendo == true) && 
				(Main.participantes.contains(p) && 
						(Main.frozen == true))) {	
							p.teleport(p.getLocation());
							e.setCancelled(true);
					}
				}
	@EventHandler
	public void playerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
			if (Main.participantes.contains(p)) {
				if (Main.Respawn == true) {
					getLoc.sendEnter(p);
					return;
				}
				Main.participantes.remove(p);
				getLoc.sendExit(p);
			
			if (Main.DropOnDeath == true) {
				e.getDrops().clear();
			}
			if (Main.DeathBroadcast == true) {
					String prefix = VaultHook.getPlayerPrefix(p.getName());
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes
							('&', Main.prefix + "§7§l: " +prefix+ p.getName() 
								+ " §eFoi eliminado do evento. §f(Resta §e" + Main.participantes.size() + " §fjogador(es)."));
			}
		}
	}
	@EventHandler
	public void playerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Main.participantes.contains(p)) {
			Main.participantes.remove(p);
				getLoc.sendExit(p);
					p.getInventory().clear();	
					p.getInventory().setArmorContents(null);
						for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
							p.removePotionEffect(AllPotionEffects.getType());
						}
						if (Main.DeathBroadcast == true) {
							String prefix = VaultHook.getPlayerPrefix(p.getName());
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes
								('&', Main.prefix + "§7§l: " +prefix+ p.getName() 
									+ " §eDesconectou do evento. §f(Restam §e" + Main.participantes.size() + " §fvivo(s)."));
						}
					}
				}
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if ((Main.EventoOcorrendo == true) &&
				(Main.participantes.contains(p) && 
					(Main.Quebrarblocos == true))) {
						if (!(p.hasPermission("customevent.staff"))) {
							p.sendMessage(Main.prefix + "§7§l: §cHey, quebrar blocos foi desativado no evento..");
							e.setCancelled(true);
						}
					}
				}
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if ((Main.EventoOcorrendo == true) &&
				(Main.participantes.contains(p) && 
					(Main.Colocarblocos == true))) {
						if (!(p.hasPermission("customevent.staff"))) {
							p.sendMessage(Main.prefix + "§7§l: §cHey, colocar blocos foi desativado no evento..");
							e.setCancelled(true);
						}
					}
				}
    @EventHandler
    public void SopaE(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getInventory().getItemInHand().getType() != Material.MUSHROOM_SOUP) return;
        	if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        		if (player.getHealth() == player.getMaxHealth()) return;
        			if (!Main.participantes.contains(player) || !Main.Sopa) return;
        				player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 7));
        					player.getInventory().getItemInHand().setType(Material.BOWL);;
        						player.updateInventory();
    }
    @EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent e) {
        boolean nullpointer = false;
        Player p = e.getPlayer();
        if ((e.getPlayer().getItemInHand().getType() == Material.COMPASS) && (Main.participantes.contains(p))) {
        	 e.setCancelled(true);
                for (Entity players : e.getPlayer().getNearbyEntities(100, 150, 100)) {
                    if ((players instanceof Player) && e.getPlayer().getLocation().distance(players.getLocation()) >= 10 && !com.weath.api.commands.vanish.getVanish().contains((Player) players)) {
                        if (players.getLocation().getY() > 170) {
                            return;
                        }
                        nullpointer = true;
                        String prefix = VaultHook.getPlayerPrefix(p.getName());
                        ChatColor.translateAlternateColorCodes('&', prefix);
                        e.getPlayer().setCompassTarget(players.getLocation());
                        e.getPlayer().sendMessage("§3§lBUSSOLA §eApontando para §7" + prefix + ((Player) players).getName());
                        return;
                    }
                }
                if (!nullpointer) {
                    e.getPlayer().sendMessage("§3§lBUSSOLA §cNenhum jogador por perto. Apontando para o Spawn!");
                    return;
                }
            } else {
            	return;
            }
        }
    
	 @EventHandler
	 public void worldChange(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		if (Main.EventoOcorrendo == true && Main.participantes.contains(p) && (!p.getWorld().getName().equalsIgnoreCase("eventos"))) {
			getLoc.sendEnter(p);
			p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
		}
	 }
    
}
	
