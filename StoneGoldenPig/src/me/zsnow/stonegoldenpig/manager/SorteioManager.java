package me.zsnow.stonegoldenpig.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.zsnow.stonegoldenpig.Main;
import me.zsnow.stonegoldenpig.configs.Configs;

public class SorteioManager {

	private static SorteioManager instance = new SorteioManager();
	
	public static SorteioManager getInstance() {
		return instance;
	}
	
	DecimalFormat formatar = new DecimalFormat("###,###,###,###,###.##");
	private boolean ocorrendo;
	private boolean apostasLiberadas;
	private double valorInicial;
	private double valorAcumulado;
	private double valorAposta = Configs.config.getConfig().getInt("valor-minimo-aposta"); // mudar
	private double valorMaisAltoApostado;
	private int tempo = Configs.config.getConfig().getInt("tempo-sorteio")+10;
	
	private boolean givarItem;
	
	private ItemStack itemLeiloado;
	
	private int numero_lances;
	private String user_top_lance;
	
	public HashMap<String, Double> valorDepositado = new HashMap<>();
	public ArrayList<Player> chatEvent = new ArrayList<>();
	private ArrayList<Player> participantes = new ArrayList<>();
	
	public HashMap<String, ItemStack> salvarItem = new HashMap<>();
	public ArrayList<String> salvarVencedor = new ArrayList<>();
	
	public void resetData() {  // TIRAR O USER TOP LANCE DE RECEBER A GRANA. tiro ele no fim sorteio
		setOcorrendoStatus(false);
		setApostasLiberadas(false);
		valorInicial = 0;
		valorAcumulado = Configs.config.getConfig().getInt("valor-acumulado-inicial");
		valorAposta = Configs.config.getConfig().getInt("valor-minimo-aposta");
		valorMaisAltoApostado = 0;
		givarItem = false;
		tempo = Configs.config.getConfig().getInt("tempo-sorteio");
		if (valorDepositado.size() >= 1) {
			for (String pn : valorDepositado.keySet()) {
				Player player = Bukkit.getServer().getPlayerExact(pn);
				if (player != null) {
					Main.getInstance().economy.depositPlayer(player, valorDepositado.get(pn));
					player.sendMessage("§aSeu dinheiro do porco dourado foi ressarcido. (R$" + formatar.format(valorDepositado.get(pn)) + "§a)");
				}
			}
		}
		numero_lances = 0;
		itemLeiloado = null;            
		user_top_lance = "§cNinguém...";
		valorDepositado.clear();
		chatEvent.clear();
		participantes.clear();
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		sh.cancelTasks(Main.getInstance());
		apagarPorco();
		
	}
	
	//getters
	
	public boolean givarItem() {
		return givarItem;
	}
	
	public ItemStack getItemLeiloado() {
		return itemLeiloado;
	}
	
	public int getTotalLances() {
		return numero_lances;
	}
	
	public String getUserTopLance() {
		return user_top_lance;
	}
	
	public double getValorDepositado(String playerName) {
		if (valorDepositado.get(playerName) == null) return 0;
		return valorDepositado.get(playerName);
	}
	
	public double getValorInicial() {
		return valorInicial;
	}
	
	public double getValorMaisAltoApostado() {
		return valorMaisAltoApostado;
	}
	
	public Boolean getOcorrendo() {
		return this.ocorrendo;
	}
	
	public Boolean apostasLiberadas() {
		return this.apostasLiberadas;
	}
	
	public ArrayList<Player> getParticipantes() {
		return this.participantes;
	}
	
	public int getTempo() {
		return this.tempo;
	}
	
	public double getValorAcumulado() {
		return this.valorAcumulado;
	}
	
	public double getValorAposta() {
		return this.valorAposta;
	}
	
	public double getValorComTaxa(String playerName) {
		double taxa = ((((getValorMaisAltoApostado()/100)*10)+getValorMaisAltoApostado())-getValorDepositado(playerName));
		return taxa;
	}
	
	//setters
	
	public void setGivarItem(Boolean booleanstatus) {
		this.givarItem = booleanstatus;
	}
	
	public void setItemLeiloado(ItemStack item) {
		this.itemLeiloado = item;
	}
	
	public void updateNumeroLances() {
		this.numero_lances++;
	}
	
	public void setUserTopLance(String playerName) {
		this.user_top_lance = playerName;
	}
	public void setValorDepositado(String playerName, Double valor) {
		if (valorDepositado.containsKey(playerName)) {
			double quantia = valorDepositado.get(playerName);
			valorDepositado.replace(playerName, (quantia + valor));
			return;
		}
		valorDepositado.put(playerName, valor);
	}
	
	public void setValorMaisAltoApostado(Double valor) {
		this.valorMaisAltoApostado = valor;
	}
	
	public void setOcorrendoStatus(Boolean status) {
		this.ocorrendo = status;
	}
	
	public void setApostasLiberadas(Boolean status) {
		this.apostasLiberadas = status;
	}
	
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	public void setValorAcumulado(Double valor) {
		this.valorAcumulado = valor;
	}
	
	public void setValorInicial(Double valor) {
		this.valorInicial = valor;
	}
	
	public void upContador() {
		if (getTempo() < (Configs.config.getConfig().getInt("tempo-sorteio"))) {
			setTempo(getTempo()+20);
		}
	}
	
	public void apagarPorco() {
		for (World mundo : Bukkit.getWorlds()) {
			for (Entity entidades : mundo.getEntities()) {
				if (entidades instanceof Pig && entidades.hasMetadata("porquinhodourado")) {
					 entidades.remove();
					 for (com.gmail.filoghost.holographicdisplays.api.Hologram hologram : HologramsAPI.getHolograms(Main.getInstance())) {
			  			    Location pigLoc = entidades.getLocation();
			  			    Location hologramLoc = hologram.getLocation();
			  			    if (pigLoc.distance(hologramLoc) < 10) {
			  			        hologram.delete();
			  			    }
			  			}
				}
			}
		}
	}
	
}
