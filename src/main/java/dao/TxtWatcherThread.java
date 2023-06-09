package dao;

import service.TxtDataHttpService;

import java.io.*;
import java.nio.file.*;

public class TxtWatcherThread extends Thread {
    int printerId;
    public TxtWatcherThread() {
    }
    public TxtWatcherThread(int printerId) {
        this.printerId = printerId;
    }
    public TxtWatcherThread(String name) {
        super(name);
    }

    /**
     * 线程运行方法，此方法局限性较大，只能监听监听打印机这种一行一行输出的文件
     */
    @Override
    public void run() {

        TxtWatcher txtWatcher = new TxtWatcher();
//        之后下面的方法可以替换成TxtWatcher的watch()方法
        WatchService watcher = txtWatcher.getWatcher();
        TxtDataHttpService txtDataHttpService;
        txtDataHttpService = new TxtDataHttpService();
        InputStreamReader isr = null;
        BufferedReader br = null;
//        虽然生成样例的程序显示的都是英文，自己调试时时看出不出来影响。但题目要求是GBK编码，所以这里要用GBK编码
        try {
            isr = new InputStreamReader(new FileInputStream(txtWatcher.getTxtFile()), "GBK");
            br = new BufferedReader(isr);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            String content = "";
            try {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path fileName = (Path) event.context();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY
                            && fileName.toString().equals(txtWatcher.getTxtFile().getName())) {
                        content = br.readLine();
                    }
                }

                boolean reset = key.reset();
                if (!reset) {
                    System.out.println("WatchKey reset failed.");
                    break;
                }

//                换成发送到服务器的方法
//                System.out.println(content);
                txtDataHttpService.sendTxtData(content, printerId);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            br.close();
            isr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
