package dao;

import model.CustomerSupport;
import exception.DataAccessException;
import java.util.List;

public interface CustomerSupportDAO {
    int createTicket(CustomerSupport support) throws DataAccessException;
    List<CustomerSupport> getTicketsByUser(int userId) throws DataAccessException;
    boolean updateTicketStatus(int supportId, String status) throws DataAccessException;
}
