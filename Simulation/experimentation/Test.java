package experimentation;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class Test
{

	public static void close(InputStream s) throws IOException
	{
		s.close();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		// TODO Auto-generated method stub

		InputStream s = new FileInputStream("refresh");

		ArrayList<Byte> file = new ArrayList<Byte>();

		while(s.available() > 5)
		{
			file.add((byte) s.read());
		}

		close(s);

		while(s.available() > 0)
		{
			file.add((byte) s.read());
		}

		Byte[] array = file.toArray(new Byte[1]);
		byte[] array2 = new byte[array.length];
		for(int x = 0; x < array.length; x++)
			array2[x] = array[x];

		String str = new String(array2);

		System.out.println(str);
	}

}
