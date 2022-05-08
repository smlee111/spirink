package egovframework.sprink.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Base64.Decoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.web.multipart.MultipartFile;

import egovframework.sprink.dto.CommonFileVo;

public class CommonUtils {
	//파이썬 실행
	public static void execPython(String[] command) throws IOException, InterruptedException {
        CommandLine commandLine = CommandLine.parse(command[0]);
        for (int i = 1, n = command.length; i < n; i++) {
            commandLine.addArgument(command[i]);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        System.out.println("commandLine"+commandLine.toString());
        int result = executor.execute(commandLine);
        System.out.println("result: " + result);
        System.out.println("output: " + outputStream.toString());

    }
	//파이썬 실행
	public static String execPython(String[] command,boolean ret) throws IOException, InterruptedException {
		String str="";
        CommandLine commandLine = CommandLine.parse(command[0]);
        for (int i = 1, n = command.length; i < n; i++) {
            commandLine.addArgument(command[i]);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        System.out.println("commandLine"+commandLine.toString());
        int result = executor.execute(commandLine);
        //System.out.println("result: " + result);
        //System.out.println("output: " + outputStream.toString());
        str=outputStream.toString();
        return str;
    }
	//파일업로드 실행
	public static CommonFileVo fileUpload(CommonFileVo fileUploadVo, Boolean isLocal) throws Exception{
		
		String filePath = ConfigUtil.SERVER_FILE_UPLOAD_PATH;
		if (isLocal) {
			filePath = ConfigUtil.LOCAL_FILE_UPLOAD_PATH;
		}
		
		MultipartFile file = fileUploadVo.getMpfile();
		String orgFileNm = file.getOriginalFilename();
		fileUploadVo.setOrgFileName(orgFileNm);
		String fileExt = orgFileNm.substring(orgFileNm.lastIndexOf('.')+1);
		String fileName =orgFileNm.substring(0, orgFileNm.lastIndexOf('.'));
		
		String fullFileName =fileName +"_"+ new SimpleDateFormat("yyyyMMddhhmm").format(new Date()) + "." + fileExt;
		
		System.out.println(fullFileName);
		
		try {
			file.transferTo(new File(filePath + File.separator + fullFileName));
			fileUploadVo.setFileName(fullFileName);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return fileUploadVo;
	}
	//파일업로드 실행
	public static CommonFileVo fileUploadType(CommonFileVo fileUploadVo, String filePath ) throws Exception{
		
		MultipartFile file = fileUploadVo.getMpfile();
		String orgFileNm = file.getOriginalFilename();
		fileUploadVo.setOrgFileName(orgFileNm);
		String fileExt = orgFileNm.substring(orgFileNm.lastIndexOf('.')+1);
		String fileName =orgFileNm.substring(0, orgFileNm.lastIndexOf('.'));
		
		String fullFileName =fileName +"_"+ new SimpleDateFormat("yyyyMMddhhmm").format(new Date()) + "." + fileExt;
		
		System.out.println(fullFileName);
		
		try {
			file.transferTo(new File(filePath + File.separator + fullFileName));
			fileUploadVo.setFileName(fullFileName);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return fileUploadVo;
	}
	public static String getString(String str) {
		String reStr="";
		Pattern pattern = Pattern.compile("['](.*?)[']");
		Matcher matcher = pattern.matcher(str);
				
		while (matcher.find()) {  // 일치하는 게 있다면  
			reStr=matcher.group(1);
		    if(matcher.group(1) ==  null)
		    	break;
		}
		return reStr;
	}
	public static String getDecode(String str) {
		String reStr="";
		byte[] targetBytes = getString(str).getBytes();
        Decoder decoder = Base64.getDecoder(); 
        byte[] decodedBytes = decoder.decode(targetBytes);
        reStr= new String(decodedBytes);
		return reStr;
	}
	
	public static void main(String args[]) {
		String str = "b'6rGU656RIOqwmeydgCDrsJgg65CQ7Jy866m0IOyii+qyoOuLpA=='";
		System.out.println(getDecode(str));
	}
}
