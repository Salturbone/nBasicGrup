package me.kadirberk.grup.Utils;

import java.io.Serializable;
import java.util.UUID;

public class G_PlayerData implements Serializable{

    private static final long serialVersionUID = 1L;

    public boolean chatstate = false;
    public UUID uid;
    public UUID g_uid;
    public UUID old_g_uid;
    public String rank;
    public UUID invite_uid;
    public G_PlayerData(UUID b_uid, UUID b_g_uid, String b_rank) {
        uid = b_uid;
        g_uid = b_g_uid;
        rank = b_rank;
        old_g_uid = UUID.randomUUID();
        invite_uid = UUID.randomUUID();
    }

    public void leave() {
        old_g_uid = g_uid;
        g_uid = UUID.randomUUID();
        rank = null;
        invite_uid = UUID.randomUUID();
        chatstate = false;
    }
}