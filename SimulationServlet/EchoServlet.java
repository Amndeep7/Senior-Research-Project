import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Simple demonstration for an Applet <-> Servlet communication.
 */
public class EchoServlet extends HttpServlet {
	private static final long serialVersionUID = 6938369357587229915L; //make eclipse be quiet

	/**
	 * Get a String-object from the applet and send it back.
	 */
	public void doPost(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
		try {
			response.setContentType("application/x-java-serialized-object");

			// read a String-object from applet
			// instead of a String-object, you can transmit any object, which
			// is known to the servlet and to the applet
			InputStream in = request.getInputStream();
			ObjectInputStream inputFromApplet = new ObjectInputStream(in);
			String echo = (String) inputFromApplet.readObject();

			// echo it to the applet
			OutputStream outstr = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(outstr);
			oos.writeObject(echo);
			oos.flush();
			oos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
