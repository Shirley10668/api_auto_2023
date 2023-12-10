package com.lemon.utils;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.lemon.constants.Constants;
import com.lemon.pojo.API;
import com.lemon.pojo.Case;
import com.lemon.pojo.WriteBackData;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

public class ExcelUtils {
	
	public static Logger log = Logger.getLogger(ExcelUtils.class);
	
	//所有的API集合
	public static List<API> apiList;
	//所有的Case集合
	public static List<Case> caseList;
	//excel 回写集合
	public static List<WriteBackData> wbdList = new ArrayList<WriteBackData>();
	
	public static void main(String[] args) throws Exception{
//		read();
//		Object[][] datas = read();
//		for(Object[] objects: datas) {
//			System.out.println(Arrays.toString(objects));
//		}
//		
		
//		FileInputStream fis = new FileInputStream("src/test/resources/cases_v3.xlsx");
		//导入参数设置类
//		ImportParams params = new ImportParams();
//		params.setStartSheetIndex(1);
		//导入验证
//		params.setNeedVerify(true);
//		List<API> importExcel = ExcelImportUtil.importExcel(fis, API.class, params);
//        for(API a: importExcel) {
//        	System.out.println(a);
//        }
		List<API> case1 = read(0,1,API.class);
		List<Case> case2 = read(1,1,Case.class);
	
	    //List<API> List<Case> --> Object[][] = {{api,case},{api,case},{api,case}...}
	}
	
	/**
	 * excel批量回写
	 */
	public static void batchWrite() {
		//poi回写代码
		//1.加载excel
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(Constants.EXCEL_PATH);
			Workbook workbook = WorkbookFactory.create(fis);
			//遍历wbdList集合
			for(WriteBackData wbd: wbdList) {
			   Sheet sheet = workbook.getSheetAt(wbd.getSheetIndex());
			   Row row = sheet.getRow(wbd.getRowNum());
			   Cell cell = row.getCell(wbd.getCellNum(),MissingCellPolicy.CREATE_NULL_AS_BLANK);
			   cell.setCellType(CellType.STRING);
			   cell.setCellValue(wbd.getContent());
			   
			}	
			
			fos = new FileOutputStream(Constants.EXCEL_PATH);
			workbook.write(fos);
			
		} catch (Exception e) {
			log.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(fos);
			close(fis);
		}
		//2. 创建workbook
		//3. 获取对应sheet
		//4. 获取对应row
		//5. 获取对应cell
		//6. setCellValue
		
		
	}
	
	/**
	 * 关流
	 * @param fos
	 */
	public static void close(Closeable fos) {
		try {
			if(fos!= null) {
				fos.close();
			}
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public static Object[][] getAPIAndCaseByApiId(String apiId){
		//需要的一个API
		API wantAPI = null;
		//需要的多个Case集合
		List<Case> wantList = new ArrayList<Case>();
		//遍历集合找到符合的API
		for(API api: apiList) {
			//找到符合要求的API对象(apiId相等)
			if(apiId.equals(api.getId())) {
				wantAPI = api;
				break;
			}
		}
		//遍历集合找到Case
		for(Case c: caseList) {
			//找到符合要求的Case对象(apiId相等)
			if(apiId.equals(c.getApiId())) {
				wantList.add(c);
			}
		}
		//wantList和wantAPI有值了
		//API和Case装到Object[][]
		Object[][] datas = new Object[wantList.size()][2];
		for(int i=0;i<datas.length;i++) {
			datas[i][0] = wantAPI;
			datas[i][1] = wantList.get(i);
		}
		return datas;
	}
	
	
//	public static Object[][] getAPIAndCaseBySheetIndex(int SheetIndex){
//		
	
//	}
	
	
	//poi , easypoi
	/**
	 * 读取excel中的sheet转成对象的list集合
	 * @param <T>  实体类型
	 * @param sheetIndex sheet开始索引
	 * @param sheetNum  读取几个sheet
	 * @param clazz    实体类型的字节码对象
	 * @return     List<实体类型>的集合
	 */
	public static <T> List<T> read(int sheetIndex, int sheetNum, Class<T> clazz) {
		List<T> list = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Constants.EXCEL_PATH);
			//导入参数设置类
			ImportParams params = new ImportParams();
			params.setSheetNum(sheetNum);
			params.setStartSheetIndex(sheetIndex);
			//导入验证
//			params.setNeedVerify(true);
			list = ExcelImportUtil.importExcel(fis, clazz, params);
		} catch (Exception e) {
			log.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error(e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	


	
	public static Object[][] read2(){
		//1. 打开excel 加载文件
		//2.创建workbook
		//3.获取sheet
		//4.获取row
		//.5.获取cell
		Object[][] datas = null;
		FileInputStream fis = null;
		try {
		fis = new FileInputStream("src/test/resources/cases_v1.xls");
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		datas = new Object[lastRowNum][4];
		
		for(int i=1;i<=lastRowNum;i++) {
			//获取row
			Row row = sheet.getRow(i);
			int lastCellNum = row.getLastCellNum();
//			int index = 0;
			for(int j=0, index=0;j<lastCellNum;j++) {
				if(j==2||j==3 || j==5 || j==6) {
					//获取cell
					Cell cell = row.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellType(CellType.STRING);
					//获取单元格的值
					String stringCellValue = cell.getStringCellValue();
					System.out.print(stringCellValue + ",");
					datas[i-1][index++] = stringCellValue;
				}
			}
			System.out.println();
		}
		//关流
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			try {
			  if(fis!=null) {
			    fis.close();
			  }
			}catch (Exception e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		return datas;
	}

}
