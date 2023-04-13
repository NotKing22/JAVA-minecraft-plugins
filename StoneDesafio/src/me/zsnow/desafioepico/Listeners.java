package me.zsnow.desafioepico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;

import me.zsnow.desafioepico.configAPI.ActionBarAPI;
import me.zsnow.desafioepico.configAPI.BossBar;
import me.zsnow.desafioepico.configAPI.Configs;
import me.zsnow.desafioepico.configAPI.LocationAPI;
import me.zsnow.desafioepico.configAPI.NBTItemStack;
import me.zsnow.desafioepico.configAPI.Particles;
import me.zsnow.desafioepico.configAPI.VaultHook;
import me.zsnow.desafioepico.controller.EntityController;
import me.zsnow.desafioepico.controller.EventController;
import me.zsnow.desafioepico.controller.MenuController;
import net.minecraft.server.v1_8_R3.EnumParticle;


public class Listeners implements Listener {

	// clica no nether botao direito, nele aparece drops e crafts || CraftingTable e algo que represente drops
	
    int CoinDropTOP1 = 12;
    int CoinDropTOP2 = 9;
    int CoinDropTOP3 = 6;
	
	HashMap<String, Integer> damageBOSS = new HashMap<>();
	ArrayList<EntityType> entidades = new ArrayList<EntityType>();
	String cavaleiro = "cavaleironegro-servo";
	String servosNether = "netherservos";
	String[] cavaleiroFrases = new String[] {
			"§3[Cavaleiro-Negro] §cEu nunca deixarei alguém como vocês invadirem este lugar!", 
			"§3[Cavaleiro-Negro] §cVocês são muitos, mas não possuem a menor chance.", 
			"§3[Cavaleiro-Negro] §cEsmagarei o pingo de esperança que ainda lhes restam.", 
			"§3[Cavaleiro-Negro] §cSe ajoelhem perante a mim, e eu serei benevolente em suas mortes.", 
			"§3[Cavaleiro-Negro] §cSua técnica com a espada é medíocre! Apenas aceite sua morte iminente."};
	String prefix = "§4[✞] ";
	String[] mortesMensagens = new String[] { 
			prefix + "§8$p§c Viu suas entranhas pularem para fora enquanto batalhava e morreu.",
			prefix + "§8$p§c Sofreu uma hemorragia interna e morreu para as criaturas.",
			prefix + "§8$p§c Teve sua cabeça separada do seu corpo e morreu.",
			prefix + "§8$p§c Cometeu um deslize e teve seu orgão vital perfurado.",
			prefix + "§8$p§c Foi transformado em um monte de carne para as criaturas.",
			prefix + "§8$p§c Teve seu corpo mutilado pelas criaturas.",
			prefix + "§8$p§c Foi massacrado pelas criaturas.",
			prefix + "§8$p§c Se entregou ao seu inevitável destino cruel e morreu.",
			prefix + "§8$p§c Foi empalado vivo enquanto lutava bravamente.",
			prefix + "§8$p§c Teve sua carne arrancada de seus ossos."};
	
