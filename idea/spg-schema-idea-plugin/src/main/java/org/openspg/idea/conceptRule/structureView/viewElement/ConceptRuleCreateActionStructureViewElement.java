package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.lang.psi.ConceptRuleCreateAction;
import org.openspg.idea.schema.SchemaIcons;


public class ConceptRuleCreateActionStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleCreateAction> {

    public ConceptRuleCreateActionStructureViewElement(ConceptRuleCreateAction element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getCreateActionSymbol().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleCreateAction element) {
        return new PresentationData(
                element.getCreateActionSymbol().getText(),
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
