package dao;

import service.TxtService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;

public class TxtWatcherThread extends Thread {
    public TxtWatcherThread() {
    }

    public TxtWatcherThread(String name) {
        super(name);
    }

    /**
     * 线程运行方法，此方法局限性较大，只能监听监听打印机这种一行一行输出的文件
     */
    @Override
    public void run() {

//        监听对象
        TxtWatcher txtWatcher = new TxtWatcher();
//        之后下面的方法可以替换成TxtWatcher的watch()方法
        WatchService watcher = txtWatcher.getWatcher();
//        读文件的对象
        RandomAccessFile randomAccessFile = null;
            TxtService txtService;
        txtService = new TxtService();


        try {
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

//                换成发送到服务器的方法
                System.out.println(content);
//                txtService.sendTxtData(content);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
