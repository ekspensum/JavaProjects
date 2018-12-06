package pl.shopapp.beans;

import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import pl.shopapp.entites.Customer;
import pl.shopapp.entites.Product;
import pl.shopapp.entites.Transaction;
import pl.shopapp.entites.User;


/**
 * Session Bean implementation class TransactionBean
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class TransactionBean implements TransactionBeanRemote, TransactionBeanLocal {

	@PersistenceContext(unitName = "ShopAppEntites")
	private EntityManager em;

	@Resource
	private UserTransaction ut;
	
    /**
     * Default constructor. 
     */
    public TransactionBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public boolean newTransaction(int idUser, List<BasketData> basketList) {
		int newUnitInStock = 0;		
		try {
			ut.begin();
			User u = em.find(User.class, idUser);
			Customer c = (Customer) em.createNamedQuery("customerQuery", Customer.class).setParameter("user", u).getSingleResult();
			Transaction t;
			for(int i=0; i<basketList.size(); i++) {
				t = new Transaction();
				t.setCustomer(c);
				t.setQuantity(basketList.get(i).getQuantity());
				Product p = em.find(Product.class, basketList.get(i).getProductId());
				newUnitInStock = p.getUnitsInStock() - basketList.get(i).getQuantity();
				String queryUpdate = "UPDATE Product SET unitsInStock = "+newUnitInStock+" WHERE id = "+p.getId()+"";
				em.createQuery(queryUpdate).executeUpdate();
				t.setProduct(p);
				t.setDateTime(LocalDateTime.now());
				em.persist(t);		
			}
			basketList.clear();
			ut.commit();
			return true;
		} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
