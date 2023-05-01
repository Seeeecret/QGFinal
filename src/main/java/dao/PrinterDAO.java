package dao;

import pojo.po.User;
import utils.CRUDUtil;

public class PrinterDAO {
    public static CRUDUtil.ResultSetWrapper queryAvailablePrinter(User user, int roleId) {
        CRUDUtil.ResultSetWrapper PrinterResultSetWrapper = null;
        if (roleId == 1) {
            PrinterResultSetWrapper = CRUDUtil.executeCommonQuery("SELECT * FROM printer");
        } else if (roleId == 2) {
           PrinterResultSetWrapper = CRUDUtil.executeCommonQuery("SELECT * FROM printer WHERE owner_id = ?", user.getUserId());
        } else {
            Integer parentId = user.getParentId();
            if(parentId!=null){
                PrinterResultSetWrapper = CRUDUtil.executeCommonQuery("SELECT * FROM printer WHERE owner_id = ?", parentId);
            }
        }
        return PrinterResultSetWrapper;
    }
    public static CRUDUtil.ResultSetWrapper queryPrinterName(int printerId) {
        CRUDUtil.ResultSetWrapper PrinterResultSetWrapper = CRUDUtil.executeCommonQuery("SELECT name FROM printer WHERE printer_id = ?", printerId);
        return PrinterResultSetWrapper;
    }
}
