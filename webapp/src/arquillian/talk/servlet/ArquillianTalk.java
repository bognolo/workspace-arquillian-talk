package arquillian.talk.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import arquillian.talk.ejb.PersonRemote;
import arquillian.talk.jpa.Address;
import arquillian.talk.jpa.Person;

/**
 * Servlet implementation class ArquillianTalk
 */
@WebServlet("/")
public class ArquillianTalk extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(lookup="java:global/persistence-ear/persistence-ejb/PersonBean!arquillian.talk.ejb.PersonRemote")
	PersonRemote personRemote;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArquillianTalk() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Person person=new Person();
		person.setDob(new Date());
		person.setName("person name");
		person.setAddresses(new HashSet<Address>());
		Address address1 = new Address();
		address1.setPerson(person);
		address1.setStreet("Park Lane");
		address1.setCity("London");
		address1.setPostcode(2345);
		person.getAddresses().add(address1);
		Address address2 = new Address();
		address2.setPerson(person);
		address2.setStreet("University Avenue");
		address2.setCity("Cardiff");
		address2.setPostcode(3356);
		person.getAddresses().add(address2);
		personRemote.savePerson(person);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
