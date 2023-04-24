package dao;

import service.TxtDataHttpService;

import java.io.*;
import java.nio.file.*;

/**
 * txt监听类
 *
 * @author Secret
 * @date 2023/04/18
 */
public class TxtWatcher {
    protected String txtPath;
    protected File txtFile;
    protected Path dir;
    protected WatchService watcher;

    public String getTxtPath() {
        return txtPath;
    }

    public File getTxtFile() {
        return txtFile;
    }

    public Path getDir() {
        return dir;
    }

    public WatchService getWatcher() {
        return watcher;
    }

    /**
     * 无参构造，默认监听C:\TPM\printer.txt
     */
    public TxtWatcher() {
        txtPath = "C:\\TPM\\printer.txt";
        txtFile = new File(txtPath);
        dir = txtFile.toPath().getParent();
        try {
            watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 带参构造，监听指定文件路径
     *
     * @param filePath 文件路径
     */
    public TxtWatcher(String filePath) {
        this.txtPath = filePath;
        txtFile = new File(filePath);
        dir = txtFile.toPath().getParent();
        try {
            watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 开始监听
     *
     */
    public void watch() {
        WatchService watcher = this.getWatcher();
//        读文件的对象
        TxtDataHttpService txtDataHttpService;
        txtDataHttpService = new TxtDataHttpService();
        InputStreamReader isr = null;
        BufferedReader br = null;
//        注释掉的代码为第一版中使用randomAccessFile记录lastPosition的代码,这里不关闭的话会不会有问题?
        try {
//            randomAccessFile = new RandomAccessFile(txtWatcher.getTxtFile(), "r");
            isr = new InputStreamReader(new FileInputStream(this.getTxtFile()), "GBK");
            br = new BufferedReader(isr);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

//        long lastPosition = 0;
        while (true) {
            String content = "";
            try {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path fileName = (Path) event.context();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY
                            && fileName.toString().equals(this.getTxtFile().getName())) {
//                        randomAccessFile.seek(lastPosition);
                        content = br.readLine();
//                        lastPosition = randomAccessFile.length();
                    }
                }

                boolean reset = key.reset();
                if (!reset) {
                    System.out.println("WatchKey reset failed.");
                    break;
                }

//                换成发送到服务器的方法
                txtDataHttpService.sendTxtData(content);
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
