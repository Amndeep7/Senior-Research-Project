package experimentation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class JustAClass
{
	static Scanner scanner;
	public JustAClass() throws IOException
	{
		scanner = new Scanner(new InputStreamReader(getClass().getResource("/experimentation/resources/text/text.txt").openStream()));
	}

	public String readAll()
	{
		String s = "";
		while(scanner.hasNext())
			s += scanner.next() + " ";
		return s;
	}

	public static void main(String[] args) throws IOException
	{
		JustAClass c = new JustAClass();
		System.out.println(c.readAll());
	}
}
