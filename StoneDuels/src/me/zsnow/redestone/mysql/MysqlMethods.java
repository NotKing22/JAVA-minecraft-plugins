package me.zsnow.redestone.mysql;

import com.google.common.collect.Sets;
import me.zsnow.redestone.Main;
import me.zsnow.redestone.cache.DuelCache;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class MysqlMethods extends ConexaoSQL {

    public static ConsoleCommandSender sc = Bukkit.getConsoleSender();
    public static String mito = null;

    public static void createPlayerData(String player) {
        new BukkitRunnable() {
            public void run() {
                PreparedStatement stm;
                try {

                    if(con == null || con.isClosed()) {
                       openConnection();
                    }

                    stm = con.prepareStatement("INSERT INTO dados(player, vitorias, derrotas, kdr, armazem) VALUES (?,?,?,?,?)");
                    stm.setString(1, player);
                    stm.setInt(2, 0);
                    stm.setInt(3, 0);
                    stm.setDouble(4, 0);
                    stm.setString(5, null);
                    stm.execute();
                    stm.close();

                } catch (SQLException ignored) {}
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public static void loadUserData() {
        PreparedStatement stm;
        DuelCache cache = DuelCache.getCache();
        int quantia = 0;
        try {

            if(con == null || con.isClosed()) {
                openConnection();
            }

            stm = con.prepareStatement("SELECT * FROM dados");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                quantia++;
                cache.setVitoriasTo(rs.getString("player"), rs.getInt("vitorias"));
                cache.setDerrotasTo(rs.getString("player"), rs.getInt("derrotas"));
                cache.updateKDR(rs.getString("player"));
                if (rs.getString("armazem") != null) {
                    DuelCache.getCache().setPlayerArmazem(rs.getString("player"), rs.getString("armazem"));
                }
            }
            sc.sendMessage("§a[StoneDuels] Foram carregados um total de " + quantia + " §adados.");
        } catch (Exception e) {
            e.printStackTrace();
            sc.sendMessage("§c[StoneDuels] Houve um erro ao carregar os dados dos jogadores.");
        }
    }

    public static void saveVitoriasToSQL(String player, Integer vitorias) {
        PreparedStatement stm = null;
        try {

            if(con == null || con.isClosed()) {
                openConnection();
            }

            stm = con.prepareStatement("UPDATE dados SET vitorias = ? WHERE player = ?");
            stm.setInt(1, vitorias);
            stm.setString(2, player);
            stm.executeUpdate();
            stm.close();
        } catch (Exception e) {
            sc.sendMessage("§c[StoneDuels] erro ao salvar vitorias.");
        }
    }

    public static void saveDerrotasToSQL(String player, Integer derrotas) {
        PreparedStatement stm = null;
        try {

            if(con == null || con.isClosed()) {
                openConnection();
            }

            stm = con.prepareStatement("UPDATE dados SET derrotas = ? WHERE player = ?");
            stm.setInt(1, derrotas);
            stm.setString(2, player);
            stm.executeUpdate();
            stm.close();
        } catch (Exception e) {
            sc.sendMessage("§c[StoneDuels] erro ao salvar derrotas.");
        }
    }

    public static void saveKDRtoSQL(String player, Double double1) {


        PreparedStatement stm = null;
        try {

            if(con == null || con.isClosed()) {
                openConnection();
            }

            stm = con.prepareStatement("UPDATE dados SET kdr = ? WHERE player = ?");
            stm.setDouble(1, double1);
            stm.setString(2, player);
            stm.executeUpdate();
            stm.close();
        } catch (Exception e) {
            sc.sendMessage("§c[StoneDuels] erro ao salvar KDR.");
        }
    }

    public static void saveArmazemToSQL(String player, String armazemBase64) {
        PreparedStatement stm = null;
        try {

            if(con == null || con.isClosed()) {
                openConnection();
            }

            stm = con.prepareStatement("UPDATE dados SET armazem = ? WHERE player = ?");
            stm.setString(1, armazemBase64);
            stm.setString(2, player);
            stm.executeUpdate();
            stm.close();
        } catch (Exception e) {
            sc.sendMessage("§c[StoneDuels] erro ao salvar armazens.");
        }
    }

    public static void forceUpdateSQL() {
        Set<String> getAllVitorias = Sets.newConcurrentHashSet();
        getAllVitorias.addAll(DuelCache.getCache().getAllVitorias());

        Set<String> getAllDerrotas = Sets.newConcurrentHashSet();
        getAllDerrotas.addAll(DuelCache.getCache().getAllDerrotas());

        Set<String> getAllKDR = Sets.newConcurrentHashSet();
        getAllKDR.addAll(DuelCache.getCache().getAllKDR());

        Set<String> getAllArmazem = Sets.newConcurrentHashSet();
        getAllArmazem.addAll(DuelCache.getCache().getAllArmazem());

        getAllVitorias.forEach(players -> saveVitoriasToSQL(players, DuelCache.getCache().getVitoriasFrom(players)));
        getAllDerrotas.forEach(players -> saveDerrotasToSQL(players, DuelCache.getCache().getDerrotasFrom(players)));
        getAllKDR.forEach(players -> {
            DuelCache.getCache().updateKDR(players);
            saveKDRtoSQL(players, DuelCache.getCache().getKDRfrom(players));
        });
        getAllArmazem.forEach(players -> saveArmazemToSQL(players, DuelCache.getCache().getArmazemFrom(players)));

        sc.sendMessage("§a[StoneDuels] O banco de dados foi atualizado!");
    }

    public static void updateDataSQL() {
        (new BukkitRunnable() {

            @Override
            public void run() {
                Set<String> getAllVitorias = Sets.newConcurrentHashSet();
                getAllVitorias.addAll(DuelCache.getCache().getAllVitorias());

                Set<String> getAllDerrotas = Sets.newConcurrentHashSet();
                getAllDerrotas.addAll(DuelCache.getCache().getAllDerrotas());

                Set<String> getAllKDR = Sets.newConcurrentHashSet();
                getAllKDR.addAll(DuelCache.getCache().getAllKDR());

                Set<String> getAllArmazem = Sets.newConcurrentHashSet();
                getAllArmazem.addAll(DuelCache.getCache().getAllArmazem());

                getAllVitorias.forEach(players -> saveVitoriasToSQL(players, DuelCache.getCache().getVitoriasFrom(players)));
                getAllDerrotas.forEach(players -> saveDerrotasToSQL(players, DuelCache.getCache().getDerrotasFrom(players)));
                getAllKDR.forEach(players -> {
                    DuelCache.getCache().updateKDR(players);
                    saveKDRtoSQL(players, DuelCache.getCache().getKDRfrom(players));
                });
                getAllArmazem.forEach(players -> saveArmazemToSQL(players, DuelCache.getCache().getArmazemFrom(players)));

                sc.sendMessage("§a[StoneDuels] O banco de dados foi atualizado!");
            }
        }).runTaskTimerAsynchronously(me.zsnow.redestone.Main.getInstance(), 60000L, 60000L); //60000
    }
}
