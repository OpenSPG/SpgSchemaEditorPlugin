package org.openspg.idea.schema.highlighter.annotator;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;

public interface SchemaHighlightAnnotator {

    boolean test(PsiElement element);

    TextAttributesKey getTextAttributesKey();

}
