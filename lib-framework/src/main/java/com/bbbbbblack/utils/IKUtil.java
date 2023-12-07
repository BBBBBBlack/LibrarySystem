package com.bbbbbblack.utils;

import com.bbbbbblack.domain.entity.Search;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class IKUtil {
    public static List<String> getTags(Search search) {
        String keywords = search.getKeywords();
        List<String> tags = new ArrayList<>();
        //若查询作者
        if (search.getType() == 2) {
            tags.add(keywords);
        } else {
            StringReader sr = new StringReader(keywords);
            IKSegmenter ik = new IKSegmenter(sr, true);
            Lexeme lex = null;
            while (true) {
                try {
                    if ((lex = ik.next()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert lex != null;
                tags.add(lex.getLexemeText());
            }
        }

        return tags;
    }
}
