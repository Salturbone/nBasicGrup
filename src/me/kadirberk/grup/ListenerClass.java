package me.kadirberk.grup;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.kadirberk.grup.DM.DataIssues;
import me.kadirberk.grup.Utils.G_PlayerData;

public class ListenerClass implements Listener{

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player attacker = (Player)event.getDamager();
        Player victim = (Player)event.getEntity();
        if (DataIssues.players.get(attacker.getUniqueId()).g_uid == DataIssues.players.get(victim.getUniqueId()).g_uid) {
            event.setCancelled(true);
            return;
        }
        return;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        G_PlayerData data;
        if (DataIssues.players.containsKey(p.getUniqueId())) {
            data = DataIssues.players.get(p.getUniqueId());
        } else {
            return;
        }
         
        if (data.chatstate) {
            String mess = event.getMessage();
            for(Player players : Bukkit.getOnlinePlayers()) {
                if(DataIssues.players.get(players.getUniqueId()).g_uid == data.g_uid) {
                    players.sendMessage(ChatColor.AQUA + "-GRUP- " + ChatColor.RESET +p.getName() + ": " + mess);
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!DataIssues.players.containsKey(player.getUniqueId())) {
            G_PlayerData plyr = new G_PlayerData(player.getUniqueId(), UUID.randomUUID(), null);
            DataIssues.players.put(player.getUniqueId(), plyr);
        } else {
            DataIssues.players.loadData(player.getUniqueId());
        }
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        G_PlayerData plyr = DataIssues.players.get(player.getUniqueId());
        DataIssues.players.saveAndUnloadData(plyr.uid);
    }
    

}