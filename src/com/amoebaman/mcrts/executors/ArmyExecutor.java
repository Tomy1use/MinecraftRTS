package com.amoebaman.mcrts.executors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
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
			Player player = (Player) sender;
			RTSPlayer rtsPlayer = RTSPlugin.getRTSPlayer(player);

			if(args[0].equalsIgnoreCase("list")){
				int page = 1;
				RTSPlayer target = rtsPlayer;
				if(args.length > 1)
					page = Integer.parseInt(args[1]);
				if(args.length > 2)
					target = RTSPlugin.getRTSPlayer(Bukkit.getPlayer(args[2]));
				if(target == null){
					player.sendMessage(ChatColor.RED + "Specified RTS player does not exist");
					return true;
				}
				player.sendMessage(ChatColor.GOLD + target.name + "'s army (" + page + "/" + (int)Math.ceil(target.army.size() / 8.0) + ")");
				for(int i = (page - 1) * 10; i < page * 10 && i < target.army.size(); i++)
					player.sendMessage("" + ChatColor.DARK_GRAY + (i+1) + " // " + ChatColor.GRAY + target.army.get(i).toString());
			}

			if(args[0].equalsIgnoreCase("killall")){
				for(Unit unit : rtsPlayer.army)
					if(unit != null && unit.getEntity() != null)
						unit.getEntity().remove();
				rtsPlayer.army.clear();
				rtsPlayer.selected.clear();
				player.sendMessage(ChatColor.RED + "All units in army killed");
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
