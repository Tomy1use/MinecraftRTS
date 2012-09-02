 package com.amoebaman.mcrts.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.amoebaman.mcrts.RTSPlugin;
import com.amoebaman.mcrts.objects.EntityObjective;
import com.amoebaman.mcrts.objects.LocationObjective;
import com.amoebaman.mcrts.objects.RTSPlayer;
import com.amoebaman.mcrts.objects.Unit;
import com.amoebaman.mcrts.objects.Objective.Aggression;
import com.amoebaman.mcrts.utilities.S_Location;

public class CommanderExecutor implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args){
		try{
			if(!(sender instanceof Player) || args.length == 0)
				return true;
			Player player = (Player) sender;
			RTSPlayer rtsPlayer = RTSPlugin.getRTSPlayer(player);

			if(args[0].equalsIgnoreCase("goto")){
				LocationObjective obj = new LocationObjective(player.getLastTwoTargetBlocks(null, 100).get(0).getLocation(), Aggression.PASSIVE, Aggression.PASSIVE_AGGRESSIVE, 4, 16);
				for(Unit unit : rtsPlayer.selected)
					unit.setObjective(obj);
				player.sendMessage(rtsPlayer.selected.size() + " selected units are now moving to " + S_Location.toString(obj.getTargetLoc()));
			}

			if(args[0].equalsIgnoreCase("strike")){
				LocationObjective obj = new LocationObjective(player.getLastTwoTargetBlocks(null, 100).get(0).getLocation(), Aggression.PASSIVE, Aggression.AGGRESSIVE, 4, 16);
				for(Unit unit : rtsPlayer.selected)
					unit.setObjective(obj);
				player.sendMessage(rtsPlayer.selected.size() + " selected units are now moving to strike " + S_Location.toString(obj.getTargetLoc()));
			}

			if(args[0].equalsIgnoreCase("assault")){
				LocationObjective obj = new LocationObjective(player.getLastTwoTargetBlocks(null, 100).get(0).getLocation(), Aggression.AGGRESSIVE, Aggression.AGGRESSIVE, 4, 16);
				for(Unit unit : rtsPlayer.selected)
					unit.setObjective(obj);
				player.sendMessage(rtsPlayer.selected.size() + " selected units are now moving to assault " + S_Location.toString(obj.getTargetLoc()));
			}

			if(args[0].equalsIgnoreCase("follow")){
				EntityObjective obj = new EntityObjective(player, Aggression.PASSIVE, Aggression.PASSIVE_AGGRESSIVE, 8, 16);
				for(Unit unit : rtsPlayer.selected)
					unit.setObjective(obj);
				player.sendMessage(rtsPlayer.selected.size() + " selected units are now following you");
			}

			if(args[0].equalsIgnoreCase("protect")){
				EntityObjective obj = new EntityObjective(player, Aggression.PASSIVE, Aggression.AGGRESSIVE, 8, 16);
				for(Unit unit : rtsPlayer.selected)
					unit.setObjective(obj);
				player.sendMessage(rtsPlayer.selected.size() + " selected units are now protecting you");
			}
		}
		catch(Exception e){
			sender.sendMessage(e.getClass().getName() + " - " + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

}
