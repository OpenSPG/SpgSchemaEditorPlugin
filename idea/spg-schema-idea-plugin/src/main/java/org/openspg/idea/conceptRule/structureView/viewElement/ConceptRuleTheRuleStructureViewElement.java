package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.lang.psi.ConceptRuleTheRule;
import org.openspg.idea.schema.SchemaIcons;


public class ConceptRuleTheRuleStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleTheRule> {

    public ConceptRuleTheRuleStructureViewElement(ConceptRuleTheRule element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getRuleHead().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleTheRule element) {
        return new PresentationData(
                element.getRuleHead().getText(),
                null,
                SchemaIcons.Nodes.Entity,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        return TreeElement.EMPTY_ARRAY;
    }

}
