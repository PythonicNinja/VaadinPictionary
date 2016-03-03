package pl.Wojtek.util;


import pl.Wojtek.dao.UserDao;
import pl.Wojtek.model.User;

import com.vaadin.server.VaadinSession;


public class LoginService {

    public static boolean isLoggedIn(){
        try{
            VaadinSession.getCurrent().getLockInstance().lock();
            if(VaadinSession.getCurrent().getSession().getAttribute("user")==null){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            VaadinSession.getCurrent().getLockInstance().unlock();
        }
        return false;
    }

    public static boolean validCredentials(String username, String password){
        UserDao userDAO = new UserDao();
        User u = userDAO.getUser(username);
        if(u!=null && u.getPassword().equals(password)){
            return true;
        }
        return false;
    }
}