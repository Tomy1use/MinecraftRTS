package com.amoebaman.mcrts.executors;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.amoebaman.mcrts.RTSPlugin;
import com.amoebaman.mcrts.objects.RTSPlayer;
import com.amoebaman.mcrts.objects.Unit;

public class ArmyExecutor implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args){
		try{
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "Requires player context");
				return true;
			}
			if(args == null || args.length == 0)
				return true;
			Player player = (Player) sender;
			RTSPlayer rtsPlayer = RTSPlugin.getRTSPlayer(player);

			if(args[0].equalsIgnoreCase("list")){
				RTSPlayer target = rtsPlayer;
				if(args.length > 1)
					target = RTSPlugin.getRTSPlayer(Bukkit.getPlayer(args[2]));
				if(target == null){
					player.sendMessage(ChatColor.RED + "Specified RTS player does not exist");
					return true;
				}
				HashMap<EntityType, Integer> numUnits = new HashMap<EntityType, Integer>();
				for(Unit unit : rtsPlayer.army){
					if(!numUnits.containsKey(unit.getType()))
						numUnits.put(unit.getType(), 0);
					numUnits.put(unit.getType(), numUnits.get(unit.getType()) + 1);
				}
				HashMap<EntityType, Integer> numSelected = new HashMap<EntityType, Integer>();
				for(Unit unit : rtsPlayer.selected){
					if(!numSelected.containsKey(unit.getType()))
						numSelected.put(unit.getType(), 0);
					numSelected.put(unit.getType(), numSelected.get(unit.getType()) + 1);
				}
				player.sendMessage(ChatColor.GOLD + target.name + "'s army");
				for(EntityType type : numUnits.keySet()){
					String message = "" + ChatColor.GRAY + numUnits.get(type) + " " + type.name().toLowerCase().replaceAll("_", " ") + "s";
					if(numSelected.containsKey(type))
						message += ChatColor.DARK_GRAY + " (" + numSelected.get(type) + " selected)";
					player.sendMessage(message);
				}
			}

			if(args[0].equalsIgnoreCase("killall")){
				ArrayList<Unit> copy = new ArrayList<Unit>();
				copy.addAll(rtsPlayer.army);
				for(Unit unit : copy)
					if(unit != null)
						unit.die();
			}
			
			if(args[0].equalsIgnoreCase("select")){
				rtsPlayer.selected.clear();
				rtsPlayer.selected.addAll(rtsPlayer.army);
				player.sendMessage("Selected all units");
			}

			if(args[0].equalsIgnoreCase("deselect")){
				rtsPlayer.selected.clear();
				player.sendMessage("Deselected all units");
			}

		}
		catch(Exception e){
			sender.sendMessage(e.getClass().getName() + " - " + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

}
