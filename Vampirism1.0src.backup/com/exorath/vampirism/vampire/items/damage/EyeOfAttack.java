package com.exorath.vampirism.vampire.items.damage;

import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.VampireItem;

public class EyeOfAttack extends VampireItem{
	private final static int BONUSDAMAGE = 12;
	public EyeOfAttack(int cost, Vampire vampire, Items itemtype) {
		super(cost, vampire, itemtype);
		getVampire().setDamage(getVampire().getDamage() + BONUSDAMAGE);
		
	}
	@Override
	public void removeItem(){
		getVampire().setDamage(getVampire().getDamage() - BONUSDAMAGE);
	}

}
