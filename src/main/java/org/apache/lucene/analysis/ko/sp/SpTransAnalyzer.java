package org.apache.lucene.analysis.ko.sp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.ko.utils.EomiUtil;
import org.apache.lucene.analysis.ko.utils.MorphUtil;
import org.apache.lucene.analysis.ko.utils.SpConstants;
/*
 * 문체변환을 실행한다.
 */
public class SpTransAnalyzer {

	//명사형어미 음을 붙일때 방법
	public String passUmEmiTran(HashMap<String, String> map,String pemi,String type,boolean inGative) {
		String anw="";
		String str=map.get("anycont");
		String anynum = map.get("anynum");
	    System.out.println("passEmiTran anytype==>"+map.get("anytype"));
		if(anynum.equals("11")||anynum.equals("12")||anynum.equals("3")||anynum.equals("4")) {
			List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
			
			StringTokenizer stk = new StringTokenizer(str,",");
			int cnt = stk.countTokens();
			for(int i=0;i<cnt;i++) {
				HashMap<String, String> hmap = new HashMap<String, String>();
				String strToken = stk.nextToken();
				String sourceChar = strToken.substring(0,strToken.indexOf("("));
				String stype = strToken.substring(strToken.indexOf("(")+1,strToken.length()-1);
				System.out.println("sourceChar"+sourceChar+"stype"+stype);
				hmap.put("sourceChar", sourceChar);
				hmap.put("stype", stype);
				analyList.add(hmap);
			}
			
			if(chkVe(analyList)){
				
				String gChk = analyList.get(0).get("sourceChar");
				String mChk = analyList.get(1).get("sourceChar");
				System.out.println("gChk+mChk"+gChk+mChk);
				//if(EomiUtil.endsWithUEEomi(gChk+mChk)){//ㅡ+어,다 인경우 자음+ㅡㅁ으로 변경한다.
					System.out.println("쁘어->쁨으로 바꿔라");
					anw =  EomiUtil.tranUEEomi(gChk+mChk);
				/*}else{
					String eChk = analyList.get(1).get("sourceChar");
					System.out.println("pemi"+pemi+"eChk"+eChk);
					String epemi = pemi.substring(0,pemi.indexOf(eChk));
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
				}*/
			}else if(chkVfe(analyList)){//stype=V, sourceChar=예쁘}, {stype=f, sourceChar=었}, {stype=e, sourceChar=다}
				String gChk = analyList.get(0).get("sourceChar");
				String mChk = analyList.get(1).get("sourceChar");
				String eChk = analyList.get(2).get("sourceChar");
				anw =  EomiUtil.tranUEPassEomi(gChk+mChk+eChk);
				
				//pemi에서 e를 떼어버리고 ~를 붙인다.
				/*String eChk = analyList.get(2).get("sourceChar");
				System.out.println(eChk+"eChk"+analyList.toString());
				String epemi = pemi.substring(0,pemi.indexOf(eChk));
				System.out.println("epemi"+epemi);
				anw = epemi+type;//이것을 만들어야 한다.
*/			}else if(chkVnj(analyList)){
				//pemi에서 nj를 떼어버리고 ~를를 붙인다.
				try{
				//n이 자음이면 기본형과 같다. 아니면 nj를 떼어버리고 다를 붙인다.
				String eChk = analyList.get(1).get("sourceChar")+analyList.get(2).get("sourceChar");
				System.out.println("pemi"+pemi+"Vnj"+eChk+"eChk"+analyList.toString());
				String epemi = pemi.substring(0,pemi.indexOf(eChk));//무조건 떼버리면 오류 발생한다.
				System.out.println("epemi"+epemi);
				anw = epemi+type;//이것을 만들어야 한다.
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(chkNtfe(analyList)){
				//pemi에서 e를 떼어버리고 ~를 붙인다.
				String eChk = analyList.get(3).get("sourceChar");
				System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
				String epemi = pemi.substring(0,analyList.size());
				System.out.println("epemi"+epemi);
				anw = epemi+type;//이것을 만들어야 한다.
			}else if(chkNtfnj(analyList)){
				//pemi에서 e를 떼어버리고 ~를 붙인다.
				String eChk = analyList.get(3).get("sourceChar")+analyList.get(4).get("sourceChar");
				System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
				String epemi = pemi.substring(0,pemi.indexOf(eChk));
				System.out.println("epemi"+epemi);
				anw = epemi+type;//이것을 만들어야 한다.
			}else{ //기존 기본형과 같다.
				for(int i=0;i<analyList.size();i++) {
					HashMap<String, String> hmap = analyList.get(i);
					anw = anw+hmap.get("sourceChar");
					if(hmap.get("stype").equals("V")) {
						anw = anw+type;
						break;
					}else if(hmap.get("stype").equals("t")) {
						anw = anw+type;
						break;
					}
				}
			}
			
		}
		return anw;
	}
	
	//해체 예쁘어 -> 예뻐
	public String passHaeEmiTran(HashMap<String, String> map,String pemi,String type,boolean inGative) {
		String anw="";
		String str=map.get("anycont");
		String anynum = map.get("anynum");
	    System.out.println("passEmiTran anytype==>"+map.get("anytype"));
		if(anynum.equals("11")||anynum.equals("12")||anynum.equals("3")||anynum.equals("4")) {
			List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
			
			StringTokenizer stk = new StringTokenizer(str,",");
			int cnt = stk.countTokens();
			for(int i=0;i<cnt;i++) {
				HashMap<String, String> hmap = new HashMap<String, String>();
				String strToken = stk.nextToken();
				String sourceChar = strToken.substring(0,strToken.indexOf("("));
				String stype = strToken.substring(strToken.indexOf("(")+1,strToken.length()-1);
				System.out.println("sourceChar"+sourceChar+"stype"+stype);
				hmap.put("sourceChar", sourceChar);
				hmap.put("stype", stype);
				analyList.add(hmap);
			}
			
			if(chkVe(analyList)){
				
				String gChk = analyList.get(0).get("sourceChar");
				String mChk = analyList.get(1).get("sourceChar");
				System.out.println(gChk+mChk);
				//if(EomiUtil.endsWithUEEomi(gChk+mChk)){//ㅡ+어인경우 자음+ㅡㅁ으로 변경한다.
					System.out.println("쁘어->뻐로 바꿔라");
					anw =  EomiUtil.tranVeHEEomi(gChk,mChk);
				/*}else{
					String eChk = analyList.get(1).get("sourceChar");
					System.out.println("pemi"+pemi+"eChk"+eChk);
					String epemi = pemi.substring(0,pemi.indexOf(eChk));
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
				}*/
			}else if(chkVfe(analyList)){
				String gChk = analyList.get(0).get("sourceChar");
				String mChk = analyList.get(1).get("sourceChar");
				String hChk = analyList.get(2).get("sourceChar");
				System.out.println(gChk+mChk+hChk);
				//pemi에서 e를 떼어버리고 ~를 붙인다.
				/*String eChk = analyList.get(2).get("sourceChar");
				System.out.println(eChk+"eChk"+analyList.toString());
				String epemi = pemi.substring(0,pemi.indexOf(eChk));
				System.out.println("epemi"+epemi);
				anw = epemi+type;//이것을 만들어야 한다.*/
				String epemi = pemi.substring(0,2);
				anw =  EomiUtil.tranVfeHeEomi(gChk+mChk+hChk);
				//anw = epemi+type;
			}else if(chkVnj(analyList)){
				//pemi에서 nj를 떼어버리고 ~를를 붙인다.
				try{
				//n이 자음이면 기본형과 같다. 아니면 nj를 떼어버리고 다를 붙인다.
				String eChk = analyList.get(1).get("sourceChar")+analyList.get(2).get("sourceChar");
				System.out.println("pemi"+pemi+"Vnj"+eChk+"eChk"+analyList.toString());
				String epemi = pemi.substring(0,pemi.indexOf(eChk));//무조건 떼버리면 오류 발생한다.
				System.out.println("epemi"+epemi);
				anw = epemi+type;//이것을 만들어야 한다.
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(chkNtfe(analyList)){
				//pemi에서 e를 떼어버리고 ~를 붙인다.
				String eChk = analyList.get(3).get("sourceChar");
				System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
				String epemi = pemi.substring(0,analyList.size());
				System.out.println("epemi"+epemi);
				anw = epemi+type;//이것을 만들어야 한다.
			}else if(chkNtfnj(analyList)){
				//pemi에서 e를 떼어버리고 ~를 붙인다.
				String eChk = analyList.get(3).get("sourceChar")+analyList.get(4).get("sourceChar");
				System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
				String epemi = pemi.substring(0,pemi.indexOf(eChk));
				System.out.println("epemi"+epemi);
				anw = epemi+type;//이것을 만들어야 한다.
			}else{ //기존 기본형과 같다.
				for(int i=0;i<analyList.size();i++) {
					HashMap<String, String> hmap = analyList.get(i);
					anw = anw+hmap.get("sourceChar");
					if(hmap.get("stype").equals("V")) {
						anw = anw+type;
						break;
					}else if(hmap.get("stype").equals("t")) {
						anw = anw+type;
						break;
					}
				}
			}
			
		}
		return anw;
	}
	
	//해체 예쁘어 -> 예뻐+요
		public String passHaeYoEmiTran(HashMap<String, String> map,String pemi,String type,boolean inGative) {
			String anw="";
			String str=map.get("anycont");
			String anynum = map.get("anynum");
		    System.out.println("passEmiTran anytype==>"+map.get("anytype"));
			if(anynum.equals("11")||anynum.equals("12")||anynum.equals("3")||anynum.equals("4")) {
				List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
				
				StringTokenizer stk = new StringTokenizer(str,",");
				int cnt = stk.countTokens();
				for(int i=0;i<cnt;i++) {
					HashMap<String, String> hmap = new HashMap<String, String>();
					String strToken = stk.nextToken();
					String sourceChar = strToken.substring(0,strToken.indexOf("("));
					String stype = strToken.substring(strToken.indexOf("(")+1,strToken.length()-1);
					System.out.println("sourceChar"+sourceChar+"stype"+stype);
					hmap.put("sourceChar", sourceChar);
					hmap.put("stype", stype);
					analyList.add(hmap);
				}
				
				if(chkVe(analyList)){
					
					String gChk = analyList.get(0).get("sourceChar");
					String mChk = analyList.get(1).get("sourceChar");
					System.out.println(gChk+mChk);
					//if(EomiUtil.endsWithUEEomi(gChk+mChk)){//ㅡ+어인경우 자음+ㅡㅁ으로 변경한다.
						System.out.println("쁘어->뻐로 바꿔라");
						anw =  EomiUtil.tranVeHEEomi(gChk,mChk)+type;
					/*}else{
						String eChk = analyList.get(1).get("sourceChar");
						System.out.println("pemi"+pemi+"eChk"+eChk);
						String epemi = pemi.substring(0,pemi.indexOf(eChk));
						System.out.println("epemi"+epemi);
						anw = epemi+type;//이것을 만들어야 한다.
					}*/
				}else if(chkVfe(analyList)){
					//pemi에서 e를 떼어버리고 ~를 붙인다.
					//String eChk = analyList.get(2).get("sourceChar");
					//System.out.println(eChk+"eChk"+analyList.toString());
					String v = analyList.get(0).get("sourceChar");
					String f = analyList.get(1).get("sourceChar");
					String e = analyList.get(2).get("sourceChar");
					String epemi = pemi.substring(0,2);
					System.out.println("epemi===>"+epemi);
					//anw = epemi+SpConstants.HAE_YOC;//이것을 만들어야 한다.
					anw =  EomiUtil.tranVfeHeYEomi(v,f,e,inGative);
				}else if(chkVnj(analyList)){
					//pemi에서 nj를 떼어버리고 ~를를 붙인다.
					try{
					//n이 자음이면 기본형과 같다. 아니면 nj를 떼어버리고 다를 붙인다.
					String eChk = analyList.get(1).get("sourceChar")+analyList.get(2).get("sourceChar");
					System.out.println("pemi"+pemi+"Vnj"+eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,pemi.indexOf(eChk));//무조건 떼버리면 오류 발생한다.
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if(chkNtfe(analyList)){
					String nChk = analyList.get(0).get("sourceChar");
					String tChk = analyList.get(1).get("sourceChar");
					String fChk = analyList.get(2).get("sourceChar");
					String eChk = analyList.get(3).get("sourceChar");
					//pemi에서 e를 떼어버리고 ~를 붙인다.
					/*String eChk = analyList.get(3).get("sourceChar");
					System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,analyList.size());
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.*/
					anw =  EomiUtil.tranNtfeHeYEomi(nChk,tChk,fChk,eChk);
				}else if(chkNtfnj(analyList)){
					//pemi에서 e를 떼어버리고 ~를 붙인다.
					String eChk = analyList.get(3).get("sourceChar")+analyList.get(4).get("sourceChar");
					System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,pemi.indexOf(eChk));
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
				}else{ //기존 기본형과 같다.
					for(int i=0;i<analyList.size();i++) {
						HashMap<String, String> hmap = analyList.get(i);
						anw = anw+hmap.get("sourceChar");
						if(hmap.get("stype").equals("V")) {
							anw = anw+type;
							break;
						}else if(hmap.get("stype").equals("t")) {
							anw = anw+type;
							break;
						}
					}
				}
				
			}
			return anw;
		}
		//해체 예쁘어 -> 예쁩니다
		public String passHapSyoEmiTran(HashMap<String, String> map,String pemi,String type,boolean inGative) {
			String chk_type = SpConstants.HAP_SYO;
			if(inGative){
				chk_type = SpConstants.HAP_SYO_IN;
			}
			String anw="";
			String str=map.get("anycont");
			String anynum = map.get("anynum");
		    System.out.println("passEmiTran anytype==>"+map.get("anytype"));
			if(anynum.equals("11")||anynum.equals("12")||anynum.equals("3")||anynum.equals("4")) {
				List<HashMap<String, String>> analyList = new ArrayList<HashMap<String,String>>();
				
				StringTokenizer stk = new StringTokenizer(str,",");
				int cnt = stk.countTokens();
				for(int i=0;i<cnt;i++) {
					HashMap<String, String> hmap = new HashMap<String, String>();
					String strToken = stk.nextToken();
					String sourceChar = strToken.substring(0,strToken.indexOf("("));
					String stype = strToken.substring(strToken.indexOf("(")+1,strToken.length()-1);
					System.out.println("sourceChar"+sourceChar+"stype"+stype);
					hmap.put("sourceChar", sourceChar);
					hmap.put("stype", stype);
					analyList.add(hmap);
				}
				
				if(chkVe(analyList)){
					
					String gChk = analyList.get(0).get("sourceChar");
					String mChk = analyList.get(1).get("sourceChar");
					System.out.println(gChk+mChk);
					
					//if(EomiUtil.endsWithUEEomi(gChk+mChk)){//ㅡ+어인경우 자음+ㅡㅁ으로 변경한다.
						System.out.println("쁘어->쁩니다로 바꿔라");
						//chk_type = SpConstants.HAP_SY;
						anw =  EomiUtil.tranHAPEomi(gChk+mChk,inGative);
					/*}else{
						String eChk = analyList.get(1).get("sourceChar");
						System.out.println("pemi"+pemi+"eChk"+eChk);
						String epemi = pemi.substring(0,pemi.indexOf(eChk));
						System.out.println("epemi"+epemi);
						anw = epemi+type;//이것을 만들어야 한다.
					}*/
				}else if(chkVfe(analyList)){
					String v = analyList.get(0).get("sourceChar");
					String f = analyList.get(1).get("sourceChar");
					String e = analyList.get(2).get("sourceChar");
					//anw =  EomiUtil.tranVfeHapEomi(gChk+mChk+hChk,inGative);
					anw =  EomiUtil.tranVfeHapEomi( v, f, e,  inGative);
				}else if(chkVnj(analyList)){
					//pemi에서 nj를 떼어버리고 ~를를 붙인다.
					try{
					//n이 자음이면 기본형과 같다. 아니면 nj를 떼어버리고 다를 붙인다.
					String eChk = analyList.get(1).get("sourceChar")+analyList.get(2).get("sourceChar");
					System.out.println("pemi"+pemi+"Vnj"+eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,pemi.indexOf(eChk));//무조건 떼버리면 오류 발생한다.
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if(chkNtfe(analyList)){
					String n = analyList.get(0).get("sourceChar");
					String t = analyList.get(1).get("sourceChar");
					String f = analyList.get(2).get("sourceChar");
					String e = analyList.get(2).get("sourceChar");
					//pemi에서 e를 떼어버리고 ~를 붙인다.
					/*String eChk = analyList.get(3).get("sourceChar");
					System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,analyList.size());
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.*/
					anw =  EomiUtil.tranNtfeHapEomi(n,t,f,e,inGative);
				}else if(chkNtfnj(analyList)){
					//pemi에서 e를 떼어버리고 ~를 붙인다.
					String eChk = analyList.get(3).get("sourceChar")+analyList.get(4).get("sourceChar");
					System.out.println("Vnj"+eChk+"eChk"+analyList.toString());
					String epemi = pemi.substring(0,pemi.indexOf(eChk));
					System.out.println("epemi"+epemi);
					anw = epemi+type;//이것을 만들어야 한다.
				}else{ //기존 기본형과 같다.
					for(int i=0;i<analyList.size();i++) {
						HashMap<String, String> hmap = analyList.get(i);
						anw = anw+hmap.get("sourceChar");
						if(hmap.get("stype").equals("V")) {
							anw = anw+type;
							break;
						}else if(hmap.get("stype").equals("t")) {
							anw = anw+type;
							break;
						}
					}
				}
				
			}
			return anw;
		}	
	//형태소가 2개이면서 Ve인지를 확인한다.
		public boolean chkVe(List<HashMap<String, String>> analyList){
			boolean chk = true;
			String[] ve = {"V","e"};
			if(analyList.size()==2&& ve.length==2){
				for(int i=0;i<ve.length;i++){
					if(!analyList.get(i).get("stype").equals(ve[i])){
						chk=false;
					}
				}
			}else{
				chk=false;
			}
			return chk;
		}
		//형태소가 3개이면서 Vfe인지를 확인한다.
		public boolean chkVfe(List<HashMap<String, String>> analyList){
			boolean chk = true;
			String[] vfe = {"V","f","e"};
			if(analyList.size()==3&& vfe.length==3){
				for(int i=0;i<vfe.length;i++){
					if(!analyList.get(i).get("stype").equals(vfe[i])){
						chk=false;
					}
				}
			}else{
				chk=false;
			}
			return chk;
		}
		//형태소가 3개이면서 Vnj인지를 확인한다.
		public boolean chkVnj(List<HashMap<String, String>> analyList){
			boolean chk = true;
			String[] vfe = {"V","n","j"};
			if(analyList.size()==3&& vfe.length==3){
				for(int i=0;i<vfe.length;i++){
					if(!analyList.get(i).get("stype").equals(vfe[i])){
						chk=false;
					}
					if(chkJaType(analyList.get(i).get("sourceChar"))){
						chk=false;
					}
				}
			}else{
				chk=false;
			}
			return chk;
		}
		//형태소가 3개이면서 Ntfe인지를 확인한다.
		public boolean chkNtfe(List<HashMap<String, String>> analyList){
			boolean chk = true;
			String[] vfe = {"N","t","f","e"};
			if(analyList.size()==4&& vfe.length==4){
				for(int i=0;i<vfe.length;i++){
					if(!analyList.get(i).get("stype").equals(vfe[i])){
						chk=false;
					}
				}
			}else{
				chk=false;
			}
			return chk;
		}
		//형태소가 5개이면서 Ntfnj인지를 확인한다.
		public boolean chkNtfnj(List<HashMap<String, String>> analyList){
			boolean chk = true;
			String[] vfe = {"N","t","f","n","j"};
			if(analyList.size()==5&& vfe.length==5){
				for(int i=0;i<vfe.length;i++){
					if(!analyList.get(i).get("stype").equals(vfe[i])){
						chk=false;
					}
				}
			}else{
				chk=false;
			}
			
			return chk;
		}
		//자음이 섞여 있는지 체크
		public boolean chkJaType(String str){
			boolean chk = false;
			String[] vfe = {"ㄱ","ㄴ","ㄷ","ㄹ","ㅁ","ㅂ","ㅅ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
			
			for(int i=0;i<vfe.length;i++){
				if(str.equals(vfe[i])){
					chk=true;
				}
			}	
			return chk;
		}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
