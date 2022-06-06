package net.momirealms.custominstruments.timer;

import net.momirealms.custominstruments.CustomInstruments;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Timer {

    private final int taskID;

    public static void stopTimer(int ID) {
        Bukkit.getScheduler().cancelTask(ID);
        Bukkit.getServer().getScheduler().cancelTask(ID);
    }

    public Timer() {
        TimerTask timerTask = new TimerTask();
        BukkitTask task = timerTask.runTaskTimerAsynchronously(CustomInstruments.instance, 1,1);
        this.taskID = task.getTaskId();
    }

    public int getTaskID() {
        return this.taskID;
    }
}
