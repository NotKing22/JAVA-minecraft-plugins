package me.zsnow.redestone.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DuelCache {
	
	private static DuelCache instance = new DuelCache();
	
	public static DuelCache getCache() {
		return instance;
	}
	
	private HashMap<String, Integer> vitorias = new HashMap<>();
	public HashMap<String, Integer> derrotas = new HashMap<>();
	private HashMap<String, Double> kdr = new HashMap<>();
	private HashMap<String, String> armazem = new HashMap<>();
	public List<String> top10 = new ArrayList<String>();
	//public List<String> outsideTop10 = new ArrayList<String>();
	
	public Boolean hasCache(String player) {
		if (vitorias.containsKey(player) && derrotas.containsKey(player)) {
			return true;
		}
		return false;
	}
	
	/*public void a(String player) {
		vitorias.put(player, 1);
		derrotas.put(player, 1);
	}*/
	
	public Set<String> getAllVitorias() {
		return vitorias.keySet();
	}
	
	public Set<String> getAllDerrotas() {
		return derrotas.keySet();
	}
	
	public Set<String> getAllKDR() {
		return kdr.keySet();
	}
	
	public Set<String> getAllArmazem() {
		return armazem.keySet();
	}
	
	public Integer getVitoriasFrom(String player) {
		return vitorias.get(player) == null ? 0 : vitorias.get(player);
	}
	
	public Integer getDerrotasFrom(String player) {
		return derrotas.get(player) == null ? 0 : derrotas.get(player);
	}
	
	public Double getKDRfrom(String player) {
		updateKDR(player);
		int vitorias = getVitoriasFrom(player);
		double derrotas = getDerrotasFrom(player);
		double KDR = (derrotas > 0 ? (double) vitorias/derrotas : vitorias);
		return KDR;
	}
	
	public void updateKDR(String player) {
		int vitorias = getVitoriasFrom(player);
		double derrotas = getDerrotasFrom(player);
		double KDR = (derrotas > 0 ? (double) vitorias/derrotas : vitorias);
			if (kdr.containsKey(player)) {
				kdr.replace(player, (double) KDR);
			} else {
				kdr.put(player, (double) KDR);
			}
		}
	
	public Boolean hasArmazem(String player) {
		if (armazem.containsKey(player) && armazem.get(player) != null) {
			return true;
		}
		return false;
	}
	
	public String getArmazemFrom(String player) {
		return armazem.get(player);
	}
	
	public void clearArmazemOf(String player) {
		if (armazem.containsKey(player)) {
			armazem.replace(player, null);
		}
	}
	
	public void setPlayerArmazem(String player, String stackBase64) {
		if (armazem.containsKey(player)) {
			armazem.replace(player, stackBase64);
		} else {
			armazem.put(player, stackBase64);
		}
	}
	
	public void setVitoriasTo(String player, int value) {
		if (vitorias.containsKey(player)) {
			vitorias.replace(player, value);
		} else {
			vitorias.put(player, value);
		}
	}
	
	public void setDerrotasTo(String player, int value) {
		if (derrotas.containsKey(player)) {
			derrotas.replace(player, value);
		} else {
			derrotas.put(player, value);
		}
	}
	
	public void addVitoriasTo(String player) {
		if (vitorias.containsKey(player)) {
		setVitoriasTo(player, getVitoriasFrom(player) + 1);
		} else {
			vitorias.put(player, 1);
		}
		updateKDR(player);
	}
	
	public void addDerrotasTo(String player) {
		if (derrotas.containsKey(player)) {
			setDerrotasTo(player, getDerrotasFrom(player) + 1);
		} else {
			derrotas.put(player, 1);
		}
		updateKDR(player);
	}

}
