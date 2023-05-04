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
	
	//private int hits, tempo;
	@SuppressWarnings("unused")
	private Player p;
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
	
	
	public static Map<UUID, SumoDuelManager> playerData = new HashMap<>();
	 
	private int KB = 0, potLvl = 0, arena = 0, hits = 0, wrong_hits = 0;
	private boolean magic_Water;
	
	public SumoDuelManager() {
		this.KB = 0;
		this.potLvl = 0;
		this.arena = 0;
		this.hits = 0;
		this.wrong_hits = 0;
		this.magic_Water = false;
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
	
	public void computeHits() {
		this.hits++;
	}
	
	public void computeWrongHits() {
		this.wrong_hits++;
	}
	
	public void unComputeWrongHits() {
		if (this.wrong_hits > 0)
		this.wrong_hits--;
	}
	
	public int getHits() {
		return this.hits;
	}
	
	public int getWrongHits() {
		return this.wrong_hits;
	}
	
	public boolean getMagicWaterEffect() {
		return this.magic_Water;
	}
	
	public void setMagicWaterStatus(boolean trueOrFalse) {
		this.magic_Water = trueOrFalse;
	}
	
	public String getPercentage(int Ataques, int Ataques_errados) {
	    double porcentagem = ((double) Ataques / (Ataques + Ataques_errados)) * 100;
	    final String porcentagem_format = String.format("%.2f", porcentagem);

	    if (Double.isNaN(porcentagem)) {
	        return "§c00,00%";
		    } else if (porcentagem < 30) {
		        return "&c" + porcentagem_format + "%";
		    } else if (porcentagem < 50) {
		        return "&e" + porcentagem_format + "%";
		    } else {
		        return "&a" + porcentagem_format + "%";
		    }
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
