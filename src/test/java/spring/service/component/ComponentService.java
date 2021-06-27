package spring.service.component;

import spring.beans.factory.annotation.Autowired;
import spring.stereotype.Component;

@Component(value = "componentService")
public class ComponentService {

    @Autowired
    private UserService userService;

    @Autowired
    private DataService dataService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public DataService getDataService() {
        return dataService;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}
