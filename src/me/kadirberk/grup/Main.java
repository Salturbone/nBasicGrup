package me.kadirberk.grup;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.kadirberk.grup.DM.DataIssues;
import me.kadirberk.grup.Utils.G_PlayerData;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {

    public static JavaPlugin ekl;
    public static int task;
    
    public void onEnable() {
        ekl = this;
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ListenerClass(), this);

        DataIssues.initalize();
        System.out.println("BasicGroup enabled!");

        BukkitScheduler scheduler = getServer().getScheduler();
        task = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!DataIssues.players.containsKey(p.getUniqueId())) {
                        DataIssues.players.put(p.getUniqueId(), new G_PlayerData(p.getUniqueId(), UUID.randomUUID(), null));
                        DataIssues.players.get(p.getUniqueId()).createFile();
                        DataIssues.players.saveAndUnloadData(p.getUniqueId());
                    }
                }
            }
        }, 5L, 5L);
    }

    @Override
    public void onDisable() {
        DataIssues.players.saveAndUnloadAllDatas();
        System.out.println("BasicGroup disabled!");
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p;
        if(label.equalsIgnoreCase("grup")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.DARK_RED + "Bu komutu yalnızca oyuncular kullanabilir.");
                return false;
            }
            
            p = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("chat")) {
                    if(DataIssues.hasGroup(p.getUniqueId())) {
                        DataIssues.players.get(p.getUniqueId()).chatstate = !DataIssues.players.get(p.getUniqueId()).chatstate;
                        p.sendMessage("Grup chati durumu değiştirildi.");
                    }
                }
                if (args[0].equalsIgnoreCase("kur")) {
                    if (!DataIssues.hasGroup(p.getUniqueId())) {
                        DataIssues.players.get(p.getUniqueId()).g_uid = p.getUniqueId();
                        DataIssues.players.get(p.getUniqueId()).rank = "leader";
                        p.sendMessage(ChatColor.AQUA + "Grup kuruldu!");
                        return true;
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Bir gruba mensupken grup kuramazsın.");
                        p.sendMessage(ChatColor.DARK_RED + "Gruptan ayrılmak için: /grup ayrıl");
                        if (DataIssues.players.get(p.getUniqueId()).rank.equalsIgnoreCase("leader")) {
                            p.sendMessage(ChatColor.DARK_RED + " !! Önemli !!  Bu işlem grubunu dağıtacaktır.");
                        }
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("kabul")) {
                    if (DataIssues.hasInvite(p.getUniqueId())) {
                        DataIssues.players.get(p.getUniqueId()).g_uid = DataIssues.players.get(p.getUniqueId()).invite_uid;
                        DataIssues.players.get(p.getUniqueId()).invite_uid = UUID.randomUUID();
                        DataIssues.players.get(p.getUniqueId()).rank = "Member";
                        p.sendMessage(ChatColor.AQUA + "Davet kabul edildi!");
                        Bukkit.getPlayer(DataIssues.players.get(p.getUniqueId()).invite_uid).sendMessage(ChatColor.AQUA + p.getName() + " Davetinizi kabul etti!");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("reddet")) {
                    if (DataIssues.hasInvite(p.getUniqueId())) {
                        p.sendMessage(ChatColor.DARK_RED + "Davet reddedildi");
                        Bukkit.getPlayer(DataIssues.players.get(p.getUniqueId()).invite_uid).sendMessage(ChatColor.DARK_RED + p.getName() + " Davetinizi reddetti!");
                        DataIssues.players.get(p.getUniqueId()).leave();
                        return true;
                    }
                }
                if(args[0].equalsIgnoreCase("ayrıl")) {
                    if(DataIssues.hasGroup(p.getUniqueId())) {
                        p.sendMessage(ChatColor.DARK_RED + "Gruptan ayrıldınız.");
                        DataIssues.players.get(p.getUniqueId()).leave();
                        return true;
                    }
                }
            }
            
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("davet")) {
                    for (Player plyr : Bukkit.getOnlinePlayers()) {
                        if (plyr.getName().equalsIgnoreCase(args[1]) &&
                         !DataIssues.hasGroup(plyr.getUniqueId())) {
                            plyr.sendMessage(ChatColor.AQUA + p.getName() + " Seni grubuna davet etti.");
                            plyr.sendMessage(ChatColor.AQUA + "Katılmak için: /grup kabul");
                            plyr.sendMessage(ChatColor.AQUA + "Reddetmek için: /grup reddet");
                            return true;
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("at")) {
                    for (Player plyr : Bukkit.getOnlinePlayers()) {
                        if (plyr.getName().equalsIgnoreCase(args[1]) && 
                        DataIssues.hasGroup(plyr.getUniqueId()) && 
                        DataIssues.players.get(p.getUniqueId()).rank.equalsIgnoreCase("leader") &&
                        DataIssues.players.get(p.getUniqueId()) == DataIssues.players.get(plyr.getUniqueId())) {
                            plyr.sendMessage(ChatColor.DARK_RED + "Gruptan atıldın!");
                            DataIssues.players.get(plyr.getUniqueId()).leave();
                            p.sendMessage(ChatColor.AQUA + args[1] + " adlı oyuncuyu grubundan attın");
                            return true;
                        }
                    }
                }
            }
        } 
        

        return true;
    }

}