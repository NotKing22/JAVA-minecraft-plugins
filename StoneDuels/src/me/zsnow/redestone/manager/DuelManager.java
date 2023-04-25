package me.zsnow.redestone.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.redestone.economy.EconomyPlugin;

import me.zsnow.redestone.config.Configs;

public class DuelManager {
	
	public static DuelManager instance = new DuelManager();
	
	public static DuelManager getInstance() {
		return instance;
	}
	
	public int custo = Configs.config.getConfig().getInt("duelo-custo");
	
	public HashMap<Player, Player> duelandoHash = new HashMap<Player, Player>();
	private ArrayList<Player> duelando = new ArrayList<>();
	private ArrayList<Player> duelandoSumo = new ArrayList<>();
	private ArrayList<Player> camarote = new ArrayList<>();
	private Boolean manutencao = false;
	private Boolean manutencaosumo = false;
	
	public Boolean getManutencaoStatus() {
		return this.manutencao;
	}

	public Boolean getManutencaoSumoStatus() {
		return this.manutencaosumo;
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
	
	public ArrayList<Player> getDuelandoSumo() {
		return duelandoSumo;
	}
	
	public Integer getCusto() {
		return custo;
	}
	
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
	
	public Player getMortoBy(Player player) {
		return duelandoHash.get(player);
	}
}
