package org.openspg.idea.schema.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.highlighter.annotator.SchemaEntityAliasNameHighlighting;
import org.openspg.idea.schema.highlighter.annotator.SchemaEntityNameHighlighting;
import org.openspg.idea.schema.highlighter.annotator.SchemaEntityTypeHighlighting;
import org.openspg.idea.schema.highlighter.annotator.SchemaHighlightAnnotator;

import java.util.HashSet;
import java.util.Set;

final class SchemaAnnotator implements Annotator {

    private final Set<SchemaHighlightAnnotator> myHighlightAnnotators;

    public SchemaAnnotator() {
        myHighlightAnnotators = new HashSet<>();
        myHighlightAnnotators.add(new SchemaEntityNameHighlighting());
        myHighlightAnnotators.add(new SchemaEntityAliasNameHighlighting());
        myHighlightAnnotators.add(new SchemaEntityTypeHighlighting());
    }

    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        for (SchemaHighlightAnnotator highlightAnnotator : myHighlightAnnotators) {
            if (highlightAnnotator.test(element)) {
                holder.newAnnotation(HighlightSeverity.TEXT_ATTRIBUTES, element.getText())
                        .range(element.getTextRange())
                        .textAttributes(highlightAnnotator.getTextAttributesKey())
                        .create();
            }
        }
    }

}
