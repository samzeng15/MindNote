package filereader;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.javatuples.Pair;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class FileReader {

    public static ArrayList<Pair<Integer, String>> readDocxFile(String fileName) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(fileName)))) {

            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            ArrayList<Pair<Integer, String>> tieredParagraphs = new ArrayList<>();
            for (XWPFParagraph p : paragraphs) {
                //self-explanatory
                System.out.println(p.getText());

                //gets level of indent (bulleted indent) i.e. non bullet = null, first bullet = 0, 2nd = 1, etc
                System.out.println(p.getNumIlvl());

                //useful for maybe start of paragraph indents ? but not really
                System.out.println(p.getFirstLineIndent());
                BigInteger bigLevel = p.getNumIlvl();
                if (bigLevel == null) {
                    bigLevel = BigInteger.valueOf(-1);
                }
                Integer level = bigLevel.intValue();
                String point = p.getText();

                if (p.getFirstLineIndent() != -1) {
                    level++;
                }
                if (p.getText().startsWith("\t")) {
                    int count = 0;
                    for (char c: p.getText().toCharArray()) {
                        if (c == '\t') {
                            count++;
                            level++;
                        }
                        else {
                            break;
                        }
                    }
                    point = p.getText().substring(count);
                }
                //increase the tier to start from 0 instead of -1 (makes more sense)
                Pair<Integer, String> pair = Pair.with(level+1, point);
                tieredParagraphs.add(pair);
            }
            return tieredParagraphs;
        }
    }
}

