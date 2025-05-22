package org.openspg.idea.schema.highlighter.annotator;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import org.openspg.idea.schema.highlighter.SchemaHighlightingColors;
import org.openspg.idea.schema.lang.psi.SchemaStructureAliasDeclaration;

public class SchemaEntityAliasNameHighlighting implements SchemaHighlightAnnotator {

    @Override
    public boolean test(PsiElement element) {
        return element instanceof SchemaStructureAliasDeclaration;
    }

    @Override
    public TextAttributesKey getTextAttributesKey() {
        return SchemaHighlightingColors.ENTITY_ALIAS;
    }
}
