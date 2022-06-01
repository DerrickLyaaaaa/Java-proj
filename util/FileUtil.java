package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {

	

	// 追加记录
	public static void saveRecordInFile(String str) {
		File record = new File("log.txt");// 记录结果文件
		try {
			if (!record.exists()) {
//				File dir = new File(record.getParent());
//				dir.mkdirs();
				record.createNewFile();
			}
			FileWriter writer = null;
			try {
				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				writer = new FileWriter(record, true);
				writer.write(str+"\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("记录保存失败");
		}
	}

}
