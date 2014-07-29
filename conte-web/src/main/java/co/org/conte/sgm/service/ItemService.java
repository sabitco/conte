package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.ItemDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Item;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jam
 */
public class ItemService {
    
    private ItemDao dao;

    public ItemService() {
        dao = new ItemDao();
    }
    
    public List<Item> findAll(){
        try {
            return dao.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(ItemService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
}
