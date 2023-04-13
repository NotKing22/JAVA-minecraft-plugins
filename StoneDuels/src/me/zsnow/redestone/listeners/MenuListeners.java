package me.zsnow.redestone.listeners;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import me.zsnow.redestone.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.zsnow.redestone.api.LocationAPI;
import me.zsnow.redestone.api.LocationAPI.location;
import me.zsnow.redestone.api.NumberFormatAPI;
import me.zsnow.redestone.cache.DuelCache;
import me.zsnow.redestone.config.Configs;
import me.zsnow.redestone.itemmethods.InventoryUtils;
import me.zsnow.redestone.itemmethods.ItemBuilder;
import me.zsnow.redestone.manager.DuelManager;
import me.zsnow.redestone.manager.InviteManager;
import me.zsnow.redestone.manager.SumoDuelManager;
import me.zsnow.redestone.mysql.ConexaoSQL;
import me.zsnow.redestone.mysql.MysqlMethods;
import net.md_5.bungee.api.ChatColor;

public class MenuListeners extends ConexaoSQL implements Listener {


    final static String storage_name = "§eArmazém de itens";
    final static String leaderboard_name = "§bLeaderboard";
    final static String camarote_name = "§eCamarote";
    final static String duelItem_name = "§aDesafiar 1v1 PvP!";
    final static String sumoDuelItem_name = "§bDesafiar 1v1 Sumo! §e§l(NOVO)";
    final static String barrinha = "│";
    final static String kbSelection = "§6Customize o KnockBack";
    final static String potSelection = "§6Customize os efeitos";
    final static String arenaSelection = "§6Escolha a arena";
    //final static String getArrowUP = "§§§b";
    //final static String getArrowDown = "§§§a";

    final static String storagemenu_name = "§b§lDUELO §fSeu armazém";
    final static String inventory_name = "§b§lDUELO §fDesafie um jogador";
    final static String topmenu_name = "§b§lDUELO §fTop 9 jogadores";
    final static String menuSelection = "§b§lDUELO §fSumo Configurações";

    public ArrayList<Player> chatEventPvP = new ArrayList<>();
    public ArrayList<Player> chatEventSumo = new ArrayList<>();

