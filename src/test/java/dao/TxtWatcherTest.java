package dao;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class TxtWatcherTest {

    @Test
    public void watch() throws FileNotFoundException {
        TxtWatcher txtWatcher = new TxtWatcher();
        txtWatcher.watch();
    }
}