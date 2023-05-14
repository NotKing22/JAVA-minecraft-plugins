package br.com.zsnow.eventocustom;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class BossManager {
	
	private String bossName;
	private Double bossHP;
	private ItemStack reward;
	private ItemStack bossItemInHand;
	private EntityType entidade;
	private List<PotionEffect> effects;//
	private ItemStack[] armours;

	public void BossCreate(String bossname, Double bossHP, ItemStack reward, ItemStack bossItemInHand, ItemStack[] armours) {
		this.setName(bossname);
		this.SetHealth(bossHP);
		this.DefineReward(reward);
		this.SetItemInHand(bossItemInHand);
		this.SetArmours(armours);
	}
	
	public List<PotionEffect> getEffects() {
		return effects;
	}
	
	public String getName() {
		return bossName;
	}
	
	public Double GetHP() {
		return bossHP;
	}
	
	public ItemStack GetReward() {
		return reward;
	}
	
	public ItemStack ItemHand() {
		return bossItemInHand;
	}
	
	public EntityType GetBossEntity() {
		return entidade;
	}
	
	public ItemStack[] Getarmours() {
		return armours;
	}
	
	//setters
	
	public void setName(String bossName) {
		this.bossName = bossName;
	}
	
	public void SetHealth(double bossHP) {
		this.bossHP = bossHP;
	}

	public void DefineReward(ItemStack reward) {
		this.reward = reward;
	}
	
	public void SetItemInHand(ItemStack bossItemInHand) {
		this.bossItemInHand = bossItemInHand;
	}
	
	public void SetBossEntity(EntityType entidade) {
		this.entidade = entidade;
	}
	
	public void SetArmours(ItemStack[] armours) {
		this.armours =  armours;
	}
	
	public void addEffect(List<PotionEffect> effects) {
		this.effects = effects;
	}
	
	public void removeEffect(List<PotionEffect> effects, Player receiveMessage) {
		effects.removeAll(getEffects());
		receiveMessage.sendMessage("§aTodos os efeitos da entidade foram removidos.");
	}
}
