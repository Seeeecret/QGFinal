package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
     * @throws FileNotFoundException 文件未发现异常
     */
    public void watch() throws FileNotFoundException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(txtFile, "r");
        long lastPosition = 0;
        while (true) {
            String content = "";
            try {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path fileName = (Path) event.context();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY
                            && fileName.toString().equals(txtFile.getName())) {
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
                System.out.println(content);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
