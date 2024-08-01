package com.raggle.faesied.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.raggle.faesied.api.DreamClientPlayer;
import com.raggle.faesied.client.sequence.SequenceManager;

import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements DreamClientPlayer {

	public int listSize;
	
	@Override
	public boolean isDream() {
		
		return SequenceManager.isCurrentSequenceImportant() ? SequenceManager.getSequence().getDreamState() : this.getPersistantData().getBoolean("faesied");
	}
	
	@Override
	public void setListSize(int i) {
		listSize = i;
	}
	@Override
	public int getListSize() {
		return listSize;
	}
	@Override
	public void incrementList() {
		listSize++;
	}
	@Override
	public void decrementList() {
		listSize--;
	}

}
