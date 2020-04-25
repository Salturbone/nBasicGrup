package me.kadirberk.grup.Utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import me.kadirberk.grup.Main;
import me.kadirberk.grup.DM.DataIssues;

public class G_PlayerData implements Serializable {

    private static final long serialVersionUID = 1L;

    File filedata;
    public boolean chatstate = false;
    public UUID uid;
    public UUID g_uid;
    public UUID old_g_uid;
    public String rank;
    public UUID invite_uid;

    public G_PlayerData(UUID b_uid, UUID b_g_uid, String b_rank) {
        filedata = new File(Main.ekl.getDataFolder(), "Players/" + b_uid.toString() + ".data");
        uid = b_uid;
        g_uid = b_g_uid;
        rank = b_rank;
        old_g_uid = UUID.randomUUID();
        invite_uid = UUID.randomUUID();
    }

    public void createFile() {
        for (G_PlayerData data : DataIssues.players.values()) {
            if (data.g_uid == this.g_uid) {
                if (!filedata.exists()) {
                    filedata.mkdirs();
                    try {
                        filedata.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void leave() {
        old_g_uid = g_uid;
        g_uid = UUID.randomUUID();
        rank = null;
        invite_uid = UUID.randomUUID();
        chatstate = false;
    }
}