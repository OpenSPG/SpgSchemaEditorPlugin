package org.openspg.idea.conceptRule.lexer;

import com.intellij.lexer.FlexAdapter;
import org.openspg.idea.lang.lexer.ConceptRuleLexer;

public class ConceptRuleLexerAdapter extends FlexAdapter {

    public ConceptRuleLexerAdapter() {
        super(new ConceptRuleLexer(null));
    }

}
