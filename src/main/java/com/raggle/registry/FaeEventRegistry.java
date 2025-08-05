package com.raggle.registry;

import org.jetbrains.annotations.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.raggle.FaeUtil;
import com.raggle.HalfDream;
import com.raggle.util.DreamState;

import dev.onyxstudios.cca.api.v3.entity.PlayerCopyCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class FaeEventRegistry {
	
    
	
	public static void init() {
		PlayerCopyCallback.EVENT.register(FaeEventRegistry::afterRespawn);
		PlayerBlockBreakEvents.BEFORE.register(FaeEventRegistry::beforeBlockBreak);
		UseBlockCallback.EVENT.register(FaeEventRegistry::onBlockUse);
		
		CommandRegistrationCallback.EVENT.register(FaeEventRegistry::dreamstate);
		CommandRegistrationCallback.EVENT.register(FaeEventRegistry::dreamclear);
		CommandRegistrationCallback.EVENT.register(FaeEventRegistry::interlope);
		
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			Identifier fishingID = new Identifier("gameplay/fishing/fish");
		    if (source.isBuiltin() && fishingID.equals(id)) {
	            
		        LootPool.Builder poolBuilder = LootPool.builder()
	                .with(LootTableEntry.builder(new Identifier(HalfDream.MOD_ID, "fishing_chest")));
		        
		        tableBuilder.pool(poolBuilder);
		    }
		});
	}
	
	private static void afterRespawn(ServerPlayerEntity copy, ServerPlayerEntity original, boolean wasDeath) {
		//FaeUtil.setDream(copy, FaeUtil.getDreamState(original));
	}
	private static boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity){
		
		DreamState player_dream = FaeUtil.getDreamState(player);
		
		if(player_dream == DreamState.ASLEEP) {
			if(!FaeUtil.isDreamBlock(pos, world)) {
				ItemEntity dream_resin = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(FaeItemRegistry.DREAM_RESIN));
				world.spawnEntity(dream_resin);
				dream_resin.setToDefaultPickupDelay(); 
				FaeUtil.setDream(dream_resin, DreamState.ASLEEP);
				FaeUtil.setDreamAir(pos, true, world);
				return false;
			}
		}
		return true;
	}
	
	private static ActionResult onBlockUse(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {

		if(world.isClient()) {
			return ActionResult.PASS;
		}
		//trying to figure out if block was placed on another, or replaced it
		Iterable<ItemStack> stacks = player.getHandItems();
		DreamState dreamState = FaeUtil.getDreamState(player);
		for(ItemStack itemStack : stacks) {
			ItemPlacementContext itemContext = new ItemPlacementContext(world, player, hand, itemStack, hitResult);
			if(itemContext.canPlace()) {
				if(dreamState == DreamState.ASLEEP) {
					FaeUtil.queueDreamBlock(itemContext.getBlockPos(), world);
					break;
				}
				else if(dreamState == DreamState.AWAKE && FaeUtil.isDreamBlock(itemContext.getBlockPos(), world)) {
					world.setBlockState(itemContext.getBlockPos(), Blocks.AIR.getDefaultState());
					break;
				}
			}
		}
		
		return ActionResult.PASS;
	}
	
	private static LiteralCommandNode<ServerCommandSource> dreamstate(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
		return dispatcher.register(
				CommandManager
				.literal("dreamstate")
				.requires(source -> source.hasPermissionLevel(2))
				.then(CommandManager.argument("value", IntegerArgumentType.integer())
						.executes(context -> 
				{
					byte dreamLevel = (byte)IntegerArgumentType.getInteger(context, "value");
					if(dreamLevel >= 0 && dreamLevel <= 2) {
						FaeUtil.setDream(context.getSource().getPlayer(), DreamState.fromByte(dreamLevel));
						context.getSource().sendFeedback(() -> Text.literal("Set dream to %s".formatted(DreamState.fromByte(dreamLevel))), false);
					}
					else {
						context.getSource().sendError(Text.literal("Value must be 0, 1, or 2"));
						return -1;
					}
					return 1;
		})
				.then(CommandManager.argument("entity", EntityArgumentType.entity())
						.executes(context -> 
				{
					byte dreamLevel = (byte)IntegerArgumentType.getInteger(context, "value");
					Entity entity = EntityArgumentType.getEntity(context, "entity");
					if(dreamLevel >= 0 && dreamLevel <= 2) {
						FaeUtil.setDream(entity, DreamState.fromByte(dreamLevel));
						context.getSource().sendFeedback(() -> Text.literal("Set dream of %s to %s".formatted(entity.getName().getString(), DreamState.fromByte(dreamLevel))), true);
					}
					else {
						context.getSource().sendError(Text.literal("Value must be 0, 1, or 2"));
						return -1;
					}
					return 1;
		})
)));
	}
	private static LiteralCommandNode<ServerCommandSource> dreamclear(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
		return dispatcher.register(
			CommandManager
			.literal("dreamclear")
			.requires(source -> source.hasPermissionLevel(2))
			.executes(context -> 
			{
				context.getSource().getServer();
				Chunk chunk = context.getSource().getPlayer().getWorld().getChunk(context.getSource().getPlayer().getBlockPos());
				int air_count = FaeComponentRegistry.DREAM_AIR.get(chunk).clear();
				int block_count = FaeComponentRegistry.DREAM_BLOCKS.get(chunk).clear();
				
				context.getSource().sendFeedback(() -> Text.literal("Deleted "+air_count+" dream air and "+block_count+" dream blocks"), false);
				return 1;
			})
		);
	}
	private static LiteralCommandNode<ServerCommandSource> interlope(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
		return dispatcher.register(
			CommandManager
			.literal("interlope")
			.requires(source -> source.hasPermissionLevel(2))
			.then(CommandManager.argument("value", BoolArgumentType.bool())
			.executes(context -> 
			{
				boolean value = BoolArgumentType.getBool(context, "value");
				FaeUtil.setInterlope(context.getSource().getPlayer(), value);
				context.getSource().sendFeedback(() -> Text.literal("Set interlope to %s".formatted(value)), false);
					
				return 1;
		}))
		);
	}
}