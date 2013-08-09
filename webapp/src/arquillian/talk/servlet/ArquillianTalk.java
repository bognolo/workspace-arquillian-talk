package arquillian.talk.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private static final long serialVersionUID = 3938201633190834833L;

	private static final String USAGE = "Please pass values on the query string. Valid parameters are:"
			+ " <ul><li>id</li><li>name</li><li>dob</li><li>street</li><li>city</li><li>postcode</li><li>deleteID</li></ul>";

	@EJB(lookup = PersonRemote.JNDI_NAME)
	private PersonRemote personRemote;

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Read values for parameters from request
		String id = request.getParameter("id");
		String deleteId = request.getParameter("deleteID");
		String name = request.getParameter("name");
		String dob = request.getParameter("dob");
		String[] street = request.getParameterValues("street");
		String[] postcode = request.getParameterValues("postcode");
		String[] city = request.getParameterValues("city");

		String responseValue;
		int parameterCount = request.getParameterMap().size();

		if (parameterCount == 1) {
			// Must be a query
			if (id != null) {
				// Search by ID
				responseValue = searchById(id);
			} else if (name != null) {
				// Search by name
				responseValue = searchByName(name);
			} else if (deleteId != null) {
				// Delete by ID
				responseValue = deleteById(deleteId);
			} else {
				// Show usage instructions
				responseValue = USAGE;
			}
		} else if (parameterCount > 0) {
			// Insert a new person
			responseValue = addPerson(name, dob, street, postcode, city);
		} else {
			// Show usage instructions
			responseValue = USAGE;
		}

		response.getWriter().print(
				"<html><body>" + responseValue.replaceAll("\n", "<br/>")
						+ "</body></html>");
	}

	/**
	 * @param name
	 * @param dob
	 * @param street
	 * @param postcode
	 * @param city
	 * @return
	 */
	private String addPerson(String name, String dob, String[] street, String[] postcode, String[] city) {
		String responseValue;
		Person person = new Person();
		try {
			person.setDob(SimpleDateFormat.getDateInstance(
					SimpleDateFormat.MEDIUM).parse(dob));
		} catch (ParseException e) {
			person.setDob(new Date());
		}
		person.setName(name);
		person.setAddresses(new HashSet<Address>());
		int maxLength = Math.max(street.length,
				Math.max(city.length, postcode.length));

		for (int i = 0; i < maxLength; i++) {
			Address address1 = new Address();
			address1.setPerson(person);
			if (i < street.length)
				address1.setStreet(street[i]);
			if (i < city.length)
				address1.setCity(city[i]);
			if (i < postcode.length)
				address1.setPostcode(Integer.parseInt(postcode[i]));
			person.getAddresses().add(address1);
		}
		personRemote.savePerson(person);

		responseValue = "Saved person:<br/>" + person.toString();
		return responseValue;
	}

	/**
	 * @param deleteId
	 * @return
	 */
	private String deleteById(String deleteId) {
		String responseValue;
		Person person = personRemote
				.getPerson(Long.parseLong(deleteId));
		personRemote.deletePerson(Long.parseLong(deleteId));
		responseValue = "Deleted person:<br/>" + person.toString();
		return responseValue;
	}

	/**
	 * @param name
	 * @return
	 */
	private String searchByName(String name) {
		String responseValue;
		Person person = personRemote.getPerson(name);
		if (person != null) {
		responseValue = person.toString();
		} else {
			responseValue = "Person with name=\"" +name+ "\" was not found.";
		}
		return responseValue;
	}

	/**
	 * @param id
	 * @return
	 */
	private String searchById(String id) {
		String responseValue;
		Person person = personRemote.getPerson(Long.parseLong(id));
		if (person != null) {
		responseValue = person.toString();
		} else {
			responseValue = "Person with ID=\"" +id+ "\" was not found.";
		}
		return responseValue;
	}
}
