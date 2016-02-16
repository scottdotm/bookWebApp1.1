package edu.wctc.ssm.bookwebapp.model;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Scott
 */
public class AuthorService {
    private AuthorDaoStrategy dao = new AuthorDao();
    
    public List<Author> getAuthorList()
            throws ClassNotFoundException, SQLException{
        return dao.getAuthorList();
    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        AuthorService srv = new AuthorService();
        List<Author> authors = srv.getAuthorList();
        System.out.println(authors);
    }
}
