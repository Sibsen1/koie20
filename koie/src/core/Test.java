package core;

public class Test {
	
	public String method(Object... variables) {
		return String.format("%s %s", variables);
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		System.out.println(test.method("hei", "og haa"));
	}

}
