package me.kadirberk.grup.DM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import me.kadirberk.grup.Utils.G_PlayerData;
import me.kadirberk.grup.Main;

import me.kadirberk.grup.DM.HashMapManager.KeyConverter;

public class DataIssues {
    
    public static File playerFile;
    public static HashMapManager<UUID, G_PlayerData> players;

    public static boolean hasGroup(UUID uid) {
        for (G_PlayerData gpd : players.values()) {
            if (players.get(uid).g_uid == gpd.uid) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasInvite(UUID uid) {
        for (G_PlayerData gpd : players.values()) {
            if (players.get(uid).invite_uid == gpd.uid) {
                return true;
            }
        }
        return false;
    }

    public static void initalize() {
        create();
        players = new HashMapManager<UUID, G_PlayerData>(playerFile, new KeyConverter<UUID>() {

            @Override
            protected String toFileName(UUID key) {
                return key.toString() + ".data";
            }

            @Override
            protected UUID toKey(String filename) {
                return UUID.fromString(filename.replaceAll(".data", ""));
            }
        });
    }

    public static void create() {
        playerFile = new File(Main.ekl.getDataFolder(), "Players");
        if (!playerFile.exists())
            playerFile.mkdirs();
    }

    public static void saveObject(Object a, File f) {
        try {
            FileOutputStream d = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(d);
            out.writeObject(a);
            out.close();
            d.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // objeyi file'dan okuma metodu
    public static Object loadObject(File f) {

        if (!f.exists())

        {
            return null;
        }
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream(f);

            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object o = in.readObject();
            in.close();
            fileIn.close();
            return o;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();

        } catch (IOException e1) {
            e1.printStackTrace();

        }
        return null;
    }

}
