package com.raggle.item;

import java.util.Iterator;
import java.util.Optional;

import com.raggle.FaeUtil;
import com.raggle.registry.FaeTagRegistry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.InstrumentTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ResinHorn extends Item {

	private final TagKey<Instrument> instrumentTag;
	
	public ResinHorn() {
		super(new FabricItemSettings().fireproof());
		this.instrumentTag = InstrumentTags.GOAT_HORNS;
	}
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		Optional<? extends RegistryEntry<Instrument>> optional = this.getInstrument(itemStack);
		if (optional.isPresent()) {
			Instrument instrument = (Instrument)(optional.get()).value();
			user.setCurrentHand(hand);
			playSound(world, user, instrument);
			user.getItemCooldownManager().set(this, instrument.useDuration());
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			if(world instanceof ServerWorld serverWorld) {
				BlockPos pos = serverWorld.locateStructure(FaeTagRegistry.RESIN_HORN_LOCATED, user.getBlockPos(), 160, false);
				if(pos != null) {
					user.sendMessage(Text.of("It's coming from "+pos.getX()+", "+pos.getY()+", "+pos.getZ()));
					FaeUtil.setInterlope(user, true);
				}
				else {
					user.sendMessage(Text.of("No response..."));
				}
			}
			return TypedActionResult.consume(itemStack);
		} else {
			return TypedActionResult.fail(itemStack);
		}
	}
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.TOOT_HORN;
	}
	private Optional<? extends RegistryEntry<Instrument>> getInstrument(ItemStack stack) {
		NbtCompound nbtCompound = stack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("instrument", NbtElement.STRING_TYPE)) {
			Identifier identifier = Identifier.tryParse(nbtCompound.getString("instrument"));
			if (identifier != null) {
				return Registries.INSTRUMENT.getEntry(RegistryKey.of(RegistryKeys.INSTRUMENT, identifier));
			}
		}

		Iterator<RegistryEntry<Instrument>> iterator = Registries.INSTRUMENT.iterateEntries(this.instrumentTag).iterator();
		return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
	}
	private static void playSound(World world, PlayerEntity player, Instrument instrument) {
		SoundEvent soundEvent = instrument.soundEvent().value();
		float f = instrument.range() / 16.0F;
		world.playSoundFromEntity(player, player, soundEvent, SoundCategory.RECORDS, f, 1.0F);
		world.emitGameEvent(GameEvent.INSTRUMENT_PLAY, player.getPos(), GameEvent.Emitter.of(player));
	}

}
