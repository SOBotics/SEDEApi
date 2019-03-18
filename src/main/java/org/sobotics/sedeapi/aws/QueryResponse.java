package org.sobotics.sedeapi.aws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * The query response, convert from cvs structure to Object --> json
 * @author Petter Friberg
 *
 */
public class QueryResponse {

	private List<Map<String, Object>> result;

	public QueryResponse() {
		super();
	}

	public QueryResponse(String cvs) throws IOException {
		super();
		build(cvs);
	}

	public void build(String cvs) throws IOException {
		this.result = new ArrayList<>();
		try (CSVParser parser = CSVParser.parse(cvs, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {		
			Map<Integer, String> header = parser.getHeaderMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
			for (CSVRecord record : parser) {
				Map<String, Object> map = new LinkedHashMap<>();
				int cols = record.size();
				for (int i = 0; i < cols; i++) {
					map.put(header.get(i), record.get(i));
				}
				this.result.add(map);
			}
		}
	}

	public List<Map<String, Object>> getResult() {
		if (result == null) {
			result = new ArrayList<>();
		}
		return result;
	}

	public void setResult(List<Map<String, Object>> result) {
		this.result = result;
	}

}
