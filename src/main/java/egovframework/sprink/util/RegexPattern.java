package egovframework.sprink.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class RegexPattern {
	public static String TRIM_CHK="^\\s+|\\s+$"; //앞뒤공백 정규식
	public static String DUP_EMP_CHK="\\s{2,}";  //연속공백 정규식
	public static String SPE_CHAR ="^[ㄱ-ㅎ가-힣a-zA-Z0-9|.?!'\"% ]*$"; //특수문자 체크  , . ? ! ' " % 제외
	public static String SPE_REV_CHAR ="[-=+#:^$@*※~&ㆍ』|()<>/…》{\\[\\]]"; //특수문자 체크  , . ? ! ' " % 제외, \\]이렇게 이해
	public static String END_CHAR=".*[.!?]$";
	//앞뒤공백 체크한다 제거는 trim()
	public static boolean isEmpty(String str){
		boolean ep = false;
		Pattern pattern = Pattern.compile(RegexPattern.TRIM_CHK);  
        Matcher match = pattern.matcher(str); 		
		return ep= match.find();
	}
	
	//연속공백 체크한다
	public static boolean isDupEmpty(String str){
		boolean ep = false;
		Pattern pattern = Pattern.compile(RegexPattern.DUP_EMP_CHK);  
        Matcher match = pattern.matcher(str); 		
		return ep= match.find();
	}
	
	//연속공백 제거한다
	public static String rvDupEmpty(String str){
		String reStr=str;
		reStr = str.replaceAll(RegexPattern.DUP_EMP_CHK, " ");
		return reStr;
	}
	//특수문자여부체크한다 공백포함하여 없으면 true
	public static boolean isSpeChar(String str){
		boolean ep = false;
		Pattern pattern = Pattern.compile(RegexPattern.SPE_CHAR);  
        Matcher match = pattern.matcher(str); 		
		return ep= match.find();
	}
	
	public static String rvSpeChar(String str){
		return str.replaceAll(SPE_REV_CHAR, "");
	}
	
	//구두점 체크
	public static boolean isEndChar(String str){
		boolean ep = false;
		Pattern pattern = Pattern.compile(RegexPattern.END_CHAR);  
        Matcher match = pattern.matcher(str); 		
		return ep= match.find();
	}
	//구두점 없으면 마침표
	public static String addEndChar(String str){
		return str+".";
	}
	
	public static void main(String args[]){
        System.out.println(isEndChar("맞춤법 체크.."));
        System.out.println(addEndChar("맞춤법$#%![](&^%$ 체크{ "));
	}
	
}

