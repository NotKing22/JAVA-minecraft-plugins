package me.zsnow.desafioepico;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.zsnow.desafioepico.controller.MenuController;

public class CustomRecipes implements Listener {
	
	@EventHandler
    public void handleCustomCrafting(PrepareItemCraftEvent e) {
        CraftingInventory inventory = e.getInventory();
	  
        if (e.getInventory().getType() == InventoryType.WORKBENCH) {
	    	
	        ItemStack[] matrix = inventory.getMatrix();
	        MenuController item = new MenuController();
	    	
	        ItemStack slot1 = matrix[1]; ItemStack slot2 = matrix[2]; /*ItemStack slot3 = matrix[3];*/ 
	        ItemStack slot4 = matrix[4]; /*ItemStack slot5 = matrix[5]; ItemStack slot6 = matrix[6];*/		ItemStack slot0 = matrix[0]; 
		    ItemStack slot7 = matrix[7]; /*ItemStack slot8 = matrix[8]; ItemStack slot9 = matrix[9]; slot 1=[0]*/
		    
	    	if (slot0 != null && slot1 != null && slot2 != null && slot4 != null && slot7 != null) {
		       if (slot0.isSimilar(item.getCristalEmber()) && slot1.isSimilar(item.getCristalEmber()) && slot2.isSimilar(item.getCristalEmber())
	    		   && slot4.isSimilar(item.getEmberStick()) && slot7.isSimilar(item.getEmberStick())) {
		            	e.getInventory().setResult(MenuController.getEmberPickaxe());
		            	return;
			        } else {
			        	if (e.getInventory().getResult() != null && e.getInventory().getResult().isSimilar(MenuController.getEmberPickaxe())) {
				        	e.getInventory().setResult(null);
				        	return;
			        	}
			        }
	        } else { return; }
	    	
	    	if (slot1 != null && slot4 != null && slot7 != null) {
	 	       if (slot1.isSimilar(item.getCarminitaBar()) && slot4.isSimilar(item.getCarminitaEmpunhadura()) && slot7.isSimilar(item.getCarminitaEmpunhadura())) {
	 	            	e.getInventory().setResult(item.getCarminitaSpade());
	 	            	return;
	 		        } else {
	 		        	if (e.getInventory().getResult() != null && e.getInventory().getResult().isSimilar(item.getCarminitaSpade())) {
	 		        	e.getInventory().setResult(null);
	 		        	return;
	 		        	}
	 		        }
	         } else { return; }
	    	
	    	if (slot1 != null && slot4 != null && slot7 != null) {
	    		if (slot1.isSimilar(item.getCarminitaBar()) && slot4.isSimilar(item.getCarminitaBar()) && 
	    			slot7.isSimilar(item.getCarminitaEmpunhadura())) {
	    				e.getInventory().setResult(MenuController.getCarminitaSword());
	 	            	HumanEntity p = e.getView().getPlayer();
	 	            	((Player) p).playSound(p.getLocation(), Sound.AMBIENCE_CAVE, 1.0f, 0.5f);
	 		        } else {
	 		        	if (e.getInventory().getResult() != null && e.getInventory().getResult().equals(MenuController.getCarminitaSword())) {
		 		        	e.getInventory().setResult(null);
		 		        	return;
	 		        	}	
	 		        }
	         } else { return; }
	    } else {
	    	return;
	    }
    }
	
	
	
	  public void makeRecipe() {
		  MenuController item = new MenuController();
        ShapedRecipe EmberPickaxe = new ShapedRecipe(MenuController.getEmberPickaxe()); 
        EmberPickaxe.shape("***", 
			        	   " | ", 
			        	   " | ");	
        EmberPickaxe.setIngredient('*', Material.QUARTZ); 
        EmberPickaxe.setIngredient('|', Material.STICK);
        Bukkit.getServer().addRecipe(EmberPickaxe); 
        
        ShapedRecipe CarminitaSword = new ShapedRecipe(MenuController.getCarminitaSword());
        CarminitaSword.shape(" * ", 
			        		 " * ", 
			        		 " | ");
        CarminitaSword.setIngredient('*', Material.NETHER_BRICK_ITEM);
        CarminitaSword.setIngredient('|', Material.BLAZE_ROD);
        Bukkit.getServer().addRecipe(CarminitaSword);
        
        ShapedRecipe CarminitaSpade = new ShapedRecipe(item.getCarminitaSpade());
        CarminitaSpade.shape(" * ",
        					 " | ",
        					 " | ");
        CarminitaSpade.setIngredient('*', Material.NETHER_BRICK_ITEM);
        CarminitaSpade.setIngredient('|', Material.BLAZE_ROD);
        Bukkit.getServer().addRecipe(CarminitaSpade);
        
	  }
}
