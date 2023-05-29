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
            int indentOffset = -1;
            for (XWPFParagraph p : paragraphs) {
                BigInteger bigLevel = p.getNumIlvl();
                if (bigLevel == null) {
                    bigLevel = BigInteger.valueOf(-1);
                }
                Integer level = bigLevel.intValue();
                String point = p.getText();

                if (p.getFirstLineIndent() != -1) {
                    indentOffset = p.getFirstLineIndent();
                    level++;
                }
                if (p.getIndentationLeft() != -1) {
                    //could be more than 1 representation to left ?
                    if (indentOffset == -1) {
                        level++;
                    }
                    else {
                        int totalIndents = p.getIndentationLeft() / indentOffset;
                        level += totalIndents;
                    }
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

