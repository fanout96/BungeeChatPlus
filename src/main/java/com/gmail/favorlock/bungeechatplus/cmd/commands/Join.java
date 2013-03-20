package com.gmail.favorlock.bungeechatplus.cmd.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.gmail.favorlock.bungeechatplus.BungeeChatPlus;
import com.gmail.favorlock.bungeechatplus.cmd.BaseCommand;
import com.gmail.favorlock.bungeechatplus.entities.Channel;
import com.gmail.favorlock.bungeechatplus.entities.Chatter;
import com.gmail.favorlock.bungeechatplus.utils.FontFormat;

public class Join extends BaseCommand {
	
	BungeeChatPlus plugin;

	public Join(BungeeChatPlus plugin) {
		super("BCP Join");
		this.plugin = plugin;
		setDescription("Join a channel");
		setUsage("/bcp join <channel>");
		setArgumentRange(1, 1);
		setPermission("bungeechat.channels.join");
		setIdentifiers(new String[] { "bcp join" });
	}

	@Override
	public boolean execute(CommandSender sender, String identifier,
			String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return false;
		}
		Chatter chatter = plugin.getChatterManager().getChatter(sender.getName());
		if (chatter == null) {
			return false;
		}
		Channel channel = plugin.getChannelManager().getChannel(args[0]);
		if (channel == null) {
			sender.sendMessage(FontFormat.translateString("&eThe channel &2" + args[0] + "&e does not exist"));
			return false;
		}
		boolean inChannel = channel.getChatters().contains(chatter);
		if ((channel.getChatters().size() >= channel.getMaxChatters()) && !(channel.getMaxChatters() == -1)
				&& !inChannel) {
			sender.sendMessage(FontFormat.translateString("&eThe channel &2" + args[0] + "&e is full"));
			return false;
		}
		int maxChannelsPerChatter = plugin.getConfig().Setting_MaxChannelsPerChatter;
		if ((chatter.getChannels().size() >= maxChannelsPerChatter) && !(maxChannelsPerChatter == -1) && !inChannel) {
			sender.sendMessage(FontFormat.translateString("&4You are in the maximum number of channels"));
			return false;
		}
		if(plugin.getChatterManager().getChatter(sender.getName()).addChannel(channel)) {
			sender.sendMessage(FontFormat.translateString("&eYou you joined the channel &2" + channel.getName()));
			return true;
		}
		sender.sendMessage(FontFormat.translateString("&2You are already in the channel &2" + channel.getName()));
		return false;
	}

}