	@EventHandler
	public void entityAttack(EntityDamageByEntityEvent e) {
		EventController evento = EventController.admin;
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			final Player assassino = (Player) e.getDamager();
			final Player vitima = (Player) e.getEntity(); //1334
			ItemStack itemHand = assassino.getItemInHand();
 		    NBTItemStack nbtFinalItemStack = new NBTItemStack(itemHand);
			if (itemHand != null && itemHand.getType() != Material.AIR && nbtFinalItemStack.getInteger("value") == 1334 && 
					itemHand.hasItemMeta() && itemHand.getItemMeta().hasLore()) {
				int random = ThreadLocalRandom.current().nextInt(100);
				if (random <= 9) { // 9
					vitima.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
					assassino.sendMessage("§c[@] §6A carminita fez "+vitima.getName()+" queimar!");
				}
			}
		}
		if(e.getDamager() instanceof Arrow){
			final Arrow arrow = (Arrow) e.getDamager();
			if(arrow.getShooter() instanceof LivingEntity && e.getEntity() instanceof LivingEntity) {
					final LivingEntity shooter = (LivingEntity) arrow.getShooter();
					final LivingEntity victim = (LivingEntity) e.getEntity();
				 if (victim.hasMetadata("netherservos") || victim.hasMetadata("cavaleironegro-servo") || victim.hasMetadata("boss-nether")) {
					 if (!(shooter instanceof Player)) {
					 e.setCancelled(true);
					 }
				}
			} 
		if (arrow.getShooter() instanceof Player && e.getEntity() instanceof Creature) {
			final int percentage = ThreadLocalRandom.current().nextInt(100);
				final Player shooter = (Player) arrow.getShooter();
					final Creature victim = (Creature) e.getEntity();
						final int hp = (int) victim.getHealth();
							final int maxHealth = (int) victim.getMaxHealth();
				if (victim.hasMetadata(servosNether)) {
					if (victim.getType().equals(EntityType.SKELETON)) {
						victim.setCustomName("§4§l✠ §8§lArqueiro Carminidio §a" + (int) hp + "§7/§a" + (int) maxHealth + " §c❤");
						shooter.playSound(shooter.getLocation(), Sound.SHOOT_ARROW, 1.0f, 0.5f);
							if (percentage <= 20) {
								victim.setTarget(shooter);
							}
							return;
					}
					if (victim.getType().equals(EntityType.PIG_ZOMBIE)) {
						victim.setCustomName("§4§l✠ §8§lGuardião do submundo §a" + (int) hp + "§7/§a" + (int) maxHealth + " §c❤");
						shooter.playSound(shooter.getLocation(), Sound.SHOOT_ARROW, 1.0f, 0.5f);
						return;
					}
				}
				if (victim.hasMetadata(cavaleiro)) {
					victim.setCustomName("§4§l✠ §8§lCavaleiro negro §a" + hp + "§7/§a" + maxHealth + " §c❤");
					shooter.playSound(shooter.getLocation(), Sound.SHOOT_ARROW, 1.0f, 0.5f);
						if (percentage <= 20) {
							victim.setTarget(shooter);
						}
					return;
				}
			}
		}
	if (evento.getNetherOcorrendo() == true) {
		if (e.getEntity() != null && e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Player) { 
			final int percentage = ThreadLocalRandom.current().nextInt(100);
			final Player p = (Player) e.getDamager();
			final LivingEntity entity =  (LivingEntity) e.getEntity();
			final int hp = (int) entity.getHealth();
			final int maxHealth = (int) entity.getMaxHealth();
				if (e.getEntity().hasMetadata(cavaleiro)) {
					e.getEntity().setCustomName("§4§l✠ §8§lCavaleiro negro §a" + hp + "§7/§a" + maxHealth + " §c❤");
						if (percentage<=10) {
							e.setCancelled(true);
							p.sendMessage("§cO cavaleiro negro defendeu o seu ataque!");
							p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0f, 0.5f);
						}
						if (percentage<=20) {
							p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 0.5f);
							e.getEntity().teleport(p);
						}
						if (percentage==0) {
							p.sendMessage(cavaleiroFrases[new Random().nextInt(cavaleiroFrases.length)]);
						}
					}
				if (e.getEntity().hasMetadata(servosNether)) {
					if (entity.getType().equals(EntityType.PIG_ZOMBIE)) {
							e.getEntity().setCustomName("§4§l✠ §8§lGuardião do submundo §a" + hp + "§7/§a" + maxHealth + " §c❤");
						}
						if (entity.getType().equals(EntityType.SKELETON)) {
							e.getEntity().setCustomName("§4§l✠ §8§lArqueiro Carminidio §a" + hp + "§7/§a" + maxHealth + " §c❤");
						}
						if (percentage <= 20) {
							((Creature) entity).setTarget(p);
						}
						return;
					}
				
				
				if (e.getEntity().hasMetadata("boss-nether")) {
						if (EntityController.NetherBossHP <= e.getDamage()) {
							e.getEntity().remove();
							
							for (Player participantes : EventController.admin.getNetherParticipantes()) {
								participantes.playSound(participantes.getLocation(), Sound.ENDERDRAGON_DEATH, 1.0f, 0.5f);
								participantes.sendMessage("");
								participantes.sendMessage("§6§lHADES: §cImpossível! Como vocês puderam me derrotar? §lEU IREI VOLTAR MAIS FORTE");
								participantes.sendMessage("");
							}
							EventController.admin.setMortalhaDaNoiteStatus(false);
							endInvasion();
								getNetherTOP3(p, p);
						         
						          for (Player participantes : EventController.admin.getNetherParticipantes()) {
						        	  
										double pontuacao;
								          if (damageBOSS.containsKey(participantes.getName())) {
								        	  pontuacao = damageBOSS.get(participantes.getName());
								          } else {
								        	  pontuacao = 0;
								          }
						        	  
							         participantes.sendMessage("                     §eSeu dano total: §a" + pontuacao +
							  	        	" §c❤ §d§l(+" + (EventController.admin.playerCoinsNether.containsKey(participantes) ? 
							  	        	EventController.admin.playerCoinsNether.get(participantes) : "0") 
							   		     + " §d§lmoeda§7(s)§d§l)");
							         
							         //moedas vindo extra por conta do getnethertop3 + isso aqui
							         	BossBar.removeStatusBar(participantes);
						          }
						          EventController.admin.playerCoinsNether.clear();
						          Bukkit.broadcastMessage("§A§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

									
							  
							  
								  for (Player participantes : evento.getNetherParticipantes()) {
									  LocationAPI.sendTo(participantes, "SAIDA-NETHER");
									}
							  
							// criar o Stopper que finaliza absolutamente tudo
							  return;
						}
						
						if (!damageBOSS.containsKey(p.getName())) {
							damageBOSS.put(p.getName(), 0);
						}
						int danoAtual = (int) e.getDamage();
						int danoAntigo = (int) damageBOSS.get(p.getName());
						damageBOSS.put(p.getName(), ((danoAntigo + danoAtual)));
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0f, 0.5f);
						
						EntityController.setNetherBossHP((int) (EntityController.getNetherBossHP() - e.getDamage()));
						ActionBarAPI.sendActionBarMessage(p, e.getEntity().getName() + " §4§l❤§c" + EntityController.NetherBossHP);
			} // vem ate aq
		}
		if(e.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getDamager();
				if(arrow.getShooter() instanceof Player) {
				Player p = (Player) arrow.getShooter();
				//Player victim = (Player) e.getEntity();
				
				if (e.getEntity().hasMetadata("boss-nether")) {
					if (EntityController.NetherBossHP <= e.getDamage()) {
						e.getEntity().remove();
						
						for (Player participantes : EventController.admin.getNetherParticipantes()) {
							participantes.playSound(participantes.getLocation(), Sound.ENDERDRAGON_DEATH, 1.0f, 0.5f);
							participantes.sendMessage("");
							participantes.sendMessage("§6§lHADES: §eParece que acabou, eu fui derrotado por meros humanos!? Patético!");
							participantes.sendMessage("");
						}
						EventController.admin.setMortalhaDaNoiteStatus(false);
						endInvasion();
							getNetherTOP3(p, p);
					         
					          for (Player participantes : EventController.admin.getNetherParticipantes()) {
					        	  
									double pontuacao;
							          if (damageBOSS.containsKey(participantes.getName())) {
							        	  pontuacao = damageBOSS.get(participantes.getName());
							          } else {
							        	  pontuacao = 0;
							          }
					        	  
						         participantes.sendMessage("                     §eSeu dano total: §a" + pontuacao +
						  	        	" §c❤ §d§l(+" + (EventController.admin.playerCoinsNether.containsKey(participantes) ? 
						  	        	EventController.admin.playerCoinsNether.get(participantes) : "0") 
						   		     + " §d§lmoeda§7(s)§d§l)");
						         
						         //moedas vindo extra por conta do getnethertop3 + isso aqui
						         	BossBar.removeStatusBar(participantes);
					          }
					          EventController.admin.playerCoinsNether.clear();
					          Bukkit.broadcastMessage("§A§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
							
							  for (Player participantes : evento.getNetherParticipantes()) {
								  LocationAPI.sendTo(participantes, "SAIDA-NETHER");
								}
						  
						// criar o Stopper que finaliza absolutamente tudo
						  return;
					}
					if (!damageBOSS.containsKey(p.getName())) {
						damageBOSS.put(p.getName(), 0);
					}
					int danoAtual = (int) e.getDamage();
					int danoAntigo = (int) damageBOSS.get(p.getName());
					damageBOSS.put(p.getName(), ((danoAntigo + danoAtual)));
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0f, 0.5f);
					
					EntityController.setNetherBossHP((int) (EntityController.getNetherBossHP() - e.getDamage()));
					ActionBarAPI.sendActionBarMessage(p, e.getEntity().getName() + " §4§l❤§c" + EntityController.NetherBossHP);
		/*	} else {
				e.setCancelled(true);
				return;
			}*/
		}
				
				
			}
		}
			
			// quando o mob ataca
			if (e.getDamager() instanceof Skeleton && e.getEntity() instanceof Player) {
				if (e.getDamager().hasMetadata(cavaleiro)) {
					int random = ThreadLocalRandom.current().nextInt(100);
					if (random<=10) {
						Player p = (Player) e.getEntity();
						e.setCancelled(true);
						p.setVelocity(new Vector(0, 2, 0));
						p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 2));
						p.sendMessage("§c§lVocê foi arremessado para cima.");
					}
				}
			}
			if (e.getDamager() instanceof MagmaCube && e.getEntity() instanceof Player) {
				if(e.getDamager().hasMetadata("boss-nether")) {
					final Player p = (Player) e.getEntity();
					int percentage = ThreadLocalRandom.current().nextInt(100);
						if (percentage <= 10) {
							final int damage = 6*2;
							if (p.getHealth() > damage) {
								p.setHealth(p.getHealth() - damage);
								p.sendMessage(" ");
								p.sendMessage("§c§lVocê recebeu acerto crítico em seu ponto vital.");
								p.sendMessage(" ");
								p.removePotionEffect(PotionEffectType.WEAKNESS);
								p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10*20, 2));
								return;
							} else {
								p.setHealth(0);
								p.sendMessage(" ");
								p.sendMessage("§c§lVocê recebeu acerto crítico em seu ponto vital.");
								p.sendMessage(" ");
								return;
							}
						}
						if (percentage <= 15) {
							final int damage = 3*2;
							if (p.getHealth() > damage) {
								p.setHealth(p.getHealth() - 3*2);
								p.sendMessage(" ");
								p.sendMessage("§c§lVocê recebeu um feitiço de fraqueza junto com um acerto crítico.");
								p.sendMessage(" ");
								p.removePotionEffect(PotionEffectType.WEAKNESS);
								p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10*20, 2));
								return;
							} else {
								p.setHealth(0);
								p.sendMessage(" ");
								p.sendMessage("§c§lVocê recebeu um feitiço de fraqueza junto com um acerto crítico.");
								p.sendMessage(" ");
								return;
							}
						}
					/*	if (percentage <= 20) {
							e.setCancelled(true);
							p.setVelocity(new Vector(0, 10, 0));
							p.removePotionEffect(PotionEffectType.BLINDNESS);
							p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 2));
							p.sendMessage(" ");
							p.sendMessage("§c§lVocê foi arremessado para cima pelo boss.");
							p.sendMessage(" ");
							return;
						}*/
						if (percentage <= 30) {
							final int damage = 3;
							if (p.getHealth() > damage) {
								p.setHealth(p.getHealth() - 3);
								p.sendMessage(" ");
								p.sendMessage("§c§lVocê recebeu um acerto crítico.");
								p.sendMessage(" ");
								p.removePotionEffect(PotionEffectType.CONFUSION);
								p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 4*20, 2));
								return;
							} else {
								p.setHealth(0);
								p.sendMessage(" ");
								p.sendMessage("§c§lVocê recebeu um acerto crítico.");
								p.sendMessage(" ");
								return;
							}
						}
					}
				}
			
			//}
		}
			
}
	String seta = "»";
	String setaMaior = "➣";
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		EventController evento = EventController.admin;
		Player p = (Player) e.getEntity();
		if (evento.getNetherParticipantes().contains(p)) {
			for (Player participantes : evento.getNetherParticipantes()) {
				if (evento.getNetherOcorrendo() == true && evento.getNetherEntradaLiberada() == false) {
					participantes.sendMessage(mortesMensagens[new Random().nextInt(mortesMensagens.length)].replace("$p", p.getName()));
				}
			}
			evento.getNetherParticipantes().remove(p);
			BossBar.removeStatusBar(p);
			LocationAPI.sendTo(p, "SAIDA-NETHER");
		}
		if (evento.getNetherOcorrendo() == true && evento.getNetherEntradaLiberada() == false && evento.getNetherParticipantes().size() == 0) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §6" + seta + " §7A invasão ao Nether falhou!");
			Bukkit.broadcastMessage(" §d" + seta + " §7Todos os guerreiros foram derrotados!");
			Bukkit.broadcastMessage("");
			EventController.admin.playerCoinsNether.clear();
			EntityController.setNetherBossHP(Configs.config.getConfig().getInt("NetherBoss.HP"));
			EventController.admin.setNetherEntradaLiberada(false);
			EventController.admin.setNetherOcorrendo(false);
			EventController.admin.setNetherTempoDecorrido(0);
			EventController.admin.setMortalhaDaNoiteStatus(false);
			for (World mundo : Bukkit.getWorlds()) {
				for (Entity entidades : mundo.getEntities()) {
					if (!(entidades instanceof Player) && (entidades.hasMetadata("boss-nether") || 
						(entidades.hasMetadata("cavaleironegro-servo") || (entidades.hasMetadata("netherservos"))))) {
						 entidades.remove();
					}
				}
			}
		}
		
	/*	if (evento.getEdenParticipantes().contains(p)) {
			for (Player participantes : evento.getEdenParticipantes()) {
				participantes.sendMessage(mortesMensagens[new Random().nextInt(mortesMensagens.length)].replace("$p", p.getName()));
			}
		}*/
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		EventController evento = EventController.admin;
		Player p = (Player) e.getPlayer();
		if (evento.getNetherParticipantes().contains(p)) {
			for (Player participantes : evento.getNetherParticipantes()) {
				participantes.sendMessage(mortesMensagens[new Random().nextInt(mortesMensagens.length)].replace("$p", p.getName()));
			}
		LocationAPI.sendTo(p, "SAIDA-NETHER");
		evento.getNetherParticipantes().remove(p);
		BossBar.removeStatusBar(p);
		if (evento.getNetherOcorrendo() == true && evento.getNetherEntradaLiberada() == false && evento.getNetherParticipantes().size() == 0) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §6" + seta + " §7A invasão ao Nether falhou!");
			Bukkit.broadcastMessage(" §d" + seta + " §7Todos os guerreiros foram derrotados!");
			Bukkit.broadcastMessage("");
			EventController.admin.playerCoinsNether.clear();
			EntityController.setNetherBossHP(Configs.config.getConfig().getInt("NetherBoss.HP"));
			EventController.admin.setNetherEntradaLiberada(false);
			EventController.admin.setNetherOcorrendo(false);
			EventController.admin.setNetherTempoDecorrido(0);
			EventController.admin.setMortalhaDaNoiteStatus(false);
			for (World mundo : Bukkit.getWorlds()) {
				for (Entity entidades : mundo.getEntities()) {
					if (!(entidades instanceof Player) && (entidades.hasMetadata("boss-nether") || 
						(entidades.hasMetadata("cavaleironegro-servo") || (entidades.hasMetadata("netherservos"))))) {
						 entidades.remove();
					}
				}
			}
		}
	}
}
	
	// pra add multi efeito faz o seguinte. Remove a lore, add a nova lore do enchant e dps poem a lore antiga de volta
	// salva a lore antiga numa variavel, mas teria q tirar o enchant antigo de alguma forma
	  @EventHandler
	  public void minerar(BlockBreakEvent event) {
		    if (event.isCancelled())
		      return; 
		    Player p = event.getPlayer();
		    Block bloco = event.getBlock();
		    ItemStack hand = p.getItemInHand();
		    //HASTE
	    if (hand.hasItemMeta() && hand.getItemMeta().hasLore()) {
		   /* if (bloco.getType().name().endsWith("_ORE") || bloco.getType().name().endsWith("_BLOCK")) {
		    	p.removePotionEffect(PotionEffectType.FAST_DIGGING);
		    	p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20, 2));
		    }*/
		    // EXPLOSIVE
	/*		NBTItemStack nbtItemStack = new NBTItemStack(hand);
			if ( NBTItemStack(hand).getInteger("Value") == 1) {
				
			}*/
		    
		    ItemStack finalItemStack = hand;
		    NBTItemStack nbtFinalItemStack = new NBTItemStack(finalItemStack);
		    
	    if (nbtFinalItemStack.getInteger("value") == 322) {
		    for(String lore : hand.getItemMeta().getLore()) {
		    	if (lore.contains("Explosive I")) {
			    if (p.getItemInHand().getType().name().endsWith("_PICKAXE") && 
		    		bloco.getType().name().endsWith("_ORE") || bloco.getType().name().endsWith("_BLOCK") || bloco.getType().name().equals("STONE")) {
			    	p.getItemInHand().setDurability((short) (p.getItemInHand().getDurability() +1));
			    	p.playSound(p.getLocation(), Sound.EXPLODE, 0.5f, 0.5f);
			    	for (int x = -1; x <= 1; x++) {
			          for (int y = -1; y <= 1; y++) {
			            for (int z = -1; z <= 1; z++) {
			              Block blocos = bloco.getRelative(x, y, z);
			                if (blocos.getType() == Material.NOTE_BLOCK) { //anti bug minapv
			                	event.setCancelled(true);
			                	p.sendMessage("§cA área de explosão possui blocos que você não pode quebrar.");
			                	return;
			                }
			              if (blocos.getType().name().endsWith("_ORE") || blocos.getType().name().endsWith("_BLOCK") || blocos.getType().name().equals("STONE"))
			            	 if (Main.getInstance().wGuard.canBuild(p, blocos)) {
			            		 if (p.getWorld().getName().equalsIgnoreCase("PlotMe")) {
			            			 Plot plotlimit = new PlotAPI().getPlot(blocos.getLocation());
			            			 if (plotlimit != null) {
			            				 if (plotlimit.getOwners().contains(p.getUniqueId()) || plotlimit.getTrusted().contains(p.getUniqueId())) {
			 				                Collection<ItemStack> drops = blocos.getDrops(p.getItemInHand());
							                for (ItemStack drop : drops) {
							                    event.getPlayer().getInventory().addItem(drop);
							                }
							                blocos.setType(Material.AIR); 
							                blocos.breakNaturally();
							                  Particles packet = new Particles(EnumParticle.EXPLOSION_NORMAL, 
							                  blocos.getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 3);
							                  packet.sendToPlayer(p);
			            				 } else {
			            					 event.setCancelled(true);
			            					 return;
			            				 }
			            			 } else {
			            				 event.setCancelled(true);
			            				 return;
			            			 }
			            		 }
				                Collection<ItemStack> drops = blocos.getDrops(p.getItemInHand());
				                for (ItemStack drop : drops) {
				                    event.getPlayer().getInventory().addItem(drop);
				                }
				                blocos.setType(Material.AIR); 
				                blocos.breakNaturally();
				                  Particles packet = new Particles(EnumParticle.EXPLOSION_NORMAL, 
				                  blocos.getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 3);
				                  packet.sendToPlayer(p);
			            	}
			            }
		             }
		         }
		      }
		    if (p.getItemInHand().getType().name().endsWith("_SPADE") && 
		    		bloco.getType().name().equals("DIRT") || (bloco.getType().name().equals("GRASS"))) {
		    		p.getItemInHand().setDurability((short) (p.getItemInHand().getDurability() +1));
		    			p.playSound(p.getLocation(), Sound.EXPLODE, 0.5f, 0.5f);
				    	for (int x = -1; x <= 1; x++) {
					          for (int y = -1; y <= 1; y++) {
					            for (int z = -1; z <= 1; z++) {
					              Block blocos = bloco.getRelative(x, y, z);
					                if (blocos.getType() == Material.NOTE_BLOCK) { //anti bug minapv
					                	event.setCancelled(true);
					                	p.sendMessage("§cA área de explosão possui blocos que você não pode quebrar.");
					                	return;
					                }
					              if (blocos.getType().name().equals("DIRT") || (blocos.getType().name().equals("GRASS")))
					            	 if (Main.getInstance().wGuard.canBuild(p, blocos)) {
					            		 if (p.getWorld().getName().equalsIgnoreCase("PlotMe")) {
					            			 Plot plotlimit = new PlotAPI().getPlot(blocos.getLocation());
					            			 if (plotlimit != null) {
					            				 if (plotlimit.getOwners().contains(p.getUniqueId()) || plotlimit.getTrusted().contains(p.getUniqueId())) {
					 				                Collection<ItemStack> drops = blocos.getDrops(p.getItemInHand());
									                for (ItemStack drop : drops) {
									                    event.getPlayer().getInventory().addItem(drop);
									                }
									                blocos.setType(Material.AIR); 
									                blocos.breakNaturally();
									                  Particles packet = new Particles(EnumParticle.EXPLOSION_NORMAL, 
									                  blocos.getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 3);
									                  packet.sendToPlayer(p);
					            					 
					            				 } else {
					            					 event.setCancelled(true);
					            					 return;
					            				 }
					            			 } else {
					            				 event.setCancelled(true);
					            				 return;
					            			 }
					            		 }
						                Collection<ItemStack> drops = blocos.getDrops(p.getItemInHand());
						                for (ItemStack drop : drops) {
						                    event.getPlayer().getInventory().addItem(drop);
						                }
						                blocos.setType(Material.AIR); 
						                blocos.breakNaturally();
						                  Particles packet = new Particles(EnumParticle.EXPLOSION_NORMAL, 
						                  blocos.getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 3);
						                  packet.sendToPlayer(p);
					            	}
					            }
				             }
				         }
		    		}
		    	}
		    }
	    } 
	  }
  }
	  
	  String[] coindrop = new String[] { 
			  " ", 
			  " §6✮ §d§l+1 Moeda épica", 
			  " " };
	  
	  String[] coindrop2 = new String[] { 
			  " ", 
			  " §6✮ §d§l+3 Moeda épica", 
			  " " };
	  
	  
	  @EventHandler
	  public void entityDeath(EntityDeathEvent e) {
		  if (e.getEntity().getKiller() != null && (!(e.getEntity() instanceof Player))) {
			  Player assassino = (Player) e.getEntity().getKiller();
			  if (EventController.admin.getNetherParticipantes().contains(assassino) || 
					  EventController.admin.getEdenParticipantes().contains(assassino)) {
				  if (e.getEntity().hasMetadata(servosNether)) {
						e.getDrops().clear();
						int random = ThreadLocalRandom.current().nextInt(100);
						if (random <= 30) {
							assassino.sendMessage(coindrop);
							int coinValue = EventController.admin.playerCoinsNether.get(assassino);
							EventController.admin.playerCoinsNether.replace(assassino, coinValue + 1);
							EventController.admin.setCoinsDroppedNether(EventController.admin.getCoinsDroppedNether() + 1); // not work
							return;
						}
			  	}
				  if (e.getEntity().hasMetadata(cavaleiro)) {
					  e.getDrops().clear();
						assassino.sendMessage(coindrop2);
						assassino.playSound(assassino.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.5f);
						int coinValue = EventController.admin.playerCoinsNether.get(assassino);
						EventController.admin.playerCoinsNether.replace(assassino, coinValue + 3);
						EventController.admin.setCoinsDroppedNether(EventController.admin.getCoinsDroppedNether() + 3);
						return;
		  		}
			  }
		  }
	  }
	  
	  @EventHandler
	  public void onJoin(PlayerJoinEvent e) {
		  EventController evento = EventController.admin;
		  Player p = e.getPlayer();
		  if (evento.getNetherEntradaLiberada() == true) {
			  p.sendMessage(" ");
			  p.sendMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §d➣ §7A invasão ao nether está prestes a começar, corra!");
			  p.sendMessage(" §7Digite ➣ §8/desafio");
			  p.sendMessage(" ");
		  }
		  if (evento.getEdenEntradaLiberada() == true) {
			  p.sendMessage(" ");
			  p.sendMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §d➣ §7A invasão ao Eden está prestes a começar, corra!");
			  p.sendMessage(" §7Digite ➣ §8/desafio");
			  p.sendMessage(" ");
		  }
	  }
	  
	  public void getNetherTOP3(Player p, Player golpeFinal) {
		  String prefixGF = VaultHook.getPlayerPrefix(golpeFinal.getName());
			 Bukkit.broadcastMessage("§A§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			 Bukkit.broadcastMessage("                          §6§lO BOSS HADES CAIU!");
			 Bukkit.broadcastMessage(" ");
			 Bukkit.broadcastMessage("                §f" + prefixGF + golpeFinal.getName() + " §7desferiu o golpe final");
			 Bukkit.broadcastMessage("                               §8§o(top 3 - dano total)");
			 Bukkit.broadcastMessage(" ");
			 //e 6 c
	          class ValueComparator implements Comparator<String> {
	 
	              Map<String, Integer> base;
	              public ValueComparator(HashMap<String, Integer> map) {
	                  this.base = map;
	              }
	 
	              public int compare(String a, String b) {
	                  if (base.get(a) >= base.get(b)) {
	                      return -1;
	                  } else {
	                      return 1;
	                  }
	              }
	          }
	 
	          damageBOSS.put("§cNinguém", 0);
	          damageBOSS.put("§c§cNinguém", 0);
	          damageBOSS.put("§c§c§cNinguém", 0);

	          
	          ValueComparator bvc =  new ValueComparator(damageBOSS);
	          TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
	          sorted_map.putAll(damageBOSS);
	 
	          // pega as moedas do top 1 e adiciona a quantia de moeda que ganha no top 1
	          MenuController item = new MenuController();
	          
	          for(int i = 1; i < 4; i ++) {
	              Entry<String, Integer> e = sorted_map.pollFirstEntry();
	              String pname = e.getKey();
	              double score = e.getValue();
	              
		       if (i == 1) {// §e§l         §6§l           §c§l         /
		    	   Player jogador = Bukkit.getServer().getPlayerExact(pname);
		    	   String prefixPlayer = VaultHook.getPlayerPrefix(pname);
		    	   Bukkit.broadcastMessage("§e§l         "+i+". §f" + prefixPlayer + pname +" §7- §e" + score + " §c❤ §d(§d§l+"+CoinDropTOP1+"§d moedas)");
		    	   final int coin = EventController.admin.playerCoinsNether.get(jogador) == null ? 0 : EventController.admin.playerCoinsNether.get(jogador);
		    	   EventController.admin.playerCoinsNether.replace(jogador, ((coin + CoinDropTOP1)));
		    	   EventController.admin.setCoinsDroppedNether(EventController.admin.getCoinsDroppedNether() + (coin + CoinDropTOP1));
		    	//   jogador.getInventory().addItem(item.giveMoeda(jogador, EventController.admin.playerCoinsNether.get(jogador)));
		       }
		       if (i == 2) {
		    	   Player jogador = Bukkit.getServer().getPlayerExact(pname);
		    	   String prefixPlayer = VaultHook.getPlayerPrefix(pname);
		    	   Bukkit.broadcastMessage("§6§l           "+i+". §f" + prefixPlayer + pname +" §7- §e" + score + " §c❤ §d(§d§l+"+CoinDropTOP2+"§d moedas)");
		    	  // final int coin = jogador == null ? 0 : EventController.admin.playerCoinsNether.get(jogador);
		    	   final int coin = EventController.admin.playerCoinsNether.get(jogador) == null ? 0 : EventController.admin.playerCoinsNether.get(jogador);
		    	   EventController.admin.playerCoinsNether.replace(jogador, ((coin + CoinDropTOP2)));
		    	   EventController.admin.setCoinsDroppedNether(EventController.admin.getCoinsDroppedNether() + (coin + CoinDropTOP2));
		       }
		       if (i == 3) {
		    	   Player jogador = Bukkit.getServer().getPlayerExact(pname);
		    	   String prefixPlayer = VaultHook.getPlayerPrefix(pname);
		    	   Bukkit.broadcastMessage("§c§l         "+i+". §f" + prefixPlayer + pname +" §7- §e" + score + " §c❤ §d(§d§l+"+CoinDropTOP3+"§d moedas)");
		    	   final int coin = EventController.admin.playerCoinsNether.get(jogador) == null ? 0 : EventController.admin.playerCoinsNether.get(jogador);
		    	   EventController.admin.playerCoinsNether.replace(jogador, ((coin + CoinDropTOP3)));
		    	   EventController.admin.setCoinsDroppedNether(EventController.admin.getCoinsDroppedNether() + (coin + CoinDropTOP3));
		       }
		   }
	          EventController evento = EventController.admin;
	          for (Player participantes : evento.getNetherParticipantes()) {
			         if (!EventController.admin.playerCoinsNether.containsKey(participantes)) {
			        	 EventController.admin.playerCoinsNether.put(participantes, 1);
			         }
			         if (EventController.admin.playerCoinsNether.get(participantes) > 0) {
			        	 participantes.getInventory().addItem(item.giveMoeda(participantes, EventController.admin.playerCoinsNether.get(participantes)));
			         }
			         LocationAPI.sendTo(participantes, "SAIDA-NETHER");
	          }
	          Bukkit.broadcastMessage(" ");  
	          EventController.admin.resetDataStats();
			 // damageBOSS.clear();
	          //damageBOSS.get(p.getName()
	          
	          // o dano total ta duplicando e os coins dropado tmb pros top
	 
	  }
	  
	  public static void endInvasion() {
		//	EventController.admin.getNetherParticipantes().clear();
			EntityController.setNetherBossHP(Configs.config.getConfig().getInt("NetherBoss.HP"));
			EventController.admin.setNetherEntradaLiberada(false);
			EventController.admin.setNetherOcorrendo(false);
			EventController.admin.setNetherTempoDecorrido(0);
			EventController.admin.setMortalhaDaNoiteStatus(false);
			Bukkit.getServer().getScheduler().cancelTasks(Main.getInstance());
			Main.getInstance().startCheck();
			for (World mundo : Bukkit.getWorlds()) {
				for (Entity entidades : mundo.getEntities()) {
					if (!(entidades instanceof Player) && (entidades.hasMetadata("boss-nether") || 
						(entidades.hasMetadata("cavaleironegro-servo") || (entidades.hasMetadata("netherservos"))))) {
						 entidades.remove();
					}
				}
			}
	  }

}
	
	
