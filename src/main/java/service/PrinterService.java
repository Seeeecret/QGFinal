package service;

import dao.PrinterDAO;
import pojo.po.Printer;
import pojo.po.User;
import utils.CRUDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PrinterService {
    /**
     * 获得可用打印机
     *
     * @return
     */
    public static List<Printer> getAvailablePrinter(String username, int roleId) throws SQLException {
        User user = UserService.query(username);
        CRUDUtil.ResultSetWrapper printerResultSetWrapper = PrinterDAO.queryAvailablePrinter(user, roleId);
        ResultSet resultSet = printerResultSetWrapper.getResultSet();
        List<Printer> printerList = new LinkedList<>();
        while (resultSet.next()) {
            Printer printer = new Printer(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4));
            printerList.add(printer);
        }
        printerResultSetWrapper.close();
        return printerList;
    }

    public static String getPrinterName(int printerId) throws SQLException {
        CRUDUtil.ResultSetWrapper printerResultSetWrapper = PrinterDAO.queryPrinterName(printerId);
        ResultSet resultSet = printerResultSetWrapper.getResultSet();
        String printerName = null;
        while (resultSet.next()) {
            printerName = resultSet.getString(1);
        }
        printerResultSetWrapper.close();
        return printerName;
    }
}