    @SuppressWarnings("unused")
	@EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equals(inventory_name) ||
                e.getInventory().getTitle().equals(topmenu_name) ||
                e.getInventory().getTitle().equals(storagemenu_name) ||
                e.getInventory().getTitle().equals(menuSelection)) {
            e.setCancelled(true);
            //e.setResult(Result.DENY);
           SumoDuelManager sumoManager = SumoDuelManager.getInstance();
            ItemStack currentItem = e.getCurrentItem();
            if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName() && currentItem.getItemMeta().hasLore()) {
                if (currentItem.isSimilar(getTopItemStack())) {
                    openTopMenu(p);
                    p.playSound(p.getLocation(), Sound.HORSE_SADDLE, 1.0f, 0.5f);
                    return;
                }
                if (currentItem.isSimilar(getStorageItem())) {
                    openStorageMenu(p);
                    return;
                }
                if (currentItem.isSimilar(new ItemBuilder(Material.STORAGE_MINECART).displayname("§aColetar").lore("§7Clique para coletar seus itens.").build())) {
                    p.playSound(p.getLocation(), Sound.HORSE_SADDLE, 1.0f, 0.5f);
                    updateStorageMenu(p);
                    return;
                }
                if (currentItem.getItemMeta().getDisplayName().equals(camarote_name)) {
                    if (!DuelManager.getInstance().getCamarotePlayers().contains(p)) {
                        LocationAPI.getLocation().teleportTo(p, location.CAMAROTE);
                        DuelManager.getInstance().getCamarotePlayers().add(p);
                        p.sendMessage("§aVocê foi enviado para o camarote!");
                        return;
                    } else {
                        LocationAPI.getLocation().teleportTo(p, location.SAIDA);
                        DuelManager.getInstance().getCamarotePlayers().remove(p);
                        p.sendMessage("§aVocê foi enviado para a saída do camarote!");
                        return;
                    }
                }
                if (currentItem.getItemMeta().getDisplayName().equals(duelItem_name)) {
                    if (DuelManager.getInstance().getManutencaoStatus() == false) {
                        if (!chatEventPvP.contains(p)) {
                            if (InviteManager.getInstance().canInvite()) {
                                chatEventPvP.add(p);
                                p.closeInventory();
                                p.sendMessage(" ");
                                p.sendMessage(" §aDigite o nickname do jogador que deseja desafiar no chat.");
                                p.sendMessage(" §aPara cancelar esta ação digite §7'cancelar' §ano §nchat local§a.");
                                p.sendMessage(" ");
                                return;
                            } else {
                                p.sendMessage("§cJá há 2 jogadores na fila de duelo. Aguarde que o pedido expire ou seja aceito por um deles para poder enviar novamente.");
                                return;
                            }
                        } else {
                            p.closeInventory();
                            p.sendMessage("");
                            p.sendMessage(" §cVocê já está tentando desafiar um jogador.");
                            p.sendMessage(" §cCaso queira cancelar esta ação, digite §7'cancelar' §cno §nchat local§a.");
                            p.sendMessage("");
                            return;
                        }
                    } else {
                        p.sendMessage("§cO sistema de duelos está em manutenção. Volte novamente mais tarde!");
                        p.closeInventory();
                        return;
                    }
                }
                if (currentItem.getItemMeta().getDisplayName().equals(kbSelection)) {
                	p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
                    switch (getDataInfo(p).getKB()) {
						case 0:
							sumoManager.setKBhash(p, 1);
							getDataInfo(p).setKB(getDataInfo(p).getKB()+1);
							break;
						case 1:
							getDataInfo(p).setKB(getDataInfo(p).getKB()+1);
							break;
						case 2:
							getDataInfo(p).setKB(getDataInfo(p).getKB()+1);
							break;
						default:
							getDataInfo(p).setKB(0);
							break;
						}
	                e.getInventory().setItem(e.getSlot(), updateKBselection(getDataInfo(p).getKB()));
	                sumoManager.setKBhash(p, getDataInfo(p).getKB());
	                p.updateInventory();
	                return;
                }
                if (currentItem.getItemMeta().getDisplayName().equals(potSelection)) {
                	p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
                    switch (getDataInfo(p).getPotLvl()) {
						case 0:
							sumoManager.setPothash(p, 1);
							getDataInfo(p).setPotLvl(getDataInfo(p).getPotLvl()+1);
							break;
						case 1:
							getDataInfo(p).setPotLvl(getDataInfo(p).getPotLvl()+1);
							break;
						case 2:
							getDataInfo(p).setPotLvl(getDataInfo(p).getPotLvl()+1);
							break;
						default:
							getDataInfo(p).setPotLvl(0);
							break;
						}
	                e.getInventory().setItem(e.getSlot(), updatePotselection(getDataInfo(p).getPotLvl()));
	                sumoManager.setPothash(p, getDataInfo(p).getPotLvl());
	                p.updateInventory();
	                return;
                }
                if (currentItem.getItemMeta().getDisplayName().equals(arenaSelection)) {
                	p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
                    switch (getDataInfo(p).getArena()) {
						case 0:
							sumoManager.setArenaHash(p, 1);
							getDataInfo(p).setArena(getDataInfo(p).getArena()+1);
							break;
						case 1:
							getDataInfo(p).setArena(getDataInfo(p).getArena()+1);
							break;
						default:
							getDataInfo(p).setArena(0);
							break;
						}
	                e.getInventory().setItem(e.getSlot(), updateArenaSelection(getDataInfo(p).getArena()));
	                sumoManager.setArenaHash(p, getDataInfo(p).getArena());
	                p.updateInventory();
	                return;
                }

                if (currentItem.getItemMeta().getDisplayName().equals(sumoDuelItem_name)) {
                    	// PRECISA SER INVITEMANAGER DO SUMO
                            	
                            	//ADICIONAR AQUI O MENU DE CUSTOMIZAÇAO
                            	//SE O CARA N TIVER VIP ELE PRECISA CLICAR NO BOTAO DE SKIP
                            	//NO FIM CHECA DNV SE A FILA TA LIVRE
                            	//ENVIA O CONVITE E COLOCA A FILA EM ESPERA
                    	
                            	openKnockbackMenuSelection(p);
                            	return;
                            }
                if (currentItem.getItemMeta().getDisplayName().equals("§aAvançar")) {
                	 if (SumoDuelManager.getInstance().getManutencaoStatus() == false) {
                    if (!chatEventSumo.contains(p)) {
                        if (InviteManager.getInstance().canInvite()) { 
                        	chatEventSumo.add(p);
			                    p.closeInventory();
			                    p.sendMessage(" ");
			                    p.sendMessage(" §aDigite o nickname do jogador que deseja desafiar no chat.");
			                    p.sendMessage(" §aPara cancelar esta ação digite §7'cancelar' §ano §nchat local§a.");
			                    p.sendMessage(" ");
			                    return;
			                } else {
			                    p.sendMessage("§cJá há 2 jogadores na fila de duelo Sumo. Aguarde que o pedido expire ou seja aceito por um deles para poder enviar novamente.");
			                    return;
			                }
			            } else {
			                p.closeInventory();
			                p.sendMessage("");
			                p.sendMessage(" §cVocê já está tentando desafiar um jogador.");
			                p.sendMessage(" §cCaso queira cancelar esta ação, digite §7'cancelar' §cno §nchat local§a.");
			                p.sendMessage("");
			                return;
			            }
			        } else {
			            p.sendMessage("§cO sistema de duelos está em manutenção. Volte novamente mais tarde!");
			            p.closeInventory();
			            return;
			        }
                }
            }
        }
    }
    
    @EventHandler
    public void onSendNickname(AsyncPlayerChatEvent e) {
        InviteManager invite = InviteManager.getInstance();
        final Player p = e.getPlayer();
        if (chatEventPvP.contains(p)) {
            e.setCancelled(true);
            final Player target = Bukkit.getPlayer(e.getMessage());
            if (e.getMessage().equals("cancelar")) {
                p.sendMessage("§c§lVocê optou por cancelar a ação convite de duelo.");
                chatEventPvP.remove(p);
                return;
            }
            if (DuelManager.getInstance().hasCoin(p)) {
                if (target != null) {
                    if (target != p) {
                        if (!invite.hasInvite(target)) {
                            if (invite.canInvite()) {
                                invite.sendInviteTo(p, target);

                                NumberFormatAPI formatter = new NumberFormatAPI();
                                String custo = formatter.formatNumber(DuelManager.getInstance().getCusto());
                                if (Configs.config.getBoolean("has-duelo-broadcast") == true) {
                                    for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("duelo-broadcast")) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                                .replace("$convidado", target.getName()))
                                                .replace("$jogador", p.getName())
                                                .replace("$tempo", String.valueOf(invite.getTempo())
                                                        .replace("$valor", custo)));
                                    }
                                }
                                for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-enviado")) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                            .replace("$convidado", target.getName()))
                                            .replace("$tempo", String.valueOf(invite.getTempo()))
                                            .replace("$valor", custo));
                                }
                                for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-recebido")) {
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                            .replace("$jogador", p.getName()))
                                            .replace("$tempo", String.valueOf(invite.getTempo()))
                                            .replace("$valor", custo));
                                }
                                invite.startTask();
                                chatEventPvP.remove(p);
                                return;
                            } else {
                                p.sendMessage("§cJá há 2 jogadores na fila de duelo. Aguarde que o pedido expire ou seja aceito por um deles para poder enviar novamente.");
                                p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                            }
                        } else {
                            p.sendMessage("§cEste jogador já possui um pedido de duelo pendente.");
                            p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                        }
                    } else {
                        p.sendMessage("§cVocê não pode desafiar a sí mesmo.");
                        p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                    }
                } else {
                    p.sendMessage("§cO jogador especificado não está online.");
                    p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                }
            } else {
                p.sendMessage("§cVocê não tem dinheiro o suficiente para desafiar um jogador.");
                p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
            }
            chatEventPvP.remove(p);
        }
        if (chatEventSumo.contains(p)) {
        	 SumoDuelManager sumoManager = SumoDuelManager.getInstance();
            e.setCancelled(true);
            final Player target = Bukkit.getPlayer(e.getMessage());
            if (e.getMessage().equals("cancelar")) {
                p.sendMessage("§c§lVocê optou por cancelar a ação convite de duelo.");
                chatEventSumo.remove(p);
                return;
            }
            if (DuelManager.getInstance().hasCoin(p)) {
                if (target != null) {
                    if (target != p) {
                        if (!invite.hasInvite(target)) {
                            if (invite.canInvite()) {
                                invite.sendInviteTo(p, target);

                                String jogador = p.getName();
                                String convidado = target.getName();
                                String kb = getKbTpe(sumoManager.getKBhash(p)); // null?
                                String pot = getPotType(sumoManager.getPothash(p)); // null?
                                String arena = getArenaType(sumoManager.getArenaHash(p));
                                //String.valueOf(invite.getTempo()
                                //a e b nulos, C e D vao
                                
                                NumberFormatAPI formatter = new NumberFormatAPI();
                                String custo = formatter.formatNumber(DuelManager.getInstance().getCusto());
                                if (Configs.config.getBoolean("has-sumo-broadcast") == true) {
                                    for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("sumo-duelo-broadcast")) {
                                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg
                                                .replace("$convidado", convidado)
                                                .replace("$jogador", jogador)
                                                .replace("$tempo", "entre 0 e infinito")
                                                .replace("$valor", custo)
                                                .replace("$kb", kb)
                                                .replace("$pot", pot)
                                                .replace("$arena",arena)));
                                    }
                                }
                                for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-enviado-sumo")) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                            .replace("$convidado", convidado)
                                            .replace("$tempo", "entre 0 e infinito")
                                            .replace("$valor", custo)
                                            .replace("$kb", kb)
                                            .replace("$pot", pot)
                                            .replace("$arena", arena)));
                                }
                                for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-recebido-sumo")) {
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                            .replace("$jogador", jogador)
                                            .replace("$convidado", convidado)
                                            .replace("$tempo", "entre 0 e infinito")
                                            .replace("$valor", custo)
                                            .replace("$kb", kb)
                                            .replace("$pot", pot)
                                            .replace("$arena", arena)));
                                }
                               // invite.startTask();  ADD INVITE DO SUMO
                                chatEventSumo.remove(p);
                                return;
                            } else {
                                p.sendMessage("§cJá há 2 jogadores na fila de duelo. Aguarde que o pedido expire ou seja aceito por um deles para poder enviar novamente.");
                                p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                            }
                        } else {
                            p.sendMessage("§cEste jogador já possui um pedido de duelo pendente.");
                            p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                        }
                    } else {
                        p.sendMessage("§cVocê não pode desafiar a sí mesmo.");
                        p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                    }
                } else {
                    p.sendMessage("§cO jogador especificado não está online.");
                    p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                }
            } else {
                p.sendMessage("§cVocê não tem dinheiro o suficiente para desafiar um jogador.");
                p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
            }
            chatEventSumo.remove(p);
        }
    }

    
   private SumoDuelManager getDataInfo(Player jogador) {
        UUID uuid = jogador.getUniqueId();
        SumoDuelManager jogadorInfo = SumoDuelManager.playerData.get(uuid);
        if (jogadorInfo == null) {
            jogadorInfo = new SumoDuelManager();
            SumoDuelManager.playerData.put(uuid, jogadorInfo);
        }
        return jogadorInfo;
    }
    
    public static ItemStack getSkipItem() {
    	ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 10);
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName("§aAvançar");
    	meta.setLore(Arrays.asList(new String[] {"§fClique para salvar as configurações"}));
    	item.setItemMeta(meta);
    	return item;
    }
    
    public static ItemStack selectKB() {
    	ItemStack item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
    			"",
    			"  §b➤ §bNenhum §8(Padrão)",
    			" §8● §7Repulsão 1",
    			" §8● §7Repulsão 2",
    			" §8● §7Repulsão 3",
    			"",
    			"§eClique para alterar.",})).build();
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
   		return item;
    }
    
    public static ItemStack selectPot() {
    	ItemStack item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
    			"",
    			"  §b➤ §bNenhum §8(Padrão)",
    			" §8● §7Velocidade 1",
    			" §8● §7Velocidade 2",
    			" §8● §7Velocidade 3",
    			"",
    			"§eClique para alterar.",})).build();
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
   		return item;
    }
    
    public static ItemStack selectArena() {
    	ItemStack item = new ItemBuilder(Material.MAP).displayname(arenaSelection).lore(Arrays.asList(new String[] {
    			"",
    			"  §b➤ Arena Clássica §8(Padrão)",
    			" §8● §7Arena Pequena",
    			" §8● §7Arena Grande",
    			"",
    			"§eClique para alterar.",})).build();
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
   		item.setItemMeta(meta);
   		return item;
    }
    
    public void openKnockbackMenuSelection(Player p) {
    	SumoDuelManager sumoManager = SumoDuelManager.getInstance();
    	
    	int kbLevel = sumoManager.getKBhash(p) == null ? 0 : sumoManager.getKBhash(p);
    	int potLvl = sumoManager.getPothash(p) == null ? 0 : sumoManager.getPothash(p);
    	int arenaLvl = sumoManager.getArenaHash(p) == null ? 0 : sumoManager.getArenaHash(p);
    	
    	Inventory inventory = Bukkit.createInventory(null, 3*9, menuSelection);
    	ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)0);
    	for (int i = 0; i < inventory.getSize(); i++) {
    		inventory.setItem(i, vidro);
    	}
    	inventory.setItem(26, getSkipItem());
    	inventory.setItem(11, updateKBselection(kbLevel));
    	inventory.setItem(13, updatePotselection(potLvl));
    	inventory.setItem(15, updateArenaSelection(arenaLvl));
    	p.openInventory(inventory);
    }
    

    @EventHandler
    public void worldChange(PlayerChangedWorldEvent e) {
        final Player p = e.getPlayer();
        String mundo = Configs.config.getConfig().getString("mundo-do-evento");
        if (DuelManager.getInstance().getDuelando().contains(p) && DuelManager.getInstance().getManutencaoStatus() == false &&
                (!p.getWorld().getName().equalsIgnoreCase(mundo))) {
            LocationAPI.getLocation().teleportTo(p, location.POS1);
            p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
        }
    }

   private static ItemStack getSumoItem() {
        String status = "§aLivre.";
        String quantidade = "se pa que sim";
       // if (DuelManager.getInstance().getManutencaoStatus() == true) {
       //     status = "§c§lManutenção";
        //}
    	return new ItemBuilder(Material.LEASH).displayname(sumoDuelItem_name).lore(Arrays.asList(new String[] { 
    			"§fDigite o nome do jogador no chat.", "",
                "  §6" + barrinha + " §fStatus da fila: " + status,
                "  §6" + barrinha + " §fDuelos ocorrendo: §7" + quantidade,
                " ",
                " §fDuelos gratuitos no período de testes!"})).build();
    }
    
    public static void openMenuPrincipal(Player p) throws IOException {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, inventory_name);
        inv.setItem(10, getSkullMenu(p));
        inv.setItem(11, getSumoItem());
        inv.setItem(15, getStorageItem());
        inv.setItem(13, getTopItemStack());
        inv.setItem(14, getCamaroteItemStack());
        p.openInventory(inv);
    }

    private static void openTopMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, topmenu_name);
        getTop10(inv);
        inv.setItem(49, getStatisticItem(p.getName()));
        p.openInventory(inv);
    }

    private static void openStorageMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, storagemenu_name);
        inv.setItem(49, new ItemBuilder(Material.HOPPER_MINECART).displayname("§aColetar").lore("§7Clique para coletar seus itens.").build());
        if (DuelCache.getCache().hasArmazem(p.getName())) {
            try {
                inv.setContents(InventoryUtils.itemStackArrayFromBase64(DuelCache.getCache().getArmazemFrom(p.getName())));
                inv.setItem(49, new ItemBuilder(Material.STORAGE_MINECART).displayname("§aColetar").lore("§7Clique para coletar seus itens.").build());
            } catch (Exception e) {
                e.printStackTrace();
                p.sendMessage("§cHouve um erro ao consultar seu armazém. Entre em contato com um Administrador!");
                return;
            }
        } else {
            inv.setItem(22, new ItemBuilder(Material.WEB).displayname("§cVazio...").lore("§7Você não possui itens para coletar.").build());
        }
        p.openInventory(inv);
    }

    public static void updateStorageMenu(Player p) {
        int slotsVazio = 0;
        int TotalDeItem = 0;
        ItemStack[] contents = p.getInventory().getContents();
        for (ItemStack item : contents) {
            if (item == null || (item.getType() == Material.AIR)) {
                slotsVazio++;
            }
        }
        if (slotsVazio >= 1) {
            try {
                if (DuelCache.getCache().hasArmazem(p.getName())) {
                    Inventory invStorage = Bukkit.createInventory(null, 6 * 9, p.getName());
                    invStorage.setContents(InventoryUtils.itemStackArrayFromBase64(DuelCache.getCache().getArmazemFrom(p.getName())));

                    for (ItemStack item : invStorage.getContents()) {
                        if (DuelCache.getCache().hasArmazem(p.getName()) && p.getInventory().firstEmpty() != -1) {
                            if (item != null && !(item.getType() == Material.AIR)) {
                                invStorage.remove(item);
                                DuelCache.getCache().setPlayerArmazem(p.getName(), InventoryUtils.toBase64(invStorage));
                                openStorageMenu(p);
                                p.getInventory().addItem(item);
                                p.closeInventory();
                                p.updateInventory();
                                slotsVazio--;
                                TotalDeItem++;

                            }
                        }
                    }
                    if (TotalDeItem == 0) {
                        DuelCache.getCache().setPlayerArmazem(p.getName(), null);
                        p.updateInventory();
                        openStorageMenu(p);
                        p.sendMessage("§cSeu armazém está vázio.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage("§cHouve um erro ao coletar os itens do seu armazém. Entre em contato com um Administrador!");
            }
        } else {
            p.sendMessage("§cVocê não possui espaço em seu inventário para coletar os itens do seu armazém.");
            p.closeInventory();
            return;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (DuelCache.getCache().hasCache(p.getName())) return;

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			MysqlMethods.createPlayerData(p.getName());
			DuelCache.getCache().setPlayerArmazem(p.getName(), "");
		});
    }

    public static ItemStack getTop10(Inventory inv) {
        List<String> top = Lists.newArrayList();
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

        int mito = 0;

        for (int stringLine = 0; stringLine < top.size(); stringLine++) {
            mito++;
            String a = top.get(stringLine);
            String[] splitA = a.split(" ");
            //
            String position = splitA[0];
            String playerName = splitA[1];
            String vitorias = splitA[3];
            String derrotas = splitA[5];

            //String prefix = VaultHook.getPlayerPrefix(playerName);

            int vitoriasINT = Integer.parseInt(vitorias.replace("°", " "));
            int derrotasINT = Integer.parseInt(derrotas.replace("°", " "));

            DecimalFormat df = new DecimalFormat("###.##");
            final String KDR = df.format(derrotasINT > 0 ? (double) vitoriasINT / derrotasINT : vitoriasINT);

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(playerName);
            meta.setDisplayName("§6" + position + " " + playerName);

            meta.setLore(Arrays.asList(new String[]{
                    "",
                    " §e" + barrinha + " §fVitórias: §7" + vitoriasINT,
                    " §e" + barrinha + " §fDerrotas: §7" + derrotasINT,
                    " ",
                    " §e" + barrinha + " §f§lKDR: §7" + KDR,
                    " "}));
            if (mito == 1) {
                List<String> lore = meta.getLore();
                lore.add("   §c⚔ §fEste jogador é o §b§lMVP§f atual!");
                lore.add(" ");
                meta.setLore(lore);
            }
            skull.setItemMeta(meta);

            for (int i = 0; i < inv.getSize(); i++) {
                if (i == 13 || (i >= 21 && i <= 23) || (i >= 29 && i <= 33)) {
                    ItemStack itemSlot = inv.getItem(i);
                    if (itemSlot == null) {
                        inv.setItem(i, skull);
                        break;
                    }
                }
            }
        }
        if (mito <= 9) {
            ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta metaVidro = vidro.getItemMeta();
            metaVidro.setDisplayName("§cVazio...");
            vidro.setItemMeta(metaVidro);
            for (int i = 0; i < inv.getSize(); i++) {
                if (i == 13 || (i >= 21 && i <= 23) || (i >= 29 && i <= 33)) {
                    ItemStack itemSlot = inv.getItem(i);
                    if (itemSlot == null) {
                        inv.setItem(i, vidro);
                    }
                }
            }
        }
        return skull;
    }

    private static ItemStack getStorageItem() {
        ItemStack item = new ItemStack(Material.STORAGE_MINECART);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(storage_name);
        meta.setLore(Arrays.asList(new String[]{
                "§fClique para acessar o seu armazém",
                "§fde itens obtidos durante os duelos.",
                "",
                " §4§l" + barrinha + " §cColete seus itens antes de duelar, ",
                " §4§l" + barrinha + " §ccaso contrário, serão perdidos.",
                " "}));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getTopItemStack() {
        return new ItemBuilder(Material.NETHER_STAR).displayname(leaderboard_name).lore(Arrays.asList(
                "",
                " §6" + barrinha + " §eConfira os §6§lTOP 10 §emelhores jogadores",
                " §6" + barrinha + " §edos duelos §e§l1v1 §ena arena.",
                "")).build();
    }

    private static ItemStack getStatisticItem(String player) {

        final int vitorias = DuelCache.getCache().getVitoriasFrom(player) == null ? 0 : DuelCache.getCache().getVitoriasFrom(player);
        final int derrotas = DuelCache.getCache().getDerrotasFrom(player) == null ? 0 : DuelCache.getCache().getDerrotasFrom(player);

        DecimalFormat df = new DecimalFormat("###.##");
        final String KDR = df.format(derrotas > 0 ? (double) vitorias / derrotas : vitorias);
        return new ItemBuilder(Material.ARMOR_STAND).displayname("§eSuas estatísticas:").lore(Arrays.asList(
                " ",
                "§c" + barrinha + " §fSuas vitórias:§7 " + vitorias,
                "§c" + barrinha + " §fSuas derrotas:§7 " + derrotas,
                "§c" + barrinha + " §fSeu KDR:§7 " + KDR,
                " ",
                " §7(Aguarde seus dados serem atualizados no top 9)",
                " ")).build();
    }

    public static ItemStack getSkullMenu(Player p) {
        String status = InviteManager.getInstance().canInvite() == true ? "§aLivre." : "§eEm andamento.";
        if (DuelManager.getInstance().getManutencaoStatus() == true) {
            status = "§c§lManutenção";
        }
        final String quantidade = DuelManager.getInstance().getDuelando().size() == 0 ? "§cNenhum." : String.valueOf(DuelManager.getInstance().getDuelando().size() / 2);
        NumberFormatAPI formatter = new NumberFormatAPI();
        String custo = formatter.formatNumber(DuelManager.getInstance().getCusto());
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(p.getName());
        meta.setDisplayName(duelItem_name);
        meta.setLore(Arrays.asList(new String[]{
                "§fDigite o nome do jogador no chat.", "",
                "  §6" + barrinha + " §fStatus da fila: " + status,
                "  §6" + barrinha + " §fDuelos ocorrendo: §7" + quantidade,
                " ",
                " §fEssa ação possui um custo!",
                " §fTaxa de §6⛃ §e" + custo + " §fpara cada oponente."}));
        skull.setItemMeta(meta);
        return skull;
    }


    public static ItemStack getCamaroteItemStack() {
        String url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjZGExNjcyYWUxNmE1NzUzOGMxNTgxN2FlYjI4MDBkNTQyMzg3MGRlNGQzMGYxOWNkMjRjMjZkMDZmYjM1NSJ9fX0=";
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        headMeta.setDisplayName(camarote_name);
        headMeta.setLore(Arrays.asList(new String[]{
                " ",
                "§7" + barrinha + " §fClique aqui para acessar ou sair",
                "§7" + barrinha + " §fdo camarote quando quiser.",
                " "}));
        head.setItemMeta(headMeta);

        return head;
    }
    
    public ItemStack updateKBselection(int i) {
    	ItemStack item = null;
    	switch (i) {
		case 0:
	    	item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			"   §b➤ §b§nNenhum §8(Padrão)",
	    			" §8● §7Repulsão 1",
	    			" §8● §7Repulsão 2",
	    			" §8● §7Repulsão 3",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 1:
	    	item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7Nenhum§8 (Padrão)",
	    			"   §b➤ §b§nRepulsão 1",
	    			" §8● §7Repulsão 2",
	    			" §8● §7Repulsão 3",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 2:
	    	item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7Nenhum §8(Padrão)",
	    			" §8● §7Repulsão 1",
	    			"   §b➤ §b§nRepulsão 2",
	    			" §8● §7Repulsão 3",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 3:
	    	item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7Nenhum §8(Padrão)",
	    			" §8● §7Repulsão 1",
	    			" §8● §7Repulsão 2",
	    			"   §b➤ §b§nRepulsão 3",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		default:
			break;
		}
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
		return item;
    }
    
    public ItemStack updatePotselection(int i) {
    	ItemStack item = null;
    	switch (i) {
		case 0:
			 item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
					"",
					"  §b➤ §nNenhum §8(Padrão)",
					" §8● §7Velocidade 1",
					" §8● §7Velocidade 2",
					" §8● §7Velocidade 3",
					"",
					"§eClique para alterar.",})).build();
			break;
		case 1:
			 item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
						"",
						" §8● §7Nenhum §8(Padrão)",
						"  §b➤ §nVelocidade 1",
						" §8● §7Velocidade 2",
						" §8● §7Velocidade 3",
						"",
						"§eClique para alterar.",})).build();
			break;
		case 2:
			 item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
						"",
						" §8● §7Nenhum §8(Padrão)",
						" §8● §7Velocidade 1",
						"  §b➤ §nVelocidade 2",
						" §8● §7Velocidade 3",
						"",
						"§eClique para alterar.",})).build();
			break;
		case 3:
			 item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
						"",
						" §8● §7Nenhum §8(Padrão)",
						" §8● §7Velocidade 1",
						" §8● §7Velocidade 2",
						"  §b➤ §nVelocidade 3",
						"",
						"§eClique para alterar.",})).build();
			break;
		default:
			break;
		}
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
		return item;
    }
    
    public ItemStack updateArenaSelection(int i) {
    	ItemStack item = null;
    	switch (i) {
		case 0:
	    	item = new ItemBuilder(Material.MAP).displayname(arenaSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			"  §b➤ §nArena Clássica§8 (Padrão)",
	    			" §8● §7Arena Pequena",
	    			" §8● §7Arena Grande",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 1:
	    	item = new ItemBuilder(Material.MAP).displayname(arenaSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7 Arena Clássica §8(Padrão)",
	    			"  §b➤ §nArena Pequena",
	    			" §8● §7Arena Grande",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 2:
	    	item = new ItemBuilder(Material.MAP).displayname(arenaSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7Arena Clássica §8(Padrão)",
	    			" §8● §7Arena Pequena",
	    			"  §b➤ §nArena Grande",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
			
		default:
			break;
		}
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
		return item;
    }
    
    public String getKbTpe(int getKbHash) {
    	String type = null;
    	switch (getKbHash) {
			case 0:
				type = "Normal";
				break;
			case 1:
				type = "Repulsão 1";
				break;
			case 2:
				type = "Repulsão 2";
				break;
			case 3:
				type = "Repulsão 3";
				break;
			default:
				type = "ERROR: unidentified";
				break;
			}
    	return type;
    }
    
    public String getPotType(int getPotHash) {
    	String type = null;
    	switch (getPotHash) {
			case 0:
				type = "Nenhum";
				break;
			case 1:
				type = "Velocidade 1";
				break;
			case 2:
				type = "Velocidade 2";
				break;
			case 3:
				type = "Velocidade 3";
				break;
			default:
				type = "ERROR: unidentified";
				break;
			}
    	return type;
    }
    
    public String getArenaType(int getArenaHash) {
    	String type = null;
    	switch (getArenaHash) {
			case 0:
				type = "Clássica";
				break;
			case 1:
				type = "Pequena";
				break;
			case 2:
				type = "Grande";
				break;
			default:
				type = "ERROR: unidentified";
				break;
			}
    	return type;
    }
    
   /* public static ItemStack getArrowUP() {
        String url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjFkNGZkZDA5MTg0MGQ5ZTdkZjA2MDE2ODFhZGRlYzYwNTE0ODVhNDg0YmE3ZjUzNmIzNWQ0ZTA1YWE4NmVmOSJ9fX0=";
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        headMeta.setDisplayName(getArrowUP);
        headMeta.setLore(Arrays.asList(new String[]{
                " ",
        		" §6§lAMPLIFICAR §3⬆",
        		" "}));
        head.setItemMeta(headMeta);
        return head;
    }
    
    public static ItemStack getArrowDown() {
        String url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFiODg2NWQ5OTViNjA5YjNmZmVlMGEzZWQyZTY1NDQwZTM1NDQwZDJjNjU1MTRmZWRiMmM1YjJjNjg3Zjg1ZCJ9fX0";
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        headMeta.setDisplayName(getArrowDown);
        headMeta.setLore(Arrays.asList(new String[]{
                " ",
        		" §6§lREDUZIR §3⬇",
        		" "}));
        head.setItemMeta(headMeta);
        return head;
    }
    */
    

    public static String formatar(int n) {
        DecimalFormat df = new DecimalFormat("#.##");
        String format = df.format(n);
        return format;
    }
}
