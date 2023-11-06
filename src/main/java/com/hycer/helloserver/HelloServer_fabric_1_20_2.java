package com.hycer.helloserver;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;

public class HelloServer_fabric_1_20_2 implements ModInitializer {
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> execute(handler.getPlayer()));
    }
    public void execute(ServerPlayerEntity player) {
        String playerName = String.valueOf(player.getName().getString());
        // 获取服务器的名字
        String serverName = String.valueOf(Objects.requireNonNull(player.getServer()).getServerMotd());
        // 获取游戏tickTime并转换为游戏天数
        int gameDays = (int) (player.getWorld().getTimeOfDay() / 24000);
        // 获取服务器开服时间
        LocalDate serverStartTime = LocalDate.parse(readServerEULA());
        LocalDate currentDate = LocalDate.from(LocalDateTime.now());
        long days = ChronoUnit.DAYS.between(serverStartTime, currentDate) + 1;
        // 给该玩家发送信息
        player.sendMessage(Text.of(String.format("§6欢迎 §b§o%s §6进入 §d§o%s §6服务器\n§6今天是开服的第 §e§o%d §6天,游戏天数第 §e§o%d §6天", playerName, serverName, days, gameDays)), false);
    }

    /**
    获取服务器同意协议的时间当作开服时间
    **/
    public String readServerEULA() {
        String filePath = "eula.txt"; // 替换为你的文本文件路径

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                if (lineNumber == 2) {
                    return convertDateFormat(line.replace("#", ""));
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "2023-11-03";
    }

    /**
     * 将协议中的时间转化为标准格式日期
     * @param date 协议日期
     * @return 标准日期字符串
     */
    public String convertDateFormat(String date){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        LocalDate outputDate = LocalDate.parse(date, inputFormatter);
        return outputDate.toString();
    }
}



