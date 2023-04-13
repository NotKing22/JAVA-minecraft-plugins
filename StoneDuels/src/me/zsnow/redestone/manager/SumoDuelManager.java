package me.zsnow.redestone.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

//import com.redestone.economy.EconomyPlugin;

//import me.zsnow.redestone.config.Configs;

public class SumoDuelManager {
	
	public static SumoDuelManager instance = new SumoDuelManager();
	
	public static SumoDuelManager getInstance() {
		return instance;
	}
	
	//public int custo = Configs.config.getConfig().getInt("duelo-custo");
	
	public HashMap<Player, Player> duelandoHash = new HashMap<Player, Player>();
	private ArrayList<Player> duelando = new ArrayList<>();
	private ArrayList<Player> camarote = new ArrayList<>();
	private static HashMap<Player, Integer> kbHash = new HashMap<>();
	private HashMap<Player, Integer> potHash = new HashMap<>();
	private HashMap<Player, Integer> arenaHash = new HashMap<>();
	
	//private int hits, tempo;
	@SuppressWarnings("unused")
	private Player p;
	private int KB = 0, potLvl = 0, arena = 0;
	private Boolean manutencao = false;
	
	public Boolean getManutencaoStatus() {
		return this.manutencao;
	}
	
	public void setManutencaoStatus(boolean booleanValue) {
		this.manutencao = booleanValue;
	}
	
	public ArrayList<Player> getCamarotePlayers() {
		return camarote;
	}
	
	public ArrayList<Player> getDuelando() {
		return duelando;
	}
	
	public Integer setKBhash(Player p, int kb) {
		return kbHash.put(p, kb);
	}
	
	public Integer setPothash(Player p, int lvl) {
		return potHash.put(p, lvl);
	}
	
	public Integer setArenaHash(Player p, int arena) {
		return arenaHash.put(p, arena);
	}
	
	public Integer getKBhash(Player p) {
		return kbHash.get(p) == null ? kbHash.put(p, 0) : kbHash.get(p);
	}
	
	public  Integer getPothash(Player p) {
		return potHash.get(p) == null ? potHash.put(p, 0) : potHash.get(p);
	}
	
	public Integer getArenaHash(Player p) {
		return arenaHash.get(p) == null ? arenaHash.put(p, 0) : arenaHash.get(p);
	}
	
	
	public static Map<UUID, SumoDuelManager> playerData = new HashMap<>();
	 
	public SumoDuelManager() {
		this.KB = 0;
		this.potLvl = 0;
		this.arena = 0;
	}
	
	public int getKB() {
		return KB;
	}
	
	public int getPotLvl() {
		return potLvl;
	}
	
	public int getArena() {
		return arena;
	}
	
	public void setKB(int kb) {
		this.KB = kb;
	}
	
	public void setPotLvl(int lvl) {
		this.potLvl = lvl;
	}
	
	public void setArena(int arena) {
		this.arena = arena;
	}
	
	//public Integer getCusto() {
	//	return custo;
	//}
	
	/*
	public Boolean hasCoin(Player player) {
		if (EconomyPlugin.getInstance().getEconomy().has(player, getCusto())) {
			return true;
		}
		return false;
	}
	
	public void paymentToEnter(Player player) {
		EconomyPlugin.getInstance().getEconomy().withdrawPlayer(player, getCusto());
	}
	
	public void paymentToWin(Player player) {
		EconomyPlugin.getInstance().getEconomy().depositPlayer(player, (getCusto() * 2));
	}
	
	public void moneyBackFor(Player player) {
		EconomyPlugin.getInstance().getEconomy().withdrawPlayer(player, getCusto());
	}
	*/
	public Player getMortoBy(Player player) {
		return duelandoHash.get(player);
	}
}
