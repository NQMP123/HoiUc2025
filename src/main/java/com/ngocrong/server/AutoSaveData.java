package com.ngocrong.server;

import org.apache.log4j.Logger;

public class AutoSaveData implements Runnable {

    private static final Logger logger = Logger.getLogger(AutoSaveData.class);

    @Override
    public void run() {
        Server server = DragonBall.getInstance().getServer();
        Config config = server.getConfig();
        while (server.start) {
            try {
                Thread.sleep(config.getDelayAutoSave());
                server.saveData();
            } catch (InterruptedException ex) {
                com.ngocrong.NQMP.UtilsNQMP.logError(ex);
                logger.error("failed!", ex);
            }
        }
    }
}
