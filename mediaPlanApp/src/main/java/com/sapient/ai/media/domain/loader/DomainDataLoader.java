package com.sapient.ai.media.domain.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

@Component
public class DomainDataLoader {

	private Map<String,List<Object>> domainCache = new HashMap<>();

	public Map<String, List<Object>> getDomainCache() {
		return domainCache;
	}

	@PostConstruct
	public void startup() throws Exception {
		domainCache = loadDomainData();
	}

	private Map<String, List<Object>> loadDomainData() throws Exception {

		Workbook workbook = WorkbookFactory.create(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("domains.xlsx"));
		Map<String, List<Object>> domainValues = new HashMap<>();

		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			Sheet sheet = workbook.getSheetAt(i);
			String sheetName = workbook.getSheetName(i);
			List<Object> values = new ArrayList<>();
			for (int r = 0; r < sheet.getLastRowNum(); r++)
			{
				Row row = sheet.getRow(r);
				Iterator<Cell> iterator = row.cellIterator();
				while (iterator.hasNext())
				{
					Object value = null;
					Cell cell = iterator.next();
					if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
						value = cell.getStringCellValue();
					}else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						value = cell.getNumericCellValue();
					}
					values.add(value);
				}
			}
			domainValues.put(sheetName, values);
		}
		return domainValues;

	}

}

