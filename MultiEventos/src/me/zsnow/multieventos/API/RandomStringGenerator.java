package me.zsnow.multieventos.API;

public class RandomStringGenerator {

	public enum Mode{
		APLHA, ALPHANUMERIC, NUMERIC, SYMBOLIC, ALPHASYMBOLIC, NUMERICSYMBOLIC, APLHANUMERICSYMBOLIC;
	}
	
	public static String getRandomString(int lenght, Mode mode){
		StringBuilder builder = new StringBuilder();
		String s = "";
		switch(mode){
		case APLHA:
			s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			break;
		case ALPHANUMERIC:
			s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			break;
		case NUMERIC:
			s = "0123456789";
			break;
		case SYMBOLIC:
			s = "~,.:?;[]{}´`^!@#$%¨&*()-_+=></ ";
			break;
		case ALPHASYMBOLIC:
			s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~,.:?;[]{}´`^!@#$%¨&*()-_+=></ ";
			break;
		case NUMERICSYMBOLIC:
			s = "0123456789~,.:?;[]{}´`^!@#$%¨&*()-_+=></ ";
			break;
		case APLHANUMERICSYMBOLIC:
			s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~,.:?;[]{}´`^!@#$%¨&*()-_+=></ ";
			break;
		}
		for(int i = 0; i<lenght; i++){
			double index = Math.random() * s.length();
			builder.append(s.charAt((int)index));
		}
		return builder.toString();
	}
	
}
