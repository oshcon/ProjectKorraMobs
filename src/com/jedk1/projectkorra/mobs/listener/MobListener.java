package com.jedk1.projectkorra.mobs.listener;

import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.manager.EntityManager;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class MobListener implements Listener {

	ProjectKorraMobs plugin;

	public MobListener(ProjectKorraMobs plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFireAttack(EntityTargetEvent event) {
		if (event.getTarget() == null) {
			return;
		}
		if (event.getEntity() instanceof LivingEntity && event.getTarget() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (entity instanceof Zombie) {
				EntityManager.addEntity(entity, (LivingEntity) event.getTarget());
			}
		}
	}
}
