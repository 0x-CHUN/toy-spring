package spring.service.aop;

import spring.beans.factory.annotation.Autowired;
import spring.dao.AccountDao;
import spring.dao.ItemDao;
import spring.stereotype.Component;
import spring.util.MessageTracker;

@Component(value = "userService")
public class UserService {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private ItemDao itemDao;

    public UserService() {
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public void placeOrder() {
        System.out.println("place order");
        MessageTracker.addMsg("place order");
    }

    public void placeOrderWithException(){
        throw new NullPointerException();
    }
}
