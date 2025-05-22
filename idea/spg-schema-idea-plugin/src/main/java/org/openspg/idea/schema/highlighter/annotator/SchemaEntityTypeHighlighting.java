package org.openspg.idea.schema.highlighter.annotator;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.openspg.idea.schema.highlighter.SchemaHighlightingColors;
import org.openspg.idea.schema.lang.psi.SchemaStructureName;
import org.openspg.idea.schema.lang.psi.SchemaStructureTypeDeclaration;

public class SchemaEntityTypeHighlighting implements SchemaHighlightAnnotator {

    @Override
    public boolean test(PsiElement element) {
        return element instanceof SchemaStructureName
                && PsiTreeUtil.getParentOfType(element, SchemaStructureTypeDeclaration.class) != null;
    }

    @Override
    public TextAttributesKey getTextAttributesKey() {
        return SchemaHighlightingColors.ENTITY_REFERENCE;
    }
}
