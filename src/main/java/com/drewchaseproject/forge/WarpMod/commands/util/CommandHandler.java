package com.drewchaseproject.forge.WarpMod.commands.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.drewchaseproject.forge.WarpMod.WarpMod;
import com.drewchaseproject.forge.WarpMod.WarpMod.LogType;
import com.drewchaseproject.forge.WarpMod.commands.WarpCommand;
import com.drewchaseproject.forge.WarpMod.util.WarpPlayer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("all")
public class CommandHandler {

	public static class CommandTimeOut {

		public CommandTimeOut(float duration) {
			final Runnable timer = new Thread() {
				@Override
				public void run() {
					// TODO: DO STUFF
				}
			};

			final ExecutorService executor = Executors.newSingleThreadExecutor();
			final Future future = executor.submit(timer);
			executor.shutdown();

			try {
				future.get((int) duration, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				WarpMod.log(LogType.Warning, "Timer ERROR: Timer Interrupted!\nMESSAGE: " + e.getLocalizedMessage());
			} catch (ExecutionException e) {
				WarpMod.log(LogType.Warning, "Timer ERROR: Execution Error!\nMESSAGE: " + e.getLocalizedMessage());
			} catch (TimeoutException e) {
				WarpMod.log(LogType.Warning, "Timer ERROR: Timer Timed Out Incorrectly!\nMESSAGE: " + e.getLocalizedMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!executor.isTerminated())
				executor.shutdownNow();
		}

	}

	public static class PlayerDeathHandler {

		@SubscribeEvent
		public void onLivingDeath(LivingDeathEvent event) {
			Entity e = event.getEntityLiving();
			if (e instanceof ServerPlayerEntity) {
				try {
					ServerPlayerEntity player = (ServerPlayerEntity) e;
					WarpCommand wc = new WarpCommand();
					wc.setPlayer(player);
					BlockPos pos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
					float pitch = player.getPitchYaw().y;
					float yaw = player.getPitchYaw().x;
					wc.back(pos, yaw, pitch, new WarpPlayer(player).getDimensionResourceLocation(), player);
				} catch (NullPointerException ex) {
					ex.printStackTrace();
					return;
				}
			}
		}

	}
}