package service;

import dao.TxtWatcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;

public class TxtWatcherThread extends Thread{
    @Override
    public void run() {
        TxtWatcher txtWatcher = new TxtWatcher();
        WatchService watcher = txtWatcher.getWatcher();
        RandomAccessFile randomAccessFile = null;
        try{
            randomAccessFile = new RandomAccessFile(txtWatcher.getTxtFile(), "r");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        long lastPosition = 0;
        while (true) {
            String content = "";
            try {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path fileName = (Path) event.context();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY
                            && fileName.toString().equals(txtWatcher.getTxtFile().getName())) {
                        randomAccessFile.seek(lastPosition);
                        content = randomAccessFile.readLine();
                        lastPosition = randomAccessFile.length();
                    }
                }
                boolean reset = key.reset();
                if (!reset) {
                    System.out.println("WatchKey reset failed.");
                    break;
                }
//                修改此处代码为将content发送给服务端

            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
