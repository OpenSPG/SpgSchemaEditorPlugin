package org.openspg.idea.schema.highlighter.annotator;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import org.openspg.idea.schema.highlighter.SchemaHighlightingColors;
import org.openspg.idea.schema.lang.psi.SchemaStructureNameDeclaration;

public class SchemaEntityNameHighlighting implements SchemaHighlightAnnotator {

    @Override
    public boolean test(PsiElement element) {
        return element instanceof SchemaStructureNameDeclaration;
    }

    @Override
    public TextAttributesKey getTextAttributesKey() {
        return SchemaHighlightingColors.ENTITY_NAME;
    }
}
