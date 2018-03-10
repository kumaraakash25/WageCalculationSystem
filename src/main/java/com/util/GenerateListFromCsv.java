package com.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate the list of POJO from the CSV file. Each POJO representing a cvs record.
 */
public class GenerateListFromCsv {

    private final static Log LOG = LogFactory.getLog(GenerateListFromCsv.class);

    public static <T extends Object> List generateListFromCsv(final Class<T> clazz, final String csvFile) {
        File file = new File(GenerateListFromCsv.class.getResource(csvFile).getFile());
        List<T> list = new ArrayList<>();
        try {
            Reader reader = new FileReader(file);
            CsvToBean csv = new CsvToBeanBuilder(reader).withType(clazz).build();
            list = csv.parse();

        } catch (IOException e) {
            LOG.error("Error occurred while parsing the CSV ",e);
        }
        LOG.info(list.size()+" Records read from the file "+csvFile);
        return list;
    }

}
