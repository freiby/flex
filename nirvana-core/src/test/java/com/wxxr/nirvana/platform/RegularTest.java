package com.wxxr.nirvana.platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.wxxr.nirvana.exception.NirvanaException;

public class RegularTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		URL url = RegularTest.class.getResource("embeddedjs2.js");
		File f1 = new File(url.getFile());
//		File f2 = new File("embeddedjs2.js");
		String srcFileName = f1.getName();
		try {
			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			StringBuilder content = new StringBuilder();
			while (br.ready()) {
				content.append(br.readLine() + "\r\n");
			}
			fr.close();
			String pattern = "#path";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(content);

			if (m.find()) {
				String newContent = m.replaceAll("test");
				File rfbk = new File(f1.getPath() + ".bk");
				if(f1.renameTo(rfbk)){
					FileWriter fw = new FileWriter(f1);
					fw.write(newContent.toString());
					fw.flush();
					fw.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
