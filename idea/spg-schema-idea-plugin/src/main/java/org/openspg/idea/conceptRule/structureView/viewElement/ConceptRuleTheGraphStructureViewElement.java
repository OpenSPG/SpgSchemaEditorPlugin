package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.lang.psi.ConceptRuleTheGraphStructure;
import org.openspg.idea.schema.SchemaIcons;


public class ConceptRuleTheGraphStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleTheGraphStructure> {

    public ConceptRuleTheGraphStructureViewElement(ConceptRuleTheGraphStructure element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getGraphStructureHead().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleTheGraphStructure element) {
        return new PresentationData(
                element.getGraphStructureHead().getText(),
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
