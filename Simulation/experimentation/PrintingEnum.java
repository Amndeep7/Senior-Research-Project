package experimentation;

public class PrintingEnum
{
	public static enum COLORS
	{
		RED, YELLOW, GREEN, BLUE;
	}

	public static void main(String[] args)
	{
		System.out.println(COLORS.RED);
		Object o = COLORS.YELLOW;
		System.out.println(o.toString());
	}

}
