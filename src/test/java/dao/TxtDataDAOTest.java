package dao;

import org.junit.Test;
import pojo.PrinterTreatedMessage;
import service.TxtDataManageService;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class TxtDataDAOTest {

    @Test
    public void insertTxtData() throws SQLException {
        String original = "1682169303:41:213";
        PrinterTreatedMessage printerTreatedMessage = TxtDataManageService.toPrinterTreatedMessage(original);
        TxtDataDAO.insertTxtData(printerTreatedMessage, original, 1);
    }
}